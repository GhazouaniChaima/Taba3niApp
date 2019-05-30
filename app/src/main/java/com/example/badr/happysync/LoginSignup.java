package com.example.badr.happysync;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;
import java.util.Vector;

import static com.example.badr.happysync.R.drawable.loginsignup2;

public class LoginSignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ConstraintLayout constraint = (ConstraintLayout) findViewById(R.id.constraintTunisBackgrounds);

        Button btnLogin = (Button) findViewById(R.id.loginBtn) ;
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.signupBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignupPage.class));
            }
        });


        Random rand = new Random();
        int idImg = rand.nextInt(10);



        if (idImg==0){
            constraint.setBackground(ContextCompat.getDrawable( this , R.drawable.carthage));
        }
        else if (idImg == 1){
            constraint.setBackground(ContextCompat.getDrawable( this , R.drawable.sidiboumarket));
        }
        else if (idImg == 2){
            constraint.setBackground(ContextCompat.getDrawable( this , R.drawable.sidibou));
        }
        else if (idImg == 3){
            constraint.setBackground(ContextCompat.getDrawable( this , R.drawable.sidiboumarket1));
        }
        else if (idImg == 4){
            constraint.setBackground(ContextCompat.getDrawable( this , R.drawable.bebb7ar));
        }
        else if (idImg == 5){
            constraint.setBackground(ContextCompat.getDrawable( this , R.drawable.lavanue));
        }
        else if (idImg == 6){
            constraint.setBackground(ContextCompat.getDrawable( this , R.drawable.sidiboustreet));
        }
        else if (idImg == 7){
            constraint.setBackground(ContextCompat.getDrawable( this , R.drawable.centreville));
        }
        else if (idImg == 8){
            constraint.setBackground(ContextCompat.getDrawable( this , R.drawable.marsa));
        }
        else{
            constraint.setBackground(ContextCompat.getDrawable( this , R.drawable.medina));
        }


    }
}
