package com.example.imkonbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MyDataBaseHelper mydb ;
    Button btn;
    TextView textView;
    ArrayList<String> user_id,user_name,user_tel_number,user_device_token;
    PaymentStoriesAdapter paymentStoriesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        MyDataBaseHelper myDb = new MyDataBaseHelper(PaymentActivity.this);
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

    public void storageDataInArray(){

        Cursor cursor = mydb.readAllData();
//        if(cursor.getCount()==0){
//            Toast.makeText(this,"malumot yoq",Toast.LENGTH_SHORT);
//        }
//        else{
//            while(cursor.moveToNext()){
//                Toast.makeText(this,cursor.getString(0),Toast.LENGTH_SHORT);
//                user_id.add(cursor.getString(0));
//                user_name.add(cursor.getString(1));
//                user_tel_number.add(cursor.getString(2));
//                user_device_token.add(cursor.getString(3));
//            }
//        }
    }

}