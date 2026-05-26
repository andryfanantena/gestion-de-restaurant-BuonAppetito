<?php

namespace App\Livewire;

use Livewire\Component;
use App\Models\Order;

class KitchenDashboard extends Component
{
    public function refreshOrders()
    {
        // Cette méthode vide sert de déclencheur pour le rafraîchissement (polling)
    }

    public function startPreparation($orderId)
    {
        $order = Order::find($orderId);
        if ($order) {
            $order->status = 'PREPARING';
            $order->save();
            session()->flash('message', "Commande #{$order->order_number} passée en cuisine.");
        }
    }

    public function markAsReady($orderId)
    {
        $order = Order::find($orderId);
        if ($order) {
            $order->status = 'READY';
            $order->save();
            session()->flash('message', "Commande #{$order->order_number} marquée comme prête !");
        }
    }

    public function render()
    {
        // Sécurisation : on récupère uniquement les commandes valides avec leurs relations existantes
        $activeOrders = Order::with(['items.dish', 'user'])
        ->orderBy('created_at', 'desc')
        ->get();

        return view('livewire.kitchen-dashboard', [
            'orders' => $activeOrders,
        ]);
    }
}