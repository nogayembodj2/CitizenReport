package com.projet.citizenreport.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projet.citizenreport.ui.theme.PurplePrimary

@Composable
fun LoginScreen(
    onLoginSuccess: (isAdmin: Boolean) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }

    // NOUVEAU : État pour savoir si l'utilisateur veut créer un compte ou se connecter
    var isSignUpMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // En-tête
        Text(
            text = "CitizenReport",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = PurplePrimary
        )
        Text(
            text = "Plateforme de Participation Citoyenne",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Titre dynamique (Bienvenue vs Créer un compte)
        Text(
            text = if (isSignUpMode) "Créer un compte" else "Bienvenue !",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Champ Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Adresse Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Champ Mot de passe
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Checkbox Admin (uniquement visible en mode Connexion)
        if (!isSignUpMode) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = isAdmin,
                    onCheckedChange = { isAdmin = it }
                )
                Text(
                    text = "Se connecter en tant qu'Administrateur",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Bouton principal (Connexion ou Inscription)
        Button(
            onClick = { onLoginSuccess(isAdmin) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
        ) {
            Text(
                text = if (isSignUpMode) "S'inscrire" else (if (isAdmin) "Connexion Admin" else "Connexion Citoyen"),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // NOUVEAU : Lien pour basculer entre Connexion et Inscription
        TextButton(
            onClick = { isSignUpMode = !isSignUpMode }
        ) {
            Text(
                text = if (isSignUpMode)
                    "Vous avez déjà un compte ? Se connecter"
                else
                    "Nouveau citoyen ? Créer un compte",
                color = PurplePrimary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}