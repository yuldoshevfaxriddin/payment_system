package com.example.imkonbank;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signuptext = findViewById(R.id.signuptext);
        textView  = findViewById(R.id.test);

        String deviceInfo = Build.BRAND + " " + Build.MODEL;
        textView.setText(deviceInfo);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telNumber = username.getText().toString();
                String passwordEntry = password.getText().toString();
                String url = "http://192.168.43.136:8000/login?tel_number="+telNumber+"&password="+passwordEntry+"&device_info="+deviceInfo;
                if(telNumber.equals("admin")){
                    Toast.makeText(LoginActivity.this, "test rejimi if ishladi", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{

                    Toast.makeText(LoginActivity.this, "else ishladi", Toast.LENGTH_SHORT).show();

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                        String status = response.getString("status");
                        String message = response.getString("message");
                        if(status.equals("succes")){
                            JSONObject user = response.getJSONObject("user");
//                            JSONObject cards = response.getJSONObject("virtual_cards"); // array keladi
//                            JSONObject devices = response.getJSONObject("devices"); //array keladi

//                            data basaga yozilsin
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
//                        textView.setText("server bilan bog'lanishda xatolik");
                        Toast.makeText(LoginActivity.this, "server bilan bog'lanishda xatolik", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE,"xatolik",error);
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(request);
                }
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