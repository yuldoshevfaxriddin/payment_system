package com.example.imkonbank;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button loginButton;
    TextView signuptext;
    TextView textView;
    String HOST_SERVER = "http://192.168.43.136:8000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        MyDataBaseHelper mydb = new MyDataBaseHelper(LoginActivity.this);

        Cursor res = mydb.readAllData();
        if (res.getCount()!=0){
            String card_number="",card_live_time="",card_owner="",card_total_price="",device_token="";
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            while(res.moveToNext()){
                card_owner = res.getString(1);
                device_token = res.getString(5);
            }

            Cursor card = mydb.readCards();
            if (card.getCount()==0){
                textView.setText("card malumot yoq");
                return ;
            }
            while(card.moveToNext()){
//                card_owner = card.getString(1);
                card_number = card.getString(2);
                card_live_time = card.getString(3);
                card_total_price = card.getString(4);
            }
            intent.putExtra("card_number",card_number);
            intent.putExtra("card_owner",card_owner);
            intent.putExtra("card_live_time",card_live_time);
            intent.putExtra("card_total_price",card_total_price);
            intent.putExtra("device_token",device_token);
            startActivity(intent);
        }
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signuptext = findViewById(R.id.signuptext);
        textView  = findViewById(R.id.test);

        String deviceInfo = Build.BRAND + " " + Build.MODEL;
//        textView.setText(deviceInfo);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telNumber = username.getText().toString();
                String passwordEntry = password.getText().toString();
                String url = HOST_SERVER+"/login?tel_number="+telNumber+"&password="+passwordEntry+"&device_info="+deviceInfo;
//                if(telNumber.equals("admin")){
////                    Toast.makeText(LoginActivity.this, "test rejimi if ishladi", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                }

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                        String status = response.getString("status");
                        String message = response.getString("message");
                        if(status.equals("succes")){
                            JSONObject user = response.getJSONObject("user");
                            String user_id = user.getString("id");
                            String name = user.getString("name");
                            String tel_number = user.getString("tel_number");
                            String device_token = user.getString("device_token");
                            String password = passwordEntry;
                            String tel_password = "admin";
                            boolean t = mydb.addUser(user_id,tel_number,name,password,tel_password,device_token);
                            if(! t){
                                Toast.makeText(LoginActivity.this, "user malumot qoshilmadi", Toast.LENGTH_SHORT).show();
                            }
                            JSONObject cards = response.getJSONObject("virtual_cards"); // array kelishi karak edi, bir dona keladigan qildim
                            String card_id = cards.getString("id");
                            String card_user_id = cards.getString("user_id");
                            String card_number = cards.getString("card_number");
                            String card_live_time = cards.getString("live_time");
                            String card_total_price = cards.getString("total_price");
                            t = mydb.addCard(card_id,card_user_id,card_number,card_live_time,card_total_price);
                            if(! t){
                                Toast.makeText(LoginActivity.this, "card malumot qoshilmadi", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, "malumotlarni o'qishda xatolik", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "server bilan bog'lanishda xatolik", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE,"xatolik",error);
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(request);
                }
        });

        signuptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}