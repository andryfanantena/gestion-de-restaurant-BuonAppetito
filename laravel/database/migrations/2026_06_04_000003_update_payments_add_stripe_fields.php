<?php
use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    public function up(): void {
        Schema::table('payments', function (Blueprint $table) {
            $table->string('stripe_payment_intent_id')->nullable()->after('order_id');
            $table->integer('convives')->default(1)->after('amount');
            $table->decimal('amount_per_person', 10, 2)->nullable()->after('convives');
            $table->string('payment_method')->default('STRIPE')->change();
        });
    }
    public function down(): void {
        Schema::table('payments', function (Blueprint $table) {
            $table->dropColumn(['stripe_payment_intent_id', 'convives', 'amount_per_person']);
        });
    }
};
