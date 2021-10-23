package com.example.knowledgeplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyArticlesActivity extends AppCompatActivity {

    ListView listView;
    FirebaseDatabase db;
    DatabaseReference article_table;
    String myUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_articles);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listView);
        ArrayList<ArticleCard> myArticleCards = new ArrayList<ArticleCard>();
        db = FirebaseDatabase.getInstance();
        article_table = db.getReference("article");
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        article_table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myArticleCards.clear();
                for (DataSnapshot articleSnapshot : snapshot.getChildren()) {
                    ArticleCard articleCard = articleSnapshot.getValue(ArticleCard.class);

                    if (articleCard.getUid().compareTo(myUid) == 0) {
                        myArticleCards.add(0, ArticleCard.newInstance(articleCard.getId(),
                                articleCard.getTitle(),
                                articleCard.getnViews(),
                                articleCard.getnComments(),
                                articleCard.getAuthor(),
                                articleCard.getUid(),
                                articleCard.getLocation(),
                                articleCard.getPublishDate(),
                                articleCard.getBody(),
                                articleCard.getnImages()));
                        Log.i("MyArticleActivity", "Article " + articleCard.getTitle() +" is added");
                    }
                }

                MyArticleCardAdapter myArticleCardAdapter = new MyArticleCardAdapter(MyArticlesActivity.this, myArticleCards);

                listView.setAdapter(myArticleCardAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Nothing
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