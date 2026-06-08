<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use App\Models\Category;
use App\Models\Dish;

class DishSeeder extends Seeder
{
    public function run(): void
    {
        // 1. Création des 5 catégories fondamentales
        $categories = [
            ['name' => 'Entrées', 'icon' => 'soup'],
            ['name' => 'Plats Résistants', 'icon' => 'restaurant'],
            ['name' => 'Fast Food', 'icon' => 'fastfood'],
            ['name' => 'Desserts', 'icon' => 'cake'],
            ['name' => 'Boissons', 'icon' => 'local_drink'],
        ];

        $createdCategories = [];
        foreach ($categories as $cat) {
            $createdCategories[$cat['name']] = Category::create($cat);
        }

        // 2. Génération de 30 plats (6 par catégorie) avec des prix en Ariary (Ar)
        $dishesData = [
            'Entrées' => [
                ['name' => 'Soupe de Crabe', 'price' => 12000, 'desc' => 'Soupe chaude traditionnelle de la côte.'],
                ['name' => 'Salade composée', 'price' => 8000, 'desc' => 'Légumes frais de saison du marché.'],
                ['name' => 'Sambos Viande (x3)', 'price' => 4500, 'desc' => 'Feuilletés croustillants au bœuf épicé.'],
                ['name' => 'Nem aux Légumes (x3)', 'price' => 4000, 'desc' => 'Nems faits maison ultra croustillants.'],
                ['name' => 'Brochettes de Zébu (x3)', 'price' => 9000, 'desc' => 'Mini brochettes marinées aux herbes.'],
                ['name' => 'Carpaccio de Tomates', 'price' => 7000, 'desc' => 'Fines tranches à l\'huile d\'olive locale.'],
            ],
            'Plats Résistants' => [
                ['name' => 'Romazava Royal', 'price' => 22000, 'desc' => 'Le plat national au zébu et brèdes mafana.', 'popular' => true],
                ['name' => 'Ravitoto au Porc', 'price' => 18000, 'desc' => 'Feuilles de manioc pilées avec viande de porc grasse.'],
                ['name' => 'Henakisoa sy Live', 'price' => 19000, 'desc' => 'Porc mijoté aux haricots blancs traditionnels.'],
                ['name' => 'Poisson Grillé Sauce Coco', 'price' => 25000, 'desc' => 'Filet de poisson frais, sauce crémeuse coco.', 'popular' => true],
                ['name' => 'Camaron à la Plancha', 'price' => 32000, 'desc' => 'Grosses crevettes sautées à l\'ail et gingembre.'],
                ['name' => 'Mine Sao Spécial', 'price' => 16000, 'desc' => 'Nouilles sautées complètes (poulet, œufs, légumes).'],
            ],
            'Fast Food' => [
                ['name' => 'Burger BuonAppetito', 'price' => 15000, 'desc' => 'Steak de zébu, fromage fondant, sauce maison.', 'popular' => true],
                ['name' => 'Cheese Burger Simple', 'price' => 12000, 'desc' => 'Le classique indémodable pour les petites faims.'],
                ['name' => 'Pizza Quatre Fromages', 'price' => 24000, 'desc' => 'Base crème ou tomate avec sélections de fromages.'],
                ['name' => 'Pizza Reine', 'price' => 20000, 'desc' => 'Jambon, champignons frais et mozzarella.'],
                ['name' => 'Sandwich Zébu Casserole', 'price' => 8500, 'desc' => 'Baguette croustillante, viande effilochée juteuse.'],
                ['name' => 'Portion de Frites Maison', 'price' => 5000, 'desc' => 'Pommes de terre locales frites à la perfection.'],
            ],
            'Desserts' => [
                ['name' => 'Mofogasy au Chocolat (x3)', 'price' => 4000, 'desc' => 'Galettes de riz traditionnelles revisitées.'],
                ['name' => 'Salade de Fruits Exotiques', 'price' => 7000, 'desc' => 'Mangue, ananas, litchi selon saison.'],
                ['name' => 'Banane Flambée au Rhum', 'price' => 9000, 'desc' => 'Bananes locales flambées au rhum de Nosy Be.'],
                ['name' => 'Crème Brûlée à la Vanille', 'price' => 11000, 'desc' => 'Parfumée à la véritable vanille de Madagascar.', 'popular' => true],
                ['name' => 'Fondant au Chocolat', 'price' => 10000, 'desc' => 'Cœur coulant, servi avec une boule de glace.'],
                ['name' => 'Glace Artisanale (2 boules)', 'price' => 8000, 'desc' => 'Parfums au choix : vanille, chocolat, fraise.'],
            ],
            'Boissons' => [
                ['name' => 'Eau Vive 1.5L', 'price' => 3500, 'desc' => 'Eau minérale naturelle.'],
                ['name' => 'Coca-Cola PM', 'price' => 3000, 'desc' => 'Boisson rafraîchissante gazeuse.'],
                ['name' => 'Jus de Thonon Naturel', 'price' => 6000, 'desc' => 'Pressé minute : orange, ananas ou passion.'],
                ['name' => 'Bière THB Canette', 'price' => 5000, 'desc' => 'La bière locale rafraîchissante.'],
                ['name' => 'Bonbon Anglais', 'price' => 3000, 'desc' => 'Limonade typique très appréciée.'],
                ['name' => 'Café Noir Local', 'price' => 2500, 'desc' => 'Café robusta de la côte Est.'],
            ],
        ];

        foreach ($dishesData as $categoryName => $dishes) {
            $category = $createdCategories[$categoryName];
            foreach ($dishes as $dish) {
                Dish::create([
                    'category_id' => $category->id,
                    'name' => $dish['name'],
                    'price' => $dish['price'],
                    'description' => $dish['desc'],
                    'is_popular' => $dish['popular'] ?? false,
                    'is_available' => true,
                    'preparation_time' => rand(10, 25),
                    'image_url' => 'https://via.placeholder.com/150'
                ]);
            }
        }
    }
}