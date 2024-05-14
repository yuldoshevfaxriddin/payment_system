<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\VirtualCard;
use App\Models\Transaction;
use App\Models\User;
use App\Models\DeviceSession;
use App\Models\PaymentStories;

class PaymentController extends Controller
{
    public function check_card(Request $req){
        /*
            cartani tekshiradi
        */
        $card_number = $req->input('card_number'); // card number ni tekshirish
        $device_token = $req->input('device_token'); // device_token ni tekshirish

        $card_info = VirtualCard::where('card_number',$card_number)->first();
        if(! $device_token){
            $respons = [
                'status'=>'error',
                'message'=>'sizda ruxsat yoq',
            ];
            return json_encode($respons);
        }
        $device = DeviceSession::where('device_token',$device_token)->first();
        if( ! ( $card_info && $device)){
            $respons = [
                'status'=>'error',
                'message'=>'malumotlar topilmadi',
            ];
            return json_encode($respons);
        }
        $device_owner = User::find($device->user_id);
        $client = User::find($card_info->user_id);
        $card = VirtualCard::where('user_id',$client->id)->first();
        $respons = [
            'status'=>'succes',
            'message'=>'malumotlar topildi',
            'device'=>$device,
            'device_owner'=>$device_owner,
            'client'=>$client,
            'card'=>$card,
        ];
        return json_encode($respons);
    }

    public function payment_info(Request $req){
        /*
            kartaga to'lov malumotlari
        */
        $card_owner_number = $req->input('card_owner_number'); // card number
        $card_client_number = $req->input('card_client_number'); // card number
        $transaction_price = $req->input('transaction_price');
        
        $card_owner_info = VirtualCard::where('card_number',$card_owner_number)->first();
        $card_client_info = VirtualCard::where('card_number',$card_client_number)->first();
        
        if((! $card_owner_info) || (! $card_client_info)){
            $respons = [
                'status'=>'error',
                'message'=>'card topilmadi',
            ];
            return json_encode($respons);
        }
        if(! $transaction_price){
            $respons = [
                'status'=>'error',
                'message'=>'pul miqdorini kiritilmadi',
            ];
            return json_encode($respons);
        }

        $card_owner = User::find($card_owner_info->user_id);
        $card_client = User::find($card_client_info->user_id);

        $transaction = Transaction::create([
            'transaction_key'=>\Illuminate\Support\Str::uuid()->toString(),
            'card_number_owner_id'=>$card_owner_info->id,
            'card_number_client_id'=>$card_client_info->id,
            'transaction_price'=>$transaction_price,
            'transaction_time'=>date("Y/m/d")." ".date("h:m:s"),
            'status'=>0,
        ]);

        $respons = [
            'status'=>'succes',
            'message'=>'transactsiya yaratildi',
            'card_owner'=>$card_owner,
            'card_owner_info'=>$card_owner_info,
            'card_client_info'=>$card_client_info,
            'card_client'=>$card_client,
            'transaction'=>$transaction,
        ];
        return json_encode($respons);
    }

    public function payment(Request $req){
        /*
            kartaga to'lov 
        */
        $transaction_key = $req->input('transaction_key');

        $transaction = Transaction::where('transaction_key',$transaction_key)->first();

        if( ! $transaction){
            $respons = [
                'status'=>'error',
                'message'=>'transactsiya mavjud emas',
            ];
            return json_encode($respons);
        }

        $card_owner_info = VirtualCard::find($transaction->card_number_owner_id);
        $card_client_info = VirtualCard::find($transaction->card_number_client_id);

        if($transaction->status == 1 ){
            $respons = [
                'status'=>'succes',
                'message'=>'transaktsiya bajarilgan',
                'card_info'=>$card_owner_info,
            ];
            return json_encode($respons);    
        }

        $card_owner_info->total_price = $card_owner_info->total_price - $transaction->transaction_price;
        $card_client_info->total_price = $card_client_info->total_price + $transaction->transaction_price;
        $transaction->status = 1;

        $payment_stories = PaymentStories::create([
            'user_id_1'=>$card_owner_info->id,
            'user_id_2'=>$card_client_info->id,
            'card_id_1'=>$card_owner_info->card_number,
            'card_id_2'=>$card_client_info->card_number,
            'price'=>$transaction->transaction_price,
            'status'=>$transaction->status,
            'protset'=>'0',
        ]);

        $card_owner_info->update();
        $card_client_info->update();
        $transaction->update();
        
        $respons = [
            'status'=>'succes',
            'message'=>'transaktsiya amalga oshdi',
            'card_info'=>$card_owner_info,
        ];
        return json_encode($respons);

    }

    public function payments_stories(Request $req){
        /*
            to'lovlar tarixi
        */
        $user_id = $req->input('user_id');
        $user = User::find($user_id);
        if( ! $user ){
            $respons = [
                'status'=>'error',
                'message'=>'user mavjud emas',
            ];
            return json_encode($respons);
        }
        $payments_stories = PaymentStories::where('user_id_1',$user->id)->orWhere('user_id_2',$user->id)->get();

        $stories_respons = [];
        foreach($payments_stories as $payment ){
            $stories_respons[] = [
                'user_owner_info'=>User::find($payment->user_id_1)->name,
                'user_client_info'=>User::find($payment->user_id_2)->name,
                'payment_info'=>$payment,
            ];
        }

        $respons = [
            'status'=>'succes',
            'message'=>'tolovlar tarixi',
            'payments'=>$stories_respons,
        ];
        return json_encode($respons);
    }
}
