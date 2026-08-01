package com.projet.citizenreport.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY id DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity)

    // --- MISE À JOUR DE L'ENTITÉ COMPLÈTE (Pour les votes) ---
    @Update
    suspend fun updateReport(report: ReportEntity)

    // --- REQUÊTE DIRECTE POUR INCRÉMENTER LE VOTE ---
    @Query("UPDATE reports SET votesCount = votesCount + 1 WHERE id = :reportId")
    suspend fun incrementVote(reportId: Int)

    // --- SUPPRESSION ---
    @Delete
    suspend fun deleteReport(report: ReportEntity)

    @Query("UPDATE reports SET status = :newStatus WHERE id = :reportId")
    suspend fun updateStatus(reportId: Int, newStatus: String)

    @Query("SELECT COUNT(*) FROM reports WHERE status = :status")
    fun getCountByStatus(status: String): Flow<Int>
}