package app.com.sekreto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        String username = getIntent().getStringExtra("username");
        textView = findViewById(R.id.textView);
        textView.setText(username);

    }
}
