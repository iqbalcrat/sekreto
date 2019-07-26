package app.com.sekreto;

import android.animation.ArgbEvaluator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import app.com.sekreto.Adapters.QuestionAdapter;
import app.com.sekreto.Models.Question;
import app.com.sekreto.friendlychat.MainActivity;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashBoardActivity";
    FloatingActionButton askQ;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ViewPager viewPager;
    List<Question> models = new ArrayList<>();
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    ProgressBar progressBar;

    private EditText questionText;
    private FloatingActionButton button;
    private DrawerLayout drawer;
    Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
   NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        mAuth = FirebaseAuth.getInstance();

        askQ =  findViewById(R.id.askQ);
        askQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AskQuestion.class));
            }
        });

        //progressBar = findViewById(R.id.progressbar);
        //askQ = findViewById(R.id.askQ);
        firebaseUser = mAuth.getCurrentUser();

        //textView.append(firebaseUser.getEmail());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView =(NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);


        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        models.add(new Question("I contested for an MLA position from mangalagiri in this elections but I ...", "Nara Lokesh", R.drawable.profilepic));
        getUpdatedList();
        final QuestionAdapter adapter = new QuestionAdapter(models, this);
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager = findViewById(R.id.viewPager);
                viewPager.setAdapter(adapter);
                viewPager.setPadding(100, 0, 100, 0);
                //progressBar.setVisibility(View.GONE);
            }
        }, 7500);


    }

    public void getUpdatedList() {

        CollectionReference listenerRegistration = db.collection("Questions");
        listenerRegistration.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                List<DocumentChange> docChanges = queryDocumentSnapshots.getDocumentChanges();
                Log.d(TAG, "No of changes: " + docChanges.size());
                for (DocumentChange doc : docChanges) {

                    if (doc.getDocument().getData().containsKey("question")) {
                        String person = "Anonymous";
                        if (doc.getDocument().getData().containsKey("User")){
                            Map<String, Object> user = (HashMap)doc.getDocument().get("User");
                            if(user.containsKey("email")){
                                person = user.get("email").toString().split("@")[0];
                                Log.d(TAG, person);
                            }
                        }
                        models.add(new Question(doc.getDocument().get("question").toString(), " " + person,  R.drawable.profilepic));
                        Log.d(TAG, doc.getDocument().get("question").toString());

                    }

                }
            }
        });

    }

    public void getMyQuestions(View view) {

        Intent intent = new Intent(this, ListView.class);
        startActivity(intent);
    }

    public void goToChat(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }






    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
