<?php
namespace Tests\Feature;

use App\Models\LoyaltyPoint;
use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class LoyaltyTest extends TestCase
{
    use RefreshDatabase;

    public function test_loyalty_returns_bronze_for_new_user(): void
    {
        $user = User::factory()->create();
        $this->actingAs($user)
            ->getJson('/api/loyalty')
            ->assertStatus(200)
            ->assertJson(['points' => 0, 'level' => 'Bronze']);
    }

    public function test_loyalty_returns_silver_at_500_points(): void
    {
        $user = User::factory()->create();
        LoyaltyPoint::create(['user_id' => $user->id, 'points' => 500]);

        $this->actingAs($user)
            ->getJson('/api/loyalty')
            ->assertStatus(200)
            ->assertJson(['level' => 'Silver']);
    }

    public function test_loyalty_returns_gold_at_1500_points(): void
    {
        $user = User::factory()->create();
        LoyaltyPoint::create(['user_id' => $user->id, 'points' => 1500]);

        $this->actingAs($user)
            ->getJson('/api/loyalty')
            ->assertStatus(200)
            ->assertJson(['level' => 'Gold']);
    }

    public function test_loyalty_has_correct_structure(): void
    {
        $user = User::factory()->create();
        $this->actingAs($user)
            ->getJson('/api/loyalty')
            ->assertStatus(200)
            ->assertJsonStructure(['points', 'level', 'next_reward', 'points_to_next']);
    }

    public function test_loyalty_requires_auth(): void
    {
        $this->getJson('/api/loyalty')->assertStatus(401);
    }
}
