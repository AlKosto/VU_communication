package com.example.imran.vucommunication;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner, spinner_program;
    FloatingActionButton fab_plus, fab_images, fab_pdf , fab_zip;
    Animation FabOpen, FabClose, FabRClockwise, FabRanticlckwise;
    String program_name, dept_name;
    boolean isOpen = false;
    private ScrollView notesUploadView;
    RadioButton freeRadioBtn, paidRadioBtn, bKashRadioBtn, dbblRadioBtn;
    private LinearLayout paymentLayout, numberAndPriceLayout;
    private CircleImageView profileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef ,NotesRef;
    private String currentUserID  ,currentDate, currentTime;
    private EditText subName, subTopic, subDescription, acPhoneNo, bdPrice;
    private RecyclerView notesList;
    private WebView dWebView;
    String notesKey,finalNamme;
    String fileName;
    int chekper=0;
    Uri pdfUri;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);


        initializafield();




        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        currentUserID = mAuth.getCurrentUser().getUid();
        NotesRef = FirebaseDatabase.getInstance().getReference().child("Notes");

        showProfileImage();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.dept_names,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



        LinearLayoutManager uLinerarLayoutManager= new LinearLayoutManager(this);
        uLinerarLayoutManager.setReverseLayout(true);
        uLinerarLayoutManager.setStackFromEnd(true);
        notesList =(RecyclerView)findViewById(R.id.notes_list);
        notesList.setLayoutManager(uLinerarLayoutManager);



        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen)
                {
                    fab_images.startAnimation(FabClose);
                    fab_pdf.startAnimation(FabClose);
                    fab_zip.startAnimation(FabClose);
                    fab_plus.startAnimation(FabRanticlckwise);
                    fab_zip.setClickable(false);
                    fab_pdf.setClickable(false);
                    fab_images.setClickable(false);
                    isOpen= false;

                    notesUploadView.setVisibility(View.GONE);

                }else {
                    fab_images.startAnimation(FabOpen);
                    fab_pdf.startAnimation(FabOpen);
                    fab_zip.startAnimation(FabOpen);
                    fab_plus.startAnimation(FabRClockwise);
                    fab_zip.setClickable(true);
                    fab_pdf.setClickable(true);
                    fab_images.setClickable(true);
                    isOpen= true;

                    notesUploadView.setVisibility(View.VISIBLE);
                }
            }
        });

        fab_zip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAvobeInfo()){
                    uploadInfo();

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/zip");
                    startActivityForResult(intent, 43);
                }
            }
        });


        fab_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAvobeInfo()){
                    uploadInfo();
                    Intent intent = new Intent(NotesActivity.this, multipleSelectActivity.class);
                    intent.putExtra("notesKey", notesKey);
                    startActivity(intent);

                }   }
        });

        fab_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkAvobeInfo()){
                    uploadInfo();
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, 86);
                }
            }
        });



        freeRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentLayout.setVisibility(View.GONE);
                numberAndPriceLayout.setVisibility(View.GONE);
            }
        });


        freeRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bKashRadioBtn.setChecked(false);
                dbblRadioBtn.setChecked(false);
                paymentLayout.setVisibility(View.GONE);
                numberAndPriceLayout.setVisibility(View.GONE);
                acPhoneNo.setText("");
                bdPrice.setText("");
            }
        });

        paidRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentLayout.setVisibility(View.VISIBLE);
                numberAndPriceLayout.setVisibility(View.VISIBLE);
            }
        });

        bKashRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dbblRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Notes> options =
                new FirebaseRecyclerOptions.Builder<Notes>()
                        .setQuery(NotesRef, Notes.class)
                        .build();


        FirebaseRecyclerAdapter<Notes, UniNotesViewHolder> adapter = new FirebaseRecyclerAdapter<Notes, UniNotesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final UniNotesViewHolder holder, int position, @NonNull final Notes model) {


                final String list_user_id = model.getId();
                final String[] userImage = new String[1];
                final String nkey=model.getNid();
                final String freeOrPaid = model.getPaid_or_free();
                final String[] userName = new String[1];
//                holder.subName.setText(model.getSub_name());
//                holder.topicName.setText(model.getTopic());
//                holder.pTime.setText(model.getCurrentTime());
//                holder.pDate.setText(model.getCurrentDate());


                RootRef.child("Users").child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()&&dataSnapshot.hasChild("image")){

                            userImage[0] = dataSnapshot.child("image").getValue().toString();

                            userName[0] = dataSnapshot.child("name").getValue().toString();
                            Picasso.get().load(userImage[0]).placeholder(R.drawable.profile).into(holder.notesProviderImage);
                        }else{

                        }

                            holder.subDescription.setText(model.getSub_description());
                            holder.subName.setText(model.getSub_name());
                            holder.topicName.setText(model.getTopic());
                            holder.pTime.setText(model.getCurrentTime());
                            holder.pDate.setText(model.getCurrentDate());
                            holder.dataTyoe.setText(model.getFtype());

                            if(freeOrPaid.equals("free")){
                                holder.paidPriceFree.setText("Free");
                                holder.buyInfoLayout.setVisibility(View.GONE);
                                holder.buyBtn.setVisibility(View.GONE);


                                holder.dataTyoe.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        dWebView.loadUrl(model.getFile());
                                        dWebView.setDownloadListener(new DownloadListener() {
                                            @Override
                                            public void onDownloadStart(String url, String userAgent,
                                                                        String contentDisposition, String mimetype,
                                                                        long contentLength) {
                                                DownloadManager.Request vuRequest = new DownloadManager.Request(Uri.parse(url));
                                                vuRequest.allowScanningByMediaScanner();
                                                vuRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                                                DownloadManager vuManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                                vuManager.enqueue(vuRequest);

                                                Toast.makeText(NotesActivity.this, "File is downloading", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });




                            }
                            else if(freeOrPaid.equals("paid")) {

                                if(list_user_id.equals(currentUserID)){

                                    holder.paidPriceFree.setText("+ People");

                                    holder.buyBtn.setVisibility(View.GONE);
                                    holder.buyInfoLayout.setVisibility(View.GONE);
                                    holder.paidPriceFree.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Intent I=new Intent(NotesActivity.this,LicturePermission.class);
                                            I.putExtra("nid",model.getNid());
                                            startActivity(I);

                                        }
                                    });
                                }else {

                                    holder.paidPriceFree.setText("Price:"+model.getPrice());
                                    holder.buyInfoLayout.setVisibility(View.GONE);
                                    holder.buyBtn.setVisibility(View.VISIBLE);
                                }




                                holder.buyBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        holder.buyBtn.setVisibility(View.GONE);
                                        holder.buyInfoLayout.setVisibility(View.VISIBLE);
                                        holder.paymenMethod.setText("You have to paid by "+model.getPayment_method());
                                        holder.paymentACNumber.setText(model.getPayment_method()+" account no: "+model.ac_phone_number);

                                        holder.buyCancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                holder.buyInfoLayout.setVisibility(View.GONE);
                                                holder.buyBtn.setVisibility(View.VISIBLE);
                                            }
                                        });



                                        holder.buyConfirmbtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                String messageText = holder.TxnId.getText().toString();

                                                if(TextUtils.isEmpty(messageText)){
                                                    Toast.makeText(NotesActivity.this, "Please Enter TxnId", Toast.LENGTH_SHORT).show();
                                                }else {

                                                    final String messageTextt="I am send balance for your lecture.\n" +
                                                            "Subject name: "+model.getSub_name()+"\n"
                                                            +"Topic is:"+model.getTopic()
                                                            +"\nBalance TxnId is: "+messageText
                                                            +"\nMy Id is: "+currentUserID
                                                            +"\nNow add this id to  your lecture for visible it to me.";



                                                    RootRef.child("Contacts").child(currentUserID).child(list_user_id).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if(dataSnapshot.exists()){
                                                                Intent chatIntent = new Intent(NotesActivity.this ,ChatActivity.class);
                                                                chatIntent.putExtra("visit_user_id", list_user_id);
                                                                chatIntent.putExtra("visit_user_name", userName[0]);
                                                                chatIntent.putExtra("visit_image", userImage[0]);
                                                                chatIntent.putExtra("message_text",messageTextt);

                                                                startActivity(chatIntent);
                                                            }
                                                            else {
                                                                Toast.makeText(NotesActivity.this, "After Accepted you chat request have to try again", Toast.LENGTH_LONG).show();
                                                                Intent profileIntent = new Intent( NotesActivity.this, ProfileActivity.class);
                                                                profileIntent.putExtra("visit_user_id", list_user_id);
                                                                startActivity(profileIntent);
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });


                                                }








                                            }
                                        });

                                    }
                                });


                                holder.dataTyoe.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        NotesRef.child(model.getNid()).child("p_user").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                                                String  CC= dataSnapshot.getValue().toString();



                                                if(CC.equals(currentUserID)){
                                                    dWebView.loadUrl(model.getFile());
                                                    dWebView.setDownloadListener(new DownloadListener() {@Override
                                                        public void onDownloadStart(String url, String userAgent,
                                                                                    String contentDisposition, String mimetype,
                                                                                    long contentLength) {
                                                            DownloadManager.Request vuRequest = new DownloadManager.Request(Uri.parse(url));
                                                            vuRequest.allowScanningByMediaScanner();
                                                            vuRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                                                            DownloadManager vuManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                                            vuManager.enqueue(vuRequest);

                                                            Toast.makeText(NotesActivity.this, "File is downloading", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                                else {
                                                    chekper=1;
                                                 }

                                            }

                                            @Override
                                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                            }

                                            @Override
                                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                                            }

                                            @Override
                                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                        if(chekper==1){
                                            Toast.makeText(NotesActivity.this, "You Have To Buy It.", Toast.LENGTH_SHORT).show();
                                        }


                                    }


                                });

                            }


                        holder.nMessageBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                RootRef.child("Contacts").child(currentUserID).child(list_user_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            Intent chatIntent = new Intent(NotesActivity.this ,ChatActivity.class);
                                            chatIntent.putExtra("visit_user_id", list_user_id);
                                            chatIntent.putExtra("visit_user_name", userName[0]);
                                            chatIntent.putExtra("visit_image", userImage[0]);
                                            chatIntent.putExtra("message_text","");
                                            startActivity(chatIntent);
                                        }
                                        else {
                                            Intent profileIntent = new Intent( NotesActivity.this, ProfileActivity.class);
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


                        holder.nCommentBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent chatIntent = new Intent(NotesActivity.this ,CommentActivity.class);
                                chatIntent.putExtra("post_id", nkey);
                                chatIntent.putExtra("check","note");
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
            public UniNotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notes_single_layout, viewGroup, false);

                UniNotesViewHolder viewHolder = new UniNotesViewHolder(view);
                return viewHolder;
            }
        };
        notesList.setAdapter(adapter);
        adapter.startListening();


    }




    public class UniNotesViewHolder extends RecyclerView.ViewHolder{

        CircleImageView notesProviderImage;
        TextView subName,topicName, pDate, pTime, paidPriceFree, subDescription,dataTyoe, paidOrFree, paymenMethod,paymentACNumber;
        FloatingActionButton currentDate,fab_setting,fab_delete;
        LinearLayout buyInfoLayout;
        Button buyBtn, buyCancel,buyConfirmbtn;
        ImageView nCommentBtn,nMessageBtn;
        EditText TxnId;

        public UniNotesViewHolder(@NonNull View itemView) {
            super(itemView);

            TxnId=itemView.findViewById(R.id.txnid_input);
            buyConfirmbtn=itemView.findViewById(R.id.buy_ok_btn);
            nMessageBtn=itemView.findViewById(R.id.message_btn);
            nCommentBtn = itemView.findViewById(R.id.comment_btn);
            paymentACNumber = itemView.findViewById(R.id.payment_ac_no);
            paymenMethod = itemView.findViewById(R.id.payment_method);
            buyCancel = itemView.findViewById(R.id.buy_cancel_btn);
            buyBtn = itemView.findViewById(R.id.buy_btn);
            buyInfoLayout = itemView.findViewById(R.id.buy_info_layout);
            dataTyoe= itemView.findViewById(R.id.data_type);
            subDescription = itemView.findViewById(R.id.description_show);
            notesProviderImage = itemView.findViewById(R.id.profile_image);
            subName = itemView.findViewById(R.id.sub_name_show);
            topicName = itemView.findViewById(R.id.topic_show);
            pDate = itemView.findViewById(R.id.post_date);
            pTime = itemView.findViewById(R.id.post_time);
            paidPriceFree = itemView.findViewById(R.id.paid_price_free);


        }



    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==86 && resultCode == RESULT_OK && data!= null){
            pdfUri = data.getData();


            Uri fileUri = data.getData();
            fileName = getFileName(fileUri);


            if(pdfUri != null) {
                uploadPdf(pdfUri);
            }else {
                Toast.makeText(NotesActivity.this, "You have to select a file", Toast.LENGTH_SHORT).show();
            }

        }

        if(requestCode==43 && resultCode == RESULT_OK && data!= null){
            pdfUri = data.getData();


            Uri fileUri = data.getData();
            fileName = getFileName(fileUri);


            if(pdfUri != null) {
                uploadZip(pdfUri);
            }else {
                Toast.makeText(NotesActivity.this, "You have to select a file", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void uploadZip(Uri pdfUri) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading Zip...");
        progressDialog.setProgress(0);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("notes");


        String extension1 = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension1 = fileName.substring(i+1);
        }


        final String finalExtension1 = extension1;


        storageReference.child("zip").child(fileName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        String url = taskSnapshot.getDownloadUrl().toString();
                        DatabaseReference reference = NotesRef.child(notesKey).child("file");
                        reference.setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    NotesRef.child(notesKey).child("ftype").setValue(finalExtension1);
                                    Toast.makeText(NotesActivity.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }else {
                                    Toast.makeText(NotesActivity.this, "File not uploaded", Toast.LENGTH_SHORT).show();

                                }
                            }

                        });

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                progressDialog.show();
                int currentProgress =(int) (100*taskSnapshot.getBytesTransferred()/ taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);


            }
        });


    }


    private void uploadPdf(Uri pdfUri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File...");
        progressDialog.setProgress(0);
        progressDialog.show();


        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("notes");


        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }


        final String finalExtension = extension;
        storageReference.child("files").child(fileName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        String url = taskSnapshot.getDownloadUrl().toString();
                        DatabaseReference reference = NotesRef.child(notesKey).child("file");
                        reference.setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    NotesRef.child(notesKey).child("ftype").setValue(finalExtension);

                                    Toast.makeText(NotesActivity.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();

                                    progressDialog.dismiss();

                                }else {
                                    Toast.makeText(NotesActivity.this, "File not uploaded", Toast.LENGTH_SHORT).show();

                                }
                            }

                        });

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                int currentProgress =(int) (100*taskSnapshot.getBytesTransferred()/ taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);


            }
        });


    }


    private boolean checkAvobeInfo() {

        program_name =spinner_program.getSelectedItem().toString();

        String subjectName = subName.getText().toString();
        String subjectTopic = subTopic.getText().toString();
        String subjectDiscription= subDescription.getText().toString();
        String accuntNumber = acPhoneNo.getText().toString();
        String price = bdPrice.getText().toString();

        if(TextUtils.isEmpty(subjectName)){
            Toast.makeText(this, "Enter Subject Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(subjectTopic)){
            Toast.makeText(this, "Enter Topic", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(subjectDiscription)){
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show();
        }
        else if(dept_name.equals("Select Your Department")){
            Toast.makeText(this, "Select One From Departments", Toast.LENGTH_SHORT).show();
        }
        else if(program_name.equals("Select Your Program")){
            Toast.makeText(this, "Select Yor Program name", Toast.LENGTH_SHORT).show();
        }
        else if(!freeRadioBtn.isChecked() && !paidRadioBtn.isChecked()){
            Toast.makeText(this, "Check paid or free", Toast.LENGTH_SHORT).show();
        }

        else if(paidRadioBtn.isChecked()  && !bKashRadioBtn.isChecked() && !dbblRadioBtn.isChecked()){
            Toast.makeText(this, "select a payment method", Toast.LENGTH_SHORT).show();
        }

        else if((bKashRadioBtn.isChecked() || dbblRadioBtn.isChecked()) && TextUtils.isEmpty(accuntNumber)&& TextUtils.isEmpty(price)){
            Toast.makeText(this, "Enter account number and price", Toast.LENGTH_SHORT).show();
        }
        else{
           return true;
        }
        return false;

    }



    private void uploadInfo(){

        String SubNameText = subName.getText().toString();
        String SubTopicsText = subTopic.getText().toString();
        String SubDescriptionText = subDescription.getText().toString();
        String deptNameText = dept_name;
        String progNameText= program_name;
        String bdPriceText ;
        String paidorfreeText, pamymentMethod,acNumber;

        if(freeRadioBtn.isChecked()){
            paidorfreeText= "free";
            pamymentMethod ="";
            bdPriceText ="";
            acNumber= "";
        }else {
            paidorfreeText= "paid";
            acNumber = acPhoneNo.getText().toString();
            bdPriceText = bdPrice.getText().toString();
            if(bKashRadioBtn.isChecked()){
                pamymentMethod = "bKash";
            }else {
                pamymentMethod = "DDBL";
            }
        }

        notesKey = NotesRef.push().getKey();

        Calendar callForDate =Calendar.getInstance();
        SimpleDateFormat currentDateFormate = new SimpleDateFormat("MMM dd,yyyy");
        currentDate = currentDateFormate.format(callForDate.getTime());


        Calendar callForTime =Calendar.getInstance();
        SimpleDateFormat currentTimeFormate = new SimpleDateFormat("hh:mm a");
        currentTime = currentTimeFormate.format(callForTime.getTime());

        HashMap<String, Object> postInforMap = new HashMap<>();
        postInforMap.put("id", currentUserID);
        postInforMap.put("sub_name", SubNameText);
        postInforMap.put("topic", SubTopicsText);
        postInforMap.put("sub_description", SubDescriptionText);
        postInforMap.put("dept_name", deptNameText);
        postInforMap.put("prog_name", progNameText);
        postInforMap.put("paid_or_free", paidorfreeText);
        postInforMap.put("ac_phone_number", acNumber);
        postInforMap.put("payment_method", pamymentMethod);
        postInforMap.put("price", bdPriceText);
        postInforMap.put("currentDate", currentDate);
        postInforMap.put("currentTime" , currentTime);
        postInforMap.put("nid", notesKey);
        postInforMap.put("ftype", "images");

        NotesRef.child(notesKey).updateChildren(postInforMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(NotesActivity.this, "info uploaded", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    private void showProfileImage() {

        RootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("image")){

                    finalNamme=dataSnapshot.child("name").getValue().toString();

                    String retrivePrfileImage=dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(retrivePrfileImage).placeholder(R.drawable.profile).into(profileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void initializafield() {

        spinner = (Spinner)findViewById(R.id.spinner_dept_name);
        spinner_program =(Spinner)findViewById(R.id.spinner_program_name);

        freeRadioBtn =(RadioButton) findViewById(R.id.free_radio_btn);
        paidRadioBtn =(RadioButton)findViewById(R.id.paid_radio_btn);
        bKashRadioBtn =(RadioButton) findViewById(R.id.biKash_redio_btn);
        dbblRadioBtn =(RadioButton)findViewById(R.id.dbbl_radio_bank);

        fab_plus = (FloatingActionButton)findViewById(R.id.fab_plus);
        fab_images = (FloatingActionButton) findViewById(R.id.fab_images);
        fab_pdf= (FloatingActionButton) findViewById(R.id.fab_pdf);
        fab_zip = (FloatingActionButton)findViewById(R.id.fab_zip) ;
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabRClockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        FabRanticlckwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
        notesUploadView =(ScrollView) findViewById(R.id.notes_upload_view);
        numberAndPriceLayout = (LinearLayout)findViewById(R.id.number_and_price_layout);
        paymentLayout =(LinearLayout)findViewById(R.id.payment_method_layout);

        profileImage=(CircleImageView)findViewById(R.id.profile_image);
        subName = (EditText)findViewById(R.id.sub_name);
        subTopic= (EditText)findViewById(R.id.sub_topic);
        subDescription =(EditText)findViewById(R.id.sub_description);

        acPhoneNo =(EditText)findViewById(R.id.ac_paid_number);
        bdPrice =(EditText)findViewById(R.id.bd_price);
        dWebView=(WebView)findViewById(R.id.dWebViw);
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

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


}
