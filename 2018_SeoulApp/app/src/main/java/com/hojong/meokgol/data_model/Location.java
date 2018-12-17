package com.hojong.meokgol.data_model;

import java.io.Serializable;

public class Location implements Serializable
{
	public static final String INTENT_KEY = "location";
	public int location_idx;
	public String location_name;
	public String location_info;
	public String location_img;

	@Override
	public String toString() { return String.format("%s:%s", location_idx, location_name); }
}
