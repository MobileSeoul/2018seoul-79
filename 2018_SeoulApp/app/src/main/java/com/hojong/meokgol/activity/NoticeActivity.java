package com.hojong.meokgol.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hojong.meokgol.R;
import com.hojong.meokgol.data_model.Notice;

public class NoticeActivity extends AppCompatActivity implements View.OnClickListener {
    Notice notice;
    TextView dateView;
    TextView titleView;
    TextView contentView;
    View backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        notice = (Notice) getIntent().getSerializableExtra(Notice.INTENT_KEY);

        dateView = findViewById(R.id.notice_write_date_view);
        dateView.setText(notice.notice_date.toString());

        titleView = findViewById(R.id.notice_title_view);
        titleView.setText(notice.notice_title);

        contentView = findViewById(R.id.notice_content_view);
        contentView.setText(notice.notice_content);

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        // back
        Log.d(toString(), "onClick");
        finish();
    }
}
