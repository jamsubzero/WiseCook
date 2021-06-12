package com.mit.jamsubzero.wisecook.Country;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.mit.jamsubzero.wisecook.R;
import com.mit.jamsubzero.wisecook.Data.Singleton;

import java.util.ArrayList;

/**
 * Created by jamsubzero on 4/20/2016.
 */

public class CustomCuisinesAdapter extends ArrayAdapter<Country> {

    private ArrayList<Country> countryList;
Context context;

    static int selectedIndex = -1;

    public CustomCuisinesAdapter(Context context, int textViewResourceId,
                                 ArrayList<Country> countryList) {
        super(context, textViewResourceId, countryList);
        this.countryList = new ArrayList<Country>();
        this.countryList.addAll(countryList);
        this.context = context;
    }

    private class ViewHolder {
      //  TextView code;
        RadioButton name;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.country_info, parent, false);

            holder = new ViewHolder();
          //  holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.name = (RadioButton) convertView.findViewById(R.id.country);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(selectedIndex == position){
            holder.name.setChecked(true);

        }
        else{
            holder.name.setChecked(false);
        }

        Country country = countryList.get(position);
    //    holder.code.setText(" (" +  country.getCode() + ")");
        holder.name.setText(country.getName());
//        holder.name.setChecked(country.isSelected());
/*
        if ( position % 2 == 0) {
          convertView.setBackgroundColor(Color.RED);
        } else {
            convertView.setBackgroundColor(Color.YELLOW);
        }*/

              return convertView;

    }



    public void setSelectedIndex(int index){
        selectedIndex = index;
    }

    public ArrayList<Country> getList(){
      return  countryList;
    }

}