package com.example.app;

/*
 Copyright 2021 Utkarsh choudhary

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.app.recyclemodel.adapter;
import com.example.app.roomDatabase.appDataHandler;
import com.example.app.roomDatabase.databaseObj;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private RecyclerView countries = null;
    private LinearLayoutManager manager = null;
    private getRestData dataCall = null;
    private adapter adapter = null;
    private static final String TAG = "MainActivity.java";
    private int pastVisibleItems;
    private int totalItemCount;
    private int visibleItemCount;
    private boolean loading;
    private JSONArray list = null,semiList = null;
    private ProgressBar loadingBar = null;
    private ProgressBar getItemProgress = null;
    private FloatingActionButton delete = null;

    private static int counter = 0;
    private boolean network;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();
        network = isNetworkAvailable();

        getItemProgress.setVisibility(View.INVISIBLE);
        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        countries.setLayoutManager(manager);
        if (network) {
            dataCall = new getRestData();
            loadRest.start();
            delete.setEnabled(false);
        }else {



            loadingBar.setVisibility(View.INVISIBLE);
            adapter = new adapter(null,this,network);
            countries.setAdapter(adapter);

        }
//        appDataHandler handler = appDataHandler.getInstance(this);
//
//        databaseObj obj = handler.obj();
//        Log.d(TAG, "onCreate: maps" + obj.allMaps().toString());

        //scrollable handler--
        countries.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    loading = true;
                else loading = false;

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (dy > 0 && network) //check for scroll down
                {
                    visibleItemCount = manager.getChildCount();
                    totalItemCount = manager.getItemCount();
                    pastVisibleItems = manager.findFirstVisibleItemPosition();

                    if(loading && visibleItemCount+pastVisibleItems >= totalItemCount){
                        getItemProgress.setVisibility(View.VISIBLE);
                        try {
                            editList();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }



                }
            }
        });
    }

    public void onClick(View v){

        if (v.getId() == R.id.deleteData){

            appDataHandler handler = appDataHandler.getInstance(this);
            databaseObj obj = handler.obj();
            obj.deleteAll();
            Toast.makeText(this,"All data deleted",Toast.LENGTH_SHORT).show();


            adapter = new adapter(null,this,network);
            countries.setAdapter(adapter);


        }

    }

    private void editList() throws JSONException {


         new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
             @Override
             public void run() {
                 if(counter<=50) {
                     try {
                         semiList.put(list.getJSONObject(counter++));
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                     adapter.addList(semiList);
                 }
                 getItemProgress.setVisibility(View.INVISIBLE);
                 Log.d(TAG, "run: length of list " + semiList.length());
             }
         },1000);

    }

    private void getViews(){
    countries = (RecyclerView)findViewById(R.id.countries);
    loadingBar = (ProgressBar)findViewById(R.id.progressBar);
    getItemProgress = (ProgressBar)findViewById(R.id.progressBar2);
    delete = findViewById(R.id.deleteData);
    }

    Thread loadRest = new Thread(){
        @Override
        public void run() {
            super.run();
            dataCall.getArray();
            String d = null;
            //repeated synchronized call
            while (d == null){
                d = dataCall.getData();
            }
            try {
                list = new JSONArray(d);
                semiList = new JSONArray();
                for(int i = 0;i < 5;i++){
                    semiList.put(list.getJSONObject(i));
                    counter++;
                }
                adapter = new adapter(semiList,MainActivity.this,isNetworkAvailable());

                //looper thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        countries.setAdapter(adapter);
                        loadingBar.setVisibility(View.INVISIBLE);
                    }
                });
            } catch (JSONException e) {
                Log.e(TAG, "run: " + e.getMessage(),e);
            }

        }
    };



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            //terminate thread process
            loadRest.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}