<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use App\Models\Category;
use App\Models\Dish;

class DishSeeder extends Seeder
{
    public function run(): void
    {
        // Catégories alignées avec les FilterChips du MenuScreen Kotlin
        $categories = [
            ['name' => 'Burgers',  'icon' => 'fastfood'],
            ['name' => 'Pizza',    'icon' => 'local_pizza'],
            ['name' => 'Pasta',    'icon' => 'restaurant'],
            ['name' => 'Desserts', 'icon' => 'cake'],
            ['name' => 'Boissons', 'icon' => 'local_drink'],
        ];

        $created = [];
        foreach ($categories as $cat) {
            $created[$cat['name']] = Category::create($cat);
        }

        $dishesData = [
            'Burgers' => [
                ['name' => 'Burger BuonAppetito',  'price' => 15000, 'desc' => 'Steak de zébu, fromage fondant, sauce maison.',        'popular' => true,  'rating' => 4.9],
                ['name' => 'Cheese Burger Double', 'price' => 18000, 'desc' => 'Double steak, double cheddar, oignons caramélisés.',   'popular' => true,  'rating' => 4.7],
                ['name' => 'Chicken Burger',       'price' => 13000, 'desc' => 'Escalope de poulet croustillante, sauce barbecue.',    'popular' => false, 'rating' => 4.5],
                ['name' => 'Burger Végétarien',    'price' => 11000, 'desc' => 'Galette de légumes, avocat, tomates fraîches.',        'popular' => false, 'rating' => 4.2],
                ['name' => 'Burger Zébu Bacon',    'price' => 16000, 'desc' => 'Bœuf zébu, bacon fumé, sauce ranch maison.',           'popular' => true,  'rating' => 4.8],
                ['name' => 'Portion Frites Maison','price' => 5000,  'desc' => 'Pommes de terre locales frites à la perfection.',      'popular' => false, 'rating' => 4.3],
            ],
            'Pizza' => [
                ['name' => 'Pizza Margherita',      'price' => 20000, 'desc' => 'Sauce tomate, mozzarella, basilic frais.',            'popular' => true,  'rating' => 4.8],
                ['name' => 'Pizza Quatre Fromages', 'price' => 24000, 'desc' => 'Mozzarella, gouda, bleu, parmesan.',                  'popular' => false, 'rating' => 4.6],
                ['name' => 'Pizza Reine',           'price' => 22000, 'desc' => 'Jambon, champignons, mozzarella.',                    'popular' => true,  'rating' => 4.7],
                ['name' => 'Pizza Fruits de Mer',   'price' => 28000, 'desc' => 'Crevettes, calamars, sauce blanche crémeuse.',        'popular' => false, 'rating' => 4.5],
                ['name' => 'Pizza Poulet BBQ',      'price' => 23000, 'desc' => 'Poulet grillé, sauce barbecue fumée, oignons.',       'popular' => false, 'rating' => 4.4],
                ['name' => 'Calzone Maison',        'price' => 21000, 'desc' => 'Pizza farcie jambon, ricotta, tomates.',              'popular' => false, 'rating' => 4.3],
            ],
            'Pasta' => [
                ['name' => 'Pasta Carbonara',       'price' => 18000, 'desc' => 'Spaghetti, pancetta, œuf, pecorino romano.',         'popular' => true,  'rating' => 4.8],
                ['name' => 'Pasta Bolognaise',      'price' => 17000, 'desc' => 'Tagliatelle, viande de zébu mijotée, tomates.',      'popular' => true,  'rating' => 4.7],
                ['name' => 'Pasta Pesto',           'price' => 16000, 'desc' => 'Fusilli, pesto basilic, parmesan.',                  'popular' => false, 'rating' => 4.4],
                ['name' => 'Pasta Fruits de Mer',   'price' => 22000, 'desc' => 'Linguine, crevettes, sauce crème citronnée.',        'popular' => false, 'rating' => 4.5],
                ['name' => 'Lasagne Maison',        'price' => 20000, 'desc' => 'Lasagne bœuf/porc, béchamel onctueuse.',             'popular' => false, 'rating' => 4.6],
                ['name' => 'Mine Sao Spécial',      'price' => 16000, 'desc' => 'Nouilles sautées (poulet, œufs, légumes).',          'popular' => true,  'rating' => 4.5],
            ],
            'Desserts' => [
                ['name' => 'Crème Brûlée Vanille', 'price' => 11000, 'desc' => 'Vanille de Madagascar, croûte caramélisée.',          'popular' => true,  'rating' => 4.9],
                ['name' => 'Fondant Chocolat',      'price' => 10000, 'desc' => 'Cœur coulant, boule de glace vanille.',              'popular' => true,  'rating' => 4.8],
                ['name' => 'Tiramisu Maison',       'price' => 9500,  'desc' => 'Mascarpone, espresso, cacao.',                      'popular' => false, 'rating' => 4.7],
                ['name' => 'Salade Fruits Exotiques','price' => 7000, 'desc' => 'Mangue, ananas, litchi selon saison.',               'popular' => false, 'rating' => 4.4],
                ['name' => 'Banane Flambée Rhum',  'price' => 9000,  'desc' => 'Bananes locales flambées au rhum de Nosy Be.',       'popular' => false, 'rating' => 4.5],
                ['name' => 'Glace Artisanale (2 boules)','price' => 8000,'desc' => 'Vanille, chocolat ou fraise.',                   'popular' => false, 'rating' => 4.3],
            ],
            'Boissons' => [
                ['name' => 'Eau Vive 1.5L',        'price' => 3500,  'desc' => 'Eau minérale naturelle.',                            'popular' => false, 'rating' => 4.0],
                ['name' => 'Coca-Cola',             'price' => 3000,  'desc' => 'Boisson gazeuse rafraîchissante.',                   'popular' => true,  'rating' => 4.2],
                ['name' => 'Jus Naturel Pressé',   'price' => 6000,  'desc' => 'Orange, ananas ou passion pressé minute.',           'popular' => true,  'rating' => 4.7],
                ['name' => 'Bière THB Canette',    'price' => 5000,  'desc' => 'La bière locale rafraîchissante.',                   'popular' => false, 'rating' => 4.3],
                ['name' => 'Café Noir Local',       'price' => 2500,  'desc' => 'Café robusta de la côte Est.',                      'popular' => false, 'rating' => 4.5],
                ['name' => 'Smoothie Tropical',    'price' => 7000,  'desc' => 'Mangue, banane, lait de coco.',                      'popular' => true,  'rating' => 4.8],
            ],
        ];

        foreach ($dishesData as $categoryName => $dishes) {
            $category = $created[$categoryName];
            foreach ($dishes as $dish) {
                Dish::create([
                    'category_id'      => $category->id,
                    'name'             => $dish['name'],
                    'price'            => $dish['price'],
                    'description'      => $dish['desc'],
                    'rating'           => $dish['rating'],
                    'is_popular'       => $dish['popular'],
                    'is_available'     => true,
                    'preparation_time' => rand(10, 25),
                    'image_url'        => 'https://via.placeholder.com/300x200?text=' . urlencode($dish['name']),
                ]);
            }
        }
    }
}
