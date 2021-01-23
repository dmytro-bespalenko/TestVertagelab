package com.hfad.testvertagelab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button logInButt = findViewById(R.id.buttonLogIn);
        logInButt.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        EditText etEmail = findViewById(R.id.editTextTextEmailAddress);
                        EditText etPass = findViewById(R.id.editTextTextPassword);
                        String email = etEmail.getText().toString();
                        String pass = etPass.getText().toString();

                        if (!validate(email)) {
                            etEmail.setError("Invalid email");
                            return;
                        }

                        if (pass.length() < 1) {
                            etPass.setError("Invalid password");
                            return;
                        }

                        Intent intent = new Intent(MainActivity.this, MapScreenActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    }
                });
    }

    private static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}