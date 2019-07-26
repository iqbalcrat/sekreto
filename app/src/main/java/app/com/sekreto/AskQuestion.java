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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_screen);
        //myDialog = new Dialog(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mChipView = findViewById(R.id.cvTag);
        this.populateTags();
        questionText = findViewById(R.id.questionText);
        button = findViewById(R.id.storeInDBButton);

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                firebaseUser = mAuth.getCurrentUser();

                Log.d(TAG , questionText.getText().toString());

                Date d1 = new Date();
                final Map<String, Object> question = new HashMap<>();
                question.put("question", questionText.getText().toString());
                question.put("User", firebaseUser);
                question.put("Time",d1 );



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
}