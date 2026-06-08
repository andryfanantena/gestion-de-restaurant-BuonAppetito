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
import androidx.compose.ui.unit.dp
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.utils.formatPrice
import com.teamsasa.buonappetito.viewmodel.OrderViewModel

@Composable
fun OrderHistoryScreen(viewModel: OrderViewModel) {
    val orderHistory by viewModel.orderHistory.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(EpicureanBg).padding(20.dp)) {
        Text("Mes Commandes", style = EpicureanTypography.titleLarge, color = TextDark, modifier = Modifier.padding(bottom = 20.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            items(orderHistory) { order ->
                Row(
                    modifier = Modifier.fillMaxWidth().background(CardBg, RoundedCornerShape(20.dp)).padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Commande #${order.orderNumber}", style = EpicureanTypography.titleMedium, color = TextDark)
                        Text(text = order.date, style = EpicureanTypography.bodyLarge)
                        Text(
                            text = when(order.status) {
                                "DELIVERED" -> "Livré"
                                "PREPARING" -> "En préparation"
                                else -> "En attente"
                            },
                            style = EpicureanTypography.labelLarge,
                            color = EpicureanPrimary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Text(text = order.totalPrice.formatPrice(), style = EpicureanTypography.titleMedium, color = EpicureanAccent)
                }
            }
        }
    }
}
