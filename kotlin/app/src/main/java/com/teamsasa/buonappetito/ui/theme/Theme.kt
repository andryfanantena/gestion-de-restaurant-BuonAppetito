package com.teamsasa.buonappetito.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = EpicureanPrimary,
    background = EpicureanBg,
    surface = CardBg
)

@Composable
fun EpicureanTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = EpicureanTypography,
        content = content
    )
}