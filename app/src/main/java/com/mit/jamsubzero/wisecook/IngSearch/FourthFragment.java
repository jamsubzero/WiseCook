package com.mit.jamsubzero.wisecook.IngSearch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mit.jamsubzero.wisecook.Data.MyDatabase;
import com.mit.jamsubzero.wisecook.Data.Singleton;
import com.mit.jamsubzero.wisecook.Data.UserIngredient;
import com.mit.jamsubzero.wisecook.IngQuan.QuanDialog;
import com.mit.jamsubzero.wisecook.IngQuan.QuanDialog.myOnClickListener;
import com.mit.jamsubzero.wisecook.MainFragment;
import com.mit.jamsubzero.wisecook.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by jamsubzero on 4/21/2016.
 */
public class FourthFragment extends Fragment {

   AutoCompleteTextView actv;

    MyDatabase db;

    //====================================
    CustomIngAddAdapter dataAddAdapter = null;

    ArrayList<UserIngredient> ingAddList;

   ListView ingAddedListView = null;
    Button ingLoad;

    //
    public myOnClickListener myListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fourth_frag, container, false);

        ingAddedListView = (ListView) v.findViewById(R.id.ingAddedList);


        db = new MyDatabase(this.getActivity().getApplicationContext());

        actv = (AutoCompleteTextView) v.findViewById(R.id.searchIngAuto);

        ingAddList = new ArrayList<UserIngredient>();
        dataAddAdapter = new CustomIngAddAdapter(this.getActivity().getApplicationContext(),R.layout.ing_added_info,  ingAddList);
        ingAddedListView.setAdapter(  dataAddAdapter);
        this.registerForContextMenu(ingAddedListView);

        View empty = v.findViewById(R.id.empty);

        ingAddedListView.setEmptyView(empty);
        ingAddedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ingAddedListView.showContextMenuForChild(view);
            }
        });

        actv.setThreshold(1);
        String[] from = { "icon", "name", "assoc" };
        int[] to = { R.id.ingIcon, R.id.recBookName, R.id.recBookDate};

        SimpleCursorAdapter a = new SimpleCursorAdapter(this.getActivity().getApplicationContext(), R.layout.ingsearch_info, null, from, to, 0);

        a.setStringConversionColumn(2);// bcoz name is in the 3rd entry of matrixCursor
        FilterQueryProvider provider = new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                // run in the background thread if too much
                Log.d("query", "runQuery constraint: " + constraint);
                if (constraint == null) {
                    return null;
                }

                String[] columnNames = { BaseColumns._ID, "icon", "name", "assoc" };
                MatrixCursor c = new MatrixCursor(columnNames);

                Cursor dbc = db.searchIngs(constraint.toString());

                while(dbc.moveToNext()){
               //TODo <fixed> comment for now for faster build
                    int resID = -1;
                    try {
                        String mDrawableName = "ing" + dbc.getString(0);
                        resID = getActivity().getApplicationContext().getResources().getIdentifier(mDrawableName, "drawable", getActivity().getApplicationContext().getPackageName());
                    }catch ( Exception e){
                        resID = R.drawable.parsley1;
                    }
                    c.newRow().add(dbc.getString(0))
                              .add(resID)
                              .add(dbc.getString(1))
                              .add(dbc.getString(2));
                }
                return c;
            }
        };
        a.setFilterQueryProvider(provider);
        actv.setAdapter(a);

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//====================================================
                final int selID = Math.round(id);

                if (!Singleton.selIngs.containsKey(selID)) { //IF ID doesnt exists

                    SharedPreferences preferences = getActivity().getSharedPreferences("WiseCook", getActivity().MODE_PRIVATE);
                    boolean dontAsk = preferences.getBoolean("dontAsk", false);

                    if(dontAsk){
                        Cursor c = db.getIngWithUnit(selID);
                        //  item, otherNames, unit
                        if(c.moveToNext()){
                            String ingName = c.getString(0);
                            String otherName = c.getString(1);
                            String ingUnit = c.getString(2) + "";

                            UserIngredient newIng = new UserIngredient(selID, ingName, otherName, ingUnit, ingUnit, -1) ;
                            //add it to the singleton
                            Singleton.selIngs.put(selID, newIng); //working tested
                            addIngredient(selID);
                            actv.setText("");
                            //enable the next button
                            notifyNextButton();
                        }

                    }else {
                        myListener = new myOnClickListener() {
                            @Override
                            public void onButtonClick() {
                                //============================ Now add the selected Ing
                           //     Toast.makeText(getActivity(), selID + " -- " + actv.getText().toString(), Toast.LENGTH_SHORT).show();
                                addIngredient(selID);
                                actv.setText("");
                                //enable the next button
                                notifyNextButton();
                                //
                            }
                        };
                        //===============================

                        QuanDialog mydialog = new QuanDialog(getActivity(), myListener, selID);
                        mydialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                actv.setText("");
                            }
                        });
                        mydialog.show();
                    }

            }else {
                    new AlertDialog.Builder(getActivity())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Duplicate Entry")
                            .setMessage("You already added this ingredient.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });
        //======================================

        Button ingSave = (Button) v.findViewById(R.id.ingSave);
        ingSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.saveUserIng();
                setSavedQuan();
            }
        });
        ingLoad = (Button) v.findViewById(R.id.ingLoad);
        ingLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSavedUserIng();
            }
        });
        setSavedQuan();
        Button ingClear = (Button) v.findViewById(R.id.ingClear);
        ingClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearIngList();
                notifyNextButton();
            }
        });

        //=================================

        return v;
   }



