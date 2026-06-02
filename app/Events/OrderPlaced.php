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

    public $order;

    public function __construct(Order $order)
    {
        // Chargement immédiat des dépendances pour la vue cuisine
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
}