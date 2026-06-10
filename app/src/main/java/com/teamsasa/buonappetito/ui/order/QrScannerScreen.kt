package com.teamsasa.buonappetito.ui.order

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScannerScreen(onQrCodeScanned: (String) -> Unit, onBack: () -> Unit = {}) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scanner QR Code", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Black.copy(alpha = 0.8f)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Scanner le code de la table", 
                        style = EpicureanTypography.titleLarge, 
                        color = Color.White
                    )
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
                        .border(BorderStroke(2.dp, EpicureanPrimary), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Visual guide for scanning frame
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)), RoundedCornerShape(12.dp))
                    )
                    
                    // Add corner accents to the frame
                    Box(modifier = Modifier.size(260.dp)) {
                        val cornerSize = 40.dp
                        val thickness = 4.dp
                        // Top Left
                        Box(modifier = Modifier.size(cornerSize, thickness).background(EpicureanPrimary, RoundedCornerShape(topStart = thickness)).align(Alignment.TopStart))
                        Box(modifier = Modifier.size(thickness, cornerSize).background(EpicureanPrimary, RoundedCornerShape(topStart = thickness)).align(Alignment.TopStart))
                        // Top Right
                        Box(modifier = Modifier.size(cornerSize, thickness).background(EpicureanPrimary, RoundedCornerShape(topEnd = thickness)).align(Alignment.TopEnd))
                        Box(modifier = Modifier.size(thickness, cornerSize).background(EpicureanPrimary, RoundedCornerShape(topEnd = thickness)).align(Alignment.TopEnd))
                        // Bottom Left
                        Box(modifier = Modifier.size(cornerSize, thickness).background(EpicureanPrimary, RoundedCornerShape(bottomStart = thickness)).align(Alignment.BottomStart))
                        Box(modifier = Modifier.size(thickness, cornerSize).background(EpicureanPrimary, RoundedCornerShape(bottomStart = thickness)).align(Alignment.BottomStart))
                        // Bottom Right
                        Box(modifier = Modifier.size(cornerSize, thickness).background(EpicureanPrimary, RoundedCornerShape(bottomEnd = thickness)).align(Alignment.BottomEnd))
                        Box(modifier = Modifier.size(thickness, cornerSize).background(EpicureanPrimary, RoundedCornerShape(bottomEnd = thickness)).align(Alignment.BottomEnd))
                    }
                }

                Button(
                    onClick = { scanLauncher.launch(scanOptions) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).height(54.dp),
                    shape = RoundedCornerShape(27.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EpicureanPrimary, contentColor = Color.White)
                ) {
                    Text(text = "Démarrer le scan", style = EpicureanTypography.titleMedium)
                }
            }
        }
    }
}
