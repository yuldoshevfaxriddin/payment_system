package com.example.imkonbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
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
    Button paymentButton;
    EditText client_card_number,price_entry;
    TextView client_card_name,total_price_info;
    String HOST_SERVER = "http://192.168.209.105:8000";
    String transaction_key = "";
    TextView card_owner_number,card_client_number,card_owner_name,card_client_name,transaction_price,transaction_date_time,transaction_key_view;
    Button paymentDone,paymentCancel;
    CardView cardView,cardViewPayment;
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

        card_owner_name = findViewById(R.id.card_owner_name);
        card_owner_number = findViewById(R.id.card_owner_number);
        card_client_name = findViewById(R.id.card_client_name);
        card_client_number = findViewById(R.id.card_client_number);
        transaction_price = findViewById(R.id.transaction_price);
        transaction_date_time = findViewById(R.id.transaction_date_time);
        transaction_key_view = findViewById(R.id.transaction_key);
        paymentDone = findViewById(R.id.paymentDone);
        paymentCancel = findViewById(R.id.paymentCancel);
        cardView = findViewById(R.id.cardView);
        cardViewPayment = findViewById(R.id.cardViewPayment);

        String device_token = getIntent().getExtras().getString("device_token");
        String total_price = getIntent().getExtras().getString("total_price");
        String owner_card_number = getIntent().getExtras().getString("card_number");

        cardView.getLayoutParams().height = 0;

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentButton.setEnabled(false);
                String urlPayment = HOST_SERVER+"/payment-info?card_owner_number="+owner_card_number+"&card_client_number="+client_card_number.getText()+"&transaction_price="+price_entry.getText();
                        JsonObjectRequest paymenInfo = new JsonObjectRequest(Request.Method.GET, urlPayment, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    String message = response.getString("message");
                                    if (status.equals("succes")) {
                                        price_entry.setEnabled(false);
                                        ViewGroup.LayoutParams params1 = cardViewPayment.getLayoutParams();
                                        ((ViewGroup.LayoutParams) params1).height = 0;
                                        cardViewPayment.setLayoutParams(params1);

                                        ViewGroup.LayoutParams params2 = cardView.getLayoutParams();
                                        ((ViewGroup.LayoutParams) params2).height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                        cardView.setLayoutParams(params2);

                                        JSONObject card_owner = response.getJSONObject("card_owner");
                                        JSONObject card_owner_info = response.getJSONObject("card_owner_info");
                                        JSONObject card_client = response.getJSONObject("card_client");
                                        JSONObject card_client_info = response.getJSONObject("card_client_info");
                                        JSONObject transaction = response.getJSONObject("transaction");
                                        card_owner_number.setText("Yuboruvchi: "+card_owner_info.getString("card_number"));
                                        card_owner_name.setText("Yuboruvchining ismi: "+card_owner.getString("name"));
                                        card_client_number.setText("Qabul qiluvchi: "+card_client_info.getString("card_number"));
                                        card_client_name.setText("Qabul qiluvchining ismi: "+card_client.getString("name"));
                                        transaction_key = transaction.getString("transaction_key");
                                        transaction_key_view.setText("Transaction key: "+transaction_key);
                                        transaction_price.setText("O'tkazilayotgan summa: "+transaction.getString("transaction_price"));
                                        transaction_date_time.setText("Vaqti: "+transaction.getString("transaction_time"));
                                    }
//                                    Toast.makeText(PaymentActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                                catch (JSONException e){
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
                            }
                        });
                    RequestQueue requestQueue = Volley.newRequestQueue(PaymentActivity.this);
                    requestQueue.add(paymenInfo);
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
                                    String client_credit_card = card_number;
                                    client_card_name.setText(client_name);
                                    client_card_number.setEnabled(true);
                                    price_entry.getLayoutParams().height = client_card_number.getLayoutParams().height;
                                    total_price_info.setText("Hisobingizda " +total_price+" so'm bor");
                                }
                                else{
                                    client_card_number.setBackgroundResource(R.drawable.custom_edittext_error);
                                    client_card_name.setText("");
                                    client_card_number.setEnabled(true);
                                }
//                                Toast.makeText(PaymentActivity.this, message, Toast.LENGTH_SHORT).show();
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

        paymentDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doneUrl = HOST_SERVER+"/payment?transaction_key="+transaction_key;
                JsonObjectRequest requestCardInfo = new JsonObjectRequest(Request.Method.GET, doneUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");
                            if (status.equals("succes")) {
                                // data bazaga pul yoz
                                JSONObject card_info = response.getJSONObject("card_info");
                                String total_price = card_info.getString("total_price");
                                String card_number = card_info.getString("card_number");
                                myDb.updateCard(card_number,total_price);
                                Intent loginIntent = new Intent(PaymentActivity.this, LoginActivity.class);
                                startActivity(loginIntent);
                            }
//                            Toast.makeText(PaymentActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(PaymentActivity.this, "malumotlarni o'qishda xatolik", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PaymentActivity.this, "server bilan bog'lanishda xatolik", Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(PaymentActivity.this);
                requestQueue.add(requestCardInfo);
            }
        });
        paymentCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PaymentActivity.this, "cancel", Toast.LENGTH_SHORT).show();
            }
        });

    }


}