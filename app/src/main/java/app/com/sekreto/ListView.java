package app.com.sekreto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.DocumentCollections;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import app.com.sekreto.Items.Item;
import app.com.sekreto.Items.ItemAdapter;
import app.com.sekreto.User.UserLogin;

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
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mItemList.clear();
                getUpdatedList();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
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


    public void getExistingData(){

        db.collection("Users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                mItemList.add(new Item(R.drawable.ic_sun, document.get("fullName").toString(), "Line " + mItemList.size()));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



}
