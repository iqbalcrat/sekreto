package app.com.sekreto;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import app.com.sekreto.Models.Item;
import app.com.sekreto.Adapters.ItemAdapter;

public class ListView extends AppCompatActivity {

    private static final String TAG = "ListView";
    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Item> mItemList = new ArrayList<>();
    Toolbar my_toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SwipeRefreshLayout swipeRefreshLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        my_toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Users");
        getUpdatedList();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
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

    public void getUpdatedList(){

        CollectionReference listenerRegistration = db.collection("Users");
        listenerRegistration.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                List<DocumentChange> docChanges = queryDocumentSnapshots.getDocumentChanges();
                Log.d(TAG, "No of changes: " + docChanges.size());
                for (DocumentChange doc : docChanges) {
                    mItemList.add(new Item(R.drawable.ic_sun, doc.getDocument().get("fullName").toString(), "Line " + mItemList.size()));
                    Log.d(TAG, doc.getDocument().get("fullName").toString());
                }
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
        });

    }
}
