package com.example.badr.happysync;

import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by badr on 3/4/19.
 */

public class SimulatorGPS {



    int compteur = -1;
    int sens = 0;
    int time = 5;
    int x = 0 ;
    Vector<String> DateArrive = new Vector<String>();
    public  SimulatorGPS(){


    }


    public void simulateBUS(){
        final Vector<Double> latitudes = new Vector<Double>();
        final Vector<Double> longitudes = new Vector<Double>();

            latitudes.add(36.81973511);
            latitudes.add(36.82062);
            latitudes.add(36.82153);
            latitudes.add(36.82239);
            latitudes.add(36.82318011);
            latitudes.add(36.82393);
            latitudes.add(36.82483);
            latitudes.add(36.82543);
            latitudes.add(36.82624);
            latitudes.add(36.82658);
            latitudes.add(36.82654);
            latitudes.add(36.82629);
            latitudes.add(36.82596);
            latitudes.add(36.82575);
            latitudes.add(36.82538);
            latitudes.add(36.82512);
            latitudes.add(36.82463542);
            //-------------------------
            longitudes.add(10.12453155);
            longitudes.add(10.12409);
            longitudes.add(10.12361);
            longitudes.add(10.12312);
            longitudes.add(10.12274869);
            longitudes.add(10.12233);
            longitudes.add(10.12177);
            longitudes.add(10.12142);
            longitudes.add(10.12102);
            longitudes.add(10.1205);
            longitudes.add(10.12007);
            longitudes.add(10.11954);
            longitudes.add(10.11883);
            longitudes.add(10.11833);
            longitudes.add(10.11831);
            longitudes.add(10.11853);
            longitudes.add(10.11886695);


        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);


        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {

                if (sens==0){
                    compteur++;
                    if (latitudes.elementAt(compteur)==10.11886695){
                        time = 20;
                    }
                    else if (latitudes.elementAt(compteur)==10.12274869|| latitudes.elementAt(compteur)== 10.12453155){
                        time = 10 ;
                    }
                    else{
                        time = 5;
                    }

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();

                    String node = "bus1";

                    DatabaseReference ref = database.getReference().child("Bus");
                    ref.child(node).child("latitude").setValue(latitudes.elementAt(compteur).toString());
                    ref.child(node).child("longitude").setValue(longitudes.elementAt(compteur).toString());
                    if (compteur == longitudes.size()-1){

                        ref.child(node).child("sens").setValue("retour");
                        compteur++;
                        sens = 1;
                    }
                }
                else{
                    compteur--;
                    if (latitudes.elementAt(compteur)==10.11886695){
                        time = 20;
                    }
                    else if (latitudes.elementAt(compteur)==10.12274869|| latitudes.elementAt(compteur)== 10.12453155){
                        time = 10 ;
                    }
                    else{
                        time = 5;
                    }

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("Bus");
                    String node = "bus1";
                    ref.child(node).child("latitude").setValue(latitudes.elementAt(compteur).toString());
                    ref.child(node).child("longitude").setValue(longitudes.elementAt(compteur).toString());
                    if (compteur==0){
                        compteur--;
                        ref.child(node).child("sens").setValue("aller");
                        sens=0;
                    }

                }


            }
        }, 0, time, TimeUnit.SECONDS);
    }

    public void simulateMetro(){
        final Vector<Double> latitudes = new Vector<Double>();
        final Vector<Double> longitudes = new Vector<Double>();
        latitudes.add(36.80649);
        latitudes.add(36.81248);
        latitudes.add(36.81568);
        latitudes.add(36.82001);
        latitudes.add(36.82346);
        latitudes.add(36.82923);
        latitudes.add(36.83329);
        latitudes.add(36.83852);
        latitudes.add(36.84428);
        latitudes.add(36.84699);
        latitudes.add(36.85441);
        latitudes.add(36.85979);
        //-------------------------
        longitudes.add(10.18081);
        longitudes.add(10.18333);
        longitudes.add(10.18357);
        longitudes.add(10.18206);
        longitudes.add(10.18556);
        longitudes.add(10.19037);
        longitudes.add(10.18277);
        longitudes.add(10.18196);
        longitudes.add(10.18406);
        longitudes.add(10.19237);
        longitudes.add(10.19591);
        longitudes.add(10.19748);

        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);


        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {

                if (sens==0){
                    compteur++;
                    if (longitudes.elementAt(compteur)==10.18081 ||longitudes.elementAt(compteur)==10.19748 ){
                        time = 15;
                    }

                    else{
                        time = 8;
                    }

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();

                    DatabaseReference ref = database.getReference().child("Metro");
                    ref.child("metro2").child("latitude").setValue(latitudes.elementAt(compteur).toString());
                    ref.child("metro2").child("longitude").setValue(longitudes.elementAt(compteur).toString());
                    if (compteur == longitudes.size()-1){

                        ref.child("metro2").child("sens").setValue("retour");
                        compteur++;
                        sens = 1;
                    }
                }
                else{
                    compteur--;
                    if (longitudes.elementAt(compteur)==10.18081 ||longitudes.elementAt(compteur)==10.19748 ){
                        time = 15;
                    }

                    else{
                        time = 8;
                    }

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("Metro");
                    String node = "metro2";
                    ref.child(node).child("latitude").setValue(latitudes.elementAt(compteur).toString());
                    ref.child(node).child("longitude").setValue(longitudes.elementAt(compteur).toString());
                    if (compteur==0){
                        compteur--;
                        ref.child(node).child("sens").setValue("aller");
                        sens=0;
                    }

                }


            }
        }, 0, time, TimeUnit.SECONDS);
    }


    int compteurMetro = -1 ;
    int sens2  = 0 ;
    public void simulateMetro2(){


        final Vector<Double> latitudes = new Vector<Double>();
        final Vector<Double> longitudes = new Vector<Double>();

        latitudes.add(36.85979);
        latitudes.add(36.85441);
        latitudes.add(36.84699);
        latitudes.add(36.84428);
        latitudes.add(36.83852);
        latitudes.add(36.83329);
        latitudes.add(36.82923);
        latitudes.add(36.82346);
        latitudes.add(36.82001);
        latitudes.add(36.81568);
        latitudes.add(36.81248);
        latitudes.add(36.80649);
        //-------------------------
        longitudes.add(10.19748);
        longitudes.add(10.19591);
        longitudes.add(10.19237);
        longitudes.add(10.18406);
        longitudes.add(10.18196);
        longitudes.add(10.18277);
        longitudes.add(10.19037);
        longitudes.add(10.18556);
        longitudes.add(10.18206);
        longitudes.add(10.18357);
        longitudes.add(10.18333);
        longitudes.add(10.18081);

        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);


        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {

                if (sens2==0){
                    compteurMetro++;
                    if (longitudes.elementAt(compteur)==10.18081 ||longitudes.elementAt(compteurMetro)==10.19748 ){
                        time = 15;
                    }

                    else{
                        time = 8;
                    }

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();

                    DatabaseReference ref = database.getReference().child("Metro");
                    ref.child("metro22").child("latitude").setValue(latitudes.elementAt(compteurMetro).toString());
                    ref.child("metro22").child("longitude").setValue(longitudes.elementAt(compteurMetro).toString());
                    if (compteurMetro == longitudes.size()-1){
                        ref.child("metro22").child("sens").setValue("retour");
                        compteurMetro++;
                        sens2 = 1;
                    }
                }
                else{
                    compteurMetro--;
                    if (longitudes.elementAt(compteurMetro)==10.18081 ||longitudes.elementAt(compteurMetro)==10.19748 ){
                        time = 15;
                    }

                    else{
                        time = 8;
                    }

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("Metro");
                    String node = "metro22";
                    ref.child(node).child("latitude").setValue(latitudes.elementAt(compteurMetro).toString());
                    ref.child(node).child("longitude").setValue(longitudes.elementAt(compteurMetro).toString());
                    if (compteurMetro==0){
                        compteurMetro--;
                        ref.child(node).child("sens").setValue("aller");
                        sens2=0;
                    }

                }


            }
        }, 0, time, TimeUnit.SECONDS);
    }




    public void simulateMetro3(){
        final Vector<Double> latitudes = new Vector<Double>();
        final Vector<Double> longitudes = new Vector<Double>();
        latitudes.add(36.80649);
        latitudes.add(36.81248);
        latitudes.add(36.81568);
        latitudes.add(36.82001);
        latitudes.add(36.82346);
        latitudes.add(36.82923);
        latitudes.add(36.83329);
        latitudes.add(36.83852);
        latitudes.add(36.84428);
        latitudes.add(36.84699);
        latitudes.add(36.85441);
        latitudes.add(36.85979);
        //-------------------------
        longitudes.add(10.18081);
        longitudes.add(10.18333);
        longitudes.add(10.18357);
        longitudes.add(10.18206);
        longitudes.add(10.18556);
        longitudes.add(10.19037);
        longitudes.add(10.18277);
        longitudes.add(10.18196);
        longitudes.add(10.18406);
        longitudes.add(10.19237);
        longitudes.add(10.19591);
        longitudes.add(10.19748);

        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);


        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {

                if (sens==0){
                    compteur++;
                    if (longitudes.elementAt(compteur)==10.18081 ||longitudes.elementAt(compteur)==10.19748 ){
                        time = 15;
                    }

                    else{
                        time = 8;
                    }

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();

                    DatabaseReference ref = database.getReference().child("Metro");
                    ref.child("metro23").child("latitude").setValue(latitudes.elementAt(compteur).toString());
                    ref.child("metro23").child("longitude").setValue(longitudes.elementAt(compteur).toString());
                    if (compteur == longitudes.size()-1){

                        ref.child("metro2").child("sens").setValue("retour");
                        compteur++;
                        sens = 1;
                    }
                }
                else{
                    compteur--;
                    if (latitudes.elementAt(compteur)==10.18081 ||latitudes.elementAt(compteur)==10.19748 ){
                        time = 15;
                    }

                    else{
                        time = 8;
                    }

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("Metro");
                    String node = "metro23";
                    ref.child(node).child("latitude").setValue(latitudes.elementAt(compteur).toString());
                    ref.child(node).child("longitude").setValue(longitudes.elementAt(compteur).toString());
                    if (compteur==0){
                        compteur--;
                        ref.child(node).child("sens").setValue("aller");
                        sens=0;
                    }

                }


            }
        }, 0, time, TimeUnit.SECONDS);
    }



    public void simulateMetro4(){
        final Vector<Double> latitudes = new Vector<Double>();
        final Vector<Double> longitudes = new Vector<Double>();
        latitudes.add(36.80649);
        latitudes.add(36.81248);
        latitudes.add(36.81568);
        latitudes.add(36.82001);
        latitudes.add(36.82346);
        latitudes.add(36.82923);
        latitudes.add(36.83329);
        latitudes.add(36.83852);
        latitudes.add(36.84428);
        latitudes.add(36.84699);
        latitudes.add(36.85441);
        latitudes.add(36.85979);
        //-------------------------
        longitudes.add(10.18081);
        longitudes.add(10.18333);
        longitudes.add(10.18357);
        longitudes.add(10.18206);
        longitudes.add(10.18556);
        longitudes.add(10.19037);
        longitudes.add(10.18277);
        longitudes.add(10.18196);
        longitudes.add(10.18406);
        longitudes.add(10.19237);
        longitudes.add(10.19591);
        longitudes.add(10.19748);

        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);


        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {

                if (sens==0){
                    compteur++;
                    if (longitudes.elementAt(compteur)==10.18081 ||longitudes.elementAt(compteur)==10.19748 ){
                        time = 15;
                    }

                    else{
                        time = 8;
                    }

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();

                    DatabaseReference ref = database.getReference().child("Metro");
                    ref.child("metro24").child("latitude").setValue(latitudes.elementAt(compteur).toString());
                    ref.child("metro24").child("longitude").setValue(longitudes.elementAt(compteur).toString());
                    if (compteur == longitudes.size()-1){

                        ref.child("metro2").child("sens").setValue("retour");
                        compteur++;
                        sens = 1;
                    }
                }
                else{
                    compteur--;
                    if (latitudes.elementAt(compteur)==10.18081 ||latitudes.elementAt(compteur)==10.19748 ){
                        time = 15;
                    }

                    else{
                        time = 8;
                    }

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("Metro");
                    String node = "metro24";
                    ref.child(node).child("latitude").setValue(latitudes.elementAt(compteur).toString());
                    ref.child(node).child("longitude").setValue(longitudes.elementAt(compteur).toString());
                    if (compteur==0){
                        compteur--;
                        ref.child(node).child("sens").setValue("aller");
                        sens=0;
                    }

                }


            }
        }, 0, time, TimeUnit.SECONDS);
    }




    public void simulateMetro5(){
        final Vector<Double> latitudes = new Vector<Double>();
        final Vector<Double> longitudes = new Vector<Double>();
        latitudes.add(36.80649);
        latitudes.add(36.81248);
        latitudes.add(36.81568);
        latitudes.add(36.82001);
        latitudes.add(36.82346);
        latitudes.add(36.82923);
        latitudes.add(36.83329);
        latitudes.add(36.83852);
        latitudes.add(36.84428);
        latitudes.add(36.84699);
        latitudes.add(36.85441);
        latitudes.add(36.85979);
        //-------------------------
        longitudes.add(10.18081);
        longitudes.add(10.18333);
        longitudes.add(10.18357);
        longitudes.add(10.18206);
        longitudes.add(10.18556);
        longitudes.add(10.19037);
        longitudes.add(10.18277);
        longitudes.add(10.18196);
        longitudes.add(10.18406);
        longitudes.add(10.19237);
        longitudes.add(10.19591);
        longitudes.add(10.19748);

        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);


        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {

                if (sens==0){
                    compteur++;
                    if (longitudes.elementAt(compteur)==10.18081 ||longitudes.elementAt(compteur)==10.19748 ){
                        time = 15;
                    }

                    else{
                        time = 8;
                    }

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();

                    DatabaseReference ref = database.getReference().child("Metro");
                    ref.child("metro25").child("latitude").setValue(latitudes.elementAt(compteur).toString());
                    ref.child("metro25").child("longitude").setValue(longitudes.elementAt(compteur).toString());
                    if (compteur == longitudes.size()-1){

                        ref.child("metro25").child("sens").setValue("retour");
                        compteur++;
                        sens = 1;
                    }
                }
                else{
                    compteur--;
                    if (latitudes.elementAt(compteur)==10.18081 ||latitudes.elementAt(compteur)==10.19748 ){
                        time = 15;
                    }

                    else{
                        time = 8;
                    }

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("Metro");
                    String node = "metro25";
                    ref.child(node).child("latitude").setValue(latitudes.elementAt(compteur).toString());
                    ref.child(node).child("longitude").setValue(longitudes.elementAt(compteur).toString());
                    if (compteur==0){
                        compteur--;
                        ref.child(node).child("sens").setValue("aller");
                        sens=0;
                    }

                }


            }
        }, 0, time, TimeUnit.SECONDS);
    }
    public void simulateTGM(){
        final Vector<Double> latitudes = new Vector<Double>();
        final Vector<Double> longitudes = new Vector<Double>();
        latitudes.add(36.80069347);
        latitudes.add(36.81417755);
        latitudes.add(36.81805125);
        latitudes.add(36.81984487);
        latitudes.add(36.82420048);
        latitudes.add(36.82878318);
        latitudes.add(36.83617046);
        latitudes.add(36.8415416);
        latitudes.add(36.84612588);
        latitudes.add(36.85012217);
        latitudes.add(36.85339133);
        latitudes.add(36.86164887);
        latitudes.add(36.86658561);
        latitudes.add(36.87045373);
        latitudes.add(36.87574092);
        latitudes.add(36.88297713);
        latitudes.add(36.88304243);
        latitudes.add(36.83194722);

        //-------------------------
        longitudes.add(10.19174295);
        longitudes.add(10.2924504);
        longitudes.add(10.30193962);
        longitudes.add(10.30560139);
        longitudes.add(10.30877351);
        longitudes.add(10.3115529);
        longitudes.add(10.31660569);
        longitudes.add(10.31910447);
        longitudes.add(10.32184529);
        longitudes.add(10.32558066);
        longitudes.add(10.32891639);
        longitudes.add(10.33494266);
        longitudes.add(10.33744243);
        longitudes.add(10.34212619);
        longitudes.add(10.33967038);
        longitudes.add(10.33348709);
        longitudes.add(10.333452);
        longitudes.add(10.31387454);


        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);


        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {

                if (sens==0){
                    compteur++;
                    if (longitudes.elementAt(compteur)==10.19174295 ||longitudes.elementAt(compteur)==10.31387454 ){
                        time = 15;
                    }

                    else{
                        time = 8;
                    }

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();

                    DatabaseReference ref = database.getReference().child("TGMGPS");
                    ref.child("tgm1").child("latitude").setValue(latitudes.elementAt(compteur).toString());
                    ref.child("tgm1").child("longitude").setValue(longitudes.elementAt(compteur).toString());
                    if (compteur == longitudes.size()-1){

                        ref.child("tgm1").child("sens").setValue("retour");
                        compteur++;
                        sens = 1;
                    }
                }
                else{
                    compteur--;
                    if (longitudes.elementAt(compteur)==10.18081 ||longitudes.elementAt(compteur)==10.19748 ){
                        time = 15;
                    }

                    else{
                        time = 8;
                    }

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("TGMGPS");
                    String node = "tgm1";
                    ref.child(node).child("latitude").setValue(latitudes.elementAt(compteur).toString());
                    ref.child(node).child("longitude").setValue(longitudes.elementAt(compteur).toString());
                    if (compteur==0){
                        compteur--;
                        ref.child(node).child("sens").setValue("aller");
                        sens=0;
                    }

                }


            }
        }, 0, time, TimeUnit.SECONDS);
    }
}