//=======================================================
private void addIngredient(int id){
    if(Singleton.selIngs.containsKey(id)) {
        UserIngredient ingredient = Singleton.selIngs.get(id);
        ingAddList.add(0, ingredient);
        dataAddAdapter.notifyDataSetChanged();
    }
}


    @Override
    public void onResume(){ // TODO HasMap SORT items BY ID, so item sort changes onResume, Fix this
        super.onResume();
           ingAddList.clear();
        dataAddAdapter.notifyDataSetChanged();
         for(int id: Singleton.selIngs.keySet()){
             addIngredient(id);
        }

        notifyNextButton();
    }

//================

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.ingAddedList) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            UserIngredient ingredientObject = (UserIngredient) ingAddedListView.getItemAtPosition(acmi.position);
           String ingName = ingredientObject.getIngName();
            //
            LayoutInflater headerInflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ViewGroup header = (ViewGroup) headerInflater.inflate(
                    R.layout.context_menu_header, null);
            //
            ImageView headerIcon = (ImageView) header.findViewById(R.id.headerIcon);
           // headerIcon.setImageResource(R.drawable.parsley1); //TODO <fixed> comment below for faster build
            //TODO <fixed> comment for now to build faster
           try {
               String mDrawableName = "ing"+ingredientObject.getCode();
               int resID = getActivity().getApplicationContext().getResources().getIdentifier(mDrawableName , "drawable", getActivity().getApplicationContext().getPackageName());
               headerIcon.setImageResource(resID);
           }catch (Exception e){
               headerIcon.setImageResource(R.drawable.parsley1);
           }


            TextView title = (TextView) header
                    .findViewById(R.id.headerText);
            title.setText(ingName);
            menu.setHeaderView(header);
            //
            menu.add(0, v.getId(), 0, "Delete");//groupId, itemId, order, title
            menu.add(0, v.getId(), 0, "Info");
          }
        }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = acmi.position;
        UserIngredient ingredient = (UserIngredient) ingAddedListView.getItemAtPosition(pos);

        if(item.getTitle()=="Delete"){
            ingAddList.remove(pos);
            dataAddAdapter.notifyDataSetChanged();
            int code =  ingredient.getCode();
            Singleton.selIngs.remove(code);
            Toast.makeText(this.getActivity().getApplicationContext(),"Ingredient Removed",Toast.LENGTH_LONG).show();
            notifyNextButton();
        }
        else if(item.getTitle()=="Info"){
              //
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            // ...Irrelevant code for customizing the buttons and title
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.ing_details_layout, null);
            dialogBuilder.setView(dialogView);

            ImageView infoIcon = (ImageView) dialogView.findViewById(R.id.infoIcon);
            //TODO <fixed> comment for faster build
            try {
                String mDrawableName = "ing" + ingredient.getCode();
                int resID = getActivity().getApplicationContext().getResources().getIdentifier(mDrawableName, "drawable", getActivity().getApplicationContext().getPackageName());
              //  Toast.makeText(getActivity().getApplicationContext(), resID, Toast.LENGTH_SHORT).show();
                infoIcon.setImageResource(resID); //TODO <fixed> replace later with the true selected icon
            }catch (Exception e){
                infoIcon.setImageResource(R.drawable.parsley1);
            }

           TextView infoName = (TextView) dialogView.findViewById(R.id.infoName);
            infoName.setText(ingredient.getIngName());
            TextView infoAssoc = (TextView) dialogView.findViewById(R.id.infoAssoc);
            infoAssoc.setText(ingredient.getOtherName());
            TextView infoQuan = (TextView) dialogView.findViewById(R.id.infoQuan);
                String quan_unit = "";
                 double quan = ingredient.getQuantity();
                 if(quan > 0){
                     DecimalFormat df = new DecimalFormat("###,###.##");
                     quan_unit = df.format(quan) + " "+ ingredient.getChoosenUnit();
                 }else if (quan == -1){
                     quan_unit = "You have enough.";
                 }else{
                     quan_unit = "0";
                 }

            infoQuan.setText(quan_unit);

            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

            Button infoOK = (Button) dialogView.findViewById(R.id.infOK);
            infoOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     alertDialog.dismiss();
                }
            });
            //
        }else{
            return false;
        }
        return true;
    }
