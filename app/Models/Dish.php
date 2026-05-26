<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class Dish extends Model
{
    use HasFactory;

    protected $fillable = [
        'name',
        'description',
        'price',
        'image_url',
        'category',
        'rating',
        'preparation_time'
    ];

    protected $casts = [
        'price' => 'double',
        'rating' => 'double',
    ];
}