package com.gj.gaojiaohui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gaojiaohui.adapter.SpecialLineAdapter;
import com.smartdot.wenbo.huiyi.R;

public class SpecialLineActivity extends GaoJiaoHuiBaseActivity implements OnClickListener {

	private String name;
	private ListView special_line_listview;
	private Context mContext;
	private SpecialLineAdapter mAdapter;
	private ArrayList<String> mList = new ArrayList<>();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_special_line);

		mContext = this;
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		mList.addAll((ArrayList<String>) bundle.getSerializable("list"));
		initView();
	}

	private void initView() {

		special_line_listview = (ListView) findViewById(R.id.special_line_listview);
		// SpecialLineAdapter adapter=new SpecialLineAdapter(mContext,list);
		mAdapter = new SpecialLineAdapter(mContext, mList, R.layout.line_item);
		special_line_listview.setAdapter(mAdapter);
		TextView title = (TextView) findViewById(R.id.custom_title_tv);
		title.setText(name);
		ImageView back = (ImageView) findViewById(R.id.custom_back_img);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_back_img:
			finish();
			break;

		default:
			break;
		}
	}
}
