package com.teamsasa.buonappetito.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.teamsasa.buonappetito.ui.theme.EpicureanPrimary
import com.teamsasa.buonappetito.ui.theme.TextMuted

data class NavItem(val route: String, val icon: ImageVector, val label: String)

@Composable
fun EpicureanBottomNavigation(currentScreen: String, onScreenSelected: (String) -> Unit) {
    val items = listOf(
        NavItem("home", Icons.Outlined.Home, "Accueil"),
        NavItem("menu", Icons.Outlined.Restaurant, "Menu"),
        NavItem("cart", Icons.Outlined.ShoppingCart, "Panier"),
        NavItem("history", Icons.Outlined.History, "Historique"),
        NavItem("profile", Icons.Outlined.Person, "Profil")
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(80.dp)
    ) {
        items.forEach { item ->
            val isSelected = currentScreen == item.route
            
            NavigationBarItem(
                icon = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) EpicureanPrimary.copy(alpha = 0.15f) else Color.Transparent)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(
                                item.icon, 
                                contentDescription = item.label,
                                tint = if (isSelected) EpicureanPrimary else TextMuted
                            )
                            if (isSelected) {
                                Text(
                                    text = item.label,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = EpicureanPrimary
                                )
                            }
                        }
                    }
                },
                selected = isSelected,
                onClick = { onScreenSelected(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                ),
                alwaysShowLabel = false
            )
        }
    }
}
