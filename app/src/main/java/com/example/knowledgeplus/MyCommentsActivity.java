package com.example.knowledgeplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyCommentsActivity extends AppCompatActivity {
    ListView listView;
    DatabaseReference commentReference = FirebaseDatabase.getInstance().getReference("comment");
    String uid;
    ArrayList<CommentCard> commentCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listView);
        commentCards = new ArrayList<CommentCard>();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        commentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentCards.clear();
                for (DataSnapshot allCommentsInArticle : snapshot.getChildren()) {
                    for (DataSnapshot comment : allCommentsInArticle.getChildren()) {
                        CommentCard commentCard = comment.getValue(CommentCard.class);
                        if (commentCard.getUid().compareTo(uid) == 0) {
                            commentCards.add(0, commentCard);
                        }
                    }
                }

                CommentCardAdapter commentCardAdapter = new CommentCardAdapter(MyCommentsActivity.this, commentCards);
                listView.setAdapter(commentCardAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Nothing
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}