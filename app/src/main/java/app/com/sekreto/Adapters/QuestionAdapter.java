package app.com.sekreto.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

import app.com.sekreto.DashboardActivity;
import app.com.sekreto.ListView;
import app.com.sekreto.MessageActivity;
import app.com.sekreto.Models.Chat;
import app.com.sekreto.Models.Question;
import app.com.sekreto.R;
import app.com.sekreto.friendlychat.MainActivity;

public class QuestionAdapter extends PagerAdapter {

    private List<Question> models;
    private List<Chat> chatModels;
    private LayoutInflater layoutInflater;
    private Context context;
    private Button button;
    FirebaseUser firebaseUser;
    private static final String TAG = "QuestionAdapter";
    private DatabaseReference reference;

    public QuestionAdapter(List<Question> models, List<Chat> chatModels,Context context) {
        this.models = models;
        this.context = context;
        this.chatModels = chatModels;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        Log.d(TAG, "QuestionAdapater-> InstantiateItem");
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_question_card, container, false);


        TextView question, name;
        ImageView profilePic;

        profilePic = view.findViewById(R.id.profile_image);
        question = view.findViewById(R.id.question);
        name = view.findViewById(R.id.name);
        button = view.findViewById(R.id.joinChat);

        profilePic.setImageResource(models.get(position).getProfilePic());
        question.setText(models.get(position).getQuestion());
        name.setText(models.get(position).getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = firebaseUser.getUid();
                DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference("Members").child(chatModels.get(position).getId());
                HashMap<String,String> memberMap = new HashMap<>();
                memberMap.put("id", chatModels.get(position).getId());
                memberMap.put("userId", userId);
                memberRef.child(userId).setValue(memberMap);

                Intent intent = new Intent(context, MessageActivity.class);
                Log.d(TAG, "Chat ID: " +  chatModels.get(position).getId());
                intent.putExtra("chatId", chatModels.get(position).getId());
                context.startActivity(intent);
                Log.d(TAG, "QuestionAdapater-> view.setOnClickListener");

            }
        });

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "QuestionAdapater->button clicked");
//                DashboardActivity a = new DashboardActivity();
//                a.goToChat();
//            }
//        });


        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


}
