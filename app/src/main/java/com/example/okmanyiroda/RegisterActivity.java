package com.example.okmanyiroda;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();

    EditText firstNameET;
    EditText lastNameET;
    EditText emailET;
    EditText passwordET;
    EditText passwordAgainET;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameET = findViewById(R.id.firstNameEditText);
        lastNameET = findViewById(R.id.lastNameEditText);
        emailET = findViewById(R.id.emailEditText);
        passwordET = findViewById(R.id.passwordEditText);
        passwordAgainET = findViewById(R.id.passwordAgainEditText);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String email = preferences.getString("email", "");
        String password = preferences.getString("password", "");

        emailET.setText(email);
        passwordET.setText(password);
        passwordAgainET.setText(password);

        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();
    }

    public void register(View view) {
        String firstName = firstNameET.getText().toString();
        String lastName = lastNameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordAgain = passwordAgainET.getText().toString();

        if (lastName.isEmpty()) {
            lastNameET.setError("Kötelező kitölteni!");
            return;
        }

        if (firstName.isEmpty()) {
            firstNameET.setError("Kötelező kitölteni!");
            return;
        }

        if (email.isEmpty()) {
            emailET.setError("Kötelező kitölteni!");
            return;
        }

        if (password.isEmpty()) {
            passwordET.setError("Kötelező kitölteni!");
            return;
        }

        if (passwordAgain.isEmpty()) {
            passwordAgainET.setError("Kötelező kitölteni!");
            return;
        }

        if (password.length() < 8) {
            passwordET.setError("A jelszónak minimum 8 karakter hosszúnak kell lennie!");
            return;
        }

        if (passwordAgain.length() < 8) {
            passwordAgainET.setError("A jelszónak minimum 8 karakter hosszúnak kell lennie!");
            return;
        }

        if (!password.equals(passwordAgain)) {
            Toast.makeText(RegisterActivity.this, "A megadott jelszavak nem egyeznek!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(LOG_TAG, mAuth.getCurrentUser() + " successfully registered");
                    finish();
                } else {
                    Log.i(LOG_TAG, email + " failed to register");
                }
            }
        });
    }

    public void cancel(View view) {
        finish();
    }
}