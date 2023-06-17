package com.trucksoft.isoft.isoft_elog.Isoft_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isoft.trucksoft_elog.Model_class.ifta_model;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


@SuppressLint("InflateParams")
public class ifta_adater extends BaseAdapter {

	LayoutInflater inflater;
	Context context;
	private Bitmap btMap = null;
	List<ifta_model> mBeans = new ArrayList<ifta_model>();
	static String strdate="";
	//static List<grs_bin_tag> listtag=new ArrayList<>();
	//List<sub_production_response> listsubproduct=new ArrayList<>();
	public ifta_adater(Context context, List<ifta_model> mBeans, String dates) {
		// TODO Auto-generated constructor stub
		this.context = context;

		this.mBeans.addAll(mBeans);
		this.strdate=dates;
		//listtag=new ArrayList<>();
		inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);

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

		TextView txtnickname;
		TextView txtdate;
		LinearLayout linstate,lintotal;
		TextView txtgrandmiltotal;
		TextView txtgrandfueltotal;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub


		View row = null;

		if (row == null) {
			row  = inflater.inflate(R.layout.ifta_list, null);
			try {
				setAttributes(position, row);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else
		{
			row = (View) convertView;
		}
		return row;
	}

	

	public void setAttributes(final int position, View itemView) throws JSONException {
		final Holder holder = new Holder();

		ifta_model movieModel=mBeans.get(position);
		holder.txtnickname=  itemView.findViewById(R.id.txt_nickname);
		holder.txtdate=  itemView.findViewById(R.id.txtdate);
		holder.linstate=  itemView.findViewById(R.id.lin_state);
		holder.lintotal=  itemView.findViewById(R.id.lintotal);
		holder.txtgrandmiltotal=  itemView.findViewById(R.id.txt_grandmil);
		holder.txtgrandfueltotal=  itemView.findViewById(R.id.txt_grandfuel);
		holder.txtnickname.setText(""+movieModel.nickname);
		holder.txtdate.setText(""+strdate);
		//Log.e("summary","@"+movieModel.summaries.toString());


		JSONArray jArray =new JSONArray(movieModel.statelist.toString());
		if (jArray != null) {
			for (int i=0;i<jArray.length();i++){
				//listdata.add(jArray.getString(i));
	//			Log.e("substatez","@"+jArray.getString(i));
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View dialogView = inflater.inflate(R.layout.state_list, null);
LinearLayout linst=dialogView.findViewById(R.id.lintop);
LinearLayout linmil=dialogView.findViewById(R.id.lin_mil);
				ImageView imgarrow=dialogView.findViewById(R.id.listview_layout_next);
				ImageView imgarrowdown=dialogView.findViewById(R.id.listview_layout_down);
TextView txtval=dialogView.findViewById(R.id.txt_val);
					TextView stname = dialogView.findViewById(R.id.txt_state);
				stname.setText(""+jArray.getString(i));
				linst.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String val=txtval.getText().toString();
						if(val.contentEquals("0")) {
							txtval.setText("1");
							linmil.setVisibility(View.VISIBLE);
							imgarrowdown.setVisibility(View.VISIBLE);
							imgarrow.setVisibility(View.GONE);
							JSONObject questionMark = null;
							LayoutInflater inflatersubhead = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							final View dialogViewsubhead = inflatersubhead.inflate(R.layout.fuel_viewhead, null);
							linmil.addView(dialogViewsubhead);
							try {
								questionMark = new JSONObject(movieModel.summaries.toString());
								Iterator keys = questionMark.keys();
								while (keys.hasNext()) {
									// loop to get the dynamic key
									String currentDynamicKey = (String) keys.next();
			//						Log.e("currentDynamicKey", "@ " + currentDynamicKey);
									// get the value of the dynamic key
									try {
										JSONObject currentDynamicValue = questionMark.getJSONObject(currentDynamicKey);
			//							Log.e("currentDynamicValue", "@ " + currentDynamicValue.toString());
										Iterator keyssub = currentDynamicValue.keys();
										while (keyssub.hasNext()) {
											// loop to get the dynamic key
											String subkey = (String) keyssub.next();
			//								Log.e("subkey", "@ " + subkey);
											try {
												if (subkey.contentEquals("" + stname.getText().toString())) {
													//JSONArray jArraymil = new JSONArray(currentDynamicValue.getJSONArray(subkey).toString());
													JSONArray jArraymil = new JSONArray(currentDynamicValue.getJSONArray("" + stname.getText().toString()).toString());
													if (jArraymil != null) {
														for (int ij = 0; ij < jArraymil.length(); ij++) {
															//listdata.add(jArray.getString(i));
				//											Log.e("subvall", "@" + jArraymil.getString(ij));
															LayoutInflater inflatersub = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
															final View dialogViewsub = inflatersub.inflate(R.layout.fuel_view, null);

															TextView stmil = dialogViewsub.findViewById(R.id.txt_mil);
															TextView stgal = dialogViewsub.findViewById(R.id.txt_gal);
															TextView txtdate = dialogViewsub.findViewById(R.id.txt_date);
															double d = Double.parseDouble(jArraymil.getString(ij));
															DecimalFormat df = new DecimalFormat("#.##");
															float value = Float.valueOf(df.format(d));

															stmil.setText("" + value + " mi");
															ij++;
															double dfkkk = Double.parseDouble(jArraymil.getString(ij));
															DecimalFormat fkdf = new DecimalFormat("#.##");
															float valuefk = Float.valueOf(fkdf.format(dfkkk));
															stgal.setText("" +valuefk + "0 gal");
															txtdate.setText("" + currentDynamicKey);
															linmil.addView(dialogViewsub);
														}
													}
												}
											} catch (Exception e) {
			//									Log.e("jsonerr", "@" + e.toString());
											}
//					}

										}
									} catch (Exception e) {
										//Log.e("jsonerr", "@"+e.toString());
									}
									// do something here with the value...
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}else{
							linmil.removeAllViews();
							txtval.setText("0");
							imgarrowdown.setVisibility(View.GONE);
							imgarrow.setVisibility(View.VISIBLE);
							linmil.setVisibility(View.GONE);
						}

					}
				});



				holder.linstate.addView(dialogView);
			}

		}
		double grtot=0.0;
		double grfuel=0.0;
		try {
			JSONObject totalMark = null;
			totalMark = new JSONObject(movieModel.mileslist.toString());
			LayoutInflater inflatersub = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View dialogViewsub = inflatersub.inflate(R.layout.fuel_viewheadtotal, null);
			holder.lintotal.addView(dialogViewsub);

			Iterator keys = totalMark.keys();
			while (keys.hasNext()) {
				// loop to get the dynamic key
				String currentDynamicKey = (String) keys.next();
	//			Log.e("cdv", "@ " + currentDynamicKey);
				// get the value of the dynamic key
				try {
					JSONObject cval = totalMark.getJSONObject(currentDynamicKey);
		//			Log.e("ckv", "@ " + cval.toString());
					Iterator keyssub = cval.keys();
					while (keyssub.hasNext()) {
						// loop to get the dynamic key
						String subkey = (String) keyssub.next();
		//				Log.e("subkkey", "@ " + subkey);
		//				Log.e("cvaluse", "@ " + cval.get(subkey));
						String subkeydd = (String) keyssub.next();
		//				Log.e("subkeydd", "@ " + subkeydd);
						//
						LayoutInflater inflatersubdd = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						final View dialogViewsubdd = inflatersubdd.inflate(R.layout.fuel_view, null);

						TextView stmil = dialogViewsubdd.findViewById(R.id.txt_mil);
						TextView stgal = dialogViewsubdd.findViewById(R.id.txt_gal);
						TextView txtdate = dialogViewsubdd.findViewById(R.id.txt_date);
						double d = Double.parseDouble(cval.get(subkey).toString());
						grtot+=d;
						DecimalFormat df = new DecimalFormat("#.##");
						float value = Float.valueOf(df.format(d));

						stmil.setText("" + value+" mi");

						double dfuel = Double.parseDouble(cval.get(subkeydd).toString());
						grfuel+=dfuel;
						DecimalFormat dffuel = new DecimalFormat("#.##");
						float valuefuel = Float.valueOf(dffuel.format(dfuel));
						stgal.setText("" + valuefuel + "0 gal");
						txtdate.setText("" + currentDynamicKey);
						holder.lintotal.addView(dialogViewsubdd);


					}
				} catch (Exception e) {
		//			Log.e("jsonerrzzz", "@"+e.toString());
				}
				// do something here with the value...
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		DecimalFormat dfkkk = new DecimalFormat("#.##");
		float vkkk = Float.valueOf(dfkkk.format(grtot));
holder.txtgrandmiltotal.setText(""+vkkk +" mi");

		DecimalFormat dfkkkfuel = new DecimalFormat("#.##");
		float vkkkfuel = Float.valueOf(dfkkkfuel.format(grfuel));
		holder.txtgrandfueltotal.setText(""+vkkkfuel +" gal");



	}

	public String parseDateToddMMyyyy(String time) {
		String inputPattern = "yyyy-MM-dd";
		String outputPattern = "MM/dd/yyyy";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

		Date date = null;
		String str = null;

		try {
			date = inputFormat.parse(time);
			str = outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

}
