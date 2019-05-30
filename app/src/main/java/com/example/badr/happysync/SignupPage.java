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
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class SignupPage extends AppCompatActivity {


    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    int emailNumbers = 0;
    boolean test = false;


    void testEmail (final String emailStr){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final EditText email = (EditText) findViewById(R.id.emailSignup);
        final EditText password = (EditText) findViewById(R.id.passwordSignup);
        final EditText name = (EditText) findViewById(R.id.nameSignup);

        findViewById(R.id.emailSignup).setOnFocusChangeListener(new View.OnFocusChangeListener() {
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


        findViewById(R.id.passwordSignup).setOnFocusChangeListener(new View.OnFocusChangeListener() {
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


        findViewById(R.id.nameSignup).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    name.getBackground().setColorFilter(Color.parseColor("#2B32B2"), PorterDuff.Mode.SRC_ATOP);
                    name.setTextColor(Color.parseColor("#2B32B2"));
                    name.setHintTextColor(Color.parseColor("#2B32B2"));
                }
                else{
                    name.getBackground().setColorFilter(Color.parseColor("#0087D6"), PorterDuff.Mode.SRC_ATOP);
                    name.setTextColor(Color.parseColor("#0087D6"));
                    name.setHintTextColor(Color.parseColor("#0087D6"));
                }
            }
        });



        findViewById(R.id.submitSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailStr = email.getText().toString();
                final String nameStr = name.getText().toString();
                final String passwordStr = password.getText().toString();

                Toast.makeText(getApplicationContext(),emailStr+" "+nameStr+" "+passwordStr,Toast.LENGTH_SHORT).show();

                if (nameStr.length()<3){
                    Toast.makeText(getApplicationContext(),"Invalid name",Toast.LENGTH_SHORT).show();
                }
                else if (isValidEmail(emailStr)==false){
                    Toast.makeText(getApplicationContext(),"Invalid email", Toast.LENGTH_SHORT).show();
                }
                else if (passwordStr.equals(" ")||passwordStr.equals("")){
                    Toast.makeText(getApplicationContext(),"Invalid password", Toast.LENGTH_SHORT).show();
                }
                else{

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("comptes");


                    ref.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (emailNumbers == 0) {
                                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                                    String emailfromDB = zoneSnapshot.child("email").getValue(String.class);
                                    if (emailfromDB.equals(emailStr)) {
                                        test = true;
                                    }
                                }
                                if (test) {
                                    Toast.makeText(getApplicationContext(), "Email already exist", Toast.LENGTH_SHORT).show();
                                    test = false;
                                } else {
                                    DatabaseReference ref = database.getReference().child("comptes");
                                    String node = passwordStr + ""+nameStr;
                                    //final DatabaseReference compte = ref.child("Compte");
                                    ref.child(node).child("email").setValue(emailStr);
                                    ref.child(node).child("password").setValue(passwordStr);
                                    ref.child(node).child("name").setValue(nameStr);
                                    ref.child(node).child("theme").setValue("bleu");
                                    ref.child(node).child("lang").setValue("fr");
                                    ref.child(node).child("longitude").setValue("0");
                                    ref.child(node).child("latitude").setValue("0");
                                    Intent intent = new Intent(getApplicationContext(),SuccessPage.class);
                                    intent.putExtra("email",emailStr);
                                    intent.putExtra("name",nameStr);
                                    intent.putExtra("password",passwordStr);
                                    startActivity(intent);
                                    emailNumbers = 1;

                                }
                            }
                        }




                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });


                }
            }
        });
    }
}
