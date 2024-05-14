<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\Hash;
use Illuminate\Http\Request;
use App\Models\User;
use App\Models\VirtualCard;
use App\Models\DeviceSession;

class AuthController extends Controller
{
    public function register(Request $req){
        // dd(\Illuminate\Support\Str::uuid()->toString(date("Y/m/d")." ".date("h:i:sa")));
        $tel_number = $req->input('tel_number');
        $password = $req->input('password');
        $name = $req->input('name');
        $device_info = $req->input('device_info');
        $user = User::where('tel_number',$tel_number)->first();
        // dd($tel_number,$user);
        if($user){
            $respons = [
                'status'=>'error',
                'message'=>'foydalanuvchi mavjud',
            ];
            return json_encode($respons);
        }

        $card_number_start = VirtualCard::max('card_number');
        if(! $card_number_start){
            $card_number_start = 9860190104600000;
        }
        $card_number_start = $card_number_start + 1;
        $user = User::create([
            'name'=>$name,
            'tel_number'=>$tel_number,
            'role'=>'user',
            'password'=>$password,
        ]);
        // 'password'=>Hash::make($password),
        $virtual_card = VirtualCard::create([
            'user_id'=>$user->id,
            'card_number'=>$card_number_start,
            'live_time'=>(date('Y') + 4) . date('/m/d'),
            'total_price'=>100000,
        ]);
        // session create
        $device_token = \Illuminate\Support\Str::uuid()->toString(date("Y/m/d")." ".date("h:i:sa"));
        $device = DeviceSession::create([
            'user_id'=>$user->id,
            'device_info'=>$device_info,
            'device_token'=>$device_token,
            'created_time'=>date("Y/m/d")." ".date("h:i:sa"),
            'ip_addres'=>$req->server->get('REMOTE_ADDR'),
        ]);

        $respons = [
            'status'=>'succes',
            'message'=>'foydalanuvchi yaratildi',
            'user'=>$user,
            'virtual_cards'=>$virtual_card,
            'devices'=>$device,
        ];
        return json_encode($respons);
    }

    public function login(Request $req){
        $tel_number = $req->input('tel_number');
        $password = $req->input('password');
        $device_info = $req->input('device_info');

        $user = User::where('tel_number',$tel_number)->where('password',$password)->first();
        // $user = User::where('tel_number',$tel_number)->where('password',Hash::make($password))->first();
        if(! $user){
            $respons = [
                'status'=>'error',
                'message'=>"telefon raqam yoki parol xato",
            ];
            return json_encode($respons);
        }

        // $devices = DeviceSession::where('user_id',$user->id)->get() ;
        // foreach($devices as $device){
        //     if($device->device_token == $device_token){
        //         $respons = [
        //             'status'=>'succes',
        //             'message'=>'tizimga kirildi',
        //             'user'=>$user,
        //             'virtual_cards'=>$virtual_cards,
        //             'devices'=>$devices,
        //         ];
        //         return json_encode($respons);
        //     }
        // }

        $devices = DeviceSession::where('user_id',$user->id)->get();
        $users_limit = 3;
        if(count($devices)>$users_limit){
            $respons = [
                'status'=>'info',
                'message'=>"Sizda ulangan ".$users_limit-1 ." ta qurilma mavjud",
            ];
            return json_encode($respons);
        }
        
        $device_token = \Illuminate\Support\Str::uuid()->toString(date("Y/m/d")." ".date("h:i:sa"));
        $device = DeviceSession::create([
            'user_id'=>$user->id,
            'device_info'=>$device_info,
            'device_token'=>$device_token,
            'created_time'=>date("Y/m/d")." ".date("h:i:sa"),
            'ip_addres'=>$req->server->get('REMOTE_ADDR'),
        ]);
        $virtual_cards = VirtualCard::where('user_id',$user->id)->get()[0] ;

	$user['device_token']=$device_token;
        $respons = [
            'status'=>'succes',
            'message'=>'tizimga kirildi',
            'user'=>$user,
            'virtual_cards'=>$virtual_cards,
            'devices'=>$devices,
            ];
        return json_encode($respons);
    }
    public function log_out(Request $req){
        $user_id = $req->input("user_id");
        $device_token = $req->input("device_token");
        $device = DeviceSession::where('user_id',$user_id)->where("device_token",$device_token)->first();
        if(! $device){
            $respons = [
            'status'=>'error',
            'message'=>'Qurilma topilmadi',
            ];
            return json_encode($respons);
        }
        $device->delete();
        $respons = [
            'status'=>'succes',
            'message'=>'Tizimdan chiqarildi',
            ];
        return json_encode($respons);
    }

    public function devices(Request $req){
        $user_id = $req->input("user_id");
        $device_token = $req->input("device_token");
        $device = DeviceSession::where('user_id',$user_id)->where("device_token",$device_token)->first();
        if(! $device){
            $respons = [
            'status'=>'error',
            'message'=>"Sizda ruxsat yo'q",
            ];
            return json_encode($respons);
        }
        $devices = DeviceSession::where('user_id',$user_id)->get();
        $respons = [
            'status'=>'succes',
            'message'=>'Tizimdan chiqarildi',
            'devices'=>$devices,
            ];
        return json_encode($respons);


    }
}
