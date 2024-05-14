<?php

use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "web" middleware group. Make something great!
|
*/
Route::get('/', function (Illuminate\Http\Request $r) {
	$respons = [
	"status"=>"succes",
	"message"=>"bu test"	
];
    return json_encode($respons);
});

Route::get('/register',[\App\Http\Controllers\AuthController::class,'register']);
Route::get('/login',[\App\Http\Controllers\AuthController::class,'login']);
Route::get('/log-out',[\App\Http\Controllers\AuthController::class,'log_out']);
Route::get('/devices',[\App\Http\Controllers\AuthController::class,'devices']);

Route::get('/check-card',[\App\Http\Controllers\PaymentController::class,'check_card']);
Route::get('/payment-info',[\App\Http\Controllers\PaymentController::class,'payment_info']);
Route::get('/payment',[\App\Http\Controllers\PaymentController::class,'payment']);
Route::get('/payments-stories',[\App\Http\Controllers\PaymentController::class,'payments_stories']);

// register
// login
// check-data
// payment