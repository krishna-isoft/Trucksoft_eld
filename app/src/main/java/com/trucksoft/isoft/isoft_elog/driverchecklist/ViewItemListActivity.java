package com.trucksoft.isoft.isoft_elog.driverchecklist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.trucksoft.isoft.isoft_elog.R;

import java.util.ArrayList;

public class ViewItemListActivity extends Activity implements OnClickListener {

	ArrayList<String> status_array;
	ArrayList<ItemListViewBean> mBeans;
	String tag;
	TextView mTextView;
	ListView mListView;
	CustomAdapterviewtrucklist mAdapter;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.item_list_viewdd);

		findViewById();

		status_array = new ArrayList<String>();
		mBeans = new ArrayList<ItemListViewBean>();

		Intent mIntent = getIntent();
		tag = mIntent.getStringExtra("tag");

		if (tag.equalsIgnoreCase("truck")) {

			mTextView.setText("Truck Items");
			status_array.addAll(DriverChecklistView.item_status_truck);
			// mBeans.addAll(DriverChecklistView.mBeans_truck);
			mBeans = (ArrayList<ItemListViewBean>) mIntent
					.getSerializableExtra("mBeans");

		} else if (tag.equalsIgnoreCase("trailer")) {

			mTextView.setText("Trailer Items");
			status_array.addAll(DriverChecklistView.item_status_trailer);
			// mBeans.addAll(DriverChecklistView.mBeans_trailer);
			mBeans = (ArrayList<ItemListViewBean>) mIntent
					.getSerializableExtra("mBeans");

		}

		mAdapter = new CustomAdapterviewtrucklist(ViewItemListActivity.this,
				status_array, mBeans);
		mListView.setAdapter(mAdapter);

	}

	private void findViewById() {
		// TODO Auto-generated method stub

		mTextView = (TextView) findViewById(R.id.itemlist_view_tv_title);
		mListView = (ListView) findViewById(R.id.itemlist_view_lv);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.itemlist_view_iv_back:

			finish();

			break;

		}

	}

}
