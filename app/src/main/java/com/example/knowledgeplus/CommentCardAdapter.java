package com.example.knowledgeplus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentCardAdapter extends ArrayAdapter<CommentCard> {
    private final String TAG = "CommentCardAdapter";
    private final int MAX_COMMENT_TEXT = 45;
    ArticleCard[] articleCards;

    Context context;

    TextView comment;
    TextView articleTitle;
    TextView publishDate;
    ImageButton delete;

    public CommentCardAdapter(Context context, ArrayList<CommentCard> commentCards) {
        super(context, 0, commentCards);
        this.context = context;
        articleCards = new ArticleCard[commentCards.size()];
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CommentCard commentCard = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_comment_card, parent, false);
        }

        comment = (TextView) convertView.findViewById(R.id.comment);
        articleTitle = (TextView) convertView.findViewById(R.id.articleTitle);
        publishDate = (TextView) convertView.findViewById(R.id.publishDate);
        delete = (ImageButton) convertView.findViewById(R.id.deleteIcon);

        setText(commentCard);
        setArticleTitle(position, commentCard);
        publishDate.setText(commentCard.date);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete the comment?")
                        .setNegativeButton("CANCEL", null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i(TAG, "Remove comment: " + commentCard.getText());
                                FirebaseDatabase.getInstance().getReference("comment")
                                        .child(commentCard.getAid())
                                        .child(commentCard.getId())
                                        .removeValue();
                                Toast.makeText(context, "Comment removed", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, articleDetail.class);
                intent.putExtra(articleDetail.ARTICLE_CARD, articleCards[position]);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private void setText(CommentCard commentCard) {
        String text = commentCard.getText();
        if (text.length() > MAX_COMMENT_TEXT) {
            comment.setText(text.substring(0, MAX_COMMENT_TEXT) + "...");
        } else {
            comment.setText(text);
        }
    }

    private void setArticleTitle(int position, CommentCard commentCard) {
        Query query = FirebaseDatabase.getInstance().getReference("article")
                .orderByChild("id")
                .equalTo(commentCard.getAid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ArticleCard articleCard = dataSnapshot.getValue(ArticleCard.class);
                    articleCards[position] = articleCard;
                    articleTitle.setText(articleCard.getTitle());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
