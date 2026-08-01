package com.projet.citizenreport.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val category: String,
    val priority: String,
    val status: String = "Reçu",
    val latitude: Double = 14.6937,
    val longitude: Double = -17.4441,
    val photoUri: String? = null,
    val date: String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()),

    // --- NOUVEAUX CHAMPS D'INNOVATION ---
    val votesCount: Int = 0,
    val recommendedService: String = "",
    val userPointsEarned: Int = 10
)