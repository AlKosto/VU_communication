package com.example.imran.vucommunication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UploadListAdater extends RecyclerView.Adapter<UploadListAdater.ViewHolder> {

    public List<String> fileNameList;
    public List<String> fileDoneList;

    public UploadListAdater(List<String> fileNameList , List<String>fileDoneList){

        this.fileDoneList = fileDoneList;
        this.fileNameList = fileNameList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_for_multiple_upload, viewGroup,false);
        return new  ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String fileName = fileNameList.get(i);
        viewHolder.fileNameView.setText(fileName);

        String fileDone = fileDoneList.get(i);

        if(fileDone.equals("uploading")){

            viewHolder.fileDoneView.setImageResource(R.drawable.ic_progress);

        } else {

            viewHolder.fileDoneView.setImageResource(R.drawable.ic_checked);

        }
    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public TextView fileNameView;
        public ImageView fileDoneView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            fileNameView =(TextView) mView.findViewById(R.id.upload_filename);
            fileDoneView =(ImageView) mView.findViewById(R.id.upload_loading);

        }
    }
}

























