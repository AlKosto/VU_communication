package com.example.imran.vucommunication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private Toolbar toolBar;
    private Button showEventEditor, attatchImageBtn,addEventBtn, cancelBtn;
    private LinearLayout eventEditorLayout;
    private TextView pickTimeAndDate;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef, uEventRef,userRef;
    private StorageReference uEventImageRef;
    private CircleImageView senderUserImage;
    private String currentUserID  ,currentDate, currentTime;
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    private ImageView uTempShowImage;
    private static final int GalleryPick =1;
    public EditText eventDescription, eventTitle;
    private RecyclerView eventList;

    Animation FabOpen, FabClose, FabRClockwise, FabRanticlckwise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        intializeField();


        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        currentUserID = mAuth.getCurrentUser().getUid();
        uEventImageRef = FirebaseStorage.getInstance().getReference("Events Image");
        uEventRef = FirebaseDatabase.getInstance().getReference().child("Events");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        LinearLayoutManager uLinerarLayoutManager= new LinearLayoutManager(this);
        uLinerarLayoutManager.setReverseLayout(true);
        uLinerarLayoutManager.setStackFromEnd(true);
        eventList =(RecyclerView)findViewById(R.id.event_list);
        eventList.setLayoutManager(uLinerarLayoutManager);

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


        attatchImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageToPost();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uTempShowImage.getDrawable() == null) {
                    eventEditorLayout.setVisibility(View.GONE);
                    showEventEditor.setVisibility(View.VISIBLE);
                    eventDescription.setText("");
                    eventTitle.setText("");
                    uTempShowImage.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(EventActivity.this, "You have to Decline Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uTempShowImage.getDrawable()==null){
                    if(monthFinal==0 && minuteFinal ==0 && yearFinal ==0){
                        Toast.makeText(EventActivity.this, "Set Date and Time", Toast.LENGTH_SHORT).show();
                    }
                    if(TextUtils.isEmpty(eventTitle.getText().toString())){
                        Toast.makeText(EventActivity.this, "Enter Title of event..", Toast.LENGTH_SHORT).show();
                    }
                    if(TextUtils.isEmpty(eventDescription.getText().toString())){
                        Toast.makeText(EventActivity.this, "Enter Description or set Image", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        setJustDescription();
                    }
                }
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Events> options =
                new FirebaseRecyclerOptions.Builder<Events>()
                        .setQuery(uEventRef, Events.class)
                        .build();

        FirebaseRecyclerAdapter<Events, UniEventsViewHolder> adapter = new FirebaseRecyclerAdapter<Events, UniEventsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final UniEventsViewHolder holder, int position, @NonNull final Events model) {


                final String eventId = model.getEid();
                final String list_user_id = model.getId();
                final String[] userName = new String[1];
                final String[] userImage = new String[1];

                RootRef.child("Users").child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()&&dataSnapshot.hasChild("image")) {

                            userName[0] = dataSnapshot.child("name").getValue().toString();
                            userImage[0] = dataSnapshot.child("image").getValue().toString();


                            holder.eventPostername.setText(userName[0]);
                            Picasso.get().load(userImage[0]).into(holder.eventPosterImage);



                        }else {
                            userName[0] = dataSnapshot.child("name").getValue().toString();


                            holder.eventPostername.setText(userName[0]);
                        }
                        if(model.getImage().isEmpty()){
                            holder.eventDescription.setVisibility(View.VISIBLE);
                            holder.eventImage.setVisibility(View.GONE);
                            holder.currentTimeView.setText(model.getCurrentTime());
                            holder.currentDateView.setText(model.getCurrentDate());
                            holder.eventDescription.setText(model.getDescription());
                            holder.eventTitle.setText(model.getTitle());
                            holder.eventTime.setText("Event At: " +model.getDay() + "-" + model.getMonth()+"-"+model.getYear()+"   "+model.getHour()+":"+model.getMinute());
                        }
                        else if(model.getDescription().isEmpty()){

                            holder.eventDescription.setVisibility(View.GONE);
                            holder.eventImage.setVisibility(View.VISIBLE);
                            holder.currentTimeView.setText(model.getCurrentTime());
                            holder.currentDateView.setText(model.getCurrentDate());
                            holder.eventTitle.setText(model.getTitle());
                            holder.eventTime.setText("Event At: " +model.getDay() + "-" + model.getMonth()+"-"+model.getYear()+"   "+model.getHour()+":"+model.getMinute());
                            Picasso.get().load(model.getImage()).into(holder.eventImage);
                        }
                        else{
                            holder.eventDescription.setVisibility(View.VISIBLE);
                            holder.eventImage.setVisibility(View.VISIBLE);
                            holder.currentTimeView.setText(model.getCurrentTime());
                            holder.currentDateView.setText(model.getCurrentDate());
                            holder.eventDescription.setText(model.getDescription());
                            holder.eventTitle.setText(model.getTitle());
                            holder.eventTime.setText("Event At: " +model.getDay() + "-" + model.getMonth()+"-"+model.getYear()+"   "+model.getHour()+":"+model.getMinute());

                            Picasso.get().load(model.getImage()).into(holder.eventImage);
                        }


                        holder.fab_setting.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(holder.fab_setting.getVisibility() == View.INVISIBLE)
                                {
                                    holder.fab_delete.startAnimation(FabClose);
                                    holder.fabAlarmBtn.startAnimation(FabClose);
                                    holder.fab_setting.startAnimation(FabRanticlckwise);
                                    holder.fab_setting.setClickable(false);
                                    holder.fab_delete.setClickable(false);
                                }else {
                                    holder.fab_delete.startAnimation(FabOpen);
                                    holder.fabAlarmBtn.startAnimation(FabOpen);
                                    holder.fab_setting.startAnimation(FabRClockwise);
                                    holder.fab_setting.setClickable(true);
                                    holder.fab_delete.setClickable(true);
                                }
                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public UniEventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_layout, viewGroup, false);

                UniEventsViewHolder viewHolder = new UniEventsViewHolder(view);
                return viewHolder;
            }
        };

        eventList.setAdapter(adapter);
        adapter.startListening();

    }


    public static class UniEventsViewHolder extends RecyclerView.ViewHolder{

        CircleImageView eventPosterImage;
        ImageView eventImage;
        TextView eventPostername,eventTitle, currentDateView, currentTimeView, eventTime, eventDescription;
        FloatingActionButton fabAlarmBtn,fab_setting,fab_delete;
        Animation FabOpen, FabClose, FabRClockwise, FabRanticlckwise;
        public UniEventsViewHolder(@NonNull View itemView) {
            super(itemView);
            eventPosterImage = itemView.findViewById(R.id.event_poster_image);
            eventPostername = itemView.findViewById(R.id.event_poster_name);
            currentDateView = itemView.findViewById(R.id.uDate);
            currentTimeView = itemView.findViewById(R.id.uTime);
            eventTime= itemView.findViewById(R.id.event_time);
            eventImage = itemView.findViewById(R.id.event_image);
            eventTitle= itemView.findViewById(R.id.event_title);
            eventDescription = itemView.findViewById(R.id.event_description_text);
            fabAlarmBtn = itemView.findViewById(R.id.fab_alarm);
            fab_setting = (FloatingActionButton)itemView.findViewById(R.id.fab_setting);
            fab_delete = (FloatingActionButton) itemView.findViewById(R.id.fab_delete);




        }
    }






    private void setJustDescription() {

        String eventText = eventDescription.getText().toString();
        String eventName = eventTitle.getText().toString();
        final String eventKey = uEventRef.push().getKey();

        Calendar callForDate =Calendar.getInstance();
        SimpleDateFormat currentDateFormate = new SimpleDateFormat("MMM dd,yyyy");
        currentDate = currentDateFormate.format(callForDate.getTime());


        Calendar callForTime =Calendar.getInstance();
        SimpleDateFormat currentTimeFormate = new SimpleDateFormat("hh:mm a");
        currentTime = currentTimeFormate.format(callForTime.getTime());

        HashMap<String, Object> postInforMap = new HashMap<>();
        postInforMap.put("id", currentUserID);
        postInforMap.put("title", eventName);
        postInforMap.put("description", eventText);
        postInforMap.put("image", "");
        postInforMap.put("year", yearFinal);
        postInforMap.put("month", monthFinal);
        postInforMap.put("day", dayFinal);
        postInforMap.put("hour", hourFinal);
        postInforMap.put("minute", minuteFinal);
        postInforMap.put("currentDate", currentDate);
        postInforMap.put("currentTime" , currentTime);
        postInforMap.put("eid", eventKey);

        uEventRef.child(eventKey).updateChildren(postInforMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EventActivity.this, "Your post is uploaded", Toast.LENGTH_SHORT).show();
                eventEditorLayout.setVisibility(View.GONE);
                showEventEditor.setVisibility(View.VISIBLE);
                eventDescription.setText("");
                eventTitle.setText("");
                eventEditorLayout.setVisibility(View.GONE);
                showEventEditor.setVisibility(View.VISIBLE);

                finish();
                startActivity(getIntent());

            }
        });

        finish();
        startActivity(getIntent());


    }




    private void addImageToPost() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == GalleryPick && resultCode == RESULT_OK  && data!=null){
            Uri ImageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK) {

                final String eventName = eventTitle.getText().toString();

                if (eventName.isEmpty()) {
                    Toast.makeText(EventActivity.this, "First Enter Event Title..", Toast.LENGTH_SHORT).show();
                }
                if(monthFinal==0 && minuteFinal ==0 && yearFinal ==0){
                    Toast.makeText(this, "Set Date And Time", Toast.LENGTH_SHORT).show();
                }
                else {
                    Uri resultUri = result.getUri();

                    final String eventKey = uEventRef.push().getKey();


                    final StorageReference filePath = uEventImageRef.child(eventKey + ".jpg");
                    filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                            if (task.isSuccessful()) {
                                Toast.makeText(EventActivity.this, "Image is ready to post", Toast.LENGTH_SHORT).show();


                                final String downloadUrl = task.getResult().getDownloadUrl().toString();

                                uTempShowImage.setVisibility(View.VISIBLE);
                                Picasso.get().load(downloadUrl).into(uTempShowImage);


                                addEventBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String eventTextt = eventDescription.getText().toString();
                                                            String eventNamee= eventTitle.getText().toString();

                                                            Calendar callForDate =Calendar.getInstance();
                                                            SimpleDateFormat currentDateFormate = new SimpleDateFormat("MMM dd,yyyy");
                                                            String currentDate1 = currentDateFormate.format(callForDate.getTime());


                                                            Calendar callForTime =Calendar.getInstance();
                                                            SimpleDateFormat currentTimeFormate = new SimpleDateFormat("hh:mm a");
                                                            String currentTime1 = currentTimeFormate.format(callForTime.getTime());


                                                            HashMap<String, Object> postInforMap = new HashMap<>();
                                                            postInforMap.put("id", currentUserID);
                                                            postInforMap.put("title", eventNamee);
                                                            postInforMap.put("description", eventTextt);
                                                            postInforMap.put("year", yearFinal);
                                                            postInforMap.put("month", monthFinal);
                                                            postInforMap.put("day", dayFinal);
                                                            postInforMap.put("hour", hourFinal);
                                                            postInforMap.put("minute", minuteFinal);
                                                            postInforMap.put("currentDate", currentDate1);
                                                            postInforMap.put("currentTime" , currentTime1);
                                                            postInforMap.put("eid", eventKey);
                                                            postInforMap.put("image", downloadUrl);


                                                            RootRef.child("Events").child(eventKey).updateChildren(postInforMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Toast.makeText(EventActivity.this, "Your post is uploaded", Toast.LENGTH_SHORT).show();
                                                                    eventEditorLayout.setVisibility(View.GONE);
                                                                    showEventEditor.setVisibility(View.VISIBLE);
                                                                    eventDescription.setText("");
                                                                    eventTitle.setText("");
                                                                    uTempShowImage.setVisibility(View.GONE);

                                                                    finish();
                                                                    startActivity(getIntent());

                                                                }
                                                            });

                                                            finish();
                                                            startActivity(getIntent());
                                    }
                                });



                                attatchImageBtn.setText("Decline Image");

                                attatchImageBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        uEventImageRef.child(eventKey + ".jpg").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                eventEditorLayout.setVisibility(View.GONE);
                                                showEventEditor.setVisibility(View.VISIBLE);
                                                eventDescription.setText("");
                                                eventTitle.setText("");
                                                uTempShowImage.setVisibility(View.GONE);
                                                Toast.makeText(EventActivity.this, "Decline Complete", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });


                            } else {
                                String errorM = task.getException().toString();
                                Toast.makeText(EventActivity.this, "Error: " + errorM, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }
    }

    private void intializeField() {
        showEventEditor = (Button)findViewById(R.id.add_event_show_btn);
        eventEditorLayout =(LinearLayout)findViewById(R.id.full);
        pickTimeAndDate = (TextView)findViewById(R.id.pick_date_and_time);
        senderUserImage = (CircleImageView)findViewById(R.id.users_profile_image);
        toolBar =(Toolbar)findViewById(R.id.event_toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Events");
        
        attatchImageBtn = (Button) findViewById(R.id.attatch_photo_btn);
        uTempShowImage =(ImageView)findViewById(R.id.u_event_image);
        addEventBtn =(Button) findViewById(R.id.add_event_btn);
        eventDescription = (EditText)findViewById(R.id.event_description);
        eventTitle = (EditText)findViewById(R.id.event_title);
        cancelBtn =(Button)findViewById(R.id.cancel_btn);



        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabRClockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        FabRanticlckwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);


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
