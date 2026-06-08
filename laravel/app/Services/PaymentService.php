<?php

namespace App\Services;

class PaymentService
{
    public function processPayment(float $amount, string $currency = 'MGA'): bool
    {
        // Traitement fictif d'une passerelle locale malgache (ex: Mvola, Orange Money, AirtelMoney)
        if ($amount > 0) {
            return true;
        }
        return false;
    }
}