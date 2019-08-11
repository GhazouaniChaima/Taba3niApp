package com.example.badr.happysync;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LoadingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ImageView setting = findViewById(R.id.settingbtn);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoadingPage.this);
                LayoutInflater inflater = LoadingPage.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.settinglayout, null);
                dialogBuilder.setView(dialogView);
                final Spinner themeslist = (Spinner) dialogView.findViewById(R.id.themeslist);
                List<String> list = new ArrayList<String>();
                list.add("bleu");
                list.add("blue ciel");
                list.add("rose");
                list.add("oranger");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinneritem, list);
                dataAdapter.setDropDownViewResource(R.layout.spinneritem);
                themeslist.setAdapter(dataAdapter);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                Button okbtn = (Button) dialogView.findViewById(R.id.savebtn);
                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String theme = themeslist.getSelectedItem().toString();
                        switch (theme){
                            case "bleu":
                                runOnUiThread(new Runnable(){
                                    @TargetApi(Build.VERSION_CODES.M)
                                    @Override
                                    public void run() {
                                        findViewById(R.id.headerimg).setBackgroundResource(R.drawable.newblueheader);
                                        findViewById(R.id.footerimg).setBackgroundResource(R.drawable.newbluefooter);
                                        findViewById(R.id.chatbot).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.newblue)));
                                    }
                                });
                                break;
                            case "blue ciel":
                                runOnUiThread(new Runnable(){
                                    @TargetApi(Build.VERSION_CODES.M)
                                    @Override
                                    public void run() {
                                        findViewById(R.id.headerimg).setBackgroundResource(R.drawable.newcielheader);
                                        findViewById(R.id.footerimg).setBackgroundResource(R.drawable.newcielfooter);
                                        findViewById(R.id.chatbot).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.newciel)));
                                    }
                                });
                                break;
                            case "rose":
                                runOnUiThread(new Runnable(){
                                    @TargetApi(Build.VERSION_CODES.M)
                                    @Override
                                    public void run() {
                                        findViewById(R.id.headerimg).setBackgroundResource(R.drawable.headerimg);
                                        findViewById(R.id.footerimg).setBackgroundResource(R.drawable.secondimg);
                                        findViewById(R.id.chatbot).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.newpink)));
                                    }
                                });
                                break;
                            case "oranger":
                                runOnUiThread(new Runnable(){
                                    @TargetApi(Build.VERSION_CODES.M)
                                    @Override
                                    public void run() {
                                        findViewById(R.id.headerimg).setBackgroundResource(R.drawable.neworangeheader);
                                        findViewById(R.id.footerimg).setBackgroundResource(R.drawable.neworangefooter);
                                        findViewById(R.id.chatbot).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.neworange)));
                                    }
                                });
                                break;
                        }
                        alertDialog.dismiss();
                    }
                });

                /*frbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        language = "fr";
                        frbtn.setBackgroundResource(R.drawable.dialogbutton2);
                        angbtn.setBackgroundResource(R.drawable.dialogbutton1);
                        frbtn.setTextColor(Color.parseColor("#FFFFFF"));
                        angbtn.setTextColor(Color.parseColor("#5400f0"));
                    }
                });
                angbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        language = "ang";
                        angbtn.setBackgroundResource(R.drawable.dialogbutton2);
                        frbtn.setBackgroundResource(R.drawable.dialogbutton1);
                        frbtn.setTextColor(Color.parseColor("#5400f0"));
                        angbtn.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                });*/

            }

        });

        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.taba3nisong);
        mp.start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(getApplicationContext(),LoginSignup.class);
                startActivity(intent);
            }
        }, 6000);   //5 seconds*/
    }
}