package com.mit.jamsubzero.wisecook.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jamsubzero on 4/20/2016.
 */
public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "wiseCookDB.db";
    private static final int DATABASE_VERSION = 10;

    public static final int CATEG_CUISINES = 1;
    public static final int CATEG_MEALS = 2;
    public static final int CATEG_COURSES = 3;
    public static final int CATEG_OCCASION = 4;
    public static final int CATEG_PREP = 5; // preparation method


    private final int RECIPE_LIMIT = 200;

    public static final int ING_TYPE_OTHER = 0;
    public static final int ING_TYPE_VOL = 1;
    public static final int ING_TYPE_MASS = 2;


    public static final String SELECT_ALL_INGS = "-1";

    Context context;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.setForcedUpgrade(); // TODO BACK UP BOOKMARKS TABLE BEFORE REPLACING DATABASE
        this.context = context;
    }

    public Cursor getCodes(int categ){
        SQLiteDatabase   mydb = this.getReadableDatabase();
        Cursor cursor = mydb.rawQuery("SELECT code, name FROM codes WHERE categ = "+categ+" ORDER BY name", null);
      //  Cursor cursor = mydb.rawQuery("SELECT recID, item FROM ing_need WHERE recID < 100", null);
        return cursor;
    }


    public Cursor searchIngs(String searchKey){
        searchKey = "%" + searchKey + "%";
        SQLiteDatabase   mydb = this.getReadableDatabase();
        Cursor cursor = mydb.rawQuery("SELECT code, item, otherNames FROM ing_codes WHERE item LIKE ? OR otherNames LIKE ? ORDER BY item", new String [] {searchKey, searchKey});
        //  Cursor cursor = mydb.rawQuery("SELECT recID, item FROM ing_need WHERE recID < 100", null);

        return cursor;
    }

    public Cursor getIng(String code){
        SQLiteDatabase   mydb = this.getReadableDatabase();
        Cursor cursor = null;
        if(code.equals(this.SELECT_ALL_INGS)) {
            cursor = mydb.rawQuery("SELECT code, item, otherNames FROM ing_codes", null);
            //  Cursor cursor = mydb.rawQuery("SELECT recID, item FROM ing_need WHERE recID < 100", null);
        }else{
            cursor = mydb.rawQuery("SELECT code, item, otherNames FROM ing_codes WHERE code = ?", new String[]{code});
        }
        return cursor;
    }


    public Cursor getRecipeInfo(String recID){
        SQLiteDatabase   mydb = this.getReadableDatabase();
        Cursor cursor = mydb.rawQuery("SELECT name, serving, prep, newPrep, kal FROM recipe WHERE recID = "+recID, null);
        return  cursor;
    }

    public Cursor getIngNeeds(String recID){
        SQLiteDatabase mydb = this.getReadableDatabase();
          String query = "SELECT ing_need.needID, \n" +
                  "        ing_need.ing_code, \n" +
                  "        ing_need.newQuan, \n" +
                  "        ing_need.item, \n" +
                  "        ing_codes.otherNames, \n" +
                  "        ing_need.stat,\n" +
                  "        ing_units.unit \n" +
                  "        FROM ing_need LEFT JOIN ing_units ON ing_need.ing_code = ing_units.ing_code \n" +
                  "                      LEFT JOIN ing_codes ON ing_need.ing_code = ing_codes.code \n" +
                  "        WHERE ing_need.recID = " + recID;
        Cursor cursor = mydb.rawQuery(query, null);
        return  cursor;
    }

    public Cursor getProcedures(String recID){
        SQLiteDatabase   mydb = this.getReadableDatabase();
        Cursor cursor = mydb.rawQuery("SELECT statement FROM procedures WHERE recID = "+recID+ " ORDER BY procID", null);
        return  cursor;
    }


    public Cursor getRecipeNow(String orderBy, String order){   // this is algo 1, normal algo, all qualifiers are present

        SQLiteDatabase mydb = this.getReadableDatabase();

        String includedIngs = "";
        String ingsQ = "";

        //=========================================================

        if(!Singleton.selIngs.isEmpty()) { //NOT EMPTY
            for (int ing : Singleton.selIngs.keySet()) {
                includedIngs = includedIngs + ing + ", ";
            }
            includedIngs = includedIngs.substring(0, includedIngs.lastIndexOf(","));
        ingsQ = " AND i.ing_code IN ( "+includedIngs+" ) "; //start with AND bcoz of 1 = 1 trick
        }

            // tested
        //===================================================

        String codesQ = "";
        for (int cuisine: Singleton.selCuisines.keySet()) {
            if(cuisine!=-1) {
                codesQ = codesQ + " AND r.keywords LIKE '%," + cuisine + ",%' ";
            }
        }
       for (int meal: Singleton.selMeals.keySet()){
           if(meal != -1) {
               codesQ = codesQ + " AND r.keywords LIKE '%," + meal + ",%' ";
           }
       }

        for (int course: Singleton.selCourses.keySet()){
            if(course != -1) {
                codesQ = codesQ + " AND r.keywords LIKE '%," + course + ",%' ";
            }
        }

        //===============================================

  //<editor-fold desc = "occasion and prep - comment for now" default = "collapsed">
/*
        for (int occasion: Singleton.selOccasion.keySet()){
            if(occasion != -1) {
                codesQ = codesQ + " AND r.keywords LIKE '%," + occasion + ",%' ";
            }
        }

        for (int prep: Singleton.selPrep.keySet()){
            if(prep != -1) {
                codesQ = codesQ + " AND r.keywords LIKE '%," + prep + ",%' ";
            }
        }

        */
        //</editor-fold>


        if(Singleton.prepQuan > 0){
            codesQ = codesQ + " AND r.newPrep <= " + Singleton.prepQuan + " ";
        }


        if(Singleton.calQuan > 0){
            codesQ = codesQ + " AND r.kal <= " + Singleton.calQuan + " ";
        }

        if(Singleton.servQuan > 0){
            codesQ = codesQ + " AND r.serving >= " + Singleton.servQuan + " ";
        }

        //===========================================================================


 //TODO If ingsQ or codesQ is NULL the Query IS Erroneous, HAndle condition here and produce separate query
String query = " SELECT r.recId, r.name, r.prep, r.newPrep, r.kal, r.serving, COUNT(r.recId) AS hit FROM recipe r  INNER JOIN ing_need i ON i.recid = r.recid "+
       " WHERE 1=1 "+ingsQ +  //1=1 bcoz of the AND at the start
       " "+codesQ+"  " +
       " GROUP BY r.recID ORDER BY "+orderBy+" "+order+" LIMIT "+this.RECIPE_LIMIT;

        Log.i("SQL", query);
        Cursor cursor = mydb.rawQuery(query, null);
        //  Cursor cursor = mydb.rawQuery("SELECT recID, item FROM ing_need WHERE recID < 100", null);

        return cursor;
    }

    //========================================================================

    public Cursor getRecipeNowAlgo2(String orderBy, String order){  // algo 2 has no other parameters

        SQLiteDatabase mydb = this.getReadableDatabase();

        String includedIngs = "";
        String ingsQ = "";

        //=========================================================

        if(!Singleton.selIngs.isEmpty()) { //NOT EMPTY
            for (int ing : Singleton.selIngs.keySet()) {
                includedIngs = includedIngs + ing + ", ";
            }
            includedIngs = includedIngs.substring(0, includedIngs.lastIndexOf(","));
            ingsQ = " AND i.ing_code IN ( "+includedIngs+" ) "; //start with AND bcoz of 1 = 1 trick
        }

        // tested
        //===================================================

        String codesQ = "";
        for (int cuisine: Singleton.selCuisines.keySet()) {
            if(cuisine!=-1) {
                codesQ = codesQ + " AND r.keywords LIKE '%," + cuisine + ",%' ";
            }
        }
        for (int meal: Singleton.selMeals.keySet()){
            if(meal != -1) {
                codesQ = codesQ + " AND r.keywords LIKE '%," + meal + ",%' ";
            }
        }

        for (int course: Singleton.selCourses.keySet()){
            if(course != -1) {
                codesQ = codesQ + " AND r.keywords LIKE '%," + course + ",%' ";
            }
        }

        //===========================================================================


        //TODO If ingsQ or codesQ is NULL the Query IS Erroneous, HAndle condition here and produce separate query
        String query = " SELECT r.recId, r.name, r.prep, r.newPrep, r.kal, r.serving, COUNT(r.recId) AS hit FROM recipe r  INNER JOIN ing_need i ON i.recid = r.recid "+
                " WHERE 1=1 "+ingsQ +  //1=1 bcoz of the AND at the start
                " "+codesQ+"  " +
                " GROUP BY r.recID ORDER BY "+orderBy+" "+order+" LIMIT "+this.RECIPE_LIMIT;

        Log.i("SQL", query);
        Cursor cursor = mydb.rawQuery(query, null);
        //  Cursor cursor = mydb.rawQuery("SELECT recID, item FROM ing_need WHERE recID < 100", null);

        return cursor;
    }


    public Cursor getRecipeNowAlgo3(String orderBy, String order){  // algo 3 has NO COURSES qualifier

        SQLiteDatabase mydb = this.getReadableDatabase();

        String includedIngs = "";
        String ingsQ = "";

        //=========================================================

        if(!Singleton.selIngs.isEmpty()) { //NOT EMPTY
            for (int ing : Singleton.selIngs.keySet()) {
                includedIngs = includedIngs + ing + ", ";
            }
            includedIngs = includedIngs.substring(0, includedIngs.lastIndexOf(","));
            ingsQ = " AND i.ing_code IN ( "+includedIngs+" ) "; //start with AND bcoz of 1 = 1 trick
        }

        // tested
        //===================================================

        String codesQ = "";
        for (int cuisine: Singleton.selCuisines.keySet()) {
            if(cuisine!=-1) {
                codesQ = codesQ + " AND r.keywords LIKE '%," + cuisine + ",%' ";
            }
        }
        for (int meal: Singleton.selMeals.keySet()){
            if(meal != -1) {
                codesQ = codesQ + " AND r.keywords LIKE '%," + meal + ",%' ";
            }
        }

        //===========================================================================

        //TODO If ingsQ or codesQ is NULL the Query IS Erroneous, HAndle condition here and produce separate query
        String query = " SELECT r.recId, r.name, r.prep, r.newPrep, r.kal, r.serving, COUNT(r.recId) AS hit FROM recipe r  INNER JOIN ing_need i ON i.recid = r.recid "+
                " WHERE 1=1 "+ingsQ +  //1=1 bcoz of the AND at the start
                " "+codesQ+"  " +
                " GROUP BY r.recID ORDER BY "+orderBy+" "+order+" LIMIT "+this.RECIPE_LIMIT;

        Log.i("SQL", query);
        Cursor cursor = mydb.rawQuery(query, null);
        //  Cursor cursor = mydb.rawQuery("SELECT recID, item FROM ing_need WHERE recID < 100", null);

        return cursor;
    }



    public Cursor getRecipeNowAlgo4(String orderBy, String order){  // algo 4 has NO INGREDIENTS qualifier

        SQLiteDatabase mydb = this.getReadableDatabase();

        String includedIngs = "";
        String ingsQ = "";

        //=========================================================


        //===================================================

        String codesQ = "";
        for (int cuisine: Singleton.selCuisines.keySet()) {
            if(cuisine!=-1) {
                codesQ = codesQ + " AND r.keywords LIKE '%," + cuisine + ",%' ";
            }
        }
        for (int meal: Singleton.selMeals.keySet()){
            if(meal != -1) {
                codesQ = codesQ + " AND r.keywords LIKE '%," + meal + ",%' ";
            }
        }

        //===========================================================================

        //TODO If ingsQ or codesQ is NULL the Query IS Erroneous, HAndle condition here and produce separate query
        String query = " SELECT r.recId, r.name, r.prep, r.newPrep, r.kal, r.serving, COUNT(r.recId) AS hit FROM recipe r  INNER JOIN ing_need i ON i.recid = r.recid "+
                " WHERE 1=1 "+ingsQ +  //1=1 bcoz of the AND at the start
                " "+codesQ+"  " +
                " GROUP BY r.recID ORDER BY "+orderBy+" "+order+" LIMIT "+this.RECIPE_LIMIT;

        Log.i("SQL", query);
        Cursor cursor = mydb.rawQuery(query, null);
        //  Cursor cursor = mydb.rawQuery("SELECT recID, item FROM ing_need WHERE recID < 100", null);

        return cursor;
    }



    public Cursor getIngWithUnit(int code){
        SQLiteDatabase   mydb = this.getReadableDatabase();
        Cursor cursor = mydb.rawQuery(" SELECT item, otherNames, unit, type FROM ing_codes " +
                "INNER JOIN ing_units ON ing_codes.code = ing_units.ing_code WHERE ing_codes.code = "+code, null);
        return  cursor;
          }



    public void saveUserIng(){
        SQLiteDatabase  mydb = this.getWritableDatabase();
        mydb.beginTransaction();
        String names = "";
        //clear the table first
        mydb.execSQL("DELETE FROM saved_userIng");
        for(UserIngredient ing: Singleton.selIngs.values()) {
            int code = ing.getCode();
            String ingName = ing.getIngName();
                                   names = names + ingName;
            String otherName = ing.getOtherName();
            String choosenUnit = ing.getChoosenUnit();
            String defaultUnit = ing.getDefaultUnt();
            double quantity = ing.getQuantity();
            String query =  "INSERT INTO saved_userIng VALUES" +
            " ("+code+", '"+ingName+"', '"+otherName+"', '"+choosenUnit+"', '"+defaultUnit+"', "+quantity+");";
            mydb.execSQL(query);

                    }

        mydb.setTransactionSuccessful();
        Toast.makeText(context, "Ingredient saved", Toast.LENGTH_SHORT).show();
        mydb.endTransaction();
    }

    public Cursor getSavedUserIng(){
        SQLiteDatabase   mydb = this.getReadableDatabase();
        Cursor cursor = mydb.rawQuery(" SELECT * FROM saved_userIng", null);
        return  cursor;
    }

    public boolean bookmarkRecipe(String recID, String recName, String recDate){
        SQLiteDatabase mydb = this.getWritableDatabase();
        boolean saved = true; // 1 true, 0 false
        try {
            mydb.execSQL("INSERT INTO bookmarks (recID, recName, date) VALUES (" + recID + ", ?, '"+recDate+"')", new String[]{recName});
        }catch (SQLiteException ex){
            saved = false;
        }
        return saved;
    }

    public boolean isBookmarkExists(String recID){
        SQLiteDatabase mydb = this.getReadableDatabase();
        Cursor c = mydb.rawQuery("SELECT * FROM bookmarks WHERE recID = " + recID, null);
        return  c.moveToNext();
    }

    public Cursor getBookmarks(){
        SQLiteDatabase mydb = this.getReadableDatabase();
        Cursor c = mydb.rawQuery("SELECT recID, recName, date FROM bookmarks ORDER BY rowid DESC", null);
        return  c;
    }

    public boolean removeBookmark(int recID){
        boolean success = true;
        SQLiteDatabase mydb = this.getWritableDatabase();
        try {
            mydb.execSQL("DELETE FROM bookmarks WHERE recID = " + recID);
        }catch (SQLiteException e){
            success = false;
        }
        return success;
    }


}
