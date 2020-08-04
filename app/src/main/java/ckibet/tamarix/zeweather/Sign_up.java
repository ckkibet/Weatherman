package ckibet.tamarix.zeweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_up extends AppCompatActivity {


    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private EditText mEmailaddress, mPassword, mRepeatPassword;
    private FirebaseAuth firebaseAuth;
    Button mSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmailaddress = findViewById(R.id.email_address);
        mPassword = findViewById(R.id.password);
        mRepeatPassword = findViewById(R.id.repeat_password);
        mSignup = findViewById(R.id.sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();

            }
        });


    }

    private void registerUser() {
        String email = mEmailaddress.getText().toString().trim();
        String password = mPassword.getText().toString();
        String rep_password = mRepeatPassword.getText().toString();


        if (TextUtils.isEmpty(email)){
            mEmailaddress.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)){
            mEmailaddress.setError("Password is required");
            return;
        }
        if (password.length()<6){
            mPassword.setError("Password must be more than 6 characters");
            return;
        }
        if(!TextUtils.equals(password,rep_password)){
            mRepeatPassword.setError("Password does not match");
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating User");
        progressDialog.create();
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(Sign_up.this, "User Created!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else  {
                    progressDialog.dismiss();
                    Toast.makeText(Sign_up.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}