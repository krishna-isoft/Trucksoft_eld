package com.trucksoft.isoft.isoft_elog.driverchecklist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class DriverCheckInsideActivity extends Activity {

	Button mButtonEdit, mButtonEdit_inactive;
	TextView title, id, carrier, trac_truck_no, odometer_reading, date,txtconfirm;
	String s_id, s_carrier, s_tractor, s_truck_no, s_odometer_reading, s_date,confdate,
			s_trashmode, s_editmode;
	ProgressDialog dialog;
	private TextView helpline;
	String str_tripopt;
	LinearLayout linedit;
	Context context;
	Preference pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_check_inside_screen);

		findViewById();
		context=this;
		Intent mIntent = getIntent();
		pref=Preference.getInstance(context);
		s_id = mIntent.getStringExtra("id");
		s_carrier = mIntent.getStringExtra("carrier");
		s_tractor = mIntent.getStringExtra("tractor");
		s_truck_no = mIntent.getStringExtra("truck_no");
		s_odometer_reading = mIntent.getStringExtra("odometer_reading");
		s_date = mIntent.getStringExtra("date");
		s_trashmode = mIntent.getStringExtra("trashmode");
		s_editmode = mIntent.getStringExtra("editmode");
		confdate= mIntent.getStringExtra("savedate");
		str_tripopt=mIntent.getStringExtra(Constant.TRIP_OPTION);

//		if (s_trashmode.equalsIgnoreCase("1")
//				|| s_editmode.equalsIgnoreCase("0")) {
//			mButtonEdit.setBackgroundResource(R.drawable.activebuttonbase);
//			mButtonEdit.setClickable(false);
//			if(s_editmode.contentEquals("0")) {
//				Toast.makeText(context, "Submitted checklist not allow to edit", Toast.LENGTH_SHORT).show();
//			}
//
//			if(s_trashmode.contentEquals("1"))
//			{
//				Toast.makeText(context, "Voided checklist not allow to edit", Toast.LENGTH_SHORT).show();
//			}
//		} else if (s_trashmode.equalsIgnoreCase("0")
//				|| s_editmode.equalsIgnoreCase("0")) {
//			mButtonEdit.setBackgroundResource(R.drawable.normalbuttonbase);
//			mButtonEdit.setClickable(true);
//		}
		title.setText("#" + s_id);
		id.setText(s_id);
		carrier.setText(s_carrier);
		if (s_tractor !=null && s_tractor.length() > 0 || s_truck_no !=null && s_truck_no.length() > 0)
			trac_truck_no.setText(s_tractor + "/" + s_truck_no);
		else
			trac_truck_no.setText("");
		odometer_reading.setText(s_odometer_reading);

		if(s_date !=null && s_date.length()>0 && !s_date.contentEquals("null")) {
			// if(formattedDate.contains(" "))
			// {
			StringTokenizer stt = new StringTokenizer(s_date, " ");
			String myDatez = "" + stt.nextToken();
			if(stt.hasMoreTokens())
			{
				String stime=stt.nextToken();

				try {
					final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
					final Date dateObj = sdf.parse(stime);
					String starttime=""+new SimpleDateFormat("h:mm a").format(dateObj);


					date.setText(myDatez+" "+starttime);


				} catch (final ParseException e) {
					e.printStackTrace();
				}
			}
		}
