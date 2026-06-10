<?php
namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Payment extends Model
{
    protected $fillable = [
        'order_id', 'stripe_payment_intent_id',
        'amount', 'convives', 'amount_per_person',
        'payment_method', 'status',
    ];

    protected $casts = [
        'amount'            => 'double',
        'amount_per_person' => 'double',
        'convives'          => 'integer',
    ];

    public function order(): BelongsTo { return $this->belongsTo(Order::class); }
}
