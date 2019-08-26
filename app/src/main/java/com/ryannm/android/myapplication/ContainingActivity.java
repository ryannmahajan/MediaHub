package com.ryannm.android.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class ContainingActivity extends AppCompatActivity {
    public static final String CONTAIN_FRAGMENT = "String to identify which fragment need to be displayed";

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.containing_activity);

        switch (getIntent().getStringExtra(CONTAIN_FRAGMENT)) {
            case PictureFragment.TAG:
                showFragment(PictureFragment.newInstance());
                break;
            case ShowRecordingsFragment.TAG:
                showFragment(new ShowRecordingsFragment());
                break;
            case VideoFragment.TAG:
                showFragment(new VideoFragment());
                break;

        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
