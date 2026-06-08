package com.teamsasa.buonappetito.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.utils.formatPrice
import com.teamsasa.buonappetito.viewmodel.MenuViewModel

@Composable
fun MenuScreen(viewModel: MenuViewModel, onDishClick: (Long) -> Unit) {
    val dishes by viewModel.dishes.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    val categories = listOf("All", "Burgers", "Pizza", "Pasta", "Desserts", "Boissons")

    Column(modifier = Modifier.fillMaxSize().background(EpicureanBg).padding(16.dp)) {
        Text(
            text = "Notre Carte",
            style = EpicureanTypography.titleLarge,
            color = TextDark,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Chips de filtre catégories
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            items(categories) { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { viewModel.setCategory(cat) },
                    label = { Text(cat, style = EpicureanTypography.labelLarge) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EpicureanPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(dishes, key = { it.id }) { dish ->
                Card(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    onClick = { onDishClick(dish.id) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = dish.imageUrl,
                            contentDescription = dish.name,
                            modifier = Modifier.size(76.dp).clip(RoundedCornerShape(14.dp)).background(Color.LightGray),
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = android.R.drawable.ic_menu_report_image),
                            placeholder = painterResource(id = android.R.drawable.ic_menu_gallery)
                        )
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