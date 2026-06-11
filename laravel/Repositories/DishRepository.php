<?php

namespace App\Repositories;

use App\Models\Dish;
use Illuminate\Database\Eloquent\Collection;

class DishRepository
{
    public function getPopular(): Collection
    {
        // Retourne les plats populaires selon la note (rating élevé)
        return Dish::where('rating', '>=', 4.5)->take(10)->get();
    }

    public function getByCategory(?string $category): Collection
    {
        if (empty($category) || $category === 'All') {
            return Dish::all();
        }
        return Dish::where('category', $category)->get();
    }

    public function findById(int $id): ?Dish
    {
        return Dish::find($id);
    }
}