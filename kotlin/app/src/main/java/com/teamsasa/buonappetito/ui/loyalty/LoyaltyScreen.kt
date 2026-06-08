package com.teamsasa.buonappetito.ui.loyalty

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamsasa.buonappetito.data.model.LoyaltyResponse
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoyaltyScreen(viewModel: OrderViewModel, onBack: () -> Unit) {
    val loyalty by viewModel.loyalty.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.loadLoyalty() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Programme Fidélité", style = EpicureanTypography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EpicureanBg)
            )
        },
        containerColor = EpicureanBg
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (loyalty == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = EpicureanPrimary)
                }
            } else {
                LoyaltyContent(loyalty = loyalty!!)
            }
        }
    }
}

@Composable
private fun LoyaltyContent(loyalty: LoyaltyResponse) {
    val levelColor = when (loyalty.level) {
        "Gold"   -> listOf(Color(0xFFFFD700), Color(0xFFFF8C00))
        "Silver" -> listOf(Color(0xFF9E9E9E), Color(0xFF607D8B))
        else     -> listOf(Color(0xFFCD7F32), Color(0xFF8D4E23))
    }
    val levelEmoji = when (loyalty.level) {
        "Gold"   -> "🥇"
        "Silver" -> "🥈"
        else     -> "🥉"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(levelColor))
            .padding(24.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(text = levelEmoji, fontSize = 48.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Niveau ${loyalty.level}", style = EpicureanTypography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Text("${loyalty.points}", fontSize = 56.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("points accumulés", style = EpicureanTypography.bodyLarge, color = Color.White.copy(alpha = 0.85f))
        }
    }

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Prochaine récompense", style = EpicureanTypography.titleMedium, color = TextDark, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(loyalty.nextReward, style = EpicureanTypography.bodyLarge, color = EpicureanPrimary)
            Spacer(modifier = Modifier.height(12.dp))
            val progress = if (loyalty.pointsToNext > 0)
                (loyalty.points % loyalty.pointsToNext).toFloat() / loyalty.pointsToNext else 1f
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                color = EpicureanPrimary, trackColor = EpicureanBg
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text("Plus que ${loyalty.pointsToNext} points", style = EpicureanTypography.bodySmall, color = TextMuted, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
        }
    }

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Comment gagner des points ?", style = EpicureanTypography.titleMedium, color = TextDark, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            listOf(
                "🛒" to "1 point par tranche de 1 000 Ar dépensés",
                "⭐" to "50 points bonus en laissant un avis",
                "🔁" to "100 points bonus à chaque 5ème commande",
                "🎉" to "Double points le week-end"
            ).forEach { (emoji, rule) ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = emoji, fontSize = 20.sp, modifier = Modifier.width(32.dp))
                    Text(text = rule, style = EpicureanTypography.bodyLarge, color = TextDark)
                }
            }
        }
    }

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Niveaux", style = EpicureanTypography.titleMedium, color = TextDark, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            listOf(
                Triple("🥉", "Bronze", "0 – 499 pts"),
                Triple("🥈", "Silver", "500 – 1 499 pts  →  -5% sur commandes"),
                Triple("🥇", "Gold",   "1 500+ pts  →  -10% + dessert offert")
            ).forEach { (emoji, level, desc) ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = emoji, fontSize = 20.sp, modifier = Modifier.width(32.dp))
                    Column {
                        Text(text = level, style = EpicureanTypography.titleMedium, color = TextDark)
                        Text(text = desc, style = EpicureanTypography.bodySmall, color = TextMuted)
                    }
                }
            }
        }
    }
}