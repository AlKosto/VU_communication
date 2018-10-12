package com.example.imran.vucommunication;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {


    private View RequestFragmentView;
    private RecyclerView myRequestsList;
    private DatabaseReference ChatRequestRef ,UserRef, ContactsRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        RequestFragmentView = inflater.inflate(R.layout.fragment_request, container, false);


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UserRef.keepSynced(true);

        ChatRequestRef = FirebaseDatabase.getInstance().getReference().child("Chat_Request");
        ChatRequestRef.keepSynced(true);

        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        ContactsRef.keepSynced(true);

        myRequestsList =(RecyclerView) RequestFragmentView.findViewById(R.id.chat_request_list);
        myRequestsList.setLayoutManager(new LinearLayoutManager(getContext()));


        return RequestFragmentView;
    }





    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(ChatRequestRef.child(currentUserId),Contacts.class )
                        .build();



        FirebaseRecyclerAdapter<Contacts,RequestViewHolder> adapter = new
                FirebaseRecyclerAdapter<Contacts, RequestViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int position, @NonNull Contacts model) {
                        holder.itemView.findViewById(R.id.request_accept_btn).setVisibility(View.VISIBLE);
                        holder.itemView.findViewById(R.id.request_cancel_btn).setVisibility(View.VISIBLE);


                        final String list_user_id = getRef(position).getKey();

                        DatabaseReference getTypeRef = ChatRequestRef.child(currentUserId).child(list_user_id).child("request_type");

                        getTypeRef.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String type = dataSnapshot.getValue().toString();

                                    if(type.equals("received")){
                                        UserRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.hasChild("image")){

                                                    final String requestProfileImage = dataSnapshot.child("image").getValue().toString();

                                                    Picasso.get().load(requestProfileImage).into(holder.prfileImage);
                                                }


                                                final String profieStatus= dataSnapshot.child("status").getValue().toString();
                                                final String pofileName= dataSnapshot.child("name").getValue().toString();
                                                final String UStudentId= dataSnapshot.child("studentid").getValue().toString();
                                                final String UserBatch= dataSnapshot.child("studentBatch").getValue().toString();
                                                final String UStudentProgram = dataSnapshot.child("programName").getValue().toString();



                                                holder.StudentProgram.setText(UStudentProgram);
                                                holder.userStudentBatch.setText(UserBatch);
                                                holder.userSId.setText(UStudentId);
                                                holder.userName.setText(pofileName);
                                                holder.userStatus.setText(profieStatus);


                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        CharSequence option[] = new CharSequence[]{
                                                                "Accept",
                                                                "Cancel"
                                                        };

                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setTitle( pofileName + "'s Chat Request");

                                                        builder.setItems(option, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if(which ==0){
                                                                    ContactsRef.child(currentUserId).child(list_user_id).child("Contact")
                                                                            .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                ContactsRef.child(list_user_id).child(currentUserId).child("Contact")
                                                                                        .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if(task.isSuccessful()){
                                                                                            ChatRequestRef.child(currentUserId).child(list_user_id)
                                                                                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    if(task.isSuccessful()){
                                                                                                        ChatRequestRef.child(list_user_id).child(currentUserId)
                                                                                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                if(task.isSuccessful()){
                                                                                                                    Toast.makeText(getContext(), "Contact Added", Toast.LENGTH_SHORT).show();

                                                                                                                }
                                                                                                            }
                                                                                                        });
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                                if(which==1){
                                                                    ContactsRef.child(list_user_id).child(currentUserId).child("Contact")
                                                                            .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                ChatRequestRef.child(currentUserId).child(list_user_id)
                                                                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if(task.isSuccessful()){
                                                                                            ChatRequestRef.child(list_user_id).child(currentUserId)
                                                                                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    if(task.isSuccessful()){
                                                                                                        Toast.makeText(getContext(), "Contact Deleted", Toast.LENGTH_SHORT).show();

                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    });
                                                                }

                                                            }
                                                        });
                                                        builder.show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });














                                    }else if(type.equals("sent")){
                                        UserRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.hasChild("image")){

                                                    final String requestProfileImage = dataSnapshot.child("image").getValue().toString();

                                                    Picasso.get().load(requestProfileImage).into(holder.prfileImage);
                                                }


                                                final String profieStatus= dataSnapshot.child("status").getValue().toString();
                                                final String pofileName= dataSnapshot.child("name").getValue().toString();
                                                final String UStudentId= dataSnapshot.child("studentid").getValue().toString();
                                                final String UserBatch= dataSnapshot.child("studentBatch").getValue().toString();
                                                final String UStudentProgram = dataSnapshot.child("programName").getValue().toString();



                                                holder.StudentProgram.setText(UStudentProgram);
                                                holder.userStudentBatch.setText(UserBatch);
                                                holder.userSId.setText(UStudentId);
                                                holder.userName.setText(pofileName);
                                                holder.userStatus.setText(profieStatus);
                                                holder.AcceptButton.setVisibility(View.INVISIBLE);


                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        CharSequence option[] = new CharSequence[]{
                                                                "Cancel"
                                                        };

                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setTitle("Chat Request to "+ pofileName);

                                                        builder.setItems(option, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if(which ==0){
                                                                    ContactsRef.child(list_user_id).child(currentUserId).child("Contact")
                                                                            .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                ChatRequestRef.child(currentUserId).child(list_user_id)
                                                                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if(task.isSuccessful()){
                                                                                            ChatRequestRef.child(list_user_id).child(currentUserId)
                                                                                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    if(task.isSuccessful()){
                                                                                                        Toast.makeText(getContext(), "Contact Deleted", Toast.LENGTH_SHORT).show();

                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    });
                                                                }

                                                            }
                                                        });
                                                        builder.show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
















                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_display_layout_for_friends, viewGroup, false);
                        RequestViewHolder holder = new RequestViewHolder(view);
                        return holder;
                    }
                };
        myRequestsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder{

        TextView userName, userStatus ,userSId, userStudentBatch, StudentProgram;
        CircleImageView prfileImage;
        Button AcceptButton, CacelButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            StudentProgram =itemView.findViewById(R.id.student_program_name);
            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            prfileImage= itemView.findViewById(R.id.users_profile_image);
            userSId = itemView.findViewById(R.id.student_id);
            userStudentBatch = itemView.findViewById(R.id.student_batch);

            AcceptButton = itemView.findViewById(R.id.request_accept_btn);
            CacelButton = itemView.findViewById(R.id.request_cancel_btn);



        }
    }

}




















