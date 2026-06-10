<div class="p-6">
    <h2 class="text-2xl font-bold mb-6">💰 Caisse — Commandes à encaisser</h2>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        @forelse($unpaidOrders as $order)
        <div class="bg-white rounded-2xl shadow p-5 border-l-4 border-green-500">
            <div class="flex justify-between items-center mb-3">
                <div>
                    <p class="font-bold text-lg">#{{ $order->order_number }}</p>
                    <p class="text-gray-500 text-sm">{{ $order->table?->table_number ?? 'À emporter' }}</p>
                </div>
                <span class="text-xl font-bold text-green-600">
                    {{ number_format($order->total_price, 0, '.', ' ') }} Ar
                </span>
            </div>
            <button wire:click="processPayment({{ $order->id }})"
                class="w-full bg-green-500 text-white rounded-xl py-2 font-semibold hover:bg-green-600">
                ✅ Encaisser
            </button>
        </div>
        @empty
        <p class="text-gray-400 col-span-2 text-center py-12">Aucune commande à encaisser.</p>
        @endforelse
    </div>
</div>
