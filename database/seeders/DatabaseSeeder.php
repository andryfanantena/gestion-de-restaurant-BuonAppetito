<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use App\Models\RestaurantTable;
use App\Models\User;
use Illuminate\Support\Facades\Hash;

class DatabaseSeeder extends Seeder
{
    public function run(): void
    {
        // 1. Génération des comptes utilisateurs de tests par rôles
        User::create([
            'name' => 'Cuisinier Chef',
            'email' => 'cook@buonappetito.mg',
            'password' => Hash::make('password'),
            'role' => 'cook'
        ]);

        User::create([
            'name' => 'Serveur Horizon',
            'email' => 'server@buonappetito.mg',
            'password' => Hash::make('password'),
            'role' => 'server'
        ]);

        User::create([
            'name' => 'Rova Client',
            'email' => 'client@gmail.com',
            'password' => Hash::make('password'),
            'role' => 'client'
        ]);

        // 2. Génération automatique de 10 tables de restaurant
        for ($i = 1; $i <= 10; $i++) {
            RestaurantTable::create([
                'table_number' => 'Table ' . $i,
                'status' => 'free',
                'capacity' => rand(2, 6)
            ]);
        }

        // 3. Lancement du Seeder de plats
        $this->call(DishSeeder::class);
    }
}