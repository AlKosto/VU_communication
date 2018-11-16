package com.example.imran.vucommunication;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class multipleSelectActivity extends AppCompatActivity {

    private TextView mSelectBtn;
    private RecyclerView mUploadList;
    private static final int RESULT_LOAD_IMAGE = 1;
    private List<String> fileNameList;
    private List<String> fileDoneList;
    private UploadListAdater uploadListAdater;
    private StorageReference mStorage;
    private DatabaseReference NotesRef;
    String notesKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_select);


        mStorage = FirebaseStorage.getInstance().getReference();

        mSelectBtn = (TextView) findViewById(R.id.select_btn);
        mUploadList = (RecyclerView) findViewById(R.id.upload_list);
        notesKey = getIntent().getExtras().get("notesKey").toString();
        NotesRef = FirebaseDatabase.getInstance().getReference().child("Notes");

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        uploadListAdater = new UploadListAdater(fileNameList, fileDoneList);


        mUploadList.setLayoutManager(new LinearLayoutManager(this));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadListAdater);


        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);

            }
        });

    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==RESULT_LOAD_IMAGE && resultCode == RESULT_OK){

            if(data.getClipData() != null){

                int totalItemSelected = data.getClipData().getItemCount();

                for (int i =0 ; i<totalItemSelected; i++){

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getFileName(fileUri);
                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");
                    uploadListAdater.notifyDataSetChanged();

                    StorageReference fileToUpload = mStorage.child("notes").child("images").child(fileName);

                    final int finalI = i;
                    fileToUpload.putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                            final String downloadUrl = task.getResult().getDownloadUrl().toString();

                            NotesRef.child(notesKey).child("images").child(String.valueOf(finalI)).setValue(downloadUrl);


                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");

                            uploadListAdater.notifyDataSetChanged();
                        }
                    });
                }


                //  Toast.makeText(this, "Selected Multiple file", Toast.LENGTH_SHORT).show();
            }
            else {

                Uri fileUri = data.getData();
                String fileName = getFileName(fileUri);
                StorageReference fileToUpload = mStorage.child("Images").child(fileName);
                fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(multipleSelectActivity.this, "File is uploaded", Toast.LENGTH_SHORT).show();
                    }
                });


                // Toast.makeText(this, "Selected Single file", Toast.LENGTH_SHORT).show();
            }
        }
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
