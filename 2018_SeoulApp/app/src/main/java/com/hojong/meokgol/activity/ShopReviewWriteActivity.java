package com.hojong.meokgol.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hojong.meokgol.APIClient;
import com.hojong.meokgol.LoginSharedPreference;
import com.hojong.meokgol.R;
import com.hojong.meokgol.data_model.Shop;
import com.hojong.meokgol.data_model.ShopReview;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopReviewWriteActivity extends AppCompatActivity {
    EditText scoreView;
    EditText titleView;
    EditText contentsView;
    View writeView;
    View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_review_write);

        scoreView = findViewById(R.id.edit_review_score);
        titleView = findViewById(R.id.review_title);
        contentsView = findViewById(R.id.review_contents);
        writeView = findViewById(R.id.write_form);
        mProgressView = findViewById(R.id.progress_bar);


        // set visible back arrow button
//        ActionBar actionbar = getSupportActionBar();
//        actionbar.setDisplayHomeAsUpEnabled(true);

        View writeBtn = findViewById(R.id.writeBtn);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptData();
            }
        });

        View cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
    
    private Callback<JsonObject> callbackWriteReview()
    {
        return new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(getApplicationContext(), "리뷰 작성 완료", Toast.LENGTH_SHORT).show();
                showProgress(false);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(this.toString(), "리뷰 작성 실패 " + t.toString());
                Toast.makeText(getApplicationContext(), "리뷰 작성 실패", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show)
    {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        writeView.setVisibility(show ? View.GONE : View.VISIBLE);
        writeView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                writeView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    // 뒤로가기 버튼 onClick
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void attemptData()
    {
        Shop shop = (Shop) getIntent().getSerializableExtra(Shop.INTENT_KEY);
        if (shop.shop_idx == -1) {
            Log.d(this.toString(), "wrong shopIdx=-1");
            showProgress(false);
            return;
        }

        String title = titleView.getText().toString();
        String content = contentsView.getText().toString();
        int star = Integer.parseInt(scoreView.getText().toString());
        if (star < 1) star = 1;
        else if (star > 5) star = 5;
        int userIdx = LoginSharedPreference.getUserIdx(getApplicationContext());

        ShopReview review = new ShopReview(title, content, star, userIdx, shop.shop_idx);

        showProgress(true);
        APIClient.getService().writeReview(review).enqueue(callbackWriteReview());
    }
}
