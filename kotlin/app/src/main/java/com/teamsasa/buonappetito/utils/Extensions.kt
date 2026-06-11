package com.teamsasa.buonappetito.utils

import java.text.NumberFormat
import java.util.*

/**
 * Formate un Double en prix avec la devise Ariary (Ar)
 */
fun Double.formatPrice(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("fr", "MG"))
    // Personnalisation pour afficher "Ar" au lieu du symbole standard si nécessaire
    return format.format(this).replace("MGA", "Ar").replace("FMG", "Ar")
}
