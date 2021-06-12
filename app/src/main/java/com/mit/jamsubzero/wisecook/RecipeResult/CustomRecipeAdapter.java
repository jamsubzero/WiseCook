package com.mit.jamsubzero.wisecook.RecipeResult;

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

public class CustomRecipeAdapter extends ArrayAdapter<Recipe> {

    private ArrayList<Recipe> recipeList;
Context context;

    public CustomRecipeAdapter(Context context, int textViewResourceId,
                               ArrayList<Recipe> recipeList) {

        super(context, textViewResourceId, recipeList);
        this.recipeList = new ArrayList<>();
        this.recipeList.addAll(recipeList);
        this.context = context;
    }

    private class ViewHolder {
    ImageView icon;
    TextView name;
   TextView info;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_list_info, parent, false);

            holder = new ViewHolder();
           holder.icon = (ImageView) convertView.findViewById(R.id.recBookIcon);
            holder.name = (TextView) convertView.findViewById(R.id.recipeName);
            holder.info = (TextView) convertView.findViewById(R.id.recipeInfo);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


        Recipe recipe = recipeList.get(position);

        holder.name.setText(Html.fromHtml(recipe.getName())); // Hmtl bcoz of &quote;
        holder.info.setText(recipe.getInfo());
        holder.icon.setImageResource(R.drawable.ico1); //TODO <fixed> uncomment To Do below


        //TODO <fixed> comment for now for faster build
        try {
            Drawable back = Drawable.createFromStream(this.context.getResources().getAssets().open("recipe_images/ico"+recipe.getCode()+".jpg"),null);
           holder.icon.setImageDrawable(back);
        }catch (Exception iox){
            holder.icon.setImageResource(R.drawable.ico1);
        }

              return convertView;

    }




    public ArrayList<Recipe> getList(){
      return  recipeList;
    }

}