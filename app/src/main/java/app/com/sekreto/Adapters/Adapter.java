package app.com.sekreto.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import app.com.sekreto.Models.Question;
import app.com.sekreto.QuestionDetails;
import app.com.sekreto.R;

public class Adapter extends PagerAdapter {

    private List<Question> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public Adapter(List<Question> models, Context context) {
        this.models = models;
        this.context = context;
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
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_question_item, container, false);


        TextView question, name;
        ImageView profilePic;

        profilePic = view.findViewById(R.id.profile_image);
        question = view.findViewById(R.id.question);
        name = view.findViewById(R.id.name);

        profilePic.setImageResource(models.get(position).getProfilePic());
        question.setText(models.get(position).getQuestion());
        name.setText(models.get(position).getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuestionDetails.class);
                intent.putExtra("param", models.get(position).getQuestion());
                context.startActivity(intent);
                // finish();
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
