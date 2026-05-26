<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Repositories\DishRepository;
use Illuminate\Http\Request;

class MenuController extends Controller
{
    private DishRepository $dishRepository;

    public function __construct(DishRepository $dishRepository)
    {
        $this->dishRepository = $dishRepository;
    }

    public function getPopularDishes()
    {
        $dishes = $this->dishRepository->getPopular();
        return response()->json($this->formatDishesCollection($dishes), 200);
    }

    public function getAllDishes(Request $request)
    {
        $category = $request->query('category');
        $dishes = $this->dishRepository->getByCategory($category);
        return response()->json($this->formatDishesCollection($dishes), 200);
    }

    private function formatDishesCollection($dishes): array
    {
        $formatted = [];
        foreach ($dishes as $dish) {
            $formatted[] = [
                'id' => $dish->id,
                'name' => $dish->name,
                'description' => $dish->description,
                'price' => (double) $dish->price,
                'imageUrl' => $dish->image_url ?? '',
                'category' => $dish->category,
                'rating' => (double) $dish->rating,
                'preparationTime' => $dish->preparation_time
            ];
        }
        return $formatted;
    }
}