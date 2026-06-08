package com.teamsasa.buonappetito.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamsasa.buonappetito.data.model.User
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel, 
    onLogout: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val currentUser: User? by authViewModel.currentUser.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().background(EpicureanBg).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mon Profil", 
            style = EpicureanTypography.titleLarge, 
            color = TextDark, 
            modifier = Modifier.align(Alignment.Start).padding(bottom = 32.dp)
        )

        Box(modifier = Modifier.size(100.dp).clip(CircleShape).background(Color.LightGray))

        Text(
            text = currentUser?.name ?: "Utilisateur", 
            style = EpicureanTypography.titleLarge, 
            color = TextDark, 
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = currentUser?.email ?: "Pas d'adresse email", 
            style = EpicureanTypography.bodyLarge, 
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Column(modifier = Modifier.fillMaxWidth().background(CardBg, RoundedCornerShape(24.dp)).padding(8.dp)) {
            ProfileMenuItem(title = "Mes Commandes", onClick = onNavigateToHistory)
            ProfileMenuItem(title = "Modifier les informations")
            ProfileMenuItem(title = "Paramètres de l'application")
            ProfileMenuItem(title = "Déconnexion", isDanger = true, onClick = onLogout)
        }
    }
}

@Composable
fun ProfileMenuItem(title: String, isDanger: Boolean = false, onClick: () -> Unit = {}) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = EpicureanTypography.titleMedium,
                color = if (isDanger) Color.Red else TextDark
            )
            Text(text = ">", color = TextMuted)
        }
    }
}
