package app.com.sekreto;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import app.com.sekreto.Items.Item;
import app.com.sekreto.Items.ItemAdapter;

public class ListView extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Item> mItemList = new ArrayList<>();
    Toolbar my_toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        my_toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Chat");



        mItemList.add(new Item(R.drawable.ic_android, "Line 1", "Line 2"));
        mItemList.add(new Item(R.drawable.ic_audio, "Line 2", "Line 2"));
        mItemList.add(new Item(R.drawable.ic_sun, "Line 3", "Line 2"));
        mItemList.add(new Item(R.drawable.ic_sun, "Line 4", "Line 2"));
        mItemList.add(new Item(R.drawable.ic_android, "Line 1", "Line 2"));
        mItemList.add(new Item(R.drawable.ic_audio, "Line 2", "Line 2"));
        mItemList.add(new Item(R.drawable.ic_sun, "Line 3", "Line 2"));
        mItemList.add(new Item(R.drawable.ic_sun, "Line 4", "Line 2"));
        mItemList.add(new Item(R.drawable.ic_android, "Line 1", "Line 2"));
        mItemList.add(new Item(R.drawable.ic_audio, "Line 2", "Line 2"));
        mItemList.add(new Item(R.drawable.ic_sun, "Line 3", "Line 2"));
        mItemList.add(new Item(R.drawable.ic_sun, "Line 4", "Line 2"));
        mItemList.add(new Item(R.drawable.ic_android, "Line 1", "Line 2"));
        mItemList.add(new Item(R.drawable.ic_audio, "Line 2", "Line 2"));
        mItemList.add(new Item(R.drawable.ic_sun, "Line 3", "Line 2"));
        mItemList.add(new Item(R.drawable.ic_sun, "Line 4", "Line 2"));

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new ItemAdapter(mItemList);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListner(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                mItemList.get(position).changeText1("clicked");
                mAdapter.notifyItemChanged(position);

            }
        });


    }


}
