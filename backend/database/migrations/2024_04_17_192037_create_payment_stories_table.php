<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('payment_stories', function (Blueprint $table) {
            $table->id();
            $table->string('user_id_1');
            $table->string('user_id_2');
            $table->string('card_id_1');
            $table->string('card_id_2');
            $table->string('price');
            $table->string('status');
            $table->string('protset');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('payment_stories');
    }
};
