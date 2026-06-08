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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamsasa.buonappetito.data.model.CartItemRequest
import com.teamsasa.buonappetito.data.model.CheckoutRequest
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.utils.formatPrice
import com.teamsasa.buonappetito.viewmodel.CartViewModel

@Composable
fun CartScreen(
    viewModel: CartViewModel, 
    onCheckoutClick: (CheckoutRequest) -> Unit,
    onNavigateToScanner: () -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val totalPrice by viewModel.totalPrice.collectAsStateWithLifecycle()
    val tableNumber by viewModel.tableNumber.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().background(EpicureanBg).padding(20.dp)) {
        Text(
            text = "Mon Panier", 
            style = EpicureanTypography.titleLarge, 
            color = TextDark, 
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Affichage du numéro de table
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            onClick = onNavigateToScanner
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (tableNumber != null) "Table : $tableNumber" else "Scanner une table",
                    style = EpicureanTypography.titleMedium,
                    color = if (tableNumber != null) EpicureanPrimary else Color.Red,
                    modifier = Modifier.weight(1f)
                )
                Text(text = "Modifier", style = EpicureanTypography.labelLarge, color = EpicureanPrimary)
            }
        }

        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            items(cartItems) { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CardBg, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = item.dish.name, style = EpicureanTypography.titleMedium, color = TextDark)
                            Text(text = "${item.dish.price.formatPrice()} x ${item.quantity}", style = EpicureanTypography.bodyLarge)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextButton(onClick = { viewModel.updateQuantity(item.dish.id, item.comment, item.quantity - 1) }) { 
                                Text("-", color = EpicureanPrimary, style = EpicureanTypography.titleLarge) 
                            }
                            Text(text = item.quantity.toString(), style = EpicureanTypography.titleMedium, modifier = Modifier.padding(horizontal = 8.dp))
                            TextButton(onClick = { viewModel.updateQuantity(item.dish.id, item.comment, item.quantity + 1) }) { 
                                Text("+", color = EpicureanPrimary, style = EpicureanTypography.titleLarge) 
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = item.comment,
                        onValueChange = { newComment -> 
                            viewModel.updateComment(item.dish.id, item.comment, newComment)
                        },
                        label = { Text("Instructions spéciales (ex: sans sel)", style = EpicureanTypography.bodySmall) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = EpicureanTypography.bodySmall,
                        singleLine = true
                    )
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
                onClick = {
                    val request = CheckoutRequest(
                        table_number = tableNumber,
                        items = cartItems.map { CartItemRequest(it.dish.id, it.quantity, it.comment) }
                    )
                    onCheckoutClick(request)
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EpicureanPrimary),
                enabled = cartItems.isNotEmpty() && tableNumber != null
            ) {
                Text("Passer la commande", style = EpicureanTypography.titleMedium, color = Color.White)
            }
        }
    }
}
