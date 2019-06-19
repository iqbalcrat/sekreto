package app.com.sekreto;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.com.sekreto.User.UserLogin;

public class DashboardActivity extends AppCompatActivity {
    TextView textView;
    Button signOut;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        textView = findViewById(R.id.textView);
        signOut = findViewById(R.id.signout_btn);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        textView.append(firebaseUser.getEmail());
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getInstance().signOut();
                Toast.makeText(DashboardActivity.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DashboardActivity.this, UserLogin.class));
                finish();
            }
        });

    }

    public void goToListView(View view) {

        Intent intent = new Intent(this, ListView.class);
        startActivity(intent);

    }
}
