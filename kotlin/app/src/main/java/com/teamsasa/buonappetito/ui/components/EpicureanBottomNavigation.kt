package com.teamsasa.buonappetito.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.teamsasa.buonappetito.ui.theme.EpicureanPrimary
import com.teamsasa.buonappetito.ui.theme.TextMuted

data class NavItem(val route: String, val icon: ImageVector, val label: String)

@Composable
fun EpicureanBottomNavigation(currentScreen: String, onScreenSelected: (String) -> Unit) {
    val items = listOf(
        NavItem("home", Icons.Default.Home, "Accueil"),
        NavItem("menu", Icons.AutoMirrored.Filled.List, "Menu"),
        NavItem("cart", Icons.Default.ShoppingCart, "Panier"),
        NavItem("profile", Icons.Default.Person, "Profil")
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentScreen == item.route,
                onClick = { onScreenSelected(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = EpicureanPrimary,
                    selectedTextColor = EpicureanPrimary,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted,
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}
