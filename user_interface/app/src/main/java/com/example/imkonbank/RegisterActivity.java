package com.example.imkonbank;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class RegisterActivity extends AppCompatActivity {
    Button regbutton;
    EditText tel_number,user_name,password1,password2;
    String HOST_SERVER = "http://192.168.209.105:8000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        regbutton = findViewById(R.id.regButton);
        tel_number =findViewById(R.id.tel_number_entry);
        password1 =findViewById(R.id.password1);
        password2 =findViewById(R.id.confirmpassword);
        user_name =findViewById(R.id.user_name_entry);
        String deviceInfo = Build.BRAND + " " + Build.MODEL;
        MyDataBaseHelper mydb = new MyDataBaseHelper(RegisterActivity.this);
        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t_n,p1,p2,u_n;
                t_n = tel_number.getText().toString();
                p2 = password2.getText().toString();
                p1 = password1.getText().toString();
                u_n = user_name.getText().toString();
                if(! p2.equals(p1)){
                    Toast.makeText(RegisterActivity.this, "Kiritilgan parol bir xil emas !", Toast.LENGTH_SHORT).show();
                    return ;
                }
                String url = HOST_SERVER+"/register?tel_number="+t_n+"&password="+p1+"&device_info="+deviceInfo+"&name="+u_n;

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");
                            if(status.equals("info")){
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                return ;
                            }
                            if(status.equals("succes")){
                                JSONObject user = response.getJSONObject("user");
                                String user_id = user.getString("id");
                                String name = user.getString("name");
                                String tel_number = user.getString("tel_number");
                                String device_token = response.getJSONObject("devices").getString("device_token");
                                String tel_password = "admin";
                                String password = password1.getText().toString();
                                boolean t = mydb.addUser(user_id,tel_number,name,password,tel_password,device_token);
                                if(! t){
                                    Toast.makeText(RegisterActivity.this, "user malumot qoshilmadi", Toast.LENGTH_SHORT).show();
                                }
                                JSONObject cards = response.getJSONObject("virtual_cards"); // array kelishi karak edi, bir dona keladigan qildim
                                String card_id = cards.getString("id");
                                String card_user_id = cards.getString("user_id");
                                String card_number = cards.getString("card_number");
                                String card_live_time = cards.getString("live_time");
                                String card_total_price = cards.getString("total_price");
                                t = mydb.addCard(card_id,card_user_id,card_number,card_live_time,card_total_price);
                                if(! t){
                                    Toast.makeText(RegisterActivity.this, "card malumot qoshilmadi", Toast.LENGTH_SHORT).show();
                                }
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);

                            }

                        } catch (JSONException e) {
                            Toast.makeText(RegisterActivity.this, "malumotlarni o'qishda xatolik", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "server bilan bog'lanishda xatolik", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE,"xatolik",error);
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                requestQueue.add(request);
            }
        });

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}