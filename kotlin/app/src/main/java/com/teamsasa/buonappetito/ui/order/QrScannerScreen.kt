package com.teamsasa.buonappetito.ui.order

import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.teamsasa.buonappetito.ui.theme.*
import java.util.concurrent.Executors

@ExperimentalGetImage
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScannerScreen(onQrCodeScanned: (String) -> Unit, onBack: () -> Unit = {}) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    
    var isScanningActive by remember { mutableStateOf(false) }

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
        containerColor = Color.Black.copy(alpha = 0.9f)
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
                        text = "Veuillez placer le code dans le carré ci-dessous", 
                        style = EpicureanTypography.bodyLarge, 
                        color = Color.White.copy(alpha = 0.7f), 
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.DarkGray.copy(alpha = 0.5f))
                        .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (isScanningActive) {
                        AndroidView(
                            factory = { ctx ->
                                val previewView = PreviewView(ctx)
                                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                                cameraProviderFuture.addListener({
                                    val cameraProvider = cameraProviderFuture.get()
                                    val preview = Preview.Builder().build().also {
                                        it.setSurfaceProvider(previewView.surfaceProvider)
                                    }

                                    val barcodeScanner = BarcodeScanning.getClient()
                                    val imageAnalysis = ImageAnalysis.Builder()
                                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                        .build()

                                    imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy: ImageProxy ->
                                        val mediaImage = imageProxy.image
                                        if (mediaImage != null) {
                                            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                                            barcodeScanner.process(image)
                                                .addOnSuccessListener { barcodes ->
                                                    for (barcode in barcodes) {
                                                        barcode.rawValue?.let { 
                                                            onQrCodeScanned(it)
                                                            isScanningActive = false 
                                                        }
                                                    }
                                                }
                                                .addOnCompleteListener {
                                                    imageProxy.close()
                                                }
                                        } else {
                                            imageProxy.close()
                                        }
                                    }

                                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                                    try {
                                        cameraProvider.unbindAll()
                                        cameraProvider.bindToLifecycle(
                                            lifecycleOwner,
                                            cameraSelector,
                                            preview,
                                            imageAnalysis
                                        )
                                    } catch (exc: Exception) {
                                        // Handle exceptions
                                    }
                                }, ContextCompat.getMainExecutor(ctx))
                                previewView
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Add corner accents to the frame (White instead of Blue/EpicureanPrimary)
                    Box(modifier = Modifier.size(280.dp)) {
                        val cornerSize = 40.dp
                        val thickness = 4.dp
                        // Top Left
                        Box(modifier = Modifier.size(cornerSize, thickness).background(Color.White, RoundedCornerShape(topStart = thickness)).align(Alignment.TopStart))
                        Box(modifier = Modifier.size(thickness, cornerSize).background(Color.White, RoundedCornerShape(topStart = thickness)).align(Alignment.TopStart))
                        // Top Right
                        Box(modifier = Modifier.size(cornerSize, thickness).background(Color.White, RoundedCornerShape(topEnd = thickness)).align(Alignment.TopEnd))
                        Box(modifier = Modifier.size(thickness, cornerSize).background(Color.White, RoundedCornerShape(topEnd = thickness)).align(Alignment.TopEnd))
                        // Bottom Left
                        Box(modifier = Modifier.size(cornerSize, thickness).background(Color.White, RoundedCornerShape(bottomStart = thickness)).align(Alignment.BottomStart))
                        Box(modifier = Modifier.size(thickness, cornerSize).background(Color.White, RoundedCornerShape(bottomStart = thickness)).align(Alignment.BottomStart))
                        // Bottom Right
                        Box(modifier = Modifier.size(cornerSize, thickness).background(Color.White, RoundedCornerShape(bottomEnd = thickness)).align(Alignment.BottomEnd))
                        Box(modifier = Modifier.size(thickness, cornerSize).background(Color.White, RoundedCornerShape(bottomEnd = thickness)).align(Alignment.BottomEnd))
                    }
                }

                if (!isScanningActive) {
                    Button(
                        onClick = { isScanningActive = true },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).height(54.dp),
                        shape = RoundedCornerShape(27.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EpicureanPrimary, contentColor = Color.White)
                    ) {
                        Text(text = "Démarrer le scan", style = EpicureanTypography.titleMedium)
                    }
                } else {
                    Spacer(modifier = Modifier.height(78.dp))
                }
            }
        }
    }
}
