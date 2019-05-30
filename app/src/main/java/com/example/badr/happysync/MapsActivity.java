
package com.example.badr.happysync;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.DUMP;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final int PERMISSIONS_REQUEST = 100;
    Double longitude = -1.0 ;
    Double latitude= -1.0 ;
    Double latitude1 = 1.0 ;
    Double longitude1 = 1.0 ;
    Marker marker;
    int n = 0;
    int nb = 0;
    boolean testArret = false ;
    Double diffLatitude = 0.006;
    Double diffLongitude = 0.011;
    private TextToSpeech repeatTTS;
    String outputText = "";
    Vector<Marker> listeBus = new Vector<Marker>();
    Vector<Marker> listeMetro = new Vector<Marker>();
    Vector<Marker> listeTGM = new Vector<Marker>();

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.FRANCE);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "veuillez disez 'bus...' ou 'metro...' ");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }



    private String downloadUrl(String strUrl) {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();
            iStream.close();
            urlConnection.disconnect();

        } catch (Exception e) {
            //Log.d("Exception while downloading url", e.toString());
        }
        //Log.d("Data =", "url"+data.toString());
        return data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final ConversationService myConversationService =
                new ConversationService(
                        "2018-02-16",
                        getString(R.string.username),
                        getString(R.string.password)
                );


        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    final Intent intent = getIntent();
                    final String type = intent.getStringExtra("type");
                    String email  = intent.getStringExtra("email");
                    final EditText edt = (EditText) findViewById(R.id.searchBar);

                    final Double  longit = Double.parseDouble(intent.getStringExtra("longitude"));
                    final Double lati = Double.parseDouble(intent.getStringExtra("latitude"));
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref;

                    final String finalStr = result.get(0);

                    MessageRequest request = new MessageRequest.Builder()
                            .inputText(finalStr)
                            .build();
                    myConversationService
                            .message(getString(R.string.workspace2), request)
                            .enqueue(new ServiceCallback<MessageResponse>() {
                                @Override
                                public void onResponse(MessageResponse response) {
                                    outputText = response.getText().get(0);
                                    runOnUiThread(new Runnable() {
                                        @SuppressLint("Range")
                                        @Override
                                        public void run() {
                                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference ref = null;
                                            if (type.equals("lignemetro")) {

                                                if (outputText.equals("tgm")) {
                                                    ref = database.getReference().child("TGM");

                                                    ref.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                                String ch = zoneSnapshot.child("nom").getValue(String.class);
                                                                longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                                latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));


                                                                LatLng positionMarker = new LatLng(latitude, longitude);
                                                                marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.tgmstation)));
                                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));



                                                            }
                                                            repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                                                @SuppressLint("NewApi")
                                                                @Override
                                                                public void onInit(int i) {
                                                                    if (i == TextToSpeech.SUCCESS) {
                                                                        repeatTTS.setLanguage(Locale.FRENCH);
                                                                        repeatTTS.speak("Si vous voulez allez au carthage ou sidi bousaid, le TGM et la meilleur solution", TextToSpeech.QUEUE_ADD, null);
                                                                    }
                                                                }
                                                            });

                                                            SimulatorGPS s = new SimulatorGPS();
                                                            s.simulateTGM();

                                                            DatabaseReference ref2 = database.getReference().child("TGMGPS");
                                                            ref2.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot snapshot) {

                                                                    for (int i=0;i<listeTGM.size();i++){
                                                                        listeTGM.elementAt(i).remove();
                                                                    }

                                                                    for (DataSnapshot noeud : snapshot.getChildren()) {

                                                                        longitude = Double.parseDouble(noeud.child("longitude").getValue(String.class));
                                                                        latitude = Double.parseDouble(noeud.child("latitude").getValue(String.class));
                                                                        LatLng positionMarker = new LatLng(latitude, longitude);
                                                                        marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title("TGM").icon(BitmapDescriptorFactory.fromResource(R.drawable.tgmplaceholder)));

                                                                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                                                                        marker.showInfoWindow();
                                                                        listeTGM.add(marker);

                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                } else if (outputText.equals("1")||outputText.equals("2")||outputText.equals("3")||outputText.equals("4")||outputText.equals("5")||outputText.equals("6")) {

                                                    ref = database.getReference().child("metro" + outputText);

                                                    ref.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                                String ch = zoneSnapshot.child("nom").getValue(String.class);
                                                                longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                                latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                                                                n++;
                                                                LatLng positionMarker = new LatLng(latitude, longitude);
                                                                marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.trainstation)));
                                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

                                                                if (n == 1) {
                                                                    longitude1 = longitude;
                                                                    latitude1 = latitude;
                                                                } else {
                                                                    mMap.addPolyline(new PolylineOptions()
                                                                            .add(new LatLng(latitude1, longitude1), new LatLng(latitude, longitude))
                                                                            .width(5)
                                                                            .color(Color.GREEN));
                                                                    longitude1 = longitude;
                                                                    latitude1 = latitude;

                                                                }

                                                            }
                                                            SimulatorGPS s = new SimulatorGPS();
                                                            s.simulateMetro();
                                                            nb = 0 ;
                                                            DatabaseReference ref2 = database.getReference().child("Metro");
                                                            ref2.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot snapshot) {

                                                                    for (int i=0;i<listeMetro.size();i++){
                                                                        listeMetro.elementAt(i).remove();
                                                                    }

                                                                    for (DataSnapshot noeud : snapshot.getChildren()) {
                                                                        String num = noeud.child("num").getValue(String.class);
                                                                        if (num != null && num.equals(outputText)) {
                                                                            longitude = Double.parseDouble(noeud.child("longitude").getValue(String.class));
                                                                            latitude = Double.parseDouble(noeud.child("latitude").getValue(String.class));
                                                                            LatLng positionMarker = new LatLng(latitude, longitude);
                                                                            marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title("Metro").icon(BitmapDescriptorFactory.fromResource(R.drawable.metroplaceholder)));

                                                                            mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                                                                            marker.showInfoWindow();
                                                                            listeMetro.add(marker);
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                }
                                                            });

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }

                                            if (type.equals("bus")) {



                                                ref = database.getReference().child("stationsbus");
                                                ref.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                            String ch = zoneSnapshot.child("Gouv").getValue(String.class);
                                                            if (ch.equals(outputText)) {
                                                                String name = zoneSnapshot.child("nom").getValue(String.class);
                                                                longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                                latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                                                                LatLng positionMarker = new LatLng(latitude, longitude);
                                                                marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinbus)));
                                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                                                                testArret = true;
                                                            }
                                                        }
                                                        if(testArret == false){
                                                            for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                                String ch = zoneSnapshot.child("Ville").getValue(String.class);
                                                                if (ch.equals(outputText)) {
                                                                    String name = zoneSnapshot.child("nom").getValue(String.class);
                                                                    longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                                    latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                                                                    LatLng positionMarker = new LatLng(latitude, longitude);
                                                                    marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinbus)));
                                                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                                                                    testArret = true;
                                                                }
                                                            }
                                                            if (testArret == false){
                                                                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                                    String ch = zoneSnapshot.child("nom").getValue(String.class);
                                                                    if (ch.equals(outputText)) {
                                                                        longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                                        latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                                                                        LatLng positionMarker = new LatLng(latitude, longitude);
                                                                        marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(outputText).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinbus)));
                                                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            else{
                                                                repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                                                    @SuppressLint("NewApi")
                                                                    @Override
                                                                    public void onInit(int i) {
                                                                        if (i == TextToSpeech.SUCCESS) {
                                                                            repeatTTS.setLanguage(Locale.FRENCH);
                                                                            repeatTTS.speak("j'ai pas pu comprendre ce que vous disez essayez de l'ecrire", TextToSpeech.QUEUE_ADD, null);
                                                                        }
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
                                            else if (type.equals("kiosque")){
                                                ref = database.getReference().child("kiosques");

                                                final DatabaseReference finalRef = ref;
                                                ref.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Marker nearKiosque = null;
                                                        float minDistance = 900000;
                                                        for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                            String ch = zoneSnapshot.child("nom").getValue(String.class);

                                                            if (ch.equals(outputText)){
                                                                longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                                latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));

                                                                LatLng positionMarker = new LatLng(latitude, longitude);
                                                                marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholdergas)));
                                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                                                                Location loc1 = new Location("");
                                                                loc1.setLatitude(lati);
                                                                loc1.setLongitude(longit);
                                                                Location loc2 = new Location("");
                                                                loc2.setLatitude(marker.getPosition().latitude);
                                                                loc2.setLongitude(marker.getPosition().longitude);
                                                                final float distanceInMeters = loc1.distanceTo(loc2);
                                                                if (distanceInMeters<minDistance){
                                                                    minDistance = distanceInMeters;
                                                                    nearKiosque = marker;
                                                                }
                                                            }
                                                        }
                                                        if (nearKiosque != null){
                                                            nearKiosque.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.placeholdergasgold));
                                                            LatLng positionMarker = new LatLng(nearKiosque.getPosition().latitude, nearKiosque.getPosition().longitude);
                                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                            String dist = "";
                                                            if (minDistance<1000){
                                                                dist = new DecimalFormat("##.##").format(minDistance)+" metres";
                                                            }
                                                            else {
                                                                dist = new DecimalFormat("##.##").format(minDistance/1000)+" kilometre";
                                                            }
                                                            final String finalDist = dist;
                                                            final Marker finalNearKiosque = nearKiosque;
                                                            repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                                                @SuppressLint("NewApi")
                                                                @Override
                                                                public void onInit(int i) {
                                                                    if (i == TextToSpeech.SUCCESS) {
                                                                        repeatTTS.setLanguage(Locale.FRENCH);
                                                                        repeatTTS.speak("Voici la station "+ finalNearKiosque.getTitle()+" la plus prés de vous, elle est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                                                    }
                                                                }
                                                            });
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }

                                            else if (type.equals("parking")){
                                                ref = database.getReference().child("Parkings");

                                                final DatabaseReference finalRef = ref;
                                                ref.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Marker nearParking = null;
                                                        float minDistance = 900000;
                                                        for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                            String ch = zoneSnapshot.child("ville").getValue(String.class);

                                                            if (ch.equals(outputText)){
                                                                longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                                latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));

                                                                LatLng positionMarker = new LatLng(latitude, longitude);
                                                                marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.parkingpin)));
                                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                                                                Location loc1 = new Location("");
                                                                loc1.setLatitude(lati);
                                                                loc1.setLongitude(longit);
                                                                Location loc2 = new Location("");
                                                                loc2.setLatitude(marker.getPosition().latitude);
                                                                loc2.setLongitude(marker.getPosition().longitude);
                                                                final float distanceInMeters = loc1.distanceTo(loc2);
                                                                if (distanceInMeters<minDistance){
                                                                    minDistance = distanceInMeters;
                                                                    nearParking = marker;
                                                                }
                                                            }
                                                        }
                                                        if (nearParking != null){
                                                            nearParking.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.nearpark));
                                                            LatLng positionMarker = new LatLng(nearParking.getPosition().latitude, nearParking.getPosition().longitude);
                                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));

                                                            String address = "";
                                                            try {
                                                                Geocoder geocoder;
                                                                List<Address> addresses;
                                                                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                                                addresses = geocoder.getFromLocation(nearParking.getPosition().latitude, nearParking.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                                                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }


                                                            String dist = "";
                                                            if (minDistance<1000){
                                                                dist = new DecimalFormat("##.##").format(minDistance)+" metres";
                                                            }
                                                            else {
                                                                dist = new DecimalFormat("##.##").format(minDistance/1000)+" kilometre";
                                                            }
                                                            final String finalDist = dist;
                                                            final Marker finalNearParking = nearParking;
                                                            final String finalAddress = address;
                                                            repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                                                @SuppressLint("NewApi")
                                                                @Override
                                                                public void onInit(int i) {
                                                                    if (i == TextToSpeech.SUCCESS) {
                                                                        repeatTTS.setLanguage(Locale.FRENCH);
                                                                        if (finalAddress.equals("")){
                                                                            repeatTTS.speak("à "+outputText+" voici le parking le plus proche de vous, il est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                                                        }
                                                                        else {
                                                                            repeatTTS.speak("à "+outputText+" le parking le plus proche de vous, ce trouve à "+finalAddress+" exactement "+finalDist+" loin", TextToSpeech.QUEUE_ADD, null);
                                                                        }


                                                                    }
                                                                }
                                                            });
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }

                                            else if (type.equals("lignebus")) {

                                                ref = database.getReference().child("Buslignes");
                                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String terminus1 = "";
                                                        String terminus2 = "";
                                                        Marker term1 = null;
                                                        Marker term2 = null;
                                                        int numero = 0;
                                                        for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                            String ch = zoneSnapshot.child("ligne").getValue(String.class);
                                                            if (ch.equals(finalStr.toUpperCase())) {

                                                                String name = zoneSnapshot.child("nom").getValue(String.class);
                                                                longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                                latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                                                                LatLng positionMarker = new LatLng(latitude, longitude);
                                                                marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinbus)));
                                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                                                                nb++;
                                                                String num = zoneSnapshot.child("num").getValue(String.class);
                                                                if (num.equals("1")){
                                                                    terminus1 = zoneSnapshot.child("nom").getValue(String.class);
                                                                    term1 = marker;
                                                                }
                                                                else{
                                                                    int numero2 = Integer.parseInt(zoneSnapshot.child("num").getValue(String.class));
                                                                    if (numero2>numero){
                                                                        numero = numero2;
                                                                        terminus2 = zoneSnapshot.child("nom").getValue(String.class);
                                                                        term2 = marker;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if (nb==0){
                                                            Toast.makeText(getApplicationContext(),"Pas de ligne avec ce numéro",Toast.LENGTH_SHORT).show();
                                                        }
                                                        else{
                                                           /* SimulatorGPS s = new SimulatorGPS();
                                                            s.simulateBUS();*/
                                                            nb = 0 ;

                                                            term1.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.buspin));
                                                            term2.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.buspin));
                                                            final String finalTerminus = terminus2;
                                                            final String finalTerminus1 = terminus1;
                                                            repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                                                @SuppressLint("NewApi")
                                                                @Override
                                                                public void onInit(int i) {
                                                                    if (i == TextToSpeech.SUCCESS) {
                                                                        repeatTTS.setLanguage(Locale.FRENCH);
                                                                        repeatTTS.speak(finalStr+" Commence son trajet du "+ finalTerminus1 +" jusqu'au "+ finalTerminus, TextToSpeech.QUEUE_ADD, null);
                                                                    }
                                                                }
                                                            });

                                                            /*DatabaseReference ref2 = database.getReference().child("Bus");
                                                            ref2.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot snapshot) {

                                                                    for (int i=0;i<listeBus.size();i++){
                                                                        listeBus.elementAt(i).remove();
                                                                    }

                                                                    for (DataSnapshot noeud : snapshot.getChildren()) {
                                                                        String ch = noeud.child("num").getValue(String.class);
                                                                        if (ch.equals(str)){
                                                                            longitude = Double.parseDouble(noeud.child("longitude").getValue(String.class));
                                                                            latitude = Double.parseDouble(noeud.child("latitude").getValue(String.class));
                                                                            LatLng positionMarker = new LatLng(latitude, longitude);
                                                                            marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title("Bus "+str).icon(BitmapDescriptorFactory.fromResource(R.drawable.buspin)));
                                                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                            mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                                                                            listeBus.add(marker);
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                }
                                                            });*/

                                                        }


                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }


                                                });

                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(),"En cour de developpement",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Exception e) {
                                }
                            });
                }
                break;
            }

        }
    }


    public Vector<Double> getUserPosition(final String email){

        final Vector<Double> userPosition = new Vector<Double>();


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("comptes");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    String ch = zoneSnapshot.child("email").getValue(String.class);

                    if (email.equals(ch)){
                        Double longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                        Double latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                        userPosition.add(latitude);
                        userPosition.add(longitude);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return  userPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        RelativeLayout header = (RelativeLayout) findViewById(R.id.mapheader);
        TextView mappagetitle = (TextView) findViewById(R.id.mappagetitle);
        ImageView returnBtn = (ImageView) findViewById(R.id.returnbtn);
        final EditText edt = (EditText) findViewById(R.id.searchBar);

        Button voice = (Button) findViewById(R.id.voicesearch);

        Toast.makeText(getApplicationContext(),"certain station mieux de les ecrire je peux pas comprendre leur nom",Toast.LENGTH_SHORT).show();
        Intent intent= getIntent();


        String type = intent.getStringExtra("type");
        String email  = intent.getStringExtra("email");

        if (type.equals("lignemetro")){
            final int sdk = Build.VERSION.SDK_INT;
            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                header.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.metroheader) );

            } else {
                header.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.metroheader));
            }

            mappagetitle.setText("Métro et TGM");
            edt.setHint("Numéro métro");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                voice.setBackgroundTintList(ColorStateList.valueOf(getApplicationContext().getResources().getColor(R.color.metrocolor)));
            }
        }
        else if (type.equals("bus")){
            final int sdk = Build.VERSION.SDK_INT;
            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                header.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.busheader) );
            } else {
                header.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.busheader));
            }

            mappagetitle.setText("Stations bus");
            edt.setHint("nom station");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                voice.setBackgroundTintList(ColorStateList.valueOf(getApplicationContext().getResources().getColor(R.color.buscolor)));
            }
        }
        else if (type.equals("kiosque")){
            final int sdk = Build.VERSION.SDK_INT;
            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                header.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.kiosqueheader) );
            } else {
                header.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.kiosqueheader));
            }

            mappagetitle.setText("Stations kiosques");
            edt.setHint("SHELL, TOTAL, AGIL");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                voice.setBackgroundTintList(ColorStateList.valueOf(getApplicationContext().getResources().getColor(R.color.kiosquecolor)));
            }
        }
        else if (type.equals("parking")){
            final int sdk = Build.VERSION.SDK_INT;
            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                header.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.parkingheader) );
            } else {
                header.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.parkingheader));
            }

            mappagetitle.setText("Parkings");
            edt.setHint("Tunis, Ariana...");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                voice.setBackgroundTintList(ColorStateList.valueOf(getApplicationContext().getResources().getColor(R.color.parkingcolor)));
            }
        }
        else if (type.equals("lignebus")){
            final int sdk = Build.VERSION.SDK_INT;
            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                header.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.busheader) );
            } else {
                header.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.busheader));
            }

            mappagetitle.setText("Ligne bus");
            edt.setHint("Numéro du bus");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                voice.setBackgroundTintList(ColorStateList.valueOf(getApplicationContext().getResources().getColor(R.color.buscolor)));
            }
        }


    }

    Vector<Integer> localisation = new Vector<Integer>();

    int nbmetro = 0;
    int nbbus = 0 ;
    int nbparking = 0 ;
    int nbkiosque = 0 ;

