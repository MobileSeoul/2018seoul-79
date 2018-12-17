package com.hojong.meokgol;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hojong.meokgol.data_model.User;

public class LoginSharedPreference {

    private static final String USER_IDX_KEY = "user_idx";

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void saveLoginIdx(Context ctx, int userIdx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(USER_IDX_KEY, userIdx);
        editor.apply();
    }

    public static int getUserIdx(Context ctx) {
        return getSharedPreferences(ctx).getInt(USER_IDX_KEY, -1);
    }

    public static void removeLoginIdx(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(USER_IDX_KEY);
        editor.apply();
    }
}
