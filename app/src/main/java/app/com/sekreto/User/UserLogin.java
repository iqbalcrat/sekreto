package app.com.sekreto.User;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.com.sekreto.DashboardActivity;
import app.com.sekreto.R;

public class UserLogin extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    EditText username, password;
    ProgressBar progressbar;
    TextView signup, forgetPassword;
    AnimationDrawable animationDrawable;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlogin_activity);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        Button login_button = (Button) findViewById(R.id.login_btn);
        username = findViewById(R.id.uname);
        forgetPassword = findViewById(R.id.forget_password);
        password = findViewById(R.id.password);
        progressbar = findViewById(R.id.progressbar);
        signup = findViewById(R.id.sign_up);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativelayout);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);
        login_button.setOnClickListener(this);
        signup.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);

        // Check if user is signed in (non-null) and update UI accordingly.
        mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!= null)
                {
                    startActivity(new Intent(UserLogin.this, DashboardActivity.class));

                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning())
            animationDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning())
            animationDrawable.stop();
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListner);
    }

    @Override
    public void onClick(View v) {



        switch (v.getId()){

            case R.id.login_btn:
                loginUser();
                break;
            case R.id.sign_up:
                startActivity(new Intent(UserLogin.this, UserRegistration.class));
                break;
            case R.id.forget_password:
                startActivity(new Intent(UserLogin.this, ForgetPassword.class));
                break;



        }
    }

    private void loginUser() {
        //User fields validations

        String email = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(UserLogin.this, "Please enter email id", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(UserLogin.this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }


        progressbar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressbar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            finish();

                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));

                        } else {
                            Toast.makeText(UserLogin.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }

                   }
                });

    }
}