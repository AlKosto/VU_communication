package com.example.imran.vucommunication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner, spinner_program;
    String program_name, dept_name;
    private EditText setUserName, setUserStatus;

    private Button updatebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        spinner = (Spinner)findViewById(R.id.spinner_dept_name);
        spinner_program =(Spinner)findViewById(R.id.spinner_program_name);
        setUserName =(EditText) findViewById(R.id.set_user_name);
        setUserStatus =(EditText) findViewById(R.id.set_profile_status);
        updatebtn = (Button) findViewById(R.id.update_setting_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.dept_names,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserName.setText(spinner.getSelectedItem().toString());
                setUserStatus.setText(spinner_program.getSelectedItem().toString());
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       dept_name = parent.getItemAtPosition(position).toString();


        if (spinner.getSelectedItem().equals("Computer science and Engineering")) {
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.program_names_cse, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter2);
     //       spinner_program.setOnItemSelectedListener(this);
            program_name =spinner_program.getSelectedItem().toString();
            //  setUserStatus.setText(spinner_program.getSelectedItem().toString());

        }
        else if (spinner.getSelectedItem().equals("Business Administration")) {
        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_name_business, android.R.layout.simple_spinner_item);
        spinner_program.setAdapter(adapter3);
    //    spinner_program.setOnItemSelectedListener(this);
        program_name =spinner_program.getSelectedItem().toString();
            //       setUserStatus.setText(spinner_program.getSelectedItem().toString());
    }
    else if(spinner.getSelectedItem().equals("Electrical and Electronic Engineering")){
        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_name_eee, android.R.layout.simple_spinner_item);
        spinner_program.setAdapter(adapter3);
   //     spinner_program.setOnItemSelectedListener(this);
        program_name =spinner_program.getSelectedItem().toString();
            //      setUserStatus.setText(spinner_program.getSelectedItem().toString());
    } else if (spinner.getSelectedItem().equals("Economics")) {
        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_in_economics, android.R.layout.simple_spinner_item);
        spinner_program.setAdapter(adapter3);
   //     spinner_program.setOnItemSelectedListener(this);
        program_name =spinner_program.getSelectedItem().toString();
            //       setUserStatus.setText(spinner_program.getSelectedItem().toString());
    } else if(spinner.getSelectedItem().equals("English")){
        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_name_english, android.R.layout.simple_spinner_item);
        spinner_program.setAdapter(adapter3);
     //   spinner_program.setOnItemSelectedListener(this);
       program_name =spinner_program.getSelectedItem().toString();
            //       setUserStatus.setText(spinner_program.getSelectedItem().toString());
        } else if (spinner.getSelectedItem().equals("Journalism, Communication and Media Studies")) {
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_in_Journalism, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
     //       spinner_program.setOnItemSelectedListener(this);
           program_name =spinner_program.getSelectedItem().toString();
            //        setUserStatus.setText(spinner_program.getSelectedItem().toString());
        } else if(spinner.getSelectedItem().equals("Law and Human Rights")){
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_name_law, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
     //       spinner_program.setOnItemSelectedListener(this);
            program_name =spinner_program.getSelectedItem().toString();
            //       setUserStatus.setText(spinner_program.getSelectedItem().toString());
        } else if (spinner.getSelectedItem().equals("Pharmacy")) {
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_in_Pharmacy, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
     //       spinner_program.setOnItemSelectedListener(this);
           program_name =spinner_program.getSelectedItem().toString();
            //         setUserStatus.setText(spinner_program.getSelectedItem().toString());
        } else if(spinner.getSelectedItem().equals("Public Health")){
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_in_public_health, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
     //       spinner_program.setOnItemSelectedListener(this);
          program_name =spinner_program.getSelectedItem().toString();
            //      setUserStatus.setText(spinner_program.getSelectedItem().toString());
        } else if(spinner.getSelectedItem().equals("Sociology")){
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_in_Sociology, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
    //        spinner_program.setOnItemSelectedListener(this);
           program_name =spinner_program.getSelectedItem().toString();
            //    setUserStatus.setText(spinner_program.getSelectedItem().toString());
        } else if(spinner.getSelectedItem().equals("Political Science")){
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_in_Political_Science, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
    //        spinner_program.setOnItemSelectedListener(this);
            program_name =spinner_program.getSelectedItem().toString();
          //  setUserStatus.setText(spinner_program.getSelectedItem().toString());
        } else if(spinner.getSelectedItem().equals("Applied Statistics")){
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_in_Applied_Statistics, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
     //       spinner_program.setOnItemSelectedListener(this);
            program_name =spinner_program.getSelectedItem().toString();
        //    setUserStatus.setText(spinner_program.getSelectedItem().toString());
        } else if(spinner.getSelectedItem().equals("Islamic History")){
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.program_in_Islamic_History, android.R.layout.simple_spinner_item);
            spinner_program.setAdapter(adapter3);
          //  spinner_program.setOnItemSelectedListener(this);
            program_name =spinner_program.getSelectedItem().toString();
          //  setUserStatus.setText(spinner_program.getSelectedItem().toString());
        }





    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
