package com.teamsasa.buonappetito.ui.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    orderId: Long,
    orderNumber: String,
    viewModel: OrderViewModel,
    onReviewDone: () -> Unit,
    onBack: () -> Unit
) {
    var rating    by remember { mutableIntStateOf(0) }
    var comment   by remember { mutableStateOf("") }

    val isLoading  by viewModel.isLoading.collectAsStateWithLifecycle()
    val reviewSent by viewModel.reviewSent.collectAsStateWithLifecycle()

    // Labels selon la note
    val ratingLabel = when (rating) {
        1 -> "😞  Très déçu"
        2 -> "😐  Peut mieux faire"
        3 -> "🙂  Correct"
        4 -> "😊  Très bien"
        5 -> "🤩  Excellent !"
        else -> ""
    }

    LaunchedEffect(reviewSent) {
        if (reviewSent) {
            viewModel.resetReviewSent()
            onReviewDone()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Donner mon avis", style = EpicureanTypography.titleLarge) },
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // En-tête
            Text(
                text = "Commande #$orderNumber",
                style = EpicureanTypography.bodyLarge,
                color = TextMuted
            )
            Text(
                text = "Comment était votre expérience ?",
                style = EpicureanTypography.titleLarge,
                color = TextDark,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            // ── Étoiles ───────────────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 1..5) {
                            Icon(
                                imageVector = if (i <= rating) Icons.Filled.Star
                                else Icons.Outlined.Star,
                                contentDescription = "$i étoile(s)",
                                tint = if (i <= rating) Color(0xFFFFC107) else Color.LightGray,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable { rating = i }
                            )
                        }
                    }

                    if (ratingLabel.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = ratingLabel,
                            style = EpicureanTypography.titleMedium,
                            color = EpicureanPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ── Commentaire ───────────────────────────────────────────────────
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Partagez votre expérience (optionnel)") },
                placeholder = { Text("Plats savoureux, service rapide...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(16.dp),
                maxLines = 5
            )

            Text(
                text = "${comment.length}/300 caractères",
                style = EpicureanTypography.bodySmall,
                color = TextMuted,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.weight(1f))

            // ── Bouton Envoyer ────────────────────────────────────────────────
            Button(
                onClick = {
                    viewModel.submitReview(orderId, rating, comment.take(300))
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EpicureanPrimary),
                enabled = rating > 0 && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Envoyer mon avis",
                        style = EpicureanTypography.titleMedium,
                        color = Color.White
                    )
                }
            }

            TextButton(onClick = onBack) {
                Text(
                    text = "Passer",
                    color = TextMuted,
                    style = EpicureanTypography.bodyLarge
                )
            }
        }
    }
}