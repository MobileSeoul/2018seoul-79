package com.hojong.meokgol.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hojong.meokgol.APIClient;
import com.hojong.meokgol.IShowableProgress;
import com.hojong.meokgol.LoginSharedPreference;
import com.hojong.meokgol.R;
import com.hojong.meokgol.adapter.ShopListAdapter;
import com.hojong.meokgol.data_model.Location;
import com.hojong.meokgol.data_model.Shop;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class ShopListActivity extends MyAppCompatActivity implements IShowableProgress, AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener
{
    protected ListView listView;
    protected View mProgressView;
	private DrawerLayout mDrawerLayout;
	private Location location;
	private ShopListAdapter adapter;
	private MenuItem shopFilterView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_list);

        location = (Location) getIntent().getSerializableExtra(Location.INTENT_KEY);
        Log.d(this.toString(), "locationIdx=" + location.location_idx);

		// set visible back arrow button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		adapter = new ShopListAdapter();
        listView = findViewById(R.id.shop_list);
		listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        mProgressView = findViewById(R.id.progress_bar);

        NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setItemIconTintList(null);

		initDrawer();
        attemptData(null);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		// list item click
		Shop shop = (Shop) adapter.getItem(i);

		showProgress(true);
		APIClient.getService().getShopInfo(shop.shop_idx).enqueue(adapter.callbackShopInfo(shop, this, adapterView.getContext()));
	}

    public void showProgress(final boolean show)
    {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        listView.setVisibility(show ? View.GONE : View.VISIBLE);
        listView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation) { listView.setVisibility(show ? View.GONE : View.VISIBLE); }
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

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    // 필터 버튼 생성
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_shop_filter, menu);
		shopFilterView = menu.getItem(0);
		return true;
	}

	// 뒤로가기 버튼, 필터 버튼 onClick
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.shop_filter:
                if (!mDrawerLayout.isDrawerOpen(GravityCompat.END))
				    mDrawerLayout.openDrawer(GravityCompat.END);
				else
				    mDrawerLayout.closeDrawer(GravityCompat.END);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// 필터 drawer
	protected void initDrawer()
	{
		mDrawerLayout = findViewById(R.id.drawer_layout);

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
	}

	public void attemptData(List menuKindList)
    {
        if (callList.size() > 0)
            return;

		int userIdx = LoginSharedPreference.getUserIdx(getContext());
        Call call = APIClient.getService().listShop(location.location_idx, menuKindList, userIdx);
        callList.add(call);
        call.enqueue(adapter.callbackShopList(this, callList, "가게 정보 가져오기 실패"));
    }

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
		// filter menu select
		String menuTitle = menuItem.getTitle().toString();
		Log.d(toString(), menuTitle);

		if (menuItem.getItemId() == R.id.nav_filter_all)
		{
            shopFilterView.setIcon(R.drawable.ic_shop_filter_off);
			attemptData(null);
		}
		else {
            shopFilterView.setIcon(R.drawable.ic_shop_filter_on);
			List<String> menuKindList = new ArrayList<>();
			menuKindList.add(menuTitle);

			attemptData(menuKindList);
		}

		menuItem.setChecked(true);
		mDrawerLayout.closeDrawers();
		return true;
	}
}
