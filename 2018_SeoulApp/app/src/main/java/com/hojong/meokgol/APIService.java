package com.hojong.meokgol;

import com.google.gson.JsonObject;
import com.hojong.meokgol.data_model.Location;
import com.hojong.meokgol.data_model.Notice;
import com.hojong.meokgol.data_model.Shop;
import com.hojong.meokgol.data_model.ShopReview;
import com.hojong.meokgol.data_model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService
{
	@POST("/login")
	Call<JsonObject> login(@Body User user);

	@GET("/{userIdx}/userinfo")
	Call<User> getUserInfo(@Path("userIdx") int userIdx);

	@GET("/location")
	Call<List<Location>> listLocation();

	@GET("/{locationIdx}/shops")
	Call<List<Shop>> listShop(@Path("locationIdx") int locationIdx, @Query("menu_kind") List<String> menuKindList, @Query("user_idx") int userIdx);

    @GET("/{shopIdx}/shopinfo")
    Call<Shop> getShopInfo(@Path("shopIdx") int shopIdx);

	@GET("/{userIdx}/favoriteshop")
	Call<List<Shop>> listFavoriteShop(@Path("userIdx") int userIdx);

	@POST("/{userIdx}/favoriteshop")
    Call<JsonObject> addFavoriteShop(@Path("userIdx") int userIdx, @Query("shop_idx") int shopIdx);

	@DELETE("/{userIdx}/favoriteshop")
	Call<JsonObject> removeFavoriteShop(@Path("userIdx") int userIdx, @Query("shop_idx") int shopIdx);

	@GET("/{shopIdx}/review")
	Call<List<ShopReview>> listReview(@Path("shopIdx") int shopIdx);

	@POST("/review/write")
	Call<JsonObject> writeReview(@Body ShopReview data);

	@POST("/review/delete")
	Call<JsonObject> deleteReview(@Body ShopReview data);

	@GET("/notice")
	Call<List<Notice>> listNotice();
}
