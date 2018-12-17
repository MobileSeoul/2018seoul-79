package com.hojong.meokgol.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hojong.meokgol.R;
import com.hojong.meokgol.activity.NoticeActivity;
import com.hojong.meokgol.data_model.Notice;

import java.util.ArrayList;
import java.util.List;

public class NoticeListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener
{
	private List<Notice> noticeDataList = new ArrayList<>();

	@Override
	public int getCount() {
		return noticeDataList.size() ;
	}

	@Override
	public View getView(int position, View noticeView, ViewGroup parent) {
		final Context context = parent.getContext();

		if (noticeView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			noticeView = inflater.inflate(R.layout.layout_notice_list_item, parent, false);
		}
        Notice notice = noticeDataList.get(position);

		TextView writeDateView = noticeView.findViewById(R.id.notice_write_date_view);
		writeDateView.setText(notice.notice_date.toString());

        TextView titleView = noticeView.findViewById(R.id.notice_title_view);
		titleView.setText(notice.notice_title);

		return noticeView;
	}

	@Override
	public long getItemId(int position) {
		return position ;
	}
	
	@Override
	public Object getItem(int position) {
		return noticeDataList.get(position) ;
	}

	public void addItem(Notice notice) {
		noticeDataList.add(notice);
	}

	public void clear() {
		noticeDataList.clear();
	}

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Context context = adapterView.getContext();
        Log.d(toString(), "onClick");

        Intent intent = new Intent(context, NoticeActivity.class);
        intent.putExtra(Notice.INTENT_KEY, noticeDataList.get(i));
        context.startActivity(intent);
    }
}
