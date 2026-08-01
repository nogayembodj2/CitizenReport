package com.projet.citizenreport.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projet.citizenreport.ui.theme.BackgroundLight
import com.projet.citizenreport.ui.theme.PurplePrimary
import kotlinx.coroutines.launch

// --- DATA CLASS ---
data class Report(
    val id: String = "",
    val title: String,
    val description: String,
    val location: String,
    val status: String,
    val date: String = "30/07/2026",
    val photoUri: String? = null,
    val recommendedService: String = "",
    var votesCount: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportListScreen(
    reports: List<Report>,
    onAddReportClick: () -> Unit,
    onVoteReport: (Report) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedBottomTab by remember { mutableIntStateOf(0) }

    // Etats pour les dialogues / interactions cliquables
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Calcul du Badge Citoyen
    val totalPoints = reports.size * 10
    val badgeTitle = when {
        totalPoints >= 100 -> "🏆 Citoyen d'Or"
        totalPoints >= 50  -> "🥈 Citoyen d'Argent"
        totalPoints >= 20  -> "🥉 Citoyen Bronze"
        else               -> "🌱 Citoyen Engagé"
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BackgroundLight,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "CitizenReport Pro",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar("Menu latéral ouvert") }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // 🔔 LOGO NOTIFICATION CLIQUABLE
                    IconButton(onClick = { showNotificationsDialog = true }) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                ) { Text("${reports.size}") }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PurplePrimary
                )
            )
        },
        bottomBar = {
            // 📱 BARRE DE NAVIGATION DU BAS CLIQUABLE
            NavigationBar(containerColor = Color.White) {
                // 1. Accueil
                NavigationBarItem(
                    selected = selectedBottomTab == 0,
                    onClick = {
                        selectedBottomTab = 0
                        scope.launch { snackbarHostState.showSnackbar("Onglet Accueil") }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Accueil") },
                    label = { Text("Accueil", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = PurplePrimary)
                )

                // 2. Carte
                NavigationBarItem(
                    selected = selectedBottomTab == 1,
                    onClick = {
                        selectedBottomTab = 1
                        scope.launch { snackbarHostState.showSnackbar("Chargement de la Carte...") }
                    },
                    icon = { Icon(Icons.Default.Place, contentDescription = "Carte") },
                    label = { Text("Carte", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = PurplePrimary)
                )

                // 3. Mes signalements
                NavigationBarItem(
                    selected = selectedBottomTab == 2,
                    onClick = {
                        selectedBottomTab = 2
                        scope.launch { snackbarHostState.showSnackbar("Mes Signalements") }
                    },
                    icon = { Icon(Icons.Default.List, contentDescription = "Mes signalements") },
                    label = {
                        Text(
                            text = "Mes\nsignalements",
                            fontSize = 10.sp,
                            lineHeight = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = PurplePrimary)
                )

                // 4. Profil (CLIQUABLE)
                NavigationBarItem(
                    selected = selectedBottomTab == 3,
                    onClick = {
                        selectedBottomTab = 3
                        showProfileDialog = true // Ouvre la boîte de profil
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                    label = { Text("Profil", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = PurplePrimary)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddReportClick,
                containerColor = PurplePrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(100)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nouveau signalement")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Barre de recherche
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Rechercher...", fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = PurplePrimary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(
                    onClick = {
                        scope.launch { snackbarHostState.showSnackbar("Filtre appliqué") }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(color = PurplePrimary, shape = RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "Filtrer",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // Affichage Badge
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                color = PurplePrimary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Statut : $badgeTitle",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = PurplePrimary
                    )
                    Text(
                        text = "$totalPoints Pts",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Statistiques
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { StatCard(count = "${reports.size}", label = "Total", color = PurplePrimary) }
                item {
                    val pendingCount = reports.count { it.status == "Reçu" || it.status == "En attente" }
                    StatCard(count = "$pendingCount", label = "En attente", color = Color(0xFFD97706))
                }
                item {
                    StatCard(count = "${reports.count { it.status == "En cours" }}", label = "En cours", color = Color(0xFF0284C7))
                }
                item {
                    StatCard(count = "${reports.count { it.status == "Résolu" }}", label = "Résolus", color = Color(0xFF16A34A))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // En-tête
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Signalements récents",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                TextButton(onClick = {
                    scope.launch { snackbarHostState.showSnackbar("Affichage de tous les signalements") }
                }) {
                    Text(text = "Voir tout", color = PurplePrimary, fontWeight = FontWeight.Bold)
                }
            }

            // Liste des Signalements
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                val filteredList = reports.filter {
                    it.title.contains(searchQuery, ignoreCase = true) ||
                            it.description.contains(searchQuery, ignoreCase = true)
                }

                items(filteredList) { report ->
                    ReportItemCard(
                        title = report.title,
                        description = report.description,
                        location = report.location,
                        status = report.status,
                        date = report.date,
                        photoUri = report.photoUri,
                        recommendedService = report.recommendedService,
                        votesCount = report.votesCount,
                        onVoteClicked = { onVoteReport(report) }
                    )
                }
            }
        }
    }

    // 🔔 1. FENÊTRE DE NOTIFICATION (CLIQUABLE VIA LOGO)
    if (showNotificationsDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationsDialog = false },
            title = { Text("Notifications FCM", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("• Votre signalement 'Voirie' est passé en cours de traitement.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Un nouveau vote a été ajouté à votre signalement.")
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationsDialog = false }) {
                    Text("Fermer", color = PurplePrimary)
                }
            }
        )
    }

    // 👤 2. FENÊTRE DE PROFIL (CLIQUABLE VIA MENU PROFIL)
    if (showProfileDialog) {
        AlertDialog(
            onDismissRequest = { showProfileDialog = false },
            title = { Text("Mon Profil Citoyen", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("👤 Citoyen : Nogaye", fontWeight = FontWeight.Bold)
                    Text("🏅 Badge actuel : $badgeTitle")
                    Text("⭐ Points accumulés : $totalPoints Pts")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Statut réseau : Synchronisé avec Firebase (Mode Online)")
                }
            },
            confirmButton = {
                TextButton(onClick = { showProfileDialog = false }) {
                    Text("OK", color = PurplePrimary)
                }
            }
        )
    }
}

// 🎯 COMPOSANT DES CARTES DE STATISTIQUES (RÉSOUT L'ERREUR UNRESOLVED REFERENCE)
@Composable
fun StatCard(count: String, label: String, color: Color) {
    Card(
        modifier = Modifier
            .width(85.dp)
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = count,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = color
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}