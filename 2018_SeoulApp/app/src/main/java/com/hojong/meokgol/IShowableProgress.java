package com.hojong.meokgol;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

public interface IShowableProgress {
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    void showProgress(final boolean show);

    Activity getActivity();
    Context getContext();
}
