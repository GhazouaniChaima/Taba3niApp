package com.example.badr.happysync;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;


public class SuccessPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_page);

        ImageView gifimg = (ImageView) findViewById(R.id.gifimg);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layoutbackground);

        final String name = getIntent().getStringExtra("name");
        final String email = getIntent().getStringExtra("email");
        final String password = getIntent().getStringExtra("password");

        Random rand = new Random();
        int idImg = rand.nextInt(4)+1;


       if (idImg == 1){

            gifimg.setImageResource(R.drawable.robotrun);
            layout.setBackgroundColor(Color.parseColor("#EFEFEF"));
             Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(getApplicationContext(),LandingPage.class);
                intent.putExtra("email",email);
                intent.putExtra("name",name);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        }, 3000);   //5 seconds
        }
        else if (idImg == 2){

            gifimg.setImageResource(R.drawable.robothappy);
            layout.setBackgroundColor(Color.parseColor("#4B317A"));
             Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(getApplicationContext(),LandingPage.class);
                intent.putExtra("email",email);
                intent.putExtra("name",name);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        }, 3000);   //5 seconds
        }
        else {

            gifimg.setImageResource(R.drawable.happyrobot);
            layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
             Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(getApplicationContext(),LandingPage.class);
                intent.putExtra("email",email);
                intent.putExtra("name",name);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        }, 3000);   //5 seconds
        }
    }
}
