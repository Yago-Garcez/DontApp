package com.example.yagog.dontapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class PictureFragment extends Fragment {

    private FloatingActionButton addFab, cameraFab, galleryFab;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private boolean isOpen = false;

    public PictureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_picture, container, false);

        addFab = (FloatingActionButton) v.findViewById(R.id.addFab);
        cameraFab = (FloatingActionButton) v.findViewById(R.id.cameraFab);
        galleryFab = (FloatingActionButton) v.findViewById(R.id.galleryFab);

        /*fabOpen = AnimationUtils.loadAnimation(null, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(null, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(null, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(null, R.anim.rotate_backward);*/

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.addFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });
        cameraFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                //Toast.makeText(PictureActivity.this, "Camera Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        galleryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                //Toast.makeText(PictureActivity.this, "Gallery Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

   private void animateFab(){
        if (isOpen){
            //addFab.startAnimation(rotateForward);
            //cameraFab.startAnimation(fabClose);
            //galleryFab.startAnimation(fabClose);
            cameraFab.setClickable(false);
            galleryFab.setClickable(false);
            cameraFab.setVisibility(View.INVISIBLE);
            galleryFab.setVisibility(View.INVISIBLE);
            isOpen = false;
        }
        else{
            //addFab.startAnimation(rotateBackward);
            //cameraFab.startAnimation(fabOpen);
            //galleryFab.startAnimation(fabOpen);
            cameraFab.setClickable(true);
            galleryFab.setClickable(true);
            cameraFab.setVisibility(View.VISIBLE);
            galleryFab.setVisibility(View.VISIBLE);
            isOpen = true;
        }
    }
}
