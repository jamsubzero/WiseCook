package com.mit.jamsubzero.wisecook;

import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.mit.jamsubzero.wisecook.Bookmark.BookmarksDialog;

/**
 * Created by jamsubzero on 5/27/2016.
 */
public class ActionBarEvents {

    public static void displayPreferences(final AppCompatActivity activity ){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.preferences_dialog, null);
        dialogBuilder.setView(dialogView);

        final Switch diaSwitch = (Switch) dialogView.findViewById(R.id.prefSwitch);


        SharedPreferences preferences = activity.getSharedPreferences("WiseCook", activity.MODE_PRIVATE);

        diaSwitch.setChecked(preferences.getBoolean("dontAsk", false));

        Button prefSave = (Button) dialogView.findViewById(R.id.prefSave);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        //========================================================================================

        prefSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = activity.getSharedPreferences("WiseCook", activity.MODE_PRIVATE);
                SharedPreferences.Editor edit= preferences.edit();

                if(diaSwitch.isChecked()){
                    edit.putBoolean("dontAsk", true);
                    edit.commit();

                }else{
                    edit.putBoolean("dontAsk", false);
                    edit.commit();
                }
                alertDialog.dismiss();
                Toast.makeText(activity, "Settings saved", Toast.LENGTH_SHORT).show();
            }
        });

//=====================================================================
        Button prefCancel = (Button) dialogView.findViewById(R.id.prefCancel);

        prefCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
    }


    public static void displayBookMarks(final AppCompatActivity activity){
        BookmarksDialog mydialog = new BookmarksDialog(activity);
        mydialog.show();
    }

    public static void displayAbout(final AppCompatActivity activity){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.about_layout, null);
        dialogBuilder.setView(dialogView);

        Button ok = (Button) dialogView.findViewById(R.id.aboutCloseBtn);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

    }


} //class en
