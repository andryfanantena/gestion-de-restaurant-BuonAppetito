<?php

namespace App\Livewire;

use Livewire\Component;
use App\Models\Order;

class KitchenDashboard extends Component
{
    public $orders;

    public function mount()
    {
        $this->refreshOrders();
    }

    public function refreshOrders()
    {
        // On charge les plats et les utilisateurs associés aux commandes
        $this->orders = Order::with(['items.dish', 'user'])
            ->whereIn('status', ['PENDING', 'PREPARING'])
            ->orderBy('created_at', 'asc')
            ->get();
    }

    public function startPreparation($orderId)
    {
        $order = Order::find($orderId);
        if ($order) {
            $order->update(['status' => 'PREPARING']);
            session()->flash('message', "La préparation de la commande {$order->order_number} a commencé !");
        }
        $this->refreshOrders();
    }

    public function markAsReady($orderId)
    {
        $order = Order::find($orderId);
        if ($order) {
            $order->update(['status' => 'READY']);
            session()->flash('message', "La commande {$order->order_number} est prête !");
        }
        $this->refreshOrders();
    }

    public function render()
    {
        return view('livewire.kitchen-dashboard')
            ->layout('layouts.app'); // Utilise la structure globale resources/views/layouts/app.blade.php
    }
}