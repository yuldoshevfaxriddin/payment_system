package com.example.imkonbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentStoriesAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    JSONArray jsonArray;
    String user_id;
    int listPaymentsSize;
    public PaymentStoriesAdapter(Context context,JSONArray res, int listPaymentsSize,String user_id){
        this.context = context;
        this.jsonArray = res;
        this.listPaymentsSize = listPaymentsSize;
        this.user_id = user_id;
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

        String user_owner_name_text = "";
        String user_client_name_text= "";
        String user_owner_card_text= "";
        String user_client_card_text= "";
        String payment_status_text= "";
        String payment_date_text= "";
        String payment_price_text= "";
        String currentUserId = "";
        try {
            JSONObject data = jsonArray.getJSONObject(position);
            JSONObject payment_info = data.getJSONObject("payment_info");

            user_owner_name_text = data.getString("user_owner_info");
            user_client_name_text = data.getString("user_client_info");
            user_owner_card_text = payment_info.getString("card_id_1");
            user_client_card_text = payment_info.getString("card_id_2");
            payment_status_text = payment_info.getString("status");
            payment_date_text = payment_info.getString("created_at");
            payment_price_text = payment_info.getString("price");
            currentUserId = payment_info.getString("user_id_1");
            if(payment_status_text.equals("1")){
                payment_status_text = "bajarildi";
                payment_status.setTextColor(convertView.getResources().getColor(R.color.done));
            }else {
                payment_status_text = "jarayonda";
                payment_status.setTextColor(convertView.getResources().getColor(R.color.wait));
            }

            if(! currentUserId.equals(user_id)){
                payment_price.setTextColor(convertView.getResources().getColor(R.color.status));
                payment_price_text = "+"+payment_price_text;
            }
            else {
                payment_price.setTextColor(convertView.getResources().getColor(R.color.non_status));
                payment_price_text = "-"+payment_price_text;
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        user_owner_name.setText("Yuboruvchi : "+user_owner_name_text);
        user_client_name.setText("Qabul qiluvchi : "+user_client_name_text);
        user_owner_card.setText("Yuboruvchi card : "+user_owner_card_text);
        user_client_card.setText("Qabul qiluvchi card : "+user_client_card_text);
        payment_status.setText("Status : "+payment_status_text);
        payment_date.setText("Sana : "+ payment_date_text);
        payment_price.setText("Summa : "+payment_price_text);

        return convertView;
    }
}
