package com.teamsasa.buonappetito.ui.order

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.teamsasa.buonappetito.ui.theme.*

@Composable
fun QrScannerScreen(onQrCodeScanned: (String) -> Unit) {
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = { result ->
            result.contents?.let { onQrCodeScanned(it) }
        }
    )

    val scanOptions = ScanOptions().apply {
        setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        setPrompt("Scannez le QR Code de votre table")
        setBeepEnabled(true)
        setOrientationLocked(false)
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.8f))) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Scanner le QR Code", style = EpicureanTypography.titleLarge, color = Color.White)
                Text(
                    text = "Veuillez scanner le code présent sur votre table", 
                    style = EpicureanTypography.bodyLarge, 
                    color = Color.White.copy(alpha = 0.7f), 
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(260.dp)
                    .border(BorderStroke(4.dp, Color.White), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Overlay visuel du scanner
            }

            Button(
                onClick = { scanLauncher.launch(scanOptions) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = TextDark)
            ) {
                Text(text = "Démarrer le scan", style = EpicureanTypography.titleMedium)
            }
        }
    }
}
