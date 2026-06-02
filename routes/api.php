<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\MenuController;
use App\Http\Controllers\Api\CartController;
use App\Http\Controllers\Api\OrderController;
use App\Livewire\KitchenDashboard; // Ou juste la vue si tu passes par Volt directement

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
*/

// Routes publiques (Authentification)
Route::post('/login', [AuthController::class, 'login']);
Route::post('/register', [AuthController::class, 'register']);
Route::get('/menu', [MenuController::class, 'getAllDishes']); // Carte complète triée
Route::get('/menu/popular', [MenuController::class, 'getPopularDishes']);
Route::get('/kitchen', KitchenDashboard::class);
use App\Http\Controllers\Api\OrderApiController;

// --- ROUTES PROTÉGÉES (Sanctum) ---

Route::middleware('auth:sanctum')->group(function () {
    
    // Tout client ou personnel connecté peut commander et suivre l'historique
    Route::post('/orders', [OrderController::class, 'createOrder']);
    Route::get('/orders/history', [OrderController::class, 'getOrderHistory']);
    Route::get('/orders/{id}/track', [OrderController::class, 'trackOrder']);
    Route::post('/orders', [OrderApiController::class, 'store']);

    // Routes restreintes au personnel (Serveur, Cuisinier, Admin)
    Route::middleware('role:server,cook,admin')->group(function () {
        // Le personnel peut modifier l'état d'une table ou valider des flux complexes
    });
});