package com.hojong.meokgol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.hojong.meokgol.R;
import com.hojong.meokgol.data_model.Location;
import com.hojong.meokgol.fragment.FavoriteShopListFragment;
import com.hojong.meokgol.fragment.LocationListFragment;
import com.hojong.meokgol.fragment.MyFragment;
import com.hojong.meokgol.fragment.MyPageFragment;
import com.hojong.meokgol.fragment.NoticeListFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements OnTabSelectListener
{
	private Bundle locationListBundle;
	private MyPageFragment myPageFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(toString(), "onCreate");

		locationListBundle = new Bundle();
        Serializable locationList = getIntent().getSerializableExtra(Location.INTENT_KEY);
        if (locationList == null)
            Log.d(toString(), "NullLocationList");
		locationListBundle.putSerializable(Location.INTENT_KEY, locationList);

		init_fragment_bottombar();
	}

	private void init_fragment_bottombar()
    {
        myPageFragment = new MyPageFragment();
        myPageFragment.appContext = getApplicationContext();
        myPageFragment.attemptData();

        BottomBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(this);
        onTabSelected(R.id.nav_locations); // default fragment
    }

    @Override
    public void onTabSelected(int tabId) {
        Fragment fragment = null;

        switch (tabId)
        {
            case R.id.nav_locations:
                fragment = new LocationListFragment();
                fragment.setArguments(locationListBundle);
                Log.d(toString(), "bundle=" + fragment.getArguments());
                break;
            case R.id.nav_favorites:
                fragment = new FavoriteShopListFragment();
                break;
            case R.id.nav_notices:
                fragment = new NoticeListFragment();
                break;
            case R.id.nav_my_page:
                fragment = myPageFragment;
                break;
        }

        loadFragment(fragment);
    }

	private boolean loadFragment(Fragment fragment) {
		//switching fragment
		if (fragment != null) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container, fragment)
					.commit();

			return true;
		}
		return false;
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
			case R.id.tutorial:
				Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
				intent.putExtra("back", true);
				startActivity(intent);
				return true;
            case R.id.refresh:
                MyFragment fragment = (MyFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                fragment.attemptData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
