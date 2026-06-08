<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Dish extends Model
{
    protected $fillable = [
        'category_id', 'name', 'description', 'price',
        'image_url', 'preparation_time', 'is_available', 'is_popular', 'rating',
    ];

    protected $casts = [
        'price'        => 'double',
        'rating'       => 'double',
        'is_available' => 'boolean',
        'is_popular'   => 'boolean',
    ];

    public function category(): BelongsTo
    {
        return $this->belongsTo(Category::class);
    }
}
