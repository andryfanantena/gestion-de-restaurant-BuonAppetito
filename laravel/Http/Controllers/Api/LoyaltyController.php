<?php
namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\LoyaltyPoint;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class LoyaltyController extends Controller
{
    /**
     * GET /api/loyalty
     * Retourne : { points, level, next_reward, points_to_next }
     */
    public function index(Request $request): JsonResponse
    {
        $loyalty = LoyaltyPoint::getForUser($request->user()->id);
        $points  = $loyalty->points;

        // Calcul du niveau
        [$level, $nextReward, $pointsToNext] = $this->getLevel($points);

        return response()->json([
            'points'         => $points,
            'level'          => $level,
            'next_reward'    => $nextReward,
            'points_to_next' => $pointsToNext,
        ], 200);
    }

    private function getLevel(int $points): array
    {
        if ($points >= 1500) {
            return ['Gold', 'Vous êtes au niveau maximum 🥇 — -10% + dessert offert', 0];
        }
        if ($points >= 500) {
            return ['Silver', 'Niveau Gold : -10% sur toutes les commandes + dessert offert', 1500 - $points];
        }
        return ['Bronze', 'Niveau Silver : -5% sur toutes vos commandes', 500 - $points];
    }
}
