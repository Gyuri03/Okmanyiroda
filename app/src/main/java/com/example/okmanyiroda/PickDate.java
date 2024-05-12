package com.example.okmanyiroda;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PickDate extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String LOG_TAG = PickDate.class.getName();
    Spinner date_spinner;
    Spinner day_spinner;
    String selectedDate;
    String selectedDay;
    private FirebaseFirestore mFireStore;
    private CollectionReference dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_date);

        mFireStore = FirebaseFirestore.getInstance();
        dates = mFireStore.collection("dates");

        date_spinner = findViewById(R.id.dateSpinner);
        date_spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter_date = ArrayAdapter.createFromResource(this,
                R.array.dates, android.R.layout.simple_spinner_item);
        adapter_date.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        date_spinner.setAdapter(adapter_date);

        day_spinner = findViewById(R.id.daySpinner);
        day_spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter_day =ArrayAdapter.createFromResource(this,
                R.array.days, android.R.layout.simple_spinner_item);
        adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day_spinner.setAdapter(adapter_day);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getSelectedItem().toString().contains(":")) {
            selectedDate = parent.getSelectedItem().toString();
        } else {
            selectedDay = parent.getSelectedItem().toString();
        }
        // Log.i(LOG_TAG, "date: " + selectedDate + " day: " + selectedDay);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void bookDate(View view) {
        Map<String, Object> data = new HashMap<>();
        Date date = new Date(selectedDate, FirebaseAuth.getInstance().getCurrentUser().getEmail());
        data.put("dateObject", Arrays.asList(date.getDate_time(), date.getOwner()));
        dates.document(selectedDay).set(data, SetOptions.merge());

        Toast.makeText(this, "Sikeres időpontfoglalás!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, SelectActivity.class);
        startActivity(intent);
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, SelectActivity.class);
        startActivity(intent);
    }
}