package com.mit.jamsubzero.wisecook.Data;

/**
 * Created by jamsubzero on 5/23/2016.
 */
public class UserIngredient {

    private int code;
    private String ingName;
    private String otherName;
    private String choosenUnit;
    private String defaultUnt;
    private double quantity;



    public UserIngredient(int code, String ingName, String otherName, String choosenUnit, String defaultUnit, double quantity){
        this.code = code;
        this.ingName = ingName;
        this.otherName = otherName;
        this.choosenUnit = choosenUnit;
        this.defaultUnt = defaultUnit;
        this.quantity = quantity;

    }


    public String getIngName() {
        return ingName;
    }

    public void setIngName(String ingName) {
        this.ingName = ingName;
    }

    public String getChoosenUnit() {
        return choosenUnit;
    }

    public void setChoosenUnit(String choosenUnit) {
        this.choosenUnit = choosenUnit;
    }

    public String getDefaultUnt() {
        return defaultUnt;
    }

    public void setDefaultUnt(String defaultUnt) {
        this.defaultUnt = defaultUnt;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
