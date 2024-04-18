<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class PaymentStories extends Model
{
    use HasFactory;
    
    protected $fillable = [
        'user_id_1',
        'user_id_2',
        'card_id_1',
        'card_id_2',
        'price',
        'status',
        'protset',
    ];

}
