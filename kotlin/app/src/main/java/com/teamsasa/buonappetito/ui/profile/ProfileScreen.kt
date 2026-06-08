package com.teamsasa.buonappetito.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamsasa.buonappetito.data.model.LoyaltyResponse
import com.teamsasa.buonappetito.data.model.User
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.viewmodel.AuthViewModel
import com.teamsasa.buonappetito.viewmodel.OrderViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    orderViewModel: OrderViewModel,
    onLogout: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToLoyalty: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
    val loyalty     by orderViewModel.loyalty.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { orderViewModel.loadLoyalty() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EpicureanBg)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mon Profil",
            style = EpicureanTypography.titleLarge,
            color = TextDark,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 24.dp)
        )

        // ── Avatar + infos ────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(EpicureanPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currentUser?.name?.firstOrNull()?.uppercase() ?: "?",
                style = EpicureanTypography.displayLarge,
                color = EpicureanPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = currentUser?.name ?: "Utilisateur",
            style = EpicureanTypography.titleLarge,
            color = TextDark,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = currentUser?.email ?: "",
            style = EpicureanTypography.bodyLarge,
            color = TextMuted
        )

        // Badge rôle
        currentUser?.role?.let { role ->
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                color = EpicureanPrimary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = when (role) {
                        "admin"  -> "⚙️  Administrateur"
                        "cook"   -> "👨‍🍳  Cuisinier"
                        "server" -> "🛎️  Serveur"
                        else     -> "🍽️  Client"
                    },
                    style = EpicureanTypography.labelLarge,
                    color = EpicureanPrimary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ── Carte fidélité mini ───────────────────────────────────────────────
        loyalty?.let { loy ->
            LoyaltyMiniCard(loyalty = loy, onClick = onNavigateToLoyalty)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ── Menu options ──────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(8.dp)
        ) {
            ProfileMenuItem(title = "🧾  Mes Commandes",        onClick = onNavigateToHistory)
            ProfileMenuItem(title = "🎁  Programme Fidélité",    onClick = onNavigateToLoyalty)
            ProfileMenuItem(title = "⚙️  Paramètres",           onClick = {})
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = EpicureanBg)
            ProfileMenuItem(title = "Déconnexion", isDanger = true, onClick = onLogout)
        }
    }
}

@Composable
private fun LoyaltyMiniCard(loyalty: LoyaltyResponse, onClick: () -> Unit) {
    val levelEmoji = when (loyalty.level) {
        "Gold"   -> "🥇"
        "Silver" -> "🥈"
        else     -> "🥉"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = EpicureanPrimary),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = levelEmoji, style = MaterialTheme.typography.headlineMedium)
                Column {
                    Text(
                        text = "Niveau ${loyalty.level}",
                        style = EpicureanTypography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${loyalty.points} points",
                        style = EpicureanTypography.bodyLarge,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
            Text(text = ">", color = Color.White.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun ProfileMenuItem(title: String, isDanger: Boolean = false, onClick: () -> Unit = {}) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = EpicureanTypography.titleMedium,
                color = if (isDanger) MaterialTheme.colorScheme.error else TextDark
            )
            if (!isDanger) Text(text = ">", color = TextMuted)
        }
    }
}