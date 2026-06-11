<?php

namespace App\Repositories;

use App\Models\Order;
use App\Models\OrderItem;
use App\Models\Dish;
use Illuminate\Support\Str;
use Illuminate\Database\Eloquent\Collection;

class OrderRepository
{
    public function createOrder(int $userId, array $itemsRequest, ?string $tableNumber): Order
    {
        $totalPrice = 0.0;
        $orderItemsData = [];

        // Parcourir les lignes demandées pour calculer les prix en fonction des plats de la BDD
        foreach ($itemsRequest as $item) {
            $dish = Dish::find($item['dish_id']);
            if ($dish) {
                $itemPrice = $dish->price * $item['quantity'];
                $totalPrice += $itemPrice;

                $orderItemsData[] = [
                    'dish_id' => $dish->id,
                    'quantity' => $item['quantity'],
                    'price' => $dish->price
                ];
            }
        }

        // Insertion globale de la commande
        $order = Order::create([
            'user_id' => $userId,
            'order_number' => 'BA-' . strtoupper(Str::random(6)),
            'total_price' => $totalPrice,
            'status' => 'PENDING',
            'table_number' => $tableNumber
        ]);

        // Sauvegarde de toutes les lignes d'achat liées
        foreach ($orderItemsData as $itemData) {
            $itemData['order_id'] = $order->id;
            OrderItem::create($itemData);
        }

        return $order->load('items.dish');
    }

    public function getHistoryByUser(int $userId): Collection
    {
        return Order::where('user_id', $userId)
            ->with('items.dish')
            ->orderBy('created_at', 'desc')
            ->get();
    }

    public function findById(int $orderId): ?Order
    {
        return Order::with('items.dish')->find($orderId);
    }
}