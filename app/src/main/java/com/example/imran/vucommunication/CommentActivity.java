package com.example.imran.vucommunication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    private EditText inputComment;
    private String postId;
    private RecyclerView commentList;
    private ImageButton postCommentBtn;
    private DatabaseReference RootRef, uCommentRef;
    private FirebaseAuth mAuth;
    private String currentUserId ,currentDate, currentTime, uCommentText, retrivePrfileImage, retriveName;
    private CircleImageView currentProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        postId = getIntent().getExtras().get("post_id").toString();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        uCommentRef= FirebaseDatabase.getInstance().getReference().child("Universal Post").child(postId).child("Comment");

        initializefield();
        showProfileImage();

        LinearLayoutManager uLinerarLayoutManager= new LinearLayoutManager(this);
        uLinerarLayoutManager.setReverseLayout(true);
        uLinerarLayoutManager.setStackFromEnd(true);
        commentList =(RecyclerView)findViewById(R.id.comment_list);
        commentList.setLayoutManager(uLinerarLayoutManager);

        postCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postCommentOnFirebaseDatabase();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Comments> options =
                new FirebaseRecyclerOptions.Builder<Comments>()
                        .setQuery(uCommentRef, Comments.class)
                        .build();


        FirebaseRecyclerAdapter<Comments, CommentPostViewHolder> adapter = new FirebaseRecyclerAdapter<Comments, CommentPostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CommentPostViewHolder holder, int position, @NonNull final Comments model) {

                uCommentRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() && dataSnapshot.hasChild("image")){
                            holder.dateView.setText(model.getDate());
                            holder.timeView.setText(model.getTime());
                            holder.commentText.setText(model.getComment());
                            Picasso.get().load(model.getImage()).into(holder.commentProfileImage);
                        }
                        else {
                            holder.dateView.setText(model.getDate());
                            holder.timeView.setText(model.getTime());
                            holder.commentText.setText(model.getComment());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }

            @NonNull
            @Override
            public CommentPostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_layout, viewGroup, false);

                CommentActivity.CommentPostViewHolder viewHolder = new CommentActivity.CommentPostViewHolder(view);
                return viewHolder;
            }
        };

        commentList.setAdapter(adapter);
        adapter.startListening();
    }





    public static class CommentPostViewHolder extends RecyclerView.ViewHolder{

        TextView commentText, timeView,dateView,name;
        CircleImageView commentProfileImage;
        public CommentPostViewHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.comment_text);
            commentProfileImage = itemView.findViewById(R.id.comment_profile_image);
            timeView = itemView.findViewById(R.id.time_view);
            dateView = itemView.findViewById(R.id.date_view);
            name = itemView.findViewById(R.id.name);

        }
    }


    private void initializefield() {

        postCommentBtn = (ImageButton)findViewById(R.id.post_comment_btn);
        inputComment = (EditText) findViewById(R.id.input_comment);
        currentProfile =(CircleImageView) findViewById(R.id.prfile_image);

    }

    private void postCommentOnFirebaseDatabase() {

        final String commentKey = uCommentRef.push().getKey();

        uCommentText = inputComment.getText().toString();

        if(TextUtils.isEmpty(uCommentText)){
            Toast.makeText(this, "Enter Comment First.. ", Toast.LENGTH_SHORT).show();
        }
        else {

            Calendar callForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormate = new SimpleDateFormat("MMM dd,yyyy");
            currentDate = currentDateFormate.format(callForDate.getTime());


            Calendar callForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormate = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormate.format(callForTime.getTime());


            HashMap<String, Object> postInforMap = new HashMap<>();
            postInforMap.put("id", currentUserId);
            postInforMap.put("name", retriveName);
            postInforMap.put("comment", uCommentText);
            postInforMap.put("date", currentDate);
            postInforMap.put("time", currentTime);
            postInforMap.put("image", retrivePrfileImage);
            postInforMap.put("cid", commentKey);


            uCommentRef.child(commentKey).updateChildren(postInforMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(CommentActivity.this, "Your comment is uploaded", Toast.LENGTH_SHORT).show();
                    inputComment.setText("");


                }
            });

        }
    }



    private void showProfileImage() {

        RootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("image")){

                    retrivePrfileImage =dataSnapshot.child("image").getValue().toString();
                    retriveName = dataSnapshot.child("name").getValue().toString();

                    Picasso.get().load(retrivePrfileImage).placeholder(R.drawable.profile).into(currentProfile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
