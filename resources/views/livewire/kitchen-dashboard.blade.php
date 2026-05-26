<div class="p-6 bg-gray-100 min-h-screen" wire:poll.5s="refreshOrders">
    <div class="max-w-7xl mx-auto">
        
        <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-lg shadow-sm">
            <div>
                <h1 class="text-2xl font-bold text-gray-800">Écran Cuisine - BuonAppetito</h1>
                <p class="text-sm text-gray-500">Mise à jour automatique toutes les 5 secondes</p>
            </div>
            <div class="flex space-x-4">
                <span class="px-3 py-1 bg-yellow-100 text-yellow-800 rounded-full text-sm font-semibold">
                    En attente: {{ $orders->where('status', 'PENDING')->count() }}
                </span>
                <span class="px-3 py-1 bg-blue-100 text-blue-800 rounded-full text-sm font-semibold">
                    En préparation: {{ $orders->where('status', 'PREPARING')->count() }}
                </span>
            </div>
        </div>

        @if (session()->has('message'))
            <div class="mb-4 p-4 bg-green-100 border-l-4 border-green-500 text-green-700 rounded shadow-sm">
                {{ session('message') }}
            </div>
        @endif

        @if($orders->isEmpty())
            <div class="bg-white p-12 text-center rounded-lg shadow border border-gray-200">
                <p class="text-gray-500 text-lg font-medium">Aucune commande en cours pour le moment. Calme plat en cuisine !</p>
            </div>
        @else
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                @foreach($orders as $order)
                    <div class="bg-white rounded-lg shadow border-t-4 {{ $order->status === 'PENDING' ? 'border-yellow-500' : 'border-blue-500' }} flex flex-col justify-between">
                        
                        <div class="p-5">
                            <div class="flex justify-between items-start mb-3">
                                <div>
                                    <span class="text-xs font-bold text-gray-400 block">N° COMMANDE</span>
                                    <h2 class="text-lg font-bold text-gray-800">{{ $order->order_number }}</h2>
                                </div>
                                <div class="text-right">
                                    <span class="px-2.5 py-1 text-xs font-semibold rounded-full {{ $order->status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' : 'bg-blue-100 text-blue-800' }}">
                                        {{ $order->status === 'PENDING' ? 'EN ATTENTE' : 'PRÉPARATION' }}
                                    </span>
                                    <span class="block text-xs text-gray-500 mt-1 font-medium">
                                        {{ $order->table_number ?? 'À emporter' }}
                                    </span>
                                </div>
                            </div>

                            <div class="border-t border-b border-gray-100 py-3 my-3">
                                <span class="text-xs font-bold text-gray-400 block mb-2">PLATS À PRÉPARER</span>
                                <ul class="space-y-2">
                                    @foreach($order->items as $item)
                                        <li class="flex justify-between text-gray-700">
                                            <span class="font-medium">
                                                <span class="text-lg font-bold text-gray-900 mr-2">x{{ $item->quantity }}</span> 
                                                {{ $item->dish->name }}
                                            </span>
                                            <span class="text-xs text-gray-500 self-center">
                                                {{ number_format($item->price, 0, ',', ' ') }} Ar
                                            </span>
                                        </li>
                                    @endforeach
                                </ul>
                            </div>

                            <div class="flex justify-between items-center mb-3 bg-gray-50 p-2 rounded">
                                <span class="text-xs font-bold text-gray-500">TOTAL COMMANDE :</span>
                                <span class="text-sm font-bold text-gray-900">
                                    {{ number_format($order->total_price, 0, ',', ' ') }} Ar
                                </span>
                            </div>

                            <div class="flex justify-between text-xs text-gray-500 font-medium pt-2">
                                <span>Client : {{ $order->user->name ?? 'Anonyme' }}</span>
                                <span>Reçu à : {{ $order->created_at->format('H:i') }}</span>
                            </div>
                        </div>

                        <div class="bg-gray-50 px-5 py-3 rounded-b-lg border-t border-gray-100">
                            @if($order->status === 'PENDING')
                                <button 
                                    wire:click="startPreparation({{ $order->id }})" 
                                    class="w-full bg-yellow-500 hover:bg-yellow-600 text-white font-bold py-2 px-4 rounded transition duration-150 ease-in-out text-center text-sm"
                                >
                                    Lancer la préparation
                                </button>
                            @elseif($order->status === 'PREPARING')
                                <button 
                                    wire:click="markAsReady({{ $order->id }})" 
                                    class="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition duration-150 ease-in-out text-center text-sm"
                                >
                                    Marquer comme Prêt
                                </button>
                            @endif
                        </div>

                    </div>
                @endforeach
            </div>
        @endif

    </div>
</div>