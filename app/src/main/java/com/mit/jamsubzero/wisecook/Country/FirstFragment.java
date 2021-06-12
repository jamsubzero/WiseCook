package com.mit.jamsubzero.wisecook.Country;

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
import android.widget.Toast;

import com.mit.jamsubzero.wisecook.Data.MyDatabase;
import com.mit.jamsubzero.wisecook.IngSearch.FourthFragment;
import com.mit.jamsubzero.wisecook.MainFragment;
import com.mit.jamsubzero.wisecook.R;
import com.mit.jamsubzero.wisecook.Data.Singleton;

import java.util.ArrayList;

/**
 * Created by jamsubzero on 4/21/2016.
 */
public class FirstFragment extends Fragment{

 ListView listView;

    MyDatabase db;


    CustomCuisinesAdapter dataAdapter = null;

    private static  final int def= 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_frag, container, false);
     listView = (ListView) v.findViewById(R.id.countryList);

    db = new MyDatabase(this.getActivity().getApplicationContext());
     //   TextView tv = (TextView) v.findViewById(R.id.tvFragFirst);
       // tv.setText(getArguments().getString("msg"));
        this.loadCuisines();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dataAdapter.setSelectedIndex(position);
        dataAdapter.notifyDataSetChanged();
        Country c = (Country)parent.getItemAtPosition(position);
        Singleton.selCuisines.clear(); // Clear it first
        Singleton.selCuisines.put(c.getCode(), c.getName());
        shakeButton();
    }
});

        return v;
    }


    //========================================================
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
        if(!Singleton.selCuisines.isEmpty()) {
            for(int i = 0; i < dataAdapter.getList().size(); i++){
                Country country = dataAdapter.getList().get(i);
                for (int c : Singleton.selCuisines.keySet()) {
                    if(country.getCode() == c){
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
    private void loadCuisines(){
        ArrayList<Country> countryList = new ArrayList<Country>();

        Cursor c = db.getCodes(MyDatabase.CATEG_CUISINES);

        Country defCountry = new Country(-1, "(Any cuisine)", false);
        countryList.add(0,defCountry);
        while (c.moveToNext()){
            Country country = new Country(c.getInt(0), c.getString(1), false);
            countryList.add(country);
        }

        dataAdapter = new CustomCuisinesAdapter(this.getActivity().getApplicationContext(),R.layout.country_info, countryList);
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

/*  if (savedInstanceState != null) {

        ArrayList<Country> countries = dataAdapter.getList();
        for (Country c : countries){
                 String code = c.getCode();
                     if(Singleton.selCuisines.containsKey(Integer.parseInt(code))){
                         c.setSelected(true);
                     }else{
                         c.setSelected(false);
                     }
       }

    }*/