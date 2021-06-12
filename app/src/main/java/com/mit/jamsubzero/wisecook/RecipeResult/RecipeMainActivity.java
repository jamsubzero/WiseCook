package com.mit.jamsubzero.wisecook.RecipeResult;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mit.jamsubzero.wisecook.ActionBarEvents;
import com.mit.jamsubzero.wisecook.Data.MyDatabase;
import com.mit.jamsubzero.wisecook.Data.Singleton;
import com.mit.jamsubzero.wisecook.R;

import java.util.ArrayList;

/**
 * Created by jamsubzero on 4/21/2016.
 */
public class RecipeMainActivity extends AppCompatActivity {
    ListView listView;

    CustomRecipeAdapter dataAdapter = null;

    ArrayList<Recipe> recipeList = new ArrayList<>();

    MyDatabase db;

    Spinner filter;
    ToggleButton order;
    TextView messageResult;
    int algoLevel = -1;

    boolean isFirst = true; // this is for the spinner

    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list_layout);

        MobileAds.initialize(this, "ca-app-pub-8099472786932887~2910125602");
        mAdView = findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        messageResult = (TextView) this.findViewById(R.id.messageResult);

        filter = (Spinner) findViewById(R.id.filter);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isFirst){
                 isFirst = false;
                }else {
                    dataAdapter.clear();
                    new FindRecipe().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        order = (ToggleButton) findViewById(R.id.order);
        order.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataAdapter.clear();
                new FindRecipe().execute();
            }
        });

       listView = (ListView) findViewById(R.id.recipeList);

        db = new MyDatabase(this.getApplicationContext());

        //loadRecipes();
        new FindRecipe().execute(); // this is an asyncTask

        View empty = this.findViewById(R.id.empty);

        listView.setEmptyView(empty);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe recipe = (Recipe)parent.getAdapter().getItem(position);
                int selID = recipe.getCode();
              //  Toast.makeText(RecipeMainActivity.this, selID+"", Toast.LENGTH_SHORT).show();
                Singleton.isBookmarkDisplayed = false;
                Intent intent = new Intent(getApplicationContext(), DisplayRecipe.class);
                intent.putExtra("recID", selID);
                startActivity(intent);
            }
        });


        //getSupportActionBar().setTitle(title);
    }

