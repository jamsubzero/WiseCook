package com.mit.jamsubzero.wisecook.Bookmark;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mit.jamsubzero.wisecook.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jamsubzero on 4/20/2016.
 */

public class CustomRecipeBookAdapter extends ArrayAdapter<RecipeBook> {

    private ArrayList<RecipeBook> recipeList;
Context context;

    int selectedIndex = -1;

    public CustomRecipeBookAdapter(Context context, int textViewResourceId,
                                   ArrayList<RecipeBook> recipeList) {
        super(context, textViewResourceId, recipeList);
        this.recipeList = new ArrayList<RecipeBook>();
        this.recipeList.addAll(recipeList);
        this.context = context;
    }

    private class ViewHolder {
    ImageView icon;
    TextView name;
        TextView date;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipebook_list_info, parent, false);

            holder = new ViewHolder();
           holder.icon = (ImageView) convertView.findViewById(R.id.recBookIcon);
            holder.name = (TextView) convertView.findViewById(R.id.recBookName);
            holder.date = (TextView) convertView.findViewById(R.id.recBookDate);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


        RecipeBook recipe = recipeList.get(position);

        holder.name.setText(Html.fromHtml(recipe.getName())); // coz of &quote;
        holder.icon.setImageResource(R.drawable.ico1); //TODO <fixed> uncomment To Do below
        holder.date.setText(recipe.getDate());



        //TODO <fixed> comment for now for faster build
        try {
            Drawable back = Drawable.createFromStream(context.getResources().getAssets().open("recipe_images/ico"+recipe.getCode()+".jpg"),null);
           holder.icon.setImageDrawable(back);
        }catch (Exception iox){
            holder.icon.setImageResource(R.drawable.ico1);
        }

              return convertView;

    }




    public ArrayList<RecipeBook> getList(){
      return  recipeList;
    }

}