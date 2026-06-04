<?php

namespace App\Events;

use App\Models\Order;
use Illuminate\Broadcasting\Channel;
use Illuminate\Broadcasting\InteractsWithSockets;
use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class OrderPlaced implements ShouldBroadcast
{
    use Dispatchable, InteractsWithSockets, SerializesModels;

    public Order $order;

    public function __construct(Order $order)
    {
        // Charger les relations nécessaires au dashboard cuisine
        $this->order = $order->load(['items.dish', 'table', 'user']);
    }

    public function broadcastOn(): array
    {
        return [new Channel('kitchen-channel')];
    }

    public function broadcastAs(): string
    {
        return 'order.placed';
    }

    /**
     * Données envoyées au dashboard cuisine via Pusher
     */
    public function broadcastWith(): array
    {
        return [
            'id'           => $this->order->id,
            'order_number' => $this->order->order_number,
            'status'       => $this->order->status,
            'table_number' => $this->order->table?->table_number,
            'items_count'  => $this->order->items->count(),
        ];
    }
}
