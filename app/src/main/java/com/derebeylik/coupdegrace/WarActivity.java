package com.derebeylik.coupdegrace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.ActionMenuView.LayoutParams;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.xmlpull.v1.XmlPullParser.TYPES;

public class WarActivity extends AppCompatActivity {


    FirebaseUser usr;

    List<HelpRequest> requests = new ArrayList<HelpRequest>();

    DatabaseReference requestdb = FirebaseDatabase.getInstance().getReference().child("HelpRequests");


    HelpRequest selectedRequest;
    DataSnapshot currentDBSnapshot;

    TextView requestDetails;
    LinearLayout requestsRow;
    Button sendRequestButton;
    Button helpSentBtn;
    Button cancelRequestBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_war);

        usr= FirebaseAuth.getInstance().getCurrentUser();

        requestDetails=(TextView)findViewById(R.id.requestDetails);
        requestsRow=(LinearLayout)findViewById(R.id.requestsRow);
        sendRequestButton=(Button)findViewById(R.id.sendRequestBtn);
        helpSentBtn=(Button)findViewById(R.id.helpSentBtn);
        cancelRequestBtn=(Button)findViewById(R.id.cancelRequestBtn);


        requestdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requests.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    HelpRequest req = ds.getValue(HelpRequest.class);
                    requests.add(req);
                }

                currentDBSnapshot=dataSnapshot;
                PopulateRequestsList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),HelpRequestFormActivity.class);
                startActivity(i);
            }
        });

        helpSentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(DataSnapshot child:currentDBSnapshot.getChildren())
                {
                    if(child.child("cityName").getValue().toString()==selectedRequest.cityName)
                    {
                        HelpRequest req = child.getValue(HelpRequest.class);
                        List<String> ls=req.helpers;
                        ls.add(usr.getDisplayName());

                        requestdb.child(child.getKey()).child("helpers").setValue(ls);
                    }
                }
            }
        });

        cancelRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(DataSnapshot child:currentDBSnapshot.getChildren())
                {
                    if(child.child("cityName").getValue().toString()==selectedRequest.cityName)
                    {
                        //ID check.
                        if(child.child("requester").getValue().toString().equals(usr.getDisplayName()) || usr.getDisplayName().equals("Derebeyi"))
                        {
                            child.getRef().removeValue();
                        }
                    }
                }
            }
        });


    }

    void PopulateRequestsList()
    {
        requestsRow.removeAllViews();

        for(final HelpRequest req:requests)
        {
            final Button btn = new Button(this);

            btn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            btn.setText(req.cityName);
            btn.setId(requests.indexOf(req));

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopulateRequestDetails(btn.getId());
                }
            });

            requestsRow.addView(btn);
        }
    }

    void PopulateRequestDetails(int index)
    {
        HelpRequest req = requests.get(index);
        selectedRequest = req;

        String s="Reinforcement Request";
        s+="\n" +"City: " + req.cityName;
        s+="\n" +"Requester: "+req.requester;
        s+="\n"+"Coordinates: " + req.coordinates;
        s+="\n"+"Info: " +req.info;
        s+="\n"+"Reinforcements: ";
        for(String helper : req.helpers)
        {
            s+=helper+",";
        }

        requestDetails.setText(s);
    }
}





