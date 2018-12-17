package com.hojong.meokgol.data_model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.sql.Timestamp;

public class Notice implements Serializable
{
	public static final String INTENT_KEY = "notice";
	public int notice_idx;
	public String notice_title;
	public String notice_content;
	public String notice_img;
	public Timestamp notice_date;

//	public Drawable img;
//	public String name;

	public Notice(String notice_title)
	{
		this.notice_title = notice_title;
	}

	@Override
	public String toString()
	{
		return notice_idx+":"+notice_title;
	}
}
