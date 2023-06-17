package com.trucksoft.isoft.isoft_elog.Isoft_adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Respp_model;
import com.isoft.trucksoft_elog.Model_class.Subdetailview;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.trucksoft.isoft.isoft_elog.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("InflateParams")
public class ReportHomeAdapter extends BaseAdapter {
	ArrayList<ReportDetailsBean> mBeans = new ArrayList<ReportDetailsBean>();
	Context mcontext;
	LayoutInflater inflater;
	CommonUtil commonUtil;
	ProgressDialog progressdlog;
	private Preference pref;
	Eld_api api;
	String type;
	Dialog dialogrkship;

	public ReportHomeAdapter(Context context,
			ArrayList<ReportDetailsBean> mBeans,String week) {
		// TODO Auto-generated constructor stub
		mcontext = context;
		this.mBeans = mBeans;
		this.type=week;
		commonUtil = new CommonUtil(context);
		inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		pref = Preference.getInstance(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mBeans.size();
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
		TextView txt_date;
		TextView txt_onduty;
		TextView count;
		TextView txt_ofduty;
		TextView txt_drive;
		TextView txt_sleep;
		TextView txtcertfy;
		TextView txtodrdr;



	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.reportlist, null);
		}

		setAttributes(position, convertView);

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				click(position);
			}
		});
		return convertView;
	}

	public void click(int position) {
		//Log.e("position","/"+position);
String dates=mBeans.get(position).getdate();
		Log.e("dates","/"+dates);
		pref.putString(Constant.G_STATUS,"1");
		pref.putString(Constant.MONTHLY_DATE,""+dates);

		Intent mintent = new Intent(mcontext, Subdetailview.class);
		mintent.putExtra("type",type);
		mcontext.startActivity(mintent);
	}
	
	
	public void setAttributes(final int position, View convertView) {
		
		
		Holder holder = new Holder();
		
		holder.txt_date = (TextView) convertView
				.findViewById(R.id.tdate);
		
		
		holder.txt_onduty = (TextView) convertView
				.findViewById(R.id.txtonduty);
	//	holder.imgview=(ImageView)convertView.findViewById(R.id.imgvk);
		
		holder.txtcertfy=convertView.findViewById(R.id.txtcertfy);
		holder.txt_ofduty=(TextView)convertView.findViewById(R.id.txtofdutty);
		holder.txt_drive=(TextView)convertView.findViewById(R.id.txtdrive);
		holder.txt_sleep=(TextView)convertView.findViewById(R.id.txt_sleep);

		holder.txtodrdr=(TextView)convertView.findViewById(R.id.todrdr);

		String sstdrive=mBeans.get(position).getDrive();

		String sstonduty=mBeans.get(position).getOOnduty();


		String sstoffduty=mBeans.get(position).getOffduty();

		String sstsleep=mBeans.get(position).getsleep();
		String sadte=mBeans.get(position).getdate().trim();

		try{
			if(type.contentEquals("weekly"))
			{
				holder.txtodrdr.setVisibility(View.VISIBLE);
				holder.txtodrdr.setText(""+mBeans.get(position).getOddrvtime());
			}else{
				holder.txtodrdr.setVisibility(View.GONE);
			}

		}catch (Exception e)
		{

		}


		String clrt=mBeans.get(position).getcertify().trim();

		if(clrt !=null && clrt.length()>0)
		{
			if(clrt.contentEquals("1"))
			{
				String statdate=mBeans.get(position).getCertifydate().trim();
				String stcontime="";
				String mdate="";
				String newdate="";
				if(statdate !=null && statdate.length()>0)
				{
					StringTokenizer stkk=new StringTokenizer(statdate," ");
					mdate=stkk.nextToken();

					SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
					try {
						Date oneWayTripDate = input.parse(mdate);                 // parse input
						newdate=output.format(oneWayTripDate);    // format output
					} catch (ParseException e) {
						e.printStackTrace();
					}
					String _24HourTime = "";
					if(stkk.hasMoreTokens())
					{
						_24HourTime=stkk.nextToken();
					}
					try {

						SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
						SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
						Date _24HourDt = _24HourSDF.parse(_24HourTime);
						//   System.out.println(_24HourDt);
						stcontime=_12HourSDF.format(_24HourDt);
						// System.out.println(_12HourSDF.format(_24HourDt));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				holder.txtcertfy.setBackgroundResource(R.drawable.button_activate_ident);
				holder.txtcertfy.setText(" CERTIFIED ON : "+newdate+" @"+stcontime+" ");
			}else{
				holder.txtcertfy.setBackgroundResource(R.drawable.button_ident);
				holder.txtcertfy.setText("VIEW & CERTIFY");
			}

		}else{
			holder.txtcertfy.setBackgroundResource(R.drawable.button_ident);
			holder.txtcertfy.setText("VIEW & CERTIFY");
		}
		if(sstoffduty.contentEquals("23:59:00") || sstoffduty.contentEquals("23:58:00")
		|| sstsleep.contentEquals("23:59:00") || sstsleep.contentEquals("23:58:00")) {
			holder.txtcertfy.setBackgroundResource(R.drawable.button_grey);
			holder.txtcertfy.setText("NOT REQUIRED");
		}


holder.txtcertfy.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) {
		//Log.e("fnnn","clicked"+mBeans.get(position).getTrip_num());
		//Log.e("valll",""+holder.txtcertfy.getText().toString());
		//Log.e("sstoffduty","&"+sstoffduty);
//		if(mBeans.get(position).getTrip_num() !=null && mBeans.get(position).getTrip_num().contentEquals("1")
//		|| sstoffduty.contentEquals("23:59:00") || sstoffduty.contentEquals("23:58:00")) {
//			if (holder.txtcertfy.getText().toString().contains("Certify Now")
//					|| holder.txtcertfy.getText().toString().contains("Certify now")
//					|| holder.txtcertfy.getText().toString().contains(" CERTIFY NOW ")) {
//				savecertify(sadte, "1", holder.txtcertfy);
//			}
//		}else{
//			showmissingtripnumalert();
//		}
		String dates=mBeans.get(position).getdate();
		Log.e("dates","/"+dates);
		pref.putString(Constant.G_STATUS,"1");
		pref.putString(Constant.MONTHLY_DATE,""+dates);

		Intent mintent = new Intent(mcontext, Subdetailview.class);
		mintent.putExtra("type",type);
//		if(mBeans.get(position).getTrip_num() !=null && mBeans.get(position).getTrip_num().contentEquals("1")
//				|| sstoffduty.contentEquals("23:59:00") || sstoffduty.contentEquals("23:58:00")) {
//
//			mintent.putExtra("cval",0);
//		}else{
//			mintent.putExtra("cval",1);
//		}
		mcontext.startActivity(mintent);
	}
});



		SimpleDateFormat formatz= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String myDatez = formatz.format(new Date());

		String currenthr=pref.getString(Constant.CURRENT_SCHEDULE_TOTAL);
