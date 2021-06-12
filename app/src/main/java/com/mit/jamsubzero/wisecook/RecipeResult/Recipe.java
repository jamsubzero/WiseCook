package com.mit.jamsubzero.wisecook.RecipeResult;

/**
 * Created by jamsubzero on 4/20/2016.
 */
public class Recipe {

   private int code = 0;
    private String name = null;
    private String info = null;



    public Recipe(int code, String name, String info) {
        super();
        this.code = code;
        this.name = name;
        this.info = info;
         }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }


}
