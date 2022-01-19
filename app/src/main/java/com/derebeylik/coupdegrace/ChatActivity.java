package com.derebeylik.coupdegrace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class ChatActivity extends AppCompatActivity {

    private DatabaseReference db;
    FirebaseUser usr;

    TextView chatView;
    Button addMessageButton;
    TextView msgField;
    ScrollView scrolls;
    TextView infoText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        db= FirebaseDatabase.getInstance().getReference();

        chatView=(TextView)findViewById(R.id.chatView);
        addMessageButton=(Button)findViewById(R.id.addMsgBtn);
        msgField=(TextView)findViewById(R.id.msgField);
        infoText=(TextView)findViewById(R.id.infoText);
        scrolls=(ScrollView)findViewById(R.id.scrolls);

        usr= FirebaseAuth.getInstance().getCurrentUser();


        addMessageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    DatabaseReference chatLog = db.child("ChatLog");

                    ChatMessage msg = new ChatMessage();

                    if(TextUtils.isEmpty(usr.getDisplayName().toString()))
                        msg.sender="Nameless Peasant";
                    else
                        msg.sender=usr.getDisplayName();

                    msg.msg=msgField.getText().toString();

                    if(msg.msg.length() == 0)
                        throw new Exception("Error:Don't leave fields blank amk.");

                    chatLog.push().setValue(msg);

                    msgField.setText("");


                }
                catch (Exception e)
                {
                    chatView.setText(e.getMessage());
                }

            }
        });

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try
                {
                    //Get ChatLog and fill chatview
                    DataSnapshot ds=dataSnapshot.child("ChatLog");
                    if(!ds.exists())
                        throw new Exception("Error:Looks like I couldn't find what I'm looking for.");
                    chatView.setText("");
                    for (DataSnapshot msg: ds.getChildren())
                    {
                        String s = chatView.getText().toString();
                        DataSnapshot ds1 = msg.child("sender");
                        s+="\n" + ds1.getValue().toString() + ": "; //username
                        DataSnapshot ds2 = msg.child("msg");
                        s+=ds2.getValue().toString();
                        chatView.setText(s);
                    }

                    scrolls.post(new Runnable() {
                        @Override
                        public void run() {
                            scrolls.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
                catch (Exception e)
                {
                    chatView.setText("??"+e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                chatView.setText("err");
            }
        });
    }
}

class ChatMessage implements Serializable
{
    public String sender;
    public String msg;
}
