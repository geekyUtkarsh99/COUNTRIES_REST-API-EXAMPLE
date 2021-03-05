package com.example.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.ByteString;

public class getRestData {

    private static final String TAG = "getRestData.java";

    private OkHttpClient client = null;
    private String data = null;
    private InputStream flag = null;

    public getRestData(){
        client = new OkHttpClient();
    }

    public void getArray(){

        final String url = "https://restcountries.eu/rest/v2/region/asia";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage(),e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try {
                    data = Objects.requireNonNull(response.body()).string();
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getMessage(),e);
                }
            }
        });

    }

    public void getFlag(String url){

        Request request = new Request.Builder().url(url).build();

            client.newCall(request).enqueue(new Callback() {

                @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e(TAG, "onFailure: " + e.getMessage(),e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    flag = Objects.requireNonNull(response.body()).byteStream();
                Log.d(TAG, "onResponse: " + flag);
            }
        });


    }

    public InputStream getFlag(){
        return flag;
    }

    public String getData() {
        return data;
    }
}
