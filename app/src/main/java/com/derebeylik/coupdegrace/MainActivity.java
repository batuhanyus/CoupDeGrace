package com.derebeylik.coupdegrace;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.derebeylik.coupdegrace.BuildConfig;

public class MainActivity extends AppCompatActivity {

    public FirebaseUser user;

    Button chatBtn;
    Button warRoomBtn;
    Button settingsBtn;
    TextView versionText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatBtn=(Button)findViewById(R.id.chatBtn);
        warRoomBtn=(Button)findViewById(R.id.warRoomBtn);
        settingsBtn=(Button)findViewById(R.id.settingsBtn);
        versionText=(TextView)findViewById(R.id.versionText);



        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ChatActivity.class);
                startActivity(i);
            }
        });

        warRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),WarActivity.class);
                startActivity(i);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Version check.
        FirebaseDatabase.getInstance().getReference().child("version").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int onlineVersion = Integer.parseInt(dataSnapshot.getValue().toString());
                int version = BuildConfig.VERSION_CODE;

                if(onlineVersion>version){
                    versionText.setText(Html.fromHtml(
                            "<a href=\"https://drive.google.com/open?id=0B0pGipbEty8YWEZsVnJSTHBTWFU\">Update Available!</a> "));
                    versionText.setMovementMethod(LinkMovementMethod.getInstance());
                    versionText.setTextSize(32f);
                }

                if(onlineVersion==version) {
                    versionText.setText("v" + BuildConfig.VERSION_NAME);
                    versionText.setTextSize(12f);
                }

                if(onlineVersion<version) {
                    dataSnapshot.getRef().setValue(version);
                    versionText.setTextSize(12f);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}


