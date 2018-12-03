package com.example.imran.vucommunication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UniversalActevity extends AppCompatActivity {

    private TextView uPostInputText,uPostbtn,uDeclinebtn, utextPostbtn;
    private Toolbar toolBar;
    private CircleImageView senderUserImage;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef, uPostRef,userRef, likeRef;
    private StorageReference UserProfileImageRef, uPostImageRef;
    private ImageButton uPostAddImagebtn;
    private static final int GalleryPick =1;
    private ImageView uTempShowImage,uSendMessageBtnToPoster;
    private LinearLayout uPostLayoutbtn, uPosterLayout;
    private String currentUserID, uPostText, currentDate,currentTime;
    private RecyclerView uPostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_actevity);

        initializeField();


        RootRef = FirebaseDatabase.getInstance().getReference();
        showProfileImage();


        UserProfileImageRef = FirebaseStorage.getInstance().getReference("Profile Images");
        uPostImageRef = FirebaseStorage.getInstance().getReference("Post Image");
        uPostRef =FirebaseDatabase.getInstance().getReference().child("Universal Post");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        likeRef = userRef.child("like");

        LinearLayoutManager uLinerarLayoutManager= new LinearLayoutManager(this);
        uLinerarLayoutManager.setReverseLayout(true);
        uLinerarLayoutManager.setStackFromEnd(true);
        uPostList =(RecyclerView)findViewById(R.id.user_post_list);
        uPostList.setLayoutManager(uLinerarLayoutManager);





        uPostInputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uPostText = uPostInputText.getText().toString();

                if(uTempShowImage.getDrawable()==null){

                    if(TextUtils.isEmpty(uPostText)){
                        uPostLayoutbtn.setVisibility(View.GONE);
                    }else{
                        uPostLayoutbtn.setVisibility(View.VISIBLE );
                        utextPostbtn.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    if(TextUtils.isEmpty(uPostText)){
                        uPostLayoutbtn.setVisibility(View.GONE);
                    }else{
                        uPostLayoutbtn.setVisibility(View.VISIBLE );
                        uPostbtn.setVisibility(View.VISIBLE);
                        uDeclinebtn.setVisibility(View.VISIBLE);
                    }
                }


            }
        });


        uPostAddImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageToPost();
            }
        });

        utextPostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOnlyTextPost();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();



        FirebaseRecyclerOptions<Posts> options =
                new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(uPostRef, Posts.class)
                .build();


        FirebaseRecyclerAdapter<Posts, UniPostViewHolder> adapter = new FirebaseRecyclerAdapter<Posts, UniPostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final UniPostViewHolder holder, int position, @NonNull final Posts model) {


                final String postId = model.getPid();
                final String list_user_id = model.getId();
                final String[] userName = new String[1];
                final String[] userImage = new String[1];


                userRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() && dataSnapshot.hasChild("image")){
                            userName[0] = dataSnapshot.child("name").getValue().toString();
                            userImage[0] = dataSnapshot.child("image").getValue().toString();



                            holder.userName.setText(userName[0]);
                            Picasso.get().load(userImage[0]).into(holder.userImage);

                            if(model.getImage().isEmpty()){
                                holder.uTextView.setVisibility(View.VISIBLE);
                                holder.uImageView.setVisibility(View.GONE);
                                holder.uTime.setText(model.getTime());
                                holder.uDate.setText(model.getDate());
                                holder.uTextView.setText(model.getPost());
                            }
                            else if(model.getPost().isEmpty()){
                                holder.uImageView.setVisibility(View.VISIBLE);
                                holder.uTextView.setVisibility(View.GONE);
                                holder.uTime.setText(model.getTime());
                                holder.uDate.setText(model.getDate());
                                Picasso.get().load(model.getImage()).into(holder.uImageView);
                            }
                            else{
                                holder.uImageView.setVisibility(View.VISIBLE);
                                holder.uTextView.setVisibility(View.VISIBLE);
                                holder.uTime.setText(model.getTime());
                                holder.uDate.setText(model.getDate());
                                holder.uTextView.setText(model.getPost());
                                Picasso.get().load(model.getImage()).into(holder.uImageView);
                            }
                        }
                        else{
                            userName[0] = dataSnapshot.child("name").getValue().toString();

                            holder.userName.setText(userName[0]);

                            if(model.getImage().isEmpty()){
                                holder.uTextView.setVisibility(View.VISIBLE);
                                holder.uImageView.setVisibility(View.GONE);
                                holder.uTime.setText(model.getTime());
                                holder.uDate.setText(model.getDate());
                                holder.uTextView.setText(model.getPost());
                            }
                            else if(model.getPost().isEmpty()){
                                holder.uImageView.setVisibility(View.VISIBLE);
                                holder.uTextView.setVisibility(View.GONE);
                                holder.uTime.setText(model.getTime());
                                holder.uDate.setText(model.getDate());
                                Picasso.get().load(model.getImage()).into(holder.uImageView);
                            }
                            else{
                                holder.uImageView.setVisibility(View.VISIBLE);
                                holder.uTextView.setVisibility(View.VISIBLE);
                                holder.uTime.setText(model.getTime());
                                holder.uDate.setText(model.getDate());
                                holder.uTextView.setText(model.getPost());
                                Picasso.get().load(model.getImage()).into(holder.uImageView);
                            }


                        }
                        holder.uPosterLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent profileIntent = new Intent( UniversalActevity.this, ProfileActivity.class);
                                profileIntent.putExtra("visit_user_id", list_user_id);
                                startActivity(profileIntent);
                            }
                        });


