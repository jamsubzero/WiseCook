package com.mit.jamsubzero.wisecook.IngSearch;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mit.jamsubzero.wisecook.Data.UserIngredient;
import com.mit.jamsubzero.wisecook.R;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by jamsubzero on 4/20/2016.
 */

public class CustomIngAddAdapter extends ArrayAdapter<UserIngredient> {

    private ArrayList<UserIngredient> items;
    private ArrayList<UserIngredient> itemsAll;
    private ArrayList<UserIngredient> suggestions;

    private int viewResourceId;
    Context context;

    @SuppressWarnings("unchecked")
    public CustomIngAddAdapter(Context context, int viewResourceId,
                               ArrayList<UserIngredient> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<UserIngredient>) items.clone();
        this.suggestions = new ArrayList<UserIngredient>();
        this.viewResourceId = viewResourceId;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        UserIngredient ingredient = items.get(position);
        if (ingredient != null) {
            ImageView icon = (ImageView) v.findViewById(R.id.ingAddIcon);
            TextView ingName = (TextView) v.findViewById(R.id.ingAddName);
            TextView ingAssoc = (TextView) v.findViewById(R.id.ingAddAssoc) ;

        //    icon.setImageResource(R.drawable.parsley1); // TODO <fixed> uncomnment TO Do below replace later with ID of ingredient, use ingredient.getCode
            icon.setTag(ingredient.getCode());

            DecimalFormat df = new DecimalFormat("###,###.##");

            String name = ingredient.getIngName() ;
            double q = ingredient.getQuantity();
            String quan = "";
            String unit = "";
            String ingDetails = "";
              if( q > 0) { //quantity specified
                 quan = df.format(q);
                 unit = ingredient.getChoosenUnit();
                 ingDetails = "<font color = '#000000'>"+name +",</font>"+ "<font color='#669900'>" + " " + quan + " " + unit + "</font>";
              }else{
                 ingDetails = "<font color='#669900'>" + name + "</font>";
              }

            ingName.setText(Html.fromHtml(ingDetails));
            ingName.setTag(ingredient.getIngName());

            ingAssoc.setText(ingredient.getOtherName());
            ingAssoc.setTag(ingredient.getOtherName());

            //TODO <fixed> comment for now to build faster
            String mDrawableName = "ing"+ingredient.getCode();
            try {
                int resID = context.getResources().getIdentifier(mDrawableName, "drawable", context.getPackageName());
                icon.setImageResource(resID);
            }catch(Exception e){
                icon.setImageResource(R.drawable.parsley1);
            }

        }
        return v;
    }


 //=========



    public ArrayList<UserIngredient> getList(){
        return itemsAll;
    }


}//classEND