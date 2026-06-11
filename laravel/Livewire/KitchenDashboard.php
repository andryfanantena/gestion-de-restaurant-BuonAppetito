<?php

namespace App\Livewire;

use Livewire\Component;
use App\Models\Order;

class KitchenDashboard extends Component
{
    public $orders;

    // Écoute l'événement Pusher broadcasté par OrderController
    protected $listeners = [
        'echo:kitchen-channel,order.placed' => 'refreshOrders',
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

    /**
     * Appelé par le bouton "Lancer la préparation" dans la vue
     * Méthode nommée startPreparation() pour correspondre au wire:click du Blade
     */
    public function startPreparation(int $orderId): void
    {
        $order = Order::find($orderId);
        if ($order && $order->status === 'PENDING') {
            $order->update(['status' => 'PREPARING']);
        }
        $this->refreshOrders();
    }

    /**
     * Appelé par le bouton "Marquer comme Prêt"
     * Méthode nommée markAsReady() pour correspondre au wire:click du Blade
     */
    public function markAsReady(int $orderId): void
    {
        $order = Order::find($orderId);
        if ($order && $order->status === 'PREPARING') {
            $order->update(['status' => 'READY']);
            // Le client Android détectera READY via le polling trackOrder (toutes les 5s)
            // et affichera une notification locale
        }
        $this->refreshOrders();
    }

    public function render()
    {
        return view('livewire.kitchen-dashboard', ['orders' => $this->orders]);
    }
}