if(myDatez.contentEquals(sadte)) {
	if (currenthr != null && currenthr.length() > 0 && !currenthr.contentEquals("null")) {
		StringTokenizer sk = new StringTokenizer(currenthr, ">>");
		String st1 = sk.nextToken();
		String st2 = sk.nextToken();
		if (!st2.contentEquals("00:00") || !st2.contentEquals("0:0")) {

			holder.txt_onduty.setText(sstonduty);
			holder.txt_ofduty.setText(sstoffduty);
			holder.txt_drive.setText(sstdrive);
			holder.txt_sleep.setText(sstsleep);
			holder.txt_date.setText(sadte);
			if (st1.contentEquals(commonUtil.ON_DUTY)) {

				long optime = splittime(sstonduty);
				long opnewtime = splittime(st2);
				long opnewtimek = optime + opnewtime;
				String opkk = printsum(opnewtimek);
				holder.txt_onduty.setText(opkk);
			} else if (st1.contentEquals(commonUtil.OFF_DUTY)) {
				long optime = splittime(sstoffduty);
				long opnewtime = splittime(st2);
				long opnewtimek = optime + opnewtime;
				String opkk = printsum(opnewtimek);
				holder.txt_ofduty.setText(opkk);

			} else if (st1.contentEquals(commonUtil.SLEEP)) {
				long optime = splittime(sstsleep);
				long opnewtime = splittime(st2);
				long opnewtimek = optime + opnewtime;
				String opkk = printsum(opnewtimek);
				holder.txt_sleep.setText(opkk);

			} else if (st1.contentEquals(commonUtil.DRIVING)) {
				long optime = splittime(sstdrive);
				long opnewtime = splittime(st2);
				long opnewtimek = optime + opnewtime;
				String opkk = printsum(opnewtimek);
				holder.txt_drive.setText(opkk);
			}
		} else {

			holder.txt_onduty.setText(sstonduty);
			holder.txt_ofduty.setText(sstoffduty);
			holder.txt_drive.setText(sstdrive);
			holder.txt_sleep.setText(sstsleep);
			holder.txt_date.setText(sadte);
		}
	} else {

		holder.txt_onduty.setText(sstonduty);
		holder.txt_ofduty.setText(sstoffduty);
		holder.txt_drive.setText(sstdrive);
		holder.txt_sleep.setText(sstsleep);
		holder.txt_date.setText(sadte);
	}

}else
{
	holder.txt_onduty.setText(sstonduty);
	holder.txt_ofduty.setText(sstoffduty);
	holder.txt_drive.setText(sstdrive);
	holder.txt_sleep.setText(sstsleep);
	holder.txt_date.setText(sadte);
}
	}
	public String printsum(long different) {

		//	long secondsInMilli = 1000;
		long minutesInMilli =  60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;

		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;

		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;

		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;

		long elapsedSeconds = different ;

		if(String.valueOf(elapsedHours).contains("-") || String.valueOf(elapsedMinutes).contains("-"))
		{
			return elapsedHours + ":" + elapsedMinutes ;
		}else {
			return pad(elapsedHours) + ":" + pad(elapsedMinutes) ;
		}

	}
	public static String pad(Long num) {
		String res = null;
		if(num < 10)
			res = "0" + num;
		else
			res =  "" + num;

		return res;
	}

	public long splittime(String time)
	{
		int seconds = 00;
//Log.e("splittime",""+time);
		if(time !=null && time.length()>0 && !time.contentEquals("null")&& !time.contains("-")) {
			String timeSplit[] = time.split(":");

			seconds = Integer.parseInt(timeSplit[0]) * 60 * 60 + Integer.parseInt(timeSplit[1]) * 60;

		}
		return seconds;

	}


	private  void savecertify(String date, String status, TextView txtcertfy) {
		progressdlog = new ProgressDialog(mcontext,
				AlertDialog.THEME_HOLO_LIGHT);
		progressdlog.setMessage("Please wait...");
		progressdlog.setCancelable(false);
		progressdlog.show();

		String did = pref.getString(Constant.DRIVER_ID);
		// Log.e("date","@"+date);


		//  Log.e("valll","http://eld.e-logbook.info/elog_app/certify_log.php?driver="+did+"&date="+date+"&status="+status);
		api = ApiServiceGenerator.createService(Eld_api.class);
		Call<Respp_model> call = api.savecertifystatus(""+did,date, ""+status,""+pref.getString(Constant.CURRENT_STATE),""+pref.getString(Constant.COMPANY_CODE));
		call.enqueue(new Callback<Respp_model>() {

			public void onResponse(Call<Respp_model> call, Response<Respp_model> response) {
				if (response.isSuccessful()) {
					//Log.e("result","success");
					Respp_model rk=new Respp_model();
					rk=response.body();
					String statdate = rk.cdate;
					// Log.e("statdate","@"+statdate);
					String stcontime="";
					String mdate="";
					String newdate="";
					if(statdate !=null && statdate.length()>0)
					{
						StringTokenizer stkk=new StringTokenizer(statdate," ");
						mdate=stkk.nextToken();

						SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
						try {
							Date oneWayTripDate = input.parse(mdate);                 // parse input
							newdate=output.format(oneWayTripDate);    // format output
						} catch (ParseException e) {
							e.printStackTrace();
						}


						String _24HourTime = "";
						if(stkk.hasMoreTokens())
						{
							_24HourTime=stkk.nextToken();
						}
						try {

							SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
							SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
							Date _24HourDt = _24HourSDF.parse(_24HourTime);
							//   System.out.println(_24HourDt);
							stcontime=_12HourSDF.format(_24HourDt);
							// System.out.println(_12HourSDF.format(_24HourDt));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					txtcertfy.setBackgroundResource(R.drawable.button_activate_ident);
					txtcertfy.setText(" CERTIFIED ON : "+newdate+" @"+stcontime+" ");
					txtcertfy.setVisibility(View.VISIBLE);


					cancelprogresssdialog();
				} else {
					cancelprogresssdialog();
					// Log.e("result","fail");
				}
			}

			@Override
			public void onFailure(Call<Respp_model> call, Throwable t) {
				// Log.e("dd"," Response Error "+t.getMessage());
				cancelprogresssdialog();
			}
		});
	}

	private void cancelprogresssdialog()
	{


		try {
			if ((progressdlog != null) && progressdlog.isShowing()) {
				progressdlog.dismiss();
			}
		} catch (final IllegalArgumentException e) {
			//   Log.e("err1.........",""+e.toString());
			// Handle or log or ignore
		} catch (final Exception e) {
			// Log.e("err2........",""+e.toString());
			// Handle or log or ignore
		} finally {
			progressdlog = null;
		}
	}
private void showmissingtripnumalert()
{
	LayoutInflater inflater = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.alert_shipdoc, null);
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        dialogrkship = new Dialog(mcontext, R.style.DialogTheme);



        dialogrkship.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogrkship.setContentView(dialogView);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogrkship.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogrkship.getWindow().setAttributes(lp);
        dialogrkship.show();


        btnsubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
			{
				dialogrkship.dismiss();
			}
        });

}

}
