package com.example.imran.vucommunication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAcoountButton;
    private EditText UserEmail, UserPassword;
    private TextView AlreadyHaveAnAccount;
    private ProgressDialog lodingBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth= FirebaseAuth.getInstance();
        InitializeFields();

        AlreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginActivity();
            }
        });


        CreateAcoountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });

    }

    private void createNewAccount() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter Your Email..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter Your Password..", Toast.LENGTH_SHORT).show();
        }
        else{


            lodingBar.setTitle("Creating new Account");
            lodingBar.setMessage("Please wait, while account create is complete ");
            lodingBar.setCanceledOnTouchOutside(true);
            lodingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                sendUserToLoginActivity();
                                Toast.makeText(RegisterActivity.this, "Account Create Successful... ", Toast.LENGTH_SHORT).show();
                                lodingBar.dismiss();
                            }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error :" + message, Toast.LENGTH_SHORT).show();
                                lodingBar.dismiss();
                            }
                        }
                    });
        }
    }


    private void InitializeFields() {
        CreateAcoountButton = (Button) findViewById(R.id.register_button);
        UserEmail =(EditText)findViewById(R.id.register_email);
        UserPassword =(EditText) findViewById(R.id.register_password);
        AlreadyHaveAnAccount =(TextView) findViewById(R.id.already_have_an_account_link);

        lodingBar = new ProgressDialog(this);
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

}

























