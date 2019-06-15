package app.com.sekreto.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.com.sekreto.DashboardActivity;
import app.com.sekreto.R;

public class UserRegistration extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    Button register;
    EditText uname_reg, email_reg, password_reg;
    TextView direct_login;
    ProgressBar progressBar;
    CheckBox checkbox;
    DatabaseReference myRef;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        mAuth = FirebaseAuth.getInstance();
        uname_reg = findViewById(R.id.username_reg);
        password_reg = findViewById(R.id.password_reg);
        email_reg = findViewById(R.id.email_reg);
        checkbox = findViewById(R.id.checkbox_reg);
        progressBar = findViewById(R.id.progress_reg);
        direct_login = findViewById(R.id.direct_login);
        register = findViewById(R.id.button_reg);
        register.setOnClickListener(this);
        direct_login.setOnClickListener(this);
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();


        // Check if user is signed in (non-null) and update UI accordingly.
        mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!= null)
                {
                    startActivity(new Intent(UserRegistration.this, DashboardActivity.class));

                }
            }
        };



    }




    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthStateListner);
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.button_reg){

            RegisterUser();

        }
        if(v.getId() == R.id.direct_login){

            startActivity(new Intent(UserRegistration.this, UserLogin.class));

        }

    }

    private void RegisterUser() {

        final String email = email_reg.getText().toString().trim();
        String pass = password_reg.getText().toString().trim();
        final String uname = uname_reg.getText().toString().trim();

        if(!(checkbox.isChecked())){

            Toast.makeText(UserRegistration.this, "Please select the checkbox if you are above 16", Toast.LENGTH_SHORT).show();
            return;

        }


        if(TextUtils.isEmpty(uname)){
            Toast.makeText(UserRegistration.this, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(UserRegistration.this, "Please enter email id", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(UserRegistration.this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(pass.length() < 6){
            Toast.makeText(UserRegistration.this, "Please enter password of more than 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            //saving user data to the database
                            UsersInfo usersInfo = new UsersInfo(uname, email);
                            myRef = database.getReference("Users");
                            myRef.child(uname).setValue(usersInfo);

                            Toast.makeText(UserRegistration.this, "Registration is successful.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UserRegistration.this, DashboardActivity.class));



                        } else {
                            progressBar.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            Toast.makeText(UserRegistration.this, "Registration failed.", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });


    }
}
