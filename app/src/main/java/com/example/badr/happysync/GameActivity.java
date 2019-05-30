package com.example.badr.happysync;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.badr.happysync.LandingPage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final ConstraintLayout background = (ConstraintLayout) findViewById(R.id.GameActivityBackground);
        final Button basket = (Button) findViewById(R.id.basket1);
        final Button j2048 = (Button) findViewById(R.id.j20481);
        final Button piano = (Button) findViewById(R.id.pianoc1);
        final Button knife = (Button) findViewById(R.id.knife1);
        final Button stack = (Button) findViewById(R.id.stack1);
        final Button sausage = (Button) findViewById(R.id.sausage1) ;
        final Button botle = (Button) findViewById(R.id.bottle1) ;
        final Button color = (Button) findViewById(R.id.color1) ;
        final Button fruit = (Button) findViewById(R.id.fruit1) ;
        final Button doodle = (Button) findViewById(R.id.doodle1) ;
        final Button stick = (Button) findViewById(R.id.stickman1) ;
        final Button enless = (Button) findViewById(R.id.uno1);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("comptes");

        final String email = getIntent().getStringExtra("email");
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    String emailfromDB = zoneSnapshot.child("email").getValue(String.class);
                    if (emailfromDB.equals(email)) {

                        String theme = zoneSnapshot.child("theme").getValue(String.class);

                        if (theme.equals("rouge")){
                            final int sdk = android.os.Build.VERSION.SDK_INT;
                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                background.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.redbackground) );
                                basket.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                j2048.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                piano.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                knife.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                stack.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                sausage.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                botle.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                color.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                fruit.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                doodle.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                stick.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                enless.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );

                            } else {
                                background.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.redbackground));
                                basket.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                j2048.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                piano.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                knife.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                stack.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                sausage.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                botle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                color.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                fruit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                doodle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                stick.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );
                                enless.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.red) );

                            }
                            break;
                        }
                        else if (theme.equals("bleu")){
                            final int sdk = android.os.Build.VERSION.SDK_INT;
                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                background.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.loginheader) );
                                basket.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                j2048.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                piano.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                knife.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                stack.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                sausage.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                botle.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                color.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                fruit.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                doodle.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                stick.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                enless.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                            } else {
                                background.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.loginheader));
                                basket.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                j2048.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                piano.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                knife.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                stack.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                sausage.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                botle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                color.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                fruit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                doodle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                stick.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                                enless.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bleu) );
                            }
                            break;
                        }
                        else if (theme.equals("violet")){
                            final int sdk = android.os.Build.VERSION.SDK_INT;
                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                background.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purplebackground) );
                                basket.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                j2048.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                piano.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                knife.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                stack.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                sausage.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                botle.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                color.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                fruit.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                doodle.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                stick.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                enless.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                            } else {
                                background.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purplebackground));
                                basket.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                j2048.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                piano.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                knife.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                stack.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                sausage.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                botle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                color.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                fruit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                doodle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                stick.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                                enless.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purp) );
                            }
                            break;
                        }
                        else{
                            final int sdk = android.os.Build.VERSION.SDK_INT;
                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                background.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pinkbackground) );
                                basket.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                j2048.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                piano.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                knife.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                stack.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                sausage.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                botle.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                color.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                fruit.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                doodle.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                stick.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                enless.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                            } else {
                                background.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pinkbackground));
                                basket.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                j2048.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                piano.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                knife.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                stack.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                sausage.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                botle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                color.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                fruit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                doodle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                stick.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                                enless.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.grisback) );
                            }
                            break;
                        }
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://m.silvergames.com/game/basketball-frvr/" ;
                Intent launchNextActivity;
                launchNextActivity = new Intent(getApplicationContext(), Main6Activity.class);
                launchNextActivity.putExtra("url",url) ;
                startActivity(launchNextActivity);
            }
        });

        j2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://m.silvergames.com/game/2048/" ;
                Intent launchNextActivity;
                launchNextActivity = new Intent(getApplicationContext(), Main6Activity.class);
                launchNextActivity.putExtra("url",url) ;
                startActivity(launchNextActivity);
            }
        });

        enless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://f3.silvergames.com/m/stack/";
                Intent launchNextActivity;
                launchNextActivity = new Intent(getApplicationContext(), Main6Activity.class);
                launchNextActivity.putExtra("url",url) ;
                startActivity(launchNextActivity);
            }
        });

        piano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url ="http://m.silvergames.com/game/piano-tiles/" ;
                Intent launchNextActivity;
                launchNextActivity = new Intent(getApplicationContext(), Main6Activity.class);
                launchNextActivity.putExtra("url",url) ;
                startActivity(launchNextActivity);
            }
        });


        knife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://m.silvergames.com/game/knife-hit/" ;
                Intent launchNextActivity;
                launchNextActivity = new Intent(getApplicationContext(), Main6Activity.class);
                launchNextActivity.putExtra("url",url) ;
                startActivity(launchNextActivity);
            }
        });


        stack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://m.silvergames.com/game/stick-samurai/" ;
                Intent launchNextActivity;
                launchNextActivity = new Intent(getApplicationContext(), Main6Activity.class);
                launchNextActivity.putExtra("url",url) ;
                startActivity(launchNextActivity);
            }
        });


        sausage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://m.silvergames.com/game/run-sausage-run/" ;
                Intent launchNextActivity;
                launchNextActivity = new Intent(getApplicationContext(), Main6Activity.class);
                launchNextActivity.putExtra("url",url) ;
                startActivity(launchNextActivity);
            }
        });


        botle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://m.silvergames.com/game/bottle-flip/" ;
                Intent launchNextActivity;
                launchNextActivity = new Intent(getApplicationContext(), Main6Activity.class);
                launchNextActivity.putExtra("url",url) ;
                startActivity(launchNextActivity);
            }
        });


        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://m.silvergames.com/game/find-color/" ;
                Intent launchNextActivity;
                launchNextActivity = new Intent(getApplicationContext(), Main6Activity.class);
                launchNextActivity.putExtra("url",url) ;
                startActivity(launchNextActivity);
            }
        });


        fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://m.silvergames.com/game/fruita-swipe/";
                Intent launchNextActivity;
                launchNextActivity = new Intent(getApplicationContext(), Main6Activity.class);
                launchNextActivity.putExtra("url",url) ;
                startActivity(launchNextActivity);
            }
        });


        doodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://m.silvergames.com/game/doodle-jump/" ;
                Intent launchNextActivity;
                launchNextActivity = new Intent(getApplicationContext(), Main6Activity.class);
                launchNextActivity.putExtra("url",url) ;
                startActivity(launchNextActivity);
            }
        });


        stick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://m.silvergames.com/game/stickman-archer-3/" ;
                Intent launchNextActivity;
                launchNextActivity = new Intent(getApplicationContext(), Main6Activity.class);
                launchNextActivity.putExtra("url",url) ;
                startActivity(launchNextActivity);
            }
        });


    }

}
