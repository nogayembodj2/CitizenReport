package com.projet.citizenreport.data.local

import java.util.UUID

data class Report(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val category: String, // ex: "Voirie", "Éclairage", "Propreté"
    val status: String = "En attente", // "En attente", "En cours", "Résolu"
    val timestamp: Long = System.currentTimeMillis()
)