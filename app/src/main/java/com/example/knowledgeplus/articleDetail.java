package com.example.knowledgeplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knowledgeplus.SendNotificationPack.APIService;
import com.example.knowledgeplus.SendNotificationPack.Client;
import com.example.knowledgeplus.SendNotificationPack.Data;
import com.example.knowledgeplus.SendNotificationPack.MyResponse;
import com.example.knowledgeplus.SendNotificationPack.NotificationSender;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class articleDetail extends AppCompatActivity {
    private final String TAG = "ArticleDetail";
    private final int MAX_DOWNLOAD_BUFFER_SIZE = 1024*1024*100; //100MB
    public static final String ARTICLE_CARD = "article_card";
    ArticleCard articleCard;
    TextView tvTitle, tvAuthor, tvLocation, tvPublishDate, tvNViews, tvNComments, tvBody, tvComment;
    EditText editText;
    Button send;
    LinearLayout images;
    DatabaseReference db;
    StorageReference imageReference;
    LinearLayout.LayoutParams lp;

    //String token = "ebj3TkJFQQevUL2K150Kuk:APA91bF8OLnOCgK2spFvUZtnY2PdCLfUVCodbWNRGJ0dVRX8gan_O3xRlDUrbOTAL_JhxkU_gaZ78KPf_s1DYWS_74ofO83j6Htcqp3Q7YBi27-paoMtx6GkgYH0K_Y5nupZI4BG3H27";
    DatabaseReference task;
    String token;
    private APIService apiService;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        Intent intent = getIntent();
        articleCard = (ArticleCard) intent.getSerializableExtra(ARTICLE_CARD);

        tvTitle = (TextView) findViewById(R.id.textViewTitle);
        tvAuthor = (TextView) findViewById(R.id.textViewAuthor);
        tvLocation = (TextView) findViewById(R.id.textViewLocation);
        tvPublishDate = (TextView) findViewById(R.id.textViewPublishdate);
        tvNViews = (TextView) findViewById(R.id.textViewNViews);
        tvNComments = (TextView) findViewById(R.id.textViewNComments);
        tvBody = (TextView) findViewById(R.id.textViewBody);
        tvComment = (TextView) findViewById(R.id.textViewComment);
        editText = (EditText) findViewById(R.id.editText);
        send = (Button) findViewById(R.id.button);
        images = (LinearLayout) findViewById(R.id.images);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,500);
        lp.setMargins(0,10,0,10);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        tvTitle.setText(articleCard.getTitle());
        tvAuthor.setText(articleCard.getAuthor());
        tvPublishDate.setText(articleCard.getPublishDate());
        tvNViews.setText(""+articleCard.getnViews());
        tvNComments.setText(""+articleCard.getnComments());
        tvBody.setText(articleCard.getBody());

        // set location
        if (articleCard.getLocation() == null || articleCard.getLocation().isEmpty()) {
            tvLocation.setText("Unknown");
        } else {
            tvLocation.setText(articleCard.getLocation());
        }

        db = FirebaseDatabase.getInstance().getReference("comment").child(articleCard.getId());
        db.addValueEventListener(listAllCommentsListener);


        // set imageView
        if (articleCard.nImages == 0) {
            Log.i(TAG, "Article " + articleCard.getTitle() +" has no image");
        } else {
            loadImages();
        }

        // set send-comment button
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(articleDetail.this);
                builder.setMessage("Send the comment?")
                        .setNegativeButton("CANCEL", null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendComment();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                task = FirebaseDatabase.getInstance().getReference().child("Tokens").child(articleCard.getUid());
                task.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        token = snapshot.child("token").getValue().toString();
                        Log.d("159", token);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void sendComment() {
        String text = editText.getText().toString().trim();
        username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (text.isEmpty()) {
            Toast.makeText(articleDetail.this, "Please enter your comment", Toast.LENGTH_SHORT).show();
            return;
        }

        String comment_id = db.push().getKey();
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String date = df.format("yyyy/MM/dd", Calendar.getInstance().getTime()).toString();
        db.child(comment_id).setValue(CommentCard.newInstance(comment_id, uid, username, articleCard.getId(), text, date));

        Toast.makeText(articleDetail.this, "Comment sent", Toast.LENGTH_SHORT).show();
        editText.setText("");
        //sendNotification(username);
        new Notify().execute();
        closeKeyBoard();
    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public class Notify extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "key=AAAAVoX4LkA:APA91bH771jkVN4jojE1iy8l9uOs1Rbh2vVAD9mymJ3XpZFx8AyM-J8wr-NA8ucXS5ZMpt2Ppg-KARsf1B5YNdhkxopWXW5Mw7w56g6SBJ_heHUxworykCLwYq4638vrMMsZXMGaE-7U");
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject json = new JSONObject();
                json.put("to", token);

                JSONObject info = new JSONObject();
                info.put("title", username + " gave you a comment");
                info.put("body", "Open the app to view");

                json.put("notification", info);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(json.toString());
                wr.flush();
                conn.getInputStream();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    // send notification to author of the article
//    private void sendNotification(String commenter) {
//        FirebaseDatabase.getInstance().getReference().child("Tokens").child(articleCard.getUid()).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String usertoken = snapshot.getValue(String.class);
//                String title = "You got a new comment for your knowledge!";
//                Log.d("158", title);
//                String body = commenter + " comments on your article";
//                Log.d("160", body);
//                sendNotification(usertoken, title, body);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void sendNotification(String usertoken, String title, String message) {
//        Log.d("articleDetail", usertoken);
//        Data data = new Data(title, message);
//        NotificationSender sender = new NotificationSender(data, usertoken);
//        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
//            @Override
//            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                if (response.code() == 200) {
//                    Log.d("190", "success");
//                    if (response.body().success != 1) {
//                        Toast.makeText(articleDetail.this, "Failed", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MyResponse> call, Throwable t) {
//
//            }
//        });
//
//    }

    private void loadImages() {
        imageReference = FirebaseStorage.getInstance().getReference().child("images").child(articleCard.getId());

        for (int i = 0; i < articleCard.getnImages(); i++) {

            imageReference.child(""+i).getBytes(MAX_DOWNLOAD_BUFFER_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setImageBitmap(bitmap);
                    imageView.setLayoutParams(lp);
                    images.addView(imageView);
                }
            });
        }
    }

    ValueEventListener listAllCommentsListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            String commentString = "";
            for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                CommentCard comment = commentSnapshot.getValue(CommentCard.class);
                commentString += "\n" + comment.getUsername() +  ", " + comment.getDate() + ":\n" + comment.getText() + "\n";
            }
            tvComment.setText(commentString);
            int nComments = (int)snapshot.getChildrenCount();
            tvNComments.setText(""+nComments);
            FirebaseDatabase.getInstance().getReference("article").child(articleCard.getId()).child("nComments").setValue(nComments);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            //Nothing
        }
    };

}