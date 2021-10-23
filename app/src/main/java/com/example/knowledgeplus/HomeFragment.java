package com.example.knowledgeplus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    final String TAG = "HomeFragment";
    ListView listView;
    ArrayList<ArticleCard> articleCards;
    FirebaseDatabase db;
    DatabaseReference article_table;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articleCards = new ArrayList<ArticleCard>();
        db = FirebaseDatabase.getInstance();
        article_table = db.getReference("article");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = (ListView) view.findViewById(R.id.listView);

        article_table.addValueEventListener(listAllArticlesListener);

        return view;
    }

    ValueEventListener listAllArticlesListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            articleCards.clear();
            for (DataSnapshot articleSnapshot : snapshot.getChildren()) {
                ArticleCard articleCard = articleSnapshot.getValue(ArticleCard.class);

                articleCards.add(0, articleCard);
            }

            ArticleCardAdapter articleCardAdapter = new ArticleCardAdapter(getContext(), articleCards);
            listView.setAdapter(articleCardAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
