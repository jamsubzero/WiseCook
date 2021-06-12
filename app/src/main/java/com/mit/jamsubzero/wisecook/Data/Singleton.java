package com.mit.jamsubzero.wisecook.Data;

import java.util.HashMap;

/**
 * Created by jamsubzero on 4/21/2016.
 */
public class Singleton {


    public static boolean isBookmarkDisplayed = false;

    private static Singleton ourInstance = new Singleton();

    public static Singleton getInstance() {
        return ourInstance;
    }

    //================
    public static HashMap<Integer, String> selCuisines;
    static
    {
        selCuisines = new HashMap<>();
        selCuisines.put(-1, "(Any Cuisine)");
    }


    public static HashMap<Integer, String> selMeals ;
    static
    {
        selMeals = new HashMap<>();
        selMeals.put(-1, "(Any Meal)");
    }


    public static HashMap<Integer, String> selCourses ;
    static
    {
        selCourses = new HashMap<>();
        selCourses.put(-1, "(Any Meal)");
    }


    public static HashMap<Integer, String> selOccasion ;
    static
    {
        selOccasion = new HashMap<>();
        selOccasion.put(-1, "(Any)");
    }

    public static HashMap<Integer, String> selPrep ;
    static
    {
        selPrep = new HashMap<>();
        selPrep.put(-1, "(Any)");
    }


    public static int prepQuan = -1;
    public static int calQuan = -1;
    public static int servQuan = -1;


    public static HashMap<Integer, UserIngredient> selIngs = new HashMap<>();

    private Singleton() {
    }
}
