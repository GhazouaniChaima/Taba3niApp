package com.example.badr.happysync;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class EventPage extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextToSpeech repeatTTS;
    Marker marker;
    Double longitude = -1.0 ;
    Double latitude= -1.0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    LatLng position;
    public LatLng getPlaceLocation(final String nom){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Places");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                    String ch = zoneSnapshot.child("nom").getValue(String.class);
                    if (ch.equals(nom)){
                        position = new LatLng(Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class)),Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class)));
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return  position;
    }

    public void drawMarker(final GoogleMap googleMap) {


            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference().child("events");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                        String ch = zoneSnapshot.child("nom").getValue(String.class);
                        final String location = zoneSnapshot.child("location").getValue(String.class);

                        Double longitude = Double.parseDouble(zoneSnapshot.child("longitude").getValue(String.class));
                        Double latitude = Double.parseDouble(zoneSnapshot.child("latitude").getValue(String.class));
                        LatLng positionMarker = new LatLng(latitude,longitude);

                        mMap = googleMap;
                        marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title(ch).icon(BitmapDescriptorFactory.fromResource(R.drawable.eventplaceholder)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }


    public void showImage(final String nom) {
        final Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });



        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("events");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                    final String ch = zoneSnapshot.child("nom").getValue(String.class);
                    if(ch.equals(nom)){

                        ImageView imageView = new ImageView(getApplicationContext());
                        String url = zoneSnapshot.child("url").getValue(String.class);
                        Picasso.with(getApplicationContext())
                                .load(url)
                                .resize(500, 800)
                                .into(imageView);
                        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                        builder.show();

                        final String type = zoneSnapshot.child("type").getValue(String.class);
                        final String location = zoneSnapshot.child("location").getValue(String.class);
                        final String horaire = zoneSnapshot.child("horaire").getValue(String.class);
                        final String date = zoneSnapshot.child("date").getValue(String.class);
                        repeatTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @SuppressLint("NewApi")
                            @Override
                            public void onInit(int i) {
                                if (i == TextToSpeech.SUCCESS) {
                                    repeatTTS.setLanguage(Locale.FRENCH);
                                    repeatTTS.speak("le "+type+" "+ch+" au "+location+" le "+date+" à "+horaire+" heure", TextToSpeech.QUEUE_ADD, null);
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



    @Override
    public void onMapReady(GoogleMap googleMap) {

        final EditText edt = (EditText) findViewById(R.id.searchBar2);
        final Double  longit = Double.parseDouble(getIntent().getStringExtra("longitude"));
        final Double lati = Double.parseDouble(getIntent().getStringExtra("latitude"));

        mMap = googleMap;
        LatLng positionMarker = new LatLng(lati, longit);
        marker = mMap.addMarker(new MarkerOptions().position(positionMarker).title("Toi").icon(BitmapDescriptorFactory.fromResource(R.drawable.boy)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(positionMarker));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

        drawMarker(mMap);


        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                }
                return false;
            }
        });



        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showImage(marker.getTitle());
                return false;
            }
        });
    }
}
