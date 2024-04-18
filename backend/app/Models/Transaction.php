<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Transaction extends Model
{
    use HasFactory;
    protected $fillable = [
        'transaction_key',
        'card_number_owner_id',
        'card_number_client_id',
        'transaction_price',
        'transaction_time',
        'status',
    ];

}
