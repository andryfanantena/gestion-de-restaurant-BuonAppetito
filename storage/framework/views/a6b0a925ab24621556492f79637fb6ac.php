<div class="p-6 bg-gray-100 min-h-screen" wire:poll.5s="refreshOrders">
    <div class="max-w-7xl mx-auto">
        
        <div class="flex justify-between items-center mb-6 bg-white p-4 rounded-lg shadow-sm">
            <div>
                <h1 class="text-2xl font-bold text-gray-800">Écran Cuisine - BuonAppetito</h1>
            </div>
            <div class="flex space-x-4">
                <span class="px-3 py-1 bg-yellow-100 text-yellow-800 rounded-full text-sm font-semibold">
                    En attente: <?php echo e($orders->where('status', 'PENDING')->count()); ?>

                </span>
                <span class="px-3 py-1 bg-blue-100 text-blue-800 rounded-full text-sm font-semibold">
                    En préparation: <?php echo e($orders->where('status', 'PREPARING')->count()); ?>

                </span>
            </div>
        </div>

        <?php if(\Livewire\Mechanisms\ExtendBlade\ExtendBlade::isRenderingLivewireComponent()): ?><!--[if BLOCK]><![endif]--><?php endif; ?><?php if(session()->has('message')): ?>
            <div class="mb-4 p-4 bg-green-100 border-l-4 border-green-500 text-green-700 rounded shadow-sm">
                <?php echo e(session('message')); ?>

            </div>
        <?php endif; ?><?php if(\Livewire\Mechanisms\ExtendBlade\ExtendBlade::isRenderingLivewireComponent()): ?><!--[if ENDBLOCK]><![endif]--><?php endif; ?>

        <?php if(\Livewire\Mechanisms\ExtendBlade\ExtendBlade::isRenderingLivewireComponent()): ?><!--[if BLOCK]><![endif]--><?php endif; ?><?php if($orders->isEmpty()): ?>
            <div class="bg-white p-12 text-center rounded-lg shadow border border-gray-200">
                <p class="text-gray-500 text-lg font-medium">Aucune commande en cours pour le moment.</p>
            </div>
        <?php else: ?>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <?php if(\Livewire\Mechanisms\ExtendBlade\ExtendBlade::isRenderingLivewireComponent()): ?><!--[if BLOCK]><![endif]--><?php \Livewire\Features\SupportCompiledWireKeys\SupportCompiledWireKeys::openLoop(); ?><?php endif; ?><?php $__currentLoopData = $orders; $__env->addLoop($__currentLoopData); foreach($__currentLoopData as $order): $__env->incrementLoopIndices(); $loop = $__env->getLastLoop(); ?><?php if(\Livewire\Mechanisms\ExtendBlade\ExtendBlade::isRenderingLivewireComponent()): ?><?php \Livewire\Features\SupportCompiledWireKeys\SupportCompiledWireKeys::startLoopIteration(); ?><?php endif; ?>
                    <div class="bg-white rounded-lg shadow border-t-4 <?php echo e($order->status === 'PENDING' ? 'border-yellow-500' : 'border-blue-500'); ?> flex flex-col justify-between">
                        
                        <div class="p-5">
                            <div class="flex justify-between items-start mb-3">
                                <div>
                                    <span class="text-xs font-bold text-gray-400 block">N° COMMANDE</span>
                                    <h2 class="text-lg font-bold text-gray-800"><?php echo e($order->order_number); ?></h2>
                                </div>
                                <div class="text-right">
                                    <span class="px-2.5 py-1 text-xs font-semibold rounded-full <?php echo e($order->status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' : 'bg-blue-100 text-blue-800'); ?>">
                                        <?php echo e($order->status === 'PENDING' ? 'EN ATTENTE' : 'PRÉPARATION'); ?>

                                    </span>
                                    <span class="block text-xs text-gray-500 mt-1 font-medium">
                                        <?php echo e($order->table_number ?? 'À emporter'); ?>

                                    </span>
                                </div>
                            </div>

                            <div class="border-t border-b border-gray-100 py-3 my-3">
                                <span class="text-xs font-bold text-gray-400 block mb-2">PLATS À PRÉPARER</span>
                                <ul class="space-y-2">
                                    <?php if(\Livewire\Mechanisms\ExtendBlade\ExtendBlade::isRenderingLivewireComponent()): ?><!--[if BLOCK]><![endif]--><?php \Livewire\Features\SupportCompiledWireKeys\SupportCompiledWireKeys::openLoop(); ?><?php endif; ?><?php $__currentLoopData = $order->items; $__env->addLoop($__currentLoopData); foreach($__currentLoopData as $item): $__env->incrementLoopIndices(); $loop = $__env->getLastLoop(); ?><?php if(\Livewire\Mechanisms\ExtendBlade\ExtendBlade::isRenderingLivewireComponent()): ?><?php \Livewire\Features\SupportCompiledWireKeys\SupportCompiledWireKeys::startLoopIteration(); ?><?php endif; ?>
                                        <li class="flex justify-between text-gray-700">
                                            <span class="font-medium">
                                                <span class="text-lg font-bold text-gray-900 mr-2">x<?php echo e($item->quantity); ?></span> 
                                                <?php echo e($item->dish->name ?? 'Plat inconnu'); ?>

                                            </span>
                                            <span class="text-xs text-gray-400 self-center">
                                                (<?php echo e($item->dish->preparation_time ?? '15 min'); ?>)
                                            </span>
                                        </li>
                                    <?php if(\Livewire\Mechanisms\ExtendBlade\ExtendBlade::isRenderingLivewireComponent()): ?><?php \Livewire\Features\SupportCompiledWireKeys\SupportCompiledWireKeys::endLoop(); ?><?php endif; ?><?php endforeach; $__env->popLoop(); $loop = $__env->getLastLoop(); ?><?php if(\Livewire\Mechanisms\ExtendBlade\ExtendBlade::isRenderingLivewireComponent()): ?><!--[if ENDBLOCK]><![endif]--><?php \Livewire\Features\SupportCompiledWireKeys\SupportCompiledWireKeys::closeLoop(); ?><?php endif; ?>
                                </ul>
                            </div>

                            <div class="flex justify-between text-xs text-gray-500 font-medium">
                                <span>Client : <?php echo e($order->user->name ?? 'Anonyme'); ?></span>
                                <span>Reçu à : <?php echo e($order->created_at->format('H:i')); ?></span>
                            </div>
                        </div>

                        <div class="bg-gray-50 px-5 py-3 rounded-b-lg border-t border-gray-100">
                            <?php if(\Livewire\Mechanisms\ExtendBlade\ExtendBlade::isRenderingLivewireComponent()): ?><!--[if BLOCK]><![endif]--><?php endif; ?><?php if($order->status === 'PENDING'): ?>
                                <button wire:click="startPreparation(<?php echo e($order->id); ?>)" class="w-full bg-yellow-500 hover:bg-yellow-600 text-white font-bold py-2 px-4 rounded transition duration-150 text-center text-sm">
                                    Lancer la préparation
                                </button>
                            <?php elseif($order->status === 'PREPARING'): ?>
                                <button wire:click="markAsReady(<?php echo e($order->id); ?>)" class="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition duration-150 text-center text-sm">
                                    Marquer comme Prêt
                                </button>
                            <?php endif; ?><?php if(\Livewire\Mechanisms\ExtendBlade\ExtendBlade::isRenderingLivewireComponent()): ?><!--[if ENDBLOCK]><![endif]--><?php endif; ?>
                        </div>

                    </div>
                <?php if(\Livewire\Mechanisms\ExtendBlade\ExtendBlade::isRenderingLivewireComponent()): ?><?php \Livewire\Features\SupportCompiledWireKeys\SupportCompiledWireKeys::endLoop(); ?><?php endif; ?><?php endforeach; $__env->popLoop(); $loop = $__env->getLastLoop(); ?><?php if(\Livewire\Mechanisms\ExtendBlade\ExtendBlade::isRenderingLivewireComponent()): ?><!--[if ENDBLOCK]><![endif]--><?php \Livewire\Features\SupportCompiledWireKeys\SupportCompiledWireKeys::closeLoop(); ?><?php endif; ?>
            </div>
        <?php endif; ?><?php if(\Livewire\Mechanisms\ExtendBlade\ExtendBlade::isRenderingLivewireComponent()): ?><!--[if ENDBLOCK]><![endif]--><?php endif; ?>

    </div>
</div><?php /**PATH C:\UPA\L2\S2\dev mobile\backend\BuonAppetito\resources\views/livewire/kitchen-dashboard.blade.php ENDPATH**/ ?>