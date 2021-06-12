package com.mit.jamsubzero.wisecook.RecipeResult;

/**
 * Created by jamsubzero on 4/20/2016.
 */
public class IngNeed {

   private int needID;
    private String ingCode;
    private double newQuan;
    private String item;
    private String assoc;
    private String stat;
    private String unit;

      /*
    SELECT ing_need.needID,
        ing_need.ing_code,
        ing_need.newQuan,
        ing_need.item,
        ing_need.stat,
        ing_units.unit
     */


    public IngNeed(int needID, String ingCode, double newQuan, String item, String assoc, String stat, String unit) {
        super();
        this.needID = needID;
        this.ingCode = ingCode;
        this.newQuan = newQuan;
        this.item = item;
        this.assoc = assoc;
        this.stat = stat;
        this.unit = unit;
    }

    public int getNeedID() {
        return needID;
    }

    public void setNeedID(int needID) {
        this.needID = needID;
    }

    public String getIngCode() {
        return ingCode;
    }

    public void setIngCode(String ingCode) {
        this.ingCode = ingCode;
    }

    public double getNewQuan() {
        return newQuan;
    }

    public void setNewQuan(double newQuan) {
        this.newQuan = newQuan;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAssoc() {
        return assoc;
    }

    public void setAssoc(String assoc) {
        this.assoc = assoc;
    }
}
