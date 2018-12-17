package com.hojong.meokgol.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.hojong.meokgol.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public abstract class MyAppCompatActivity extends AppCompatActivity {
    protected List<Call> callList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callList = new ArrayList<>();
    }
}
