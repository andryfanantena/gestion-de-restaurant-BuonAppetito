package com.teamsasa.buonappetito.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun HomeScreen(
    viewModel: MenuViewModel,
    authViewModel: AuthViewModel,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToProfile: () -> Unit = {}
) {
    val dishes: List<Dish> by viewModel.dishes.collectAsStateWithLifecycle()
    val currentUser: User? by authViewModel.currentUser.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EpicureanBg)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header: App Name and Profile Icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Buon Appetito",
                style = EpicureanTypography.titleLarge.copy(fontSize = 24.sp),
                color = EpicureanPrimary,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = onNavigateToProfile,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(EpicureanPrimary.copy(alpha = 0.1f))
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profil",
                    tint = EpicureanPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

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
                .height(130.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(EpicureanPrimary)
        ) {
            Column(modifier = Modifier.padding(20.dp).align(Alignment.CenterStart)) {
                Text("Livraison Gratuite\nAujourd'hui", style = EpicureanTypography.titleLarge.copy(fontSize = 18.sp), color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = EpicureanPrimary),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("En profiter", style = EpicureanTypography.labelLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Plats populaires", style = EpicureanTypography.titleLarge, color = TextDark)
        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
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
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp), // Fixed height to ensure all cards are the same size
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = dish.imageUrl,
                contentDescription = dish.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop,
                error = painterResource(id = android.R.drawable.ic_menu_report_image),
                placeholder = painterResource(id = android.R.drawable.ic_menu_gallery)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = dish.name, 
                    style = EpicureanTypography.titleMedium, 
                    color = TextDark,
                    maxLines = 1
                )
                Text(
                    text = dish.description, 
                    style = EpicureanTypography.bodySmall, 
                    color = TextMuted, 
                    maxLines = 2,
                    modifier = Modifier.height(32.dp) // Fixed height for description area
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(), 
                    horizontalArrangement = Arrangement.SpaceBetween, 
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dish.price.formatPrice(), 
                        style = EpicureanTypography.titleMedium, 
                        color = EpicureanAccent,
                        fontSize = 14.sp
                    )
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(EpicureanPrimary), 
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", color = Color.White, style = EpicureanTypography.titleMedium)
                    }
                }
            }
        }
    }
}
