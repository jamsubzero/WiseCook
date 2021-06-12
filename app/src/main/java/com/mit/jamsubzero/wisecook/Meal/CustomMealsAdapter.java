package com.mit.jamsubzero.wisecook.Meal;

import android.content.Context;
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

public class CustomMealsAdapter extends ArrayAdapter<Meal> {

    private ArrayList<Meal> mealList;
Context context;
    int selectedIndex = -1;
    public CustomMealsAdapter(Context context, int textViewResourceId,
                              ArrayList<Meal> mealList) {
        super(context, textViewResourceId, mealList);
        this.mealList = new ArrayList<Meal>();
        this.mealList.addAll(mealList);
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

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.meal_info, parent, false);

            holder = new ViewHolder();
          //  holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.name = (RadioButton) convertView.findViewById(R.id.meal);
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

        Meal meal = mealList.get(position);
    //    holder.code.setText(" (" + meal.getCode() + ")");
        holder.name.setText(meal.getName());

        return convertView;

    }


    public void setSelectedIndex(int index){
        selectedIndex = index;
    }

    public ArrayList<Meal> getList(){
      return  mealList;
    }

}