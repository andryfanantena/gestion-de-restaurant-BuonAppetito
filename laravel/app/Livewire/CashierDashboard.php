<?php

namespace App\Livewire;

use Livewire\Component;
use App\Models\Order;
use App\Models\Table;

class CashierDashboard extends Component
{
    public $unpaidOrders = [];
    public $tables = [];

    public function mount(): void
    {
        $this->loadData();
    }

    public function loadData(): void
    {
        // Récupère les commandes prêtes à être réglées
        $this->unpaidOrders = Order::with(['user', 'table'])
            ->where('status', 'READY')
            ->orderBy('updated_at', 'desc')
            ->get();

        // Récupère l'état actuel de toutes les tables de la salle
        $this->tables = Table::orderBy('number', 'asc')->get();
    }

    public function processPayment($orderId): void
    {
        $order = Order::find($orderId);
        if ($order) {
            $order->update(['status' => 'PAID']);
            
            // Libération automatique de la table lors du paiement
            if ($order->table_id) {
                $table = Table::find($order->table_id);
                if ($table) {
                    $table->update(['status' => 'FREE']);
                }
            }
        }
        $this->loadData();
    }

    public function render()
    {
        return view('livewire.cashier-dashboard')->with([
            'unpaidOrders' => $this->unpaidOrders,
            'tables' => $this->tables
        ]);
    }
}