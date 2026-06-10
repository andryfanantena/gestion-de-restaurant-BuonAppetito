<?php
namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\LoyaltyPoint;
use App\Models\Order;
use App\Models\Review;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class ReviewController extends Controller
{
    /**
     * POST /api/orders/{id}/review
     * Reçu du Kotlin : { order_id, rating, comment }
     * Retourne      : { success, message }
     */
    public function store(Request $request, int $id): JsonResponse
    {
        $request->validate([
            'rating'  => 'required|integer|min:1|max:5',
            'comment' => 'nullable|string|max:300',
        ]);

        $order = Order::find($id);
        if (!$order) {
            return response()->json(['success' => false, 'message' => 'Commande introuvable.'], 404);
        }

        // Vérifier que c'est bien la commande de l'utilisateur connecté
        if ($order->user_id !== $request->user()->id) {
            return response()->json(['success' => false, 'message' => 'Non autorisé.'], 403);
        }

        // Empêcher les avis en double
        if (Review::where('order_id', $id)->where('user_id', $request->user()->id)->exists()) {
            return response()->json(['success' => false, 'message' => 'Vous avez déjà laissé un avis.'], 409);
        }

        Review::create([
            'order_id' => $id,
            'user_id'  => $request->user()->id,
            'rating'   => $request->rating,
            'comment'  => $request->comment ?? '',
        ]);

        // Bonus fidélité pour avoir laissé un avis
        LoyaltyPoint::addPoints($request->user()->id, 50);

        return response()->json([
            'success' => true,
            'message' => 'Avis enregistré. +50 points fidélité !',
        ], 201);
    }
}
