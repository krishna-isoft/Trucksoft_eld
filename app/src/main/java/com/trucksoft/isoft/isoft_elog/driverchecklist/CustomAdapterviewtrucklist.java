package com.trucksoft.isoft.isoft_elog.driverchecklist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trucksoft.isoft.isoft_elog.R;

import java.util.ArrayList;

@SuppressLint("InflateParams")
public class CustomAdapterviewtrucklist extends BaseAdapter {

	LayoutInflater inflater;
	Context context;
	ArrayList<String> status_array = new ArrayList<String>();
	ArrayList<ItemListViewBean> mBeans = new ArrayList<ItemListViewBean>();

	public CustomAdapterviewtrucklist(Context context,
                                      ArrayList<String> status_array, ArrayList<ItemListViewBean> mBeans) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.status_array.addAll(status_array);
		this.mBeans.addAll(mBeans);

		inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return status_array.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class Holder {

		TextView tv_name;
		TextView tv_other_value;
		ListView lv_sub_names;
		ImageView imgtr;
		TextView txturl;
		CheckBox chselect;
		CheckBox chunselect;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

//		if (convertView == null) {
//			convertView = inflater.inflate(R.layout.trucklist_view_layout_new, null);
//		}
//
//		setAttributes(position, convertView);
//
//
//		return convertView;
		View row = null;

		if (row == null) {
			row  = inflater.inflate(R.layout.trucklist_view_layout_new, null);
			setAttributes(position, row);
		}else
		{
			row = (View) convertView;
		}
		return row;
	}

	

	public void setAttributes(final int position, View convertView) {
		final Holder holder = new Holder();


		holder.imgtr=convertView.findViewById(R.id.img_view);
		holder.txturl=convertView.findViewById(R.id.txt_url);
		holder.chselect=convertView.findViewById(R.id.ch_select);
		holder.chunselect=convertView.findViewById(R.id.ch_unselect);
holder.chselect.setClickable(false);
		if (status_array.get(position).equalsIgnoreCase("1")) {
			holder.chselect.setChecked(true);
		} else {
			holder.chselect.setChecked(false);
		}
		holder.chselect.setClickable(false);

		//Log.e("stddddd",""+mBeans.get(position).getGoodstatus());
		if(mBeans.get(position).getGoodstatus() !=null && mBeans.get(position).getGoodstatus().length()>0)
		{
			if(mBeans.get(position).getGoodstatus().contentEquals("2"))
			{
				holder.chunselect.setChecked(true);
			}
		}
		holder.chunselect.setClickable(false);
		if(mBeans.get(position).getImg_url() !=null && mBeans.get(position).getImg_url().length()>0&& mBeans.get(position).getImg_url().contains(".png"))
		{	holder.imgtr.setVisibility(View.VISIBLE);
			Picasso.with(context).load(mBeans.get(position).getImg_url()).into(holder.imgtr);
			holder.txturl.setText(""+mBeans.get(position).getImg_url());
		}else
		{
			holder.imgtr.setVisibility(View.INVISIBLE);
		}
holder.imgtr.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View v) {
		Intent intenttt = new Intent(context, Image_view_bol.class);
		intenttt.putExtra("img_url", "" + holder.txturl.getText().toString());
		context.startActivity(intenttt);
	}
});
		holder.tv_name = (TextView) convertView
				.findViewById(R.id.item_list_view_tv_item_name);

		holder.tv_name.setText(mBeans.get(position).getItem_name());

		holder.tv_other_value = (TextView) convertView
				.findViewById(R.id.item_list_view_tv_item_other_value);
		
		holder.tv_other_value.setText(mBeans.get(position).getItem_name_other());

		holder.lv_sub_names = (ListView) convertView
				.findViewById(R.id.item_list_view_lv_sub_items);

		/*
		 * if(mBeans.get(position).getSubitem()!=null) { String[] mStringArray =
		 * new String[mBeans.get(position).getSubitem().size()]; mStringArray =
		 * mBeans.get(position).getSubitem().toArray(mStringArray);
		 * ArrayAdapter<String> adapter = new ArrayAdapter<String>( context,
		 * R.layout.textview_layout, mStringArray);
		 * Utility.setListViewHeightBasedOnChildren(holder.lv_sub_names);
		 * holder.lv_sub_names.setAdapter(adapter); }
		 */

	}
}
