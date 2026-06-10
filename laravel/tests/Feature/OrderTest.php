<?php

namespace Tests\Feature;

use App\Models\Category;
use App\Models\Dish;
use App\Models\Order;
use App\Models\RestaurantTable;
use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class OrderTest extends TestCase
{
    use RefreshDatabase;

    private User $user;
    private Dish $dish;
    private RestaurantTable $table;

    protected function setUp(): void
    {
        parent::setUp();

        $this->user  = User::factory()->create(['role' => 'client']);
        $category    = Category::create(['name' => 'Burgers', 'icon' => 'food']);
        $this->dish  = Dish::create([
            'category_id'      => $category->id,
            'name'             => 'Test Burger',
            'price'            => 15000,
            'description'      => 'Test',
            'is_available'     => true,
            'is_popular'       => false,
            'preparation_time' => 15,
            'rating'           => 4.5,
        ]);
        $this->table = RestaurantTable::create([
            'table_number' => 'Table 1',
            'status'       => 'free',
            'capacity'     => 4,
        ]);
    }

    // ── POST /api/orders ──────────────────────────────────────────────────────

    public function test_create_order_returns_formatted_order(): void
    {
        $response = $this->actingAs($this->user)
            ->postJson('/api/orders', [
                'table_number' => 'Table 1',
                'items'        => [
                    ['dish_id' => $this->dish->id, 'quantity' => 2, 'comment' => 'sans sel'],
                ],
            ]);

        $response->assertStatus(201)
            ->assertJsonStructure(['id', 'order_number', 'items', 'total_price', 'status', 'date'])
            ->assertJson(['status' => 'PENDING', 'total_price' => 30000]);
    }

    public function test_create_order_requires_auth(): void
    {
        $this->postJson('/api/orders', [
            'items' => [['dish_id' => $this->dish->id, 'quantity' => 1]],
        ])->assertStatus(401);
    }

    public function test_create_order_fails_with_invalid_dish(): void
    {
        $this->actingAs($this->user)
            ->postJson('/api/orders', [
                'items' => [['dish_id' => 9999, 'quantity' => 1]],
            ])->assertStatus(422);
    }

    public function test_create_order_sets_table_to_occupied(): void
    {
        $this->actingAs($this->user)->postJson('/api/orders', [
            'table_number' => 'Table 1',
            'items'        => [['dish_id' => $this->dish->id, 'quantity' => 1]],
        ]);

        $this->assertDatabaseHas('restaurant_tables', [
            'table_number' => 'Table 1',
            'status'       => 'occupied',
        ]);
    }

    // ── GET /api/orders/history ───────────────────────────────────────────────

    public function test_order_history_returns_user_orders(): void
    {
        Order::create([
            'user_id'      => $this->user->id,
            'order_number' => 'BA-TEST01',
            'total_price'  => 15000,
            'status'       => 'DELIVERED',
        ]);

        $data = $this->actingAs($this->user)
            ->getJson('/api/orders/history')
            ->assertStatus(200)
            ->json();

        $this->assertCount(1, $data);
        $this->assertEquals('BA-TEST01', $data[0]['order_number']);
    }

    public function test_order_history_excludes_other_users(): void
    {
        $otherUser = User::factory()->create();
        Order::create([
            'user_id'      => $otherUser->id,
            'order_number' => 'BA-OTHER',
            'total_price'  => 10000,
            'status'       => 'PENDING',
        ]);

        $data = $this->actingAs($this->user)
            ->getJson('/api/orders/history')
            ->assertStatus(200)
            ->json();

        $this->assertCount(0, $data);
    }

    // ── GET /api/orders/{id}/track ────────────────────────────────────────────

    public function test_track_order_returns_current_status(): void
    {
        $order = Order::create([
            'user_id'      => $this->user->id,
            'order_number' => 'BA-TRACK1',
            'total_price'  => 15000,
            'status'       => 'PREPARING',
        ]);

        $this->actingAs($this->user)
            ->getJson("/api/orders/{$order->id}/track")
            ->assertStatus(200)
            ->assertJson(['status' => 'PREPARING', 'id' => $order->id]);
    }

    public function test_track_nonexistent_order_returns_404(): void
    {
        $this->actingAs($this->user)
            ->getJson('/api/orders/99999/track')
            ->assertStatus(404);
    }

    // ── PATCH /api/orders/{id}/status ────────────────────────────────────────

    public function test_update_order_status_to_ready(): void
    {
        $order = Order::create([
            'user_id'      => $this->user->id,
            'order_number' => 'BA-READY1',
            'total_price'  => 15000,
            'status'       => 'PREPARING',
        ]);

        $cook = User::factory()->create(['role' => 'cook']);
        $this->actingAs($cook)
            ->patchJson("/api/orders/{$order->id}/status", ['status' => 'READY'])
            ->assertStatus(200)
            ->assertJson(['status' => 'READY']);
    }
}
