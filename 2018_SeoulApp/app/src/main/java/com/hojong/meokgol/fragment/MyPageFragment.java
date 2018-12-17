package com.hojong.meokgol.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hojong.meokgol.APIClient;
import com.hojong.meokgol.LoginSharedPreference;
import com.hojong.meokgol.R;
import com.hojong.meokgol.activity.LoginActivity;
import com.hojong.meokgol.data_model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageFragment extends MyFragment implements View.OnClickListener
{
	public static final int LOGIN_SUCCESS = 0;
    public static User user = null;
    public Context appContext;
	View loginBtn;

	View contentView;
	TextView userNameView;
    TextView userIdView;
    TextView userEmailView;
	View logoutBtn;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView;
		int userIdx = LoginSharedPreference.getUserIdx(getContext());

        if (userIdx == -1) {
			rootView = inflater.inflate(R.layout.fragment_my_page_login, null);

			loginBtn = rootView.findViewById(R.id.login_btn);
			loginBtn.setOnClickListener(this);
		}
		else
		{
			rootView = inflater.inflate(R.layout.fragment_my_page, null);
            contentView = rootView.findViewById(R.id.content_view);
            userNameView = rootView.findViewById(R.id.user_name);
            userIdView = rootView.findViewById(R.id.user_id);
            userEmailView = rootView.findViewById(R.id.user_email);
			logoutBtn = rootView.findViewById(R.id.logout_btn);
			logoutBtn.setOnClickListener(this);

			if (user == null)
                attemptData();
			else
			    fillTextView();

            mProgressView = rootView.findViewById(R.id.progress_bar);
		}

		return rootView;
	}

	public Callback<User> callbackUserInfo(final int userIdx)
    {
        return new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                user.user_idx = userIdx;
                Log.d(this.toString(), user.toString());
                callList.remove(call);

                fillTextView();

                showProgress(false);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(this.toString(), "유저 정보 가져오기 실패 " + t.toString());
                callList.remove(call);
                if (getActivity() != null)
                    Toast.makeText(getContext(), "유저 정보 가져오기 실패", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        };
    }

    private void fillTextView()
    {
        if (userNameView == null || userIdView == null)
            return;

        userNameView.setText(user.user_name);
        userIdView.setText(user.user_id);
		userEmailView.setText(user.user_email);
    }

    @Override
    public void showProgress(final boolean show)
    {
        Log.d(this.toString(), "show="+show);
        if (mProgressView == null || getActivity() == null)
            return;

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        contentView.setVisibility(show ? View.GONE : View.VISIBLE);
        contentView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                contentView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

	@Override
	public void onClick(View view) {
		if (view == loginBtn)
			startLoginActivity();
		else if (view == logoutBtn) {
			logout();
			refreshFragment();
		}
	}

	private void startLoginActivity()
	{
		Intent intent = new Intent(getContext(), LoginActivity.class);
		startActivityForResult(intent, LOGIN_SUCCESS);
	}

	private void logout()
	{
		showProgress(true);
		LoginSharedPreference.removeLoginIdx(getContext());
		user = null;
        showProgress(false);
	}

	private void refreshFragment()
	{
		getFragmentManager().beginTransaction()
				.detach(this)
				.attach(this)
				.commit();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(this.toString(), "resultCode: "+ resultCode);

		if (resultCode == requestCode)
		{
			refreshFragment();
		}
	}

	@Override
    public void attemptData()
    {
        Context context = appContext;

        int userIdx = LoginSharedPreference.getUserIdx(context);
        if (userIdx == -1 || user != null)
            return;

        super.attemptData();
        Call call = APIClient.getService().getUserInfo(userIdx);
        callList.add(call);
        call.enqueue(callbackUserInfo(userIdx));
    }
}
