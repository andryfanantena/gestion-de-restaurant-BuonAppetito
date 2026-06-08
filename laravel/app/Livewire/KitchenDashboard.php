<?php

namespace App\Livewire;

use Livewire\Component;
use App\Models\Order;
use App\Models\Table;

class KitchenDashboard extends Component
{
    public $orders = [];

    // Configuration de l'écouteur d'événements Pusher intégrée à Livewire
    protected $listeners = [
        'echo:kitchen-channel,order.placed' => 'refreshOrders'
    ];

    public function mount(): void
    {
        $this->refreshOrders();
    }

    public function refreshOrders(): void
    {
        $this->orders = Order::with(['items.dish', 'table', 'user'])
            ->whereIn('status', ['PENDING', 'PREPARING'])
            ->orderBy('created_at', 'asc')
            ->get();
    }

    public function startPreparing($orderId): void
    {
        $order = Order::find($orderId);
        if ($order) {
            $order->update(['status' => 'PREPARING']);
            // Optionnel : émettre un événement ici pour notifier le client mobile
        }
        $this->refreshOrders();
    }

    public function setReady($orderId): void
    {
        $order = Order::find($orderId);
        if ($order) {
            $order->update(['status' => 'READY']);
            // Optionnel : déclencher la notification push Firebase/Pusher au client Kotlin
        }
        $this->refreshOrders();
    }

    public function render()
    {
        return view('livewire.kitchen-dashboard')->with('orders', $this->orders);
    }
}