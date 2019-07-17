package app.com.sekreto;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    private DrawerLayout drawer;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        my_toolbar = findViewById(R.id.toolbar);
        getUpdatedList();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ItemAdapter(mItemList);
        mRecyclerView.setAdapter(mAdapter);


        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
       /* mAdapter.setOnItemClickListner(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                mItemList.get(position).changeText1("clicked");
                mAdapter.notifyItemChanged(position);

            }
        });*/


    }

    public void getUpdatedList(){

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        CollectionReference listenerRegistration = db.collection("Questions");
        listenerRegistration.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                List<DocumentChange> docChanges = queryDocumentSnapshots.getDocumentChanges();
                Log.d(TAG, "No of changes: " + docChanges.size());
                for (DocumentChange doc : docChanges) {

                    if(doc.getDocument().getData().containsKey("question")){
                        Log.d(TAG, doc.getDocument().getData().toString());
                        if(doc.getDocument().getData().containsKey("User")){

                            Map<String, Object> user =(HashMap<String,Object>) doc.getDocument().get("User");
                            Log.d(TAG, user.toString());
                            if(firebaseUser.getEmail().contentEquals(user.get("email").toString())){
                                mItemList.add(new Item(R.drawable.profilepic, doc.getDocument().get("question").toString(), "Answers : 0" ));
                                Log.d(TAG, doc.getDocument().get("question").toString());

                            }
                        }
                    }

                }
                mAdapter = new ItemAdapter(mItemList);
                mRecyclerView.setAdapter(mAdapter);
               /* mAdapter.setOnItemClickListner(new ItemAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        mItemList.get(position).changeText1("clicked");
                        mAdapter.notifyItemChanged(position);

                    }
                });
*/

            }
        });

    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
