<?php
namespace App\Livewire;

use Livewire\Component;
use App\Models\Order;
use App\Models\RestaurantTable;

class CashierDashboard extends Component
{
    public $unpaidOrders = [];
    public $tables = [];

    public function mount(): void { $this->loadData(); }

    public function loadData(): void
    {
        $this->unpaidOrders = Order::with(['user', 'table'])
            ->whereIn('status', ['READY', 'PAID'])
            ->orderBy('updated_at', 'desc')
            ->get();

        $this->tables = RestaurantTable::orderBy('table_number')->get();
    }

    public function processPayment(int $orderId): void
    {
        $order = Order::find($orderId);
        if ($order) {
            $order->update(['status' => 'DELIVERED']);
            if ($order->restaurant_table_id) {
                RestaurantTable::find($order->restaurant_table_id)?->update(['status' => 'free']);
            }
        }
        $this->loadData();
    }

    public function render()
    {
        return view('livewire.cashier-dashboard', [
            'unpaidOrders' => $this->unpaidOrders,
            'tables'       => $this->tables,
        ]);
    }
}
