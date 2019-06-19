package app.com.sekreto.Items;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.sekreto.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private ArrayList<Item> itemArrayList;

    public static class  ItemViewHolder extends RecyclerView.ViewHolder{


        public TextView mTextView1;
        public TextView mTextView2;
        public CircleImageView mProfilePic;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfilePic = itemView.findViewById(R.id.profile_image);
            mTextView1 = itemView.findViewById(R.id.textView1);
            mTextView2 = itemView.findViewById(R.id.textView2);
        }
    }

    public ItemAdapter(ArrayList<Item> itemArrayList){

        this.itemArrayList = itemArrayList;

    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ItemViewHolder evh = new ItemViewHolder(v);
        return evh;


    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {

        Item currentItem = itemArrayList.get(i);

        itemViewHolder.mProfilePic.setImageResource(currentItem.getmImageResource());
        itemViewHolder.mTextView1.setText(currentItem.getLine1());
        itemViewHolder.mTextView2.setText(currentItem.getLine2());

    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }




}
