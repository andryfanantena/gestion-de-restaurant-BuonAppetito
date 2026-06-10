<?php
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\MenuController;
use App\Http\Controllers\Api\OrderController;
use App\Http\Controllers\Api\PaymentController;
use App\Http\Controllers\Api\ReviewController;
use App\Http\Controllers\Api\LoyaltyController;
use App\Http\Controllers\Api\TicketController;

// ── Publiques ──────────────────────────────────────────────────────────────────
Route::post('/register', [AuthController::class, 'register']);
Route::post('/login',    [AuthController::class, 'login']);
Route::get('/menu',         [MenuController::class, 'getAllDishes']);
Route::get('/menu/popular', [MenuController::class, 'getPopularDishes']);

// ── Protégées Sanctum ──────────────────────────────────────────────────────────
Route::middleware('auth:sanctum')->group(function () {

    Route::post('/logout', [AuthController::class, 'logout']);

    // Commandes
    Route::post('/orders',                  [OrderController::class, 'createOrder']);
    Route::get('/orders/history',           [OrderController::class, 'getOrderHistory']);
    Route::get('/orders/{id}/track',        [OrderController::class, 'trackOrder']);
    Route::patch('/orders/{id}/status',     [OrderController::class, 'updateStatus']);

    // ── J3 : Ajouter plats pendant commande en cours ──────────────────────────
    Route::post('/orders/{id}/add-items',   [OrderController::class, 'addItems']);

    // ── J3 : Notation ─────────────────────────────────────────────────────────
    Route::post('/orders/{id}/review',      [ReviewController::class, 'store']);

    // ── J3 : Stripe ───────────────────────────────────────────────────────────
    Route::post('/payments/create-intent',  [PaymentController::class, 'createIntent']);
    Route::post('/payments/confirm',        [PaymentController::class, 'confirm']);

    // ── J3 : Fidélité ────────────────────────────────────────────────────────
    Route::get('/loyalty',                  [LoyaltyController::class, 'index']);

    // ── J3 : Ticket PDF/HTML ──────────────────────────────────────────────────
    Route::get('/orders/{id}/ticket',       [TicketController::class, 'show']);

    // Dashboard cuisine
    Route::middleware('role:cook,server,admin')->group(function () {
        Route::get('/kitchen/orders',       [OrderController::class, 'kitchenOrders']);
    });
});
