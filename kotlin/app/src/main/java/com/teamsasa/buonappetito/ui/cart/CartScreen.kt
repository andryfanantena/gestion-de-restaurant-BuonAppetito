package com.teamsasa.buonappetito.ui.cart

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
import androidx.compose.ui.unit.dp
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.utils.formatPrice
import com.teamsasa.buonappetito.viewmodel.CartViewModel

@Composable
fun CartScreen(viewModel: CartViewModel, onCheckoutClick: () -> Unit) {
    val cartItems by viewModel.cartItems.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(EpicureanBg).padding(20.dp)) {
        Text("Mon Panier", style = EpicureanTypography.titleLarge, color = TextDark, modifier = Modifier.padding(bottom = 20.dp))

        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            items(cartItems) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().background(CardBg, RoundedCornerShape(16.dp)).padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = item.dish.name, style = EpicureanTypography.titleMedium, color = TextDark)
                        Text(text = "${item.dish.price.formatPrice()} x ${item.quantity}", style = EpicureanTypography.bodyLarge)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(onClick = { viewModel.updateQuantity(item.dish.id, item.quantity - 1) }) { Text("-", color = EpicureanPrimary) }
                        Text(text = item.quantity.toString(), style = EpicureanTypography.titleMedium, modifier = Modifier.padding(horizontal = 8.dp))
                        TextButton(onClick = { viewModel.updateQuantity(item.dish.id, item.quantity + 1) }) { Text("+", color = EpicureanPrimary) }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.fillMaxWidth().background(CardBg, RoundedCornerShape(24.dp)).padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Sous-total", style = EpicureanTypography.bodyLarge)
                Text(totalPrice.formatPrice(), style = EpicureanTypography.titleMedium, color = TextDark)
            }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Frais de livraison", style = EpicureanTypography.bodyLarge)
                Text("Gratuit", style = EpicureanTypography.titleMedium, color = EpicureanAccent)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = EpicureanBg)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Total", style = EpicureanTypography.titleLarge, color = TextDark)
                Text(totalPrice.formatPrice(), style = EpicureanTypography.titleLarge, color = EpicureanAccent)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onCheckoutClick,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EpicureanPrimary)
            ) {
                Text("Passer la commande", style = EpicureanTypography.titleMedium, color = Color.White)
            }
        }
    }
}
