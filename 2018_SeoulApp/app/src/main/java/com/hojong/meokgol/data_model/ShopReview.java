package com.hojong.meokgol.data_model;

import java.io.Serializable;
import java.sql.Timestamp;

public class ShopReview implements Serializable
{
	public int review_idx;
	public String review_title;
	public String review_content;
	public int review_star;
	public Timestamp review_date;
	public int user_idx;
	public String user_name;
	public int shop_idx;

	public ShopReview() { }

	public ShopReview(String title, String contents, int star, int user_idx, int shop_idx)
	{
		this.review_title = title;
		this.review_content = contents;
		this.review_star = star;
		this.user_idx = user_idx;
		this.shop_idx = shop_idx;
	}

	@Override
	public String toString()
	{
		return review_idx + ":" + review_title;
	}
}
