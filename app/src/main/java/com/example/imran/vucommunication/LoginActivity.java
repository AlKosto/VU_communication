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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {


    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private ProgressDialog lodingBar;
    private Button LoginButton, PhoneLoginButton;
    private EditText UserEmail, UserPassword;
    private TextView NeedNewAccountLink, ForgetPasswordLink;
    private DatabaseReference UserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference("Users") ;
        UserRef.keepSynced(true);

        currentUser= mAuth.getCurrentUser();

        InitializeFields();

        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegisterActivity();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowUserToLgin();
            }
        });

        PhoneLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneLoginIntent = new Intent(LoginActivity.this, PhoneLoginActivity.class);
                startActivity(phoneLoginIntent);
            }
        });
    }

    private void allowUserToLgin() {

        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter Your Email..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter Your Password..", Toast.LENGTH_SHORT).show();
        }else {

            lodingBar.setTitle("Sign In");
            lodingBar.setMessage("Please wait, while you are sign in... ");
            lodingBar.setCanceledOnTouchOutside(true);
            lodingBar.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        String currentUserId = mAuth.getCurrentUser().getUid();
                        String deviceToken = FirebaseInstanceId.getInstance().getToken();
                        UserRef.child(currentUserId).child("device_token")
                                .setValue(deviceToken)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            lodingBar.dismiss();
                                            checkEmaiVerification();
                                        }
                                    }
                                });


                    }
                    else {
                        String message = task.getException().toString();
                        Toast.makeText(LoginActivity.this, "Error :" + message, Toast.LENGTH_SHORT).show();
                        lodingBar.dismiss();
                    }
                }
            });

        }
    }


    private void checkEmaiVerification(){

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        Boolean emailflag = firebaseUser.isEmailVerified();

        if(emailflag){

            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
            Toast.makeText(LoginActivity.this, "Loged In Successful...", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {

            Toast.makeText(this, "Email is not verified so verify..", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }

    private void InitializeFields() {
        LoginButton = (Button) findViewById(R.id.login_button);
        PhoneLoginButton =(Button)findViewById(R.id.phone_login_button);
        UserEmail =(EditText)findViewById(R.id.login_email);
        UserPassword =(EditText) findViewById(R.id.login_password);
        NeedNewAccountLink =(TextView) findViewById(R.id.need_new_account_link);
        ForgetPasswordLink = (TextView) findViewById(R.id.forget_password_link);

        lodingBar = new ProgressDialog(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser != null)
        {
            sendUserToMainActivity();
        }
    }

    private void sendUserToRegisterActivity() {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}





















