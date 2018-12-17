package com.hojong.meokgol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hojong.meokgol.R;
import com.hojong.meokgol.data_model.ShopMenu;

import java.util.ArrayList;
import java.util.List;

public class ShopMenuListAdapter extends BaseAdapter
{
	private List<ShopMenu> menuDataList = new ArrayList<>();

	@Override
	public int getCount() {
		return menuDataList.size() ;
	}

	@Override
	public View getView(int position, View shopMenuView, ViewGroup parent) {
		final Context context = parent.getContext();

		if (shopMenuView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			shopMenuView = inflater.inflate(R.layout.layout_menu_list_item, parent, false);
		}
        ShopMenu shopMenu = menuDataList.get(position);

		ImageView imgView = shopMenuView.findViewById(R.id.menu_img_view);
		Glide.with(context).load(shopMenu.menu_img).into(imgView);

		TextView nameView = shopMenuView.findViewById(R.id.menu_name_view);
		nameView.setText(shopMenu.menu_name);

		TextView priceView = shopMenuView.findViewById(R.id.menu_price_view);
		priceView.setText(shopMenu.menu_price+"원");

		return shopMenuView;
	}

	@Override
	public long getItemId(int position) {
		return position ;
	}
	
	@Override
	public Object getItem(int position) {
		return menuDataList.get(position) ;
	}

	public void addItem(ShopMenu menu) {
		menuDataList.add(menu);
	}

	public void clear() {
		menuDataList.clear();
	}
}
