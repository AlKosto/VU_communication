package com.example.imran.vucommunication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private Button showEventEditor;
    private LinearLayout eventEditorLayout;
    private TextView pickTimeAndDate;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef, uPostRef;
    private CircleImageView senderUserImage;
    private String currentUserID;
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        intializeField();


        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        currentUserID = mAuth.getCurrentUser().getUid();

        showProfileImage();

        showEventEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventEditorLayout.setVisibility(View.VISIBLE);
                showEventEditor.setVisibility(View.GONE);
            }
        });


        pickTimeAndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day =c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EventActivity.this , EventActivity.this,
                        year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void intializeField() {
        showEventEditor = (Button)findViewById(R.id.add_event_show_btn);
        eventEditorLayout =(LinearLayout)findViewById(R.id.full);
        pickTimeAndDate = (TextView)findViewById(R.id.pick_date_and_time);
        senderUserImage = (CircleImageView)findViewById(R.id.users_profile_image);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal = year;
        monthFinal = month + 1;
        dayFinal = dayOfMonth;


        Calendar c= Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(EventActivity.this, EventActivity.this,
                hour, minute, true);

        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hourFinal = hourOfDay;
        minuteFinal = minute;


        pickTimeAndDate.setText("at: " + dayFinal + "-" + monthFinal +"-" + yearFinal +"   "+ hourFinal+":"+minuteFinal);
    }


    private void showProfileImage() {

        RootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("image")){

                    String retrivePrfileImage=dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(retrivePrfileImage).placeholder(R.drawable.profile).into(senderUserImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
