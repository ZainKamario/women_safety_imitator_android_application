package com.lixer.womensafetyapp;

import static android.app.Activity.RESULT_OK;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


import java.io.FileNotFoundException;

public class profile_screen extends Fragment {

    private View root_view;


//    private ConstraintLayout music_btn;
//    private ConstraintLayout sound_btn;
//    private ConstraintLayout profile_image_btn;
//    public static ImageView profile_image;
//
//
//
//
//
//    private Dialog profile_image_dialog_box;
//    private ConstraintLayout select_image_profile_dialog_box;
//    private ConstraintLayout change_avatar_profile_dialog_box;
//    private CardView cancel_btn_profile_dialog_box;
//    private TextView profileName ;
//    private TextView profileQueCount;
//
//    public static int REQUEST_FOR_IMAGE = 1;
//
//    private ImageView profile_back_btn;
//
//
//    private ConstraintLayout invite_friend;
//    private ConstraintLayout logout;
//
//    private String col_number = "";
//    private String id = "1";
//    private String value = "";
//    private Cursor cursor;
//    private ViewGroup Container;
//    private float switch_speed = 1.5f;
//    private Object profile_screen;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root_view = inflater.inflate(R.layout.profile_screen, container, false);

//        Container = container;
//
//
//        invite_friend = root_view.findViewById(R.id.invite_friend_btn);
//        music_btn = root_view.findViewById(R.id.music_btn);
//        sound_btn = root_view.findViewById(R.id.sound_btn);
//        logout = root_view.findViewById(R.id.logout_btn);
//        profile_image_btn = root_view.findViewById(R.id.profile_image_btn);
//        profile_image= root_view.findViewById(R.id.profile_image);
//        profile_back_btn = root_view.findViewById(R.id.profile_back_btn);
//        profileName = root_view.findViewById(R.id.profileName);
//        profileQueCount = root_view.findViewById(R.id.profileQueCount);
//
//
//        music_btn.setOnClickListener(this);
//        sound_btn.setOnClickListener(this);
//        invite_friend.setOnClickListener(this);
//        logout.setOnClickListener(this);
//        profile_image_btn.setOnClickListener(this);
//
//




        return root_view;

    }



}