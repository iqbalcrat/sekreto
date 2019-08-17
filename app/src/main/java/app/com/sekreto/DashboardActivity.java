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
        //askQ = findViewById(R.id.askQ);
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


    public void ShowPopup(View v) {
        TextView txtclose;
        myDialog.setContentView(R.layout.questionpopup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");

        questionText = myDialog.findViewById(R.id.questionText);
        button = myDialog.findViewById(R.id.storeInDBButton);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(myDialog.toString())){

                    Toast.makeText(DashboardActivity.this, "Please enter the question", Toast.LENGTH_SHORT).show();
                    return;

                }

                mAuth = FirebaseAuth.getInstance();
                firebaseUser = mAuth.getCurrentUser();

                Log.d(TAG , questionText.getText().toString());

                Date d1 = new Date();
                final Map<String, Object> question = new HashMap<>();
                question.put("question", questionText.getText().toString());
                question.put("User", firebaseUser);
                question.put("Time",d1 );

                String userId = firebaseUser.getUid();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                String pushId = reference.child("Chats").push().getKey();
                /*Insert data into Chat Node*/
                addQuestionToChatModel(reference, new Chat(pushId, questionText.getText().toString(), userId));
                /*************Insert data into Member node***********/
                DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference("Members").child(pushId);
                HashMap<String,String> memberMap = new HashMap<>();
                memberMap.put("id", pushId);
                memberMap.put("userId", userId);
                memberRef.child(userId).setValue(memberMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DashboardActivity.this, "Member added", Toast.LENGTH_SHORT).show();
                    }
                });
                /***********Insert data into Member node***********/
                db.collection("Questions")
                        .add(question)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                myDialog.dismiss();
                                Toast.makeText(DashboardActivity.this, "Question Added ; " + question.get("question"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DashboardActivity.this, DashboardActivity.class);
                                startActivity(intent);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });




            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void addQuestionToChatModel(DatabaseReference reference, Chat chat){

        reference.child("Chats").child(chat.getId()).setValue(chat);

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
