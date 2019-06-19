package app.com.sekreto.User;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import app.com.sekreto.R;

public class ForgetPassword extends AppCompatActivity {
    EditText emailRes;
    Button resetRes;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        emailRes =  findViewById(R.id.email_res);
        progressBar = findViewById(R.id.progressbar);
        resetRes = findViewById(R.id.btn_res);
        mAuth = FirebaseAuth.getInstance();
        resetRes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               final String email =  emailRes.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
              mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      progressBar.setVisibility(View.GONE);
                      if(task.isSuccessful()){
                          Toast.makeText(ForgetPassword.this, "Email sent", Toast.LENGTH_SHORT).show();
                          startActivity(new Intent(ForgetPassword.this, UserLogin.class));
                          finish();
                      }else
                          Toast.makeText(ForgetPassword.this, "unable to rest, please try again", Toast.LENGTH_SHORT).show();
                  }
              });
        }

    });
    }
}