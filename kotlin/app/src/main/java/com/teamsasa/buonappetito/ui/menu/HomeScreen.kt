package com.teamsasa.buonappetito.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.teamsasa.buonappetito.data.model.Dish
import com.teamsasa.buonappetito.data.model.User
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.utils.formatPrice
import com.teamsasa.buonappetito.viewmodel.AuthViewModel
import com.teamsasa.buonappetito.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: MenuViewModel, authViewModel: AuthViewModel, onNavigateToDetail: (Long) -> Unit) {
    val dishes: List<Dish> by viewModel.dishes.collectAsStateWithLifecycle()
    val currentUser: User? by authViewModel.currentUser.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EpicureanBg)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Bonjour, ${currentUser?.name ?: "Client"}", 
            style = EpicureanTypography.displayLarge, 
            color = TextDark
        )
        Text(
            text = "Qu'est-ce qui vous ferait plaisir aujourd'hui ?", 
            style = EpicureanTypography.bodyLarge, 
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Rechercher des plats...", style = EpicureanTypography.bodyLarge) },
            leadingIcon = { Icon(painter = painterResource(id = android.R.drawable.ic_menu_search), contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedBorderColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(EpicureanPrimary)
        ) {
            Column(modifier = Modifier.padding(20.dp).align(Alignment.CenterStart)) {
                Text("Livraison Gratuite\nAujourd'hui", style = EpicureanTypography.titleLarge, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = EpicureanPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("En profiter", style = EpicureanTypography.labelLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Plats populaires", style = EpicureanTypography.titleLarge, color = TextDark)
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(dishes, key = { it.id }) { dish ->
                PopularDishCard(dish = dish, onClick = { onNavigateToDetail(dish.id) })
            }
        }
    }
}

@Composable
fun PopularDishCard(dish: Dish, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(210.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            AsyncImage(
                model = dish.imageUrl,
                contentDescription = dish.name,
                modifier = Modifier.fillMaxWidth().height(120.dp).background(Color.LightGray),
                contentScale = ContentScale.Crop,
                error = painterResource(id = android.R.drawable.ic_menu_report_image),
                placeholder = painterResource(id = android.R.drawable.ic_menu_gallery)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = dish.name, style = EpicureanTypography.titleMedium, color = TextDark)
                Text(text = dish.description, style = EpicureanTypography.bodySmall, color = TextMuted, maxLines = 2)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = dish.price.formatPrice(), style = EpicureanTypography.titleMedium, color = EpicureanAccent)
                    Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(10.dp)).background(EpicureanPrimary), contentAlignment = Alignment.Center) {
                        Text("+", color = Color.White, style = EpicureanTypography.titleMedium)
                    }
                }
            }
        }
    }
}
