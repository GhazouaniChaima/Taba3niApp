package com.example.badr.happysync;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.*;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class EnglishChatbot extends AppCompatActivity {

    private TextToSpeech repeatTTS;
    private static final int REQ_CODE_SPEECH_INPUT = 100;


    int nb2 = 0;
    String dt = "";
    String ch5 = "";
    String url = "";
    Double diffLatitude = 0.006;
    Double diffLongitude = 0.011;
    String outputText = "";
    String userName = "";
    String PreviousAnswer = "";
    String email = "";
    String language = "fr";
    String longitudeString = "0.0";
    String latitudeString ="0.0";
    LatLng latLng = null;
    String stationName = "";
    String theme ="";
    Vector<Float> distances = new Vector<Float>();
    Vector<String> stationNames = new Vector<String>();
    LocationManager locationManager;
    HashMap<String, Integer> VillesIndex = new HashMap<String, Integer>();
    Vector<String> Villes = new Vector<String>();


    int couleur = 0 ;

    public void Intialize(){
        VillesIndex.put("passage",0);/** Bus Metro */
        VillesIndex.put("ariana",1);/** Bus Metro */
        VillesIndex.put("ben arous",2);/** Bus Metro */
        VillesIndex.put("manouba",3);/** Bus Metro */
        VillesIndex.put("marsa",4);/** Bus TGM */
        VillesIndex.put("sidi bousaid",5);/** Bus TGM */
        VillesIndex.put("carthage",6);/** Bus TGM */
        VillesIndex.put("jaafer",7);/** Bus */
        VillesIndex.put("aouina",8);/** Bus */
        VillesIndex.put("lac",9);/** Bus */
        VillesIndex.put("kram",10);/** Bus TGM */
        VillesIndex.put("la goulette",11);/** Bus TGM */
        VillesIndex.put("borj louzir",12);/** Bus */
        VillesIndex.put("manzah 6",13);/** Bus */
        VillesIndex.put("manzah 5",14);/** Bus */
        VillesIndex.put("manzah 7",15);/** Bus */
        VillesIndex.put("manzah 8",16);/** Bus */
        VillesIndex.put("manzah 9",17);/** Bus */
        VillesIndex.put("manar",18);/** Bus Metro */
        VillesIndex.put("bardo",19);/** Bus Metro */
        VillesIndex.put("danden",20);/** Bus Metro */
        VillesIndex.put("oued elil",21);/** Bus */
        VillesIndex.put("soukra",22);/** Bus */
        VillesIndex.put("khaznadar",23);/** Bus Metro */
        VillesIndex.put("zahrouni",24);/** Bus */
        VillesIndex.put("cite etadhamen",25);/** Bus Metro */
        VillesIndex.put("cite naser",26);/** Bus */
        VillesIndex.put("hay khadhra",27);/** Bus Metro */
        VillesIndex.put("mornagia",28);/** Bus Train */
        VillesIndex.put("mourouj",29);/** Bus Metro */
        VillesIndex.put("borj cedria",30);/** Bus Train */
        VillesIndex.put("rades",31);/** Bus Train */
        VillesIndex.put("megrin",32);/** Bus Train */
        VillesIndex.put("fouchana",33);/** Bus */
        VillesIndex.put("cite ghazela",34);/** Bus */
        VillesIndex.put("raoued",35);/** Bus */
        VillesIndex.put("daouar hicher",36);/** Bus */
        VillesIndex.put("tborba",37);/** Bus */
        VillesIndex.put("batan",38);/** Bus */
        VillesIndex.put("beb saadoun",39);/** Bus Metro */
        VillesIndex.put("kabaria",40);/** Bus Metro */
        VillesIndex.put("yasminete",41);/** Bus */
        VillesIndex.put("hamam lif",42);/** Bus Train */
        VillesIndex.put("gammarth",43);/** Bus */
        VillesIndex.put("sidi dhrif",44);/** Bus TGM */
        VillesIndex.put("ibn khaldoun",45);/** Bus Metro */
        VillesIndex.put("barcelone",46);/** Bus Metro Train */
        VillesIndex.put("tgm",47);/** Bus Metro TGM */



        Villes.add("passage");
        Villes.add("ariana");
        Villes.add("ben arous");
        Villes.add("manouba");
        Villes.add("marsa");
        Villes.add("sidi bousaid");
        Villes.add("carthage");
        Villes.add("jaafer");
        Villes.add("aouina");
        Villes.add("lac");
        Villes.add("kram");
        Villes.add("la goulette");
        Villes.add("borj louzir");
        Villes.add("manzah 6");
        Villes.add("manzah 5");
        Villes.add("manzah 7");
        Villes.add("manzah 8");
        Villes.add("manzah 9");
        Villes.add("manar");
        Villes.add("bardo");
        Villes.add("danden");
        Villes.add("oued elil");
        Villes.add("soukra");
        Villes.add("khaznadar");
        Villes.add("zahrouni");
        Villes.add("cite etadhamen");
        Villes.add("cite naser");
        Villes.add("hay khadhra");
        Villes.add("mornagia");
        Villes.add("mourouj");
        Villes.add("borj cedria");
        Villes.add("rades");
        Villes.add("megrin");
        Villes.add("fouchana");
        Villes.add("cite ghazela");
        Villes.add("raoued");
        Villes.add("daouar hicher");
        Villes.add("tborba");
        Villes.add("batan");
        Villes.add("beb saadoun");
        Villes.add("kabaria");
        Villes.add("yasminete");
        Villes.add("hamam lif");
        Villes.add("gammarth");
        Villes.add("sidi dhrif");
        Villes.add("ibn khaldoun");
        Villes.add("barcelone");
        Villes.add("tgm");


    }

    public Vector<String> calculItinéraire (String possibilities){
        String chaine = "";
        Vector v = new Vector();
        for (int i = 0 ; i< possibilities.length() ; i++){
            if (possibilities.charAt(i)!='-'){
                chaine = chaine + possibilities.charAt(i);
            }
            else{
                v.add(chaine);
                chaine = "";
            }
        }
        return v ;
    }

    public void Itineraire(LinearLayout linearLayout, String outputText){
        int indice = VillesIndex.get(outputText);
        int indice2 = 1 ;

        Graphs g = new Graphs(48);
        Vector<String> vector =  g.AllPossiblities(indice2,indice);

        final HorizontalScrollView scroll4 = new HorizontalScrollView(getApplicationContext());
        final LinearLayout linearLayout44 = new LinearLayout(getApplicationContext());
        Vector<String> LesPossibilte = new Vector<String>();

        for (int i = 0 ; i<vector.size() ; i++ ){
            String ch = "" ;
            Vector v = calculItinéraire(vector.elementAt(i));

            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(500, LinearLayout.LayoutParams.WRAP_CONTENT);

            LinearLayout linearLayout15 = new LinearLayout(getApplicationContext());
            linearLayout15.setOrientation(LinearLayout.VERTICAL);
            linearLayout15.setBackgroundColor(Color.parseColor("#FFFFFF"));
            linearLayout15.setLayoutParams(lp2);
            linearLayout15.setGravity(Gravity.CENTER);
            linearLayout15.setPadding(20, 20, 20, 10);

            String TransportPrecedent = "tt";
            for (int j = 0 ; j<v.size();j++){

                if (!TransportPrecedent.equals(v.elementAt(j).toString())){
                    ch = ch+v.elementAt(j).toString()+"-";
                    LinearLayout linearLayout5 = new LinearLayout(getApplicationContext());
                    linearLayout5.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout5.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    linearLayout5.setGravity(Gravity.CENTER);
                    linearLayout5.setPadding(20, 20, 20, 10);

                    final LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

                    ImageView imgv = new ImageView(getApplicationContext());
                    String tarif = "";
                    if (v.elementAt(j).toString().contains("Metro")) {
                        imgv.setImageResource(R.drawable.metroitin);
                        tarif ="470 millimes";
                    }
                    else if (v.elementAt(j).toString().contains("TGM")){
                        imgv.setImageResource(R.drawable.tgmitin);
                        tarif ="470 millimes";
                    }
                    else if (v.elementAt(j).toString().contains("Train")){
                        imgv.setImageResource(R.drawable.trainitin);
                        tarif ="470 millimes";
                    }
                    else {
                        imgv.setImageResource(R.drawable.busitin);
                        tarif ="470 millimes";
                    }
                    imgv.setLayoutParams(layoutParams2);

                    linearLayout5.addView(imgv);

                    TextView text = new TextView(getApplicationContext());
                    text.setText(v.elementAt(j).toString());
                    text.setGravity(Gravity.CENTER);
                    text.setTextColor(Color.BLACK);
                    text.setTextSize(18);
                    linearLayout5.addView(text);

                    TextView text2 = new TextView(getApplicationContext());
                    text2.setText(tarif);
                    text2.setGravity(Gravity.CENTER);
                    text2.setTextColor(Color.BLACK);
                    text2.setPadding(20,0,0,0);
                    text2.setTextSize(18);
                    linearLayout5.addView(text2);

                    if(linearLayout5.getParent() != null) {
                        ((ViewGroup)linearLayout5.getParent()).removeView(linearLayout5); // <- fix
                    }
                    linearLayout15.addView(linearLayout5);
                    if(linearLayout15.getParent() != null) {
                        ((ViewGroup)linearLayout15.getParent()).removeView(linearLayout15); // <- fix
                    }


                    TransportPrecedent = v.elementAt(j).toString();
                }
            }

            if (LesPossibilte.contains(ch)==false){
                linearLayout44.addView(linearLayout15);
                LesPossibilte.add(ch);
                ch = "";
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(10, ViewGroup.LayoutParams.WRAP_CONTENT);
                LinearLayout linearLayout77 = new LinearLayout(getApplicationContext());
                linearLayout77.setLayoutParams(lp);
                linearLayout44.addView(linearLayout77);
            }
            else{
                ch = "";
            }
        }
        scroll4.addView(linearLayout44);
        linearLayout.addView(scroll4);

    }


    public void nearestMetrotoStation(final String numMetro, final String nomstation, final LatLng arret, final LinearLayout linearLayout, final ScrollView scroll){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Metro");

        final DatabaseReference finalRef = ref;
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                float minDistance = 900000;
                LatLng Metro = null;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                    String metroNum = zoneSnapshot.child("num").getValue(String.class);
                    String sens = zoneSnapshot.child("sens").getValue(String.class);
                    if (numMetro.equals(metroNum) && sens.equals("aller")){
                        Double longi = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                        Double lati = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                        LatLng position = new LatLng(lati,longi);
                        Location loc1 = new Location("");
                        loc1.setLatitude(lati);
                        loc1.setLongitude(longi);
                        Location loc2 = new Location("");
                        loc2.setLatitude(arret.latitude);
                        loc2.setLongitude(arret.longitude);
                        final float distanceInMeters = loc1.distanceTo(loc2);

                        if (distanceInMeters < minDistance){
                            Metro = new LatLng(lati,longi);
                            minDistance = distanceInMeters;
                        }

                    }

                }

                if(Metro != null){
                    if (minDistance<300.0){
                        Toast.makeText(getApplicationContext(),"Metro déja en station",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Double speed = 8.33333;

                        final double estimatedTime = ((minDistance / speed))/60;

                        Calendar now = Calendar.getInstance();

                        final Calendar tmp = (Calendar) now.clone();
                        tmp.add(Calendar.MINUTE, (int) Math.round(estimatedTime+2));


                        String address = "";
                        try {
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            addresses = geocoder.getFromLocation(Metro.latitude, Metro.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        final String tempArrivage =  new DecimalFormat("##.##").format(Math.round(estimatedTime+2))+" minutes";

                        final TextView textView1 = new TextView(getApplicationContext());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(10, 10, 50, 10);
                        textView1.setLayoutParams(layoutParams);
                        textView1.setTextColor(Color.BLACK);
                        textView1.setBackgroundResource(R.drawable.bottext1);
                        //textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                        textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        textView1.setTypeface(null, Typeface.NORMAL);
                        linearLayout.addView(textView1);
                        if(address.equals("")){
                            textView1.setText("le prochain metro "+numMetro+" arrive à "+nomstation+" en "+tempArrivage+" donc a peut prés à "+tmp.get(Calendar.HOUR)+":"+tmp.get(Calendar.MINUTE)+".");
                            repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                @SuppressLint("NewApi")
                                @Override
                                public void onInit(int i) {
                                    if (i == TextToSpeech.SUCCESS) {
                                        repeatTTS.setLanguage(Locale.FRENCH);
                                        repeatTTS.speak("le prochain metro "+numMetro+" arrive en "+nomstation+" donc a peut prés à "+tmp.get(Calendar.HOUR)+":"+tmp.get(Calendar.MINUTE), TextToSpeech.QUEUE_ADD, null);
                                    }
                                }
                            });
                        }
                        else{
                            textView1.setText("le prochain metro "+numMetro+" arrive à "+nomstation+" en "+tempArrivage+" donc a peut prés à "+tmp.get(Calendar.HOUR)+":"+tmp.get(Calendar.MINUTE)+" le metro se trouve maintenat à "+address);
                            final String finalAddress = address;
                            repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                @SuppressLint("NewApi")
                                @Override
                                public void onInit(int i) {
                                    if (i == TextToSpeech.SUCCESS) {
                                        repeatTTS.setLanguage(Locale.FRENCH);
                                        repeatTTS.speak("le prochain metro "+numMetro+" arrive en "+tempArrivage+" donc a peut prés à "+tmp.get(Calendar.HOUR)+":"+tmp.get(Calendar.MINUTE)+" le metro se trouve maintenat à "+ finalAddress, TextToSpeech.QUEUE_ADD, null);
                                    }
                                }
                            });
                        }


                        scroll.post(new Runnable() {
                            @Override
                            public void run() {
                                scroll.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });

                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void nearestArret(final LinearLayout linearLayout, final ScrollView scroll, final LatLng latLng2, final String numMetro){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("metro"+numMetro);

        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        final LinearLayout linearLayout11 = new LinearLayout(getApplicationContext());
        linearLayout11.setOrientation(LinearLayout.HORIZONTAL);
        final HorizontalScrollView scroll21 = new HorizontalScrollView(getApplicationContext());

        final DatabaseReference finalRef = ref;
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                float minDistance = 900000;

                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {


                    final Double longi = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                    final Double lati = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));

                    final Button myButton22 = new Button(getApplicationContext());
                    myButton22.setText(zoneSnapshot.child("nom").getValue(String.class));
                    myButton22.setBackgroundResource(R.drawable.roundbtn);

                    myButton22.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                    myButton22.setGravity(Gravity.CENTER);
                    myButton22.setTextColor(Color.parseColor("#ffffff"));
                    myButton22.setLayoutParams(layoutParams);
                    linearLayout11.addView(myButton22);

                    myButton22.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(50, 10, 10, 10);
                            TextView textView1 = new TextView(getApplicationContext());
                            textView1.setLayoutParams(layoutParams);
                            textView1.setText(myButton22.getText().toString());
                            textView1.setTextColor(Color.WHITE);
                            layoutParams.gravity = Gravity.RIGHT;
                            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);


                            if (theme.equals("red")){
                                if (couleur==0){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.redtext);
                                }
                                else if (couleur==1){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.redtext2);
                                }
                                else if (couleur==2){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.redtext3);
                                }
                                else if (couleur == 3){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.redtext4);
                                }
                                else if (couleur == 4){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.redtext5);
                                }
                                else{
                                    couleur=0;
                                    textView1.setBackgroundResource(R.drawable.redtext6);
                                }

                            }
                            else if (theme.equals("bleu")){
                                if (couleur==0){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.mytext1);
                                }
                                else if (couleur==1){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.bluetext2);
                                }
                                else if (couleur==2){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.bluetext3);
                                }
                                else if (couleur == 3){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.bluetext4);
                                }
                                else if (couleur == 4){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.bluetext5);
                                }
                                else{
                                    couleur=0;
                                    textView1.setBackgroundResource(R.drawable.bluetext6);
                                }
                            }
                            else if (theme.equals("violet")){
                                if (couleur==0){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.purpletext);
                                }
                                else if (couleur==1){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.purpletext2);
                                }
                                else if (couleur==2){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.purpletext3);
                                }
                                else if (couleur == 3){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.purpletext4);
                                }
                                else if (couleur == 4){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.purpletext5);
                                }
                                else{
                                    couleur=0;
                                    textView1.setBackgroundResource(R.drawable.purpletext6);
                                }
                            }
                            else{
                                if (couleur==0){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.pinktext);
                                }
                                else if (couleur==1){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.pinktext2);
                                }
                                else if (couleur==2){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.pinktext3);
                                }
                                else if (couleur == 3){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.pinktext4);
                                }
                                else if (couleur == 4){
                                    couleur++;
                                    textView1.setBackgroundResource(R.drawable.pinktext5);
                                }
                                else{
                                    couleur=0;
                                    textView1.setBackgroundResource(R.drawable.pinktext6);
                                }
                            }



                            textView1.setPadding(20, 20, 50, 20);// in pixels (left, top, right, bottom)
                            linearLayout.addView(textView1);

                            scroll.post(new Runnable() {
                                @Override
                                public void run() {
                                    scroll.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });

                            nearestMetrotoStation(numMetro,myButton22.getText().toString(),new LatLng(lati,longi),linearLayout,scroll);
                        }
                    });



                    Location loc1 = new Location("");
                    loc1.setLatitude(lati);
                    loc1.setLongitude(longi);
                    Location loc2 = new Location("");
                    longitudeString = getIntent().getStringExtra("longitude");
                    latitudeString = getIntent().getStringExtra("latitude");
                    loc2.setLatitude(latLng2.latitude);
                    loc2.setLongitude(latLng2.longitude);
                    final float distanceInMeters = loc1.distanceTo(loc2);
                    if (distanceInMeters<minDistance){
                        minDistance = distanceInMeters;
                        stationName = zoneSnapshot.child("nom").getValue(String.class);
                        latLng = new LatLng(lati,longi);
                    }

                }
                if (minDistance>1000){


                    final TextView textView1 = new TextView(getApplicationContext());

                    layoutParams.setMargins(10, 10, 50, 10);
                    textView1.setLayoutParams(layoutParams);
                    textView1.setTextColor(Color.BLACK);
                    textView1.setBackgroundResource(R.drawable.bottext1);
                    //textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                    textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    textView1.setTypeface(null, Typeface.NORMAL);
                    linearLayout.addView(textView1);
                    textView1.setText("No station of metro  "+ numMetro+" is near you, At wich station you want to know?");
                    scroll21.addView(linearLayout11);
                    linearLayout.addView(scroll21);


                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onInit(int i) {
                            if (i == TextToSpeech.SUCCESS) {
                                repeatTTS.setLanguage(Locale.ENGLISH);
                                repeatTTS.speak("No stations are near you, at wich station you want to know ?", TextToSpeech.QUEUE_ADD, null);
                            }
                        }
                    });
                    scroll.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
                else if (latLng != null){




                    final Vector<Float> Distances = new Vector<Float>();

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("Metro");

                    final DatabaseReference finalRef = ref;
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            float minDistance = 900000;
                            LatLng Metro = null;
                            for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                String metroNum = zoneSnapshot.child("num").getValue(String.class);
                                String sens = zoneSnapshot.child("sens").getValue(String.class);
                                if (numMetro.equals(metroNum) && sens.equals("aller")){
                                    Double longi = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                    Double lati = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                                    LatLng position = new LatLng(lati,longi);
                                    Location loc1 = new Location("");
                                    loc1.setLatitude(lati);
                                    loc1.setLongitude(longi);
                                    Location loc2 = new Location("");
                                    loc2.setLatitude(latLng.latitude);
                                    loc2.setLongitude(latLng.longitude);
                                    final float distanceInMeters = loc1.distanceTo(loc2);

                                    if (distanceInMeters < minDistance){
                                        Metro = new LatLng(lati,longi);
                                        minDistance = distanceInMeters;
                                    }

                                }

                            }

                            if(Metro != null){
                                if (minDistance<300.0){
                                    final TextView textView1 = new TextView(getApplicationContext());
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                                    layoutParams.setMargins(10, 10, 50, 10);
                                    textView1.setLayoutParams(layoutParams);
                                    textView1.setTextColor(Color.BLACK);
                                    textView1.setBackgroundResource(R.drawable.bottext1);
                                    //textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                                    textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
                                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                    textView1.setTypeface(null, Typeface.NORMAL);
                                    linearLayout.addView(textView1);
                                    textView1.setText("There's already a metro in station "+stationName);
                                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                        @SuppressLint("NewApi")
                                        @Override
                                        public void onInit(int i) {
                                            if (i == TextToSpeech.SUCCESS) {
                                                repeatTTS.setLanguage(Locale.ENGLISH);
                                                repeatTTS.speak("There's already a metro in station "+stationName, TextToSpeech.QUEUE_ADD, null);
                                            }
                                        }
                                    });
                                    scroll.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
                                        }
                                    });
                                }
                                else{
                                    Double speed = 6.4;

                                    final double estimatedTime = ((minDistance / speed))/60;

                                    Calendar now = Calendar.getInstance();

                                    final Calendar tmp = (Calendar) now.clone();
                                    int tempAjoute = Math.round((minDistance/2)/60);
                                    //Toast.makeText(getApplicationContext(),tempAjoute+" ",Toast.LENGTH_SHORT).show();
                                    tmp.add(Calendar.MINUTE, (int) Math.round(estimatedTime+5));


                                    String address = "";
                                    try {
                                        Geocoder geocoder;
                                        List<Address> addresses;
                                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                        addresses = geocoder.getFromLocation(Metro.latitude, Metro.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    final String tempArrivage =  new DecimalFormat("##.##").format(Math.round(estimatedTime+2))+" minutes";

                                    final TextView textView1 = new TextView(getApplicationContext());
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                                    layoutParams.setMargins(10, 10, 50, 10);
                                    textView1.setLayoutParams(layoutParams);
                                    textView1.setTextColor(Color.BLACK);
                                    textView1.setBackgroundResource(R.drawable.bottext1);
                                    //textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                                    textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
                                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                    textView1.setTypeface(null, Typeface.NORMAL);
                                    linearLayout.addView(textView1);
                                    if(address.equals("")){
                                        textView1.setText("The next metro "+numMetro+" comes in "+stationName+" in "+tempArrivage+" so maybe at "+tmp.get(Calendar.HOUR)+":"+tmp.get(Calendar.MINUTE)+".");
                                        repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                            @SuppressLint("NewApi")
                                            @Override
                                            public void onInit(int i) {
                                                if (i == TextToSpeech.SUCCESS) {
                                                    repeatTTS.setLanguage(Locale.ENGLISH);
                                                    repeatTTS.speak("The next metro "+numMetro+" comes in "+tempArrivage+" so maybe at "+tmp.get(Calendar.HOUR)+":"+tmp.get(Calendar.MINUTE), TextToSpeech.QUEUE_ADD, null);
                                                }
                                            }
                                        });
                                    }
                                    else{
                                        textView1.setText("The next metro "+numMetro+" comes to "+stationName+" in "+tempArrivage+" so maybe  "+tmp.get(Calendar.HOUR)+":"+tmp.get(Calendar.MINUTE)+" now the metro is in "+address);
                                        final String finalAddress = address;
                                        repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                            @SuppressLint("NewApi")
                                            @Override
                                            public void onInit(int i) {
                                                if (i == TextToSpeech.SUCCESS) {
                                                    repeatTTS.setLanguage(Locale.ENGLISH);
                                                    repeatTTS.speak("The next metro "+numMetro+" comes to "+stationName+" in "+tempArrivage+" so maybe  "+tmp.get(Calendar.HOUR)+":"+tmp.get(Calendar.MINUTE)+" now the metro is in "+finalAddress, TextToSpeech.QUEUE_ADD, null);
                                                }
                                            }
                                        });
                                    }


                                    scroll.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
                                        }
                                    });

                                }
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

                else{
                    final TextView textView1 = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10, 10, 50, 10);
                    textView1.setLayoutParams(layoutParams);
                    textView1.setTextColor(Color.BLACK);
                    textView1.setBackgroundResource(R.drawable.bottext1);
                    //textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                    textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    textView1.setTypeface(null, Typeface.NORMAL);
                    linearLayout.addView(textView1);
                    textView1.setText("Soryy i couldn't locate the metro.");
                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onInit(int i) {
                            if (i == TextToSpeech.SUCCESS) {
                                repeatTTS.setLanguage(Locale.ENGLISH);
                                repeatTTS.speak("Soryy i couldn't locate the metro.", TextToSpeech.QUEUE_ADD, null);
                            }
                        }
                    });
                    scroll.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void spect(final LinearLayout linearLayout, final ScrollView scroll) {



        final String str3 = "";

        final HorizontalScrollView scroll4 = new HorizontalScrollView(getApplicationContext());

        final LinearLayout linearLayout44 = new LinearLayout(getApplicationContext());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mostafa = ref.child("events");


        mostafa.addValueEventListener(new ValueEventListener() {
            @SuppressLint("Range")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    ch5 = " " + ch5 + " " + zoneSnapshot.child("nom").getValue(String.class);

                    ch5 = ch5 + " à " + zoneSnapshot.child("date").getValue(String.class) + "\n";
                    url = zoneSnapshot.child("url").getValue(String.class);
                    nb2++;

                    dt = zoneSnapshot.child("date").getValue(String.class);


                    final LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    ImageView imgv = new ImageView(getApplicationContext());
                    imgv.setLayoutParams(layoutParams2);

                    Picasso.with(getApplicationContext())
                            .load(url)
                            .resize(400, 600)
                            .into(imgv);
                    LinearLayout linearLayout5 = new LinearLayout(getApplicationContext());
                    linearLayout5.setOrientation(LinearLayout.VERTICAL);
                    linearLayout5.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    linearLayout5.addView(imgv);
                    TextView text = new TextView(getApplicationContext());
                    text.setText(zoneSnapshot.child("nom").getValue(String.class) + " au " + zoneSnapshot.child("location").getValue(String.class)+" à "+zoneSnapshot.child("horaire").getValue(String.class)+"h");
                    text.setTextColor(Color.BLACK);
                    linearLayout5.setPadding(10, 10, 10, 10);
                    text.setGravity(Gravity.CENTER);
                    text.setTextSize(18);


                    Button btn1 = new Button(getApplicationContext());
                    btn1.setText("Reserver");
                    btn1.setTextColor(Color.WHITE);

                    if (theme.equals("rouge")){
                        //btn2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.redroundbtn));
                        btn1.setBackgroundResource(R.drawable.redroundbtn);
                    }
                    else if (theme.equals("bleu")){
                        //btn2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blueroundbtn));
                        btn1.setBackgroundResource(R.drawable.blueroundbtn);
                    }
                    else if (theme.equals("violet")){
                        //btn2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purpleroundbtn));
                        btn1.setBackgroundResource(R.drawable.purpleroundbtn);
                    }
                    else{
                        //btn2.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pinkroundbtn) );
                        btn1.setBackgroundResource(R.drawable.pinkroundbtn);
                    }
                    linearLayout5.addView(btn1);

                    LinearLayout linearLayout77 = new LinearLayout(getApplicationContext());


                    linearLayout44.addView(linearLayout5);
                    linearLayout44.addView(linearLayout77);

                    btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String url = "https://tunisiebillet.com";

                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    });


                }


                scroll4.addView(linearLayout44);
                linearLayout.addView(scroll4);



                TextView textView1 = new TextView(getApplicationContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 10, 10, 10);

                textView1.setLayoutParams(layoutParams);

                textView1.setText(outputText + ch5);
                textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                textView1.setTextColor(Color.WHITE);
                textView1.setBackgroundResource(R.drawable.bottext1);
                //textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                textView1.setPadding(20, 20, 50, 20);// in pixels (left, top, right, bottom)

                // linearLayout.addView(textView1);
                scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                ch5 = "";
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void nearestKiosque(final LinearLayout linearLayout, final ScrollView scroll){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("kiosques");

        final DatabaseReference finalRef = ref;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                float minDistance = 900000;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                    Double longi = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                    Double lati = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));

                    Location loc1 = new Location("");
                    loc1.setLatitude(lati);
                    loc1.setLongitude(longi);
                    Location loc2 = new Location("");
                    /*longitudeString = getIntent().getStringExtra("longitude");
                    latitudeString = getIntent().getStringExtra("latitude");*/
                    loc2.setLatitude(Double.parseDouble(latitudeString));
                    loc2.setLongitude(Double.parseDouble(longitudeString));
                    final float distanceInMeters = loc1.distanceTo(loc2);
                    if (distanceInMeters<minDistance){
                        minDistance = distanceInMeters;
                        latLng = new LatLng(lati,longi);
                    }

                }
                if (latLng != null){

                    String dist = "";
                    if (minDistance<1000){
                        dist = new DecimalFormat("##.##").format(minDistance)+" metres";
                    }
                    else {
                        dist = new DecimalFormat("##.##").format(minDistance/1000)+" kilometre";
                    }

                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    final TextView textView1 = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10, 10, 50, 10);
                    textView1.setLayoutParams(layoutParams);
                    textView1.setTextColor(Color.BLACK);
                    textView1.setBackgroundResource(R.drawable.bottext1);
                    //textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                    textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    textView1.setTypeface(null, Typeface.NORMAL);
                    linearLayout.addView(textView1);
                    if (address.equals("")){
                        textView1.setText("the nearest gas station more or less "+ dist +" far.Do you want to see it in map ?");
                    }
                    else{
                        textView1.setText("The nearest gas station is located in "+ address +", more or less "+ dist +" far.Do you want to see it in map ?");
                    }
                    final String finalAddress = address;
                    final String finalDist = dist;
                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onInit(int i) {
                            if (i == TextToSpeech.SUCCESS) {
                                repeatTTS.setLanguage(Locale.ENGLISH);
                                if (finalAddress.equals("")){
                                    repeatTTS.speak("the nearest gas station more or less "+ finalDist +" far.Do you want to see it in map ?", TextToSpeech.QUEUE_ADD, null);
                                }
                                else{
                                    repeatTTS.speak("The nearest gas station is located in "+ finalAddress +", more or less "+ finalDist +" far.Do you want to see it in map ?", TextToSpeech.QUEUE_ADD, null);
                                }

                            }
                        }
                    });
                    scroll.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
                else{
                    final TextView textView1 = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10, 10, 50, 10);
                    textView1.setLayoutParams(layoutParams);
                    textView1.setTextColor(Color.BLACK);
                    textView1.setBackgroundResource(R.drawable.bottext1);
                    //textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                    textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    textView1.setTypeface(null, Typeface.NORMAL);
                    linearLayout.addView(textView1);
                    textView1.setText("Sorry but i cannot get you location, i cannot know the nearest gas station");
                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onInit(int i) {
                            if (i == TextToSpeech.SUCCESS) {
                                repeatTTS.setLanguage(Locale.ENGLISH);
                                repeatTTS.speak("Sorry but i cannot get you location, i cannot know the nearest gas station.", TextToSpeech.QUEUE_ADD, null);
                            }
                        }
                    });
                    scroll.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void nearestParking(final LinearLayout linearLayout, final ScrollView scroll){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Parkings");

        final DatabaseReference finalRef = ref;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                float minDistance = 900000;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                    Double longi = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                    Double lati = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));

                    Location loc1 = new Location("");
                    loc1.setLatitude(lati);
                    loc1.setLongitude(longi);
                    Location loc2 = new Location("");
                    /*longitudeString = getIntent().getStringExtra("longitude");
                    latitudeString = getIntent().getStringExtra("latitude");*/
                    loc2.setLatitude(Double.parseDouble(latitudeString));
                    loc2.setLongitude(Double.parseDouble(longitudeString));
                    final float distanceInMeters = loc1.distanceTo(loc2);
                    if (distanceInMeters<minDistance){
                        minDistance = distanceInMeters;
                        latLng = new LatLng(lati,longi);
                    }

                }
                if (latLng != null){

                    String dist = "";
                    if (minDistance<1000){
                        dist = new DecimalFormat("##.##").format(minDistance)+" metres";
                    }
                    else {
                        dist = new DecimalFormat("##.##").format(minDistance/1000)+" kilometre";
                    }

                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    final TextView textView1 = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10, 10, 50, 10);
                    textView1.setLayoutParams(layoutParams);
                    textView1.setTextColor(Color.BLACK);
                    textView1.setBackgroundResource(R.drawable.bottext1);
                    //textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                    textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    textView1.setTypeface(null, Typeface.NORMAL);
                    linearLayout.addView(textView1);
                    if (address.equals("")){
                        textView1.setText("The nearest car park more or less "+ dist +" far. Do you want me to show it to you ?");
                    }
                    else{
                        textView1.setText("There's a parking lot in "+ address +", and it's "+ dist +" far away .Do you want to see it in map ?");
                    }
                    final String finalAddress = address;
                    final String finalDist = dist;
                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onInit(int i) {
                            if (i == TextToSpeech.SUCCESS) {
                                repeatTTS.setLanguage(Locale.ENGLISH);
                                if (finalAddress.equals("")){
                                    repeatTTS.speak("The nearest car park more or less "+ finalDist +" far. Do you want me to show it to you ?", TextToSpeech.QUEUE_ADD, null);
                                }
                                else{
                                    repeatTTS.speak("There's a parking lot in "+ finalAddress +", and it's "+ finalDist +" far away .Do you want to see it in map ?", TextToSpeech.QUEUE_ADD, null);
                                }

                            }
                        }
                    });
                    scroll.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
                else{
                    final TextView textView1 = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10, 10, 50, 10);
                    textView1.setLayoutParams(layoutParams);
                    textView1.setTextColor(Color.BLACK);
                    textView1.setBackgroundResource(R.drawable.bottext1);
                    //textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                    textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    textView1.setTypeface(null, Typeface.NORMAL);
                    linearLayout.addView(textView1);
                    textView1.setText("Sorry i cannot get the nearest parking, i don't know your position");
                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onInit(int i) {
                            if (i == TextToSpeech.SUCCESS) {
                                repeatTTS.setLanguage(Locale.ENGLISH);
                                repeatTTS.speak("Sorry i cannot get the nearest parking, i don't know your position", TextToSpeech.QUEUE_ADD, null);
                            }
                        }
                    });
                    scroll.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void Chat(final String str, final LinearLayout linearLayout, final ScrollView scroll, final ConversationService myConversationService, final String name) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(50, 10, 10, 10);
        TextView textView1 = new TextView(getApplicationContext());
        textView1.setLayoutParams(layoutParams);
        textView1.setText(str);
        textView1.setTextColor(Color.WHITE);
        layoutParams.gravity = Gravity.RIGHT;
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);


        if (theme.equals("red")){
            if (couleur==0){
                couleur++;
                textView1.setBackgroundResource(R.drawable.redtext);
            }
            else if (couleur==1){
                couleur++;
                textView1.setBackgroundResource(R.drawable.redtext2);
            }
            else if (couleur==2){
                couleur++;
                textView1.setBackgroundResource(R.drawable.redtext3);
            }
            else if (couleur == 3){
                couleur++;
                textView1.setBackgroundResource(R.drawable.redtext4);
            }
            else if (couleur == 4){
                couleur++;
                textView1.setBackgroundResource(R.drawable.redtext5);
            }
            else{
                couleur=0;
                textView1.setBackgroundResource(R.drawable.redtext6);
            }

        }
        else if (theme.equals("bleu")){
            if (couleur==0){
                couleur++;
                textView1.setBackgroundResource(R.drawable.mytext1);
            }
            else if (couleur==1){
                couleur++;
                textView1.setBackgroundResource(R.drawable.bluetext2);
            }
            else if (couleur==2){
                couleur++;
                textView1.setBackgroundResource(R.drawable.bluetext3);
            }
            else if (couleur == 3){
                couleur++;
                textView1.setBackgroundResource(R.drawable.bluetext4);
            }
            else if (couleur == 4){
                couleur++;
                textView1.setBackgroundResource(R.drawable.bluetext5);
            }
            else{
                couleur=0;
                textView1.setBackgroundResource(R.drawable.bluetext6);
            }
        }
        else if (theme.equals("violet")){
            if (couleur==0){
                couleur++;
                textView1.setBackgroundResource(R.drawable.purpletext);
            }
            else if (couleur==1){
                couleur++;
                textView1.setBackgroundResource(R.drawable.purpletext2);
            }
            else if (couleur==2){
                couleur++;
                textView1.setBackgroundResource(R.drawable.purpletext3);
            }
            else if (couleur == 3){
                couleur++;
                textView1.setBackgroundResource(R.drawable.purpletext4);
            }
            else if (couleur == 4){
                couleur++;
                textView1.setBackgroundResource(R.drawable.purpletext5);
            }
            else{
                couleur=0;
                textView1.setBackgroundResource(R.drawable.purpletext6);
            }
        }
        else{
            if (couleur==0){
                couleur++;
                textView1.setBackgroundResource(R.drawable.pinktext);
            }
            else if (couleur==1){
                couleur++;
                textView1.setBackgroundResource(R.drawable.pinktext2);
            }
            else if (couleur==2){
                couleur++;
                textView1.setBackgroundResource(R.drawable.pinktext3);
            }
            else if (couleur == 3){
                couleur++;
                textView1.setBackgroundResource(R.drawable.pinktext4);
            }
            else if (couleur == 4){
                couleur++;
                textView1.setBackgroundResource(R.drawable.pinktext5);
            }
            else{
                couleur=0;
                textView1.setBackgroundResource(R.drawable.pinktext6);
            }
        }



        textView1.setPadding(20, 20, 50, 20);// in pixels (left, top, right, bottom)
        linearLayout.addView(textView1);

        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        MessageRequest request = new MessageRequest.Builder()
                .inputText(str)
                .build();
        myConversationService
                .message(getString(R.string.workspace4), request)
                .enqueue(new ServiceCallback<MessageResponse>() {
                    @Override
                    public void onResponse(MessageResponse response) {
                        outputText = response.getText().get(0);

                        if (outputText.equals("Quel map vous voulez ? des stations métro ? des parking? des kiosques ?") ||
                                outputText.equals("Quel type de map vous voulez ?") ||
                                outputText.equals("Map des stations métro ? map des stations bus ? map générale ?")) {
                            PreviousAnswer = "Map";
                        }
                        if (outputText.equals("quel bus exactement vous voulez ?") || outputText.equals("à votre maison ou au travail ?")) {
                            PreviousAnswer = "Bus";
                        }
                        if (outputText.equals("Metro 2 of the passage to Ariana it also passes by Cité Khadhra and the Olympic city, do you want me to show you its stations?")){
                            PreviousAnswer = "2";
                        }
                        if (outputText.equals("Metro 1 from barcelone to ben arous, you want see it's stations ?")
                                ||
                                outputText.equals("So, Metro 1 from barcelone to Ben arous, i can show you it's stations")
                                )
                        {
                            PreviousAnswer = "1";
                        }
                        if (outputText.equals("Metro 3 from barcelona to ibn khaldoun, you want to see its resorts?")){
                            PreviousAnswer = "3";
                        }
                        if (outputText.equals("Metro 4 his trip is from Barcelona to manouba, I show you his stop?")){
                            PreviousAnswer = "4";
                        }
                        if (outputText.equals("Metro 5 of Etadhamen city also passes the campus Manar and Beb Saadoun, see its stations?")){
                            PreviousAnswer = "5";
                        }
                        if (outputText.equals("metro du mourouj, terminus mourouj 4, I know his stations I can posters for you?")){
                            PreviousAnswer = "6";
                        }

                        runOnUiThread(new Runnable() {
                            @SuppressLint("Range")
                            @Override
                            public void run() {
                                String userMessage = str ;
                                String str = "";
                                String spect = "";
                                final String ch3 = "";
                                final Vector e = new Vector();
                                String ch1 = "";
                                String ch2 = "";
                                int x;
                                final TextView textView1 = new TextView(getApplicationContext());
                                final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(10, 10, 50, 10);
                                textView1.setLayoutParams(layoutParams);
                                textView1.setTextColor(Color.BLACK);
                                textView1.setBackgroundResource(R.drawable.bottext1);
                                //textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                                textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                textView1.setTypeface(null, Typeface.NORMAL);
                                if ((PreviousAnswer.equals("Map") && outputText.equals("Map Metro")) || outputText.equals("Map Metro")) {
                                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                        @Override
                                        public void onInit(int i) {
                                            if (i == TextToSpeech.SUCCESS) {
                                                repeatTTS.setLanguage(Locale.ENGLISH);
                                                repeatTTS.speak("donne moi le numéro du métro que vous voulez", TextToSpeech.QUEUE_ADD, null);
                                            }
                                        }
                                    });
                                    startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("type", "lignemetro"));
                                } else if (PreviousAnswer.equals("Map") && outputText.equals("Station bus") || outputText.equals("Station bus")) {
                                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                        @Override
                                        public void onInit(int i) {
                                            if (i == TextToSpeech.SUCCESS) {
                                                repeatTTS.setLanguage(Locale.ENGLISH);
                                                repeatTTS.speak("Give me the bus number you want", TextToSpeech.QUEUE_ADD, null);
                                            }
                                        }
                                    });
                                    startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("type","lignebus").putExtra("email",email).putExtra("longitude",longitudeString).putExtra("latitude",latitudeString));
                                } else if (outputText.equals("Terminus du métro 2 est Ariana voici la map exacte")) {
                                    /*LatLng station = getStationPosition("Ariana");
                                    Double myLat = Double.parseDouble(latitudeString);
                                    Double myLong = Double.parseDouble(longitudeString);

                                    float[] results = new float[1];
                                    Location.distanceBetween(myLat, myLong, station.latitude,station.longitude, results);*/


                                   /* final String speech = "Distance entre votre position et terminus métro 2 est: "+String.valueOf(new DecimalFormat("#0.00").format(results[0]*0.001))+" Kilomètre";
                                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

                                        @Override
                                        public void onInit(int i) {
                                            if (i == TextToSpeech.SUCCESS) {
                                                repeatTTS.setLanguage(Locale.ENGLISH);
                                                repeatTTS.speak(speech ,TextToSpeech.QUEUE_ADD, null);
                                            }
                                        }
                                    });*/
                                    startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("type", "lignemetro").putExtra("station","Ariana").putExtra("email",email).putExtra("longitude",longitudeString).putExtra("latitude",latitudeString));

                                }
                                else if ((PreviousAnswer.equals("2")||PreviousAnswer.equals("1")||PreviousAnswer.equals("3")||PreviousAnswer.equals("4")||PreviousAnswer.equals("5")||PreviousAnswer.equals("6"))&&outputText.equals("Okey, here is")){
                                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                        @Override
                                        public void onInit(int i) {
                                            if (i == TextToSpeech.SUCCESS) {
                                                repeatTTS.setLanguage(Locale.ENGLISH);
                                                repeatTTS.speak(outputText, TextToSpeech.QUEUE_ADD, null);
                                            }
                                        }
                                    });
                                    startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("type", "lignemetro").putExtra("metro",PreviousAnswer).putExtra("email",email).putExtra("longitude",longitudeString).putExtra("latitude",latitudeString));
                                }
                                if (outputText.equals("StationMetroNear")) {
                                    Vector<Double> position = new Vector<Double>();

                                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                        buildAlertMessageNoGps();

                                    } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                        position = getLocation();
                                    }

                                    for (int i = 1; i < 7; i++) {
                                        final String metro = String.valueOf(i);
                                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference ref = database.getReference().child("metro" + metro);

                                        final Vector<Double> finalPosition = position;
                                        ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                    String ch = zoneSnapshot.child("nom").getValue(String.class);
                                                    Double longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                    Double latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));

                                                    if (latitude <= finalPosition.elementAt(0) + diffLatitude && latitude >= finalPosition.elementAt(0) - diffLatitude) {
                                                        if (longitude <= finalPosition.elementAt(1) + diffLongitude && longitude >= finalPosition.elementAt(1) - diffLongitude) {
                                                            outputText = ch + " du métro " + metro;
                                                            final TextView textView1 = new TextView(getApplicationContext());
                                                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                                                            layoutParams.setMargins(10, 10, 50, 10);
                                                            textView1.setLayoutParams(layoutParams);
                                                            textView1.setTextColor(Color.BLACK);
                                                            textView1.setBackgroundResource(R.drawable.bottext1);
                                                            //textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                                                            textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
                                                            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                                            textView1.setTypeface(null, Typeface.NORMAL);
                                                            linearLayout.addView(textView1);
                                                            textView1.setText(outputText);
                                                            repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                                                @Override
                                                                public void onInit(int i) {
                                                                    if (i == TextToSpeech.SUCCESS) {
                                                                        repeatTTS.setLanguage(Locale.ENGLISH);
                                                                        repeatTTS.speak(outputText, TextToSpeech.QUEUE_ADD, null);
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                }
                                else if (outputText.equals("nearkiosque")){
                                    PreviousAnswer = "kiosque";
                                    nearestKiosque(linearLayout,scroll);
                                }
                                else if (outputText.equals("Okey, here is")&&PreviousAnswer.equals("kiosque")){
                                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                        @Override
                                        public void onInit(int i) {
                                            if (i == TextToSpeech.SUCCESS) {
                                                repeatTTS.setLanguage(Locale.ENGLISH);
                                                repeatTTS.speak(outputText, TextToSpeech.QUEUE_ADD, null);
                                            }
                                        }
                                    });
                                    startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("type", "nearkiosque").putExtra("email",email).putExtra("longitude",longitudeString).putExtra("latitude",latitudeString));
                                }
                                else if (outputText.equals("nearparking")){
                                    PreviousAnswer ="parking";
                                    nearestParking(linearLayout,scroll);
                                }
                                else if (outputText.equals("Okey, here is")&&PreviousAnswer.equals("parking")){
                                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                        @Override
                                        public void onInit(int i) {
                                            if (i == TextToSpeech.SUCCESS) {
                                                repeatTTS.setLanguage(Locale.ENGLISH);
                                                repeatTTS.speak(outputText, TextToSpeech.QUEUE_ADD, null);
                                            }
                                        }
                                    });
                                    startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("type", "nearparking").putExtra("email",email).putExtra("longitude",longitudeString).putExtra("latitude",latitudeString));
                                }
                                else if (outputText.equals("temp estimation")){

                                    LatLng latLng2 = new LatLng(36.8447231,10.1836221);
                                    nearestArret(linearLayout,scroll,latLng2,"2");

                                }
                                else if (outputText.equals("Temp estime metro 1")){

                                    LatLng latLng2 = new LatLng(36.8447231,10.1836221);
                                    nearestArret(linearLayout,scroll,latLng2,"1");

                                }
                                else if (outputText.equals("Temp estime metro 3")){

                                    LatLng latLng2 = new LatLng(36.8447231,10.1836221);
                                    nearestArret(linearLayout,scroll,latLng2,"3");

                                }
                                else if (outputText.equals("Temp estime metro 4")){

                                    LatLng latLng2 = new LatLng(36.8447231,10.1836221);
                                    nearestArret(linearLayout,scroll,latLng2,"4");

                                }
                                else if (outputText.equals("Temp estime metro 5")){

                                    LatLng latLng2 = new LatLng(36.8447231,10.1836221);
                                    //nearestArret(linearLayout,scroll,latLng2,"5");

                                }
                                else if (outputText.equals("Temp estime metro 6")){

                                    LatLng latLng2 = new LatLng(36.8447231,10.1836221);
                                    nearestArret(linearLayout,scroll,latLng2,"6");

                                }
                                else if (outputText.equals("itinéraire")){
                                    String ch = "";
                                    if (str.contains("i want to go to")){
                                        ch = userMessage.substring(userMessage.indexOf("to")+2, userMessage.length());
                                    }
                                    else{
                                        //Toast.makeText(getApplicationContext(),userMessage,Toast.LENGTH_SHORT).show();
                                        ch = userMessage.substring(userMessage.indexOf("to")+2, userMessage.length());
                                    }


                                    MessageRequest request = new MessageRequest.Builder()
                                            .inputText(ch)
                                            .build();
                                    myConversationService
                                            .message(getString(R.string.workspace3), request)
                                            .enqueue(new ServiceCallback<MessageResponse>() {
                                                @Override
                                                public void onResponse(MessageResponse response) {
                                                    outputText = response.getText().get(0);
                                                    runOnUiThread(new Runnable() {
                                                        @SuppressLint("Range")
                                                        @Override
                                                        public void run() {
                                                            if (outputText.equals("Je n'ai pas compris.")){
                                                                linearLayout.addView(textView1);
                                                                textView1.setText("i didn't understant the city you want to go to, here a list of cities may help you");
                                                                repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                                                    @Override
                                                                    public void onInit(int i) {
                                                                        if (i == TextToSpeech.SUCCESS) {
                                                                            repeatTTS.setLanguage(Locale.ENGLISH);
                                                                            repeatTTS.speak("i didn't understant the city you want to go to, here a list of cities may help you", TextToSpeech.QUEUE_ADD, null);
                                                                        }
                                                                    }
                                                                });
                                                                final LinearLayout linearLayout11 = new LinearLayout(getApplicationContext());
                                                                linearLayout11.setOrientation(LinearLayout.HORIZONTAL);
                                                                final HorizontalScrollView scroll21 = new HorizontalScrollView(getApplicationContext());
                                                                for (int i=0;i<Villes.size();i++){
                                                                    final Button myButton22 = new Button(getApplicationContext());
                                                                    myButton22.setText(Villes.elementAt(i));
                                                                    if (theme.equals("rouge")){
                                                                        //btn2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.redroundbtn));
                                                                        myButton22.setBackgroundResource(R.drawable.redroundbtn);
                                                                    }
                                                                    else if (theme.equals("bleu")){
                                                                        //btn2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blueroundbtn));
                                                                        myButton22.setBackgroundResource(R.drawable.blueroundbtn);
                                                                    }
                                                                    else if (theme.equals("violet")){
                                                                        //btn2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purpleroundbtn));
                                                                        myButton22.setBackgroundResource(R.drawable.purpleroundbtn);
                                                                    }
                                                                    else{
                                                                        //btn2.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pinkroundbtn) );
                                                                        myButton22.setBackgroundResource(R.drawable.pinkroundbtn);
                                                                    }
                                                                    myButton22.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                                                                    myButton22.setGravity(Gravity.CENTER);
                                                                    myButton22.setTextColor(Color.parseColor("#ffffff"));
                                                                    myButton22.setLayoutParams(layoutParams);
                                                                    linearLayout11.addView(myButton22);
                                                                    myButton22.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View view) {


                                                                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                                                                            layoutParams.setMargins(50, 10, 10, 10);
                                                                            TextView textView1 = new TextView(getApplicationContext());
                                                                            textView1.setLayoutParams(layoutParams);
                                                                            textView1.setText(myButton22.getText().toString());
                                                                            textView1.setTextColor(Color.WHITE);
                                                                            layoutParams.gravity = Gravity.RIGHT;
                                                                            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);


                                                                            if (theme.equals("red")){
                                                                                if (couleur==0){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.redtext);
                                                                                }
                                                                                else if (couleur==1){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.redtext2);
                                                                                }
                                                                                else if (couleur==2){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.redtext3);
                                                                                }
                                                                                else if (couleur == 3){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.redtext4);
                                                                                }
                                                                                else if (couleur == 4){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.redtext5);
                                                                                }
                                                                                else{
                                                                                    couleur=0;
                                                                                    textView1.setBackgroundResource(R.drawable.redtext6);
                                                                                }

                                                                            }
                                                                            else if (theme.equals("bleu")){
                                                                                if (couleur==0){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.mytext1);
                                                                                }
                                                                                else if (couleur==1){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.bluetext2);
                                                                                }
                                                                                else if (couleur==2){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.bluetext3);
                                                                                }
                                                                                else if (couleur == 3){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.bluetext4);
                                                                                }
                                                                                else if (couleur == 4){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.bluetext5);
                                                                                }
                                                                                else{
                                                                                    couleur=0;
                                                                                    textView1.setBackgroundResource(R.drawable.bluetext6);
                                                                                }
                                                                            }
                                                                            else if (theme.equals("violet")){
                                                                                if (couleur==0){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.purpletext);
                                                                                }
                                                                                else if (couleur==1){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.purpletext2);
                                                                                }
                                                                                else if (couleur==2){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.purpletext3);
                                                                                }
                                                                                else if (couleur == 3){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.purpletext4);
                                                                                }
                                                                                else if (couleur == 4){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.purpletext5);
                                                                                }
                                                                                else{
                                                                                    couleur=0;
                                                                                    textView1.setBackgroundResource(R.drawable.purpletext6);
                                                                                }
                                                                            }
                                                                            else{
                                                                                if (couleur==0){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.pinktext);
                                                                                }
                                                                                else if (couleur==1){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.pinktext2);
                                                                                }
                                                                                else if (couleur==2){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.pinktext3);
                                                                                }
                                                                                else if (couleur == 3){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.pinktext4);
                                                                                }
                                                                                else if (couleur == 4){
                                                                                    couleur++;
                                                                                    textView1.setBackgroundResource(R.drawable.pinktext5);
                                                                                }
                                                                                else{
                                                                                    couleur=0;
                                                                                    textView1.setBackgroundResource(R.drawable.pinktext6);
                                                                                }
                                                                            }



                                                                            textView1.setPadding(20, 20, 50, 20);// in pixels (left, top, right, bottom)
                                                                            linearLayout.addView(textView1);

                                                                            scroll.post(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    scroll.fullScroll(ScrollView.FOCUS_DOWN);
                                                                                }
                                                                            });

                                                                        }
                                                                    });
                                                                }
                                                                scroll21.addView(linearLayout11);
                                                                linearLayout.addView(scroll21);
                                                            }
                                                            else{
                                                                linearLayout.addView(textView1);
                                                                textView1.setText("Here some possible itinerates you can follow to go to "+outputText);
                                                                repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                                                    @Override
                                                                    public void onInit(int i) {
                                                                        if (i == TextToSpeech.SUCCESS) {
                                                                            repeatTTS.setLanguage(Locale.ENGLISH);
                                                                            repeatTTS.speak("Here some possible itinerates you can follow to go to "+outputText, TextToSpeech.QUEUE_ADD, null);
                                                                        }
                                                                    }
                                                                });
                                                                Itineraire(linearLayout,outputText);
                                                            }
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onFailure(Exception e) {
                                                }
                                            });
                                }
                                else if (outputText.equals("events")){
                                    linearLayout.addView(textView1);
                                    outputText = "Here the list of all the events near to you";
                                    textView1.setText(outputText);
                                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                        @Override
                                        public void onInit(int i) {
                                            if (i == TextToSpeech.SUCCESS) {
                                                repeatTTS.setLanguage(Locale.ENGLISH);
                                                repeatTTS.speak(outputText, TextToSpeech.QUEUE_ADD, null);
                                            }
                                        }
                                    });
                                    spect(linearLayout, scroll);
                                }
                                else {
                                    linearLayout.addView(textView1);
                                    textView1.setText(outputText);
                                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                        @Override
                                        public void onInit(int i) {
                                            if (i == TextToSpeech.SUCCESS) {
                                                repeatTTS.setLanguage(Locale.ENGLISH);
                                                repeatTTS.speak(outputText, TextToSpeech.QUEUE_ADD, null);
                                            }
                                        }
                                    });
                                }

                                scroll.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        scroll.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                });


                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {
                    }
                });


    }

    private Vector<Double> getLocation() {
        Vector<Double> position = new Vector<Double>();

        if (ActivityCompat.checkSelfPermission(EnglishChatbot.this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (EnglishChatbot.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(EnglishChatbot.this, new String[]{ACCESS_FINE_LOCATION}, 1);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();

                position.add(latti);
                position.add(longi);


            } else if (location1 != null) {

                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                position.add(latti);
                position.add(longi);


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                position.add(latti);
                position.add(longi);


            } else {

                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();

            }
        }
        return position;

    }


    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    final ConversationService myConversationService =
                            new ConversationService(
                                    "2018-02-16",
                                    getString(R.string.username),
                                    getString(R.string.password)
                            );
                    final ScrollView scroll = (ScrollView) findViewById(R.id.scroll1_english);

                    final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_example_english);


                    Chat(result.get(0), linearLayout, scroll, myConversationService, userName);

                }
                break;
            }

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_chatbot);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final ConversationService myConversationService =
                new ConversationService(
                        "2018-02-16",
                        getString(R.string.username),
                        getString(R.string.password)
                );

        Intialize();

        final String name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        /*longitudeString = getIntent().getStringExtra("longitude");
        latitudeString = getIntent().getStringExtra("latitude");*/
        longitudeString = "10.2132017";
        latitudeString = "36.8862531";

        userName = name ;

        final RelativeLayout header = (RelativeLayout) findViewById(R.id.chatheader_english);
        final ImageButton btn2 = (ImageButton) findViewById(R.id.button_chatbox_send_english);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("comptes");

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    String emailfromDB = zoneSnapshot.child("email").getValue(String.class);
                    if (emailfromDB.equals(email)) {
                        String langue = zoneSnapshot.child("lang").getValue(String.class);
                        theme = zoneSnapshot.child("theme").getValue(String.class);

                        if (theme.equals("rouge")){
                            final int sdk = android.os.Build.VERSION.SDK_INT;
                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                header.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.redbackground) );
                                btn2.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.redroundbtn) );

                            } else {
                                header.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.redbackground));
                                btn2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.redroundbtn));

                            }
                            break;
                        }
                        else if (theme.equals("bleu")){
                            final int sdk = android.os.Build.VERSION.SDK_INT;
                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                header.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.loginheader) );
                                btn2.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blueroundbtn) );

                            } else {
                                header.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.loginheader));
                                btn2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blueroundbtn));
                            }
                            break;
                        }
                        else if (theme.equals("violet")){
                            final int sdk = android.os.Build.VERSION.SDK_INT;
                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                header.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purplebackground) );
                                btn2.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purpleroundbtn) );
                            } else {
                                header.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purplebackground));
                                btn2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.purpleroundbtn));
                            }
                            break;
                        }
                        else{
                            final int sdk = android.os.Build.VERSION.SDK_INT;
                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                header.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pinkbackground) );
                                btn2.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pinkroundbtn) );
                            } else {
                                header.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pinkbackground));
                                btn2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pinkroundbtn));
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





        String message = "Hello again "+name+", What can i help you with ?";


        final ScrollView scroll = (ScrollView) findViewById(R.id.scroll1_english);
        final EditText edt = (EditText) findViewById(R.id.edittext_chatbox_english);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_example_english);



        TextView textView1 = new TextView(getApplicationContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 50, 10);
        textView1.setLayoutParams(layoutParams);
        textView1.setTextColor(Color.BLACK);
        textView1.setBackgroundResource(R.drawable.bottext1);
        //textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
        textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView1.setTypeface(null, Typeface.NORMAL);
        linearLayout.addView(textView1);
        textView1.setText(message);
        final String finalMessage = message;
        repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    repeatTTS.setLanguage(Locale.ENGLISH);
                    repeatTTS.speak(finalMessage, TextToSpeech.QUEUE_ADD, null);
                }
            }
        });


       /* VoiceInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                //startVoiceInput();



            }
        });


        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0){
                    btn2.setImageResource(R.drawable.sendicon);
                    btn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String str = edt.getText().toString();
                            if (str.equals("18 0")){
                                Graphs g = new Graphs(48);
                                Vector<String> v = g.AllPossiblities(18,0);
                                Toast.makeText(getApplicationContext(),v.elementAt(0),Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Chat(str, linearLayout, scroll, myConversationService, userName);
                                edt.setText("");
                            }

                        }
                    });
                }

                else {
                    btn2.setImageResource(R.drawable.voices);
                    btn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startVoiceInput();
                        }
                    });

                }

            }
        });



        final String password = getIntent().getStringExtra("password");
        ImageView settings = (ImageView) findViewById(R.id.settingbtn_english);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EnglishChatbot.this);
                LayoutInflater inflater = EnglishChatbot.this.getLayoutInflater();
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
                final Button frbtn = (Button) dialogView.findViewById(R.id.languagefr);
                final Button angbtn = (Button) dialogView.findViewById(R.id.languageang);




                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String theme = themeslist.getSelectedItem().toString();
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference().child("comptes");
                        String node = password+ ""+name;
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

        ImageView games = (ImageView) findViewById(R.id.gamesbtn_english);
        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),GameActivity.class));
            }
        });
    }
}
