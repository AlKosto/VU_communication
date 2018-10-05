package com.example.imran.vucommunication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {


    private EditText UserName, UserStatus, StudentId, StudentSession, StudentBatch ,StudentSection,StudentDept,StudentProgram;
    private CircleImageView userProfileImage;
    private Button UpdateAccountSetting;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        InitializeField();


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        UpdateAccountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent settingIntent = new Intent(ProfileActivity.this, SettingActivity.class);
                    startActivity(settingIntent);

                }

        });

        RetriveUsersInfo();

    }



    private void InitializeField() {


        UserName =(EditText) findViewById(R.id.set_user_name);
        UserStatus =(EditText) findViewById(R.id.set_profile_status);
        StudentId = (EditText) findViewById(R.id.set_student_id);
        StudentSection =(EditText) findViewById(R.id.set_user_Section);
        StudentBatch =(EditText) findViewById(R.id.set_user_batch);
        StudentSession = (EditText) findViewById(R.id.set_student_session);
        StudentDept =(EditText)findViewById(R.id.set_user_dept);
        StudentProgram=(EditText)findViewById(R.id.set_user_program);


        UpdateAccountSetting = (Button) findViewById(R.id.go_setting_button);

        userProfileImage = (CircleImageView) findViewById(R.id.set_profile_image);


    }


    private void RetriveUsersInfo(){
        RootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
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
                    StudentSection.setText(retriveSession);
                    StudentBatch.setText(retriveBatch);
                    StudentSession.setText(retriveSection);
                    StudentDept.setText(retriveDeptName);
                    StudentProgram.setText(retriveProgramName);



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

}
