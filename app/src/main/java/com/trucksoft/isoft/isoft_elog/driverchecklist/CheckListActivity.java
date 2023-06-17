package com.trucksoft.isoft.isoft_elog.driverchecklist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckListActivity extends Activity implements OnClickListener {
	ListView mListView;
	TextView mTextView, mTextView2;
	ProgressDialog dialog;
	String tag = "";
	ArrayList<String> items = new ArrayList<String>();
	ArrayList<String> item_ids = new ArrayList<String>();
	ArrayList<String> sub_items;
	ArrayList<String> checked_status_array ;

	// HashMap<String, ArrayList<String>> mHashMap = new HashMap<String,
	// ArrayList<String>>();
	// CustomAdapterItemsList mAdapter;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.truck_items_checklist);

		findViewById();
		checked_status_array = new ArrayList<String>();

		Intent mIntent = getIntent();
		tag = mIntent.getStringExtra("tag");
		if (tag.equalsIgnoreCase("truck")) {
			mTextView2.setText("Truck Items");
			checked_status_array = mIntent
					.getStringArrayListExtra("truckfld_status");
		} else if (tag.equalsIgnoreCase("trailer")) {
			mTextView2.setText("Trailer Items");
			checked_status_array = mIntent
					.getStringArrayListExtra("trailfld_status");
		}

		mTextView.setOnClickListener(this);

		//items_list();

	}

	private void findViewById() {
		// TODO Auto-generated method stub
		mListView = (ListView) findViewById(R.id.checklist_lv);
		mTextView = (TextView) findViewById(R.id.checklist_tv_done);
		mTextView2 = (TextView) findViewById(R.id.checklist_tv_title);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.checklist_iv_back:
			finish();
			break;

		case R.id.checklist_tv_done:

			SparseBooleanArray checked = mListView.getCheckedItemPositions();
			ArrayList<String> selectedItems = new ArrayList<String>();
			ArrayList<String> selectedItemsIds = new ArrayList<String>();
			for (int i = 0; i < checked.size(); i++) {
				// Item position in adapter
				int position = checked.keyAt(i);
				// Add sport if it is checked i.e.) == TRUE!
				if (checked.valueAt(i)) {
					selectedItems.add(adapter.getItem(position));
					selectedItemsIds.add(item_ids.get(position));
				}
			}
			
			checked_status_array = new ArrayList<String>();
			for(int i=0;i<item_ids.size();i++)
			{
				for(int j=0;j<selectedItemsIds.size();j++)
				{
					if(item_ids.get(i).equalsIgnoreCase(selectedItemsIds.get(j)))
					{
						checked_status_array.add(i,"1");
						break;
					}else
					{
						checked_status_array.add(i,"0");
					}
				}
			}
			Bundle b = new Bundle();
			b.putStringArrayList("sel_item_ids", selectedItemsIds);
			b.putString("tag", tag);
			b.putStringArrayList("checked_status_array", checked_status_array);
			Intent intent = new Intent();
			intent.putExtras(b);
			setResult(RESULT_OK, intent);
			finish();

			/*
			 * String[] outputStrArr = new String[selectedItemsIds.size()];
			 * 
			 * for (int i = 0; i < selectedItemsIds.size(); i++) {
			 * outputStrArr[i] = selectedItemsIds.get(i);
			 * Toast.makeText(CheckListActivity.this, "" + outputStrArr[i],
			 * Toast.LENGTH_SHORT).show(); }
			 */
			break;

		}

	}

