package com.mit.jamsubzero.wisecook.Others;

/**
 * Created by jamsubzero on 4/20/2016.
 */
public class Occasion {

        private String item;
        private int id;

        public Occasion(int id, String item) {
           this.item = item;
            this.id = id;
        }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
        public String toString() {
            return this.item;
        }
    }

