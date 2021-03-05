package com.example.app.recyclemodel;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;

import com.pixplicity.sharp.Sharp;

import org.w3c.dom.Text;

import java.io.InputStream;

public class dataHolder extends RecyclerView.ViewHolder {

    //items to display
    private TextView country,capital,region,subregion,border,population,languages;
    private ImageView flag = null;

    //base constructor
    public dataHolder(@NonNull View itemView) {
        super(itemView);
        country = (TextView)itemView.findViewById(R.id.country);
        capital = (TextView)itemView.findViewById(R.id.capital);
        region = (TextView)itemView.findViewById(R.id.region);
        subregion = (TextView)itemView.findViewById(R.id.Subregion);
        border = (TextView)itemView.findViewById(R.id.border);
        population = (TextView)itemView.findViewById(R.id.population);
        languages = (TextView)itemView.findViewById(R.id.languages);
        flag = (ImageView)itemView.findViewById(R.id.flag);
    }




    public ImageView getFlag() {
        return flag;
    }

    public void setFlag(ImageView flag) {
        this.flag = flag;
    }

    public TextView getCountry() {
        return country;
    }

    public void setCountry(TextView country) {
        this.country = country;
    }

    public TextView getCapital() {
        return capital;
    }

    public void setCapital(TextView capital) {
        this.capital = capital;
    }

    public TextView getRegion() {
        return region;
    }

    public void setRegion(TextView region) {
        this.region = region;
    }

    public TextView getSubregion() {
        return subregion;
    }

    public void setSubregion(TextView subregion) {
        this.subregion = subregion;
    }

    public TextView getBorder() {
        return border;
    }

    public void setBorder(TextView border) {
        this.border = border;
    }

    public TextView getPopulation() {
        return population;
    }

    public void setPopulation(TextView population) {
        this.population = population;
    }

    public TextView getLanguages() {
        return languages;
    }

    public void setLanguages(TextView languages) {
        this.languages = languages;
    }


}
