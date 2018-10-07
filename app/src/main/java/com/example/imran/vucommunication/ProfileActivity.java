package com.example.imran.vucommunication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {


    private TextView UserName, UserStatus, StudentId, StudentSession, StudentBatch ,StudentSection,StudentDept,StudentProgram;
    private CircleImageView userProfileImage;
    private Button SendMessaheRequestButton;

    private String receivedUserID, Current_State;
    private String senderUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef, ChatRequetRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        InitializeField();


        mAuth = FirebaseAuth.getInstance();
        receivedUserID = getIntent().getExtras().get("visit_user_id").toString();
        senderUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        ChatRequetRef = FirebaseDatabase.getInstance().getReference().child("Chat_Request");
        Current_State = "new";




        RetriveUsersInfo();

    }



    private void InitializeField() {


        UserName =(TextView) findViewById(R.id.set_user_name);
        UserStatus =(TextView) findViewById(R.id.set_profile_status);
        StudentId = (TextView) findViewById(R.id.set_student_id);
        StudentSection =(TextView) findViewById(R.id.set_user_Section);
        StudentBatch =(TextView) findViewById(R.id.set_user_batch);
        StudentSession = (TextView) findViewById(R.id.set_student_session);
        StudentDept =(TextView)findViewById(R.id.set_user_dept);
        StudentProgram=(TextView)findViewById(R.id.set_user_program);

        SendMessaheRequestButton =(Button)findViewById(R.id.send_message_request_button);

        userProfileImage = (CircleImageView) findViewById(R.id.set_profile_image);


    }


    private void RetriveUsersInfo(){
        RootRef.child("Users").child(receivedUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")  && (dataSnapshot.hasChild("image"))&&
                        (dataSnapshot.hasChild("deptName"))&& (dataSnapshot.hasChild("programName"))&&
                        (dataSnapshot.hasChild("studentBatch"))&& (dataSnapshot.hasChild("studentSession"))&&
                        (dataSnapshot.hasChild("student_section"))&& (dataSnapshot.hasChild("studentid"))) ){

                    String retriveUserName = dataSnapshot.child("name").getValue().toString();
                    String retriveStatus = dataSnapshot.child("status").getValue().toString();
                    String retriveDeptName = dataSnapshot.child("deptName").getValue().toString();
                    String retriveProgramName = dataSnapshot.child("programName").getValue().toString();
                    String retriveBatch = dataSnapshot.child("studentBatch").getValue().toString();
                    String retriveSession = dataSnapshot.child("studentSession").getValue().toString();
                    String retriveSection = dataSnapshot.child("student_section").getValue().toString();
                    String retriveSID = dataSnapshot.child("studentid").getValue().toString();
                    String retrivePrfileImage=dataSnapshot.child("image").getValue().toString();

                    UserName.setText(retriveUserName);
                    UserStatus.setText(retriveStatus);
                    StudentId.setText(retriveSID);
                    StudentSection.setText(retriveSection);
                    StudentBatch.setText(retriveBatch);
                    StudentSession.setText(retriveSession);
                    StudentDept.setText(retriveDeptName);
                    StudentProgram.setText(retriveProgramName);
                    Picasso.get().load(retrivePrfileImage).placeholder(R.drawable.profile).into(userProfileImage);

                    ManageChatRequests();



                }
                else if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))&&
                        (dataSnapshot.hasChild("deptName"))&& (dataSnapshot.hasChild("programName"))&&
                        (dataSnapshot.hasChild("studentBatch"))&& (dataSnapshot.hasChild("studentSession"))&&
                        (dataSnapshot.hasChild("student_section"))&& (dataSnapshot.hasChild("studentid"))){


                    String retriveUserName = dataSnapshot.child("name").getValue().toString();
                    String retriveStatus = dataSnapshot.child("status").getValue().toString();
                    String retriveDeptName = dataSnapshot.child("deptName").getValue().toString();
                    String retriveProgramName = dataSnapshot.child("programName").getValue().toString();
                    String retriveBatch = dataSnapshot.child("studentBatch").getValue().toString();
                    String retriveSession = dataSnapshot.child("studentSession").getValue().toString();
                    String retriveSection = dataSnapshot.child("student_section").getValue().toString();
                    String retriveSID = dataSnapshot.child("studentid").getValue().toString();

                    UserName.setText(retriveUserName);
                    UserStatus.setText(retriveStatus);
                    StudentId.setText(retriveSID);
                    StudentSection.setText(retriveSession);
                    StudentBatch.setText(retriveBatch);
                    StudentSession.setText(retriveSection);
                    StudentDept.setText(retriveDeptName);
                    StudentProgram.setText(retriveProgramName);

                    ManageChatRequests();

                }
                else{
                    Toast.makeText(ProfileActivity.this, "Please update your profile info..", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ManageChatRequests() {
        if(!senderUserID.equals(receivedUserID)){

            SendMessaheRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendMessaheRequestButton.setEnabled(false);
                    if(Current_State.equals("new")){
                        SendChatRequest();
                    }
                }
            });
        }
        else {
            SendMessaheRequestButton.setVisibility(View.INVISIBLE);
        }
    }

    private void SendChatRequest() {
        ChatRequetRef.child(senderUserID).child(receivedUserID)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        ChatRequetRef.child(receivedUserID).child(senderUserID)
                                .child("request_type").setValue("received")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            SendMessaheRequestButton.setEnabled(true);
                                            Current_State ="request_sent";
                                            SendMessaheRequestButton.setText("Cancel Chat Request");
                                        }
                                    }
                                });


                        Toast.makeText(ProfileActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                    }
                });

    }

}