//                        holder.uSendMessageBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent chatIntent = new Intent(UniversalActevity.this ,ChatActivity.class);
//                                chatIntent.putExtra("visit_user_id", list_user_id);
//                                chatIntent.putExtra("visit_user_name", userName[0]);
//                                chatIntent.putExtra("visit_image", userImage[0]);
//                                startActivity(chatIntent);
//                            }
//                        });


                        holder.uSendMessageBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                RootRef.child("Contacts").child(currentUserID).child(list_user_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            Intent chatIntent = new Intent(UniversalActevity.this ,ChatActivity.class);
                                            chatIntent.putExtra("visit_user_id", list_user_id);
                                            chatIntent.putExtra("visit_user_name", userName[0]);
                                            chatIntent.putExtra("visit_image", userImage[0]);
                                            chatIntent.putExtra("message_text","");
                                            startActivity(chatIntent);
                                        }
                                        else {
                                            Intent profileIntent = new Intent( UniversalActevity.this, ProfileActivity.class);
                                            profileIntent.putExtra("visit_user_id", list_user_id);
                                            startActivity(profileIntent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }
                        });

                        holder.uCommentBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent chatIntent = new Intent(UniversalActevity.this ,CommentActivity.class);
                                chatIntent.putExtra("post_id", postId);
                                chatIntent.putExtra("check","uni");
                                startActivity(chatIntent);
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
            public UniPostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.universal_layout, viewGroup, false);

                UniPostViewHolder viewHolder = new UniPostViewHolder(view);
                return viewHolder;
            }
        };


        uPostList.setAdapter(adapter);
        adapter.startListening();

    }



    public static class UniPostViewHolder extends RecyclerView.ViewHolder{

        TextView userName, uTextView, uDate,uTime;
        CircleImageView userImage;
        ImageView uImageView, uSendMessageBtn, uCommentBtn;
        LinearLayout uPosterLayout;


        public UniPostViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage= itemView.findViewById(R.id.users_profile_image);
            userName = itemView.findViewById(R.id.user_profile_name);
            uTextView = itemView.findViewById(R.id.universal_post_text);
            uImageView = itemView.findViewById(R.id.universal_image);
            uDate = itemView.findViewById(R.id.uDate);
            uTime = itemView.findViewById(R.id.uTime);
            uPosterLayout =itemView.findViewById(R.id.poster_layout);
            uSendMessageBtn = itemView.findViewById(R.id.uSendMessageBtn);
            uCommentBtn = itemView.findViewById(R.id.uCommentBtn);

        }
    }






    private void initializeField() {

        toolBar =(Toolbar)findViewById(R.id.universal_toolbar);
        uPostInputText =(TextView) findViewById(R.id.u_post_input_text);
        uPostbtn =(TextView) findViewById(R.id.add_post_text_view_btn);;
        uPostAddImagebtn =(ImageButton ) findViewById(R.id.u_post_add_image);
        uDeclinebtn =(TextView) findViewById(R.id.decline_post_text_view_btn);
        uTempShowImage= (ImageView) findViewById(R.id.u_post_image);
        uPostLayoutbtn =(LinearLayout) findViewById(R.id.uButtonLayout);
        utextPostbtn =(TextView)findViewById(R.id.post_text_view_btn) ;

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Universal Posts");

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.keepSynced(true);

        senderUserImage = (CircleImageView)findViewById(R.id.users_profile_image);

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

            if(resultCode == RESULT_OK){

                Uri resultUri = result.getUri();




                final String postKey = uPostRef.push().getKey();


                final StorageReference filePath = uPostImageRef.child(postKey +".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(UniversalActevity.this, "Image is ready to post", Toast.LENGTH_SHORT).show();


                            final String downloadUrl= task.getResult().getDownloadUrl().toString();

                            uTempShowImage.setVisibility(View.VISIBLE);
                            Picasso.get().load(downloadUrl).into(uTempShowImage);

                            uPostLayoutbtn.setVisibility(View.VISIBLE );
                            uPostbtn.setVisibility(View.VISIBLE);
                            uDeclinebtn.setVisibility(View.VISIBLE);

                            uPostbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RootRef.child("Universal Post").child(postKey).child("image")
                                            .setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){


                                                        uPostText = uPostInputText.getText().toString();

                                                        Calendar callForDate =Calendar.getInstance();
                                                        SimpleDateFormat currentDateFormate = new SimpleDateFormat("MMM dd,yyyy");
                                                        currentDate = currentDateFormate.format(callForDate.getTime());


                                                        Calendar callForTime =Calendar.getInstance();
                                                        SimpleDateFormat currentTimeFormate = new SimpleDateFormat("hh:mm a");
                                                        currentTime = currentTimeFormate.format(callForTime.getTime());



                                                        HashMap<String, Object> postInforMap = new HashMap<>();
                                                        postInforMap.put("id", currentUserID);
                                                        postInforMap.put("post", uPostText);
                                                        postInforMap.put("date", currentDate);
                                                        postInforMap.put("time", currentTime);
                                                        postInforMap.put("pid", postKey);

                                                        uPostRef.child(postKey).updateChildren(postInforMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(UniversalActevity.this, "Your post is uploaded", Toast.LENGTH_SHORT).show();
                                                                uPostLayoutbtn.setVisibility(View.GONE);
                                                                uTempShowImage.setVisibility(View.GONE);
                                                                uPostInputText.setText("");

                                                                finish();
                                                                startActivity(getIntent());

                                                            }
                                                        });

                                                        finish();
                                                        startActivity(getIntent());


                                             }
                                            else {

                                                String message= task.getException().toString();
                                                Toast.makeText(UniversalActevity.this, "Error: " +message, Toast.LENGTH_SHORT).show();


                                                        finish();
                                                        startActivity(getIntent());
                                            }
                                                }
                                            });
                                }

                            });

                            uDeclinebtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    uPostImageRef.child(postKey + ".jpg").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            uPostLayoutbtn.setVisibility(View.GONE);
                                            uPostInputText.setText("");
                                            uTempShowImage.setVisibility(View.GONE);
                                            Toast.makeText(UniversalActevity.this, "Decline Complete", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                        }
                        else {
                            String errorM = task.getException().toString();
                            Toast.makeText(UniversalActevity.this, "Error: "+ errorM, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
    }


    private void addOnlyTextPost() {

        final String postKey = uPostRef.push().getKey();

        uPostText = uPostInputText.getText().toString();

        Calendar callForDate =Calendar.getInstance();
        SimpleDateFormat currentDateFormate = new SimpleDateFormat("MMM dd,yyyy");
        currentDate = currentDateFormate.format(callForDate.getTime());


        Calendar callForTime =Calendar.getInstance();
        SimpleDateFormat currentTimeFormate = new SimpleDateFormat("hh:mm a");
        currentTime = currentTimeFormate.format(callForTime.getTime());



        HashMap<String, Object> postInforMap = new HashMap<>();
        postInforMap.put("id", currentUserID);
        postInforMap.put("post", uPostText);
        postInforMap.put("date", currentDate);
        postInforMap.put("time", currentTime);
        postInforMap.put("image", "");
        postInforMap.put("pid", postKey);

        uPostRef.child(postKey).updateChildren(postInforMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(UniversalActevity.this, "Your post is uploaded", Toast.LENGTH_SHORT).show();
                uPostLayoutbtn.setVisibility(View.GONE);
                uTempShowImage.setVisibility(View.GONE);
                uPostInputText.setText("");

                finish();
                startActivity(getIntent());

            }
        });



    }

}
