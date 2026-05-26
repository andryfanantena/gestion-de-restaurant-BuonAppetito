<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Order extends Model
{
    use HasFactory;

    protected $fillable = [
        'user_id',
        'restaurant_table_id',
        'order_number',
        'total_price',
        'status',
        'table_number',
        'notes'
    ];

    // Relation avec les lignes de la commande (OrderItems)
    public function items()
    {
        return $this->hasMany(OrderItem::class);
    }

    // Relation avec l'utilisateur qui a passé la commande
    public function user()
    {
        return $this->belongsTo(User::class);
    }

    // Relation avec la table physique du restaurant
    public function restaurantTable()
    {
        return $this->belongsTo(RestaurantTable::class);
    }
    
}