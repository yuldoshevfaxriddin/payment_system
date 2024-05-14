package com.example.imkonbank;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaymentStoriesAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    String data;
    JSONArray jsonArray;
    int listPaymentsSize;
    public PaymentStoriesAdapter(Context context,JSONArray res, int listPaymentsSize){
        this.context = context;
        this.jsonArray = res;
        this.listPaymentsSize = listPaymentsSize;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return listPaymentsSize;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.activity_payments_item,null);
        TextView user_owner_name = (TextView) convertView.findViewById(R.id.user_owner_name);
        TextView user_client_name = (TextView) convertView.findViewById(R.id.user_client_name);
        TextView user_owner_card = (TextView) convertView.findViewById(R.id.user_owner_card);
        TextView user_client_card = (TextView) convertView.findViewById(R.id.user_client_card);
        TextView payment_status = (TextView) convertView.findViewById(R.id.payment_status);
        TextView payment_date = (TextView) convertView.findViewById(R.id.payment_date);
        TextView payment_price = (TextView) convertView.findViewById(R.id.payment_price);

//        JSONObject = jsonArray.getJSONObject(0);

        String user_owner_name_text = "";
        String user_client_name_text= "";
        String user_owner_card_text= "";
        String user_client_card_text= "";
        String payment_status_text= "";
        String payment_date_text= "";
        String payment_price_text= "";

        user_owner_name.setText("Yuboruvchi : "+ "abdullayev jamshid "+user_owner_name_text);
        user_client_name.setText("Qabul qiluvchi : "+ "toshpolatov shaxzodbek"+user_client_name_text);
        user_owner_card.setText("Yuboruvchi card : "+ "9860190104600001"+user_owner_card_text);
        user_client_card.setText("Qabul qiluvchi card : "+ "9860190104601181"+user_client_card_text);
        payment_status.setText("Status : "+ "amalga oshgan"+payment_status_text);
        payment_date.setText("Sana : "+ "12.12.2024"+payment_date_text);
        payment_price.setText("Summa : "+ "45000"+payment_price_text);

        return convertView;
    }
}
