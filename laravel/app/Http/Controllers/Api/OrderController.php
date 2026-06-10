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
    // POST /api/orders
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

            $table = null;
            if ($request->filled('table_number')) {
                $table = RestaurantTable::where('table_number', $request->table_number)->first();
            }

            $order = Order::create([
                'user_id'             => $request->user()->id,
                'restaurant_table_id' => $table?->id,
                'order_number'        => $orderNumber,
                'status'              => 'PENDING',
                'total_price'         => 0,
                'notes'               => null,
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
            if ($table) $table->update(['status' => 'occupied']);

            DB::commit();
            broadcast(new OrderPlaced($order->load('items.dish')))->toOthers();
            return response()->json($this->formatOrder($order->load('items.dish')), 201);

        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json(['message' => 'Erreur : ' . $e->getMessage()], 500);
        }
    }

    // GET /api/orders/history
    public function getOrderHistory(Request $request): JsonResponse
    {
        $orders = Order::with('items.dish')
            ->where('user_id', $request->user()->id)
            ->orderBy('created_at', 'desc')
            ->get()
            ->map(fn($o) => $this->formatOrder($o));
        return response()->json($orders, 200);
    }

    // GET /api/orders/{id}/track
    public function trackOrder(int $id): JsonResponse
    {
        $order = Order::with('items.dish')->find($id);
        if (!$order) return response()->json(['message' => 'Commande introuvable.'], 404);
        return response()->json($this->formatOrder($order), 200);
    }

    // PATCH /api/orders/{id}/status
    public function updateStatus(Request $request, int $id): JsonResponse
    {
        $request->validate(['status' => 'required|in:PENDING,PREPARING,READY,DELIVERED']);
        $order = Order::find($id);
        if (!$order) return response()->json(['message' => 'Commande introuvable.'], 404);

        $order->update(['status' => $request->status]);
        if ($request->status === 'DELIVERED' && $order->restaurant_table_id) {
            RestaurantTable::find($order->restaurant_table_id)?->update(['status' => 'free']);
        }
        return response()->json($this->formatOrder($order->load('items.dish')), 200);
    }

    // GET /api/kitchen/orders
    public function kitchenOrders(): JsonResponse
    {
        $orders = Order::with('items.dish')
            ->whereIn('status', ['PENDING', 'PREPARING'])
            ->orderBy('created_at', 'asc')
            ->get()
            ->map(fn($o) => $this->formatOrder($o));
        return response()->json($orders, 200);
    }

    // ── J3 : POST /api/orders/{id}/add-items ─────────────────────────────────
    public function addItems(Request $request, int $id): JsonResponse
    {
        $request->validate([
            'items'              => 'required|array|min:1',
            'items.*.dish_id'    => 'required|integer|exists:dishes,id',
            'items.*.quantity'   => 'required|integer|min:1',
            'items.*.comment'    => 'nullable|string|max:255',
        ]);

        $order = Order::with('items.dish')->find($id);
        if (!$order) return response()->json(['message' => 'Commande introuvable.'], 404);

        // On ne peut ajouter que si la commande est encore en cuisine
        if (!in_array($order->status, ['PENDING', 'PREPARING'])) {
            return response()->json(['message' => 'Impossible d\'ajouter des plats : commande déjà prête.'], 422);
        }

        $addedTotal = 0.0;
        foreach ($request->items as $item) {
            $dish        = Dish::findOrFail($item['dish_id']);
            $addedTotal += $dish->price * $item['quantity'];
            OrderItem::create([
                'order_id' => $order->id,
                'dish_id'  => $dish->id,
                'quantity' => $item['quantity'],
                'price'    => $dish->price,
                'comment'  => $item['comment'] ?? null,
            ]);
        }

        $order->increment('total_price', $addedTotal);

        return response()->json($this->formatOrder($order->fresh()->load('items.dish')), 200);
    }

    private function formatOrder(Order $order): array
    {
        $items = $order->items->map(fn($item) => [
            'dish'     => [
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
        ])->values()->toArray();

        $tableNumber = $order->restaurant_table_id
            ? RestaurantTable::find($order->restaurant_table_id)?->table_number
            : null;

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
