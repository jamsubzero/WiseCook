package com.mit.jamsubzero.wisecook;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mit.jamsubzero.wisecook.Country.FirstFragment;
import com.mit.jamsubzero.wisecook.Course.ThirdFragment;
import com.mit.jamsubzero.wisecook.Data.MyDatabase;
import com.mit.jamsubzero.wisecook.Data.Singleton;
import com.mit.jamsubzero.wisecook.Data.UserIngredient;
import com.mit.jamsubzero.wisecook.IngSearch.CustomIngAddAdapter;
import com.mit.jamsubzero.wisecook.IngSearch.FourthFragment;
import com.mit.jamsubzero.wisecook.Meal.SecondFragment;
import com.mit.jamsubzero.wisecook.Others.OtherFragment;
import com.mit.jamsubzero.wisecook.RecipeResult.RecipeMainActivity;

import java.util.ArrayList;

/**
 * Created by jamsubzero on 4/21/2016.
 */
public class MainFragment extends AppCompatActivity {

    public static final int PAGER_CUISINE = 0;
    public static final int PAGER_MEAL = 1;
    public static final int PAGER_COURSE = 2;
    public static final int PAGER_OTHER = 3;
    public static final int PAGER_INGREDIENT = 4;

    ViewPager pager;

    MyDatabase db;

    ImageButton nxt;
    FirstFragment firstFrag ;
    SecondFragment secondFrag;
    ThirdFragment thirdFrag;
    OtherFragment otherFrag;
    FourthFragment fourthFrag;

    ListView ingAddedListView = null;

    AlertDialog alertDialog = null;


    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_fragment_layout);
        MobileAds.initialize(this, "ca-app-pub-8099472786932887~2910125602");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        firstFrag = new FirstFragment();
        secondFrag = new SecondFragment();
        thirdFrag = new ThirdFragment();
        otherFrag = new OtherFragment();
        fourthFrag = new FourthFragment();

        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        db = new MyDatabase(this);

        //getSupportActionBar().setTitle(title);
       pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

           }

           @Override
           public void onPageSelected(int position) {

               if(position != PAGER_INGREDIENT ) {//First ONLY, enable NEXT
                         nxt.setEnabled(true);
                         nxt.setImageResource(R.drawable.ic_chevron_right_white_24dp);
               }else { //last
                   nxt.setImageResource(R.drawable.ic_done_white_24dp);
                   if(Singleton.selCuisines.containsKey(-1) && Singleton.selMeals.containsKey(-1) &&
                           Singleton.selCourses.containsKey(-1) &&  Singleton.selOccasion.containsKey(-1) &&
                           Singleton.selPrep.containsKey(-1) && Singleton.selIngs.isEmpty()){
                       //if three of them is empty
                       nxt.setEnabled(false);
                   }else{
                       nxt.setEnabled(true);
                   }
               }
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       });

       nxt =  (ImageButton)this.findViewById(R.id.next);




    }//================END ON CREATE


public void prev(View v){
 if(pager.getCurrentItem()!=this.PAGER_CUISINE) { //NOT firstpage
     int prevIndex = pager.getCurrentItem() -1;
     pager.setCurrentItem(prevIndex, true);
 }

}


public void next(View v){
    if(pager.getCurrentItem() != this.PAGER_INGREDIENT ) {//Not last
        int nextIndex = pager.getCurrentItem() + 1;
        pager.setCurrentItem(nextIndex, true);
     } else{ // last page Ingredietn Page // DO NOT proceed if NO SELECTED parameter, CHECK THE SingleTon
        if(Singleton.selCuisines.containsKey(-1) && Singleton.selMeals.containsKey(-1) &&
                Singleton.selCourses.containsKey(-1) &&  Singleton.selOccasion.containsKey(-1) &&
                Singleton.selPrep.containsKey(-1) && Singleton.selIngs.isEmpty()){
                Toast.makeText(MainFragment.this, "You cant proceed without parameter", Toast.LENGTH_SHORT).show();
                // TODO Change this to ALert Builder
            }else { // Proceed // TODO diplay PARAMETER SUMMARY here first before switching to new activity,
                Intent intent = new Intent(getApplicationContext(), RecipeMainActivity.class);
                startActivity(intent);
            }

        }
}

    public void checkSelected(View v){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.params_dialog_layout, null);
        dialogBuilder.setView(dialogView);
