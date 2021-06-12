package com.mit.jamsubzero.wisecook.IngQuan;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.mit.jamsubzero.wisecook.Data.MyDatabase;
import com.mit.jamsubzero.wisecook.Data.Singleton;
import com.mit.jamsubzero.wisecook.Data.UserIngredient;
import com.mit.jamsubzero.wisecook.R;

import java.text.DecimalFormat;

/**
 * Created by jamsubzero on 5/19/2016.
 */
public class QuanDialog extends Dialog{


    public myOnClickListener myListener;
    private int ingID;
    private int ingType;
    private MyDatabase mydb;

    String ingName;
    String otherName;
    String ingUnit;

    Button diaOk, diaCancel;
    TextView diaQuest;
    Spinner diaUnit;
    EditText diaQuan;
    Switch diaSwitch;



    Context context;
    private TextView diaEqui;
    private RadioGroup rg;
    private ImageView diaIcon;

    public QuanDialog(Context context, myOnClickListener myclick, int ingID) {
        super(context);
        this.myListener = myclick;
        this.ingID = ingID;
        this.context = context;
        mydb = new MyDatabase(context);
        //=============
        Cursor c = mydb.getIngWithUnit(ingID);
        //  item, otherNames, unit
        if(c.moveToNext()){
             ingName = c.getString(0);
            otherName = c.getString(1);
            ingUnit = c.getString(2) + "";
            ingType = c.getInt(3);
        }

//==============
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
      //  this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        //================




    } //END of consructor

    // This is my interface //
    public interface myOnClickListener {
        void onButtonClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.quan_dialog_layout);

       diaOk = (Button) findViewById(R.id.diaOk);
        diaCancel = (Button) findViewById(R.id.diaCancel);
        diaIcon = (ImageView) this.findViewById(R.id.diaIcon);
        diaQuest = (TextView) this.findViewById(R.id.diaQuest);

        diaQuan = (EditText) this.findViewById(R.id.diaQuan);

        diaUnit = (Spinner) this.findViewById(R.id.diaUnit);
        diaEqui = (TextView) this.findViewById(R.id.diaEqui);
        ArrayAdapter<CharSequence> adapter = null;

