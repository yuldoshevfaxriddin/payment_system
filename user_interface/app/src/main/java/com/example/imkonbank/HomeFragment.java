package com.example.imkonbank;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    MyDataBaseHelper myDb = new MyDataBaseHelper(getActivity());
//    Cursor card = myDb.readCards();
    TextView payment_btn;
    TextView payment_stories,card_owner,card_number,card_live_time,card_total_price;
    String device_token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        payment_btn = view.findViewById(R.id.payment);
        payment_stories = view.findViewById(R.id.payment_stories);

        card_number = view.findViewById(R.id.card_number);
        card_owner = view.findViewById(R.id.card_owner);
        card_live_time = view.findViewById(R.id.card_live_time);
        card_total_price = view.findViewById(R.id.card_total_price);

        card_number.setText(getActivity().getIntent().getExtras().getString("card_number"));
        card_owner.setText(getActivity().getIntent().getExtras().getString("card_owner"));
        card_live_time.setText(getActivity().getIntent().getExtras().getString("card_live_time"));
        card_total_price.setText(getActivity().getIntent().getExtras().getString("card_total_price"));
        device_token = getActivity().getIntent().getExtras().getString("device_token");
        payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                intent.putExtra("device_token",device_token);
                intent.putExtra("total_price",card_total_price.getText());
                intent.putExtra("card_number",card_number.getText());
                startActivity(intent);
            }
        });
        payment_stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PaymentStoriesActivity.class);
                startActivity(intent);
            }
        });
}

}