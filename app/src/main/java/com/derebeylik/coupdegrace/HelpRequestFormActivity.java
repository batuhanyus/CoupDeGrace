package com.derebeylik.coupdegrace;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HelpRequestFormActivity extends AppCompatActivity {

    FirebaseUser usr;
    DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("HelpRequests");

    TextView infoText;
    EditText cityNameText;
    EditText coordinatesText;
    EditText requestInfoField;
    Button sendRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_request_form);

        usr= FirebaseAuth.getInstance().getCurrentUser();

        infoText= (TextView) findViewById(R.id.infoText);
        cityNameText=(EditText) findViewById(R.id.cityNameField);
        coordinatesText= (EditText) findViewById(R.id.coordinatesField);
        requestInfoField= (EditText) findViewById(R.id.requestInfoField);
        sendRequest= (Button) findViewById(R.id.sendHelpRequestButton);

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest.setClickable(false);

                HelpRequest req = new HelpRequest(cityNameText.getText().toString(),
                        usr.getDisplayName().toString(),
                        coordinatesText.getText().toString(),
                        requestInfoField.getText().toString(),
                        usr.getDisplayName());

                db.push().setValue(req).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            infoText.setText("Request sent.");
                            cityNameText.setText("");
                            coordinatesText.setText("");
                            requestInfoField.setText("");
                            sendRequest.setClickable(true);

                            Intent i = new Intent(getApplicationContext(),WarActivity.class);
                            startActivity(i);
                        }

                        else
                            infoText.setText("Error!Request couldn't send.");
                    }
                });
            }
        });
    }
}
