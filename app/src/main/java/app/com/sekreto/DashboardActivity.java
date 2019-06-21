package app.com.sekreto;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import app.com.sekreto.Adapters.Adapter;
import app.com.sekreto.Models.Question;
import app.com.sekreto.User.UserLogin;

public class DashboardActivity extends AppCompatActivity {
    Button signOut;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ViewPager viewPager;
    List<Question> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        signOut = findViewById(R.id.signout_btn);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        //textView.append(firebaseUser.getEmail());
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getInstance().signOut();
                Toast.makeText(DashboardActivity.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DashboardActivity.this, UserLogin.class));
                finish();
            }
        });


        models = new ArrayList<>();
        models.add(new Question("I contested for an MLA position from mangalagiri in this elections but I ...", "Brochure", R.drawable.anonymous));
        models.add(new Question("Mangalagiri", "Sticker", R.drawable.anonymous));
        models.add(new Question("Mangalagiri", "Poster", R.drawable.anonymous));
        models.add(new Question("Mangalagiri", "Namecard", R.drawable.anonymous));

        final Adapter adapter = new Adapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter((PagerAdapter) adapter);
        viewPager.setPadding(130, 0, 130, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                }

                else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void goToListView(View view) {

        Intent intent = new Intent(this, ListView.class);
        startActivity(intent);

    }
}
