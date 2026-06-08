package com.teamsasa.buonappetito.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.teamsasa.buonappetito.ui.theme.*

@Composable
fun EpicureanBottomNavigation(
    currentScreen: String,
    onScreenSelected: (String) -> Unit
) {
    val items = listOf(
        NavigationItem("home", "Accueil", android.R.drawable.ic_menu_view),
        NavigationItem("menu", "Menu", android.R.drawable.ic_menu_agenda),
        NavigationItem("cart", "Panier", android.R.drawable.btn_star_big_on),
        NavigationItem("profile", "Profil", android.R.drawable.ic_menu_my_calendar)
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentScreen == item.route

                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) EpicureanPrimary.copy(alpha = 0.15f) else Color.Transparent,
                    label = "OvaleBackground"
                )
                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) EpicureanPrimary else TextMuted,
                    label = "OvaleContent"
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(32.dp))
                        .background(backgroundColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onScreenSelected(item.route) }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = item.iconRes),
                            contentDescription = item.title,
                            tint = contentColor,
                            modifier = Modifier.size(24.dp)
                        )
                        if (isSelected) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.title,
                                color = contentColor,
                                style = EpicureanTypography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

data class NavigationItem(val route: String, val title: String, val iconRes: Int)
