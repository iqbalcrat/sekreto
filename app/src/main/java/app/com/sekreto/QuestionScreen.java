package app.com.sekreto;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.com.sekreto.User.ForgetPassword;

public class QuestionScreen extends AppCompatActivity {
    private static final String TAG = "QuestionScreen";
    Dialog myDialog;
    private EditText questionText;
    private Button button;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_screen);
        myDialog = new Dialog(this);

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
                                Toast.makeText(QuestionScreen.this, "Question Added ; " + question.get("question"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(QuestionScreen.this, DashboardActivity.class);
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


}