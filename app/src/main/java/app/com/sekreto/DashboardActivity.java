package app.com.sekreto;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Delayed;

import javax.annotation.Nullable;

import app.com.sekreto.Adapters.ItemAdapter;
import app.com.sekreto.Adapters.QuestionAdapter;
import app.com.sekreto.Models.Item;
import app.com.sekreto.Models.Question;
import app.com.sekreto.User.UserLogin;
import app.com.sekreto.User.UserRegistration;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashBoardActivity";
    Button signOut;
    Button askQuestion;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ViewPager viewPager;
    List<Question> models = new ArrayList<>();
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        askQuestion = findViewById(R.id.signout_btn);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        //textView.append(firebaseUser.getEmail());

        askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mAuth.getInstance().signOut();
                //Toast.makeText(DashboardActivity.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DashboardActivity.this, QuestionScreen.class));
                finish();
            }
        });

        models.add(new Question("I contested for an MLA position from mangalagiri in this elections but I ...", "Join Chat", R.drawable.anonymous));
        getUpdatedList();
        final QuestionAdapter adapter = new QuestionAdapter(models, this);
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager = findViewById(R.id.viewPager);
                viewPager.setAdapter(adapter);
                viewPager.setPadding(130, 0, 130, 0);
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
                        models.add(new Question(doc.getDocument().get("question").toString(), "Join Chat", R.drawable.anonymous));
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
}
