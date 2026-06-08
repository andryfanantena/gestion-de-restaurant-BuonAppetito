package com.teamsasa.buonappetito.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.teamsasa.buonappetito.data.model.Dish
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.utils.formatPrice
import com.teamsasa.buonappetito.viewmodel.MenuViewModel

@Composable
fun MenuScreen(viewModel: MenuViewModel, onDishClick: (Long) -> Unit) {
    val dishes by viewModel.dishes.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(EpicureanBg).padding(16.dp)) {
        Text("Notre Carte", style = EpicureanTypography.titleLarge, color = TextDark, modifier = Modifier.padding(bottom = 16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(dishes) { dish ->
                Card(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    onClick = { onDishClick(dish.id) }
                ) {
                    Row(modifier = Modifier.fillMaxSize().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(76.dp).clip(RoundedCornerShape(14.dp)).background(Color.LightGray))

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = dish.name, style = EpicureanTypography.titleMedium, color = TextDark)
                            Text(text = dish.description, style = EpicureanTypography.bodyLarge, maxLines = 1)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = dish.price.formatPrice(), style = EpicureanTypography.titleMedium, color = EpicureanAccent)
                        }
                    }
                }
            }
        }
    }
}
