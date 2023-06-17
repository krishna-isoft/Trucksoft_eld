package com.trucksoft.isoft.isoft_elog.driverchecklist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Dvir_view_model;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverChecklistView extends Activity implements OnClickListener {

	TextView tv_carrier, tv_carr_addrs, tv_date_time, tv_truck_no, tv_odometer,
			tv_trailer, tv_remark, tv_driver_name, tv_mechanics_name,
			tv_driver_date, tv_mechanic_date, tv_driver_addrs,
			tv_mechanics_addrs, tv_truck_item_bt, tv_trailer_item_bt,txt_trip,txtcdate;

	ImageView iv_driver_sign, iv_mechanic_sign;
CheckBox chkdefect,chkdefect1,chkdefect2;
	String id;

	ProgressDialog dialog;

	LinearLayout mLayout;

	ItemListViewBean mBean;
	ArrayList<String> item_subitems = new ArrayList<String>();
	ArrayList<String> truck_item_old=new ArrayList<>();
	ArrayList<String> selectid=new ArrayList<>();
	public static ArrayList<ItemListViewBean> mBeans_truck;
	public static ArrayList<ItemListViewBean> mBeans_trailer;

	public static ArrayList<String> item_status_truck;
	public static ArrayList<String> item_status_trailer;
	private ImageView imgphoto;

	private LinearLayout lindate,linaddress;
	private TextView txt_address,txt_date;
	private ArrayList<String> arrayimgold=new ArrayList<>();

	private LinearLayout lintks;
	private static Preference pref;
	private TextView txttl;
	private String strtripopt;
	private LinearLayout linconfirm;
	LinearLayout lytLayout;

	private TextView txtcarierid;
	Eld_api api;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.view_driver_checklist);

		Intent mIntent = getIntent();
		id = mIntent.getStringExtra("ID");


		findViewById();
		pref = Preference.getInstance(this);
		txtcarierid.setText(""+id);
		chkdefect.setClickable(false);
        chkdefect1.setClickable(false);
		chkdefect2.setClickable(false);
		String ccode=pref.getString(Constant.COMPANY_CODE).trim();
		if(ccode.contentEquals("wwe"))
		{
			String wmsg=pref.getString(Constant.WWE_MSG);

			if(wmsg !=null && wmsg.length()>0 && !wmsg.contentEquals("null"))
			{
				txttl.setText(wmsg);
			}
			lintks.setVisibility(View.VISIBLE);
		}else
		{
			lintks.setVisibility(View.GONE);
		}
		driverCheckListView(id);

		tv_truck_item_bt.setOnClickListener(this);
		tv_trailer_item_bt.setOnClickListener(this);

	}

	private void findViewById() {
		// TODO Auto-generated method stub
		txtcarierid=findViewById(R.id.txt_id);
		lintks=(LinearLayout)findViewById(R.id.tks) ;
		chkdefect=findViewById(R.id.chkadc);
        chkdefect1=findViewById(R.id.chkadc1);
		chkdefect2=findViewById(R.id.chkadc2);
		txt_trip=findViewById(R.id.txt_tripopt);
		lytLayout = (LinearLayout) findViewById(R.id.addimg);
		tv_carrier = (TextView) findViewById(R.id.driver_checklist_view_tv_carrier);
		txttl= (TextView) findViewById(R.id.addih);
		linconfirm=findViewById(R.id.lincdate);
		txtcdate=findViewById(R.id.driver_checklist_confirmtime);
		tv_carr_addrs = (TextView) findViewById(R.id.driver_checklist_view_tv_carr_addrs);
		tv_date_time = (TextView) findViewById(R.id.driver_checklist_view_tv_date_time);
		tv_truck_no = (TextView) findViewById(R.id.driver_checklist_view_tv_truck_no);
		tv_odometer = (TextView) findViewById(R.id.driver_checklist_view_tv_odometer);
		tv_trailer = (TextView) findViewById(R.id.driver_checklist_view_tv_trailer);
		tv_remark = (TextView) findViewById(R.id.driver_checklist_view_tv_remark);
		tv_driver_date = (TextView) findViewById(R.id.driver_checklist_view_tv_dateDriver);
		tv_mechanic_date = (TextView) findViewById(R.id.driver_checklist_view_tv_dateMechanic);
		tv_driver_name = (TextView) findViewById(R.id.driver_checklist_view_tv_driver_name);
		tv_mechanics_name = (TextView) findViewById(R.id.driver_checklist_view_tv_mechanic_name);
		tv_driver_addrs = (TextView) findViewById(R.id.driver_checklist_view_tv_addrsDriver);
		tv_mechanics_addrs = (TextView) findViewById(R.id.driver_checklist_view_tv_addrsMechanic);
		tv_truck_item_bt = (TextView) findViewById(R.id.driver_checklist_view_tv_truck_items);
		tv_trailer_item_bt = (TextView) findViewById(R.id.driver_checklist_view_tv_trailer_items);
		iv_driver_sign = (ImageView) findViewById(R.id.driver_checklist_view_iv_signDriver);
		iv_mechanic_sign = (ImageView) findViewById(R.id.driver_checklist_view_iv_signMechanic);
		//iv_tick1 = (ImageView) findViewById(R.id.driver_checklist_view_iv_tick1);
		//iv_tick2 = (ImageView) findViewById(R.id.driver_checklist_view_iv_tick2);
	//	iv_tick3 = (ImageView) findViewById(R.id.driver_checklist_view_iv_tick3);
		mLayout = (LinearLayout) findViewById(R.id.driver_checklist_view_ll_main);
		imgphoto= (ImageView) findViewById(R.id.imgs);
		
		linaddress = (LinearLayout) findViewById(R.id.linimgaddress);
		lindate = (LinearLayout) findViewById(R.id.linimgdate);
		txt_address=(TextView) findViewById(R.id.imaddress);
		txt_date=(TextView) findViewById(R.id.imdate);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.driver_checklist_view_iv_back:
			finish();
			break;

		case R.id.driver_checklist_view_tv_truck_items:

//			Intent mIntent = new Intent(DriverChecklistView.this,
//					ItemListActivity.class);
			Intent mIntent = new Intent(DriverChecklistView.this,
					ViewItemListActivity.class);
			mIntent.putExtra("tag", "truck");
			mIntent.putExtra("mBeans", mBeans_truck);
			startActivity(mIntent);
			break;

		case R.id.driver_checklist_view_tv_trailer_items:
			Intent mIntent2 = new Intent(DriverChecklistView.this,
					ViewItemListActivity.class);
			mIntent2.putExtra("tag", "trailer");
			mIntent2.putExtra("mBeans", mBeans_trailer);
			startActivity(mIntent2);
			break;

		}

	}
	private void driverCheckListView(String id2) {
		dialog = new ProgressDialog(this,
				AlertDialog.THEME_HOLO_LIGHT);

		if (OnlineCheck.isOnline(this)) {
			dialog.setMessage("Please wait...");
			dialog.setCancelable(false);
			dialog.show();
			api = ApiServiceGenerator.createService(Eld_api.class);


			Call<List<Dvir_view_model>> call = api.viewdriverchecklist(""+pref.getString(Constant.DRIVER_ID), ""+pref.getString(Constant.COMPANY_CODE),""+id2,"driver");
			call.enqueue(new Callback<List<Dvir_view_model>>() {
				@Override
				public void onResponse(Call<List<Dvir_view_model>> call, Response<List<Dvir_view_model>> response) {
					cancelprogresssdialog();
					if (response.isSuccessful()) {
						List<Dvir_view_model> result = response.body();
						if (result.size() > 0) {
							array_method(result);
						} else {

						}
						//should call the custom method adapter.notifyDataChanged here to get the correct loading status
					}

				}

				@Override
				public void onFailure(Call<List<Dvir_view_model>> call, Throwable t) {
					// Log.e("ddd"," Load More Response Error "+t.getMessage());
					cancelprogresssdialog();
				}
			});
		}
	}
