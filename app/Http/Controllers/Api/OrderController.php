<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Dish;
use App\Models\Order;
use App\Models\OrderItem;
use App\Models\RestaurantTable;
use App\Events\OrderPlaced;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Str;

class OrderController extends Controller
{
    /**
     * POST /api/orders
     * Body attendu (CheckoutRequest Kotlin) :
     *   { table_number: "Table 3", items: [{ dish_id, quantity, comment }] }
     *
     * Réponse attendue (Order Kotlin) :
     *   { id, order_number, items, total_price, status, date, table_number }
     */
    public function createOrder(Request $request): JsonResponse
    {
        $request->validate([
            'table_number'       => 'nullable|string|max:50',
            'items'              => 'required|array|min:1',
            'items.*.dish_id'    => 'required|integer|exists:dishes,id',
            'items.*.quantity'   => 'required|integer|min:1',
            'items.*.comment'    => 'nullable|string|max:255',
        ]);

        DB::beginTransaction();
        try {
            $totalPrice  = 0.0;
            $orderNumber = 'BA-' . strtoupper(Str::random(6));

            // Cherche la table par son numéro (ex: "Table 3")
            $table = null;
            if ($request->filled('table_number')) {
                $table = RestaurantTable::where('table_number', $request->table_number)->first();
            }

            $order = Order::create([
                'user_id'               => $request->user()->id,
                'restaurant_table_id'   => $table?->id,
                'order_number'          => $orderNumber,
                'status'                => 'PENDING',
                'total_price'           => 0,
                'notes'                 => null,
            ]);

            foreach ($request->items as $item) {
                $dish        = Dish::findOrFail($item['dish_id']);
                $totalPrice += $dish->price * $item['quantity'];

                OrderItem::create([
                    'order_id' => $order->id,
                    'dish_id'  => $dish->id,
                    'quantity' => $item['quantity'],
                    'price'    => $dish->price,
                    'comment'  => $item['comment'] ?? null,
                ]);
            }

            $order->update(['total_price' => $totalPrice]);

            // Passer la table en OCCUPIED
            if ($table) {
                $table->update(['status' => 'occupied']);
            }

            DB::commit();

            // Broadcast Pusher vers le dashboard cuisine
            broadcast(new OrderPlaced($order->load('items.dish')))->toOthers();

            return response()->json($this->formatOrder($order->load('items.dish')), 201);

        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json([
                'message' => 'Erreur serveur : ' . $e->getMessage()
            ], 500);
        }
    }

    /**
     * GET /api/orders/history
     * Retourne la liste des commandes de l'utilisateur connecté (List<Order> Kotlin)
     */
    public function getOrderHistory(Request $request): JsonResponse
    {
        $orders = Order::with('items.dish')
            ->where('user_id', $request->user()->id)
            ->orderBy('created_at', 'desc')
            ->get()
            ->map(fn($order) => $this->formatOrder($order));

        return response()->json($orders, 200);
    }

    /**
     * GET /api/orders/{id}/track
     * Retourne l'état actuel d'une commande (Order Kotlin)
     */
    public function trackOrder(int $id): JsonResponse
    {
        $order = Order::with('items.dish')->find($id);

        if (!$order) {
            return response()->json(['message' => 'Commande introuvable.'], 404);
        }

        return response()->json($this->formatOrder($order), 200);
    }

    /**
     * PATCH /api/orders/{id}/status
     * Utilisé par le dashboard cuisine pour changer le statut (PREPARING / READY / DELIVERED)
     */
    public function updateStatus(Request $request, int $id): JsonResponse
    {
        $request->validate([
            'status' => 'required|string|in:PENDING,PREPARING,READY,DELIVERED',
        ]);

        $order = Order::find($id);
        if (!$order) {
            return response()->json(['message' => 'Commande introuvable.'], 404);
        }

        $order->update(['status' => $request->status]);

        // Libère la table si livré
        if ($request->status === 'DELIVERED' && $order->restaurant_table_id) {
            RestaurantTable::find($order->restaurant_table_id)?->update(['status' => 'free']);
        }

        return response()->json($this->formatOrder($order->load('items.dish')), 200);
    }

    /**
     * GET /api/kitchen/orders
     * Commandes PENDING et PREPARING pour le dashboard cuisine
     */
    public function kitchenOrders(): JsonResponse
    {
        $orders = Order::with('items.dish')
            ->whereIn('status', ['PENDING', 'PREPARING'])
            ->orderBy('created_at', 'asc')
            ->get()
            ->map(fn($order) => $this->formatOrder($order));

        return response()->json($orders, 200);
    }

    /**
     * Format une commande pour correspondre EXACTEMENT aux @SerializedName Kotlin :
     *   id, order_number, items, total_price, status, date, table_number
     */
    private function formatOrder(Order $order): array
    {
        $items = $order->items->map(function ($item) {
            return [
                'dish' => [
                    'id'               => $item->dish->id,
                    'name'             => $item->dish->name,
                    'description'      => $item->dish->description ?? '',
                    'price'            => (float) $item->dish->price,
                    'image_url'        => $item->dish->image_url ?? '',
                    'category'         => $item->dish->category?->name ?? '',
                    'rating'           => (float) ($item->dish->rating ?? 0),
                    'preparation_time' => $item->dish->preparation_time ?? '15 min',
                    'is_available'     => (bool) $item->dish->is_available,
                ],
                'quantity' => (int) $item->quantity,
                'comment'  => $item->comment ?? '',
            ];
        })->values()->toArray();

        // Récupère le numéro de table lisible
        $tableNumber = null;
        if ($order->restaurant_table_id) {
            $tableNumber = RestaurantTable::find($order->restaurant_table_id)?->table_number;
        }

        return [
            'id'           => $order->id,
            'order_number' => $order->order_number,
            'items'        => $items,
            'total_price'  => (float) $order->total_price,
            'status'       => $order->status,
            'date'         => $order->created_at?->format('d/m/Y H:i') ?? '',
            'table_number' => $tableNumber,
        ];
    }
}
