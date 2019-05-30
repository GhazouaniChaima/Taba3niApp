package com.example.badr.happysync;

import android.*;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LandingPage extends AppCompatActivity implements RecognitionListener {

    private static final int PERMISSIONS_REQUEST = 100;
    private SpeechRecognizer speech;

    private  String language = "fr";
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
        } else {
            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTrackerService() {
        startService(new Intent(this, TrackerService.class));
        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();
        finish();
    }

    String longitude = "0.0";
    String latitude = "0.0";

    TextToSpeech repeatTTS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        final String name = getIntent().getStringExtra("name");
        final String email = getIntent().getStringExtra("email");
        final String password = getIntent().getStringExtra("password");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final RelativeLayout header = (RelativeLayout) findViewById(R.id.Pageheader);
        final RelativeLayout footer = (RelativeLayout) findViewById(R.id.footerContainer);
        final ImageView chatbot = (ImageView) findViewById(R.id.chatbotbtn);



        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "fr");
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());

        speech.startListening(intent);



        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getApplicationContext(),"can't get location",Toast.LENGTH_SHORT).show();
        }
        int permission = ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"Mettre a jour de position...",Toast.LENGTH_SHORT).show();
            LocationRequest request = new LocationRequest();
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
            final String pathLongitude = "comptes" + "/" +password+""+name+"/"+"longitude";
            final String pathLatitude = "comptes" + "/" + password+""+name+"/"+"latitude";
            permission = ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (permission == PackageManager.PERMISSION_GRANTED) {
                client.requestLocationUpdates(request, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        DatabaseReference refLongitude = FirebaseDatabase.getInstance().getReference(pathLongitude);
                        DatabaseReference refLatitude = FirebaseDatabase.getInstance().getReference(pathLatitude);

                        Location location = locationResult.getLastLocation();
                        if (location != null) {
                            refLongitude.setValue(String.valueOf(location.getLongitude()));
                            refLatitude.setValue(String.valueOf(location.getLatitude()));
                            longitude = String.valueOf(location.getLongitude());
                            latitude = String.valueOf(location.getLatitude());

                        }
                    }
                }, null);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }



        //**** Theme ***//
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("comptes");


        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                    for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                        String emailfromDB = zoneSnapshot.child("email").getValue(String.class);
                        if (emailfromDB.equals(email)) {
                            language = zoneSnapshot.child("lang").getValue(String.class);
                            String theme = zoneSnapshot.child("theme").getValue(String.class);

                            if (theme.equals("rouge")){
                                final int sdk = android.os.Build.VERSION.SDK_INT;
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    header.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.redbackground) );
                                    footer.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.redfooter) );
                                    chatbot.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.robotr) );
                                } else {
                                    header.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.redbackground));
                                    footer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.redfooter));
                                    chatbot.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.robotr));
                                }
                                break;
                            }
                            else if (theme.equals("bleu")){
                                final int sdk = android.os.Build.VERSION.SDK_INT;
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    header.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.loginheader) );
                                    footer.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.footer) );
                                    chatbot.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.robotp) );
                                } else {
                                    header.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.loginheader));
                                    footer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.footer));
                                    chatbot.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.robotp));
                                }
                                break;
                            }
                            else if (theme.equals("violet")){
                                final int sdk = android.os.Build.VERSION.SDK_INT;
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    header.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purplebackground) );
                                    footer.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purplefooter) );
                                    chatbot.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.robotb) );
                                } else {
                                    header.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purplebackground));
                                    footer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purplefooter));
                                    chatbot.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.robotb));
                                }
                                break;
                            }
                            else{
                                final int sdk = android.os.Build.VERSION.SDK_INT;
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    header.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pinkbackground) );
                                    footer.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pinkfooter) );
                                    chatbot.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.robotpink) );
                                } else {
                                    header.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pinkbackground));
                                    footer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pinkfooter));
                                    chatbot.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.robotpink));
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


        ImageView mapBtn = (ImageView) findViewById(R.id.MapButton);
        Button busstations = (Button) findViewById(R.id.mapbusstation);
        CardView maplignemetro = (CardView) findViewById(R.id.maplignemetro);

        Button kiosques = (Button) findViewById(R.id.kiosqueBTN);
        Button parking = (Button) findViewById(R.id.parkingBTN);
        Button events = (Button) findViewById(R.id.btnEvent);

        ImageView settings = (ImageView) findViewById(R.id.settingbutton);
        ImageView logout = (ImageView) findViewById(R.id.logoutbtn);

        CardView lignebus = (CardView) findViewById(R.id.lignebus);

        ImageView games = (ImageView) findViewById(R.id.JeuButton);


        Button places = (Button) findViewById(R.id.placesbtn);

        places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(),Marafe9.class));
            }
        });

        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),GameActivity.class).putExtra("email",email));
            }
        });

        maplignemetro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("type","lignemetro").putExtra("email",email).putExtra("longitude",longitude).putExtra("latitude",latitude));
            }
        });


        busstations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("type","bus").putExtra("email",email).putExtra("longitude",longitude).putExtra("latitude",latitude));
            }
        });




        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("type","all").putExtra("email",email).putExtra("longitude",longitude).putExtra("latitude",latitude));
            }
        });

        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Calendar rightNow = Calendar.getInstance();
                final int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);

                if (currentHour > 22 && currentHour < 5){
                        startActivity(new Intent(getApplicationContext(), SleepRobot.class));
                }*/
                /*else {*/

                    if (language.equals("fr")){
                        Intent intent = new Intent(getApplicationContext(),ChatbotConversation.class).putExtra("longitude",longitude).putExtra("latitude",latitude);
                        intent.putExtra("name",name);
                        intent.putExtra("email",email);
                        intent.putExtra("password",password);

                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(),EnglishChatbot.class).putExtra("longitude",longitude).putExtra("latitude",latitude);
                        intent.putExtra("name",name);
                        intent.putExtra("email",email);
                        intent.putExtra("password",password);

                        startActivity(intent);
                    }

                //}
            }
        });

        kiosques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("type","kiosque").putExtra("email",email).putExtra("longitude",longitude).putExtra("latitude",latitude));
            }
        });

        parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("type","parking").putExtra("email",email).putExtra("longitude",longitude).putExtra("latitude",latitude));
            }
        });

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EventPage.class).putExtra("email",email).putExtra("longitude",longitude).putExtra("latitude",latitude));
            }
        });

        lignebus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("type","lignebus").putExtra("email",email).putExtra("longitude",longitude).putExtra("latitude",latitude));
            }
        });


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LandingPage.this);
                LayoutInflater inflater = LandingPage.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.settinglayout, null);
                dialogBuilder.setView(dialogView);

                final Spinner themeslist = (Spinner) dialogView.findViewById(R.id.themeslist);
                List<String> list = new ArrayList<String>();
                list.add("bleu");
                list.add("violet");
                list.add("rouge");
                list.add("rose");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinneritem, list);
                dataAdapter.setDropDownViewResource(R.layout.spinneritem);
                themeslist.setAdapter(dataAdapter);





                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                Button okbtn = (Button) dialogView.findViewById(R.id.savebtn);
                Button cancelbtn = (Button) dialogView.findViewById(R.id.cancelbtn);
                final Button frbtn = (Button) dialogView.findViewById(R.id.languagefr);
                final Button angbtn = (Button) dialogView.findViewById(R.id.languageang);




                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String theme = themeslist.getSelectedItem().toString();
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference().child("comptes");
                        String node = password + ""+name;
                        ref.child(node).child("theme").setValue(theme);
                        ref.child(node).child("lang").setValue(language);
                        finish();
                        startActivity(getIntent());
                    }
                });

                frbtn.setOnClickListener(new View.OnClickListener() {
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
                });

        }

        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(LandingPage.this);
                builder.setTitle("Exit");
                builder.setMessage("Do you want to exit ??");
                builder.setPositiveButton("Yes. Exit now!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){

                        finish();

                    }
                });
                builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){

                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getWindow().setLayout(600, 400);
            }
        });





    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onResults(Bundle bundle) {

    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + " ";
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }
}
