package com.teamsasa.buonappetito.utils

fun Double.formatPrice(): String {
    // Formatage personnalisé pour l'Ariary (ex: 25 000 Ar)
    return String.format("%,.0f Ar", this).replace(",", " ")
}

