package com.teamsasa.buonappetito.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.teamsasa.buonappetito.data.model.Dish
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.utils.formatPrice
import com.teamsasa.buonappetito.viewmodel.CartViewModel

@Composable
fun DishDetailScreen(dish: Dish, cartViewModel: CartViewModel, onBack: () -> Unit) {
    var quantity by remember { mutableIntStateOf(1) }
    var comment by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(EpicureanBg)) {
        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            AsyncImage(
                model = dish.imageUrl,
                contentDescription = dish.name,
                modifier = Modifier.fillMaxSize().background(Color.LightGray),
                contentScale = ContentScale.Crop,
                error = painterResource(id = android.R.drawable.ic_menu_report_image),
                placeholder = painterResource(id = android.R.drawable.ic_menu_gallery)
            )
            Button(
                onClick = onBack,
                modifier = Modifier.padding(16.dp).size(40.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = TextDark),
                contentPadding = PaddingValues(0.dp)
            ) { Text("<") }
        }

        Surface(
            modifier = Modifier.fillMaxWidth().weight(1.5f),
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
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(text = "Instructions spéciales", style = EpicureanTypography.titleMedium, color = TextDark)
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    placeholder = { Text("Ex: sans oignons, bien cuit...", style = EpicureanTypography.bodyMedium) },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                )

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
                        onClick = { 
                            cartViewModel.addToCart(dish, quantity, comment)
                            onBack()
                        },
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
