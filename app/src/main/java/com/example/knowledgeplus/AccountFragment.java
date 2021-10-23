package com.example.knowledgeplus;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment  {
    private String username;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        //TextView userNameTV = view.findViewById(R.id.username);
        //userNameTV.setText(username);

        Button profile = (Button) view.findViewById(R.id.profileButton);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        Button myArticles = (Button) view.findViewById(R.id.myArticlesButton);
        myArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyArticlesActivity.class);
                startActivity(intent);
            }
        });

        Button myComments = (Button) view.findViewById(R.id.myCommentsButton);
        myComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyCommentsActivity.class);
                startActivity(intent);
            }
        });

        Button about = (Button) view.findViewById(R.id.aboutButton);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        Button logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Log out?")
                        .setNegativeButton("CANCEL", null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logoutUser();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        return view;
    }

    // TODO : user can upload profile image

    private void logoutUser() {
        // TODO
        Intent intent = new Intent(getActivity(), MainActivity.class);
        Toast.makeText(getContext(), "You have successfully logged out", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

}
