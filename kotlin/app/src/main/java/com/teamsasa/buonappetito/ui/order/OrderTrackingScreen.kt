package com.teamsasa.buonappetito.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamsasa.buonappetito.data.model.Order
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.viewmodel.OrderViewModel

@Composable
fun OrderTrackingScreen(orderId: Long, viewModel: OrderViewModel) {
    val currentOrder: Order? by viewModel.trackedOrder.collectAsStateWithLifecycle()

    LaunchedEffect(orderId) {
        viewModel.startTrackingOrder(orderId)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(EpicureanBg).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Suivi de commande", 
            style = EpicureanTypography.displayLarge, 
            color = TextDark
        )
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Statut : ${currentOrder?.status ?: "En cours de validation..."}", 
                    style = EpicureanTypography.titleLarge, 
                    color = EpicureanPrimary
                )
            }
        }
    }
}
