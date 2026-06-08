package com.teamsasa.buonappetito.ui.order

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.teamsasa.buonappetito.ui.theme.*

@Composable
fun QrScannerScreen(onQrCodeScanned: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.8f))) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Scanner le QR Code", style = EpicureanTypography.titleLarge, color = Color.White)
                Text("Veuillez scanner le code présent sur votre table", style = EpicureanTypography.bodyLarge, color = Color.White.copy(alpha = 0.7f), modifier = Modifier.padding(top = 8.dp))
            }

            Box(
                modifier = Modifier
                    .size(260.dp)
                    .border(BorderStroke(4.dp, Color.White), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {}

            Button(
                onClick = { onQrCodeScanned("Table 12") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = TextDark)
            ) {
                Text("Simuler le scan (Table 12)", style = EpicureanTypography.titleMedium)
            }
        }
    }
}
