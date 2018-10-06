package com.example.imran.vucommunication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton sendMessageButton;
    private EditText userMessageInput;
    private ScrollView mScrollView;
    private TextView displayTextMessage;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef , GroupNameRef ,GroupMessageKeyRef;

    private String currentGrooupName , currentUserID, currentUserName, currentDate, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        currentGrooupName = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(this, currentGrooupName, Toast.LENGTH_SHORT).show();

        mAuth=FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef =FirebaseDatabase.getInstance().getReference().child("Group").child(currentGrooupName);



        InitializeField();

        GetUserInfo();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessageInfoToDatabase();

                userMessageInput.setText("");

            }
        });
    }

    private void saveMessageInfoToDatabase() {
        String message= userMessageInput.getText().toString();
        String messageKey = GroupNameRef.push().getKey();


        if(TextUtils.isEmpty(message)){
            Toast.makeText(this, "At first enter your message", Toast.LENGTH_SHORT).show();
        }else {
            Calendar callForDate =Calendar.getInstance();
            SimpleDateFormat currentDateFormate = new SimpleDateFormat("MMM dd,yyyy");
            currentDate = currentDateFormate.format(callForDate.getTime());


            Calendar callForTime =Calendar.getInstance();
            SimpleDateFormat currentTimeFormate = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormate.format(callForTime.getTime());

            HashMap<String,Object> groupMessageKey = new HashMap<>();
            GroupNameRef.updateChildren(groupMessageKey);

            GroupMessageKeyRef = GroupNameRef.child(messageKey);

            HashMap<String, Object> messageInforMap = new HashMap<>();
            messageInforMap.put("name" ,currentUserName);
            messageInforMap.put("message", message);
            messageInforMap.put("date", currentDate);
            messageInforMap.put("time", currentTime);

            GroupMessageKeyRef.updateChildren(messageInforMap);

        }


    }


    private void InitializeField() {
        mToolbar = (Toolbar) findViewById(R.id.group_chat_bar_layout);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(currentGrooupName);

        sendMessageButton =(ImageButton)findViewById(R.id.send_message_button);
        userMessageInput =(EditText) findViewById(R.id.input_group_message);
        displayTextMessage =(TextView)findViewById(R.id.group_chat_text_display);
        mScrollView=(ScrollView) findViewById(R.id.my_scroll_view);


    }


    private void GetUserInfo() {

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    currentUserName = dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}