//	private void items_list() {
//
//		dialog = new ProgressDialog(CheckListActivity.this,
//				AlertDialog.THEME_HOLO_LIGHT);
//
//		if(OnlineCheck.isOnline(this))
//		{
//			dialog.setMessage("Please wait...");
//			dialog.setCancelable(false);
//			dialog.show();
//			WebServices.itemsList(this,tag, new JsonHttpResponseHandler() {
//				@Override
//				public void onFailure(int statusCode, Header[] headers,
//						String responseString, Throwable throwable) {
//					// TODO Auto-generated method stub
//					super.onFailure(statusCode, headers, responseString,
//							throwable);
//
//					dialog.dismiss();
//				}
//
//				@Override
//				public void onFailure(int statusCode, Header[] headers,
//						Throwable throwable, JSONArray errorResponse) {
//					// TODO Auto-generated method stub
//					super.onFailure(statusCode, headers, throwable,
//							errorResponse);
//
//					dialog.dismiss();
//				}
//
//				@Override
//				public void onFailure(int statusCode, Header[] headers,
//						Throwable throwable, JSONObject errorResponse) {
//					// TODO Auto-generated method stub
//					super.onFailure(statusCode, headers, throwable,
//							errorResponse);
//
//					dialog.dismiss();
//
//				}
//
//				@Override
//				public void onSuccess(int statusCode, Header[] headers,
//						JSONArray response) {
//					// TODO Auto-generated method stub
//					super.onSuccess(statusCode, headers, response);
//					dialog.dismiss();
//					//Log.e("truck item response", ""+response.toString());
//					if (response != null) {
//						if (tag.equalsIgnoreCase("truck")) {
//							truck_items(response);
//						} else if (tag.equalsIgnoreCase("trailer")) {
//							trailer_items(response);
//						}
//
//					}
//				}
//
//				@Override
//				public void onSuccess(int statusCode, Header[] headers,
//						JSONObject response) {
//					// TODO Auto-generated method stub
//					super.onSuccess(statusCode, headers, response);
//					dialog.dismiss();
//					//Log.e("truck item response111", ""+response.toString());
//					if (response != null) {
//
//					}
//
//				}
//
//				@Override
//				public void onSuccess(int statusCode, Header[] headers,
//						String responseString) {
//					// TODO Auto-generated method stub
//					super.onSuccess(statusCode, headers, responseString);
//					dialog.dismiss();
//				}
//
//			});
//		}
//
//
//
//	}

	protected void trailer_items(JSONArray response) {
		// TODO Auto-generated method stub
		try {
			if (response.length() > 0) {
				for (int i = 0; i < response.length(); i++) {
					JSONObject mJsonObject = response.getJSONObject(i);

					String status = mJsonObject.getString("status");
					if (status.equalsIgnoreCase("1")) {
						String id = mJsonObject.getString("id");
						item_ids.add(id);
						String item_name = mJsonObject.getString("item_name");
						items.add(item_name);
					}
				}
				items.add("other");
				if (checked_status_array.size() < 1) {
					for (int i = 0; i < items.size(); i++) {
						checked_status_array.add("0");
					}
				}

				item_ids.add("other_id");
				if (items != null) {
					adapter = new ArrayAdapter<String>(this,
							R.layout.truck_list_item,
							items);
					mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
					mListView.setAdapter(adapter);
					for (int i = 0; i < checked_status_array.size(); i++) {
						if (checked_status_array.get(i).equalsIgnoreCase("1")) {
							mListView.setItemChecked(i, true);
						}
					}
				}
			}

			/*
			 * mAdapter = new CustomAdapterItemsList(CheckListActivity.this,
			 * "trailer",mTextView, item_ids, items);
			 * mListView.setAdapter(mAdapter);
			 */

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	protected void truck_items(JSONArray response) {
		// TODO Auto-generated method stub
		try {
			if (response.length() > 0) {
				for (int i = 0; i < response.length(); i++) {
					JSONObject mJsonObject = response.getJSONObject(i);

					String status = mJsonObject.getString("status");
					if (status.equalsIgnoreCase("1")) {
						String id = mJsonObject.getString("id");
						item_ids.add(id);
						String item_name = mJsonObject.getString("item_name");
						items.add(item_name);
						/*
						 * String sub_item_name = mJsonObject
						 * .getString("sub_item_name"); if
						 * (!(sub_item_name.equalsIgnoreCase("null"))) {
						 * JSONArray mJsonArray = mJsonObject
						 * .getJSONArray("sub_item_name"); if
						 * (mJsonArray.length() > 0) { sub_items = new
						 * ArrayList<String>(); for (int j = 0; j <
						 * mJsonArray.length(); j++) { JSONObject nameJsonObject
						 * = mJsonArray .getJSONObject(j); String name =
						 * nameJsonObject .getString("name");
						 * sub_items.add(name); mHashMap.put(item_name,
						 * sub_items); }
						 * 
						 * } } else { sub_items = new ArrayList<String>();
						 * sub_items.add("null"); mHashMap.put(item_name,
						 * sub_items); }
						 */

					}
				}
				items.add("other");
				if (checked_status_array.size() < 1) {
					for (int i = 0; i < items.size(); i++) {
						checked_status_array.add("0");
					}
				}
				item_ids.add("other_id");
				if (items != null) {
					adapter = new ArrayAdapter<String>(this,
							R.layout.truck_list_item,items);
					mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
					mListView.setAdapter(adapter);
					for (int i = 0; i < checked_status_array.size(); i++) {
						if (checked_status_array.get(i).equalsIgnoreCase("1")) {
							mListView.setItemChecked(i, true);
						}
					}

				}

			}

			

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	

}
