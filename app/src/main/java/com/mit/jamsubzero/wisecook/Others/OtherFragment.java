package com.mit.jamsubzero.wisecook.Others;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.mit.jamsubzero.wisecook.Country.Country;
import com.mit.jamsubzero.wisecook.Country.CustomCuisinesAdapter;
import com.mit.jamsubzero.wisecook.Data.MyDatabase;
import com.mit.jamsubzero.wisecook.Data.Singleton;
import com.mit.jamsubzero.wisecook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamsubzero on 4/21/2016.
 */
public class OtherFragment extends Fragment{

MyDatabase db;
//Spinner occasionSpinner;
EditText prepQuan, calQuan, servQuan;
Button prepMinus, prepPlus;
Button calMinus, calPlus;
Button servMinus, servPlus;

  //  Spinner prepSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.other_frag, container, false);
        db = new MyDatabase(getActivity().getApplicationContext());
       // occasionSpinner = (Spinner) v.findViewById(R.id.otherOccasion);
       // prepSpinner = (Spinner) v.findViewById(R.id.otherPrep);

        prepQuan = (EditText) v.findViewById(R.id.prepQuan);
        calQuan = (EditText) v.findViewById(R.id.calQuan);
        servQuan = (EditText) v.findViewById(R.id.servQuan);

        prepMinus = (Button) v.findViewById(R.id.prepMinus);
        prepPlus = (Button) v.findViewById(R.id.prepPlus);
        calMinus = (Button) v.findViewById(R.id.calMinus);
        calPlus = (Button) v.findViewById(R.id.calPlus);
        servMinus = (Button) v.findViewById(R.id.servMinus);
        servPlus = (Button) v.findViewById(R.id.servPlus);

        prepMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minus(prepQuan, 15);
            }
        });

        prepPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus(prepQuan, 15);
            }
        });

        calMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minus(calQuan, 50);
            }
        });

        calPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus(calQuan, 50);
            }
        });

        servMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minus(servQuan, 1);
            }
        });

        servPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus(servQuan, 1);
            }
        });

        prepQuan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                   int prep = Integer.parseInt(s.toString());
                    if(prep < 1){  // all less than 1 is converted to  -1
                     prep = -1;
                    }
                    Singleton.prepQuan = prep;
                }catch (Exception e){
                    Singleton.prepQuan = -1;
                                 }


            }
        });

        calQuan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    int in = Integer.parseInt(s.toString());
                    if(in < 1){  // all less than 1 is converted to  -1
                        in = -1;
                    }
                    Singleton.calQuan = in;
                }catch (Exception e){
                    Singleton.calQuan = -1;
                }


            }
        });

        servQuan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    int in = Integer.parseInt(s.toString());
                    if(in < 1){  // all less than 1 is converted to  -1
                        in = -1;
                    }
                    Singleton.servQuan = in;
                }catch (Exception e){
                    Singleton.servQuan = -1;
                }


            }
        });

//<editor-fold desc = "occasion and prep - comment for now" default = "collapsed">
/*


        loadOccasionData();
        loadPrepData();

        occasionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              Occasion o = (Occasion) parent.getItemAtPosition(position);
                Singleton.selOccasion.clear(); // Clear it first
                Singleton.selOccasion.put(o.getId(), o.getItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        prepSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Occasion o = (Occasion) parent.getItemAtPosition(position);
                Singleton.selPrep.clear(); // Clear it first
                Singleton.selPrep.put(o.getId(), o.getItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
        // </editor-fold>

        return v;
    }

    private void plus(EditText t, int inc_dec){ //increment and decrement

        String s = t.getText().toString();
        try{
            int i = Integer.parseInt(s);
            i = i + inc_dec;
            t.setText(i + "");
        }catch (Exception e){
            t.setText(inc_dec + "");
        }

        shakeButton();
    }

    private void minus(EditText t, int inc_dec){
        String s = t.getText().toString();
        try{
            int i = Integer.parseInt(s);
            i = i - inc_dec;
            if(i > 0) {
                t.setText(i + "");
            }else{
                t.setText("");
            }
        }catch (Exception e){
            t.setText("");
        }
        shakeButton();
    }



    private void loadOccasionData() {
        List<Occasion> list = new ArrayList<>();
            Cursor c = db.getCodes(MyDatabase.CATEG_OCCASION);

           list.add(new Occasion(-1, "(any)"));
            while (c.moveToNext()){
                Occasion occasion = new Occasion(c.getInt(0), c.getString(1));
                list.add(occasion);
            }
        ArrayAdapter<Occasion> adap = new ArrayAdapter<Occasion> (getActivity().getApplicationContext(),R.layout.other_occasion_info, list);
     //   occasionSpinner.setAdapter(adap);
    }

    private void loadPrepData() {
        List<Occasion> list = new ArrayList<>();
        Cursor c = db.getCodes(MyDatabase.CATEG_PREP);

        list.add(new Occasion(-1, "(any)"));
        while (c.moveToNext()){
            Occasion occasion = new Occasion(c.getInt(0), c.getString(1));
            list.add(occasion);
        }
        ArrayAdapter<Occasion> adap = new ArrayAdapter<Occasion> (getActivity().getApplicationContext(),R.layout.other_occasion_info, list);
//        prepSpinner.setAdapter(adap);
    }


    private void shakeButton(){
        ViewPager vp=(ViewPager) getActivity().findViewById(R.id.viewPager);
        ImageButton nxt = (ImageButton) this.getActivity().findViewById(R.id.next);
        Animation myAnim = AnimationUtils.loadAnimation(this.getActivity(), R.anim.milkshake);

        nxt.setAnimation(myAnim);
        nxt.startAnimation(myAnim);
    }




}
