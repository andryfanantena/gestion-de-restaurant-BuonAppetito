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
        // 1. Comptes de test par rôle
        User::create([
            'name' => 'Admin BuonAppetito',
            'email' => 'admin@buonappetito.mg',
            'password' => Hash::make('password'),
            'role' => 'admin',
        ]);

        User::create([
            'name' => 'Cuisinier Chef',
            'email' => 'cook@buonappetito.mg',
            'password' => Hash::make('password'),
            'role' => 'cook',
        ]);

        User::create([
            'name' => 'Serveur Horizon',
            'email' => 'server@buonappetito.mg',
            'password' => Hash::make('password'),
            'role' => 'server',
        ]);

        User::create([
            'name' => 'Rova Client',
            'email' => 'client@gmail.com',
            'password' => Hash::make('password'),
            'role' => 'client',
        ]);

        // 2. 10 tables — table_number doit correspondre à ce que renvoie le QR Code
        //    Le QR Code encode "Table 1", "Table 2", etc.
        for ($i = 1; $i <= 10; $i++) {
            RestaurantTable::create([
                'table_number' => 'Table ' . $i,
                'status'       => 'free',
                'capacity'     => rand(2, 6),
            ]);
        }

        // 3. Plats avec seeders
        $this->call(DishSeeder::class);
    }
}
