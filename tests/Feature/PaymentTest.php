<?php
namespace Tests\Feature;

use App\Models\Order;
use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class PaymentTest extends TestCase
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
            'order_number' => 'BA-PAY001',
            'total_price'  => 30000,
            'status'       => 'READY',
        ]);
    }

    public function test_create_payment_intent_returns_client_secret(): void
    {
        $this->actingAs($this->user)
            ->postJson('/api/payments/create-intent', [
                'order_id' => $this->order->id,
                'convives' => 2,
            ])
            ->assertStatus(200)
            ->assertJsonStructure(['client_secret', 'amount', 'currency']);
    }

    public function test_payment_intent_divides_by_convives(): void
    {
        $data = $this->actingAs($this->user)
            ->postJson('/api/payments/create-intent', [
                'order_id' => $this->order->id,
                'convives' => 3,
            ])
            ->json();

        // 30000 Ar / 3 = 10000 Ar = 1000000 centimes
        $expected = (int) round((30000 * 100) / 3);
        $this->assertEquals($expected, $data['amount']);
    }

    public function test_confirm_payment_marks_order_delivered(): void
    {
        $this->actingAs($this->user)
            ->postJson('/api/payments/confirm', [
                'payment_intent_id' => 'pi_demo_test',
                'order_id'          => $this->order->id,
            ])
            ->assertStatus(200)
            ->assertJson(['success' => true]);

        $this->assertDatabaseHas('orders', [
            'id'     => $this->order->id,
            'status' => 'DELIVERED',
        ]);
    }

    public function test_confirm_payment_credits_loyalty_points(): void
    {
        $this->actingAs($this->user)
            ->postJson('/api/payments/confirm', [
                'payment_intent_id' => 'pi_demo_test',
                'order_id'          => $this->order->id,
            ]);

        // 30000 / 1000 = 30 points
        $this->assertDatabaseHas('loyalty_points', [
            'user_id' => $this->user->id,
            'points'  => 30,
        ]);
    }

    public function test_payment_requires_auth(): void
    {
        $this->postJson('/api/payments/create-intent', [
            'order_id' => $this->order->id,
            'convives' => 1,
        ])->assertStatus(401);
    }
}
