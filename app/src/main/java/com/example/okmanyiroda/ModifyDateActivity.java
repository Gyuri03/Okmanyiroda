package com.example.okmanyiroda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ModifyDateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String LOG_TAG = PickDate.class.getName();
    Spinner date_spinner;
    Spinner day_spinner;
    String selectedDate;
    String selectedDay;
    private FirebaseFirestore mFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_date);

        mFireStore = FirebaseFirestore.getInstance();
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

    public void cancel(View view) {
        Intent intent = new Intent(this, SelectActivity.class);
        startActivity(intent);
    }

    public void bookDate(View view) {
        Map<String, Object> data = new HashMap<>();
        Date date = new Date(selectedDate, FirebaseAuth.getInstance().getCurrentUser().getEmail());
        data.put("dateObject", Arrays.asList(date.getDate_time(), date.getOwner()));
        mFireStore.collection("dates").document(selectedDay).set(data, SetOptions.merge());
        Toast.makeText(this, "Sikeres időpont módosítás", Toast.LENGTH_LONG).show();
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
}