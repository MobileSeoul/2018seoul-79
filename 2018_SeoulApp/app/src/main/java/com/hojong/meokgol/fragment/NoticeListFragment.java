package com.hojong.meokgol.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.hojong.meokgol.APIClient;
import com.hojong.meokgol.R;
import com.hojong.meokgol.adapter.NoticeListAdapter;
import com.hojong.meokgol.data_model.Notice;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO 데이터 유지 (lazy)
public class NoticeListFragment extends MyFragment
{
	ListView listView;
	NoticeListAdapter adapter;
	View contentView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_notice_list, null);

		mProgressView = rootView.findViewById(R.id.progress_bar);
		listView = rootView.findViewById(R.id.notice_list);

		adapter = new NoticeListAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(adapter);

		contentView = rootView.findViewById(R.id.notice_list);

		attemptData();

		return rootView;
	}

	private Callback<List<Notice>> callbackNoticeList() {
		return new Callback<List<Notice>>() {
			@Override
			public void onResponse(Call<List<Notice>> call, Response<List<Notice>> response) {
				Log.d(this.toString(), "response "+response.body());
				callList.remove(call);
				adapter.clear();

				if (response.body() != null) {
					for (Notice notice : response.body())
						adapter.addItem(notice);
					adapter.notifyDataSetChanged();
				}
				showProgress(false);
			}

			@Override
			public void onFailure(Call<List<Notice>> call, Throwable t) {
				Log.d(this.toString(), "공지사항 가져오기 실패 " + t.toString());
				callList.remove(call);
				if (getActivity() != null)
					Toast.makeText(getContext(), "공지사항 가져오기 실패", Toast.LENGTH_SHORT).show();
				showProgress(false);
			}
		};
	}

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show)
	{
		if (getActivity() == null)
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

	public void attemptData()
	{
	    super.attemptData();
		Call call = APIClient.getService().listNotice();
		callList.add(call);
		call.enqueue(callbackNoticeList());
	}
}
