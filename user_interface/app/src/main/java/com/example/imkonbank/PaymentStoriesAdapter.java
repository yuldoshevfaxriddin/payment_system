package com.example.imkonbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PaymentStoriesAdapter extends RecyclerView.Adapter<PaymentStoriesAdapter.MyViewHolder> {
    Context context;
    ArrayList user_id,user_tel_number,user_name,user_device_token;
    PaymentStoriesAdapter(Context context,
                          ArrayList user_id,
                          ArrayList user_tel_number,
                          ArrayList user_name,
                          ArrayList user_device_token){
        this.context = context;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_tel_number = user_tel_number;
        this.user_device_token = user_device_token;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.user_id.setText(String.valueOf(user_id.get(position)));
        holder.username.setText(String.valueOf(user_name.get(position)));
        holder.tel_number.setText(String.valueOf(user_tel_number.get(position)));
        holder.device_token.setText(String.valueOf(user_device_token.get(position)));
    }

    @Override
    public int getItemCount() {
        return  user_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView user_id, username, tel_number, device_token;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            user_id = itemView.findViewById(R.id.user_id);
            username = itemView.findViewById(R.id.username);
            tel_number = itemView.findViewById(R.id.tel_number);
            device_token = itemView.findViewById(R.id.device_token);
//            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
//            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
//            mainLayout.setAnimation(translate_anim);
        }
    }
}
