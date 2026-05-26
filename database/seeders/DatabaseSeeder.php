<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use App\Models\User;
use App\Models\Order;
use App\Models\OrderItem;
use App\Models\Dish;
use Illuminate\Support\Str;
use Illuminate\Support\Facades\Hash;

class DatabaseSeeder extends Seeder
{
    public function run(): void
    {
        // 1. Insertion des 30 plats répartis sur les 5 catégories
        $this->call([
            DishSeeder::class,
        ]);

        // 2. CRÉATION DES 4 COMPTES UTILISATEURS (Un par rôle)
        
        // Compte Administrateur
        $admin = User::firstOrCreate(
            ['email' => 'admin@buonappetito.com'],
            [
                'name' => 'Andry Admin',
                'password' => Hash::make('password123'),
                'role' => 'admin'
            ]
        );

        // Compte Cuisinier
        $cuisinier = User::firstOrCreate(
            ['email' => 'cuisine@buonappetito.com'],
            [
                'name' => 'Chef Cuisinier',
                'password' => Hash::make('password123'),
                'role' => 'cuisinier'
            ]
        );

        // Compte Serveur
        $serveur = User::firstOrCreate(
            ['email' => 'serveur@buonappetito.com'],
            [
                'name' => 'Toky Serveur',
                'password' => Hash::make('password123'),
                'role' => 'serveur'
            ]
        );

        // Compte Client de base
        $client = User::firstOrCreate(
            ['email' => 'client@buonappetito.com'],
            [
                'name' => 'Raza Client',
                'password' => Hash::make('password123'),
                'role' => 'client'
            ]
        );

        // 3. GÉNÉRATION DES COMMANDES DE TEST POUR LE DASHBOARD CUISINE
        $dishes = Dish::all();

        if ($dishes->count() > 0) {
            // Création de 15 commandes réparties aléatoirement sur les 10 tables
            for ($i = 1; $i <= 15; $i++) {
                $tableNumber = 'Table ' . rand(1, 10);
                
                $order = Order::create([
                    'user_id' => $client->id, // Assigne la commande au compte client créé ci-dessus
                    'order_number' => 'BA-' . strtoupper(Str::random(6)),
                    'total_price' => 0, 
                    'status' => $i % 3 === 0 ? 'PREPARING' : 'PENDING', // Répartition équitable des statuts
                    'table_number' => $tableNumber
                ]);

                $totalPrice = 0;
                // Sélection de 3 plats aléatoires pour garnir le ticket
                $randomDishes = $dishes->random(3);
                foreach ($randomDishes as $dish) {
                    $qty = rand(1, 2);
                    $price = $dish->price * $qty;
                    $totalPrice += $price;

                    OrderItem::create([
                        'order_id' => $order->id,
                        'dish_id' => $dish->id,
                        'quantity' => $qty,
                        'price' => $dish->price
                    ]);
                }

                // Enregistrement du montant total en Ariary
                $order->update(['total_price' => $totalPrice]);
            }
        }
    }
}