package com.example.imran.vucommunication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class LicturePermission extends AppCompatActivity {


    String nId;
    private Button done,checkUser;
    private DatabaseReference RootRef ,NotesRef;
    private EditText inputUserId;
    private CircleImageView profile;
    private TextView name,infoText;
    private LinearLayout recever_info;
    String userId;
    int check=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licture_permission);

        nId=getIntent().getExtras().get("nid").toString();
        done=(Button)findViewById(R.id.done);
        inputUserId =(EditText) findViewById(R.id.input_user_id);
        RootRef = FirebaseDatabase.getInstance().getReference();
        NotesRef = FirebaseDatabase.getInstance().getReference().child("Notes").child("");
        profile=(CircleImageView)findViewById(R.id.profile) ;
        name=(TextView)findViewById(R.id.name) ;
        recever_info=(LinearLayout) findViewById(R.id.recever_info);
        infoText=(TextView)findViewById(R.id.textview) ;
        checkUser=(Button)findViewById(R.id.check_user);



        checkUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId=inputUserId.getText().toString();

                if(userId.isEmpty()){
                    Toast.makeText(LicturePermission.this, "Enter user id...", Toast.LENGTH_SHORT).show();
                }else {

                    RootRef.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists() && dataSnapshot.hasChild("image")){

                                recever_info.setVisibility(View.VISIBLE);
                                infoText.setVisibility(View.VISIBLE);
                                done.setVisibility(View.VISIBLE);

                                String finalNamme = dataSnapshot.child("name").getValue().toString();
                                name.setText(finalNamme);

                                String retrivePrfileImage=dataSnapshot.child("image").getValue().toString();
                                Picasso.get().load(retrivePrfileImage).placeholder(R.drawable.profile).into(profile);
                                check=0;

                            }else if(dataSnapshot.exists()){

                                recever_info.setVisibility(View.VISIBLE);
                                infoText.setVisibility(View.VISIBLE);
                                done.setVisibility(View.VISIBLE);

                                String finalNamme = dataSnapshot.child("name").getValue().toString();
                                name.setText(finalNamme);
                                check=0;

                            }else{

                                check=1;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

//                if(check==1){
//                    Toast.makeText(LicturePermission.this, "You enter a wrong user id...", Toast.LENGTH_SHORT).show();
//                }
            }


        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotesRef.child(nId).child("p_user").push().setValue(userId).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        Toast.makeText(LicturePermission.this, "Id updated.", Toast.LENGTH_SHORT).show();
                        inputUserId.setText("");
                        recever_info.setVisibility(View.GONE);
                        infoText.setVisibility(View.GONE);
                        done.setVisibility(View.GONE);
                    }
                });

            }
        });


    }
}