//=====
private int loadRecipes(String filterBy, String orderBy){  // DO NOT UPDATE UI HERE, coz its called inside doInBackground
 
     Cursor c = db.getRecipeNow(filterBy, orderBy);

    int algoLevel = 1;
    //          r.recId, r.name, r.prep, r.newPrep, r.kal, r.serving, hit

    while (c.moveToNext()){
        String time = converTime(c.getString(2),c.getInt(3));
        String sKal = "";

        Float kal = c.getFloat(4);

        if (kal > 0) {
            sKal = kal + " Cal";
        } else {
            sKal = "N/A";
        }

        String sServing = "";

        int servings = c.getInt(5);

        if (servings > 0) {
            sServing = servings + " servings";
        } else {
            sServing = "N/A";
        }

        //=============================================================
        String recInfo = time + ", " + sKal + ", " + sServing;
        Log.i("recInfo", recInfo);
          //============================================================
        Recipe recipe = new Recipe(c.getInt(0), c.getString(1), recInfo);
        recipeList.add(recipe);
    }

    if(c.getCount() < 5){ //ALGO LEVEL 2
        Log.i("Algo", "Algo 2 is triggered");
        //clear the list first
        recipeList.clear();
        algoLevel = 2;
        Cursor c2 = db.getRecipeNowAlgo2(filterBy, orderBy);
        while (c2.moveToNext()) {
            String time = converTime(c2.getString(2), c2.getInt(3));

            String sKal = "";
            Float kal = c2.getFloat(4);

            if (kal > 0) {
                sKal = kal + " Cal";
            } else {
                sKal = "N/A";
            }

            String sServing = "";

            int servings = c2.getInt(5);

            if (servings > 0) {
                sServing = servings + " servings";
            } else {
                sServing = "N/A";
            }

            //===========================================================
            String recInfo = time + ", " + sKal + ", " + sServing;
            Log.i("recInfo", recInfo);
            //============================================================
            Recipe recipe = new Recipe(c2.getInt(0), c2.getString(1), recInfo);
            recipeList.add(recipe);
        }
     //======================================================================
        if(recipeList.size() < 5) {    //  ALGO LEVEL 3
            //===========================================
            Log.i("Algo", "Algo 3 is triggered");
            //clear it first
            recipeList.clear();
            algoLevel = 3;
            Cursor c3 = db.getRecipeNowAlgo3(filterBy, orderBy);
            while (c3.moveToNext()) {
                String time = converTime(c3.getString(2), c3.getInt(3));

                String sKal = "";
                Float kal = c3.getFloat(4);

                if (kal > 0) {
                    sKal = kal + " Cal";
                } else {
                    sKal = "N/A";
                }

                String sServing = "";

                int servings = c3.getInt(5);

                if (servings > 0) {
                    sServing = servings + " servings";
                } else {
                    sServing = "N/A";
                }

                //===========================================================
                String recInfo = time + ", " + sKal + ", " + sServing;
                Log.i("recInfo", recInfo);
                //============================================================
                Recipe recipe = new Recipe(c3.getInt(0), c3.getString(1), recInfo);
                recipeList.add(recipe);
                //==============================================
            }//end of while

            if(recipeList.size() < 5) {    //  ALGO LEVEL 4
                //===========================================
                Log.i("Algo", "Algo 4 is triggered");
                //clear it first
                recipeList.clear();
                algoLevel = 4;
                Cursor c4 = db.getRecipeNowAlgo4(filterBy, orderBy);
                while (c4.moveToNext()) {
                    String time = converTime(c4.getString(2), c4.getInt(3));

                    String sKal = "";
                    Float kal = c4.getFloat(4);

                    if (kal > 0) {
                        sKal = kal + " Cal";
                    } else {
                        sKal = "N/A";
                    }

                    String sServing = "";

                    int servings = c4.getInt(5);

                    if (servings > 0) {
                        sServing = servings + " servings";
                    } else {
                        sServing = "N/A";
                    }

                    //===========================================================
                    String recInfo = time + ", " + sKal + ", " + sServing;
                    Log.i("recInfo", recInfo);
                    //============================================================
                    Recipe recipe = new Recipe(c4.getInt(0), c4.getString(1), recInfo);
                    recipeList.add(recipe);
                    //==============================================
                }
            }
        }//======================================

    }

    return algoLevel;

   }



    private class FindRecipe extends AsyncTask<Void, Void, Long>{

        private ProgressDialog pd;



        String filterBy = "hit";
        String orderBy = "DESC";

        public FindRecipe() {
            super();

            int selPos = filter.getSelectedItemPosition();
            switch (selPos){
                case 0:
                    filterBy = "hit";
                break;
                case 1:
                    filterBy = "r.newPrep";
                    break;
                case 2:
                    filterBy = "r.serving";
                    break;
                case 3:
                    filterBy = "r.kal";
                    break;
                default:
                    filterBy = "hit";
            }

            if(order.isChecked()){
                orderBy = "DESC";
            }else{
                orderBy = "ASC";
            }


        }

        @Override
        protected Long doInBackground(Void... params) {

            long startTime = System.currentTimeMillis();

            algoLevel = loadRecipes(filterBy, orderBy);

            long endTime = System.currentTimeMillis();

            return endTime - startTime; // return duration of query
        }

        @Override
        protected void onPostExecute(Long duration) {
            db.close();
            dataAdapter = new CustomRecipeAdapter(RecipeMainActivity.this, R.layout.recipe_list_info, recipeList);
            listView.setAdapter(dataAdapter);
            int result = dataAdapter.getCount();
            if(algoLevel==1){
             messageResult.setText("Your query has "+result + " recipe suggestions");
            }else{  // ALTERNATIVE QUERY
                messageResult.setText("Your query has very few recipe suggestions. " +
                        "To provide enough suggestions, we disregard some of your parameters.");
            }
            if(recipeList.isEmpty()){
                messageResult.setText("Your query has 0 recipe suggestions");

            }

            pd.dismiss();
            Toast.makeText(RecipeMainActivity.this, "That took me "+duration + " ms", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
           pd = ProgressDialog.show(RecipeMainActivity.this, "Analyzing qualifiers", "Please wait...");

        }

    }


    public void algoLevelEvent(View v){
        Toast.makeText(RecipeMainActivity.this, "Algo level "+algoLevel, Toast.LENGTH_SHORT).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_fragment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                  ActionBarEvents.displayPreferences(this);
                break;
            case R.id.action_bookmarks:
                ActionBarEvents.displayBookMarks(this); // 1 for params, 2 for results, 3 for display recipe
                break;
            case R.id.action_about:
                ActionBarEvents.displayAbout(this);
                break;
            case android.R.id.home:
               finish();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(Singleton.isBookmarkDisplayed){
            ActionBarEvents.displayBookMarks(this); // 1 for params, 2 for results, 3 for display recipe
        }
    }

    private String converTime(String time, int newPrep){
        String newTime = "";
        if(newPrep != -1) {
            time = time.replace("PT", "");
            String timeH = "";
            if (time.contains("H")) {
                timeH = time.substring(0, time.indexOf("H")) + " hrs";
                if (time.contains("M")) {
                    timeH = timeH + ", " + time.substring(time.indexOf("H") + 1, time.indexOf("M")) + " mins";
                }
            } else {
                timeH = timeH + time.substring(0, time.indexOf("M")) + " mins";
            }
            newTime = timeH;
        }else{ // prep is undetermined, bcoz of OS -> -1
            newTime = "N/A";
        }
        return newTime;
    }


    private class QueryResult{

        private int algoLevel;
        private int result;

        public QueryResult(int algoLevel, int result) {
            this.algoLevel = algoLevel;
            this.result = result;
        }

        public int getAlgoLevel() {
            return algoLevel;
        }

        public int getResult() {
            return result;
        }
    }


/*
public void next(View v){
    if(pager.getCurrentItem() != this.PAGER_INGREDIENT ) {//Not last
        int nextIndex = pager.getCurrentItem() + 1;
        pager.setCurrentItem(nextIndex, true);
     } else{ // last page Ingredietn Page // DO NOT proceed if NO SELECTED parameter, CHECK THE SingleTon
            if(Singleton.selCuisines.isEmpty() && Singleton.selMeals.isEmpty() && Singleton.selIngs.isEmpty()){
                Toast.makeText(RecipeMainFragment.this, "You cant proceed without parameter", Toast.LENGTH_SHORT).show();
                // TODO Change this to ALert Builder
            }else { // Proceed // TODO diplay PARAMETER SUMMARY here first before switching to new activity,
                Intent intent = new Intent(getApplicationContext(), RecipeResult.class);
                startActivity(intent);
            }

        }
}*/


}
