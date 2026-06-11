<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;

class CartController extends Controller
{
    // Selon l'annotation de votre architecture, ce controleur est facultatif car la synchronisation 
    // s'opère en mémoire locale Jetpack Compose (CartViewModel).
    public function syncCart(Request $request)
    {
        return response()->json(['success' => true, 'message' => 'Panier synchronisé localement'], 200);
    }
}