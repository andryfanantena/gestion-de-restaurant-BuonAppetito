<?php

namespace App\Services;

class QrCodeService
{
    public function validateTableQrCode(string $qrCodePayload): ?string
    {
        // Exemple de parsing : l'application Kotlin envoie la chaîne de caractères brute "Table 12"
        if (str_starts_with($qrCodePayload, 'Table ')) {
            return $qrCodePayload; 
        }
        return null;
    }
}