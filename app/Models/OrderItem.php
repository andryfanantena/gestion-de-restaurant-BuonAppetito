<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class OrderItem extends Model
{
    protected $fillable = ['order_id', 'dish_id', 'quantity', 'comment'];

    public function dish(): BelongsTo
    {
        return $this->belongsTo(Dish::class);
    }

    public function order(): BelongsTo
    {
        return $this->belongsTo(Order::class);
    }
}