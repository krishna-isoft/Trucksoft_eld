package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessaging;
import com.isoft.trucksoft_elog.Loginactivitynew;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.version_model;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.Vehicle.Vehicle_seect;
import com.isoft.trucksoft_elog.company.companydashboard;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_DriverConfig;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_Notify_Utils;
import com.trucksoft.isoft.isoft_elog.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity  {
	private Preference pref;
	String str = "";
	Context context;
	private CommonUtil commonUtil;
	private String version;
	private String oldversion;
	private ImageView imglg;
	String regnum="";
	private BroadcastReceiver mRegistrationBroadcastReceiver;
	private Location mylocation;

	Thread th;
	Eld_api api;
	@SuppressWarnings("null")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.splash_screen);
		context = this.getApplicationContext();
		pref = Preference.getInstance(this);
		commonUtil = new CommonUtil(context);
	    pref.putString(Constant.OLD_VERSION_FIELD,"0");

		pref.putString(Constant.DEVICE_SUPPORT_BLUETOOTH_CALLING,"1");
		str = pref.getString(Constant.LOGIN_CHECK);


		mRegistrationBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(Trucksoft_elog_DriverConfig.DRIVER_APP_REGISTRATION_COMPLETE)) {
					// gcm successfully registered
					// now subscribe to `global` topic to receive app wide notifications
					FirebaseMessaging.getInstance().subscribeToTopic(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_GLOBAL);

					displayFirebaseRegId();

				} else if (intent.getAction().equals(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION)) {
					// new push notification is received

					String message = intent.getStringExtra("message");

//					Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();


					//notification(message);
				}
			}
		};
		displayFirebaseRegId();


		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			oldversion=""+packageInfo.versionCode;
			//Log.e("oldversion","@"+oldversion);
			chkversion(""+packageInfo.versionCode);
			//Log.e("versionName", ""+packageInfo.versionCode);

		}
		catch (PackageManager.NameNotFoundException e) {

		}


			 th = new Thread() {
				public void run() {
					try {

						sleep(2000);

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						//Log.e("val","end");


	//pref = Preference.getInstance(getApplicationContext());

	str = pref.getString(Constant.LOGIN_CHECK);
	Log.e("strlog","@"+str);
	boolean test = true;

	if (version != null && version.length() > 0 && !version.contentEquals("null")) {
		Double a = 0.00;
		a = Double.parseDouble(version);
		Double b = 0.00;
		b = Double.parseDouble(oldversion);

		//	b=Integer.parseInt(oldversion);
		if (a > b) {
			test = false;
		} else {
			test = true;
		}
	} else {
		test = true;
	}

	if (test) {

		if (str != null && str.length() > 0 && !str.contentEquals("null")) {
			if (str.equalsIgnoreCase("logged_inn")) {
				String vinx=pref.getString(Constant.VIN_NUMBER);
				if(vinx !=null && vinx.length()>0 && !vinx.contentEquals("null"))
				{
					if(vinx.contentEquals("Demo"))
					{
						finish();
//						Intent mIntent = new Intent(
//								SplashScreenActivity.this,
//								Select_vehicle.class);
//						startActivity(mIntent);
						Log.e("vc","2");
						Intent mIntent = new Intent(
								SplashScreenActivity.this,
								Vehicle_seect.class);
						startActivity(mIntent);
					}else{
						finish();
                        if(pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH) !=null && pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).length()>0 && pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).contentEquals("no"))
                        {
                            pref.putString(
                                    Constant.DRIVER_MESSAGE_STATUS,
                                    "0");
//                            Intent mIntent = new Intent(
//                                    SplashScreenActivity.this,
//                                    Home_activity.class);
							Intent mIntent = new Intent(
									SplashScreenActivity.this,
									Home_activity_bluetooth.class);
                            startActivity(mIntent);

                        }else {


							if(pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH) ==null ||pref.getString(Constant.DEVICE_SUPPORT_BLUETOOTH).length()==0)
							{
								pref.putString(Constant.DEVICE_SUPPORT_BLUETOOTH,"no");
							}

							if(pref.getString(Constant.NETWORK_TYPE)==null || pref.getString(Constant.NETWORK_TYPE).length()==0)
							{
								pref.putString(Constant.NETWORK_TYPE,""+Constant.CELLULAR);
							}


							 if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR))
							{

                                pref.putString(
                                        Constant.DRIVER_MESSAGE_STATUS,
                                        "0");
//                                Intent mIntent = new Intent(
//                                        SplashScreenActivity.this,
//										Home_activity.class);
								Intent mIntent = new Intent(
										SplashScreenActivity.this,
										Home_activity_bluetooth.class);
                                startActivity(mIntent);

                            }else{
								 pref.putString(
										 Constant.DRIVER_MESSAGE_STATUS,
										 "0");
								/* Intent mIntent = new Intent(
										 SplashScreenActivity.this,
										 ScanActivity.class);*/
								 Intent mIntent = new Intent(
										 SplashScreenActivity.this,
										 Home_activity_bluetooth.class);
								 startActivity(mIntent);
							 }
                        }



//						Intent mIntent = new Intent(
//								SplashScreenActivity.this,
//								BluetoothMainActivity.class);
//						startActivity(mIntent);

//						Intent mIntent1 = new Intent(SplashScreenActivity.this, Report_Home.class);
//						startActivity(mIntent1);

					}
				}else{
					finish();
//					Intent mIntent = new Intent(
//							SplashScreenActivity.this,
//							Select_vehicle.class);
//					startActivity(mIntent);
					Log.e("vc","1");
					Intent mIntent = new Intent(
							SplashScreenActivity.this,
							Vehicle_seect.class);
					startActivity(mIntent);
				}

			} else if (pref.getString(Constant.COMPANY_LOGIN_STATUS) !=null &&
					pref.getString(Constant.COMPANY_LOGIN_STATUS).contentEquals("success")) {
				finish();
				Intent mIntent = new Intent(
						SplashScreenActivity.this,
						companydashboard.class);
				startActivity(mIntent);
			}
			else if (str.equalsIgnoreCase("logged_off")) {
				finish();
				Intent mIntent = new Intent(
						SplashScreenActivity.this,
						Loginactivitynew.class);
				startActivity(mIntent);
			}
		} else {

			str = pref.getString(Constant.LOGIN_CHECK);
//										Log.e("elccr", "" + str);
//										boolean isAppInstalled = appInstalledOrNot("com.dashlogic.dlj1939service");
//										if (isAppInstalled) {
			finish();
			Intent mIntent = new Intent(
					SplashScreenActivity.this,
					Loginactivitynew.class);
			startActivity(mIntent);

//			Intent mIntent = new Intent(
//					SplashScreenActivity.this,
//					Dashboard.class);
//			startActivity(mIntent);

		}
	} else {
		final String appPackageName = getPackageName();

		try {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
		}
		finish();
	}
