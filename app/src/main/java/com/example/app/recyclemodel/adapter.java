package com.example.app.recyclemodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.BuildConfig;
import com.example.app.R;
import com.example.app.getRestData;
import com.example.app.roomDatabase.Map;
import com.example.app.roomDatabase.appDataHandler;

import com.example.app.roomDatabase.databaseObj;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.pixplicity.sharp.Sharp;
import com.pixplicity.sharp.SharpDrawable;
import com.pixplicity.sharp.SharpPicture;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okio.ByteString;

public class adapter extends RecyclerView.Adapter<dataHolder> {

    //TAG--
    private static final  String TAG = "adapter.java";

    private JSONArray data = null;
    private InputStream stream;
    private boolean network;
    Context context = null;
    private boolean isLoadingAdded = false;
    public int showing = 0;
    private List<JSONArray> dataSet = new ArrayList<>();
    private List<Map> secondaryData = null;

    public adapter(JSONArray data, Context context,boolean network){
        this.network = network;

        if (network)
        dataSet.add(data);
        else {
            appDataHandler handler = appDataHandler.getInstance(context);
            databaseObj obj = handler.obj();
            secondaryData = obj.allMaps();


        }
        this.context = context;

    }

    @NonNull
    @Override
    public dataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_model,parent,false);

        return new dataHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull dataHolder holder, int position) {

        Bitmap b = null;
        stream = null;
        JSONObject object = null;

        try {
            if (network) {
                //load on recycler view items--
                object = dataSet.get(0).getJSONObject(position);
                holder.getCountry().setText(object.getString("name"));
                holder.getCapital().setText(object.getString("capital"));
                holder.getRegion().setText(object.getString("region"));
                holder.getSubregion().setText(object.getString("subregion"));
                holder.getPopulation().setText(object.getString("population"));

                getRestData getImage = new getRestData();
                getImage.getFlag(object.getString("flag"));

                while (stream == null) {
                    stream = getImage.getFlag();
                }

                //load .svg images from rest api--
                Sharp.loadInputStream(stream).into(holder.getFlag());

                String borders = " ";
                JSONArray array = object.getJSONArray("borders");

                for (int i = 0; i < array.length(); i++) {
                    if (array.get(i) != null)
                        borders += array.get(i) + " , ";
                }


                holder.getBorder().setText(borders);

                String languages = " ";
                JSONArray array1 = object.getJSONArray("languages");

                for (int i = 0; i < array1.length(); i++) {
                    languages += (array1.getJSONObject(i).getString("name")) + " , ";
                }

                holder.getBorder().setText(borders);
                holder.getLanguages().setText(languages);

            }else {


                holder.saveData.setEnabled(false);
                holder.getBorder().setText(secondaryData.get(position).border);
                holder.getCapital().setText(secondaryData.get(position).capital);
                holder.getCountry().setText(secondaryData.get(position).countryName);

                holder.getFlag().setImageDrawable(Drawable.createFromPath(secondaryData.get(position).flag));
                holder.getLanguages().setText(secondaryData.get(position).language);
                holder.getPopulation().setText(secondaryData.get(position).population);
                holder.getRegion().setText(secondaryData.get(position).region);
                holder.getSubregion().setText(secondaryData.get(position).subregion);

            }

            holder.saveData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    appDataHandler  handler = appDataHandler.getInstance(context);

                    databaseObj obj = handler.obj();

                    Map m = new Map();
                    m.countryName = holder.getCountry().getText().toString().trim();
                    m.capital = holder.getCapital().getText().toString().trim();

                    String d = holder.getFlag().getDrawable().toString();
                    m.flag = d;
                    m.border = holder.getBorder().getText().toString().trim();
                    m.language = holder.getLanguages().getText().toString().trim();
                    m.population = holder.getPopulation().getText().toString().trim();
                    m.region = holder.getRegion().getText().toString().trim();
                    m.subregion = holder.getSubregion().getText().toString().trim();

                    obj.insert(m);
//                    Log.d(TAG, "onClick:  check insert" );

                    Toast.makeText(context,"SAVED",Toast.LENGTH_SHORT).show();

                }
            });

        } catch (JSONException e) {
            Log.d(null, "onBindViewHolder: " + e.getMessage());
        }
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (network)
        return dataSet.get(0).length();
        else return secondaryData.size();
    }

    public JSONArray getData() {
        return data;
    }



    public void addList(JSONArray semiList)  {

        dataSet.clear();
        dataSet.add(semiList);
        notifyItemInserted(dataSet.get(0).length() - 1);

        Log.d(TAG, "addList: " + dataSet.get(0).toString());

    }
}
