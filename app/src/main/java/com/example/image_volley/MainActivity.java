package com.example.image_volley;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.image_volley.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String url="https://jsonplaceholder.typicode.com/photos";
    ArrayList<Data_modal> datalist=new ArrayList<>();
ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding=ActivityMainBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());

        GridLayoutManager manager=new GridLayoutManager(this,2);
        binding.recycler.setLayoutManager(manager);
        if (isonline()) {
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray=new JSONArray(response);
                        for (int i=0;i< jsonArray.length();i++)
                        {
                            JSONObject mainobj=jsonArray.getJSONObject(i);
                            String url=mainobj.getString("url");
                            String thumbnailUrl=mainobj.getString("thumbnailUrl");
                            Data_modal modal=new Data_modal();
                            modal.setUrl(url);
                            modal.setThumbUrl(thumbnailUrl);
                            datalist.add(modal);

                        }
                        Adapter_image adapter_image=new Adapter_image(MainActivity.this,datalist);
                        binding.recycler.setAdapter(adapter_image);
                    }
                    catch (JSONException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(stringRequest);
        }
        else {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Error.....!");
            builder.setMessage("Check Your Internet Connection...");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.show();
        }
    }

    private boolean isonline() {
        ConnectivityManager manager= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        return(info != null && info.isConnected());
    }
}