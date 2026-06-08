<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Order;
use App\Models\OrderItem;
use App\Models\Dish;
use App\Models\Table;
use App\Events\OrderPlaced;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Str;

class OrderApiController extends Controller
{
    public function store(Request $request): JsonResponse
    {
        // 1. Validation avec le bon nom de colonne : table_id
        $request->validate([
            'table_id'         => 'required|exists:tables,id',
            'notes'            => 'nullable|string|max:500',
            'items'            => 'required|array|min:1',
            'items.*.dish_id'  => 'required|exists:dishes,id',
            'items.*.quantity' => 'required|integer|min:1',
            'items.*.comment'  => 'nullable|string|max:255',
        ]);

        DB::beginTransaction();

        try {
            $totalPrice = 0;
            $orderNumber = 'CMD-' . strtoupper(Str::random(6));

            // 2. Création de la commande avec table_id
            $order = Order::create([
                'user_id'      => $request->user()->id,
                'table_id'     => $request->table_id, // Mis à jour
                'order_number' => $orderNumber,
                'status'       => 'PENDING',
                'total_price'  => 0, 
                'notes'        => $request->notes ?? null
            ]);

            // 3. Ajout des plats
            foreach ($request->items as $item) {
                $dish = Dish::findOrFail($item['dish_id']);
                $totalPrice += $dish->price * $item['quantity'];

                OrderItem::create([
                    'order_id' => $order->id,
                    'dish_id'  => $item['dish_id'],
                    'quantity' => $item['quantity'],
                    'comment'  => $item['comment'] ?? null,
                ]);
            }

            // Mise à jour du prix total (qui sera affiché en Ariary)
            $order->update(['total_price' => $totalPrice]);

            // 4. Passage de la table en OCCUPIED
            $table = Table::findOrFail($request->table_id);
            $table->update(['status' => 'OCCUPIED']);

            DB::commit();

            // 5. Signal temps réel Pusher pour le dashboard de la cuisine
            broadcast(new OrderPlaced($order))->toOthers();

            return response()->json([
                'success'      => true,
                'message'      => 'Commande enregistrée avec succès !',
                'order_number' => $orderNumber,
                'total_price'  => $totalPrice
            ], 201);

        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json([
                'success' => false,
                'message' => 'Erreur lors du traitement de la commande sur le serveur.',
                'error'   => $e->getMessage()
            ], 500);
        }
    }
}