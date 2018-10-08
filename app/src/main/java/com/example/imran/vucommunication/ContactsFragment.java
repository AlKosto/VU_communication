package com.example.imran.vucommunication;


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
public class ContactsFragment extends Fragment {

    private View ContactsView;
    private RecyclerView myContactsList;

    private DatabaseReference ContactsRef, UserRef;
    private FirebaseAuth mAuth;
    private String currentUserID;


    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ContactsView = inflater.inflate(R.layout.fragment_contacts, container, false);

        myContactsList = (RecyclerView)ContactsView.findViewById(R.id.contacts_list);
        myContactsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth =FirebaseAuth.getInstance();
        currentUserID= mAuth.getCurrentUser().getUid();

        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
        UserRef =FirebaseDatabase.getInstance().getReference().child("Users");

        return ContactsView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(ContactsRef, Contacts.class)
                .build();
        FirebaseRecyclerAdapter<Contacts , ContactsViewHolder> adapter
                =new FirebaseRecyclerAdapter<Contacts, ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int position, @NonNull Contacts model) {

                String userID = getRef(position).getKey();

                UserRef.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("image")){
                            String userImage= dataSnapshot.child("image").getValue().toString();
                            String profieStatus= dataSnapshot.child("status").getValue().toString();
                            String pofileName= dataSnapshot.child("name").getValue().toString();
                            String UStudentId= dataSnapshot.child("studentid").getValue().toString();
                            String UserBatch= dataSnapshot.child("studentBatch").getValue().toString();
                            String UStudentProgram = dataSnapshot.child("programName").getValue().toString();


                            holder.StudentProgram.setText(UStudentProgram);
                            holder.userStudentBatch.setText(UserBatch);
                            holder.userName.setText(pofileName);
                            holder.userStatus.setText(profieStatus);
                            holder.userSId.setText(UStudentId);
                            Picasso.get().load(userImage).into(holder.profileImage);

                        }else {
                            String profieStatus= dataSnapshot.child("status").getValue().toString();
                            String pofileName= dataSnapshot.child("name").getValue().toString();
                            String UStudentId= dataSnapshot.child("studentid").getValue().toString();
                            String UserBatch= dataSnapshot.child("studentBatch").getValue().toString();
                            String UStudentProgram = dataSnapshot.child("programName").getValue().toString();


                            holder.StudentProgram.setText(UStudentProgram);
                            holder.userStudentBatch.setText(UserBatch);
                            holder.userSId.setText(UStudentId);
                            holder.userName.setText(pofileName);
                            holder.userStatus.setText(profieStatus);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_display_layout , viewGroup, false);
                ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                return viewHolder;
            }
        };

        myContactsList.setAdapter(adapter);
        adapter.startListening();
    }



    public static class ContactsViewHolder extends RecyclerView.ViewHolder
    {

        TextView userName, userStatus ,userSId, userStudentBatch, StudentProgram;
        CircleImageView profileImage;


        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);


            StudentProgram =itemView.findViewById(R.id.student_program_name);
            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            profileImage= itemView.findViewById(R.id.users_profile_image);
            userSId = itemView.findViewById(R.id.student_id);
            userStudentBatch = itemView.findViewById(R.id.student_batch);

        }
    }
}






















