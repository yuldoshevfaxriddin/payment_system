package com.example.imkonbank;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DevicesFragment extends Fragment {

    JSONArray jsonArray;
    int jsonArraySize;
    ListView listView;
    String HOST_SERVER = "http://192.168.209.105:8000";
    String device_token= "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.devices_render);
        String user_id = "";

        MyDataBaseHelper mydb = new MyDataBaseHelper(getContext());
        Cursor userTable = mydb.readAllData();
        if (userTable.getCount()!=0){
            while(userTable.moveToNext()){
                user_id = userTable.getString(0);
                device_token = userTable.getString(5);
            }
        }
        String requestUrl = HOST_SERVER+"/devices?user_id="+user_id+"&device_token="+device_token;

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status = null;
                try {
                    status = response.getString("status");
                    if(status.equals("succes")){
                        JSONArray payments = response.getJSONArray("devices");
                        jsonArray = payments;
                        jsonArraySize = payments.length();
                        DevicesViewAdapter devicesViewAdapter = new DevicesViewAdapter(getActivity(),jsonArray,jsonArraySize,device_token);
                        listView.setAdapter(devicesViewAdapter);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Tarmoqda xatolik !", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonRequest);

    }
}