<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    public function up(): void {
        Schema::create('dishes', function (Blueprint $table) {
            $table->id();
            $table->foreignId('category_id')->constrained()->onDelete('cascade');
            $table->string('name');
            $table->text('description')->nullable();
            $table->decimal('price', 10, 2);       // Prix en Ariary (Ar)
            $table->string('image_url')->nullable();
            $table->integer('preparation_time')->default(15); // En minutes
            $table->decimal('rating', 3, 1)->default(0);      // Note sur 5
            $table->boolean('is_available')->default(true);
            $table->boolean('is_popular')->default(false);
            $table->timestamps();
        });
    }

    public function down(): void {
        Schema::dropIfExists('dishes');
    }
};
