<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use App\Models\Dish;

class DishSeeder extends Seeder
{
    public function run(): void
    {
        $dishes = [
            // Catégorie 1 : Entrées (6 plats)
            ['name' => 'Sambos Royale', 'desc' => 'Feuilletés croustillants au bœuf haché et épices locales.', 'price' => 8000, 'cat' => 'Entrées', 'time' => '10 min'],
            ['name' => 'Nem aux Crevettes', 'desc' => 'Nems faits maison accompagnés de sauce aigre-douce.', 'price' => 12000, 'cat' => 'Entrées', 'time' => '12 min'],
            ['name' => 'Salade de Poulpe', 'desc' => 'Poulpe frais de Nosy Be mariné au citron et huile d’olive.', 'price' => 18000, 'cat' => 'Entrées', 'time' => '15 min'],
            ['name' => 'Achards de Légumes', 'desc' => 'Entrée croquante traditionnelle malgache légèrement pimentée.', 'price' => 6000, 'cat' => 'Entrées', 'time' => '8 min'],
            ['name' => 'Velouté de Potiron', 'desc' => 'Soupe onctueuse parfumée à la noix de muscade.', 'price' => 10000, 'cat' => 'Entrées', 'time' => '10 min'],
            ['name' => 'Carpaccio de Zébu', 'desc' => 'Fines tranches de filet de zébu, parmesan et roquette.', 'price' => 20000, 'cat' => 'Entrées', 'time' => '12 min'],

            // Catégorie 2 : Plats Résistants (6 plats)
            ['name' => 'Romazava Royal', 'desc' => 'Le plat national : ragoût de zébu aux brèdes mafana.', 'price' => 28000, 'cat' => 'Plats', 'time' => '25 min'],
            ['name' => 'Ravitoto au Porc', 'desc' => 'Feuilles de manioc pilées cuites avec de la viande de porc grasse.', 'price' => 26000, 'cat' => 'Plats', 'time' => '30 min'],
            ['name' => 'Henakisoa sy Amalona', 'desc' => 'Mélange traditionnel de porc et d’anguille en sauce.', 'price' => 32000, 'cat' => 'Plats', 'time' => '35 min'],
            ['name' => 'Steak de Zébu au Poivre Vert', 'desc' => 'Filet tendre accompagné d’une sauce crémeuse au poivre vert de Madagascar.', 'price' => 30000, 'cat' => 'Plats', 'time' => '20 min'],
            ['name' => 'Poulet au Coco', 'desc' => 'Morceaux de poulet mijotés dans du lait de coco et du safran.', 'price' => 24000, 'cat' => 'Plats', 'time' => '22 min'],
            ['name' => 'Filet de Capitaine Grillé', 'desc' => 'Poisson frais grillé servi avec une sauce au beurre citronné.', 'price' => 29000, 'cat' => 'Plats', 'time' => '18 min'],

            // Catégorie 3 : Pizzas & Burgers (6 plats)
            ['name' => 'Pizza Margherita', 'desc' => 'Sauce tomate, mozzarella, basilic frais et filet d’olive.', 'price' => 22000, 'cat' => 'Pizzas & Burgers', 'time' => '12 min'],
            ['name' => 'Pizza Quatre Fromages', 'desc' => 'Mélange onctueux de fromages locaux et importés.', 'price' => 28000, 'cat' => 'Pizzas & Burgers', 'time' => '15 min'],
            ['name' => 'Pizza Gasy', 'desc' => 'Sauce tomate, émincé de zébu sauté, oignons et piments doux.', 'price' => 26000, 'cat' => 'Pizzas & Burgers', 'time' => '14 min'],
            ['name' => 'Le Gasy Burger', 'desc' => 'Steak de zébu, fromage fondant, achards de légumes maison.', 'price' => 25000, 'cat' => 'Pizzas & Burgers', 'time' => '15 min'],
            ['name' => 'Cheese Burger Classique', 'desc' => 'Double cheddar, steak, cornichons et sauce burger.', 'price' => 23000, 'cat' => 'Pizzas & Burgers', 'time' => '13 min'],
            ['name' => 'Crispy Chicken Burger', 'desc' => 'Poulet pané ultra croustillant, salade et mayonnaise épicée.', 'price' => 24000, 'cat' => 'Pizzas & Burgers', 'time' => '15 min'],

            // Catégorie 4 : Desserts (6 plats)
            ['name' => 'Banane Flambée', 'desc' => 'Bananes locales flambées au rhum de Madagascar.', 'price' => 12000, 'cat' => 'Desserts', 'time' => '10 min'],
            ['name' => 'Koba au Chocolat', 'desc' => 'Gâteau traditionnel revisité au chocolat de Diego-Suarez.', 'price' => 10000, 'cat' => 'Desserts', 'time' => '8 min'],
            ['name' => 'Mousse au Chocolat Noir', 'desc' => 'Faite avec du chocolat 70% pur cacao fin.', 'price' => 14000, 'cat' => 'Desserts', 'time' => '7 min'],
            ['name' => 'Crème Brûlée à la Vanille', 'desc' => 'Parfumée avec de véritables gousses de vanille de Sambava.', 'price' => 16000, 'cat' => 'Desserts', 'time' => '12 min'],
            ['name' => 'Salade de Fruits Exotiques', 'desc' => 'Mangue, litchi, ananas et passion selon la saison.', 'price' => 10000, 'cat' => 'Desserts', 'time' => '5 min'],
            ['name' => 'Glace Artisanale (3 boules)', 'desc' => 'Parfums au choix : Vanille, Chocolat, Coco, Corossol.', 'price' => 12000, 'cat' => 'Desserts', 'time' => '5 min'],

            // Catégorie 5 : Boissons (6 plats)
            ['name' => 'Jus de THB (Prestige)', 'desc' => 'Bière locale emblématique Three Horses Beer.', 'price' => 8000, 'cat' => 'Boissons', 'time' => '3 min'],
            ['name' => 'Bonbon Anglais', 'desc' => 'La boisson gazeuse sucrée typique de l’île.', 'price' => 5000, 'cat' => 'Boissons', 'time' => '2 min'],
            ['name' => 'Eau Vive 1.5L', 'desc' => 'Eau minérale naturelle plate.', 'price' => 4000, 'cat' => 'Boissons', 'time' => '2 min'],
            ['name' => 'Jus de Passion Frais', 'desc' => 'Fruits pressés sur place, 100% naturel.', 'price' => 8000, 'cat' => 'Boissons', 'time' => '4 min'],
            ['name' => 'Citronnade Maison', 'desc' => 'Citron vert, eau filtrée, gingembre et miel.', 'price' => 7000, 'cat' => 'Boissons', 'time' => '4 min'],
            ['name' => 'Café de Madagascar', 'desc' => 'Café Robusta local corsé servi chaud.', 'price' => 5000, 'cat' => 'Boissons', 'time' => '3 min'],
        ];

        foreach ($dishes as $dish) {
            Dish::create([
                'name' => $dish['name'],
                'description' => $dish['desc'],
                'price' => $dish['price'],
                'category' => $dish['cat'],
                'image_url' => '',
                'rating' => rand(40, 50) / 10, // Note aléatoire entre 4.0 et 5.0
                'preparation_time' => $dish['time']
            ]);
        }
    }
}