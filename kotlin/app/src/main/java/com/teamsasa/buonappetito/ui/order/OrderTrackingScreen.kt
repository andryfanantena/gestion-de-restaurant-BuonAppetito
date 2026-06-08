package com.teamsasa.buonappetito.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamsasa.buonappetito.data.model.Order
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.viewmodel.CartViewModel
import com.teamsasa.buonappetito.viewmodel.MenuViewModel
import com.teamsasa.buonappetito.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    orderId: Long,
    viewModel: OrderViewModel,
    cartViewModel: CartViewModel,
    menuViewModel: MenuViewModel,
    onNavigateToPayment: (Long, Float) -> Unit,
    onNavigateToReview: (Long, String) -> Unit
) {
    val orderState by viewModel.trackedOrder.collectAsStateWithLifecycle()

    // Charger la commande au démarrage et rafraîchir l'état
    LaunchedEffect(orderId) {
        viewModel.trackOrder(orderId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Suivi de commande", style = EpicureanTypography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.stopTracking() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EpicureanBg)
            )
        },
        containerColor = EpicureanBg
    ) { paddingValues ->
        orderState?.let { order ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // En-tête : Numéro de commande et Table
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Commande #${order.orderNumber}",
                            style = EpicureanTypography.titleMedium,
                            color = TextDark,
                            fontWeight = FontWeight.Bold
                        )
                        order.tableNumber?.let { table ->
                            Text(
                                text = "Table n°$table",
                                style = EpicureanTypography.bodyMedium,
                                color = TextMuted
                            )
                        }
                    }

                    // Badge de statut principal
                    Surface(
                        color = getStatusColor(order.status).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = formatStatusText(order.status),
                            style = EpicureanTypography.labelLarge,
                            color = getStatusColor(order.status),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // ── Étapes de progression visuelles ───────────────────────────────
                TrackingProgressBar(currentStatus = order.status)

                // ── Liste des articles commandés ──────────────────────────────────
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Résumé de votre commande",
                            style = EpicureanTypography.titleMedium,
                            color = TextDark,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(order.items) { item ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${item.quantity}x ${item.dishName}",
                                        style = EpicureanTypography.bodyLarge,
                                        color = TextDark
                                    )
                                    Text(
                                        text = String.format("%.2f €", item.price * item.quantity),
                                        style = EpicureanTypography.bodyLarge,
                                        color = TextDark,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = EpicureanBg, modifier = Modifier.padding(vertical = 12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total",
                                style = EpicureanTypography.titleMedium,
                                color = TextDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = String.format("%.2f €", order.totalPrice),
                                style = EpicureanTypography.titleLarge,
                                color = EpicureanPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // ── Bouton d'action contextuel selon le statut ────────────────────
                when (order.status) {
                    "pending", "preparing", "ready" -> {
                        // Optionnel : un bouton pour demander de l'aide au serveur
                        OutlinedButton(
                            onClick = { /* Appeler un serveur */ },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = EpicureanPrimary)
                        ) {
                            Text("🛎️ Appeler un serveur", style = EpicureanTypography.titleMedium)
                        }
                    }
                    "delivered" -> {
                        // La commande est servie, l'utilisateur passe au paiement
                        Button(
                            onClick = { onNavigateToPayment(order.id, order.totalPrice.toFloat()) },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EpicureanPrimary)
                        ) {
                            Text("💳 Procéder au paiement", style = EpicureanTypography.titleMedium, color = Color.White)
                        }
                    }
                    "paid" -> {
                        // La commande est payée, on invite à donner son avis
                        Button(
                            onClick = { onNavigateToReview(order.id, order.orderNumber) },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EpicureanPrimary)
                        ) {
                            Text("⭐ Donner mon avis", style = EpicureanTypography.titleMedium, color = Color.White)
                        }
                    }
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = EpicureanPrimary)
        }
    }
}

@Composable
private fun TrackingProgressBar(currentStatus: String) {
    val steps = listOf("pending", "preparing", "ready", "delivered")
    val currentStepIndex = steps.indexOf(currentStatus).let { if (it == -1 && currentStatus == "paid") 3 else it }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, step ->
            val isCompleted = index <= currentStepIndex
            val isActive = index == currentStepIndex

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isActive -> EpicureanPrimary
                                isCompleted -> EpicureanPrimary.copy(alpha = 0.6f)
                                else -> Color.LightGray
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (index + 1).toString(),
                        color = if (isCompleted) Color.White else TextMuted,
                        style = EpicureanTypography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (step) {
                        "pending" -> "Reçue"
                        "preparing" -> "En cuisine"
                        "ready" -> "Prête"
                        else -> "Servie"
                    },
                    style = EpicureanTypography.bodySmall,
                    color = if (isActive) EpicureanPrimary else TextMuted,
                    textAlign = TextAlign.Center,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

private fun getStatusColor(status: String): Color {
    return when (status) {
        "pending" -> Color(0xFFFF9800)   // Orange
        "preparing" -> Color(0xFF2196F3) // Bleu
        "ready" -> Color(0xFF4CAF50)     // Vert
        "delivered" -> Color(0xFF9C27B0) // Violet
        "paid" -> EpicureanPrimary       // Couleur thème de base
        else -> Color.Gray
    }
}

private fun formatStatusText(status: String): String {
    return when (status) {
        "pending" -> "En attente"
        "preparing" -> "En préparation"
        "ready" -> "Prête à servir"
        "delivered" -> "Livrée à table"
        "paid" -> "Payée"
        else -> status.replaceFirstChar { it.uppercase() }
    }
}