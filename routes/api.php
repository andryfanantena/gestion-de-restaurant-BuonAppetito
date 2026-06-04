<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\MenuController;
use App\Http\Controllers\Api\OrderController;

/*
|--------------------------------------------------------------------------
| API Routes — BuonAppetito
|--------------------------------------------------------------------------
*/

// ── Routes publiques ──────────────────────────────────────────────────────
Route::post('/register', [AuthController::class, 'register']);
Route::post('/login',    [AuthController::class, 'login']);

// Menu public (le front l'appelle sans token)
Route::get('/menu',         [MenuController::class, 'getAllDishes']);
Route::get('/menu/popular', [MenuController::class, 'getPopularDishes']);

// ── Routes protégées par Sanctum ──────────────────────────────────────────
Route::middleware('auth:sanctum')->group(function () {

    // Auth
    Route::post('/logout', [AuthController::class, 'logout']);

    // Commandes
    Route::post('/orders',            [OrderController::class, 'createOrder']);
    Route::get('/orders/history',     [OrderController::class, 'getOrderHistory']);
    Route::get('/orders/{id}/track',  [OrderController::class, 'trackOrder']);
    Route::patch('/orders/{id}/status', [OrderController::class, 'updateStatus']);

    // Routes cuisine/caisse (personnel connecté)
    Route::middleware('role:cook,server,admin')->group(function () {
        Route::get('/kitchen/orders', [OrderController::class, 'kitchenOrders']);
    });
});