        if(ingType == MyDatabase.ING_TYPE_MASS) {
            adapter = ArrayAdapter.createFromResource(
                    context, R.array.ent_mass, android.R.layout.simple_spinner_item);

        }else if (ingType == MyDatabase.ING_TYPE_VOL){
            adapter = ArrayAdapter.createFromResource(
                    context, R.array.ent_volume, android.R.layout.simple_spinner_item);
        }else{
           adapter =  new ArrayAdapter<CharSequence>(getContext(),
                   android.R.layout.simple_spinner_item,
                    new String[] { this.ingUnit });
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diaUnit.setAdapter(adapter);
        int defaultPos = adapter.getPosition(this.ingUnit);
        diaUnit.setSelection(defaultPos);
        diaUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selUnit = diaUnit.getSelectedItem().toString();
                try {
                    double val = Double.parseDouble(diaQuan.getText().toString());
                    if (val > 0) {
                        diaOk.setEnabled(true);
                    } else {
                        diaOk.setEnabled(false);
                    }
                    if(ingType != MyDatabase.ING_TYPE_OTHER) {  //it it is MASS or VOLUME
                        convertVolume(selUnit, val);
                    }else{ //OTHER
                        diaEqui.setText("");
                    }

                } catch (Exception e) {
                    diaEqui.setText("");
                    diaOk.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //=================================

        diaQuan.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                    String selUnit = diaUnit.getSelectedItem().toString();
                    try {
                        double val = Double.parseDouble(s.toString());
                        if (val > 0) {
                            diaOk.setEnabled(true);
                        } else {
                            diaOk.setEnabled(false);
                        }
                        if(ingType != MyDatabase.ING_TYPE_OTHER) {  //it it is MASS or VOLUME
                        convertVolume(selUnit, val);
                        }else{ //OTHER
                            diaEqui.setText("");
                        }

                    } catch (Exception e) {
                        diaEqui.setText("");
                        diaOk.setEnabled(false);
                    }

            }


        });




        String question = "How much <b>"+this.ingName+"</b> do you have?";
      //  this.setTitle("How much..");

        diaQuest.setText(Html.fromHtml(question));

        //TODO <fixed> comment for now to build faster
          String mDrawableName = "ing"+ingID;
        try {
            int resID = context.getResources().getIdentifier(mDrawableName, "drawable", context.getPackageName());
            diaIcon.setImageResource(resID);
        }catch (Exception e){
            diaIcon.setImageResource(R.drawable.parsley1);
        }

        diaSwitch = (Switch) this.findViewById(R.id.diaSwitch);
        diaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {



                if(isChecked){
                   rg.check(R.id.diaEnough);
                     for (int i = 0; i < rg.getChildCount(); i++) {
                        rg.getChildAt(i).setEnabled(false);
                    }
                }else{
                    rg.check(R.id.diaIHave);
                    for (int i = 0; i < rg.getChildCount(); i++) {
                        rg.getChildAt(i).setEnabled(true);
                    }
                }

            }
        });

      diaOk.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              //Creat a new intance of UserIngredient
              double quan = 0;
              try {
                  quan = Double.parseDouble(diaQuan.getText().toString());
              }catch (Exception e){
                  quan = 0;
              }
              if(rg.getCheckedRadioButtonId() == R.id.diaEnough){
                  quan = -1;
              }

              String choosenUnit = diaUnit.getSelectedItem().toString();

              UserIngredient newIng = new UserIngredient(ingID, ingName, otherName, choosenUnit, ingUnit, quan) ;
              //add it to the singleton
              Singleton.selIngs.put(ingID, newIng); //working tested

              //==========================FOR THE PREFERENCES
              SharedPreferences preferences = context.getSharedPreferences("WiseCook", context.MODE_PRIVATE);
              SharedPreferences.Editor edit= preferences.edit();

              if(diaSwitch.isChecked()){
                  edit.putBoolean("dontAsk", true);
                  edit.commit();
                  new AlertDialog.Builder(context)
                          .setIcon(android.R.drawable.ic_dialog_info)
                          .setTitle("Settings changed")
                          .setMessage("Quantity will not be asked again.\n" +
                                      "You can always change this in Settings.")
                          .setCancelable(false)
                          .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int id) {
                                  dialog.dismiss();
                              }
                          }).show();
              }else{
                  edit.putBoolean("dontAsk", false);
                  edit.commit();
              }
              //=======================================================
              myListener.onButtonClick();

              dismiss();
          }
      });

       diaCancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               cancel();
           }
       });
        ///================

      rg = (RadioGroup) this.findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                diaQuan.setText("");
                if(checkedId == R.id.diaIHave){
                    diaQuan.setEnabled(true);
                    diaUnit.setEnabled(true);
                    diaOk.setEnabled(false);
                    diaQuan.setHint("Enter Quantity");
                }else{
                    diaQuan.setEnabled(false);
                    diaUnit.setEnabled(false);
                    diaOk.setEnabled(true);
                    diaQuan.setHint("Quantity not needed");
                }

            }
        });

