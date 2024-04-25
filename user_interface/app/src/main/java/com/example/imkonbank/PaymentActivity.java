package com.example.imkonbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MyDataBaseHelper mydb ;
    Button paymentButton;
    EditText client_card_number,price_entry;
    TextView client_card_name,total_price_info;
    String HOST_SERVER = "http://192.168.43.136:8000";

    ArrayList<String> user_id,user_name,user_tel_number,user_device_token;
    PaymentStoriesAdapter paymentStoriesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        MyDataBaseHelper myDb = new MyDataBaseHelper(PaymentActivity.this);

        paymentButton = findViewById(R.id.paymentButton);
        client_card_name = findViewById(R.id.client_card_name);
        client_card_number = findViewById(R.id.client_card_number);
        price_entry = findViewById(R.id.price);
        total_price_info = findViewById(R.id.total_price_info);

        String device_token = getIntent().getExtras().getString("device_token");
        String total_price = getIntent().getExtras().getString("total_price");

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(PaymentActivity.this, "tolov bosildi " +price_entry.getText(), Toast.LENGTH_SHORT).show();
//                client_card_name.setText(client_card_number.getText());
            }
        });

        client_card_number.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                client_card_number.setBackgroundResource(R.drawable.custom_edittext);
                client_card_name.setText("");
                price_entry.getLayoutParams().height = 0;
                String card_number = client_card_number.getText().toString();
                String url ;
                if(card_number.length()==16 && event.getKeyCode()!=67 && event.getKeyCode()!=59){
                    client_card_number.setEnabled(false);
                    client_card_name.setText("Qidirilmoqda ...");
                    url = HOST_SERVER+"/check-card?card_number="+card_number+"&device_token="+device_token;

                    JsonObjectRequest requestCardInfo = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String status = response.getString("status");
                                String message = response.getString("message");
                                if (status.equals("succes")) {
                                    paymentButton.setEnabled(true);
                                    String client_name = response.getJSONObject("client").getString("name");
                                    String client_credit_card = response.getJSONObject("card").getString("card_number");
                                    client_card_name.setText(client_name+client_credit_card);
                                    client_card_number.setEnabled(true);
                                    price_entry.getLayoutParams().height = client_card_number.getLayoutParams().height;
                                    total_price_info.setText("Hisobingizda " +total_price+" so'm bor");
                                }
                                else{
                                    client_card_number.setBackgroundResource(R.drawable.custom_edittext_error);
                                    client_card_name.setText("");
                                    client_card_number.setEnabled(true);
                                }
                                Toast.makeText(PaymentActivity.this, message, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(PaymentActivity.this, "malumotlarni o'qishda xatolik", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                client_card_number.setEnabled(false);
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(PaymentActivity.this, "server bilan bog'lanishda xatolik", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE,"xatolik",error);
                            client_card_number.setEnabled(false);
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(PaymentActivity.this);
                    requestQueue.add(requestCardInfo);
                }
                else {
                    paymentButton.setEnabled(false);
                }
                return false;
            }
        });

        price_entry.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Integer calc_total_price = Integer.parseInt(total_price);
                String temp = price_entry.getText().toString();
                if(temp.length()>3){
                    int calc_price = Integer.parseInt(temp);
                    if(calc_total_price - calc_price >= 0 ){
                        price_entry.setBackgroundResource(R.drawable.custom_edittext);
                    }
                    else{
                        price_entry.setBackgroundResource(R.drawable.custom_edittext_error);
                    }
                }
                return false;
            }
        });

//        btn = findViewById(R.id.btn_test);
//        textView = findViewById(R.id.text_test);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Cursor res = myDb.readAllData();
//                if (res.getCount()==0){
//                    textView.setText("user malumot yoq");
//                    return ;
//                }
//                StringBuffer buffer = new StringBuffer();
//                while(res.moveToNext()){
//                    buffer.append(" id "+res.getString(0));
//                    buffer.append(" name "+res.getString(1));
//                    buffer.append(" tel_number "+res.getString(2));
//                    buffer.append(" password "+res.getString(3));
//                    buffer.append(" tel_password "+res.getString(4));
//                    buffer.append(" token "+res.getString(5));
//                }
//                textView.setText(buffer);
//                Cursor card = myDb.readCards();
//                if (card.getCount()==0){
//                    textView.setText("card malumot yoq");
//                    return ;
//                }
//                StringBuffer bufferCard = new StringBuffer();
//                while(card.moveToNext()){
//                    bufferCard.append(" id "+card.getString(0));
//                    bufferCard.append(" user_id "+card.getString(1));
//                    bufferCard.append(" card_number "+card.getString(2));
//                    bufferCard.append(" live_time "+card.getString(3));
//                    bufferCard.append(" total_price "+card.getString(4));
//                }
//                textView.setText(bufferCard);
//
//
//            }
//        });
//        boolean t = myDb.addUser("1","932840470","tohir abdullayev","asasfds908098sd0f80sd9f");
//        mydb = new MyDataBaseHelper(PaymentActivity.this);
//        user_id = new ArrayList<>();
//        user_name = new ArrayList<>();
//        user_tel_number = new ArrayList<>();
//        user_device_token = new ArrayList<>();

//        storageDataInArray();
//        paymentStoriesAdapter = new PaymentStoriesAdapter(PaymentActivity.this,user_id,user_name,user_tel_number,user_device_token);
//        recyclerView.setAdapter(paymentStoriesAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(PaymentActivity.this));
    }


}