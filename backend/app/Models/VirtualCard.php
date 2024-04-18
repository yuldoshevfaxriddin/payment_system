<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class VirtualCard extends Model
{
    use HasFactory;
    protected $fillable = [
        'user_id',
        'card_number',
        'live_time',
        'total_price',
    ];
    protected $table = 'virtual_cards';
}
