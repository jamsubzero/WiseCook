package com.mit.jamsubzero.wisecook.Meal;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mit.jamsubzero.wisecook.Data.MyDatabase;
import com.mit.jamsubzero.wisecook.R;
import com.mit.jamsubzero.wisecook.Data.Singleton;

import java.util.ArrayList;

/**
 * Created by jamsubzero on 4/21/2016.
 */
public class SecondFragment extends Fragment {

    ListView listView;

    MyDatabase db;

    CustomMealsAdapter dataAdapter = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.second_frag, container, false);
        listView = (ListView) v.findViewById(R.id.mealList);

        db = new MyDatabase(this.getActivity().getApplicationContext());
        //   TextView tv = (TextView) v.findViewById(R.id.tvFragFirst);
        // tv.setText(getArguments().getString("msg"));

        loadMeals();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dataAdapter.setSelectedIndex(position);
                dataAdapter.notifyDataSetChanged();
                Meal c = (Meal) parent.getItemAtPosition(position);
                Singleton.selMeals.clear(); // Clear it first
                Singleton.selMeals.put(c.getCode(), c.getName());
                shakeButton();
            }
        });
           //====================================
        return v;
    }

    /*
    public SecondFragment() {
        setArguments(new Bundle());
    }
    public static SecondFragment newInstance(String text) {

        SecondFragment f = new SecondFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
    */
    //=========================


    @Override
    public void onResume(){
        super.onResume();
        restoreSelectedIndex();
    }

    @Override
    public void onStart() {
        super.onStart();
        restoreSelectedIndex();
    }


    void restoreSelectedIndex(){
        int curPos = -1;
        if(!Singleton.selMeals.isEmpty()) {
            for(int i = 0; i < dataAdapter.getList().size(); i++){
                Meal meal = dataAdapter.getList().get(i);
                for (int c : Singleton.selMeals.keySet()) {
                    if(meal.getCode() == c){
                        curPos = i;
                        break;
                    }
                }
            }
        }
        dataAdapter.setSelectedIndex(curPos);
        dataAdapter.notifyDataSetChanged();
    }




//============================

    //get data from the database
    private void loadMeals(){
        ArrayList<Meal> mealList = new ArrayList<Meal>();

        Cursor c = db.getCodes(MyDatabase.CATEG_MEALS);

        Meal defMeal = new Meal(-1, "(Any Meal)", false);
        mealList.add(0, defMeal);
        while (c.moveToNext()){
          Meal meal = new Meal(c.getInt(0), c.getString(1), false);
            mealList.add(meal);
        }

        dataAdapter = new CustomMealsAdapter(this.getActivity().getApplicationContext(),R.layout.meal_info, mealList);
        listView.setAdapter(dataAdapter);
    }

    private void shakeButton(){
        ViewPager vp=(ViewPager) getActivity().findViewById(R.id.viewPager);
        ImageButton nxt = (ImageButton) this.getActivity().findViewById(R.id.next);
        Animation myAnim = AnimationUtils.loadAnimation(this.getActivity(), R.anim.milkshake);

        nxt.setAnimation(myAnim);
        nxt.startAnimation(myAnim);
    }



}
