package app.com.sekreto;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.allyants.chipview.ChipView;
import com.allyants.chipview.SimpleChipAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.com.sekreto.Models.Chat;

public class AskQuestion extends AppCompatActivity {
    private static final String TAG = "AskQuestion";
    Dialog myDialog;
    private EditText questionText;
    private FloatingActionButton button;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    ChipView mChipView;

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_screen);
        myDialog = new Dialog(this);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mChipView = findViewById(R.id.cvTag);
        this.populateTags();
        questionText = findViewById(R.id.questionText);
        button = findViewById(R.id.storeInDBButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AskQuestion", questionText.getText().toString());
                if (TextUtils.isEmpty( questionText.getText().toString())){

                    Toast.makeText(AskQuestion.this, "Please enter the question", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AskQuestion.this, "Member added", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(AskQuestion.this, "Question Added ; " + question.get("question"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AskQuestion.this, DashboardActivity.class);
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

    }



    private void populateTags(){
        ArrayList tags = new ArrayList();
        tags.add("CSE");
        tags.add("ECE");
        tags.add("EEE");
        tags.add("MECH");
        tags.add("CIVIL");
        tags.add("CHEM");
        tags.add("MME");
        SimpleChipAdapter adapter =  new SimpleChipAdapter(tags);
        mChipView.setAdapter(adapter);


    }

    private void addQuestionToChatModel(DatabaseReference reference, Chat chat){

        reference.child("Chats").child(chat.getId()).setValue(chat);

    }

}