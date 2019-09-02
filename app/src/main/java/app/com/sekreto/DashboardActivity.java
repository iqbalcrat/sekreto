package app.com.sekreto;

import android.animation.ArgbEvaluator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import app.com.sekreto.Adapters.QuestionAdapter;
import app.com.sekreto.Models.Chat;
import app.com.sekreto.Models.Members;
import app.com.sekreto.Models.Question;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashBoardActivity";
    FloatingActionButton askQ;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ViewPager viewPager;
    List<Question> models = new ArrayList<>();
    List<Chat> chatModels = new ArrayList<>();
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    ProgressBar progressBar;
    Dialog myDialog;
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
        //progressBar = findViewById(R.id.progressbar);
        askQ = findViewById(R.id.askQ);
        askQ =  findViewById(R.id.askQ);
        askQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AskQuestion.class));
            }
        });


        firebaseUser = mAuth.getCurrentUser();
        myDialog = new Dialog(this);
        //textView.append(firebaseUser.getEmail());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView =(NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);


        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //models.add(new Question("I contested for an MLA position from mangalagiri in this elections but I ...", "Nara Lokesh", R.drawable.profilepic));
        getUpdatedList();
        Log.d(TAG, "chat Models" + chatModels.toString() );
        final QuestionAdapter adapter = new QuestionAdapter(models, chatModels,this);
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager = findViewById(R.id.viewPager);
                viewPager.setAdapter(adapter);
                viewPager.setPadding(100, 0, 100, 0);
                //progressBar.setVisibility(View.GONE);
            }
        }, 3000);
    }

    public void getUpdatedList() {

        DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference().child("Chats");
        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    String id = ((DataSnapshot)iterator.next()).getKey();
                    Log.d(TAG, "Chat id in iterator:" + id);
                    chatModels.add(new Chat(id, "",""));


                }

                Iterator iterator1 = dataSnapshot.getChildren().iterator();

                while(iterator1.hasNext()){
                    Chat chat = ((DataSnapshot)iterator1.next()).getValue(Chat.class);
                    models.add(new Question(chat.getTitle(), firebaseUser.getEmail().split("@")[0],  R.drawable.profilepic));
                    Log.d("Get question Names:" ,chat.toString() );
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getMyQuestions(View view) {

        Intent intent = new Intent(this, ListView.class);
        startActivity(intent);
    }

    private void addMemberToChatModel(DatabaseReference reference, Members members){



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
