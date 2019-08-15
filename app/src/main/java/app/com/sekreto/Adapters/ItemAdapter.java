package app.com.sekreto.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.com.sekreto.Models.Item;
import app.com.sekreto.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private ArrayList<Item> itemArrayList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public void setOnItemClickListner(OnItemClickListener listener){
        mListener = listener;
    }

    public static class  ItemViewHolder extends RecyclerView.ViewHolder{


        public TextView mTextView1;
        public TextView mTextView2;
        public CircleImageView mProfilePic;


        public ItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mProfilePic = itemView.findViewById(R.id.profile_image);
            mTextView1 = itemView.findViewById(R.id.question);
            mTextView2 = itemView.findViewById(R.id.name);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }

                }
            });
        }
    }

    public ItemAdapter(ArrayList<Item> itemArrayList){

        this.itemArrayList = itemArrayList;

    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ItemViewHolder evh = new ItemViewHolder(v, mListener);
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
