package com.teamsasa.buonappetito.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamsasa.buonappetito.data.model.Order
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.utils.formatPrice
import com.teamsasa.buonappetito.viewmodel.OrderViewModel

@Composable
fun OrderHistoryScreen(viewModel: OrderViewModel) {
    val orderHistory: List<Order> by viewModel.orderHistory.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().background(EpicureanBg).padding(20.dp)) {
        Text(
            text = "Mes Commandes", 
            style = EpicureanTypography.titleLarge, 
            color = TextDark, 
            modifier = Modifier.padding(bottom = 20.dp)
        )

        if (orderHistory.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Aucune commande pour le moment", style = EpicureanTypography.bodyLarge, color = TextMuted)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                items(orderHistory, key = { it.id }) { order ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
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
                                    Text(
                                        text = order.date, 
                                        style = EpicureanTypography.bodySmall,
                                        color = TextMuted
                                    )
                                }
                                
                                Surface(
                                    color = when(order.status) {
                                        "DELIVERED" -> Color(0xFFE8F5E9)
                                        "PREPARING" -> Color(0xFFFFF3E0)
                                        "READY" -> Color(0xFFE3F2FD)
                                        else -> Color(0xFFF5F5F5)
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = when(order.status) {
                                            "DELIVERED" -> "Livré"
                                            "PREPARING" -> "En cuisine"
                                            "READY" -> "Prête"
                                            "PENDING" -> "En attente"
                                            else -> order.status
                                        },
                                        style = EpicureanTypography.labelSmall,
                                        color = when(order.status) {
                                            "DELIVERED" -> Color(0xFF2E7D32)
                                            "PREPARING" -> Color(0xFFEF6C00)
                                            "READY" -> Color(0xFF1565C0)
                                            else -> TextDark
                                        },
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = EpicureanBg)
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${order.items.size} article(s)", 
                                    style = EpicureanTypography.bodyMedium,
                                    color = TextMuted
                                )
                                Text(
                                    text = order.totalPrice.formatPrice(), 
                                    style = EpicureanTypography.titleMedium, 
                                    color = EpicureanAccent,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
