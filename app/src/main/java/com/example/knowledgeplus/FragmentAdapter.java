package com.example.knowledgeplus;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {
    
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] {"HOME",  "ACCOUNT"};
    private Context context;

    public FragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                // Home Tab
                return HomeFragment.newInstance();
            case 1:
                // Account Tab
                return AccountFragment.newInstance();
        }

        return null;
    }
}