//==============

private void notifyNextButton(){
    ViewPager vp=(ViewPager) getActivity().findViewById(R.id.viewPager);
    if(vp.getCurrentItem() == MainFragment.PAGER_INGREDIENT) {  // if not in course
        ImageButton nxt = (ImageButton) FourthFragment.this.getActivity().findViewById(R.id.next);
        if(Singleton.selCuisines.containsKey(-1) && Singleton.selMeals.containsKey(-1) &&
                Singleton.selCourses.containsKey(-1) &&  Singleton.selOccasion.containsKey(-1) &&
                Singleton.selPrep.containsKey(-1) && Singleton.selIngs.isEmpty()){
            nxt.setEnabled(false);
        } else {
            nxt.setEnabled(true);
            shakeButton();
        }
    }
}

    private void shakeButton(){
        ViewPager vp=(ViewPager) getActivity().findViewById(R.id.viewPager);
        ImageButton nxt = (ImageButton) this.getActivity().findViewById(R.id.next);
        Animation myAnim = AnimationUtils.loadAnimation(this.getActivity(), R.anim.milkshake);

        nxt.setAnimation(myAnim);
        nxt.startAnimation(myAnim);
    }

    private void loadSavedUserIng(){
        Cursor c = db.getSavedUserIng();
        clearIngList(); //list ONLY
        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                int code = c.getInt(0);
                String ingName = c.getString(1);
                String otherName = c.getString(2);
                if (otherName == null || otherName.equals("null")) {
                    otherName = "";
                }
                String choosenUnit = c.getString(3);
                if (choosenUnit == null || choosenUnit.equals("null")) {
                    choosenUnit = "";
                }
                String defaultUnit = c.getString(4);
                if (defaultUnit == null || defaultUnit.equals("null")) {
                    defaultUnit = "";
                }
                double quantity = c.getDouble(5);
                UserIngredient newIng = new UserIngredient(code, ingName, otherName, choosenUnit, defaultUnit, quantity);
                //add it to the singleton
                Singleton.selIngs.put(code, newIng);
                //add it to the listView
            }
            for (UserIngredient ing : Singleton.selIngs.values()) {
                ingAddList.add(0, ing);
                dataAddAdapter.notifyDataSetChanged();
            }

        }else{
            new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("No ingredient found")
                    .setMessage("It seems you haven't saved an ingredient yet.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

notifyNextButton();
    }

    private void clearIngList(){
        Singleton.selIngs.clear();
        ingAddList.clear();
        dataAddAdapter.notifyDataSetChanged();

    }

    private void setSavedQuan(){
        Cursor c = db.getSavedUserIng();
        int savedNum = c.getCount();
            ingLoad.setText("Load("+savedNum + ")");

    }

/*
    public static FourthFragment newInstance(String text) {

        FourthFragment f = new FourthFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
    */
}
