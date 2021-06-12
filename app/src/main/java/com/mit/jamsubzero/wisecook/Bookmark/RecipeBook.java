package com.mit.jamsubzero.wisecook.Bookmark;

/**
 * Created by jamsubzero on 4/20/2016.
 */
public class RecipeBook {

   private int code = 0;
    private String name = null;
    private String date;

    public RecipeBook(int code, String name, String date) {
        super();
        this.code = code;
        this.name = name;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
