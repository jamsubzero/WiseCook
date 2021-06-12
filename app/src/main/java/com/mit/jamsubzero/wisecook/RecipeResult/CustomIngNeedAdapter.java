package com.mit.jamsubzero.wisecook.RecipeResult;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mit.jamsubzero.wisecook.Data.Singleton;
import com.mit.jamsubzero.wisecook.Data.UserIngredient;
import com.mit.jamsubzero.wisecook.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jamsubzero on 4/20/2016.
 */

public class CustomIngNeedAdapter extends ArrayAdapter<IngNeed> {
    private ArrayList<IngNeed> ingNeedList;
    Context context;
    private final int INC_GOOD = 0;
    private final int INC_LACK = 1;
    private final int INC_BAD = 2;
    private final int INC_DEF = 3;
    HashMap <Integer, Integer>colorList = new HashMap<>();
    public CustomIngNeedAdapter(Context context, int textViewResourceId,
                                ArrayList<IngNeed> ingNeedList) {
        super(context, textViewResourceId, ingNeedList);
        this.ingNeedList = new ArrayList<IngNeed>();
        this.ingNeedList.addAll(ingNeedList);
        this.context = context;
    }

    private class ViewHolder {
        TextView ingNeedName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rec_ings_info, parent, false);

            holder = new ViewHolder();

            holder.ingNeedName = (TextView) convertView.findViewById(R.id.ingNeedName);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

      /*  if(position % 2 == 0){
            convertView.setBackgroundColor(Color.WHITE);
        }else{
            convertView.setBackgroundColor(Color.parseColor("#FFD6D7D7"));
        }
*/
        IngNeed ingredient = ingNeedList.get(position);

        //IngNeed(int needID, int ingCode, double newQuan, String item, String stat, String unit)

        double recQuan = ingredient.getNewQuan();
        String recUnit = ingredient.getUnit();


        UserIngredient userIngredient = null;

      //  String unitsDet = "";

        DecimalFormat df = new DecimalFormat("###,###.##");

        if(ingredient.getIngCode()!= null && ingredient.getIngCode().length()!=0) { // null and "" --> empty

           if (Singleton.selIngs.containsKey(Integer.parseInt(ingredient.getIngCode()))) {//GOOD
               userIngredient = Singleton.selIngs.get(Integer.parseInt(ingredient.getIngCode()));
               double userQuan = userIngredient.getQuantity();
               String choosenUnit = userIngredient.getChoosenUnit();
               String defUnit = userIngredient.getDefaultUnt();
               if (!(choosenUnit.equals(defUnit))) {
                   userQuan = UnitConverter.convertVolume(choosenUnit, userQuan, defUnit);
               }
               if (userQuan == -1) { //-1 means the user have eonugh
                   colorList.put(position, this.INC_GOOD);
               }
               else{
                       if (userQuan >= recQuan) { //more than or equal
                           colorList.put(position, this.INC_GOOD); //GREEN
                       } else if (userQuan < recQuan) { // if lacking
                           colorList.put(position, this.INC_LACK);
                       }
               }
             //  unitsDet = "--[[o:"+df.format(recQuan)+", u:"+df.format(userQuan)+"]]";

            } else {
                    colorList.put(position, this.INC_BAD);//BAD
            }
        }else{
            colorList.put(position, this.INC_DEF);//DEFAULT

        }
        //=============
        int rowType = colorList.get(position);
        switch (rowType){
            case 0:
             //   convertView.setBackgroundColor(Color.GREEN);
                holder.ingNeedName.setTextColor(Color.GREEN);
                break;
            case 1:
                holder.ingNeedName.setTextColor(Color.parseColor("#FF8800"));//Orange
                break;
            case 2:
              //  convertView.setBackgroundColor(Color.RED);
                holder.ingNeedName.setTextColor(Color.RED);
                break;
            case 3:
             //   convertView.setBackgroundColor(Color.parseColor("#00FF0000"));
                holder.ingNeedName.setTextColor(Color.BLACK);
                break;
        }


        String quan_unit_name = "";

        if(recQuan > 0) {
          //format it first
            quan_unit_name = quan_unit_name + df.format(recQuan) + " ";
            recUnit = ingredient.getUnit() + "".trim();
            if (!recUnit.isEmpty()) {
                quan_unit_name = quan_unit_name + recUnit + " "; // Add space first

            }
        }
//==================================================

        quan_unit_name = quan_unit_name + ingredient.getItem() ; // this is SURE, every item must have a name

        String stat =  ingredient.getStat()+"".trim();
        if(!stat.isEmpty()){  // Add , if stat NOT empty
            stat = ", " +  stat;
        }

        String sourceString = "<b>" + quan_unit_name + "</b>" + stat;
        holder.ingNeedName.setText(Html.fromHtml(sourceString));

        return convertView;

    }


    public ArrayList<IngNeed> getList(){
      return  ingNeedList;
    }




}