package com.example.okmanyiroda;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    EditText emailET;
    EditText passwordET;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;
    private NotificationHandler notificationHandler;
    private boolean permissionGranted;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailET = findViewById(R.id.emailEditText);
        passwordET = findViewById(R.id.passwordEditText);
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        notificationHandler = new NotificationHandler(this);

        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                permissionGranted = isGranted;
            });

    public void login(View view) {

        if (!permissionGranted) {
            Toast.makeText(this, "Engedélyezni kell a névjegyek olvasását!", Toast.LENGTH_LONG).show();
            return;
        }

        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        if (email.isEmpty()) {
            emailET.setError("Kötelező kitölteni!");
            return;
        }

        if (password.isEmpty()) {
            passwordET.setError("Kötelező kitölteni!");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(LOG_TAG, mAuth.getCurrentUser().getEmail() + " successfully logged in");
                    loggedIn();
                } else {
                    Log.i(LOG_TAG, email + " failed to log in");
                }
            }
        });
    }

    public void loggedIn() {
        notificationHandler.send("Sikeres bejelentkezés!");
        Intent intent = new Intent(this, SelectActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        if (!permissionGranted) {
            Toast.makeText(this, "Engedélyezni kell a névjegyek olvasását!", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", emailET.getText().toString());
        editor.putString("password", passwordET.getText().toString());
        editor.apply();
    }
}