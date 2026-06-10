<?php
namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class LoyaltyPoint extends Model
{
    protected $fillable = ['user_id', 'points'];
    protected $casts    = ['points' => 'integer'];

    public function user(): BelongsTo { return $this->belongsTo(User::class); }

    public static function addPoints(int $userId, int $points): void
    {
        $record = self::firstOrCreate(['user_id' => $userId], ['points' => 0]);
        $record->increment('points', $points);
    }

    public static function getForUser(int $userId): self
    {
        return self::firstOrCreate(['user_id' => $userId], ['points' => 0]);
    }
}
