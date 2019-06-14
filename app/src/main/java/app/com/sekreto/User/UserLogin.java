package app.com.sekreto.User;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    EditText username, password;
    ProgressBar progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlogin_activity);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        Button login_button = (Button) findViewById(R.id.login_btn);
        username = findViewById(R.id.uname);
        password = findViewById(R.id.password);
        progressbar = findViewById(R.id.progressbar);
        login_button.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent I = new Intent(UserLogin.this, DashboardActivity.class);
        I.putExtra("username", currentUser);


    }





    @Override
    public void onClick(View v) {
    if(v.getId() == R.id.login_btn){

        loginUser();

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

        if(pass.length() < 6){
            Toast.makeText(UserLogin.this, "Please enter password of more than 6 characters", Toast.LENGTH_SHORT).show();
        return;
        }
        progressbar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String TAG;
                        if (task.isSuccessful()) {
                            progressbar.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));

                        } else {
                            Toast.makeText(UserLogin.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }
}