//

        TextView paramCuisine = (TextView) dialogView.findViewById(R.id.paramCuisine);
        paramCuisine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(PAGER_CUISINE);
                alertDialog.dismiss();
            }
        });

        TextView paramMeal = (TextView) dialogView.findViewById(R.id.paramMeal);
        paramMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(PAGER_MEAL);
                alertDialog.dismiss();
            }
        });

        TextView paramCourse = (TextView) dialogView.findViewById(R.id.paramCourse);
        paramCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(PAGER_COURSE);
                alertDialog.dismiss();
            }
        });

        TextView paramOthers = (TextView) dialogView.findViewById(R.id.paramOther);
        paramOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(PAGER_OTHER);
                alertDialog.dismiss();
            }
        });

        TextView ingLabel = (TextView) dialogView.findViewById(R.id.ingLabel);
        ingLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(PAGER_INGREDIENT);
                alertDialog.dismiss();
            }
        });


        String selCuisine = "";
        if(!Singleton.selCuisines.containsKey(-1)){
            for(String s:Singleton.selCuisines.values()){
                selCuisine = s;
            }
        }
        paramCuisine.setText(selCuisine);

        String selMeal = "";
        if(!Singleton.selMeals.containsKey(-1)){
            for(String s:Singleton.selMeals.values()){
                selMeal = s;
            }
        }
        paramMeal.setText(selMeal);


        String selCourse = "";
        if(!Singleton.selCourses.containsKey(-1)){
            for(String s:Singleton.selCourses.values()){
                selCourse = s;
            }
        }
        paramCourse.setText(selCourse);
        //================ OTHERS

        String detOther = "";

        //<editor-fold desc = "occasion and prep - comment for now" default = "collapsed">
/*
        String selOccasion = "";
        if(!Singleton.selOccasion.containsKey(-1)){
            for(String s:Singleton.selOccasion.values()){
                selOccasion = s;
            }
        }

        String selPrep = "";
        if(!Singleton.selPrep.containsKey(-1)){
            for(String s:Singleton.selPrep.values()){
                selPrep = s;
            }
        }



        if(!selOccasion.isEmpty()) {
            detOther = detOther + selOccasion + ", ";
        }
        if(!selPrep.isEmpty()){
            detOther  = detOther + selPrep + ", ";
        }
        */
        // </editor-fold>

        if(Singleton.prepQuan > 0){
            detOther  = detOther + Singleton.prepQuan + " mins, ";
        }

        if(Singleton.calQuan > 0){
            detOther  = detOther + Singleton.calQuan + " cals, ";
        }

        if(Singleton.servQuan > 0){
            detOther  = detOther + Singleton.servQuan + " servings, ";
        }
        ////================

        if(!detOther.isEmpty()) { // if not empty, there's excess
            detOther = detOther.substring(0, detOther.length() - 2); //remove the excess ", "
        }
        paramOthers.setText(detOther);


        //=============================================================
        CustomIngAddAdapter dataAddAdapter = null;
        ArrayList<UserIngredient> ingAddList;

        ingAddList = new ArrayList<UserIngredient>();
        dataAddAdapter = new CustomIngAddAdapter(this.getApplicationContext(),R.layout.ing_added_info,  ingAddList);

        ingAddedListView = (ListView) dialogView.findViewById(R.id.ingAddedList);
        ingAddedListView.setAdapter(  dataAddAdapter);

        View empty = dialogView.findViewById(R.id.empty);

        ingAddedListView.setEmptyView(empty);

        for (UserIngredient ing: Singleton.selIngs.values()) {
            ingAddList.add(0, ing);
        }
        dataAddAdapter.notifyDataSetChanged();


       alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(ingAddList.size()==0){
            relativeParams.addRule(RelativeLayout.BELOW, R.id.empty);
        }else{
            relativeParams.addRule(RelativeLayout.BELOW, R.id.ingAddedList);
        }

        dialogView.findViewById(R.id.closeBtn).setLayoutParams(relativeParams);

        Button closeBtn = (Button) dialogView.findViewById(R.id.closeBtn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
    //===========================================================

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
                      ActionBarEvents.displayBookMarks(this);
                break;
            case R.id.action_about:
                   ActionBarEvents.displayAbout(this);
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

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return firstFrag;
                case 1: return secondFrag;
                case 2: return thirdFrag;
                case 3: return otherFrag;
                case 4: return fourthFrag;
               default: return firstFrag;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }





}//================================================
