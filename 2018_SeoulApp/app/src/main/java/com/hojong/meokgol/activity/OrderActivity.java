package com.hojong.meokgol.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hojong.meokgol.R;
import com.hojong.meokgol.adapter.ShopMenuListAdapter;
import com.hojong.meokgol.data_model.Shop;
import com.hojong.meokgol.data_model.ShopMenu;

public class OrderActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    ShopMenuListAdapter adapter;
    ListView listView;
    Shop shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        shop  = (Shop) getIntent().getSerializableExtra(Shop.INTENT_KEY);
        Log.d(toString(), "menuList=" + shop.menu_list);

        adapter = new ShopMenuListAdapter();
        for (ShopMenu shopMenu : shop.menu_list)
            adapter.addItem(shopMenu);

        listView = findViewById(R.id.menu_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ShopMenu shopMenu = (ShopMenu) adapter.getItem(position);
        final String msg = String.format(
                "[먹거리골목 먹골 앱]\n" +
                "%s\n" +
                "%s 주문합니다!", shop.shop_name, shopMenu.menu_name);

        Toast.makeText(this, String.format("%s 메뉴를 주문합니다", shopMenu.menu_name), Toast.LENGTH_SHORT).show();

        // menu order
        Log.d(toString(), "phoneNumber=" + shop.shop_phone);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + shop.shop_phone));
        sendIntent.putExtra("sms_body", msg);
        startActivity(sendIntent);
    }
}
