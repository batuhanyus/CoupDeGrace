package com.derebeylik.coupdegrace;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.*;
import java.io.FileOutputStream;
import java.io.Serializable;

import com.derebeylik.coupdegrace.BuildConfig;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity {


    DatabaseReference userdb;


    FirebaseAuth fbAuth;
    FirebaseAuth.AuthStateListener fbAuthListener;
    FirebaseUser user;

    Button loginBtn;
    Button registerBtn;
    EditText unameField;
    EditText passwordField;
    TextView infoText;
    RadioButton rememberMeToggle;
    EditText emailField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userdb = FirebaseDatabase.getInstance().getReference().child("Users");

        fbAuth=FirebaseAuth.getInstance();


        loginBtn=(Button)findViewById(R.id.btnLogin);
        registerBtn=(Button)findViewById(R.id.btnRegister);
        unameField = (EditText)findViewById(R.id.unameField);
        passwordField=(EditText)findViewById(R.id.passwordField);
        infoText=(TextView)findViewById(R.id.infoText);
        rememberMeToggle=(RadioButton)findViewById(R.id.rememberMeToggle);
        emailField=(EditText)findViewById(R.id.emailField);



        fbAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user=firebaseAuth.getCurrentUser();
            }
        };

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });


        LoadRememberMe();


    }

    @Override
    public void onStart()
    {
        super.onStart();
        fbAuth.addAuthStateListener(fbAuthListener);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if(fbAuthListener!=null)
            fbAuth.removeAuthStateListener(fbAuthListener);
    }

    void CreateAccount()
    {
        infoText.setText("Registering...");

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        fbAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    infoText.setText("Succesfuly registered.Logging in...");
                else
                    infoText.setText("Couldn't register.");

                Login();
            }
        });

    }

    void Login()
    {
        infoText.setText("Logging in...");

        String email=emailField.getText().toString();
        String password=passwordField.getText().toString();
        fbAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    infoText.setText("Succesfuly logged in.");
                    UpdateProfile(unameField.getText().toString());

                    //Proceed Main Activity.
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                }
                else
                {
                    infoText.setText("Couldn't log in." + task.getException().toString());
                }

            }
        });

        if(rememberMeToggle.isChecked())
            SaveRememberMe();
    }


    void UpdateProfile(String newUsername)
    {
        UserProfileChangeRequest req=new UserProfileChangeRequest.Builder().setDisplayName(newUsername).build();

        user.updateProfile(req).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    void SaveRememberMe()
    {
        try
        {
            RememberMe obj = new RememberMe(emailField.getText().toString(),passwordField.getText().toString(),unameField.getText().toString());

            FileOutputStream stream=openFileOutput("rememberMe.coup",MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(stream);
            os.writeObject(obj);
            os.close();
            stream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    void LoadRememberMe()
    {
        try
        {
            RememberMe obj;

            FileInputStream fin = openFileInput("rememberMe.coup");
            ObjectInputStream is = new ObjectInputStream(fin);
            obj = (RememberMe)is.readObject();
            is.close();
            fin.close();

            emailField.setText(obj.email);
            passwordField.setText(obj.password);
            unameField.setText(obj.displayName);
            rememberMeToggle.setChecked(true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class RememberMe implements Serializable{
    public String email;
    public String password;
    public String displayName;

    public RememberMe(String _email,String _password,String _displayName)
    {
        email=_email;
        password=_password;
        displayName=_displayName;
    }
}

