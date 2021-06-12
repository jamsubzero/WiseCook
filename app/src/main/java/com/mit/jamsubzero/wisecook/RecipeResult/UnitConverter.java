package com.mit.jamsubzero.wisecook.RecipeResult;

/**
 * Created by jamsubzero on 5/24/2016.
 */
public class UnitConverter {

   public static double convertVolume(String fromUnit, double fromQuan, String toUnit){
        switch (fromUnit) {  //NOTE!!!!: _ is for replacement of color later after switch // "/n" is repalced by <br/>
            //<editor-fold desc="FOR VOLUME">
            case "cup":
                switch (toUnit) {
                    case "tsp":
                        return fromQuan * 48;

                    case "tbsp":
                        return fromQuan * 16;

                    case "fl. oz":
                        return fromQuan * 8;

                    case "l":
                        return fromQuan * 0.236588;

                    case "ml":
                        return fromQuan * 236.588;

                    case "pt":
                        return fromQuan * 0.5;

                    case "qt":
                        return fromQuan * 0.25;

                }
                break;
            case "tsp":
                switch (toUnit) {
                    case "cup":
                        return fromQuan * 0.0208333;

                    case "tbsp":
                        return fromQuan * 0.333333;

                    case "fl. oz":
                        return fromQuan * 0.166667;

                    case "l":
                        return fromQuan * 0.00492892;

                    case "ml":
                        return fromQuan * 4.92892;

                    case "pt":
                        return fromQuan * 0.0104167;

                    case "qt":
                        return fromQuan * 0.00520833;

                }
                break;
            case "tbsp":
                switch (toUnit) {
                    case "cup":
                        return fromQuan * 0.0625;

                    case "tsp":
                        return fromQuan * 3;

                    case "fl. oz":
                        return fromQuan * 0.5;

                    case "l":
                        return fromQuan * 0.0147868;

                    case "ml":
                        return fromQuan * 14.7868;

                    case "pt":
                        return fromQuan * 0.03125;

                    case "qt":
                        return fromQuan * 0.015625;

                }
                break;

            case "fl. oz":
                switch (toUnit) {
                    case "cup":
                        return fromQuan * 0.125;

                    case "tsp":
                        return fromQuan * 6;

                    case "tbsp":
                        return fromQuan * 2;

                    case "l":
                        return fromQuan * 0.0295735;

                    case "ml":
                        return fromQuan * 29.5735;

                    case "pt":
                        return fromQuan * 0.0625;

                    case "qt":
                        return fromQuan * 0.03125;

                }

                break;
            case "l":
                switch (toUnit) {
                    case "cup":
                        return fromQuan * 4.22675;

                    case "tsp":
                        return fromQuan * 202.884;

                    case "tbsp":
                        return fromQuan * 67.628;

                    case "fl.oz":
                        return fromQuan * 33.814;

                    case "ml":
                        return fromQuan * 1000;

                    case "pt":
                        return fromQuan * 2.11338;

                    case "qt":
                        return fromQuan * 1.05669;

                }
                break;
            case "ml":
                switch (toUnit) {
                    case "cup":
                        return fromQuan * 0.00422675;

                    case "tsp":
                        return fromQuan * 0.202884;

                    case "tbsp":
                        return fromQuan * 0.067628;

                    case "fl.oz":
                        return fromQuan * 0.033814;

                    case "l":
                        return fromQuan * 0.001;

                    case "pt":
                        return fromQuan * 0.00211338;

                    case "qt":
                        return fromQuan * 0.00105669;

                }
                break;
            case "pt":
                switch (toUnit) {
                    case "cup":
                        return fromQuan * 2;

                    case "tsp":
                        return fromQuan * 96;

                    case "tbsp":
                        return fromQuan * 32;

                    case "fl.oz":
                        return fromQuan * 16;

                    case "l":
                        return fromQuan * 0.473176;

                    case "ml":
                        return fromQuan * 473.176;

                    case "qt":
                        return fromQuan * 0.5;

                }
                break;
            case "qt":
                switch (toUnit) {
                    case "cup":
                        return fromQuan * 4;

                    case "tsp":
                        return fromQuan * 192;

                    case "tbsp":
                        return fromQuan * 64;

                    case "fl.oz":
                        return fromQuan * 32;

                    case "l":
                        return fromQuan * 0.946353;

                    case "ml":
                        return fromQuan * 946.353;

                    case "pt":
                        return fromQuan * 2;

                }
                break;
            //</editor-fold>

            //<editor-fold desc="FOR MASS">
            case "oz":
                switch (toUnit) {
                    case "lb":
                        return fromQuan * 0.0625;

                    case "kg":
                        return fromQuan * 0.0283495;

                    case "g":
                        return fromQuan * 28.3495;

                    case "mg":
                        return fromQuan * 28349.5;

                }
                break;
            case "lb":
                switch (toUnit) {
                    case "oz":
                        return fromQuan * 16;

                    case "kg":
                        return fromQuan * 0.453592;

                    case "g":
                        return fromQuan * 453.592;

                    case "mg":
                        return fromQuan * 453592;

                }
                break;
            case "kg":
                switch (toUnit) {
                    case "oz":
                        return fromQuan * 35.274;

                    case "lb":
                        return fromQuan * 2.20462;

                    case "g":
                        return fromQuan * 1000;

                    case "mg":
                        return fromQuan * 1000000;

                }
                break;
            case "g":
                switch (toUnit) {
                    case "oz":
                        return fromQuan * 0.035274;

                    case "lb":
                        return fromQuan * 0.00220462;

                    case "kg":
                        return fromQuan * 0.001;

                    case "mg":
                        return fromQuan * 1000;

                }
                break;
            case "mg":
                switch (toUnit) {
                    case "oz":
                        return fromQuan * 3.5274e-5;

                    case "lb":
                        return fromQuan * 2.20462e-6;

                    case "kg":
                        return fromQuan * 1e-6;

                    case "g":
                        return fromQuan * 0.001;

                }
                break;


            //</editor-fold>

        }
        ///===================================================================

        return -1; //if reached here, ERROR
    }



}
