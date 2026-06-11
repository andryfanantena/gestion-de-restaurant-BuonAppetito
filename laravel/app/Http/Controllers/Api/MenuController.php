<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Dish;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class MenuController extends Controller
{
    /**
     * GET /api/menu?category=Burgers
     * Retourne une liste PLATE de plats (format attendu par Kotlin : List<Dish>)
     * Si ?category= est passé, filtre par nom de catégorie.
     */
    public function getAllDishes(Request $request): JsonResponse
    {
        $query = Dish::with('category')->where('is_available', true);

        // Filtre optionnel par catégorie (reçu depuis MenuViewModel.setCategory())
        if ($request->filled('category') && $request->category !== 'All') {
            $query->whereHas('category', function ($q) use ($request) {
                $q->where('name', $request->category);
            });
        }

        $dishes = $query->get()->map(fn($dish) => $this->formatDish($dish));

        return response()->json($dishes, 200);
    }

    /**
     * GET /api/menu/popular
     * Retourne les plats marqués is_popular = true (format List<Dish>)
     */
    public function getPopularDishes(): JsonResponse
    {
        $dishes = Dish::with('category')
            ->where('is_popular', true)
            ->where('is_available', true)
            ->get()
            ->map(fn($dish) => $this->formatDish($dish));

        return response()->json($dishes, 200);
    }

    /**
     * Format un plat en respectant exactement les @SerializedName Kotlin :
     *   image_url, preparation_time, is_available
     * Et le champ "category" est le nom de la catégorie (String).
     */
    private function formatDish($dish): array
    {
        return [
            'id'               => $dish->id,
            'name'             => $dish->name,
            'description'      => $dish->description ?? '',
            'price'            => (float) $dish->price,
            'image_url'        => $dish->image_url ?? '',
            'category'         => $dish->category?->name ?? '',
            'rating'           => (float) ($dish->rating ?? 0),
            'preparation_time' => $dish->preparation_time ?? '15 min',
            'is_available'     => (bool) $dish->is_available,
        ];
    }
}
