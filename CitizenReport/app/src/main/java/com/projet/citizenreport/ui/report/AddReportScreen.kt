package com.projet.citizenreport.ui.report

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.projet.citizenreport.ui.theme.PurplePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReportScreen(
    onReportSubmitted: (title: String, description: String, category: String, priority: String, photoUri: String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Voirie") }
    var priority by remember { mutableStateOf("Moyenne") }
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var isCategoryExpanded by remember { mutableStateOf(false) }

    val categoriesList = listOf("Voirie", "Salubrité & Déchets", "Éclairage Public", "Eau & Assainissement", "Autre")
    val prioritiesList = listOf("Basse", "Moyenne", "Haute")

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedPhotoUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nouveau Signalement", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PurplePrimary)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Titre
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Titre du problème") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // 2. Sélection de Catégorie
            ExposedDropdownMenuBox(
                expanded = isCategoryExpanded,
                onExpandedChange = { isCategoryExpanded = !isCategoryExpanded }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Catégorie") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = isCategoryExpanded,
                    onDismissRequest = { isCategoryExpanded = false }
                ) {
                    categoriesList.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                category = item
                                isCategoryExpanded = false
                            }
                        )
                    }
                }
            }

            // 3. Sélection de la Priorité (Basse, Moyenne, Haute)
            Column {
                Text(
                    text = "Priorité",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    prioritiesList.forEach { item ->
                        val isSelected = priority == item
                        OutlinedButton(
                            onClick = { priority = item },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) PurplePrimary else Color.Transparent,
                                contentColor = if (isSelected) Color.White else Color.DarkGray
                            )
                        ) {
                            Text(item, fontSize = 13.sp)
                        }
                    }
                }
            }

            // 4. Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description détaillée") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(12.dp)
            )

            // 5. Zone Ajouter une Photo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F0F0))
                    .clickable { photoPickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (selectedPhotoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedPhotoUri),
                        contentDescription = "Photo sélectionnée",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = PurplePrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Ajouter une photo (Optionnel)", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            }

            // 6. Bouton Soumettre
            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        onReportSubmitted(
                            title,
                            description,
                            category,
                            priority,
                            selectedPhotoUri?.toString()
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
            ) {
                Text("Soumettre le signalement", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}