//==============================================================
      diaQuan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

    } //end of Oncreate




    void convertVolume(String fromUnit, double fromQuan){
/*
        <string-array name="ent_volume">
        <item>cup</item>
        <item>tsp</item>
        <item>tbsp</item>
        <item>fl. oz</item>
        <item>l</item>
        <item>ml</item>
        <item>pt</item>
        <item>qt</item>
    </string-array>
    */
     String result = "";
        DecimalFormat df = new DecimalFormat("###,###.##");
        switch (fromUnit){  //NOTE!!!!: _ is for replacement of color later after switch // "/n" is repalced by <br/>
          //<editor-fold desc="FOR VOLUME">
            case "cup":
                result = result + df.format(fromQuan * 48) + "_" + "tsp" + "\n";
                result = result + df.format(fromQuan * 16) + "_" + "tbsp" + "\n";
                result = result + df.format(fromQuan * 8) + "_" + "fl. oz" + "\n";
                result = result + df.format(fromQuan * 0.236588) + "_" + "l" + "\n";
                result = result + df.format(fromQuan * 236.588) + "_" + "ml" + "\n";
                result = result + df.format(fromQuan * 0.5) + "_" + "pt" + "\n";
                result = result + df.format(fromQuan * 0.25) + "_" + "qt" + "\n";
                break;
            case "tsp":
                result = result + df.format(fromQuan * 0.0208333) + "_" + "cup" + "\n";
                result = result + df.format(fromQuan * 0.333333) + "_" + "tbsp" + "\n";
                result = result + df.format(fromQuan * 0.166667) + "_" + "fl. oz" + "\n";
                result = result + df.format(fromQuan * 0.00492892) + "_" + "l" + "\n";
                result = result + df.format(fromQuan * 4.92892) + "_" + "ml" + "\n";
                result = result + df.format(fromQuan * 0.0104167) + "_" + "pt" + "\n";
                result = result + df.format(fromQuan * 0.00520833) + "_" + "qt" + "\n";
                break;
            case "tbsp":
                result = result + df.format(fromQuan * 0.0625) + "_" + "cup" + "\n";
                result = result + df.format(fromQuan * 3) + "_" + "tsp" + "\n";
                result = result + df.format(fromQuan * 0.5) + "_" + "fl. oz" + "\n";
                result = result + df.format(fromQuan * 0.0147868) + "_" + "l" + "\n";
                result = result + df.format(fromQuan * 14.7868) + "_" + "ml" + "\n";
                result = result + df.format(fromQuan * 0.03125) + "_" + "pt" + "\n";
                result = result + df.format(fromQuan * 0.015625) + "_" + "qt" + "\n";
                break;

            case "fl. oz":
                result = result + df.format(fromQuan * 0.125) + "_" + "cup" + "\n";
                result = result + df.format(fromQuan * 6) + "_" + "tsp" + "\n";
                result = result + df.format(fromQuan * 2) + "_" + "tbsp" + "\n";
                result = result + df.format(fromQuan * 0.0295735) + "_" + "l" + "\n";
                result = result + df.format(fromQuan * 29.5735) + "_" + "ml" + "\n";
                result = result + df.format(fromQuan * 0.0625) + "_" + "pt" + "\n";
                result = result + df.format(fromQuan * 0.03125) + "_" + "qt" + "\n";
                break;
            case "l":
                result = result + df.format(fromQuan * 4.22675) + "_" + "cup" + "\n";
                result = result + df.format(fromQuan * 202.884) + "_" + "tsp" + "\n";
                result = result + df.format(fromQuan * 67.628) + "_" + "tbsp" + "\n";
                result = result + df.format(fromQuan * 33.814) + "_" + "fl. oz" + "\n";
                result = result + df.format(fromQuan * 1000) + "_" + "ml" + "\n";
                result = result + df.format(fromQuan * 2.11338) + "_" + "pt" + "\n";
                result = result + df.format(fromQuan * 1.05669) + "_" + "qt" + "\n";
                break;
            case "ml":
                result = result + df.format(fromQuan * 0.00422675) + "_" + "cup" + "\n";
                result = result + df.format(fromQuan * 0.202884) + "_" + "tsp" + "\n";
                result = result + df.format(fromQuan * 0.067628) + "_" + "tbsp" + "\n";
                result = result + df.format(fromQuan * 0.033814) + "_" + "fl. oz" + "\n";
                result = result + df.format(fromQuan * 0.001) + "_" + "l" + "\n";
                result = result + df.format(fromQuan * 0.00211338) + "_" + "pt" + "\n";
                result = result + df.format(fromQuan * 0.00105669) + "_" + "qt" + "\n";
                break;
            case "pt":
                result = result + df.format(fromQuan * 2) + "_" + "cup" + "\n";
                result = result + df.format(fromQuan * 96) + "_" + "tsp" + "\n";
                result = result + df.format(fromQuan * 32) + "_" + "tbsp" + "\n";
                result = result + df.format(fromQuan * 16) + "_" + "fl. oz" + "\n";
                result = result + df.format(fromQuan * 0.473176) + "_" + "l" + "\n";
                result = result + df.format(fromQuan * 473.176) + "_" + "ml" + "\n";
                result = result + df.format(fromQuan * 0.5) + "_" + "qt" + "\n";
                break;
            case "qt":
                result = result + df.format(fromQuan * 4) + "_" + "cup" + "\n";
                result = result + df.format(fromQuan * 192) + "_" + "tsp" + "\n";
                result = result + df.format(fromQuan * 64) + "_" + "tbsp" + "\n";
                result = result + df.format(fromQuan * 32) + "_" + "fl. oz" + "\n";
                result = result + df.format(fromQuan * 0.946353) + "_" + "l" + "\n";
                result = result + df.format(fromQuan * 946.353) + "_" + "ml" + "\n";
                result = result + df.format(fromQuan * 2) + "_" + "pt" + "\n";
                break;
            //</editor-fold>
                           /*     <item>oz</item>
                                <item>lb</item>
                                <item>kg</item>
                                <item>g</item>
                                <item>mg</item>
                                */
            //<editor-fold desc="FOR MASS">
            case "oz":
                result = result + df.format(fromQuan * 0.0625) + "_" + "lb" + "\n";
                result = result + df.format(fromQuan * 0.0283495) + "_" + "kg" + "\n";
                result = result + df.format(fromQuan * 28.3495) + "_" + "g" + "\n";
                result = result + df.format(fromQuan * 28349.5) + "_" + "mg" + "\n";
                break;
            case "lb":
                result = result + df.format(fromQuan * 16) + "_" + "oz" + "\n";
                result = result + df.format(fromQuan * 0.453592) + "_" + "kg" + "\n";
                result = result + df.format(fromQuan * 453.592) + "_" + "g" + "\n";
                result = result + df.format(fromQuan * 453592) + "_" + "mg" + "\n";
                break;
            case "kg":
                result = result + df.format(fromQuan * 35.274) + "_" + "oz" + "\n";
                result = result + df.format(fromQuan * 2.20462) + "_" + "lb" + "\n";
                result = result + df.format(fromQuan * 1000) + "_" + "g" + "\n";
                result = result + df.format(fromQuan * 1000000) + "_" + "mg" + "\n";
                break;
            case "g":
                result = result + df.format(fromQuan * 0.035274) + "_" + "oz" + "\n";
                result = result + df.format(fromQuan * 0.00220462) + "_" + "lb" + "\n";
                result = result + df.format(fromQuan * 0.001) + "_" + "kg" + "\n";
                result = result + df.format(fromQuan * 1000) + "_" + "mg" + "\n";
                break;
            case "mg":
                result = result + df.format(fromQuan * 3.5274e-5) + "_" + "oz" + "\n";
                result = result + df.format(fromQuan * 2.20462e-6) + "_" + "lb" + "\n";
                result = result + df.format(fromQuan * 1e-6) + "_" + "kg" + "\n";
                result = result + df.format(fromQuan * 0.001) + "_" + "g" + "\n";
                break;






            //</editor-fold>


            ///===================================================================
        }
        result = result.replaceAll("_", "<font color='#669900'>&nbsp;");
        result = result.replaceAll("\n", "</font><br/>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        result = "Equivalents:<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + result ;
        diaEqui.setText(Html.fromHtml(result));

    }



}// class ends
