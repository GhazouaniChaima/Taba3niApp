package com.example.badr.happysync;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LoginPage extends AppCompatActivity {

    boolean verifaccount = false ;
    int nb = -1;
    private int progressStatus = 0;

    private Handler handler = new Handler();

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password) ;
        Button btnLogin = (Button) findViewById(R.id.submitLogin);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getProgressDrawable().setColorFilter(
                Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String emailStr = email.getText().toString();
                final String passwordStr = password.getText().toString();


                if (isValidEmail(emailStr)==false || passwordStr.equals("") || passwordStr.equals(" ")){
                    Toast.makeText(getApplicationContext(), "please verify your informations",Toast.LENGTH_SHORT).show();
                }
                else {

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("comptes");


                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                    String emailfromDB = zoneSnapshot.child("email").getValue(String.class);
                                    String passwordfromDB = zoneSnapshot.child("password").getValue(String.class);
                                    if (emailfromDB.equals(emailStr) && passwordfromDB.equals(passwordStr)) {


                                        String name = zoneSnapshot.child("name").getValue(String.class);
                                        Intent intent = new Intent(getApplicationContext(), SuccessPage.class);
                                        intent.putExtra("name", name);
                                        intent.putExtra("email", emailStr);
                                        intent.putExtra("password", passwordStr);

                                        startActivity(intent);
                                        verifaccount = true;
                                        break;

                                    }

                            }
                            if (!verifaccount) {
                                Toast.makeText(getApplicationContext(), "Invalid account", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                }




            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    email.getBackground().setColorFilter(Color.parseColor("#2B32B2"), PorterDuff.Mode.SRC_ATOP);
                    email.setTextColor(Color.parseColor("#2B32B2"));
                    email.setHintTextColor(Color.parseColor("#2B32B2"));

                }
                else{
                    email.getBackground().setColorFilter(Color.parseColor("#0087D6"), PorterDuff.Mode.SRC_ATOP);
                    email.setTextColor(Color.parseColor("#0087D6"));
                    email.setHintTextColor(Color.parseColor("#0087D6"));
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    password.getBackground().setColorFilter(Color.parseColor("#2B32B2"), PorterDuff.Mode.SRC_ATOP);
                    password.setTextColor(Color.parseColor("#2B32B2"));
                    password.setHintTextColor(Color.parseColor("#2B32B2"));
                }
                else{
                    password.getBackground().setColorFilter(Color.parseColor("#0087D6"), PorterDuff.Mode.SRC_ATOP);
                    password.setTextColor(Color.parseColor("#0087D6"));
                    password.setHintTextColor(Color.parseColor("#0087D6"));
                }
            }
        });




    }
}
