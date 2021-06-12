package com.mit.jamsubzero.wisecook.RecipeResult;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mit.jamsubzero.wisecook.ActionBarEvents;
import com.mit.jamsubzero.wisecook.Data.MyDatabase;
import com.mit.jamsubzero.wisecook.Data.Singleton;
import com.mit.jamsubzero.wisecook.Data.UserIngredient;
import com.mit.jamsubzero.wisecook.R;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class DisplayRecipe extends AppCompatActivity {

    TextView recName, recServings, recKal, recTime, procSteps, curStep;

    CustomIngNeedAdapter dataAdapter;
    ListView listView;

    private String RECIPE_ID;
    private String RECIPE_NAME;

    Cursor procedures;
    private int stepCount = -1;

    RelativeLayout recipeHead;
    ImageButton recBookmarkBtn;

    int origServe = 0;
    int userServe = 0;

    EditText modServe;

    MyDatabase db;


    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_display_recipe);
        MobileAds.initialize(this, "ca-app-pub-8099472786932887~2910125602");
        mAdView = findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.RECIPE_ID = this.getIntent().getExtras().getInt("recID") + "";

         listView = (ListView) this.findViewById(R.id.recIngs);
         recipeHead = (RelativeLayout) this.findViewById(R.id.recipeInfo);
        procSteps = (TextView) this.findViewById(R.id.procSteps);
        curStep = (TextView) this.findViewById(R.id.curStep);
        recName = (TextView) this.findViewById(R.id.recName);
        recServings = (TextView) this.findViewById(R.id.recServings);
        recKal = (TextView) this.findViewById(R.id.recKal);
        recTime = (TextView) this.findViewById(R.id.recTime);
        db = new MyDatabase(this.getApplicationContext());

        modServe = (EditText) this.findViewById(R.id.modServe);
        //======================================================================
        modServe.addTextChangedListener(new TextWatcher() {
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

                    userServe = in;

                }catch (Exception e){
                   userServe = 0;
                }
                loadCuisines();

            }
        });

        //========================================

        Cursor cursor = db.getRecipeInfo(this.RECIPE_ID); //round the id first to become int

        if(cursor.moveToNext()) {
            this.RECIPE_NAME = cursor.getString(0); //We start from Name
            recName.setText(Html.fromHtml(this.RECIPE_NAME)); // Html becoz of &quote;

            origServe = cursor.getInt(1);
            userServe = Singleton.servQuan;



            if(userServe!= -1){ //user has specified
                if (origServe > 0) {
                    recServings.setText(origServe + " Servings");
                    modServe.setText(userServe + "");
                } else {
                    modServe.setText("");
                    recServings.setText("N/A");
                }

            }else {   // default Servings
                if (origServe > 0) {
                    recServings.setText(origServe + " Servings");
                    modServe.setText("");
                } else {
                    recServings.setText("N/A");

                }
            }

            String time = cursor.getString(2);
            int newPrep = cursor.getInt(3);

            if (newPrep != -1) {
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
                recTime.setText(timeH);
            } else { // prep is undetermined, bcoz of OS -> -1
                recTime.setText("N/A");
            }

            Float kal = cursor.getFloat(4);

            if (kal > 0) {
                recKal.setText(kal + " Cal/Serving");
            } else {
                recKal.setText("N/A");
            }
        }


        loadCuisines();

        procedures = db.getProcedures(this.RECIPE_ID);
        stepCount = procedures.getCount();

        //auto proceed to 1st step
        procedures.moveToNext();
        curStep.setText(Html.fromHtml(procedures.getString(0)));
        procSteps.setText("1/"+stepCount);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IngNeed ingNeed = dataAdapter.getItem(position);
                DecimalFormat df = new DecimalFormat("###,###.##");

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DisplayRecipe.this);
                // ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = DisplayRecipe.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.ing_rec_details_layout, null);
                dialogBuilder.setView(dialogView);

                ImageView infoIcon = (ImageView) dialogView.findViewById(R.id.infoIcon);
               // infoIcon.setImageResource(R.drawable.parsley1); //TODO <fixed> comment for faster build
                //TODO <fixed> comment for now to build faster
                try {
                    String mDrawableName = "ing" + ingNeed.getIngCode();
                    int resID = DisplayRecipe.this.getResources().getIdentifier(mDrawableName, "drawable", DisplayRecipe.this.getPackageName());
                  //  Toast.makeText(DisplayRecipe.this, resID, Toast.LENGTH_SHORT).show();
                    infoIcon.setImageResource(resID);
                }catch (Exception e){
                    infoIcon.setImageResource(R.drawable.parsley1);
                }


                TextView infoName = (TextView) dialogView.findViewById(R.id.infoName);
                infoName.setText(ingNeed.getItem());
                TextView infoAssoc = (TextView) dialogView.findViewById(R.id.infoAssoc);
                infoAssoc.setText(ingNeed.getAssoc());
                TextView inforecNeeds = (TextView) dialogView.findViewById(R.id.infoRecNeeds);

                double recQuan = ingNeed.getNewQuan();
                String quan_unit = "";
                String defUnit = ingNeed.getUnit();
                if (recQuan > 0) {
                    quan_unit = df.format(recQuan) + " " + defUnit;
                } else {
                    quan_unit = "";
                }
                inforecNeeds.setText(quan_unit);
                // ------------- THE USER HAVE
                TextView infoUserHave = (TextView) dialogView.findViewById(R.id.infoUserHave);

                try{
                    /*
                    We need to try and catch because there still ing that dont have ing_code,
                    so meaning NOT ALL has Integer Value for Ing_Code, some is NULL or some is Empty..
                    If parsing is successful, meaning it has valid ing_code,
                     If NOT, for now display a toast informing the error, see Catch Below
                     */

                    int ingCode = Integer.parseInt(ingNeed.getIngCode());

                if (Singleton.selIngs.containsKey(ingCode)) {

                    UserIngredient userIngredient = Singleton.selIngs.get(Integer.parseInt(ingNeed.getIngCode()));
                    double userQuan = userIngredient.getQuantity();
                    String choosenUnit = userIngredient.getChoosenUnit();
                    String newUnit = "";
                    double newUserQuan = 0;
                    if (!(choosenUnit.equals(defUnit))) { //Determine if choosen Unit is same as default
                        newUserQuan = UnitConverter.convertVolume(choosenUnit, userQuan, defUnit);
                        newUnit = defUnit;
                    } else { // All the Same, NO CHANGES
                        newUserQuan = userQuan;
                        newUnit = choosenUnit;
                    }
                    if (userQuan == -1) { //-1 means the user have eonugh
                        infoUserHave.setTextColor(Color.GREEN);
                        infoUserHave.setText("You have enough");
                    } else {  // QUANTITY SPECIFIED

                        String sUserQuan = df.format(userQuan);
                        String sNewUserQuan = df.format(newUserQuan);
                        if (newUserQuan >= recQuan) { //more than or equal
                            infoUserHave.setTextColor(Color.GREEN);
                            String userQuaninfo = "";
                            if (!(choosenUnit.equals(defUnit))) {
                                       userQuaninfo = sUserQuan + " " + choosenUnit + " (" + sNewUserQuan + " " + newUnit + ")";
                            }else{
                                userQuaninfo =sNewUserQuan + " " + newUnit;
                            }
                            infoUserHave.setText(userQuaninfo);// nclude also the oldQuan and choosenUnit

                        } else if (newUserQuan < recQuan) { // if lacking
                            infoUserHave.setTextColor(Color.parseColor("#FF8800"));
                            String userQuanInfo = "";
                            if (!(choosenUnit.equals(defUnit))) {
                                userQuanInfo = sUserQuan + " " + choosenUnit +
                                        " (" + sNewUserQuan + " " + newUnit + ")" // nclude also the oldQuan and choosenUnit
                                        + ", <font color = '#dd4b39'>you lack " + df.format(recQuan - newUserQuan) + " " + newUnit + "</font>";
                            }else{
                                userQuanInfo = sUserQuan + " " + choosenUnit
                                        + ", <font color = '#dd4b39'>you lack " + df.format(recQuan - newUserQuan) + " " + newUnit + "</font>";
                                 }
                            infoUserHave.setText(Html.fromHtml(userQuanInfo));
                        }
                    }
                } else { // IF the user dont have the ing, Hint is Colored
                    infoUserHave.setText("");
                }
//===============================================================================================================
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

            ///////-==========================Catch the Error
            } catch(Exception e){
                    Toast.makeText(DisplayRecipe.this, "Sorry, this ingredient is not formatted YET.\n" +
                                                           "All black colored Ingredient is NOT Formatted", Toast.LENGTH_SHORT).show();
                }
                //===================== FOR the DIalog



            }//ONCLICK

        });//EVENT LISTENER
        //========== bookmarks
         recBookmarkBtn = (ImageButton) this.findViewById(R.id.recBookmarkBtn);
        // check if it already exists
        recBookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!db.isBookmarkExists(RECIPE_ID)){  // IF NOT EXIST
                    SimpleDateFormat dFormat = new SimpleDateFormat("EEE, MMM-dd-yyyy");
                    String date = dFormat.format(Calendar.getInstance().getTime());
                    boolean isSuccess =   db.bookmarkRecipe(RECIPE_ID, RECIPE_NAME, date);
                    if(isSuccess){
                        new AlertDialog.Builder(DisplayRecipe.this)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setTitle("Recipe Saved")
                                .setMessage("Recipe successfully bookmarked.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                        recBookmarkBtn.setImageResource(R.drawable.bookchecklong);
                        recBookmarkBtn.setClickable(false);
                    }

                }


            }
        });
        if(db.isBookmarkExists(RECIPE_ID)){  // CHECK
            recBookmarkBtn.setImageResource(R.drawable.bookchecklong);
            recBookmarkBtn.setClickable(false);
            recBookmarkBtn.setEnabled(false);
        }else{                                  // PLUS
            recBookmarkBtn.setImageResource(R.drawable.booklong);
            recBookmarkBtn.setClickable(true);
        }
    //================================

   setRecipeBackground();

    }//END ONCREATE

    private void setRecipeBackground() {
        //TODO <fixed> comment background for now for faster build
        try {
            Drawable back = Drawable.createFromStream(this.getResources().getAssets().open("recipe_images/ico"+RECIPE_ID+".jpg"),null);
            recipeHead.setBackground(back);
        }catch (IOException iox){
            recipeHead.setBackgroundResource(R.drawable.ico1);
        }

    }

