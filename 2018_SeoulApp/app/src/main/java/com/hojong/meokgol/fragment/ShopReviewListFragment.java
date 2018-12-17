package com.hojong.meokgol.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hojong.meokgol.APIClient;
import com.hojong.meokgol.R;
import com.hojong.meokgol.activity.ShopReviewWriteActivity;
import com.hojong.meokgol.adapter.ShopReviewListAdapter;
import com.hojong.meokgol.data_model.Shop;
import com.hojong.meokgol.data_model.ShopReview;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ShopReviewListFragment extends MyFragment {
    Shop shop;
    ShopReviewListAdapter adapter;
    TextView scoreView;
    TextView cntView;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false); // refresh button disable
    }

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_shop_review_list, null);

        shop = (Shop) getArguments().getSerializable(Shop.INTENT_KEY);

        mProgressView = rootView.findViewById(R.id.progress_bar);

        ImageButton reviewWriteBtn = rootView.findViewById(R.id.review_write_btn);
        reviewWriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShopReviewWriteActivity.class);
                intent.putExtra(Shop.INTENT_KEY, shop);
                startActivityForResult(intent, RESULT_OK);
            }
        });

        scoreView = rootView.findViewById(R.id.review_score_view);
        scoreView.setText(String.format(Locale.KOREA, "별점 : %.2f", shop.review_avg));

        cntView = rootView.findViewById(R.id.review_cnt_view);
        cntView.setText("후기 수 : " + shop.review_count  + "개");

        adapter = new ShopReviewListAdapter();
		listView = rootView.findViewById(R.id.shop_review_list);
		listView.setAdapter(adapter);

        attemptData();

		return rootView;
	}

	private Callback<List<ShopReview>> callbackReviewList()
    {
        return new Callback<List<ShopReview>>() {
            @Override
            public void onResponse(Call<List<ShopReview>> call, Response<List<ShopReview>> response) {
                Log.d(this.toString(), "response "+response.body());
                callList.remove(call);
                adapter.clear();
                for (ShopReview notice : response.body())
                    adapter.addItem(notice);
                adapter.notifyDataSetChanged();
                showProgress(false);
            }

            @Override
            public void onFailure(Call<List<ShopReview>> call, Throwable t) {
                Log.d(this.toString(), "후기 가져오기 실패 " + t.toString());
                callList.remove(call);
                if (getActivity() != null)
                    Toast.makeText(getContext(), "후기 가져오기 실패", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        };
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show)
    {
        if (getActivity() == null)
            return;

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        listView.setVisibility(show ? View.GONE : View.VISIBLE);
        listView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                listView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    public void attemptData()
    {
        super.attemptData();
        Call call = APIClient.getService().listReview(shop.shop_idx);
        callList.remove(call);
        call.enqueue(callbackReviewList());
    }
}
