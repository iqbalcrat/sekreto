package app.com.sekreto.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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
import android.widget.RelativeLayout;
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
    AnimationDrawable animationDrawable;
    RelativeLayout relativeLayout;
    private  String TAG = "UserRegistrationActivity";


    @SuppressLint("LongLogTag")
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
        relativeLayout = findViewById(R.id.relativelayout);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);
        register.setOnClickListener(this);
        direct_login.setOnClickListener(this);
        myRef = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();

        Log.d(TAG, "Entered into OnCreate");

        // Check if user is signed in (non-null) and update UI accordingly.
        mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(TAG, "Checking whether already user is logged in or not");
                if(firebaseAuth.getCurrentUser()!= null)
                {
                    Log.d(TAG, "User is already logged in");
                    startActivity(new Intent(UserRegistration.this, DashboardActivity.class));
                    finish();
                }
            }
        };
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Activity on Resume");
        if (animationDrawable != null && !animationDrawable.isRunning())
            animationDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Activity on Pause");
        if (animationDrawable != null && animationDrawable.isRunning())
            animationDrawable.stop();
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

        Log.d(TAG, "Activity in Register User method");

        final String email = email_reg.getText().toString().trim();
        String pass = password_reg.getText().toString().trim();
        final String uname = uname_reg.getText().toString().trim();
        //Input fields validations
        if(TextUtils.isEmpty(uname)){
            Toast.makeText(UserRegistration.this, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(UserRegistration.this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
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

        if(!(checkbox.isChecked())){

            Toast.makeText(UserRegistration.this, "Please select the checkbox if you are above 16", Toast.LENGTH_SHORT).show();
            return;

        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "Activity inmAuth.createUserWithEmailAndPassword");
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Creation of user is successfull");
                            //saving user data to the database
                            UsersInfo usersInfo = new UsersInfo(uname, email);
                            String id = myRef.push().getKey();
                            myRef.child(id).setValue(usersInfo);
                            Toast.makeText(UserRegistration.this, "Registration is successful.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UserRegistration.this, DashboardActivity.class));


                        } else {
                         // If sign in fails, display a message to the user.
                            Log.d(TAG, "Creation of user is failed");
                            Toast.makeText(UserRegistration.this, "Registration failed.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }
}
