<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\MenuController;
use App\Http\Controllers\Api\CartController;
use App\Http\Controllers\Api\OrderController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
*/

// Routes publiques (Authentification)
Route::post('/login', [AuthController::class, 'login']);
Route::post('/register', [AuthController::class, 'register']);

// Routes publiques pour la consultation du menu d'affichage Android
Route::get('/menu/popular', [MenuController::class, 'getPopularDishes']);
Route::get('/menu', [MenuController::class, 'getAllDishes']);

// Routes protégées par Laravel Sanctum (Jeton Bearer requis)
Route::middleware('auth:sanctum')->group(function () {
    
    // Commandes
    Route::post('/orders', [OrderController::class, 'createOrder']);
    Route::get('/orders/history', [OrderController::class, 'getOrderHistory']);
    Route::get('/orders/{id}/track', [OrderController::class, 'trackOrder']);

    // Panier optionnel
    Route::post('/cart/sync', [CartController::class, 'syncCart']);
});