//							}



//else{
//
//	pref = Preference.getInstance(getApplicationContext());
//
//	str = pref.getString(Constant.LOGIN_CHECK);
//
//	boolean test = true;
//
//	if (version != null && version.length() > 0 && !version.contentEquals("null")) {
//		Double a = 0.00;
//		a = Double.parseDouble(version);
//		Double b = 0.00;
//		b = Double.parseDouble(oldversion);
//
//		//	b=Integer.parseInt(oldversion);
//		if (a > b) {
//			test = false;
//		} else {
//			test = true;
//		}
//	} else {
//		test = true;
//	}
//
//	if (test) {
//
//		if (str != null && str.length() > 0 && !str.contentEquals("null")) {
//			if (str.equalsIgnoreCase("logged_inn")) {
//				finish();
//				pref.putString(
//						Constant.DRIVER_MESSAGE_STATUS,
//						"0");
//				Intent mIntent = new Intent(
//						SplashScreenActivity.this,
//						Home_activity.class);
//				startActivity(mIntent);
//			} else if (str.equalsIgnoreCase("logged_off")) {
//
////											boolean isAppInstalled = appInstalledOrNot("com.dashlogic.dlj1939service");
////											if (isAppInstalled) {
//				finish();
//				Intent mIntent = new Intent(
//						SplashScreenActivity.this,
//						Loginactivitynew.class);
//				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
//						Intent.FLAG_ACTIVITY_NEW_TASK);
//				startActivity(mIntent);
////											} else {
////												finish();
////												startActivity(getIntent());
////											}
//			}
//		} else {
//
//			str = pref.getString(Constant.LOGIN_CHECK);
////										Log.e("elccr", "" + str);
////										boolean isAppInstalled = appInstalledOrNot("com.dashlogic.dlj1939service");
////										if (isAppInstalled) {
//			finish();
//			Intent mIntent = new Intent(
//					SplashScreenActivity.this,
//					Loginactivitynew.class);
//			startActivity(mIntent);
////										} else {
////											finish();
////											startActivity(getIntent());
////										}
//		}
//	} else {
//		final String appPackageName = getPackageName();
//
//		try {
//			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//		} catch (android.content.ActivityNotFoundException anfe) {
//			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//		}
//		finish();
//	}
////							}
//
//}
					}
				}
			};
			th.start();

	}





	private void displayFirebaseRegId() {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(Trucksoft_elog_DriverConfig.ISOFT_SHARED_PREF, 0);
		String regId = pref.getString("regId", null);
		regnum= pref.getString("regId", null);

		Log.e("Firebase reg id: ","" + regId);


	}
	@Override
	protected void onResume() {
		super.onResume();

		// register GCM registration complete receiver
		LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
				new IntentFilter(Trucksoft_elog_DriverConfig.DRIVER_APP_REGISTRATION_COMPLETE));

		// register new push message receiver
		// by doing this, the activity will be notified each time a new message arrives
		LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
				new IntentFilter(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION));

		// clear the notification area when the app is opened
		Trucksoft_elog_Notify_Utils.clearNotifications(getApplicationContext());
	}

	@Override
	protected void onPause() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
		super.onPause();
	}
	public void chkversion(String vcode)
	{
		api = ApiServiceGenerator.createService(Eld_api.class);
		  //Log.e("url","checkversion_new.php?vcode="+vcode+"&regnum="+regnum+"&did="+pref.getString(Constant.DRIVER_ID));
		Call<List<version_model>> call = api.getcheckversion(vcode,regnum,""+pref.getString(Constant.DRIVER_ID));

		call.enqueue(new Callback<List<version_model>>() {
			@Override
			public void onResponse(Call<List<version_model>> call, Response<List<version_model>> response) {

				Log.e("Responsestring", response.body().toString());
				if (response.isSuccessful()) {
					List<version_model> vk = response.body();
					for(int j=0;j<vk.size();j++)
					{
						version_model vm=vk.get(j);
											if (vm.status.equalsIgnoreCase("1")) {
									version=vm.versions;
									Log.e("version","@"+version);
									pref.putString(Constant.OLD_VERSION_FIELD,""+version);
								} else if (vm.status.equalsIgnoreCase("0")) {

									version=null;
								}
					}

				}

			}

			@Override
			public void onFailure(Call<List<version_model>> call, Throwable t) {
				Log.e("Exceptionwttttttt", t.toString());
				// cancelprogresssdialogz();
			}
		});
	}