if(s_editmode.contentEquals("0")) {
	linedit.setVisibility(View.VISIBLE);
	if (confdate != null && confdate.length() > 0 && !confdate.contentEquals("null")) {
		// if(formattedDate.contains(" "))
		// {
		StringTokenizer stt = new StringTokenizer(confdate, " ");
		String myDatez = "" + stt.nextToken();
		if (stt.hasMoreTokens()) {
			String stime = stt.nextToken();

			try {
				final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
				final Date dateObj = sdf.parse(stime);
				String starttime = "" + new SimpleDateFormat("h:mm a").format(dateObj);


				txtconfirm.setText(myDatez + " " + starttime);


			} catch (final ParseException e) {
				e.printStackTrace();
			}
		}
	}

}else{
	linedit.setVisibility(View.GONE);
}

	}

	private void findViewById() {
		// TODO Auto-generated method stub
		mButtonEdit = (Button) findViewById(R.id.driver_check_inside_bt_edit);
		id = (TextView) findViewById(R.id.driver_check_inside_tv_id);
		carrier = (TextView) findViewById(R.id.driver_check_inside_tv_carrier);
		trac_truck_no = (TextView) findViewById(R.id.driver_check_inside_tv_trac_truck_no);
		odometer_reading = (TextView) findViewById(R.id.driver_check_inside_tv_odometer);
		date = (TextView) findViewById(R.id.driver_check_inside_tv_date);
		title = (TextView) findViewById(R.id.driver_check_inside_tv_title);
		//helpline=(TextView) findViewById(R.id.helpline);
		txtconfirm=findViewById(R.id.driver_check_confirm_date);
		linedit=findViewById(R.id.linedit);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.driver_check_inside_bt_view:
//			if(OnlineCheck.isOnline(this))
//			{
//				deleteCache(context);
//			Intent mIntent = new Intent(DriverCheckInsideActivity.this,
//					DriverChecklistView.class);
//			mIntent.putExtra("ID", s_id);
//				mIntent.putExtra(Constant.TRIP_OPTION, ""+str_tripopt);
//			startActivity(mIntent);
//
//			}
			break;

		case R.id.driver_check_inside_bt_edit:
			//Log.e("clling","clicked");
			pref.putString("seletecteditem","");
			pref.putString("seletecteditemtrailer","");
			if (s_trashmode.equalsIgnoreCase("1")
					|| s_editmode.equalsIgnoreCase("0")) {
				mButtonEdit.setBackgroundResource(R.drawable.activebuttonbase);
				mButtonEdit.setClickable(false);
				if(s_editmode.contentEquals("0")) {
					Toast.makeText(context, "Submitted checklist not allow to edit", Toast.LENGTH_SHORT).show();
				}

				if(s_trashmode.contentEquals("1"))
				{
					Toast.makeText(context, "Voided checklist not allow to edit", Toast.LENGTH_SHORT).show();
				}
			} else if (s_trashmode.equalsIgnoreCase("0")
					|| s_editmode.equalsIgnoreCase("0")) {
				mButtonEdit.setBackgroundResource(R.drawable.normalbuttonbase);
				mButtonEdit.setClickable(true);

				deleteCache(context);
				if (OnlineCheck.isOnline(this)) {
					Intent mIntent2 = new Intent(DriverCheckInsideActivity.this,
							AddDriverChecklist.class);
					mIntent2.putExtra("ID", s_id);
					mIntent2.putExtra("action", "edit");
					mIntent2.putExtra(Constant.TRIP_OPTION, "" + str_tripopt);
					startActivityForResult(mIntent2, 1);
				}
			}
			break;

		case R.id.driver_check_inside_bt_void:
			//service_void();
			break;

		case R.id.driver_check_inside_iv_back:
			pref.putString(Constant.VIEW_BACK,Constant.VIEW_BACK);
			finish();

			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		finish();
	}

	public static void deleteCache(Context context) {
		try {
			File dir = context.getCacheDir();
			deleteDir(dir);
		} catch (Exception e) { e.printStackTrace();}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
			return dir.delete();
		} else if(dir!= null && dir.isFile()) {
			return dir.delete();
		} else {
			return false;
		}
	}

//	private void service_void() {
//		// TODO Auto-generated method stub
//
//		// TODO Auto-generated method stub
//		dialog = new ProgressDialog(DriverCheckInsideActivity.this,
//				AlertDialog.THEME_HOLO_LIGHT);
//
//		if(OnlineCheck.isOnline(this))
//		{
//			dialog.setMessage("Please wait...");
//			dialog.setCancelable(false);
//			dialog.show();
//			WebServices.driver_inside_service_void(this,s_id,
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
//
//						}
//
//						@Override
//						public void onSuccess(int statusCode, Header[] headers,
//								JSONObject response) {
//							// TODO Auto-generated method stub
//							super.onSuccess(statusCode, headers, response);
//							dialog.dismiss();
//							if (response != null) {
//								object_method(response);
//							}
//
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
//
//
//	}

	protected void object_method(JSONObject response) {
		// TODO Auto-generated method stub

		try {
			String message = response.getString("message");

			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
					.show();
			finish();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	protected void array_method(JSONArray response) {
		// TODO Auto-generated method stub

	}

	
}
