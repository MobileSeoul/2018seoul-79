package com.hojong.meokgol.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hojong.meokgol.R;
import com.hojong.meokgol.adapter.ShopMenuListAdapter;
import com.hojong.meokgol.data_model.Shop;
import com.hojong.meokgol.data_model.ShopMenu;

public class ShopInfoFragment extends Fragment implements AdapterView.OnItemClickListener
{
    ListView listView;
	ShopMenuListAdapter adapter;

    @Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_shop_info, null);

		Shop shop = (Shop) getArguments().getSerializable(Shop.INTENT_KEY);
        Log.d(this.toString(), "shopInfo=" + shop.shop_info);

        TextView shopInfoView = rootView.findViewById(R.id.shop_info_view);
        shopInfoView.setText(shop.shop_info);

        adapter = new ShopMenuListAdapter();
        for (ShopMenu menu : shop.menu_list)
            adapter.addItem(menu);

		Log.d(toString(), "menuList=" + shop.menu_list);
		listView = rootView.findViewById(R.id.menu_list);
		listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

		return rootView;
	}

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // TODO : menu item click (lazy)
        Context context = adapterView.getContext();
        Log.d(toString(), "onClick");
    }
}
