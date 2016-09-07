package com.development.austin.insight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.development.austin.insight.session.SessionManager;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LoginActivity extends AppCompatActivity {
    // Importing Views using Butterknife
    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;


    //Values
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SessionManager session = new SessionManager(getApplication());
        if(session.isLoggedIn()){
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        //Butterknife Injection
        ButterKnife.inject(this);
        //Initialize ImageView
        ImageView _imageView = (ImageView) findViewById(R.id.imageView);
        // Load Image
        Picasso.with(getBaseContext())
                .load(R.drawable.sdda)
                .placeholder(R.drawable.sdda)
                .into(_imageView);


        //Sign In On Click Button
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = _emailText.getText().toString();
                final String password = _passwordText.getText().toString();
                login(email, password);
            }
        });

        //Sign Up On Click Button
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

    }
    // Login Authentication Algorithm
    public void login(final String email, final String password) {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        //Firebase Initialization
        Firebase.setAndroidContext(this);
        Firebase ref = new Firebase("https://insight-16.firebaseio.com/users");
        Query queryRef = ref.orderByChild("email").equalTo(email);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                if (snapshot.child("password").getValue().toString().equals(password)) {
                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                    onLoginSuccess( snapshot.child("email").getValue().toString(),
                                    snapshot.child("name").getValue().toString(),
                                    snapshot.child("username").getValue().toString());
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Please check your password again!",Toast.LENGTH_SHORT).show();
                    onLoginFailed();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String email, String name, String username) {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        System.out.println(name);
        SessionManager session = new SessionManager(getApplication());
        session.createLoginSession(name,email,username);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
