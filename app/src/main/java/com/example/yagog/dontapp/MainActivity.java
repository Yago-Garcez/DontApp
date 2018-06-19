package com.example.yagog.dontapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //private BottomNavigationView mMainNav;
    //private FrameLayout mMainFrame;
    private TextFragment textFragment;
    private PictureFragment pictureFragment;

    public FloatingActionButton addFab, cameraFab, galleryFab;
    public Animation fabOpen, fabClose, rotateForward, rotateBackward;
    public boolean isOpen = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_text:
                    setFragment(textFragment);

                    return true;

                case R.id.navigation_picture:
                    setFragment(pictureFragment);
                    //Intent intent = new Intent (MainActivity.this, PictureActivity.class);
                    //startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mMainFrame = (FrameLayout) findViewById(R.id.mainFrame);
        //mMainNav = (BottomNavigationView) findViewById(R.id.mainNav);
        textFragment = new TextFragment();
        pictureFragment = new PictureFragment();
        setFragment(textFragment);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.mainNav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
