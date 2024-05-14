package com.example.imkonbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DevicesViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    JSONArray jsonArray;
    int jsonArraySize;
    public DevicesViewAdapter(Context context, JSONArray jsonArray, int jsonArraySize ){
        this.context = context;
        this.jsonArray = jsonArray;
        this.jsonArraySize = jsonArraySize;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return jsonArraySize;
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
        convertView = layoutInflater.inflate(R.layout.devices_row, null);

        TextView id = (TextView) convertView.findViewById(R.id.user_id);
        TextView device_name = (TextView) convertView.findViewById(R.id.device_name);
        TextView ip_address = (TextView) convertView.findViewById(R.id.ip_address);
        TextView connected_time = (TextView) convertView.findViewById(R.id.connected_time);
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.device_layout);

        String d="",i="",t="";
        int id_number = position+1;
        try {
            JSONObject data = jsonArray.getJSONObject(position);
            d = data.getString("device_info");
            i = data.getString("ip_addres");
            t = data.getString("created_at");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        id.setText(""+id_number);
        device_name.setText(d);
        ip_address.setText(i);
        connected_time.setText(t);

        return convertView;
    }
}