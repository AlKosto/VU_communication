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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    private Button sendVerificationCodeButton, verifyButton;
    private EditText InputPhoneNumber, InputVerificationCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private ProgressDialog lodingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);


        sendVerificationCodeButton =(Button)findViewById(R.id.send_ver_code_button);
        verifyButton =(Button)findViewById(R.id.verify_button);
        InputPhoneNumber =(EditText) findViewById(R.id.phone_number_input);
        InputVerificationCode =(EditText) findViewById(R.id.verification_code_input);
        mAuth = FirebaseAuth.getInstance();
        lodingBar= new ProgressDialog(this);


        sendVerificationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String phoneNumber = InputPhoneNumber.getText().toString();

                if(TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(PhoneLoginActivity.this, "Enter Your Phone Number First", Toast.LENGTH_SHORT).show();
                }

                else {


                    lodingBar.setTitle("Phone Verification.");
                    lodingBar.setMessage("Please wait, while we are authenticating your phone..");
                    lodingBar.setCanceledOnTouchOutside(false);
                    lodingBar.show();


                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            PhoneLoginActivity.this,               // Activity (for callback binding)
                            callbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        });



        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCodeButton.setVisibility(View.INVISIBLE);
                InputPhoneNumber.setVisibility(View.INVISIBLE);

                String verifcationCode = InputVerificationCode.getText().toString();

                if(TextUtils.isEmpty(verifcationCode)){
                    Toast.makeText(PhoneLoginActivity.this, "Enter Verification Code First...", Toast.LENGTH_SHORT).show();
                }
                else {



                    lodingBar.setTitle("Verification Code.");
                    lodingBar.setMessage("Please wait, while we are authenticating your verification code..");
                    lodingBar.setCanceledOnTouchOutside(false);
                    lodingBar.show();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verifcationCode);
                    signInWithPhoneAuthCredential(credential);

                }
            }
        });


        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                lodingBar.dismiss();

                Toast.makeText(PhoneLoginActivity.this, "Invalid Phone Number, Please enter correct phone number with country code..", Toast.LENGTH_LONG).show();

                sendVerificationCodeButton.setVisibility(View.VISIBLE);
                InputPhoneNumber.setVisibility(View.VISIBLE);

                verifyButton.setVisibility(View.INVISIBLE);
                InputVerificationCode.setVisibility(View.INVISIBLE);

            }

            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                lodingBar.dismiss();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                Toast.makeText(PhoneLoginActivity.this, "Code has been sent, please check it to verify..", Toast.LENGTH_SHORT).show();


                sendVerificationCodeButton.setVisibility(View.INVISIBLE);
                InputPhoneNumber.setVisibility(View.INVISIBLE);

                verifyButton.setVisibility(View.VISIBLE);
                InputVerificationCode.setVisibility(View.VISIBLE);
            }
        };
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            lodingBar.dismiss();
                            Toast.makeText(PhoneLoginActivity.this, "Congratulation, you are logged in successfully", Toast.LENGTH_SHORT).show();
                            sendUserToMainActivity();
                        } else {

                            String error = task.getException().toString();
                            Toast.makeText(PhoneLoginActivity.this, "Error: " +error, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(PhoneLoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}









