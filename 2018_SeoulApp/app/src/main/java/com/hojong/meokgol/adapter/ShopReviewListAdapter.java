package com.hojong.meokgol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hojong.meokgol.R;
import com.hojong.meokgol.data_model.ShopReview;

import java.util.ArrayList;
import java.util.List;

public class ShopReviewListAdapter extends BaseAdapter
{
	private List<ShopReview> shopReviewDataList = new ArrayList<>();

	@Override
	public int getCount() {
		return shopReviewDataList.size() ;
	}

	@Override
	public View getView(int position, View itemView, ViewGroup parent) {
		final Context context = parent.getContext();

		if (itemView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemView = inflater.inflate(R.layout.layout_shop_review_list_item, parent, false);
		}

		ShopReview review = shopReviewDataList.get(position);

        TextView titleView = itemView.findViewById(R.id.textview_title);
        titleView.setText(review.review_title);

		TextView writerView = itemView.findViewById(R.id.textview_writer);
		writerView.setText(review.user_name);

        TextView scoreView = itemView.findViewById(R.id.textview_score);
        scoreView.setText("별점 : "+review.review_star);

        TextView writeDateView = itemView.findViewById(R.id.textview_write_date);
        writeDateView.setText(review.review_date.toString());

        TextView contentView = itemView.findViewById(R.id.textview_content);
        contentView.setText(review.review_content);

		return itemView;
	}

	@Override
	public long getItemId(int position) {
		return position ;
	}
	
	@Override
	public Object getItem(int position) {
		return shopReviewDataList.get(position) ;
	}

	public void addItem(ShopReview shopReview) {
		shopReviewDataList.add(shopReview);
	}

	public void clear()
	{
		shopReviewDataList.clear();
	}
}
