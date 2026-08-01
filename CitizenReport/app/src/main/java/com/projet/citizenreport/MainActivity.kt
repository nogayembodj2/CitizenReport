package com.projet.citizenreport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.projet.citizenreport.data.local.AppDatabase
import com.projet.citizenreport.ui.auth.LoginScreen
import com.projet.citizenreport.ui.report.AddReportScreen
import com.projet.citizenreport.ui.report.AdminScreen
import com.projet.citizenreport.ui.report.Report
import com.projet.citizenreport.ui.report.ReportListScreen
import com.projet.citizenreport.ui.report.ReportViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val viewModel = ReportViewModel(database.reportDao())

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF6750A4),
                    secondary = Color(0xFF625B71)
                )
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF6F6F6)) {
                    var isLoggedIn by remember { mutableStateOf(false) }
                    var isAdminMode by remember { mutableStateOf(false) }
                    var showAddScreen by remember { mutableStateOf(false) }

                    // Récupération des données depuis la base Room
                    val reportsEntities by viewModel.reports.collectAsState(initial = emptyList())

                    // POPULATION AUTOMATIQUE
                    LaunchedEffect(reportsEntities) {
                        if (reportsEntities.isEmpty()) {
                            viewModel.addReport(
                                title = "Nid de poule dangereux",
                                desc = "Un gros trou sur la chaussée risquant d'endommager les véhicules.",
                                category = "Voirie",
                                priority = "Élevée",
                                photoUri = null
                            )
                            viewModel.addReport(
                                title = "Panne d'éclairage public",
                                desc = "La rue est totalement sombre depuis deux jours.",
                                category = "Éclairage",
                                priority = "Moyenne",
                                photoUri = null
                            )
                            viewModel.addReport(
                                title = "Dépôt sauvage d'ordures",
                                desc = "Accumulation de déchets ménagers bloquant le trottoir.",
                                category = "Propreté",
                                priority = "Faible",
                                photoUri = null
                            )
                        }
                    }

                    // Conversion Room -> UI
                    val reportsList = reportsEntities.map { entity ->
                        Report(
                            id = entity.id.toString(),
                            title = entity.title,
                            description = entity.description,
                            location = entity.category,
                            status = entity.status,
                            date = entity.date,
                            photoUri = entity.photoUri,
                            recommendedService = entity.recommendedService,
                            votesCount = entity.votesCount
                        )
                    }

                    when {
                        !isLoggedIn -> {
                            LoginScreen(
                                onLoginSuccess = { isAdmin ->
                                    isAdminMode = isAdmin
                                    isLoggedIn = true
                                }
                            )
                        }
                        showAddScreen -> {
                            AddReportScreen(
                                onReportSubmitted = { title, desc, category, priority, photoUri ->
                                    viewModel.addReport(title, desc, category, priority, photoUri)
                                    showAddScreen = false
                                }
                            )
                        }
                        isAdminMode -> {
                            AdminScreen(
                                reports = reportsList,
                                onBackClick = { isAdminMode = false },
                                onStatusChange = { reportUI, newStatus ->
                                    val idInt = reportUI.id.toIntOrNull()
                                    if (idInt != null) {
                                        viewModel.changeStatus(idInt, newStatus)
                                    }
                                }
                            )
                        }
                        else -> {
                            ReportListScreen(
                                reports = reportsList,
                                onAddReportClick = { showAddScreen = true },
                                onVoteReport = { reportUI ->
                                    // 💡 FIX: On recherche l'entité Room correspondant à l'ID du rapport cliqué
                                    val targetEntity = reportsEntities.find { it.id.toString() == reportUI.id }
                                    if (targetEntity != null) {
                                        viewModel.voteForReport(targetEntity)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}