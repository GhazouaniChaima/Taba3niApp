package com.example.badr.happysync;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class Marafe9 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String outputText = "";
    private TextToSpeech repeatTTS;
    Double longitude = -1.0 ;
    Double latitude= -1.0 ;
    Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marafe9);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        final EditText edt = (EditText) findViewById(R.id.searchBar3);

        final ConversationService myConversationService =
                new ConversationService(
                        "2018-02-16",
                        getString(R.string.username),
                        getString(R.string.password)
                );


        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String message = edt.getText().toString();


                    MessageRequest request = new MessageRequest.Builder()
                            .inputText(message)
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
                                            if (outputText.equals("Poste")) {
                                                DrawPoste(10.213111 , 36.8861552 , googleMap);
                                            }
                                            else if (outputText.equals("Steg")){
                                                DrawSteg(10.213111 , 36.8861552 , googleMap);
                                            }
                                            else if (outputText.equals("Sonede")){
                                                DrawSonede(10.213111 , 36.8861552 , googleMap);
                                            }
                                            else if (outputText.equals("Municipalite")){
                                                DrawBaladia(10.213111 , 36.8861552 , googleMap);
                                            }
                                            else if (outputText.equals("Carrefour")){
                                                DrawCarrefour(10.213111 , 36.8861552 , googleMap);
                                            }
                                            else if (outputText.equals("Musee")){
                                                DrawMuseum(10.213111 , 36.8861552 , googleMap);
                                            }
                                            else if (outputText.equals("Aziza")){
                                                DrawAziza(10.213111 , 36.8861552 , googleMap);
                                            }
                                            else if (outputText.equals("MG")){
                                                DrawMG(10.213111 , 36.8861552 , googleMap);
                                            }
                                            else if (outputText.equals("Monoprix")){
                                                DrawMonoprix(10.213111 , 36.8861552 , googleMap);
                                            }
                                            else if(outputText.equals("Hopital")){
                                                DrawHopital(10.213111 , 36.8861552 , googleMap);
                                            }
                                            else if (outputText.equals("pharmacie")){
                                                DrawPharmacie(10.213111 , 36.8861552 , googleMap);
                                            }
                                            else if (outputText.equals("cinema")){
                                                DrawPharmacie(10.213111 , 36.8861552 , googleMap);
                                            }
                                            else{

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
    }

    public void DrawPoste (final Double Userlongitude, final Double Userlatitude  , final GoogleMap googleMap)
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Poste");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float minDistance = 90000000 ;
                Marker nearestPoste = null;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    String ville = zoneSnapshot.child("Ville").getValue(String.class);
                    longitude = Double.parseDouble(zoneSnapshot.child("Longitude").getValue(String.class));
                    latitude = Double.parseDouble(zoneSnapshot.child("Latitude").getValue(String.class));


                    mMap = googleMap;
                    LatLng positionMarker = new LatLng(latitude, longitude);
                    marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ville).icon(BitmapDescriptorFactory.fromResource(R.drawable.postepin)));

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
                        nearestPoste = marker;
                    }
                }
                if (nearestPoste != null){


                    LatLng you = new LatLng(Userlatitude, Userlongitude);
                    marker = mMap.addMarker(new MarkerOptions().position(you).title("Toi").icon(BitmapDescriptorFactory.fromResource(R.drawable.maplaceholder)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(you));

                    LatLng positionMarker = new LatLng(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude);
                    mMap.addMarker(new MarkerOptions().position(positionMarker).title("Poste").icon(BitmapDescriptorFactory.fromResource(R.drawable.a9rebposte)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    String dist;


                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
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
                                    repeatTTS.speak("Voici la poste la plus proche de vous, il est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                }
                                else {
                                    repeatTTS.speak("Le bureau de poste le plus proche de vous se trouve exactement à "+ finalAddress, TextToSpeech.QUEUE_ADD, null);
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

    public void DrawSteg(final Double Userlongitude, final Double Userlatitude,final GoogleMap  googleMap){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Steg");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float minDistance = 90000000 ;
                Marker nearestPoste = null;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    String ville = zoneSnapshot.child("Ville").getValue(String.class);
                    longitude = Double.parseDouble(zoneSnapshot.child("Longitude").getValue(String.class));
                    latitude = Double.parseDouble(zoneSnapshot.child("Latitude").getValue(String.class));


                    mMap = googleMap;
                    LatLng positionMarker = new LatLng(latitude, longitude);
                    marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ville).icon(BitmapDescriptorFactory.fromResource(R.drawable.stegpin)));

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
                        nearestPoste = marker;
                    }
                }
                if (nearestPoste != null){


                    LatLng you = new LatLng(Userlatitude, Userlongitude);
                    marker = mMap.addMarker(new MarkerOptions().position(you).title("Toi").icon(BitmapDescriptorFactory.fromResource(R.drawable.maplaceholder)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(you));

                    LatLng positionMarker = new LatLng(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude);
                    mMap.addMarker(new MarkerOptions().position(positionMarker).title("Steg").icon(BitmapDescriptorFactory.fromResource(R.drawable.a9rebsteg)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    String dist;


                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
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
                                    repeatTTS.speak("Voici le bureau steg le plus proche de vous, il est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                }
                                else {
                                    repeatTTS.speak("Le bureau de steg le plus proche de vous se trouve exactement à "+ finalAddress, TextToSpeech.QUEUE_ADD, null);
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


    public void DrawSonede(final Double Userlongitude, final Double Userlatitude,final GoogleMap  googleMap){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Sonede");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float minDistance = 90000000 ;
                Marker nearestPoste = null;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    String ville = zoneSnapshot.child("Ville").getValue(String.class);
                    longitude = Double.parseDouble(zoneSnapshot.child("Longitude").getValue(String.class));
                    latitude = Double.parseDouble(zoneSnapshot.child("Latitude").getValue(String.class));


                    mMap = googleMap;
                    LatLng positionMarker = new LatLng(latitude, longitude);
                    marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ville).icon(BitmapDescriptorFactory.fromResource(R.drawable.sonedepin)));

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
                        nearestPoste = marker;
                    }
                }
                if (nearestPoste != null){


                    LatLng you = new LatLng(Userlatitude, Userlongitude);
                    marker = mMap.addMarker(new MarkerOptions().position(you).title("Toi").icon(BitmapDescriptorFactory.fromResource(R.drawable.maplaceholder)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(you));

                    LatLng positionMarker = new LatLng(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude);
                    mMap.addMarker(new MarkerOptions().position(positionMarker).title("Sonede").icon(BitmapDescriptorFactory.fromResource(R.drawable.a9rebsonede)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    String dist;


                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
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
                                    repeatTTS.speak("Voici le bureau sonede le plus proche de vous, il est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                }
                                else {
                                    repeatTTS.speak("Le bureau de sonede le plus proche de vous se trouve exactement à "+ finalAddress, TextToSpeech.QUEUE_ADD, null);
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

    public void DrawBaladia(final Double Userlongitude, final Double Userlatitude,final GoogleMap  googleMap){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Municipalite");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float minDistance = 90000000 ;
                Marker nearestPoste = null;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    String ville = zoneSnapshot.child("Ville").getValue(String.class);
                    longitude = Double.parseDouble(zoneSnapshot.child("Longitude").getValue(String.class));
                    latitude = Double.parseDouble(zoneSnapshot.child("Latitude").getValue(String.class));


                    mMap = googleMap;
                    LatLng positionMarker = new LatLng(latitude, longitude);
                    marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ville).icon(BitmapDescriptorFactory.fromResource(R.drawable.baladiapin)));

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
                        nearestPoste = marker;
                    }
                }
                if (nearestPoste != null){


                    LatLng you = new LatLng(Userlatitude, Userlongitude);
                    marker = mMap.addMarker(new MarkerOptions().position(you).title("Toi").icon(BitmapDescriptorFactory.fromResource(R.drawable.maplaceholder)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(you));

                    LatLng positionMarker = new LatLng(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude);
                    mMap.addMarker(new MarkerOptions().position(positionMarker).title("Sonede").icon(BitmapDescriptorFactory.fromResource(R.drawable.a9rebbaladia)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    String dist;


                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
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
                                    repeatTTS.speak("Voici le bureau sonede le plus proche de vous, il est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                }
                                else {
                                    repeatTTS.speak("Le bureau de sonede le plus proche de vous se trouve exactement à "+ finalAddress, TextToSpeech.QUEUE_ADD, null);
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

    public void DrawCarrefour(final Double Userlongitude, final Double Userlatitude,final GoogleMap  googleMap){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Carrefour");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float minDistance = 90000000 ;
                Marker nearestPoste = null;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    String ville = zoneSnapshot.child("Ville").getValue(String.class);
                    longitude = Double.parseDouble(zoneSnapshot.child("Longitude").getValue(String.class));
                    latitude = Double.parseDouble(zoneSnapshot.child("Latitude").getValue(String.class));


                    mMap = googleMap;
                    LatLng positionMarker = new LatLng(latitude, longitude);
                    marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ville).icon(BitmapDescriptorFactory.fromResource(R.drawable.carrefourpin)));

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
                        nearestPoste = marker;
                    }
                }
                if (nearestPoste != null){


                    LatLng you = new LatLng(Userlatitude, Userlongitude);
                    marker = mMap.addMarker(new MarkerOptions().position(you).title("Toi").icon(BitmapDescriptorFactory.fromResource(R.drawable.maplaceholder)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(you));

                    LatLng positionMarker = new LatLng(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude);
                    mMap.addMarker(new MarkerOptions().position(positionMarker).title("Sonede").icon(BitmapDescriptorFactory.fromResource(R.drawable.carrefourpin)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    String dist;


                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
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
                                    repeatTTS.speak("Voici le bureau sonede le plus proche de vous, il est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                }
                                else {
                                    repeatTTS.speak("Le bureau de sonede le plus proche de vous se trouve exactement à "+ finalAddress, TextToSpeech.QUEUE_ADD, null);
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

    public void DrawMuseum(final Double Userlongitude, final Double Userlatitude,final GoogleMap  googleMap){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Musee");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float minDistance = 90000000 ;
                Marker nearestPoste = null;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    String ville = zoneSnapshot.child("Ville").getValue(String.class);
                    longitude = Double.parseDouble(zoneSnapshot.child("Latitude").getValue(String.class));
                    latitude = Double.parseDouble(zoneSnapshot.child("Longitude").getValue(String.class));


                    mMap = googleMap;
                    LatLng positionMarker = new LatLng(latitude, longitude);
                    marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ville).icon(BitmapDescriptorFactory.fromResource(R.drawable.museumpin)));

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
                        nearestPoste = marker;
                    }
                }
                if (nearestPoste != null){


                    LatLng you = new LatLng(Userlatitude, Userlongitude);
                    marker = mMap.addMarker(new MarkerOptions().position(you).title("Toi").icon(BitmapDescriptorFactory.fromResource(R.drawable.maplaceholder)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(you));

                    LatLng positionMarker = new LatLng(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude);
                    mMap.addMarker(new MarkerOptions().position(positionMarker).title("Sonede").icon(BitmapDescriptorFactory.fromResource(R.drawable.museumpin)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    String dist;


                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
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
                                    repeatTTS.speak("Voici le bureau sonede le plus proche de vous, il est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                }
                                else {
                                    repeatTTS.speak("Le bureau de sonede le plus proche de vous se trouve exactement à "+ finalAddress, TextToSpeech.QUEUE_ADD, null);
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

    public void DrawMonoprix(final Double Userlongitude, final Double Userlatitude,final GoogleMap  googleMap){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Monoprix");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float minDistance = 90000000 ;
                Marker nearestPoste = null;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    String ville = zoneSnapshot.child("Ville").getValue(String.class);
                    longitude = Double.parseDouble(zoneSnapshot.child("Longitude").getValue(String.class));
                    latitude = Double.parseDouble(zoneSnapshot.child("Latitude").getValue(String.class));


                    mMap = googleMap;
                    LatLng positionMarker = new LatLng(latitude, longitude);
                    marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ville).icon(BitmapDescriptorFactory.fromResource(R.drawable.monoprixpix)));

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
                        nearestPoste = marker;
                    }
                }
                if (nearestPoste != null){


                    LatLng you = new LatLng(Userlatitude, Userlongitude);
                    marker = mMap.addMarker(new MarkerOptions().position(you).title("Toi").icon(BitmapDescriptorFactory.fromResource(R.drawable.maplaceholder)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(you));

                    LatLng positionMarker = new LatLng(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude);
                    mMap.addMarker(new MarkerOptions().position(positionMarker).title("Sonede").icon(BitmapDescriptorFactory.fromResource(R.drawable.monoprixpix)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    String dist;


                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
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
                                    repeatTTS.speak("Voici le bureau sonede le plus proche de vous, il est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                }
                                else {
                                    repeatTTS.speak("Le bureau de sonede le plus proche de vous se trouve exactement à "+ finalAddress, TextToSpeech.QUEUE_ADD, null);
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

    public void DrawMG(final Double Userlongitude, final Double Userlatitude,final GoogleMap  googleMap){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("MG");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float minDistance = 90000000 ;
                Marker nearestPoste = null;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    String ville = zoneSnapshot.child("Ville").getValue(String.class);
                    longitude = Double.parseDouble(zoneSnapshot.child("Longitude").getValue(String.class));
                    latitude = Double.parseDouble(zoneSnapshot.child("Latitude").getValue(String.class));

                    mMap = googleMap;
                    LatLng positionMarker = new LatLng(latitude, longitude);
                    marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ville).icon(BitmapDescriptorFactory.fromResource(R.drawable.mgpin)));

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
                        nearestPoste = marker;
                    }
                }
                if (nearestPoste != null){


                    LatLng you = new LatLng(Userlatitude, Userlongitude);
                    marker = mMap.addMarker(new MarkerOptions().position(you).title("Toi").icon(BitmapDescriptorFactory.fromResource(R.drawable.maplaceholder)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(you));

                    LatLng positionMarker = new LatLng(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude);
                    mMap.addMarker(new MarkerOptions().position(positionMarker).title("Sonede").icon(BitmapDescriptorFactory.fromResource(R.drawable.mgpin)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    String dist;


                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
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
                                    repeatTTS.speak("Voici le bureau sonede le plus proche de vous, il est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                }
                                else {
                                    repeatTTS.speak("Le bureau de sonede le plus proche de vous se trouve exactement à "+ finalAddress, TextToSpeech.QUEUE_ADD, null);
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

    public void DrawAziza(final Double Userlongitude, final Double Userlatitude,final GoogleMap  googleMap){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Aziza");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float minDistance = 90000000 ;
                Marker nearestPoste = null;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    String ville = zoneSnapshot.child("Ville").getValue(String.class);
                    longitude = Double.parseDouble(zoneSnapshot.child("Longitude").getValue(String.class));
                    latitude = Double.parseDouble(zoneSnapshot.child("Latitude").getValue(String.class));
                    mMap = googleMap;
                    LatLng positionMarker = new LatLng(latitude, longitude);
                    marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ville).icon(BitmapDescriptorFactory.fromResource(R.drawable.azizapin)));

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
                        nearestPoste = marker;
                    }
                }
                if (nearestPoste != null){


                    LatLng you = new LatLng(Userlatitude, Userlongitude);
                    marker = mMap.addMarker(new MarkerOptions().position(you).title("Toi").icon(BitmapDescriptorFactory.fromResource(R.drawable.maplaceholder)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(you));

                    LatLng positionMarker = new LatLng(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude);
                    mMap.addMarker(new MarkerOptions().position(positionMarker).title("Sonede").icon(BitmapDescriptorFactory.fromResource(R.drawable.azizapin)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    String dist;


                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
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
                                    repeatTTS.speak("Voici le bureau sonede le plus proche de vous, il est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                }
                                else {
                                    repeatTTS.speak("Le bureau de sonede le plus proche de vous se trouve exactement à "+ finalAddress, TextToSpeech.QUEUE_ADD, null);
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


    public void DrawHopital(final Double Userlongitude, final Double Userlatitude,final GoogleMap  googleMap){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Hopital");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float minDistance = 90000000 ;
                Marker nearestPoste = null;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    String nom = zoneSnapshot.child("Nom").getValue(String.class);
                    longitude = Double.parseDouble(zoneSnapshot.child("Longitude").getValue(String.class));
                    latitude = Double.parseDouble(zoneSnapshot.child("Latitude").getValue(String.class));
                    mMap = googleMap;
                    LatLng positionMarker = new LatLng(latitude, longitude);
                    marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(nom).icon(BitmapDescriptorFactory.fromResource(R.drawable.sbitar)));

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
                        nearestPoste = marker;
                    }
                }
                if (nearestPoste != null){


                    LatLng you = new LatLng(Userlatitude, Userlongitude);
                    marker = mMap.addMarker(new MarkerOptions().position(you).title("Toi").icon(BitmapDescriptorFactory.fromResource(R.drawable.maplaceholder)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(you));

                    LatLng positionMarker = new LatLng(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude);
                    mMap.addMarker(new MarkerOptions().position(positionMarker).title(nearestPoste.getTitle()).icon(BitmapDescriptorFactory.fromResource(R.drawable.sbitar)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    String dist;


                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
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
                                    repeatTTS.speak("Voici le bureau sonede le plus proche de vous, il est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                }
                                else {
                                    repeatTTS.speak("Le bureau de sonede le plus proche de vous se trouve exactement à "+ finalAddress, TextToSpeech.QUEUE_ADD, null);
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


    public void DrawPharmacie(final Double Userlongitude, final Double Userlatitude,final GoogleMap  googleMap){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("pharmacie");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float minDistance = 90000000 ;
                Marker nearestPoste = null;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                    longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                    latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                    mMap = googleMap;
                    LatLng positionMarker = new LatLng(latitude, longitude);
                    marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title("Pharmacie").icon(BitmapDescriptorFactory.fromResource(R.drawable.hopitalpin)));

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
                        nearestPoste = marker;
                    }
                }
                if (nearestPoste != null){


                    LatLng you = new LatLng(Userlatitude, Userlongitude);
                    marker = mMap.addMarker(new MarkerOptions().position(you).title("Toi").icon(BitmapDescriptorFactory.fromResource(R.drawable.maplaceholder)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(you));

                    LatLng positionMarker = new LatLng(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude);
                    mMap.addMarker(new MarkerOptions().position(positionMarker).title("Pharmacie").icon(BitmapDescriptorFactory.fromResource(R.drawable.hopitalpin)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    String dist;


                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
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
                                    repeatTTS.speak("Voici le bureau sonede le plus proche de vous, il est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                }
                                else {
                                    repeatTTS.speak("Le bureau de sonede le plus proche de vous se trouve exactement à "+ finalAddress, TextToSpeech.QUEUE_ADD, null);
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


    public void DrawCinema(final Double Userlongitude, final Double Userlatitude,final GoogleMap  googleMap){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("cinema");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float minDistance = 90000000 ;
                Marker nearestPoste = null;
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                    String nom = zoneSnapshot.child("nom").getValue(String.class);
                    longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                    latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                    mMap = googleMap;
                    LatLng positionMarker = new LatLng(latitude, longitude);
                    marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(nom).icon(BitmapDescriptorFactory.fromResource(R.drawable.moviepin)));

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
                        nearestPoste = marker;
                    }
                }
                if (nearestPoste != null){


                    LatLng you = new LatLng(Userlatitude, Userlongitude);
                    marker = mMap.addMarker(new MarkerOptions().position(you).title("Toi").icon(BitmapDescriptorFactory.fromResource(R.drawable.maplaceholder)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(you));

                    LatLng positionMarker = new LatLng(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude);
                    mMap.addMarker(new MarkerOptions().position(positionMarker).title("Pharmacie").icon(BitmapDescriptorFactory.fromResource(R.drawable.moviepin)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

                    String dist;


                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(nearestPoste.getPosition().latitude, nearestPoste.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
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
                                    repeatTTS.speak("Voici le bureau sonede le plus proche de vous, il est "+ finalDist + " loin", TextToSpeech.QUEUE_ADD, null);
                                }
                                else {
                                    repeatTTS.speak("Le bureau de sonede le plus proche de vous se trouve exactement à "+ finalAddress, TextToSpeech.QUEUE_ADD, null);
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


}