public void drawMarker(final Double Userlongitude, final Double Userlatitude, final GoogleMap googleMap) {


    for (int i = 1; i < 7; i++) {
        String str = String.valueOf(i);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("metro" + str);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                    String ch = zoneSnapshot.child("nom").getValue(String.class);
                    longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                    latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));

                    if (latitude <= Userlatitude + diffLatitude && latitude >= Userlatitude - diffLatitude) {
                        if (longitude <= Userlongitude + diffLongitude && longitude >= Userlongitude - diffLongitude) {
                            mMap = googleMap;
                            LatLng positionMarker = new LatLng(latitude, longitude);
                            marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.metro)));

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference().child("stationsbus");

    ref.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                String ch = zoneSnapshot.child("nom").getValue(String.class);
                longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));

                if (latitude <= Userlatitude + diffLatitude && latitude >= Userlatitude - diffLatitude) {
                    if (longitude <= Userlongitude + diffLongitude && longitude >= Userlongitude - diffLongitude) {
                        mMap = googleMap;
                        LatLng positionMarker = new LatLng(latitude, longitude);
                        marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinbus)));

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });


    ref = database.getReference().child("Parkings");

    ref.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {


                longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));

                if (latitude <= Userlatitude + diffLatitude && latitude >= Userlatitude - diffLatitude) {
                    if (longitude <= Userlongitude + diffLongitude && longitude >= Userlongitude - diffLongitude) {
                        mMap = googleMap;
                        LatLng positionMarker = new LatLng(latitude, longitude);
                        marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title("Parking").icon(BitmapDescriptorFactory.fromResource(R.drawable.parkingpin)));

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

    ref = database.getReference().child("kiosques");

    ref.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                String ch = zoneSnapshot.child("nom").getValue(String.class);
                longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));

                if (latitude <= Userlatitude + diffLatitude && latitude >= Userlatitude - diffLatitude) {
                    if (longitude <= Userlongitude + diffLongitude && longitude >= Userlongitude - diffLongitude) {
                        mMap = googleMap;
                        LatLng positionMarker = new LatLng(latitude, longitude);
                        marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholdergas)));

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });


    ref = database.getReference().child("TGM");

    ref.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                String ch = zoneSnapshot.child("nom").getValue(String.class);
                longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));

                if (latitude <= Userlatitude + diffLatitude && latitude >= Userlatitude - diffLatitude) {
                    if (longitude <= Userlongitude + diffLongitude && longitude >= Userlongitude - diffLongitude) {
                        mMap = googleMap;
                        LatLng positionMarker = new LatLng(latitude, longitude);
                        marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.tgm)));

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}


    public void drawParking(final Double Userlongitude, final Double Userlatitude, final GoogleMap googleMap) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Parkings");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float minDistance = 90000000 ;
                Marker NearestParking = null;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                    longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                    latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));


                    mMap = googleMap;
                    LatLng positionMarker = new LatLng(latitude, longitude);
                    marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title("Parking").icon(BitmapDescriptorFactory.fromResource(R.drawable.parkingpin)));

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    Location loc1 = new Location("");
                    loc1.setLatitude(Userlatitude);
                    loc1.setLongitude(Userlongitude);
                    Location loc2 = new Location("");
                    loc2.setLatitude(marker.getPosition().latitude);
                    loc2.setLongitude(marker.getPosition().longitude);


                    final float distanceInMeters = loc1.distanceTo(loc2);
                    if (distanceInMeters<minDistance){
                        minDistance = distanceInMeters;
                        NearestParking = marker;
                    }
                }
                if (NearestParking != null){
                    mMap.clear();

                    LatLng you = new LatLng(Userlatitude, Userlongitude);
                    marker = mMap.addMarker(new MarkerOptions().position(you).title("Toi").icon(BitmapDescriptorFactory.fromResource(R.drawable.maplaceholder)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(you));

                    LatLng positionMarker = new LatLng(NearestParking.getPosition().latitude, NearestParking.getPosition().longitude);
                    mMap.addMarker(new MarkerOptions().position(positionMarker).title("Parking").icon(BitmapDescriptorFactory.fromResource(R.drawable.nearpark)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    String dist;


                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(NearestParking.getPosition().latitude, NearestParking.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    if (minDistance<1000){
                        dist = new DecimalFormat("##.##").format(minDistance)+" metres";
                    }
                    else {
                        dist = new DecimalFormat("##.##").format(minDistance/1000)+" kilometre";
                    }
                    final String finalDist = dist;
                    final String finalAddress = address;
                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onInit(int i) {
                            if (i == TextToSpeech.SUCCESS) {
                                repeatTTS.setLanguage(Locale.FRENCH);
                                if (finalAddress.equals("")){
                                    repeatTTS.speak("Voici le parking la plus proche de vous, il est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                }
                                else {
                                    repeatTTS.speak("Le parking le plus proche de vous se trouve exactement à "+ finalAddress, TextToSpeech.QUEUE_ADD, null);
                                }

                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void drawKiosque(final Double Userlongitude, final Double Userlatitude, final GoogleMap googleMap){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference ref = database.getReference().child("kiosques");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Marker nearKiosque = null;
                float minDistance = 900000;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                    String ch = zoneSnapshot.child("nom").getValue(String.class);

                        longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                        latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));

                        mMap = googleMap;
                        LatLng positionMarker = new LatLng(latitude, longitude);
                        marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholdergas)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                        Location loc1 = new Location("");
                        loc1.setLatitude(Userlatitude);
                        loc1.setLongitude(Userlongitude);
                        Location loc2 = new Location("");
                        loc2.setLatitude(marker.getPosition().latitude);
                        loc2.setLongitude(marker.getPosition().longitude);
                        final float distanceInMeters = loc1.distanceTo(loc2);
                        if (distanceInMeters<minDistance){
                            minDistance = distanceInMeters;
                            nearKiosque = marker;
                        }

                }
                if (nearKiosque != null){
                    nearKiosque.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.placeholdergasgold));
                    LatLng positionMarker = new LatLng(nearKiosque.getPosition().latitude, nearKiosque.getPosition().longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        SimulatorGPS s ;

        final ConversationService myConversationService =
                new ConversationService(
                        "2018-02-16",
                        getString(R.string.username),
                        getString(R.string.password)
                );

        final Intent intent = getIntent();
        final String type = intent.getStringExtra("type");
        String email  = intent.getStringExtra("email");
        final EditText edt = (EditText) findViewById(R.id.searchBar);

        final Double  longit = Double.parseDouble(intent.getStringExtra("longitude"));
        final Double lati = Double.parseDouble(intent.getStringExtra("latitude"));






        mMap = googleMap;
        LatLng positionMarker = new LatLng(lati, longit);
        marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title("Toi").icon(BitmapDescriptorFactory.fromResource(R.drawable.maplaceholder)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

        if (!(intent.getStringExtra("type").equals("lignemetro")&&intent.getStringExtra("metro")!=null )   && !(intent.getStringExtra("type").equals("parking")) && !type.equals("nearkiosque")) {

            drawMarker(longit, lati, googleMap);

         }


         if (intent.getStringExtra("type").equals("parking") || intent.getStringExtra("type").equals("nearparking")){
             drawParking(longit, lati, googleMap);
         }


         if (type.equals("nearkiosque")){
             drawKiosque(longit,lati,googleMap);
         }




        Button voicesearch = (Button) findViewById(R.id.voicesearch);

        voicesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVoiceInput();
            }
        });







        if (type.equals("lignemetro")&&intent.getStringExtra("metro")!=null){
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref;
            ref = database.getReference().child("metro"+intent.getStringExtra("metro"));

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                        String ch = zoneSnapshot.child("nom").getValue(String.class);
                        longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                        latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                        n++;
                        mMap = googleMap;
                        LatLng positionMarker = new LatLng(latitude, longitude);
                        marker =  mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.metro)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );

                        if (n == 1){
                            longitude1 = longitude;
                            latitude1 = latitude;
                        }
                        else{
                            mMap.addPolyline(new PolylineOptions()
                                    .add(new LatLng(latitude1, longitude1), new LatLng(latitude, longitude))
                                    .width(5)
                                    .color(Color.BLUE));
                            longitude1 = longitude;
                            latitude1 = latitude;

                        }

                    }
                    n = 0;

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        if (type.equals("lignemetro")&&intent.getStringExtra("station")!=null){
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = null;

            for (int i=1;i<7;i++){
                ref = database.getReference().child("metro"+i);

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                            String ch = zoneSnapshot.child("nom").getValue(String.class);
                            if (ch.equals(intent.getStringExtra("station"))) {
                                Double longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                Double latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                                //latLng = new LatLng(latitude,longitude);
                                mMap = googleMap;
                                LatLng positionMarker = new LatLng(latitude, longitude);
                                marker =  mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.metro)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
                                break;
                            }
                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }


        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {


                    mMap.clear();

                    mMap = googleMap;
                    LatLng myPos = new LatLng(lati, longit);
                    Marker me = mMap.addMarker(new MarkerOptions().position(myPos).title("Toi").icon(BitmapDescriptorFactory.fromResource(R.drawable.boy)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    final String str = edt.getText().toString();


                    MessageRequest request = new MessageRequest.Builder()
                            .inputText(str)
                            .build();
                    myConversationService
                            .message(getString(R.string.workspace2), request)
                            .enqueue(new ServiceCallback<MessageResponse>() {
                                @Override
                                public void onResponse(MessageResponse response) {
                                    outputText = response.getText().get(0);
                                    runOnUiThread(new Runnable() {
                                        @SuppressLint("Range")
                                        @Override
                                        public void run() {
                                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference ref = null;
                                            if (type.equals("lignemetro")) {

                                                if (outputText.equals("tgm")) {
                                                    ref = database.getReference().child("TGM");

                                                    ref.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                                String ch = zoneSnapshot.child("nom").getValue(String.class);
                                                                longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                                latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                                                                mMap = googleMap;

                                                                LatLng positionMarker = new LatLng(latitude, longitude);
                                                                marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.tgmstation)));
                                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));



                                                            }
                                                            repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                                                @SuppressLint("NewApi")
                                                                @Override
                                                                public void onInit(int i) {
                                                                    if (i == TextToSpeech.SUCCESS) {
                                                                        repeatTTS.setLanguage(Locale.FRENCH);
                                                                        repeatTTS.speak("Si vous voulez allez au carthage ou sidi bousaid, le TGM et la meilleur solution", TextToSpeech.QUEUE_ADD, null);
                                                                    }
                                                                }
                                                            });

                                                            SimulatorGPS s = new SimulatorGPS();
                                                            s.simulateTGM();

                                                            DatabaseReference ref2 = database.getReference().child("TGMGPS");
                                                            ref2.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot snapshot) {

                                                                    for (int i=0;i<listeTGM.size();i++){
                                                                        listeTGM.elementAt(i).remove();
                                                                    }

                                                                    for (DataSnapshot noeud : snapshot.getChildren()) {

                                                                        longitude = Double.parseDouble(noeud.child("longitude").getValue(String.class));
                                                                        latitude = Double.parseDouble(noeud.child("latitude").getValue(String.class));
                                                                        LatLng positionMarker = new LatLng(latitude, longitude);
                                                                        marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title("TGM").icon(BitmapDescriptorFactory.fromResource(R.drawable.tgmplaceholder)));


                                                                        marker.showInfoWindow();
                                                                        listeTGM.add(marker);

                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }else if (outputText.equals("Train")){
                                                    ref = database.getReference().child("TrainRades");

                                                    ref.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                                String ch = zoneSnapshot.child("Nom").getValue(String.class);
                                                                longitude = Double.parseDouble(zoneSnapshot.child("Longitude").getValue(String.class));
                                                                latitude = Double.parseDouble(zoneSnapshot.child("Latitude").getValue(String.class));
                                                                mMap = googleMap;

                                                                LatLng positionMarker = new LatLng(latitude, longitude);
                                                                marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.trainrades)));
                                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));



                                                            }
                                                            repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                                                @SuppressLint("NewApi")
                                                                @Override
                                                                public void onInit(int i) {
                                                                    if (i == TextToSpeech.SUCCESS) {
                                                                        repeatTTS.setLanguage(Locale.FRENCH);
                                                                        repeatTTS.speak("le train commence son du place barcelone au Erriadh, passe aussi par rades et Hamem lif", TextToSpeech.QUEUE_ADD, null);
                                                                    }
                                                                }
                                                            });

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                                else if (outputText.equals("1")||outputText.equals("2")||outputText.equals("3")||outputText.equals("4")||outputText.equals("5")||outputText.equals("6")) {

                                                    ref = database.getReference().child("metro" + outputText);

                                                    ref.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                                String ch = zoneSnapshot.child("nom").getValue(String.class);
                                                                longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                                latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                                                                n++;
                                                                mMap = googleMap;
                                                                LatLng positionMarker = new LatLng(latitude, longitude);
                                                                marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.trainstation)));
                                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

                                                                if (n == 1) {
                                                                    longitude1 = longitude;
                                                                    latitude1 = latitude;
                                                                } else {
                                                                    mMap.addPolyline(new PolylineOptions()
                                                                            .add(new LatLng(latitude1, longitude1), new LatLng(latitude, longitude))
                                                                            .width(5)
                                                                            .color(Color.GREEN));
                                                                    longitude1 = longitude;
                                                                    latitude1 = latitude;

                                                                }

                                                            }
                                                            SimulatorGPS s = new SimulatorGPS();
                                                            s.simulateMetro();
                                                            s.simulateMetro2();
                                                            nb = 0 ;
                                                            DatabaseReference ref2 = database.getReference().child("Metro");
                                                            ref2.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot snapshot) {

                                                                    for (int i=0;i<listeMetro.size();i++){
                                                                        listeMetro.elementAt(i).remove();
                                                                    }

                                                                    for (DataSnapshot noeud : snapshot.getChildren()) {
                                                                        String num = noeud.child("num").getValue(String.class);
                                                                        if (num != null && num.equals(outputText)) {
                                                                            longitude = Double.parseDouble(noeud.child("longitude").getValue(String.class));
                                                                            latitude = Double.parseDouble(noeud.child("latitude").getValue(String.class));
                                                                            LatLng positionMarker = new LatLng(latitude, longitude);
                                                                            marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title("Metro").icon(BitmapDescriptorFactory.fromResource(R.drawable.metroplaceholder)));
                                                                            marker.showInfoWindow();
                                                                            listeMetro.add(marker);
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                }
                                                            });

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }

                                            if (type.equals("bus")) {



                                                ref = database.getReference().child("stationsbus");
                                                ref.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Marker neararret = null;
                                                        float minDistance = 900000;
                                                        for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                            String ch = zoneSnapshot.child("Gouv").getValue(String.class);
                                                            if (ch.equals(outputText)) {
                                                                String name = zoneSnapshot.child("nom").getValue(String.class);
                                                                longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                                latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                                                                LatLng positionMarker = new LatLng(latitude, longitude);
                                                                marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinbus)));
                                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                                                                testArret = true;

                                                                Location loc1 = new Location("");
                                                                loc1.setLatitude(lati);
                                                                loc1.setLongitude(longit);
                                                                Location loc2 = new Location("");
                                                                loc2.setLatitude(marker.getPosition().latitude);
                                                                loc2.setLongitude(marker.getPosition().longitude);
                                                                final float distanceInMeters = loc1.distanceTo(loc2);
                                                                if (distanceInMeters<minDistance){
                                                                    minDistance = distanceInMeters;
                                                                    neararret = marker;
                                                                }
                                                            }
                                                        }
                                                        if(testArret == false){
                                                            for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                                String ch = zoneSnapshot.child("Ville").getValue(String.class);
                                                                if (ch.equals(outputText)) {
                                                                    String name = zoneSnapshot.child("nom").getValue(String.class);
                                                                    longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                                    latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                                                                    LatLng positionMarker = new LatLng(latitude, longitude);
                                                                    marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinbus)));
                                                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                                                                    testArret = true;

                                                                    Location loc1 = new Location("");
                                                                    loc1.setLatitude(lati);
                                                                    loc1.setLongitude(longit);
                                                                    Location loc2 = new Location("");
                                                                    loc2.setLatitude(marker.getPosition().latitude);
                                                                    loc2.setLongitude(marker.getPosition().longitude);
                                                                    final float distanceInMeters = loc1.distanceTo(loc2);
                                                                    if (distanceInMeters<minDistance){
                                                                        minDistance = distanceInMeters;
                                                                        neararret = marker;
                                                                    }
                                                                }
                                                            }
                                                            if (testArret == false){
                                                                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                                    String ch = zoneSnapshot.child("nom").getValue(String.class);
                                                                    if (ch.equals(outputText)) {
                                                                        longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                                        latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                                                                        LatLng positionMarker = new LatLng(latitude, longitude);
                                                                        marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(outputText).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinbus)));
                                                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            else{
                                                                repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                                                    @SuppressLint("NewApi")
                                                                    @Override
                                                                    public void onInit(int i) {
                                                                        if (i == TextToSpeech.SUCCESS) {
                                                                            repeatTTS.setLanguage(Locale.FRENCH);
                                                                            repeatTTS.speak("j'ai pas pu comprendre ce que vous disez essayez de l'ecrire", TextToSpeech.QUEUE_ADD, null);
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }

                                                        if (neararret != null){
                                                            neararret.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.buspin));
                                                            LatLng positionMarker = new LatLng(neararret.getPosition().latitude, neararret.getPosition().longitude);
                                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                            String dist = "";
                                                            if (minDistance<1000){
                                                                dist = new DecimalFormat("##.##").format(minDistance)+" metres";
                                                            }
                                                            else {
                                                                dist = new DecimalFormat("##.##").format(minDistance/1000)+" kilometre";
                                                            }
                                                            final String finalDist = dist;
                                                            final Marker finalNearKiosque = neararret;
                                                            repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                                                @SuppressLint("NewApi")
                                                                @Override
                                                                public void onInit(int i) {
                                                                    if (i == TextToSpeech.SUCCESS) {
                                                                        repeatTTS.setLanguage(Locale.FRENCH);
                                                                        repeatTTS.speak("Voici la station la plus prés de vous, elle est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                                                    }
                                                                }
                                                            });
                                                        }


                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                            }
                                            else if (type.equals("kiosque")){
                                                ref = database.getReference().child("kiosques");

                                                final DatabaseReference finalRef = ref;
                                                ref.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Marker nearKiosque = null;
                                                        float minDistance = 900000;
                                                        for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                            String ch = zoneSnapshot.child("nom").getValue(String.class);

                                                            if (ch.equals(outputText)){
                                                                longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                                latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));

                                                                mMap = googleMap;
                                                                LatLng positionMarker = new LatLng(latitude, longitude);
                                                                marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholdergas)));
                                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                                                                Location loc1 = new Location("");
                                                                loc1.setLatitude(lati);
                                                                loc1.setLongitude(longit);
                                                                Location loc2 = new Location("");
                                                                loc2.setLatitude(marker.getPosition().latitude);
                                                                loc2.setLongitude(marker.getPosition().longitude);
                                                                final float distanceInMeters = loc1.distanceTo(loc2);
                                                                if (distanceInMeters<minDistance){
                                                                    minDistance = distanceInMeters;
                                                                    nearKiosque = marker;
                                                                }
                                                            }
                                                        }
                                                        if (nearKiosque != null){
                                                            nearKiosque.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.placeholdergasgold));
                                                            LatLng positionMarker = new LatLng(nearKiosque.getPosition().latitude, nearKiosque.getPosition().longitude);
                                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                            String dist = "";
                                                            if (minDistance<1000){
                                                                dist = new DecimalFormat("##.##").format(minDistance)+" metres";
                                                            }
                                                            else {
                                                                dist = new DecimalFormat("##.##").format(minDistance/1000)+" kilometre";
                                                            }
                                                            final String finalDist = dist;
                                                            final Marker finalNearKiosque = nearKiosque;
                                                            repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                                                @SuppressLint("NewApi")
                                                                @Override
                                                                public void onInit(int i) {
                                                                    if (i == TextToSpeech.SUCCESS) {
                                                                        repeatTTS.setLanguage(Locale.FRENCH);
                                                                        repeatTTS.speak("Voici la station "+ finalNearKiosque.getTitle()+" la plus prés de vous, elle est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                                                    }
                                                                }
                                                            });
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }

                                            else if (type.equals("parking")){
                                                ref = database.getReference().child("Parkings");

                                                final DatabaseReference finalRef = ref;
                                                ref.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Marker nearParking = null;
                                                        float minDistance = 900000;
                                                        for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                            String ch = zoneSnapshot.child("ville").getValue(String.class);

                                                            if (ch.equals(outputText)){
                                                                longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                                latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));

                                                                mMap = googleMap;
                                                                LatLng positionMarker = new LatLng(latitude, longitude);
                                                                marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.parkingpin)));
                                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                                                                Location loc1 = new Location("");
                                                                loc1.setLatitude(lati);
                                                                loc1.setLongitude(longit);
                                                                Location loc2 = new Location("");
                                                                loc2.setLatitude(marker.getPosition().latitude);
                                                                loc2.setLongitude(marker.getPosition().longitude);
                                                                final float distanceInMeters = loc1.distanceTo(loc2);
                                                                if (distanceInMeters<minDistance){
                                                                    minDistance = distanceInMeters;
                                                                    nearParking = marker;
                                                                }
                                                            }
                                                        }
                                                        if (nearParking != null){
                                                            nearParking.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.nearpark));
                                                            LatLng positionMarker = new LatLng(nearParking.getPosition().latitude, nearParking.getPosition().longitude);
                                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));

                                                            String address = "";
                                                            try {
                                                                Geocoder geocoder;
                                                                List<Address> addresses;
                                                                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                                                addresses = geocoder.getFromLocation(nearParking.getPosition().latitude, nearParking.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                                                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }


                                                            String dist = "";
                                                            if (minDistance<1000){
                                                                dist = new DecimalFormat("##.##").format(minDistance)+" metres";
                                                            }
                                                            else {
                                                                dist = new DecimalFormat("##.##").format(minDistance/1000)+" kilometre";
                                                            }
                                                            final String finalDist = dist;
                                                            final Marker finalNearParking = nearParking;
                                                            final String finalAddress = address;
                                                            repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                                                @SuppressLint("NewApi")
                                                                @Override
                                                                public void onInit(int i) {
                                                                    if (i == TextToSpeech.SUCCESS) {
                                                                        repeatTTS.setLanguage(Locale.FRENCH);
                                                                        if (finalAddress.equals("")){
                                                                            repeatTTS.speak("à "+str+" voici le parking le plus proche de vous, il est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                                                        }
                                                                        else {
                                                                            repeatTTS.speak("à "+str+" le parking le plus proche de vous, ce trouve à "+finalAddress+" exactement "+finalDist+" loin", TextToSpeech.QUEUE_ADD, null);
                                                                        }


                                                                    }
                                                                }
                                                            });
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }

                                            else if (type.equals("lignebus")) {

                                                ref = database.getReference().child("Buslignes");
                                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String terminus1 = "";
                                                        String terminus2 = "";
                                                        Marker term1 = null;
                                                        Marker term2 = null;
                                                        int numero = 0;
                                                        for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                                            String ch = zoneSnapshot.child("ligne").getValue(String.class);
                                                            if (ch.equals(str.toUpperCase())) {

                                                                String name = zoneSnapshot.child("nom").getValue(String.class);
                                                                longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                                                                latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                                                                LatLng positionMarker = new LatLng(latitude, longitude);
                                                                marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.pinbus)));
                                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                                                                nb++;
                                                                String num = zoneSnapshot.child("num").getValue(String.class);
                                                                if (num.equals("1")){
                                                                    terminus1 = zoneSnapshot.child("nom").getValue(String.class);
                                                                    term1 = marker;
                                                                }
                                                                else{
                                                                    int numero2 = Integer.parseInt(zoneSnapshot.child("num").getValue(String.class));
                                                                    if (numero2>numero){
                                                                        numero = numero2;
                                                                        terminus2 = zoneSnapshot.child("nom").getValue(String.class);
                                                                        term2 = marker;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if (nb==0){
                                                            Toast.makeText(getApplicationContext(),"Pas de ligne avec ce numéro",Toast.LENGTH_SHORT).show();
                                                        }
                                                        else{
                                                           /* SimulatorGPS s = new SimulatorGPS();
                                                            s.simulateBUS();*/
                                                            nb = 0 ;

                                                            term1.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.buspin));
                                                            term2.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.buspin));
                                                            final String finalTerminus = terminus2;
                                                            final String finalTerminus1 = terminus1;
                                                            repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                                                @SuppressLint("NewApi")
                                                                @Override
                                                                public void onInit(int i) {
                                                                    if (i == TextToSpeech.SUCCESS) {
                                                                        repeatTTS.setLanguage(Locale.FRENCH);
                                                                        repeatTTS.speak(str+" Commence son trajet du "+ finalTerminus1 +" jusqu'au "+ finalTerminus, TextToSpeech.QUEUE_ADD, null);
                                                                    }
                                                                }
                                                            });

                                                            /*DatabaseReference ref2 = database.getReference().child("Bus");
                                                            ref2.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot snapshot) {

                                                                    for (int i=0;i<listeBus.size();i++){
                                                                        listeBus.elementAt(i).remove();
                                                                    }

                                                                    for (DataSnapshot noeud : snapshot.getChildren()) {
                                                                        String ch = noeud.child("num").getValue(String.class);
                                                                        if (ch.equals(str)){
                                                                            longitude = Double.parseDouble(noeud.child("longitude").getValue(String.class));
                                                                            latitude = Double.parseDouble(noeud.child("latitude").getValue(String.class));
                                                                            LatLng positionMarker = new LatLng(latitude, longitude);
                                                                            marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title("Bus "+str).icon(BitmapDescriptorFactory.fromResource(R.drawable.buspin)));
                                                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                                                                            mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                                                                            listeBus.add(marker);
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                }
                                                            });*/

                                                        }


                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }


                                                });

                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(),"En cour de developpement",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }


                                @Override
                                public void onFailure(Exception e) {
                                }
                            });

                }

                return false;

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){

            @Override
            public boolean onMarkerClick(final Marker marker) {



                if (type.equals("bus")) {

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("stationsbus");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                String ch = zoneSnapshot.child("nom").getValue(String.class);
                                if (ch.equals(marker.getTitle())) {
                                    final String bus = zoneSnapshot.child("Lignes").getValue(String.class);
                                    String busfinal = "";
                                    for (int i=0 ; i<bus.length();i++){
                                        if (bus.charAt(i)==' '){
                                            busfinal = busfinal + " , ";
                                        }
                                        else{
                                            busfinal = busfinal + bus.charAt(i);
                                        }
                                    }

                                    final String finalBusfinal = busfinal;
                                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                        @SuppressLint("NewApi")
                                        @Override
                                        public void onInit(int i) {
                                            if (i == TextToSpeech.SUCCESS) {
                                                repeatTTS.setLanguage(Locale.FRENCH);
                                                repeatTTS.speak("Les bus qui passe par ici sont : "+ finalBusfinal, TextToSpeech.QUEUE_ADD, null);
                                            }
                                        }
                                    });
                                    Location loc1 = new Location("");
                                    loc1.setLatitude(lati);
                                    loc1.setLongitude(longit);
                                    Location loc2 = new Location("");
                                    loc2.setLatitude(marker.getPosition().latitude);
                                    loc2.setLongitude(marker.getPosition().longitude);

                                    final float distanceInMeters = loc1.distanceTo(loc2);
                                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                        @SuppressLint("NewApi")
                                        @Override
                                        public void onInit(int i) {
                                            if (i == TextToSpeech.SUCCESS) {
                                                repeatTTS.setLanguage(Locale.FRENCH);
                                                repeatTTS.speak("Distance entre vous et "+marker.getTitle()+" , égale à "+new DecimalFormat("##.##").format(distanceInMeters/1000)+" kilometre.", TextToSpeech.QUEUE_ADD, null);
                                            }
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
                else if (type.equals("kiosque")){

                    marker.showInfoWindow();
                    Location loc1 = new Location("");
                    loc1.setLatitude(lati);
                    loc1.setLongitude(longit);
                    Location loc2 = new Location("");
                    loc2.setLatitude(marker.getPosition().latitude);
                    loc2.setLongitude(marker.getPosition().longitude);

                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    final float distanceInMeters = loc1.distanceTo(loc2);


                    String dist ;


                    if (distanceInMeters<1000){
                        dist = new DecimalFormat("##.##").format(distanceInMeters)+" metres";
                    }
                    else {
                        dist = new DecimalFormat("##.##").format(distanceInMeters/1000)+" kilometre";
                    }

                    final String finalDist = dist;
                    final String finalAddress = address;
                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onInit(int i) {
                            if (i == TextToSpeech.SUCCESS) {
                                repeatTTS.setLanguage(Locale.FRENCH);
                                if (finalAddress.equals("")){
                                    repeatTTS.speak("Distance entre vous et ce kiosque est "+ finalDist, TextToSpeech.QUEUE_ADD, null);
                                }
                                else{
                                    repeatTTS.speak("Ce kiosque se trouve exactement à "+ finalAddress +". Il est "+finalDist+" loin ", TextToSpeech.QUEUE_ADD, null);
                                }


                            }
                        }
                    });
                }

                else if (type.equals("parking")){
                    marker.showInfoWindow();
                    Location loc1 = new Location("");
                    loc1.setLatitude(lati);
                    loc1.setLongitude(longit);
                    Location loc2 = new Location("");
                    loc2.setLatitude(marker.getPosition().latitude);
                    loc2.setLongitude(marker.getPosition().longitude);

                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    final float distanceInMeters = loc1.distanceTo(loc2);


                    String dist ;


                    if (distanceInMeters<1000){
                        dist = new DecimalFormat("##.##").format(distanceInMeters)+" metres";
                    }
                    else {
                        dist = new DecimalFormat("##.##").format(distanceInMeters/1000)+" kilometre";
                    }

                    final String finalDist = dist;
                    final String finalAddress = address;
                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onInit(int i) {
                            if (i == TextToSpeech.SUCCESS) {
                                repeatTTS.setLanguage(Locale.FRENCH);
                                if (finalAddress.equals("")){
                                    repeatTTS.speak("Ce parking est exactement "+finalDist+" loin", TextToSpeech.QUEUE_ADD, null);
                                }
                                else{
                                    repeatTTS.speak("Ce parking se trouve à "+finalAddress+". à peu pré "+finalDist+" loin", TextToSpeech.QUEUE_ADD, null);
                                }


                            }
                        }
                    });
                }

                else if (type.equals("lignebus")){

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("Bus");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                                String ch = zoneSnapshot.child("num").getValue(String.class);
                                if (ch.equals(marker.getTitle())){

                                    Location loc1 = new Location("");
                                    loc1.setLatitude(Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class)));
                                    loc1.setLongitude(Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class)));
                                    Location loc2 = new Location("");
                                    loc2.setLatitude(marker.getPosition().latitude);
                                    loc2.setLongitude(marker.getPosition().longitude);
                                    final float distanceInMeters = loc1.distanceTo(loc2);

                                    repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                        @SuppressLint("NewApi")
                                        @Override
                                        public void onInit(int i) {
                                            if (i == TextToSpeech.SUCCESS) {
                                                repeatTTS.setLanguage(Locale.FRENCH);
                                                if (distanceInMeters<1000){
                                                    repeatTTS.speak(new DecimalFormat("##.##").format(distanceInMeters)+" metres la plus proche buse. elle est en "+zoneSnapshot.child("sens").getValue(String.class), TextToSpeech.QUEUE_ADD, null);

                                                }
                                                else {
                                                    repeatTTS.speak(new DecimalFormat("##.##").format(distanceInMeters)+" kilometre la plus proche buse. elle est en "+zoneSnapshot.child("sens").getValue(String.class), TextToSpeech.QUEUE_ADD, null);
                                                }


                                            }
                                        }
                                    });

                                    marker.setTitle(new DecimalFormat("##.##").format(distanceInMeters)+" metres la plus proche bus");
                                    marker.showInfoWindow();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                    return true;
            }
        });

    }








}
