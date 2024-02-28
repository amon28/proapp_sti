package com.example.proapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Student_Activity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Button logout_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
    }

    @Override
    public void onStart(){
        super.onStart();

        logout_button = (Button) findViewById(R.id.log_out);
        mAuth = FirebaseAuth.getInstance();


        logout_button.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(Student_Activity.this, MainActivity.class));
            finish();
        });
    }
}