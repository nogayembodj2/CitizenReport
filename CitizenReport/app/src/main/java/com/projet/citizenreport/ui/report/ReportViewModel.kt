package com.projet.citizenreport.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projet.citizenreport.data.local.ReportDao
import com.projet.citizenreport.data.local.ReportEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReportViewModel(private val dao: ReportDao) : ViewModel() {

    val reports: Flow<List<ReportEntity>> = dao.getAllReports()

    val recuCount = dao.getCountByStatus("Reçu").stateIn(viewModelScope, SharingStarted.Lazily, 0)
    val enCoursCount = dao.getCountByStatus("En cours").stateIn(viewModelScope, SharingStarted.Lazily, 0)
    val resoluCount = dao.getCountByStatus("Résolu").stateIn(viewModelScope, SharingStarted.Lazily, 0)

    // Calcul du total des points et du Badge Citoyen
    val totalUserPoints = reports.map { list -> list.size * 10 }.stateIn(viewModelScope, SharingStarted.Lazily, 0)
    val userBadge = totalUserPoints.map { points -> getUserBadgeTitle(points) }.stateIn(viewModelScope, SharingStarted.Lazily, "🌱 Citoyen Engagé")

    // --- 1. RECOMMANDATION AUTOMATIQUE DU SERVICE COMPÉTENT ---
    private fun calculateRecommendedService(category: String): String {
        return when (category) {
            "Eau & Assainissement" -> "Direction de l'Hydraulique & Assainissement"
            "Voirie" -> "Service Technique des Travaux Publics"
            "Éclairage" -> "Service Énergie & Éclairage Public"
            "Propreté" -> "Service de Gestion des Déchets / Salubrité"
            "Sécurité" -> "Police Municipale & Protection Civile"
            else -> "Secrétariat Général de la Mairie"
        }
    }

    // --- 2. GESTION DES BADGES ET POINTS ---
    private fun getUserBadgeTitle(points: Int): String {
        return when {
            points >= 100 -> "🏆 Citoyen d'Or"
            points >= 50  -> "🥈 Citoyen d'Argent"
            points >= 20  -> "🥉 Citoyen Bronze"
            else          -> "🌱 Citoyen Engagé"
        }
    }

    // --- AJOUT D'UN SIGNALEMENT AVEC AFFECTATION AUTOMATIQUE ---
    fun addReport(title: String, desc: String, category: String, priority: String, photoUri: String? = null) {
        viewModelScope.launch {
            val autoService = calculateRecommendedService(category)

            val report = ReportEntity(
                title = title,
                description = desc,
                category = category,
                priority = priority,
                photoUri = photoUri,
                recommendedService = autoService,
                userPointsEarned = 10
            )
            dao.insertReport(report)
        }
    }

    // --- 3. SYSTÈME DE VOTE CITOYEN (C'EST CETTE FONCTION QUI MANQUAIT !) ---
    fun voteForReport(report: ReportEntity) {
        viewModelScope.launch {
            val updatedReport = report.copy(votesCount = report.votesCount + 1)
            dao.updateReport(updatedReport)
        }
    }

    fun deleteReport(report: ReportEntity) {
        viewModelScope.launch {
            dao.deleteReport(report)
        }
    }

    fun changeStatus(reportId: Int, newStatus: String) {
        viewModelScope.launch {
            dao.updateStatus(reportId, newStatus)
        }
    }
}