//	public void checkversion(String vcode) {
//
//
//		if (OnlineCheck.isOnline(this)) {
//
//			WebServices.chkversion(this, vcode,regnum, new JsonHttpResponseHandler() {
//			//	@Override
//				public void onFailure(int statusCode, Header[] headers,
//									  String responseString, Throwable throwable) {
//					// TODO Auto-generated method stub
//					super.onFailure(statusCode, headers, responseString,
//							throwable);
//
//				}
//
//				//@Override
//				public void onFailure(int statusCode, Header[] headers,
//									  Throwable throwable, JSONArray errorResponse) {
//					// TODO Auto-generated method stub
//					super.onFailure(statusCode, headers, throwable,
//							errorResponse);
//
//				}
//
//				//@Override
//				public void onFailure(int statusCode, Header[] headers,
//									  Throwable throwable, JSONObject errorResponse) {
//					// TODO Auto-generated method stub
//					super.onFailure(statusCode, headers, throwable,
//							errorResponse);
//
//				}
//
//				//@Override
//				public void onSuccess(int statusCode, Header[] headers,
//									  JSONArray response) {
//					// TODO Auto-generated method stub
//					super.onSuccess(statusCode, headers, response);
//
//					//Log.e("response",""+response.toString());
//
//					try {
//						if (response != null) {
//
//
//							for(int j=0;j < response.length();j++)
//							{
//
//								JSONObject objdispdet = response.getJSONObject(j);
//								String status = objdispdet.getString("status");
//
//								if (status.equalsIgnoreCase("1")) {
//									version=objdispdet
//											.getString("versions");
//									//Log.e("version","@"+version);
//									pref.putString(Constant.OLD_VERSION_FIELD,""+version);
//								} else if (status.equalsIgnoreCase("0")) {
//
//									version=null;
//								}
//
//							}
//
//
//
//
//
//
//
//
//
//						}
//
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//
//				//@Override
//				public void onSuccess(int statusCode,Header[] headers,
//									  JSONObject response) {
//					// TODO Auto-generated method stub
//					super.onSuccess(statusCode, headers, response);
//					//Log.e("response1",""+response.toString());
//
//					try {
//						if (response != null) {
//
//
//
//
//
//
//							String status = response
//									.getString("status");
//
//							if (status.equalsIgnoreCase("1")) {
//								version=response
//										.getString("versions");
//								pref.putString(Constant.OLD_VERSION_FIELD,""+version);
//							} else if (status.equalsIgnoreCase("0")) {
//
//								version=null;
//							}
//
//
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//
//				//@Override
//				public void onSuccess(int statusCode, Header[] headers,
//									  String responseString) {
//					// TODO Auto-generated method stub
//					super.onSuccess(statusCode, headers, responseString);
//				}
//
//			});
//		}
//
//	}

/******************************** Permission test**************************************/







	}



