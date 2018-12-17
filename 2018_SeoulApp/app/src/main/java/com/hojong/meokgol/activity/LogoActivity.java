package com.hojong.meokgol.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.hojong.meokgol.APIClient;
import com.hojong.meokgol.IShowableProgress;
import com.hojong.meokgol.R;
import com.hojong.meokgol.adapter.LocationListAdapter;
import com.hojong.meokgol.data_model.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;

public class LogoActivity extends MyAppCompatActivity implements IShowableProgress, AnimationListener
{
	private List<Location> locationList;
	Intent intent;
	boolean animDone;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);

        animDone = false;

        locationList = new ArrayList<>();
        attemptData();

        final GifImageView logoView = findViewById(R.id.logo_view);
        GifDrawable logoDrawable = (GifDrawable) logoView.getDrawable();
        logoDrawable.addAnimationListener(this);
        logoDrawable.setLoopCount(1);

        // is first check, init intent
        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        boolean isFirst = pref.getBoolean("isFirst", true);

        if(isFirst)
        {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst",false);
            editor.apply();

            intent = new Intent(getApplicationContext(), TutorialActivity.class);
        }
        else {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }
	}

    public void attemptData()
    {
        Call call = APIClient.getService().listLocation();
        callList.add(call);

        LocationListAdapter adapter = new LocationListAdapter() {
            @Override
            public void clear() { locationList.clear(); }
            @Override
            public void addItem(Object i) { locationList.add((Location)i); }
            @Override
            public int getCount() { return 0; }
            @Override
            public Object getItem(int position) { return null; }
            @Override
            public long getItemId(int position) { return 0; }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) { return null; }
            @Override
            public void notifyDataSetChanged() {
                // start activity
                intent.putExtra(Location.INTENT_KEY, (Serializable) locationList);
                Log.d(this.toString(), "LocationList=" + intent.getSerializableExtra(Location.INTENT_KEY).toString());

                if (animDone) {
                    startActivity(intent);
                    finish();
                }
            }
        };

        call.enqueue(adapter.callbackListLocation(this, callList));
    }

    @Override
    public void showProgress(boolean show) { }
    @Override
    public Activity getActivity() { return this; }
    @Override
    public Context getContext() { return getApplicationContext(); }

    @Override
    public void onAnimationCompleted(int loopNumber) {
        animDone = true;

        if (locationList.size() > 0) {
            startActivity(intent);
            finish();
        }
    }
}
