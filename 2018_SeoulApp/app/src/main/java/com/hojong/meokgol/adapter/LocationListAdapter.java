package com.hojong.meokgol.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hojong.meokgol.IShowableProgress;
import com.hojong.meokgol.R;
import com.hojong.meokgol.activity.ShopListActivity;
import com.hojong.meokgol.data_model.Location;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationListAdapter extends MyListAdapter implements AdapterView.OnItemClickListener
{
	private List<Location> locationDataList = new ArrayList<>();

    public Callback<List<Location>> callbackListLocation(final IShowableProgress fragment, final List<Call> callList) {
        return new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                Log.d(this.toString(), "callbackListLocation " + response.body());
                callList.remove(call);
                clear();
                for (Location i : response.body()) {
                    Log.d(this.toString(), "location_img="+i.location_img);
                    addItem(i);
                }
                notifyDataSetChanged();
//				showProgress(false);
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                Log.d(this.toString(), "지역 가져오기 실패 " + t.toString());
                callList.remove(call);
                if (fragment.getActivity() != null)
                    Toast.makeText(fragment.getContext(), "지역 가져오기 실패", Toast.LENGTH_SHORT).show();
                fragment.showProgress(false);
                // finish?
            }
        };
    }

	@Override
	public int getCount() {
		return locationDataList.size() ;
	}

	@Override
	public View getView(int position, View locationView, ViewGroup parent) {
		final Context context = parent.getContext();

		if (locationView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			locationView = inflater.inflate(R.layout.layout_location_list_item, parent, false);
		}

		// get view
		ImageView imageView = locationView.findViewById(R.id.location_img);
		// get data
		Location location = locationDataList.get(position);
		// set data to view
        Glide.with(context).load(location.location_img).into(imageView);

//        if (location.getBmp() != null) {
//            Log.d(this.toString(), location.getBmp().toString());
//            imageView.setImageBitmap(location.getBmp());
//            imageView.setBackground(new BitmapDrawable(parent.getResources(), location.getBmp()));
//        }

		return locationView;
	}

	@Override
	public long getItemId(int position) {
		return position ;
	}
	
	@Override
	public Object getItem(int position) {
		return locationDataList.get(position) ;
	}

	public void addItem(Object location) {
		locationDataList.add((Location) location);
	}

	public void clear() {
	    locationDataList.clear();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Context context = adapterView.getContext();
        Intent intent = new Intent(context, ShopListActivity.class);
        Location location = locationDataList.get(i);
        intent.putExtra(Location.INTENT_KEY, location);
        Log.d(this.toString(), "Selected " + location);
        context.startActivity(intent);
    }
}
