package app.com.sekreto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import app.com.sekreto.Items.Item;
import app.com.sekreto.Items.ItemAdapter;

public class ListView extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        ArrayList<Item> ItemList = new ArrayList<>();
        ItemList.add(new Item(R.drawable.ic_android, "Line 1", "Line 2"));
        ItemList.add(new Item(R.drawable.ic_audio, "Line 2", "Line 2"));
        ItemList.add(new Item(R.drawable.ic_sun, "Line 3", "Line 2"));
        ItemList.add(new Item(R.drawable.ic_sun, "Line 4", "Line 2"));
        ItemList.add(new Item(R.drawable.ic_android, "Line 1", "Line 2"));
        ItemList.add(new Item(R.drawable.ic_audio, "Line 2", "Line 2"));
        ItemList.add(new Item(R.drawable.ic_sun, "Line 3", "Line 2"));
        ItemList.add(new Item(R.drawable.ic_sun, "Line 4", "Line 2"));
        ItemList.add(new Item(R.drawable.ic_android, "Line 1", "Line 2"));
        ItemList.add(new Item(R.drawable.ic_audio, "Line 2", "Line 2"));
        ItemList.add(new Item(R.drawable.ic_sun, "Line 3", "Line 2"));
        ItemList.add(new Item(R.drawable.ic_sun, "Line 4", "Line 2"));
        ItemList.add(new Item(R.drawable.ic_android, "Line 1", "Line 2"));
        ItemList.add(new Item(R.drawable.ic_audio, "Line 2", "Line 2"));
        ItemList.add(new Item(R.drawable.ic_sun, "Line 3", "Line 2"));
        ItemList.add(new Item(R.drawable.ic_sun, "Line 4", "Line 2"));

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new ItemAdapter(ItemList);

        mRecyclerView.setAdapter(mAdapter);


    }
}
