package com.example.imran.vucommunication;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class NotesActivity extends AppCompatActivity {

    FloatingActionButton fab_plus, fab_images, fab_pdf , fab_zip;
    Animation FabOpen, FabClose, FabRClockwise, FabRanticlckwise;
    boolean isOpen = false;

    private TextView addImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        fab_plus = (FloatingActionButton)findViewById(R.id.fab_plus);
        fab_images = (FloatingActionButton) findViewById(R.id.fab_images);
        fab_pdf= (FloatingActionButton) findViewById(R.id.fab_pdf);
        fab_zip = (FloatingActionButton)findViewById(R.id.fab_zip) ;
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabRClockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        FabRanticlckwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);


        addImages= (TextView)findViewById(R.id.addImages);

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

                }else {
                    fab_images.startAnimation(FabOpen);
                    fab_pdf.startAnimation(FabOpen);
                    fab_zip.startAnimation(FabOpen);
                    fab_plus.startAnimation(FabRClockwise);
                    fab_zip.setClickable(true);
                    fab_pdf.setClickable(true);
                    fab_images.setClickable(true);
                    isOpen= true;
                }
            }
        });

        fab_zip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NotesActivity.this, "Fuck zip", Toast.LENGTH_SHORT).show();
            }
        });


        addImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesActivity.this, multipleSelectActivity.class));
            }
        });

    }
}
