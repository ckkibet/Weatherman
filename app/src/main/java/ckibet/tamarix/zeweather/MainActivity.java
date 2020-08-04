package ckibet.tamarix.zeweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    EditText mEmailAddress;
    EditText mPassword;
    Button mSignUp;
    Button mSignin;
    TextView mForgotPassword;

    private GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 10;

    Button verify;
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

        mEmailAddress = findViewById(R.id.email_address);
        mPassword = findViewById(R.id.password);
        mSignin = findViewById(R.id.sign_in);
        mSignUp = findViewById(R.id.sign_up);
        mForgotPassword = findViewById(R.id.forgot_password);
        mAuth = FirebaseAuth.getInstance();

        signin();
        signup();
        forgotPassword();

    }

    private void signin() {



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

    private void forgotPassword() {

    }


}