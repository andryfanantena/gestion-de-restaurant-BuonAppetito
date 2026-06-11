package com.teamsasa.buonappetito.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("client") }

    val isLoading: Boolean by viewModel.isLoading.collectAsStateWithLifecycle()
    val error: String? by viewModel.error.collectAsStateWithLifecycle()

    // Les rôles disponibles à l'inscription (selon le PDF)
    val roles = listOf(
        "client"   to "🍽️  Client",
        "cook"     to "👨‍🍳  Cuisinier",
        "server"   to "🛎️  Serveur",
        "admin"    to "⚙️  Admin"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EpicureanBg)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Créer un compte",
            style = EpicureanTypography.displayLarge,
            color = EpicureanPrimary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nom complet") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ── Sélecteur de rôle ──────────────────────────────────────────
        Text(
            text = "Je suis...",
            style = EpicureanTypography.titleMedium,
            color = TextDark,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 12.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            roles.forEach { (roleKey, roleLabel) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (selectedRole == roleKey) EpicureanPrimary.copy(alpha = 0.08f)
                            else Color.Transparent,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedRole == roleKey,
                        onClick = { selectedRole = roleKey },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = EpicureanPrimary
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = roleLabel,
                        style = EpicureanTypography.titleMedium,
                        color = if (selectedRole == roleKey) EpicureanPrimary else TextDark
                    )
                }
            }
        }

        // Affichage erreur
        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 12.dp),
                style = EpicureanTypography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.register(name, email, password, selectedRole, onRegisterSuccess)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EpicureanPrimary),
            enabled = !isLoading && name.isNotBlank() && email.isNotBlank() && password.length >= 6
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("S'inscrire", style = EpicureanTypography.titleMedium)
            }
        }

        TextButton(
            onClick = onNavigateToLogin,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = "Déjà un compte ? Se connecter",
                color = EpicureanPrimary
            )
        }
    }
}