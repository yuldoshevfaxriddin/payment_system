package com.example.imkonbank;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentStoriesActivity extends AppCompatActivity {

    ListView listView;
    int  listPaymentsSize = 0;
    JSONArray allRespons = null;
    String HOST_SERVER = "http://192.168.43.105:8000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_stories);
        listView = findViewById(R.id.payments_render);
        String user_id = "";
        MyDataBaseHelper mydb = new MyDataBaseHelper(PaymentStoriesActivity.this);
        Cursor userTable = mydb.readAllData();
        if (userTable.getCount()!=0){
            while(userTable.moveToNext()){
                user_id = userTable.getString(0);
            }
        }
        String requestUrl = "payments-stories?user_id="+user_id;

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status = null;
                try {
                    status = response.getString("status");
                    if(status.equals("succes")){
                        JSONArray payments = response.getJSONArray("payments");
                        allRespons = payments;
                        listPaymentsSize = payments.length();
                    }
                } catch (JSONException e) {
                    Toast.makeText(PaymentStoriesActivity.this, "ma'lumotlarni o'qishda xatolik ", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PaymentStoriesActivity.this, "Tarmoqda xatolik !", Toast.LENGTH_SHORT).show();
            }
        });

        PaymentStoriesAdapter paymentStoriesAdapter = new PaymentStoriesAdapter(getApplicationContext(),allRespons,listPaymentsSize);
        listView.setAdapter(paymentStoriesAdapter);
    }
}