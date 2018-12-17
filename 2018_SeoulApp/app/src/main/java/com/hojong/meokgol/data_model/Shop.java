package com.hojong.meokgol.data_model;

import android.util.Log;

import java.io.Serializable;
import java.util.List;

public class Shop implements Serializable
{
    public static final String INTENT_KEY = "shop";
	public int shop_idx;
	public String shop_name;
	public String shop_info;
	public String shop_img;
	public int location_idx;
	public List<ShopMenu> menu_list;
	public String shop_phone;
	public double review_avg;
	public int review_count;
	public int favorite;
	public int shop_latitude; // 위도
	public int shop_longitude; // 경도
	public String menu_kind1;

	public Shop (String shop_name)
	{
	    shop_idx = 1;
        this.shop_name = shop_name;
    }

    public void merge(Shop recv_shop)
	{
        Shop shop;

	    shop = recv_shop;
        Log.d("merge()", String.format("%s,%s/%s,%s/%s,%s", shop.review_avg, shop.review_count, shop.menu_list, shop.shop_phone, shop.shop_latitude, shop.shop_longitude));
        shop = this;
		Log.d("merge()", String.format("%s,%s/%s,%s/%s,%s", shop.review_avg, shop.review_count, shop.menu_list, shop.shop_phone, shop.shop_latitude, shop.shop_longitude));

		menu_list = recv_shop.menu_list;
		shop_phone = recv_shop.shop_phone;

		shop_latitude = recv_shop.shop_latitude;
		shop_longitude = recv_shop.shop_longitude;
	}

    @Override
    public String toString()
    {
        return String.format("Shop(%s:%s/menuList=%s/favorite=%s)", shop_idx, shop_name, menu_list, favorite);
    }
}
