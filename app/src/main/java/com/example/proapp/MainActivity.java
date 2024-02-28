package com.example.proapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private LinearLayout login_layout;
    private Button LoginButton;
    private Button RegisterEmailButton;
    private CardView cView;
    private EditText email_text;
    private EditText password_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart(){
        super.onStart();

        LoginButton = (Button) findViewById(R.id.login_button);
        RegisterEmailButton = (Button) findViewById(R.id.register_email_button);
        cView = (CardView) findViewById(R.id.login_card_view);
        login_layout = (LinearLayout) findViewById(R.id.login_layout);
        email_text = (EditText) findViewById(R.id.email_input_text);
        password_text = (EditText) findViewById(R.id.password_input_text);

        mAuth = FirebaseAuth.getInstance();

        //Setup margin for card view containing login
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cView.getLayoutParams();
        params.setMargins(0,0,0,-1020);
        cView.setLayoutParams(params);

        //Animate card view to go up
        new CountDownTimer(100,10){
            public void onTick(long millisUntilFinished){}
            public void onFinish(){
                new CountDownTimer(500,10){
                    public void onTick(long x){
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cView.getLayoutParams();
                        int bottomValue = params.bottomMargin;
                        params.setMargins(0,0,0,bottomValue+21);
                        cView.setLayoutParams(params);
                    }

                    public void onFinish(){}
                }.start();

            }
        }.start();

        //Check if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && currentUser.isEmailVerified()){
            Toast.makeText(MainActivity.this, "Already Login", Toast.LENGTH_SHORT).show();
        }

        //Event Listener for login button
        login_views();


    }

    public void register_views(){
        RegisterEmailButton = (Button) findViewById(R.id.register_email_button);

        RegisterEmailButton.setOnClickListener(v -> {
            login_layout.removeAllViews();
            login_layout.addView(View.inflate(MainActivity.this, R.layout.register_sub_activity_main, null));

            ImageButton back_button = (ImageButton) findViewById(R.id.back_button);
            Button register_button = (Button) findViewById(R.id.register_button);
            email_text = (EditText) findViewById(R.id.register_email_input_text);
            password_text = (EditText) findViewById(R.id.register_password_input_text);

            mAuth = FirebaseAuth.getInstance();

            back_button.setOnClickListener(back ->{
                login_layout.removeAllViews();
                login_layout.addView(View.inflate(MainActivity.this,R.layout.login_sub_activity_main, null));
                login_views();
            });

            register_button.setOnClickListener(register -> {
                String register_email = email_text.getText().toString();
                String register_password = password_text.getText().toString();

                if(register_email.matches("") || register_password.matches("")){
                    Toast.makeText(MainActivity.this, "Empty Email or Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(register_email, register_password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification();
                            Toast.makeText(MainActivity.this, "Register Success!", Toast.LENGTH_SHORT).show();
                            login_views();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "User Already Exist!.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            });
        });
    }
    public void login_views(){
        login_layout.removeAllViews();
        login_layout.addView(View.inflate(MainActivity.this,R.layout.login_sub_activity_main, null));

        LoginButton = (Button) findViewById(R.id.login_button);
        email_text = (EditText) findViewById(R.id.email_input_text);
        password_text = (EditText) findViewById(R.id.password_input_text);

        LoginButton.setOnClickListener(v -> {
            //setContentView(R.layout.student_activity);
            //Log.i("t","t");
            String email = email_text.getText().toString();
            String password = password_text.getText().toString();

            if(email.matches("") || password.matches("")){
                Toast.makeText(MainActivity.this, "Empty Email or Password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d("tag", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user.isEmailVerified()){
                            startActivity(new Intent(MainActivity.this, Student_Activity.class));
                            finish();
                        }else{
                            Toast.makeText(MainActivity.this, "Email Not Verified", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(MainActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.w("tag", "signInWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }
                }
            });
        });
        //Event listener for register button
        register_views();
    }
}