package com.teamsasa.buonappetito.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.teamsasa.buonappetito.data.model.Dish
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.utils.formatPrice
import com.teamsasa.buonappetito.viewmodel.CartViewModel

@Composable
fun DishDetailScreen(dish: Dish, cartViewModel: CartViewModel, onBack: () -> Unit) {
    var quantity by remember { mutableIntStateOf(1) }

    Column(modifier = Modifier.fillMaxSize().background(EpicureanBg)) {
        Box(modifier = Modifier.fillMaxWidth().weight(1f).background(Color.LightGray)) {
            Button(
                onClick = onBack,
                modifier = Modifier.padding(16.dp).size(40.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = TextDark),
                contentPadding = PaddingValues(0.dp)
            ) { Text("<") }
        }

        Surface(
            modifier = Modifier.fillMaxWidth().weight(1.2f),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = dish.name, style = EpicureanTypography.displayLarge, color = TextDark, modifier = Modifier.weight(1f))
                    Text(text = dish.price.formatPrice(), style = EpicureanTypography.displayLarge, color = EpicureanAccent)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = dish.description, style = EpicureanTypography.bodyLarge, color = TextMuted)
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { if (quantity > 1) quantity-- }) { Text("-", style = EpicureanTypography.titleLarge) }
                        Text(text = quantity.toString(), style = EpicureanTypography.titleLarge, modifier = Modifier.padding(horizontal = 16.dp))
                        IconButton(onClick = { quantity++ }) { Text("+", style = EpicureanTypography.titleLarge) }
                    }

                    Button(
                        onClick = { cartViewModel.addToCart(dish, quantity) },
                        colors = ButtonDefaults.buttonColors(containerColor = EpicureanPrimary),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.height(54.dp).width(180.dp)
                    ) {
                        Text("Ajouter au panier", style = EpicureanTypography.titleMedium, color = Color.White)
                    }
                }
            }
        }
    }
}
