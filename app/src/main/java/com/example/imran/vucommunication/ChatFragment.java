package com.example.imran.vucommunication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private View PrivetChatView;
    private RecyclerView chatList;

    private DatabaseReference ChatsRef, UsersRef;
    private FirebaseAuth mAuth;
    private String currentUserID;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        PrivetChatView= inflater.inflate(R.layout.fragment_chat, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        ChatsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
        ChatsRef.keepSynced(true);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UsersRef.keepSynced(true);

        chatList =(RecyclerView) PrivetChatView.findViewById(R.id.chat_list);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));

        return PrivetChatView;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(ChatsRef , Contacts.class)
                .build();



        FirebaseRecyclerAdapter<Contacts, ChatViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, ChatViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatViewHolder holder, int position, @NonNull Contacts model) {

                        final String usersIDs = getRef(position).getKey();
                        final String[] userImage = {"default_image"};

                        UsersRef.child(usersIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    if(dataSnapshot.hasChild("image"))
                                    {
                                        userImage[0] = dataSnapshot.child("image").getValue().toString();

                                        Picasso.get().load(userImage[0]).into(holder.profileImage);
                                    }


                                    final String profieStatus= dataSnapshot.child("status").getValue().toString();
                                    final String pofileName= dataSnapshot.child("name").getValue().toString();
                                    final String UStudentId= dataSnapshot.child("studentid").getValue().toString();
                                    final String UserBatch= dataSnapshot.child("studentBatch").getValue().toString();
                                    final String UStudentProgram = dataSnapshot.child("programName").getValue().toString();



                                    holder.userStudentBatch.setText(UserBatch);
                                    holder.userSId.setText(UStudentId);
                                    holder.userName.setText(pofileName);
                                    holder.userStatus.setText(profieStatus);


                                    if(dataSnapshot.child("userState").hasChild("state")){

                                        String state= dataSnapshot.child("userState").child("state").getValue().toString();
                                        String date= dataSnapshot.child("userState").child("date").getValue().toString();
                                        String time= dataSnapshot.child("userState").child("time").getValue().toString();

                                        if(state.equals("online")){
                                            holder.indicator.setText("Active now");
                                            holder.StudentProgram.setText("");
                                        }
                                        else if(state.equals("offline")){
                                            holder.indicator.setText("Last seen:");
                                            holder.StudentProgram.setText( date+"  "+time);
                                        }

                                    }else {

                                        holder.indicator.setText("");
                                        holder.StudentProgram.setText("offline");
                                    }




                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent chatIntent = new Intent(getContext() ,ChatActivity.class);
                                            chatIntent.putExtra("visit_user_id", usersIDs);
                                            chatIntent.putExtra("visit_user_name", pofileName);
                                            chatIntent.putExtra("visit_image", userImage[0]);
                                            startActivity(chatIntent);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_display_layout , viewGroup, false);
                        return new ChatViewHolder(view);
                    }
                };

        chatList.setAdapter(adapter);
        adapter.startListening();
    }




    public static class ChatViewHolder extends RecyclerView.ViewHolder{

        TextView userName, userStatus ,userSId, userStudentBatch, StudentProgram, indicator;
        CircleImageView profileImage;


        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);


            StudentProgram =itemView.findViewById(R.id.student_program_name);
            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            profileImage= itemView.findViewById(R.id.users_profile_image);
            userSId = itemView.findViewById(R.id.student_id);
            userStudentBatch = itemView.findViewById(R.id.student_batch);
            indicator = itemView.findViewById(R.id.program_indicator);


        }
    }

}
















