package com.example.imran.vucommunication;

import android.content.Intent;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner, spinner_program;
    String program_name, dept_name;
    private EditText UserName, UserStatus, StudentId, StudentSession, StudentBatch ,StudentSection;
    private CircleImageView userProfileImage;
    private Button UpdateAccountSetting;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        InitializeField();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.dept_names,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        UpdateAccountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSettings();
            }
        });



    }




    private void InitializeField() {



        spinner = (Spinner)findViewById(R.id.spinner_dept_name);
        spinner_program =(Spinner)findViewById(R.id.spinner_program_name);


        UserName =(EditText) findViewById(R.id.set_user_name);
        UserStatus =(EditText) findViewById(R.id.set_profile_status);
        StudentId = (EditText) findViewById(R.id.set_student_id);
        StudentSection =(EditText) findViewById(R.id.set_user_Section);
        StudentBatch =(EditText) findViewById(R.id.set_user_batch);
        StudentSession = (EditText) findViewById(R.id.set_student_session);


        UpdateAccountSetting = (Button) findViewById(R.id.update_setting_button);

        userProfileImage = (CircleImageView) findViewById(R.id.set_profile_image);
    }

    private void UpdateSettings() {


        program_name =spinner_program.getSelectedItem().toString();

        String setUserName = UserName.getText().toString();
        String userStatus = UserStatus.getText().toString();
        String studentID= StudentId.getText().toString();
        String studentSession = StudentSession.getText().toString();
        String studentBatch= StudentBatch.getText().toString();
        String studentSection = StudentSection.getText().toString();


        if(TextUtils.isEmpty(setUserName)){
            Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(userStatus)){
            Toast.makeText(this, "Enter Your Status", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(studentID)){
            Toast.makeText(this, "Enter Your Student ID", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(studentSession)){
            Toast.makeText(this, "Enter Your Session", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(studentBatch)){
            Toast.makeText(this, "Enter Your Batch", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(studentSection)){
            Toast.makeText(this, "Enter Your Section", Toast.LENGTH_SHORT).show();
        }
        if(dept_name.equals("Select Your Department")){
            Toast.makeText(this, "Select One From Departments", Toast.LENGTH_SHORT).show();
        }
        if(program_name.equals("Select Your Program")){
            Toast.makeText(this, "Select Yor Program name", Toast.LENGTH_SHORT).show();
        }
        else{

            HashMap<String, String> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserID);
            profileMap.put("name", setUserName);
            profileMap.put("status", userStatus);
            profileMap.put("studentid", studentID);
            profileMap.put("studentSession", studentSession);
            profileMap.put("studentBatch", studentBatch);
            profileMap.put("student_section", studentSection);
            profileMap.put("deptName",dept_name);
            profileMap.put("programName", program_name);

            RootRef.child("Users").child(currentUserID).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SettingActivity.this, "Profile Update Successfully", Toast.LENGTH_SHORT).show();
                        sendUserToMainActivity();
                    }else
                    {
                        String error = task.getException().toString();
                        Toast.makeText(SettingActivity.this,"Error : "+error, Toast.LENGTH_LONG).show();

                    }

                }
            });

        }


    }







    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(SettingActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       dept_name = parent.getItemAtPosition(position).toString();


        if (spinner.getSelectedItem().equals("Computer science and Engineering")) {
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.program_names_cse, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter2);


        }
        else if (spinner.getSelectedItem().equals("Business Administration")) {
        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_name_business, android.R.layout.simple_spinner_item);
        spinner_program.setAdapter(adapter3);

    }
    else if(spinner.getSelectedItem().equals("Electrical and Electronic Engineering")){
        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_name_eee, android.R.layout.simple_spinner_item);
        spinner_program.setAdapter(adapter3);

    } else if (spinner.getSelectedItem().equals("Economics")) {
        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_in_economics, android.R.layout.simple_spinner_item);
        spinner_program.setAdapter(adapter3);

    } else if(spinner.getSelectedItem().equals("English")){
        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_name_english, android.R.layout.simple_spinner_item);
        spinner_program.setAdapter(adapter3);

        } else if (spinner.getSelectedItem().equals("Journalism, Communication and Media Studies")) {
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_in_Journalism, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
     //       spinner_program.setOnItemSelectedListener(this);
       //    program_name =spinner_program.getSelectedItem().toString();
            //        setUserStatus.setText(spinner_program.getSelectedItem().toString());
        } else if(spinner.getSelectedItem().equals("Law and Human Rights")){
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_name_law, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
     //       spinner_program.setOnItemSelectedListener(this);
     //       program_name =spinner_program.getSelectedItem().toString();
            //       setUserStatus.setText(spinner_program.getSelectedItem().toString());
        } else if (spinner.getSelectedItem().equals("Pharmacy")) {
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_in_Pharmacy, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
     //       spinner_program.setOnItemSelectedListener(this);
     //      program_name =spinner_program.getSelectedItem().toString();
            //         setUserStatus.setText(spinner_program.getSelectedItem().toString());
        } else if(spinner.getSelectedItem().equals("Public Health")){
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_in_public_health, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
     //       spinner_program.setOnItemSelectedListener(this);
       //   program_name =spinner_program.getSelectedItem().toString();
            //      setUserStatus.setText(spinner_program.getSelectedItem().toString());
        } else if(spinner.getSelectedItem().equals("Sociology")){
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_in_Sociology, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
    //        spinner_program.setOnItemSelectedListener(this);
      //     program_name =spinner_program.getSelectedItem().toString();
            //    setUserStatus.setText(spinner_program.getSelectedItem().toString());
        } else if(spinner.getSelectedItem().equals("Political Science")){
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_in_Political_Science, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
    //        spinner_program.setOnItemSelectedListener(this);
       //     program_name =spinner_program.getSelectedItem().toString();
          //  setUserStatus.setText(spinner_program.getSelectedItem().toString());
        } else if(spinner.getSelectedItem().equals("Applied Statistics")){
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_in_Applied_Statistics, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
     //       spinner_program.setOnItemSelectedListener(this);
        //    program_name =spinner_program.getSelectedItem().toString();
        //    setUserStatus.setText(spinner_program.getSelectedItem().toString());
        } else if(spinner.getSelectedItem().equals("Islamic History")){
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_in_Islamic_History, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
            //  spinner_program.setOnItemSelectedListener(this);
       //     program_name =spinner_program.getSelectedItem().toString();
            //  setUserStatus.setText(spinner_program.getSelectedItem().toString());
        }
        else if(spinner.getSelectedItem().equals("Select Your Department")){
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_null, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
            //  spinner_program.setOnItemSelectedListener(this);
          //  program_name =spinner_program.getSelectedItem().toString();
            //  setUserStatus.setText(spinner_program.getSelectedItem().toString());
        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
