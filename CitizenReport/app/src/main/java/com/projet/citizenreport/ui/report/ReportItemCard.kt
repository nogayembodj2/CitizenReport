package com.projet.citizenreport.ui.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@Composable
fun ReportItemCard(
    title: String,
    description: String,
    location: String,
    status: String,
    date: String,
    photoUri: String?,
    // --- PARAMS NOUVEAUX ---
    recommendedService: String = "",
    votesCount: Int = 0,
    onVoteClicked: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // En-tête : Titre + Statut
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.weight(1f))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (status) {
                        "Résolu" -> Color(0xFFE8F5E9)
                        "En cours" -> Color(0xFFFFF3E0)
                        else -> Color(0xFFE3F2FD)
                    }
                ) {
                    Text(
                        text = status,
                        color = when (status) {
                            "Résolu" -> Color(0xFF2E7D32)
                            "En cours" -> Color(0xFFE65100)
                            else -> Color(0xFF1565C0)
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(description, color = Color.DarkGray, fontSize = 14.sp)

            // 🏛️ INNOVATION 1 : SERVICE RECOMMANDÉ AUTOMATIQUEMENT
            if (recommendedService.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "🏛️ Service : $recommendedService",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1976D2)
                )
            }

            // Affichage de la photo enregistrée
            if (!photoUri.isNull_or_Empty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = rememberAsyncImagePainter(photoUri),
                    contentDescription = "Photo du signalement",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pied de Carte : Infos (Localisation/Date) + Bouton Vote
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = location, fontSize = 11.sp, color = Color.Gray)
                    Text(text = date, fontSize = 11.sp, color = Color.Gray)
                }

                // 🗳️ INNOVATION 2 : BOUTON DE VOTE CITOYEN
                OutlinedButton(
                    onClick = { onVoteClicked() },
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Voter",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$votesCount votes",
                        fontSize = 12.sp,
                        color = Color(0xFF1976D2),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun String?.isNull_or_Empty(): Boolean = this == null || this.isEmpty()