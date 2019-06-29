package app.com.sekreto;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuestionScreen extends AppCompatActivity {
    Dialog myDialog;
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

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}