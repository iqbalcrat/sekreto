package app.com.sekreto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.com.sekreto.Adapters.MessageAdapter;
import app.com.sekreto.Models.Messages;
import app.com.sekreto.Models.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {


    CircleImageView profile_image;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference reference;

    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Messages> mChat;

    RecyclerView recyclerView;


    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();

        final String chatId = intent.getStringExtra("chatId");
        Log.d("MessageActivity", "Chat Id:" + chatId);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        username.setText(fuser.getEmail().split("@")[0]);
        profile_image.setImageResource(R.drawable.profilepic);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(), chatId, msg);
                }else{
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        readMessages(chatId);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MessageActivity.this, "These are the list of Members", Toast.LENGTH_SHORT).show();
            }
        });

//        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                username.setText(user.getUsername());
//                if(user.getImageURL().equals("defaults")){
//                    profile_image.setImageResource(R.drawable.profilepic);
//                }else{
//                    profile_image.setImageResource(R.drawable.profilepic);
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//
//        });



    }

    private void sendMessage(String sender, String chatId, String message){


        DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference("messages").child(chatId);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", chatId);
        hashMap.put("message", message);
        memberRef.push().setValue(hashMap);
    }


    private void readMessages(final String chatId){
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("messages");
        reference.child(chatId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.d("User Id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Log.d("PChatKey:" , snapshot.getKey());
                   // Log.d("PChat:" , snapshot.getValue().toString());
                    Messages messages = snapshot.getValue(Messages.class);
                    Log.d("messageModel", messages.toString());
                    mChat.add(messages);
                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat, "default");
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                mChat.clear();
//                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
//
//                    Log.d("User Id", FirebaseAuth.getInstance().getCurrentUser().getUid());
//                    Log.d("Chat Key:" , snapshot.getKey());
//                    Log.d("Chats:" , snapshot.getValue().toString());
////                    Messages chat = snapshot.getValue(Messages.class);
////                    Log.d("MessageActivity:" , chat.toString());
////                    if(chat.getAuthor().equals(myid) && chat.getSender().equals(userid)||
////                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
////                        mChat.add(chat);
////                    }
////
////                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat, imageurl);
////                    recyclerView.setAdapter(messageAdapter);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }


}
