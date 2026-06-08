package com.teamsasa.buonappetito.ui.payement


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.rememberPaymentSheet
import com.teamsasa.buonappetito.ui.theme.*
import com.teamsasa.buonappetito.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    orderId: Long,
    totalPrice: Double,
    viewModel: OrderViewModel,
    onPaymentSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var convives by remember { mutableIntStateOf(1) }
    var isProcessing by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Calcul du prix par personne selon le nombre de convives
    val pricePerPerson = totalPrice / convives

    // Initialisation du PaymentSheet de Stripe
    val paymentSheet = rememberPaymentSheet { paymentResult ->
        isProcessing = false
        when (paymentResult) {
            is PaymentSheet.PaymentResult.Completed -> {
                onPaymentSuccess()
            }
            is PaymentSheet.PaymentResult.Failed -> {
                errorMessage = paymentResult.error.localizedMessage ?: "Le paiement a échoué"
            }
            is PaymentSheet.PaymentResult.Canceled -> {
                // Paiement annulé par l'utilisateur, rien à faire de spécial
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Paiement de la commande", style = EpicureanTypography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack, enabled = !isProcessing) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EpicureanBg)
            )
        },
        containerColor = EpicureanBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Souhaitez-vous diviser l'addition ?",
                style = EpicureanTypography.titleMedium,
                color = TextDark,
                fontWeight = FontWeight.Bold
            )

            // ── Sélection du nombre de convives ─────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nombre de personnes",
                        style = EpicureanTypography.bodyLarge,
                        color = TextDark
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FilledIconButton(
                            onClick = { if (convives > 1) convives-- },
                            enabled = convives > 1 && !isProcessing,
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = EpicureanBg)
                        ) {
                            Text("-", style = EpicureanTypography.titleLarge, color = TextDark)
                        }

                        Text(
                            text = convives.toString(),
                            style = EpicureanTypography.titleLarge,
                            color = TextDark,
                            fontWeight = FontWeight.Bold
                        )

                        FilledIconButton(
                            onClick = { convives++ },
                            enabled = !isProcessing,
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = EpicureanBg)
                        ) {
                            Text("+", style = EpicureanTypography.titleLarge, color = TextDark)
                        }
                    }
                }
            }

            // ── Résumé du montant ───────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EpicureanPrimary.copy(alpha = 0.08f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (convives > 1) "Votre part à payer" else "Total de l'addition",
                        style = EpicureanTypography.bodyMedium,
                        color = TextMuted
                    )
                    Text(
                        text = String.format("%.2f €", pricePerPerson),
                        style = EpicureanTypography.displayLarge,
                        color = EpicureanPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    if (convives > 1) {
                        Text(
                            text = String.format("Sur un total de %.2f €", totalPrice),
                            style = EpicureanTypography.bodySmall,
                            color = TextMuted
                        )
                    }
                }
            }

            // Affichage de l'erreur si le paiement échoue
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = EpicureanTypography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // ── Bouton d'action de paiement ─────────────────────────────────────
            Button(
                onClick = {
                    isProcessing = true
                    errorMessage = null

                    // Appel réseau via le ViewModel pour récupérer le client_secret de Stripe
                    viewModel.createPaymentIntent(orderId, convives) { response ->
                        if (response != null) {
                            // Configuration de la clé publique Stripe
                            PaymentConfiguration.init(context, "pk_test_votre_cle_publique_stripe")

                            // Présentation du formulaire natif Stripe
                            paymentSheet.presentWithPaymentIntent(
                                paymentIntentClientSecret = response.clientSecret,
                                configuration = PaymentSheet.Configuration(
                                    merchantDisplayName = "Buon Appetito"
                                )
                            )
                        } else {
                            isProcessing = false
                            errorMessage = "Impossible d'initialiser le paiement avec le serveur."
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EpicureanPrimary),
                enabled = !isProcessing
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (convives > 1) "Payer ma part" else "Procéder au paiement",
                        style = EpicureanTypography.titleMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}