//	private void driverCheckListView(String id2) {
//
//		dialog = new ProgressDialog(DriverChecklistView.this,
//				AlertDialog.THEME_HOLO_LIGHT);
//
//		if (OnlineCheck.isOnline(this)) {
//			dialog.setMessage("Please wait...");
//			dialog.setCancelable(false);
//			dialog.show();
//			WebServices.driver_checklist_view(this,id2,
//					new JsonHttpResponseHandler() {
//						@Override
//						public void onFailure(int statusCode, Header[] headers,
//								String responseString, Throwable throwable) {
//							// TODO Auto-generated method stub
//							super.onFailure(statusCode, headers,
//									responseString, throwable);
//
//							dialog.dismiss();
//							// CommonMethod.showMsg(getActivity(), ""+
//							// responseString);
//						}
//
//						@Override
//						public void onFailure(int statusCode, Header[] headers,
//								Throwable throwable, JSONArray errorResponse) {
//							// TODO Auto-generated method stub
//							super.onFailure(statusCode, headers, throwable,
//									errorResponse);
//
//							dialog.dismiss();
//							// CommonMethod.showMsg(getActivity(), ""+
//							// errorResponse);
//						}
//
//						@Override
//						public void onFailure(int statusCode, Header[] headers,
//								Throwable throwable, JSONObject errorResponse) {
//							// TODO Auto-generated method stub
//							super.onFailure(statusCode, headers, throwable,
//									errorResponse);
//
//							dialog.dismiss();
//
//						}
//
//						@Override
//						public void onSuccess(int statusCode, Header[] headers,
//								JSONArray response) {
//							// TODO Auto-generated method stub
//							super.onSuccess(statusCode, headers, response);
//							dialog.dismiss();
//							if (response != null) {
//								array_method(response);
//							}
//						}
//
//						@Override
//						public void onSuccess(int statusCode, Header[] headers,
//								JSONObject response) {
//							// TODO Auto-generated method stub
//							super.onSuccess(statusCode, headers, response);
//							dialog.dismiss();
//							errorMessage(response);
//						}
//
//						@Override
//						public void onSuccess(int statusCode, Header[] headers,
//								String responseString) {
//							// TODO Auto-generated method stub
//							super.onSuccess(statusCode, headers, responseString);
//							dialog.dismiss();
//						}
//
//					});
//		}
//
//	}

	protected void array_method(List<Dvir_view_model> response) {
		// TODO Auto-generated method stub
//Log.e("response", ""+response.toString());
		try {
			mLayout.setVisibility(View.VISIBLE);
			mBeans_truck = new ArrayList<ItemListViewBean>();
			mBeans_trailer = new ArrayList<ItemListViewBean>();
			item_status_truck = new ArrayList<String>();
			item_status_trailer = new ArrayList<String>();
			if (response.size() > 0) {
				for (int i = 0; i < response.size(); i++) {
					Dvir_view_model mJsonObject = new Dvir_view_model();
					mJsonObject=response.get(i);
					String status = mJsonObject.status;
					if (status.equalsIgnoreCase("1")) {
						//String id = mJsonObject.getString("id");
						String carrier = mJsonObject.carrier;
						if (!(carrier.equalsIgnoreCase("null")))
							tv_carrier.setText(carrier);
						String carrier_address = mJsonObject
								.carrier_address;
						tv_carr_addrs.setText(carrier_address);
						String savedate = mJsonObject.savedate;

						//String Date_Time = mJsonObject.Date_Time;

						if(savedate !=null && savedate.length()>0 && !savedate.contentEquals("null")) {
							// if(formattedDate.contains(" "))
							// {
							StringTokenizer stt = new StringTokenizer(savedate, " ");
							String myDatez = "" + stt.nextToken();
							if(stt.hasMoreTokens())
							{
								String stime=stt.nextToken();

								try {
									final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
									final Date dateObj = sdf.parse(stime);
									String starttime=""+new SimpleDateFormat("h:mm a").format(dateObj);


										tv_date_time.setText(myDatez+" "+starttime);


								} catch (final ParseException e) {
									e.printStackTrace();
								}
							}
						}
//String editmode=mJsonObject.getString("editmode");
//						if(editmode.contentEquals("0")) {
//							linconfirm.setVisibility(View.VISIBLE);
//
//							if (Date_Time != null && Date_Time.length() > 0 && !Date_Time.contentEquals("null")) {
//								// if(formattedDate.contains(" "))
//								// {
//								StringTokenizer stt = new StringTokenizer(Date_Time, " ");
//								String myDatez = "" + stt.nextToken();
//								if (stt.hasMoreTokens()) {
//									String stime = stt.nextToken();
//
//									try {
//										final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
//										final Date dateObj = sdf.parse(stime);
//										String starttime = "" + new SimpleDateFormat("h:mm a").format(dateObj);
//
//
//										txtcdate.setText(myDatez + " " + starttime);
//
//
//									} catch (final ParseException e) {
//										e.printStackTrace();
//									}
//								}
//							}
//						}else{
//							linconfirm.setVisibility(View.GONE);
//						}

						//tv_date_time.setText(Date_Time);
						String Truck_No = mJsonObject.Truck_No;
						if (!(Truck_No.equalsIgnoreCase("null")))
							tv_truck_no.setText(Truck_No);
						String Odometer_Reading = mJsonObject
								.Odometer_Reading;
						tv_odometer.setText(Odometer_Reading);
						String Trailer = mJsonObject.Trailer;
						if (!(Trailer.equalsIgnoreCase("null")))
							tv_trailer.setText(Trailer);
						String Remark = mJsonObject.Remark;
						tv_remark.setText(Remark);
						String CONDITION_OF_THE = mJsonObject
								.CONDITION_OF_THE;
						if (CONDITION_OF_THE.equalsIgnoreCase("1")) {
						//	iv_tick1.setVisibility(View.VISIBLE);
							chkdefect2.setChecked(true);
							chkdefect2.setClickable(false);
						}
						String Driver_Name = mJsonObject
								.Driver_Name;
						tv_driver_name.setText(Driver_Name);
						String Driver_Signature = mJsonObject
								.Driver_Signature;
						if (!(Driver_Signature.equalsIgnoreCase("null"))) {
							if (Driver_Signature.length() > 0) {
								iv_driver_sign.setVisibility(View.VISIBLE);
								Picasso.with(getApplicationContext())
										.load(Driver_Signature)
										.into(iv_driver_sign);
							}
						}
						String Driver_date = mJsonObject
								.Driver_date;
						if (!(Driver_date.equalsIgnoreCase("null"))) {
							if (Driver_date.length() > 0) {
								tv_driver_date.setVisibility(View.VISIBLE);
								tv_driver_date.setText(Driver_date);
							}
						}
						String Driver_address = mJsonObject
								.Driver_address;
						if (!(Driver_address.equalsIgnoreCase("null"))
								&& !(Driver_address.equalsIgnoreCase("false"))) {
							if (Driver_address.length() > 0) {
								tv_driver_addrs.setText(Driver_address);
							}
						}
						String ABOVE_DEFECTS_CORRECTED = mJsonObject
								.ABOVE_DEFECTS_CORRECTED;
						if (ABOVE_DEFECTS_CORRECTED.equalsIgnoreCase("1")) {
							//iv_tick2.setVisibility(View.VISIBLE);
							chkdefect.setChecked(true);
							//chkdefect.setEnabled(false);
                            chkdefect.setClickable(false);
						}else{
							//chkdefect.setEnabled(false);
                            chkdefect.setClickable(false);
						}
						String ABOVE_DEFECTS_NEED_NOT_BE_CORRECTED = mJsonObject
								.ABOVE_DEFECTS_NEED_NOT_BE_CORRECTED;
						if (ABOVE_DEFECTS_NEED_NOT_BE_CORRECTED
								.equalsIgnoreCase("1")) {
						//	iv_tick3.setVisibility(View.VISIBLE);
							chkdefect1.setChecked(true);
							chkdefect1.setClickable(false);
						}else{
                            chkdefect1.setClickable(false);
                        }
						String Mechanics_Name = mJsonObject
								.Mechanics_Name;
						tv_mechanics_name.setText(Mechanics_Name);
						String Mechanics_Signature = mJsonObject
								.Mechanics_Signature;
						if (!(Mechanics_Signature.equalsIgnoreCase("null"))) {
							if (Mechanics_Signature.length() > 0) {
								iv_mechanic_sign.setVisibility(View.VISIBLE);
								Picasso.with(getApplicationContext())
										.load(Mechanics_Signature)
										.into(iv_mechanic_sign);
								chkdefect.setChecked(true);
								//chkdefect.setEnabled(false);
								chkdefect.setClickable(false);
							}
						}
						String Mechanics_date = mJsonObject
								.Mechanics_date;
						if (!(Mechanics_date.equalsIgnoreCase("null"))) {
							if (Mechanics_date.length() > 0) {
								tv_mechanic_date.setVisibility(View.VISIBLE);
								tv_mechanic_date.setText(Mechanics_date);
							}
						}
						String Mechanics_address = mJsonObject
								.Mechanics_address;
						if (!(Mechanics_address.equalsIgnoreCase("null"))
								&& !(Mechanics_address
										.equalsIgnoreCase("false"))) {
							if (Mechanics_address.length() > 0) {
								tv_mechanics_addrs.setText(Mechanics_address);
							}
						}


						if(mJsonObject.tripopt!=null) {
							String trip_opt = mJsonObject
									.tripopt;
							strtripopt=trip_opt;
							txt_trip.setText(""+strtripopt);
						}
						String photos = mJsonObject
								.image_photo;
						String photoaddress=mJsonObject
								.imgaddress;
						
						String imgdate=mJsonObject
								.imgdate;
						
					if(photos.length()>0)
					{imgphoto.setVisibility(View.GONE);
						//imgphoto.setVisibility(View.VISIBLE);
						Picasso.with(getApplicationContext())
						.load(photos)
						.into(imgphoto);
					}else
					{
						imgphoto.setVisibility(View.GONE);
					}
						
						
					if (!(imgdate.equalsIgnoreCase("null"))
							&& !(imgdate
									.equalsIgnoreCase("false"))) {
						if (imgdate.length() > 0) {
							lindate.setVisibility(View.VISIBLE);
							txt_date.setText(imgdate);
						}
					}
					
					
					if (!(photoaddress.equalsIgnoreCase("null"))
							&& !(photoaddress
									.equalsIgnoreCase("false"))) {
						if (photoaddress.length() > 0) {
							linaddress.setVisibility(View.VISIBLE);
							txt_address.setText(photoaddress);
						}
					}




















//						JSONArray mArrayTruckItems = mJsonObject
//								.truck_item_details;
//						//Log.e("mArrayTruckItems","@"+mArrayTruckItems.toString());
//						if (mArrayTruckItems.length() > 0
//								|| mArrayTruckItems != null) {
//
//							for (int j = 0; j < mArrayTruckItems.length(); j++) {
//
//								mBean = new ItemListViewBean();
//
//								JSONObject mObjectTruck = mArrayTruckItems
//										.getJSONObject(j);
//
//								String truck_item_status = mObjectTruck
//										.getString("truck_item_status");
//								item_status_truck.add(truck_item_status);
//
//								String truck_id = mObjectTruck
//										.getString("truck_id");
//
//								String truck_item_name = mObjectTruck
//										.getString("truck_item_name");
//								//Log.e("truck_item_name","@"+truck_item_name);
//								mBean.setItem_name(truck_item_name);
//								mBean.setItem_name_other("");
//								mBean.setGoodstatus(""+truck_item_status);
//
//								String stimage=mObjectTruck.getString("tr_img");
//								//Log.e("stimage","@"+stimage);
//if(stimage !=null && stimage.length()>0) {
//	if (stimage.contains(".png")) {
//		mBean.setImg_url(stimage);
//	}else{
//		mBean.setImg_url("");
//	}
//}else
//{
//	mBean.setImg_url("");
//}
//
//								if (truck_id.equalsIgnoreCase("21")
//										|| truck_id.equalsIgnoreCase("7")
//										|| truck_id.equalsIgnoreCase("28")) {
//
//									JSONArray mArraySubitems = mObjectTruck
//											.getJSONArray("truck_sub_item_name");
//
//									if (mArraySubitems.length() > 0) {
//
//										for (int k = 0; k < mArraySubitems
//												.length(); k++) {
//
//											JSONObject mObjectSubitemName = mArraySubitems
//													.getJSONObject(k);
//
//											String name = mObjectSubitemName
//													.getString("name");
//
//											item_subitems.add(name);
//										}
//
//										mBean.setSubitem(item_subitems);
//									}
//
//								} else {
//									String truck_sub_item_name = mObjectTruck
//											.getString("truck_sub_item_name");
//								}
//
//								mBeans_truck.add(mBean);
//							}
//						}
//						String truck_other_status = mJsonObject
//								.truck_other_status;
//						item_status_truck.add(truck_other_status);
//
//						String truck_other = mJsonObject
//								.truck_other;
//
//						mBean = new ItemListViewBean();
//						mBean.setItem_name("Other");
//						mBean.setItem_name_other(truck_other);
//						mBeans_truck.add(mBean);
//
//						JSONArray mArrayTrailerItems = mJsonObject
//								.getJSONArray("trailer_item_details");
//
//						if (mArrayTrailerItems.length() > 0
//								|| mArrayTrailerItems != null) {
//
//							for (int j = 0; j < mArrayTrailerItems.length(); j++) {
//
//								mBean = new ItemListViewBean();
//
//								JSONObject mObjectTrailer = mArrayTrailerItems
//										.getJSONObject(j);
//
//								String trailer_item_status = mObjectTrailer
//										.getString("trailer_item_status");
//							//	Log.e("trailer_item_status",""+trailer_item_status);
//								item_status_trailer.add(trailer_item_status);
//
//								String trailer_id = mObjectTrailer
//										.getString("trailer_id");
//
//								String trailer_item_name = mObjectTrailer
//										.getString("trailer_item_name");
//								mBean.setItem_name(trailer_item_name);
//								mBean.setItem_name_other("");
//								mBean.setGoodstatus(""+trailer_item_status);
//								String stimage=mObjectTrailer.getString("trailerimg");
//								//Log.e("stimage","@"+stimage);
//								if(stimage !=null && stimage.length()>0) {
//									if (stimage.contains(".png")) {
//										mBean.setImg_url(stimage);
//									} else {
//										mBean.setImg_url("");
//									}
//								}
//								mBeans_trailer.add(mBean);
//
//							}
//						}
//						//multiple image
//						JSONArray multi_img = mJsonObject
//								.getJSONArray("truck_img");
//						//Log.e("ccxccx","@@@"+multi_img.toString());
//						arrayimgold=new ArrayList<>();
//						if (multi_img != null && multi_img.length()>0 ) {
//							if (multi_img.length() > 0) {
//
//								for (int j = 0; j < multi_img.length(); j++) {
//									JSONObject mResultJsonimg = multi_img
//											.getJSONObject(j);
//									String imgid = mResultJsonimg
//											.getString("img_id");
//									String imgurl = mResultJsonimg
//											.getString("img_url");
//									arrayimgold.add(imgid+">>"+imgurl);
//								}
//
//								setimageold();
//							}
//						}





						String trailer_other_status = mJsonObject
								.trailer_other_status;
						item_status_trailer.add(trailer_other_status);
						String trailer_other = mJsonObject
								.trailer_other;

						mBean = new ItemListViewBean();
						mBean.setItem_name("Other");
						mBean.setItem_name_other(trailer_other);
						mBeans_trailer.add(mBean);

					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void errorMessage(JSONObject response) {
		try {

			if (response != null) {

				String status = response.getString("status");

				if (status.equalsIgnoreCase("0")) {
					String message = response.getString("message");
					Toast.makeText(getApplicationContext(), message,
							Toast.LENGTH_SHORT).show();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	private void setimageold()
	{
		//lytLayout.removeAllViews();
		for(int k=0;k<arrayimgold.size();k++)
		{
			String urlval=arrayimgold.get(k);
			StringTokenizer skr=new StringTokenizer(urlval,">>");
			String simgid=skr.nextToken();
			String url=skr.nextToken();
			LayoutInflater layoutInflater =
					(LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View addView = layoutInflater.inflate(R.layout.add_views_img, null);
			// TextView textOut = (TextView)addView.findViewById(R.id.textout);
			// textOut.setText(textIn.getText().toString());
			ImageView imgkt = (ImageView) addView.findViewById(R.id.dashkt);
			final ImageView imgremove = (ImageView) addView.findViewById(R.id.badge1);
			imgremove.setVisibility(View.GONE);
			TextView txt_url = (TextView) addView.findViewById(R.id.txturl);
			final TextView txt_demo = (TextView) addView.findViewById(R.id.txtdemo);
			TextView txt_rid = (TextView) addView.findViewById(R.id.txtrid);
			//Log.e("txt_rid", "/"+lastid);
			txt_url.setText(""+urlval);
			txt_rid.setText(simgid);
			txt_demo.setText(""+url);
			Picasso.with(this)
					.load(url + "?.time();")
					.memoryPolicy(MemoryPolicy.NO_CACHE)
					.networkPolicy(NetworkPolicy.NO_CACHE)
					.placeholder(R.drawable.prog_animation)
					.error(R.drawable.whitekt)
					.into(imgkt);
			imgkt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intenttt = new Intent(DriverChecklistView.this, Image_view_bol.class);
					intenttt.putExtra("img_url", "" + txt_demo.getText().toString());
					startActivity(intenttt);
				}
			});


			lytLayout.addView(addView);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	private void cancelprogresssdialog() {

		try {
			if ((dialog != null) && dialog.isShowing()) {
				dialog.dismiss();
			}
		} catch (final IllegalArgumentException e) {
			// Log.e("err1.........",""+e.toString());
			// Handle or log or ignore
		} catch (final Exception e) {
			// Log.e("err2........",""+e.toString());
			// Handle or log or ignore
		} finally {
			dialog = null;
		}
	}
}
