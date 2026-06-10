<?php
namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\LoyaltyPoint;
use App\Models\Order;
use App\Models\Payment;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class PaymentController extends Controller
{
    /**
     * POST /api/payments/create-intent
     * Reçu du Kotlin : { order_id, convives }
     * Retourne      : { client_secret, amount, currency }
     *
     * NOTE : Sans clé Stripe réelle, on génère un faux client_secret
     *        pour que le flux de démo fonctionne.
     *        En production : remplacer par Stripe\PaymentIntent::create()
     */
    public function createIntent(Request $request): JsonResponse
    {
        $request->validate([
            'order_id' => 'required|integer|exists:orders,id',
            'convives' => 'required|integer|min:1|max:20',
        ]);

        $order    = Order::findOrFail($request->order_id);
        $convives = $request->convives;

        // Calcul du montant par personne (en centimes pour Stripe)
        $totalCentimes      = (int) round($order->total_price * 100);
        $amountPerPerson    = (int) round($totalCentimes / $convives);

        // ── Production : décommentez ce bloc et ajoutez stripe/stripe-php au composer ──
        // \Stripe\Stripe::setApiKey(config('services.stripe.secret'));
        // $intent = \Stripe\PaymentIntent::create([
        //     'amount'   => $amountPerPerson,
        //     'currency' => 'eur',
        //     'metadata' => ['order_id' => $order->id, 'convives' => $convives],
        // ]);
        // $clientSecret = $intent->client_secret;
        // ──────────────────────────────────────────────────────────────────────────────

        // ── Démo sans clé Stripe réelle ───────────────────────────────────────
        $clientSecret = 'pi_demo_' . $order->id . '_' . $convives . '_secret_buonappetito';
        // ─────────────────────────────────────────────────────────────────────

        // Enregistrer le payment en attente
        Payment::updateOrCreate(
            ['order_id' => $order->id],
            [
                'stripe_payment_intent_id' => $clientSecret,
                'amount'                   => $order->total_price,
                'convives'                 => $convives,
                'amount_per_person'        => $order->total_price / $convives,
                'payment_method'           => 'STRIPE',
                'status'                   => 'PENDING',
            ]
        );

        return response()->json([
            'client_secret' => $clientSecret,
            'amount'        => $amountPerPerson,
            'currency'      => 'eur',
        ], 200);
    }

    /**
     * POST /api/payments/confirm
     * Reçu du Kotlin : { payment_intent_id, order_id }
     * Retourne      : { success, message }
     */
    public function confirm(Request $request): JsonResponse
    {
        $request->validate([
            'payment_intent_id' => 'required|string',
            'order_id'          => 'required|integer|exists:orders,id',
        ]);

        $order   = Order::findOrFail($request->order_id);
        $payment = Payment::where('order_id', $order->id)->first();

        if ($payment) {
            $payment->update(['status' => 'COMPLETED']);
        }

        // Passer la commande en DELIVERED après paiement
        $order->update(['status' => 'DELIVERED']);

        // Libérer la table
        if ($order->restaurant_table_id) {
            \App\Models\RestaurantTable::find($order->restaurant_table_id)
                ?->update(['status' => 'free']);
        }

        // ── Créditer les points de fidélité (1 pt par 1000 Ar) ──────────────
        $points = (int) floor($order->total_price / 1000);
        if ($points > 0) {
            LoyaltyPoint::addPoints($request->user()->id, $points);
        }

        return response()->json([
            'success' => true,
            'message' => 'Paiement confirmé. ' . $points . ' points fidélité crédités.',
        ], 200);
    }
}
