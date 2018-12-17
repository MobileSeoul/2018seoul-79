package com.hojong.meokgol.data_model;

import java.io.Serializable;

public class ShopMenu implements Serializable
{
    public static final String INTENT_KEY = "menu";
    public String menu_name;
    public int menu_price;
    public String menu_img;
    public String menu_kind1;

    @Override
    public String toString()
    {
        return String.format("%s,%s,%s", menu_name, menu_price, menu_kind1);
    }
}
