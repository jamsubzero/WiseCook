package com.mit.jamsubzero.wisecook.Bookmark;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mit.jamsubzero.wisecook.Data.MyDatabase;
import com.mit.jamsubzero.wisecook.Data.Singleton;
import com.mit.jamsubzero.wisecook.R;
import com.mit.jamsubzero.wisecook.RecipeResult.DisplayRecipe;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jamsubzero on 5/19/2016.
 */
public class BookmarksDialog extends Dialog{

    Context context;

    ListView bookListView;

    CustomRecipeBookAdapter dataAdapter = null;

    MyDatabase db;

    ArrayList<RecipeBook> recipeList;

    int fromAct;
    public BookmarksDialog(Context context) {
        super(context);
        this.fromAct = fromAct;
        this.context = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    } //END of consructor


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.bookmarks_dialog);
        View empty = this.findViewById(R.id.empty);
        db = new MyDatabase(context);

         bookListView = (ListView) this.findViewById(R.id.bookRecList);
        registerForContextMenu(bookListView);
        bookListView.setOnCreateContextMenuListener(this);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bookListView.showContextMenuForChild(view);
            }
        });

        bookListView.setEmptyView(empty);
        loadRecipeBooks();

        determineLayoutBelow();

        Button closeBtn = (Button) this.findViewById(R.id.closeBtn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.isBookmarkDisplayed = false;
                dismiss();
            }
        });

    }


    void determineLayoutBelow(){
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if(recipeList.size()==0){
            relativeParams.addRule(RelativeLayout.BELOW, R.id.empty);
        }else{
            relativeParams.addRule(RelativeLayout.BELOW, R.id.bookRecList);
        }

        this.findViewById(R.id.closeBtn).setLayoutParams(relativeParams);
    }


    private void loadRecipeBooks(){
        recipeList = new ArrayList<>();

        Cursor c = db.getBookmarks();

        while (c.moveToNext()){
            RecipeBook recipeBook = new RecipeBook(c.getInt(0), c.getString(1), c.getString(2));
            recipeList.add(recipeBook);
        }

        dataAdapter = new CustomRecipeBookAdapter(context, R.layout.recipebook_list_info, recipeList);
        bookListView.setAdapter(dataAdapter);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.bookRecList) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            RecipeBook recipeObject = (RecipeBook) bookListView.getItemAtPosition(acmi.position);
            String recName = recipeObject.getName();
            //
            LayoutInflater headerInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ViewGroup header = (ViewGroup) headerInflater.inflate(
                    R.layout.context_menu_header, null);
            //
            ImageView headerIcon = (ImageView) header.findViewById(R.id.headerIcon);
            headerIcon.setImageResource(R.drawable.ico1); //TODO <fixed> Later replace true icon, uncomment below
            //TODO <fixed> comment for now for faster build
            try {
                Drawable back = Drawable.createFromStream(context.getResources().getAssets().open("recipe_images/ico"+recipeObject.getCode()+".jpg"),null);
                headerIcon.setImageDrawable(back);
            }catch (Exception iox){
                headerIcon.setImageResource(R.drawable.ico1);
            }

            TextView title = (TextView) header
                    .findViewById(R.id.headerText);
            title.setText(recName);
            menu.setHeaderView(header);
            //
            menu.add(0, v.getId(), 0, "Display recipe");//groupId, itemId, order, title
            menu.add(0, v.getId(), 0, "Delete");
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
   // public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = acmi.position;
        RecipeBook recipe = (RecipeBook) bookListView.getItemAtPosition(pos);
         int code = recipe.getCode();

        if (item.getTitle().equals("Display recipe")) {
            Singleton.isBookmarkDisplayed = true;
            Intent intent = new Intent(context, DisplayRecipe.class);
            intent.putExtra("recID", code);
            context.startActivity(intent);
            dismiss();
        } else if(item.getTitle().equals("Delete")) {

            if(db.removeBookmark(code)){
                recipeList.remove(pos);
                dataAdapter.notifyDataSetChanged();
                Toast.makeText(context, "Removed " + code, Toast.LENGTH_LONG).show();
                determineLayoutBelow();
            }

        }else{
            return  false;
        }

return true;
    }



}// class ends
