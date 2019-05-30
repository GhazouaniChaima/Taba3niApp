package com.example.badr.happysync;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        final Spinner themeslist = (Spinner) findViewById(R.id.themeslist);
        List<String> list = new ArrayList<String>();
        list.add("bleu");
        list.add("violet");
        list.add("rouge");
        list.add("rose");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinneritem, list);
        dataAdapter.setDropDownViewResource(R.layout.spinneritem);
        themeslist.setAdapter(dataAdapter);

        /*Button okbtn = (Button) findViewById(R.id.savebtn);
                Button cancelbtn = (Button) findViewById(R.id.cancelbtn);
                final Button frbtn = (Button) findViewById(R.id.languagefr);
                final Button angbtn = (Button) findViewById(R.id.languageang);*/

    }
}
