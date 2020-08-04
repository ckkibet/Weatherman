package ckibet.tamarix.zeweather;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    EditText mEmailAddress;
    EditText mPassword;
    Button mSignUp;
    Button mSignin;
    TextView mForgotPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user!=null){
            Intent intent = new Intent(getApplicationContext(), Weather.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmailAddress = findViewById(R.id.login_email_address);
        mPassword = findViewById(R.id.login_password);
        mSignin = findViewById(R.id.sign_in);
        mSignUp = findViewById(R.id.login_sign_up);
        mForgotPassword = findViewById(R.id.forgot_password);
        mAuth = FirebaseAuth.getInstance();


        signIn();
        signup();

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword(view);
            }
        });

    }

    private void signup() {

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Sign_up.class));
                finish();
            }
        });

    }

        private void signIn() {

            mSignin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Signing in");

                    String login_email = mEmailAddress.getText().toString().trim();
                    String password = mPassword.getText().toString();

                    if (TextUtils.isEmpty(login_email)){
                        mEmailAddress.setError("Email is required");
                        return;
                    }
                    if (TextUtils.isEmpty(password)){
                        mPassword.setError("Password is required");
                        return;
                    }
                    progressDialog.show();
                    progressDialog.create();
                    mAuth.signInWithEmailAndPassword(login_email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), Weather.class));
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });

        }

        private void forgotPassword(View view){
            final EditText editText = new EditText(this);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog
                    .setTitle("Reset Password")
                    .setMessage("Enter Email Address to reset password")
                    .setPositiveButton("Proceed" ,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

            alertDialog.setView(editText);

            alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog alert = alertDialog.create();
            alert.show();

            alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Sending Email");

                    boolean isError = false;
                    final String string = editText.getText().toString().trim();

                    if (string.isEmpty()){
                        isError=true;
                        editText.setError("Email cannot be empty!");
                    }
                    if (!isError){
                        progressDialog.show();
                        progressDialog.create();
                        mAuth.sendPasswordResetEmail(string).addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Reset Email sent to " +string, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        };
                    });
                }
            }
        });
    }
}