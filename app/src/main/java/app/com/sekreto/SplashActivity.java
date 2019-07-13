package app.com.sekreto;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.WanderingCubes;

import app.com.sekreto.User.UserRegistration;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        CubeGrid doubleBounce = new CubeGrid();
        progressBar.setIndeterminateDrawable(doubleBounce);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, UserRegistration.class);
                startActivity(intent);
                finish();

            }
        },2500);
    }
}
