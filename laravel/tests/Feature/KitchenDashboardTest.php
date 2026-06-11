<?php

namespace Tests\Feature;

use App\Livewire\KitchenDashboard;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Livewire\Livewire;
use Tests\TestCase;

class KitchenDashboardTest extends TestCase
{
    use RefreshDatabase;

    public function test_kitchen_dashboard_renders_successfully(): void
    {
        Livewire::test(KitchenDashboard::class)
            ->assertOk()
            ->assertViewIs('livewire.kitchen-dashboard');
    }
}
