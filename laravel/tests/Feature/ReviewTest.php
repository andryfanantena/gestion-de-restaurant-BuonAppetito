<?php
namespace Tests\Feature;

use App\Models\Category;
use App\Models\Dish;
use App\Models\Order;
use App\Models\Review;
use App\Models\RestaurantTable;
use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class ReviewTest extends TestCase
{
    use RefreshDatabase;

    private User  $user;
    private Order $order;

    protected function setUp(): void
    {
        parent::setUp();
        $this->user  = User::factory()->create(['role' => 'client']);
        $this->order = Order::create([
            'user_id'      => $this->user->id,
            'order_number' => 'BA-REV001',
            'total_price'  => 25000,
            'status'       => 'DELIVERED',
        ]);
    }

    public function test_submit_review_with_valid_rating(): void
    {
        $this->actingAs($this->user)
            ->postJson("/api/orders/{$this->order->id}/review", [
                'rating'  => 5,
                'comment' => 'Excellent repas !',
            ])
            ->assertStatus(201)
            ->assertJson(['success' => true]);

        $this->assertDatabaseHas('reviews', [
            'order_id' => $this->order->id,
            'rating'   => 5,
        ]);
    }

    public function test_submit_review_without_comment(): void
    {
        $this->actingAs($this->user)
            ->postJson("/api/orders/{$this->order->id}/review", ['rating' => 3])
            ->assertStatus(201)
            ->assertJson(['success' => true]);
    }

    public function test_submit_review_fails_with_invalid_rating(): void
    {
        $this->actingAs($this->user)
            ->postJson("/api/orders/{$this->order->id}/review", ['rating' => 6])
            ->assertStatus(422);
    }

    public function test_cannot_review_same_order_twice(): void
    {
        Review::create(['order_id' => $this->order->id, 'user_id' => $this->user->id, 'rating' => 4]);

        $this->actingAs($this->user)
            ->postJson("/api/orders/{$this->order->id}/review", ['rating' => 5])
            ->assertStatus(409);
    }

    public function test_review_requires_auth(): void
    {
        $this->postJson("/api/orders/{$this->order->id}/review", ['rating' => 4])
            ->assertStatus(401);
    }

    public function test_review_credits_loyalty_points(): void
    {
        $this->actingAs($this->user)
            ->postJson("/api/orders/{$this->order->id}/review", ['rating' => 5]);

        $this->assertDatabaseHas('loyalty_points', [
            'user_id' => $this->user->id,
            'points'  => 50,
        ]);
    }
}