//===========

    public void nextStep(View v){
        if(procedures.moveToNext()) {

            curStep.setText(Html.fromHtml(procedures.getString(0)));  // from Html becoz some have &quote;
            procSteps.setText(procedures.getPosition() + 1 + "/" + stepCount); // plus 1 becoz array
        }
    }
    //======================
    public void prevStep(View v){
        if(procedures.moveToPrevious()) {
            curStep.setText(Html.fromHtml(procedures.getString(0)));  // from Html becoz some have &quote;
            procSteps.setText(procedures.getPosition() + 1 + "/" + stepCount); // plus 1 becoz array
        }
    }

    private void loadCuisines(){
        ArrayList<IngNeed> ingNeedList = new ArrayList<IngNeed>();

        Cursor c = db.getIngNeeds(this.RECIPE_ID);

        while (c.moveToNext()){
            String unit  = c.getString(6);

            double quan = 0;
            double origQuan = c.getDouble(2);

            if(userServe>0){ //user specified

                double diff =  origServe - userServe;
                Log.i("quan", "diff is "+diff+ " " + origServe +"-" +userServe );

                if(diff < 0){ // meaning negative, the quantity increased
                    diff = diff * -1; // convert it to positive
                    double mult = diff / origServe; // how much difference
                    Log.i("quan", "mult is "+mult+ " " + diff +"/" +origServe );
                    double additional = origQuan * mult; //
                    Log.i("quan", "add is "+additional+ " " + origQuan +"*" +mult );
                    quan = origQuan + additional; // add it

                }else if (diff > 0){// positive meaning, quantity decreased
                    double mult = diff / origServe;
                    double additional = origQuan * mult;
                    quan = origQuan - additional;  // subtract it
                }else{
                    quan = origQuan; // roll back to original
                }

            }else{
              quan = origQuan; // roll back to original
            }

            Log.i("quan", "quan is "+quan);
            /*
            cup
            fl. oz
            tbsp
            tsp
             */
            double newQuan = 0;
            String newUnit = "";
            try{
                    /*
                    We need to try and catch because there still ing that dont have ing_code,
                    so meaning NOT ALL has Integer Value for Ing_Code, some is NULL or some is Empty..
                    If parsing is successful, meaning it has valid ing_code,
                     If NOT, for now display a toast informing the error, see Catch Below
                     */

                int ingCode = Integer.parseInt(c.getString(1)); // this has nothing to do with the conversion

                if(unit.equals("cup")) { // if cup is too small
                    if (quan < 0.25) {
                        newQuan = UnitConverter.convertVolume("cup", quan, "tbsp");
                        newUnit = "tbsp";
                        Log.i("conv", unit + " to " + newUnit + ": " + quan + " : " + newQuan + " " +  c.getString(3));
                         quan = newQuan;
                        unit = newUnit ;
                    }
                }
                if(unit.equals("tsp")) { // if tsp is more than 10, too many
                    if (quan > 10) {
                        newQuan = UnitConverter.convertVolume("tsp", quan, "tbsp");
                        newUnit = "tbsp";
                        Log.i("conv", unit + " to " + newUnit + ": " + quan + " : " + newQuan + " " +  c.getString(3));
                        quan = newQuan;
                        unit = newUnit ;
                    }
                }

                if(unit.equals("tbsp")) { // if tbsp is more than 10, too many
                    if (quan > 10) {
                        newQuan = UnitConverter.convertVolume("tbsp", quan, "fl. oz");
                        newUnit = "fl. oz";
                        Log.i("conv", unit + " to " + newUnit + ": " + quan + " : " + newQuan + " " +  c.getString(3));
                        quan = newQuan;
                        unit = newUnit ;
                    }
                }


                if(unit.equals("tbsp")) { // if tbsp is too small
                    if (quan < 0.5) {
                        newQuan = UnitConverter.convertVolume("tbsp", quan, "tsp");
                        newUnit = "tsp";
                        Log.i("conv", unit + " to " + newUnit + ": " + quan + " : " + newQuan + " " +  c.getString(3));
                        quan = newQuan;
                        unit = newUnit ;
                    }
                }

                if(unit.equals("fl.oz")) { // if tbsp is more than 10, too many
                    if (quan > 10) {
                        newQuan = UnitConverter.convertVolume("fl. oz", quan, "cup");
                        newUnit = "cup";
                        Log.i("conv", unit + " to " + newUnit + ": " + quan + " : " + newQuan + " " +  c.getString(3));
                        quan = newQuan;
                        unit = newUnit ;
                    }
                }
//============================FOR MASS====================================================================================================
/*
g
lb
oz
//
mg
kg
 */
                if(unit.equals("g")) { // if g is too small
                    if (quan < 1) {
                        newQuan = UnitConverter.convertVolume("g", quan, "mg");
                        newUnit = "mg";
                        Log.i("conv", unit + " to " + newUnit + ": " + quan + " : " + newQuan + " " +  c.getString(3));
                        quan = newQuan;
                        unit = newUnit ;
                    }
                }

                if(unit.equals("g")) { // if g is too small
                    if (quan > 1000) {
                        newQuan = UnitConverter.convertVolume("g", quan, "kg");
                        newUnit = "kg";
                        Log.i("conv", unit + " to " + newUnit + ": " + quan + " : " + newQuan + " " +  c.getString(3));
                        quan = newQuan;
                        unit = newUnit ;
                    }
                }


                if(unit.equals("lb")) { // if lb is too small
                    if (quan < 1) {
                        newQuan = UnitConverter.convertVolume("lb", quan, "oz");
                        newUnit = "oz";
                        Log.i("conv", unit + " to " + newUnit + ": " + quan + " : " + newQuan + " " +  c.getString(3));
                        quan = newQuan;
                        unit = newUnit ;
                    }
                }


                if(unit.equals("oz")) { // if lb is too small
                    if (quan > 32) {
                        newQuan = UnitConverter.convertVolume("oz", quan, "lb");
                        newUnit = "lb";
                        Log.i("conv", unit + " to " + newUnit + ": " + quan + " : " + newQuan + " " +  c.getString(3));
                        quan = newQuan;
                        unit = newUnit ;
                    }
                }


                    newQuan = quan;
                    newUnit = unit;


            }catch (Exception e){
                newQuan = quan;
                newUnit = unit;
            }
                                //IngNeed(int needID, int ingCode, double newQuan, String item, String assoc, String stat, String unit)
            IngNeed ingNeed = new IngNeed(c.getInt(0), c.getString(1), newQuan, c.getString(3), c.getString(4), c.getString(5), newUnit);
            ingNeedList.add(ingNeed);
        }

        dataAdapter = new CustomIngNeedAdapter(this.getApplicationContext(),R.layout.rec_ings_info, ingNeedList);
        listView.setAdapter(dataAdapter);

    }

//==========================================================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_fragment_menu, menu);
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

            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return true;
    }


    public void quanPlus(View v){
        plus(modServe, 1);
    }

    public void quanMinus(View v){
        minus(modServe, 1);
    }


    private void plus(EditText t, int inc_dec){ //increment and decrement

        String s = t.getText().toString();
        try{
            int i = Integer.parseInt(s);
            i = i + inc_dec;
            t.setText(i + "");
        }catch (Exception e){
            int i = origServe;
            i = i + inc_dec;
            t.setText(i + "");
        }

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
            int i = origServe;
            i = i - inc_dec;
            t.setText(i + "");
        }

    }








}//class END
