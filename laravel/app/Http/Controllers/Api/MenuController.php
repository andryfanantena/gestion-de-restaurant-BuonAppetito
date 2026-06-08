<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Category;
use Illuminate\Http\JsonResponse;

class MenuController extends Controller
{
    /**
     * GET /api/menu
     * Renvoie la carte complète structurée par catégorie
     */
    public function getAllDishes(): JsonResponse
    {
        try {
            // 1. On récupère les catégories et on y imbrique les plats associés
            $categories = Category::with('dishes')->get();

            // 2. On structure le JSON proprement pour ton code Kotlin/Android
            $formattedMenu = $categories->map(function ($category) {
                return [
                    'category_id' => $category->id,
                    'category_name' => $category->name,
                    'dishes' => $category->dishes->map(function ($dish) {
                        return [
                            'id' => $dish->id,
                            'name' => $dish->name,
                            'description' => $dish->description,
                            'price' => $dish->price, // Affiché en Ariary (Ar)
                            'preparation_time' => $dish->preparation_time ?? '15 min',
                            'image_url' => $dish->image_url,
                        ];
                    })
                ];
            });

            return response()->json([
                'success' => true,
                'message' => 'Carte complète par catégorie.',
                'data' => $formattedMenu
            ], 200);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Erreur lors de la récupération du menu.',
                'error' => $e->getMessage()
            ], 500);
        }
    }
}