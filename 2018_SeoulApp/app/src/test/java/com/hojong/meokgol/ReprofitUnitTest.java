package com.hojong.meokgol;

import com.google.gson.JsonObject;
import com.hojong.meokgol.data_model.Location;
import com.hojong.meokgol.data_model.Notice;
import com.hojong.meokgol.data_model.Shop;
import com.hojong.meokgol.data_model.ShopReview;
import com.hojong.meokgol.data_model.User;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ReprofitUnitTest
{
	private APIService service;

	public ReprofitUnitTest()
	{
		service = APIClient.getService();
	}

	@Test
	public void login() throws Exception
	{
		Response<JsonObject> response = service.login(new User("guest", "guest")).execute();
		assertEquals(200, response.code());
		System.out.println(response.body());
	}

	@Test
	public void getUserInfo() throws Exception
	{
		Response<User> response = service.getUserInfo(1).execute();
		assertEquals(200, response.code());
		System.out.println(response.body());
	}

	@Test
	public void listLocation() throws Exception
	{
		Response<List<Location>> response = service.listLocation().execute();
		assertEquals(200, response.code());
		System.out.println(response.body());
	}

	@Test
	public void listShop() throws Exception
	{
		List<String> menuList = new ArrayList<>();
//		menuList.add("면류");
		menuList.add("밥류");
		Response<List<Shop>> response = service.listShop(1, menuList, 3).execute();
		assertEquals(200, response.code());

		System.out.println(response.body());
		for (Shop shop : response.body())
		    System.out.println(shop.menu_kind1);
	}

	@Test
	public void getShopInfo() throws Exception
	{
		Response<Shop> response = service.getShopInfo(1).execute();
		assertEquals(200, response.code());
		System.out.println(response.body());
	}

	@Test
	public void listFavorite() throws Exception
	{
		Response<List<Shop>> response = service.listFavoriteShop(3).execute();
		assertEquals(200, response.code());
		System.out.println(response.body());
	}

	@Test
	public void addFavoriteShop() throws Exception
	{
		Response<JsonObject> response = service.addFavoriteShop(3, 1).execute();
		assertEquals(200, response.code());
		System.out.println(response.body());
		assertEquals(true, response.body().get("success").getAsBoolean());
	}

	@Test
	public void listReview() throws Exception
	{
		Response<List<ShopReview>> response = service.listReview(1).execute();
		assertEquals(200, response.code());
		System.out.println(response.body());
	}

	@Test
	public void writeReview() throws Exception
	{
		ShopReview review = new ShopReview("test_title1", "test_content1", 5, 1, 1);

		Response<JsonObject> response = service.writeReview(review).execute();
		assertEquals(200, response.code());
		assertEquals(true, response.body().get("success").getAsBoolean());
	}

	@Test
	public void deleteReview() throws Exception
	{
		ShopReview review = new ShopReview();
		review.user_idx = 1;
		review.review_idx = 1;

		Response<JsonObject> response = service.deleteReview(review).execute();
		assertEquals(200, response.code());
		System.out.println(response.body());
	}

	@Test
	public void listNotice() throws Exception
	{
		Response<List<Notice>> response = service.listNotice().execute();
		assertEquals(200, response.code());
		System.out.println(response.body());
	}
}
