<?php

namespace Tests\Feature;

use App\Models\Category;
use App\Models\Dish;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class MenuTest extends TestCase
{
    use RefreshDatabase;

    private function createDish(array $attrs = []): Dish
    {
        $category = Category::create(['name' => $attrs['category'] ?? 'Burgers', 'icon' => 'food']);
        return Dish::create(array_merge([
            'category_id'      => $category->id,
            'name'             => 'Test Dish',
            'description'      => 'Desc',
            'price'            => 10000,
            'image_url'        => '',
            'preparation_time' => 15,
            'rating'           => 4.5,
            'is_available'     => true,
            'is_popular'       => false,
        ], $attrs));
    }

    // ── GET /api/menu ─────────────────────────────────────────────────────────

    public function test_get_all_dishes_returns_list(): void
    {
        $this->createDish();

        $this->getJson('/api/menu')
            ->assertStatus(200)
            ->assertJsonIsArray()
            ->assertJsonStructure([['id', 'name', 'price', 'image_url', 'category', 'rating']]);
    }

    public function test_get_dishes_filtered_by_category(): void
    {
        $this->createDish(['category' => 'Pizza']);
        $this->createDish(['category' => 'Burgers', 'name' => 'Burger Test']);

        $response = $this->getJson('/api/menu?category=Pizza');
        $response->assertStatus(200);

        // Tous les plats retournés sont de la catégorie Pizza
        $data = $response->json();
        $this->assertTrue(collect($data)->every(fn($d) => $d['category'] === 'Pizza'));
    }

    public function test_get_all_dishes_response_has_snake_case_keys(): void
    {
        $this->createDish();

        $data = $this->getJson('/api/menu')->json();
        $first = $data[0];

        // Vérifie que les clés sont en snake_case (compatibles @SerializedName Kotlin)
        $this->assertArrayHasKey('image_url', $first);
        $this->assertArrayHasKey('preparation_time', $first);
        $this->assertArrayHasKey('is_available', $first);
    }

    // ── GET /api/menu/popular ─────────────────────────────────────────────────

    public function test_popular_dishes_returns_only_popular(): void
    {
        $this->createDish(['is_popular' => true,  'name' => 'Popular Dish']);
        $this->createDish(['is_popular' => false, 'name' => 'Normal Dish']);

        $data = $this->getJson('/api/menu/popular')->assertStatus(200)->json();

        $this->assertCount(1, $data);
        $this->assertEquals('Popular Dish', $data[0]['name']);
    }

    public function test_unavailable_dishes_excluded_from_menu(): void
    {
        $this->createDish(['is_available' => false, 'name' => 'Hidden']);
        $this->createDish(['is_available' => true,  'name' => 'Visible']);

        $data = $this->getJson('/api/menu')->json();
        $this->assertCount(1, $data);
        $this->assertEquals('Visible', $data[0]['name']);
    }
}
