package com.hojong.meokgol.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hojong.meokgol.R;
import com.hojong.meokgol.data_model.Shop;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;

public class ShopMapFragment extends Fragment implements View.OnClickListener, NMapView.OnMapStateChangeListener, NMapView.OnMapViewTouchEventListener
{
    NMapView mapView;
	private NMapContext mMapContext;
	private static final String CLIENT_ID = "xU8vQG7PePFCSD0Qr6M6";// 애플리케이션 클라이언트 아이디 값
	Shop shop;
	NMapController mMapController;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_shop_map, null);
        Context context = container.getContext();
		shop = (Shop) getArguments().getSerializable(Shop.INTENT_KEY);

		TextView mapBtn = rootView.findViewById(R.id.map_btn);
		mapBtn.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMapContext =  new NMapContext(super.getActivity());
		mMapContext.onCreate();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mapView = (NMapView)getView().findViewById(R.id.mapView);
		Log.d(toString(), "mapView=" + mapView);
		if (mapView == null)
			return;

		mapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정
//		mapView.setClickable(true);

		mMapContext.setupMapView(mapView);

		// use map controller to zoom in/out, pan and set map center, zoom level etc.
		mMapController = mapView.getMapController();

		mapView.setOnMapStateChangeListener(this);
		mapView.setOnMapViewTouchEventListener(this);
	}

	@Override
	public void onStart(){
		super.onStart();
		mMapContext.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapContext.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapContext.onPause();
	}

	@Override
	public void onStop() {
		mMapContext.onStop();
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		mMapContext.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onMapInitHandler(NMapView nMapView, NMapError nMapError)
	{
		if (nMapError == null) { // success
			int level = 14;
			Log.d(toString(), String.format("shop_longitude=%s, shop_latitude=%s", shop.shop_longitude, shop.shop_latitude));
			mMapController.setMapCenter(new NGeoPoint(shop.shop_longitude, shop.shop_latitude), level);
		} else { // fail
			Log.e(toString(), "onMapInitHandler: error=" + nMapError.toString());
		}
	}

	@Override
	public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint)
	{

	}

	@Override
	public void onMapCenterChangeFine(NMapView nMapView)
	{

	}

	@Override
	public void onZoomLevelChange(NMapView nMapView, int i)
	{

	}

	@Override
	public void onAnimationStateChange(NMapView nMapView, int i, int i1)
	{

	}

	@Override
	public void onLongPress(NMapView nMapView, MotionEvent motionEvent)
	{

	}

	@Override
	public void onLongPressCanceled(NMapView nMapView)
	{

	}

	@Override
	public void onTouchDown(NMapView nMapView, MotionEvent motionEvent)
	{

	}

	@Override
	public void onTouchUp(NMapView nMapView, MotionEvent motionEvent)
	{

	}

	@Override
	public void onScroll(NMapView nMapView, MotionEvent motionEvent, MotionEvent motionEvent1)
	{

	}

	@Override
	public void onSingleTapUp(NMapView nMapView, MotionEvent motionEvent)
	{

	}

    @Override
    public void onClick(View view) {
        // map btn click
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        String url = String.format("geo:%s,%s?q=%s", shop.shop_latitude, shop.shop_longitude, shop.shop_name);
        sendIntent.setData(Uri.parse(url));
        startActivity(sendIntent);
    }
}
