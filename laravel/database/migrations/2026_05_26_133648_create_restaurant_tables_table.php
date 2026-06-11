<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    public function up(): void {
        Schema::create('restaurant_tables', function (Blueprint $table) {
            $table->id();
            $table->string('table_number')->unique(); // Ex: "Table 1" ou QR payload direct
            $table->string('status')->default('free'); // free, occupied, reserved
            $table->integer('capacity')->default(4);
            $table->timestamps();
        });
    }

    public function down(): void {
        Schema::dropIfExists('restaurant_tables');
    }
};