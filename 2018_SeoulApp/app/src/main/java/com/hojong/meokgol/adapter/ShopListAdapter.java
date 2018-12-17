package com.hojong.meokgol.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.hojong.meokgol.APIClient;
import com.hojong.meokgol.IShowableProgress;
import com.hojong.meokgol.LoginSharedPreference;
import com.hojong.meokgol.R;
import com.hojong.meokgol.activity.ShopActivity;
import com.hojong.meokgol.data_model.Shop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopListAdapter extends MyListAdapter implements View.OnClickListener
{
	public static final int ADD_FAV = 1;
	public static final int RM_FAV = 0;
	private List<Shop> shopDataList = new ArrayList<>();

	@Override
	public int getCount() {
		return shopDataList.size() ;
	}

	@Override
	public View getView(int position, View shopView, ViewGroup parent) {
		final Context context = parent.getContext();

		// "listview_item" Layout을 inflate하여 convertView 참조 획득.
		if (shopView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			shopView = inflater.inflate(R.layout.layout_shop_list_item, parent, false);
		}

		Shop shop = shopDataList.get(position);

		// shop bmp
		ImageView shopImageView = shopView.findViewById(R.id.location_img);
        Glide.with(context).load(shop.shop_img).into(shopImageView);
		// shop name
		TextView shopNameView = shopView.findViewById(R.id.shop_name);
		shopNameView.setText(shop.shop_name);

		// TODO : shop score to image (lazy)
		TextView shopScoreView = shopView.findViewById(R.id.score_view);
		shopScoreView.setText(String.format(Locale.KOREA, "별점 : %.2f", shop.review_avg));
		TextView reviewCntView = shopView.findViewById(R.id.review_cnt_view);
		reviewCntView.setText("후기 수 : " + shop.review_count + "개");

        List<String> kindList = Arrays.asList(shop.menu_kind1.split(","));

		ImageView kindFriedView = shopView.findViewById(R.id.kind_fried_view);
		if (kindList.contains("튀김류"))
		    kindFriedView.setVisibility(View.VISIBLE);
		else
            kindFriedView.setVisibility(View.GONE);

        ImageView kindNoodleView = shopView.findViewById(R.id.kind_noodle_view);
        if (kindList.contains("면류"))
            kindNoodleView.setVisibility(View.VISIBLE);
        else
            kindNoodleView.setVisibility(View.GONE);

        ImageView kindRiceView = shopView.findViewById(R.id.kind_rice_view);
        if (kindList.contains("밥류"))
            kindRiceView.setVisibility(View.VISIBLE);
        else
            kindRiceView.setVisibility(View.GONE);

        ImageView kindVegetableView = shopView.findViewById(R.id.kind_vegetable_view);
        if (kindList.contains("야채류"))
            kindVegetableView.setVisibility(View.VISIBLE);
        else
            kindVegetableView.setVisibility(View.GONE);

		// click listener
		ImageButton shopFavoriteBtn = shopView.findViewById(R.id.favorite_btn);
		if (shop.favorite == 1)
			shopFavoriteBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.shop_fav_on));
		shopFavoriteBtn.setOnClickListener(this);

		return shopView;
	}

	@Override
	public long getItemId(int position) {
		return position ;
	}
	
	@Override
	public Object getItem(int position) {
		return shopDataList.get(position) ;
	}

	public void addItem(Object shop) {
		Log.d(toString(), "addItem " + shop);
		shopDataList.add((Shop) shop);
	}

    public void clear()
    {
        shopDataList.clear();
    }

    @Override
    public void onClick(View view) {
	    // add/rm fav shop button
        ViewGroup listView = (ViewGroup) view.getParent().getParent();
        View itemView = (View) view.getParent();

        int userIdx = LoginSharedPreference.getUserIdx(view.getContext());
        if (userIdx == -1) {
			Toast.makeText(view.getContext(), "즐겨찾기는 로그인이 필요합니다", Toast.LENGTH_SHORT).show();
			return;
		}

        Shop shop = shopDataList.get(listView.indexOfChild(itemView));
        Log.d(toString(), "onClick.item="+shop);

        if (shop.favorite == 0) { // add
			Log.d(toString(), String.format("addFavorite userIdx=%s,shopIdx=%s", userIdx, shop.shop_idx));
            APIClient.getService().addFavoriteShop(userIdx, shop.shop_idx).enqueue(callbackFavoriteShop(ADD_FAV, (ImageButton) view, shop, listView.getContext()));
        }
        else // remove
        {
			Log.d(toString(), String.format("rmFavorite userIdx=%s,shopIdx=%s", userIdx, shop.shop_idx));
            APIClient.getService().removeFavoriteShop(userIdx, shop.shop_idx).enqueue(callbackFavoriteShop(RM_FAV, (ImageButton) view, shop, listView.getContext()));
        }
    }

    public Callback<List<Shop>> callbackShopList(final IShowableProgress showableProgress, final List<Call> callList, final String failureMsg)
    {
        return new Callback<List<Shop>>() {
            @Override
            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
                Log.d(this.toString(), String.format("callbackShopList msg=%s,shopList=%s", response.message(), response.body()));
                if (response.code() == 200) {
                    callList.remove(call);
                    clear();
                    for (Shop i : response.body()) {
                        Log.d(this.toString(), "shop_img=" + i.shop_img);
                        addItem(i);
                    }
                    notifyDataSetChanged();
				    showableProgress.showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {
                Log.d(this.toString(), failureMsg + t.toString());
                callList.remove(call);

                if (showableProgress.getActivity() == null)
                    return;

                Toast.makeText(showableProgress.getActivity(), failureMsg, Toast.LENGTH_SHORT).show();
                showableProgress.showProgress(false);
            }
        };
    }

    public Callback<Shop> callbackShopInfo(final Shop shop, final IShowableProgress showableProgress, final Context context)
    {
        return new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {
                Log.d(this.toString(), String.format("callbackShopInfo msg=%s,shopList=%s", response.message(), response.body()));
                if (response.code() == 200) {
                	Shop recv_shop = response.body();
//					Log.d(toString(), String.format("recv_shop shop_longitude=%s, shop_latitude=%s", recv_shop.shop_longitude, recv_shop.shop_latitude));
                	shop.merge(recv_shop);

				    showableProgress.showProgress(false);

                    Intent intent = new Intent(context, ShopActivity.class);
                    Log.d(toString(), String.format("shop=%s,phone=%s", shop, shop.shop_phone));
                    intent.putExtra(Shop.INTENT_KEY, shop);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {
                Log.d(this.toString(), "가게 정보 가져오기 실패" + t.toString());

                if (showableProgress.getActivity() == null)
                    return;

                Toast.makeText(showableProgress.getActivity(), "가게 정보 가져오기 실패", Toast.LENGTH_SHORT).show();
                showableProgress.showProgress(false);
            }
        };
    }

    public Callback<JsonObject> callbackFavoriteShop(final int type, final ImageButton view, final Shop shop, final Context context)
    {
    	final ShopListAdapter adapter = this;

        return new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200 && response.body().get("success").getAsBoolean()) {
                	if (type == ADD_FAV) {
						Toast.makeText(context, "즐겨찾기 추가 완료", Toast.LENGTH_SHORT).show();
						view.setBackground(ContextCompat.getDrawable(context, R.drawable.shop_fav_on));
						shop.favorite = 1;
					}
					else if (type == RM_FAV) {
						Toast.makeText(context, "즐겨찾기 취소 완료", Toast.LENGTH_SHORT).show();
						view.setBackground(ContextCompat.getDrawable(context, R.drawable.shop_fav_off));
						shop.favorite = 0;
					}
				}
                else {
					Log.d(toString(), "callbackAddFavoriteShop.msg=" + response.message());
					if (type == ADD_FAV) {
						Toast.makeText(context, "즐겨찾기 추가 실패", Toast.LENGTH_SHORT).show();
					}
					else if (type == RM_FAV) {
						Toast.makeText(context, "즐겨찾기 취소 실패", Toast.LENGTH_SHORT).show();
					}
				}
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
				Log.d(toString(), t.getMessage());
				if (type == ADD_FAV) {
					Toast.makeText(context, "즐겨찾기 추가 실패", Toast.LENGTH_SHORT).show();
				}
				else if (type == RM_FAV) {
					Toast.makeText(context, "즐겨찾기 취소 실패", Toast.LENGTH_SHORT).show();
				}
            }
        };
    }
}
