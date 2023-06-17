package com.trucksoft.isoft.isoft_elog.driverchecklist;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Loginactivitynew;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Remark_model;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_DriverConfig;
import com.isoft.trucksoft_elog.isoft_eloggcm.Trucksoft_elog_Notify_Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddDriverChecklist extends FragmentActivity implements OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

	TextView   tv_truck_no, tv_truck_items,
			tv_trailer, tv_trailer_items, tv_driver_sign,
			tv_save, tv_submit;
	static  TextView tv_date;
	EditText  et_odometer, et_remark,
			 et_truck_other, et_trailer_other;
	ImageView iv_sign_driver, iv_sign_driver_clear;
	TextView et_driver_name;
	private String str_driversig="";
	private String str_mechrsig="";
	LinearLayout mLayout_timemode, mLayout_truck_other, mLayout_trailer_other;
	LinearLayout mLayout;
	RadioGroup mGroup_timemode;
	RadioButton mButton_timemode_am, mButton_timemode_pm;

	RadioGroup mGroup_tripmode;
	RadioButton mButton_trippost, mButton_trippre;
	String MyPREFERENCES = "filter_status";
	String mTag, mValue;
	String s_action;
	CheckBox cb_tick1,  cb_tick3;
	SharedPreferences sharedpreferences;
	ProgressDialog dialog;
	String hour2;
	String min2;
	String lat = "", lon = "", lat2 = "", lon2 = "", lat3 = "", lon3 = "";
	String image_string = "", image_string2 = "", image_string3 = "";
	ArrayList<String> truckfld = new ArrayList<String>();
	ArrayList<String> truckfldimg = new ArrayList<String>();
	ArrayList<String> truckfldgood = new ArrayList<>();
//	ArrayList<String> trailfld = new ArrayList<String>();
	ArrayList<String> truckfld_status = new ArrayList<String>();
	ArrayList<String> trailfld_status = new ArrayList<String>();
	Boolean check_truck_ids = false;
	Boolean check_trailer_ids = false;
	String id = "";
	Editor editor;
	TextView title;
	private ImageView imgphoto;

	private Bitmap bitmap;
	private byte[] img;
	private static final int PICK_FROM_FILE = 44;
	protected int outputX = 300;
	protected int outputY = 300;
	protected int aspectX = 1;
	protected int aspectY = 1;
	// private CommonUtil commonUtil;
	private static final int PICK_FROM_CAMERA = 11;
	private static final int CROP_FROM_CAMERA = 33;
	private Uri mImageCaptureUri;
	private Uri mImageCropUri;
	private static final int NOTIFY_ME_ID = 1337;

	private String pphoto;
	Bitmap b1;
	private Context context;
	private byte[] image;
	String updatedStringimage;

	private static final int RESULT_LOAD_IMAGE = 10;
	String filePath = "";
	private TextView taddress, txt_ddate;


	private TextView timgaddress, txt_imgddate;

	private LinearLayout linedriver, lineimg;
	private LinearLayout lintks;
	private static Preference pref;
	private TextView txttl;

	//Spinner spintripoption;
	private String strTrip;
    private ImageView imgadd;
    LinearLayout lytLayout;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private int imgval=0;
    ArrayList<Uri> arrayimg=new ArrayList<>();
    ArrayList<String> arrayimgold=new ArrayList<>();
//	ArrayList<String> truck_item_old=new ArrayList<>();
	ArrayList<String> selectid=new ArrayList<>();
	ArrayList<String> arrayimgstring=new ArrayList<>();
	Dialog dialogprivacy;
	ArrayList<String> truckfldtrailer = new ArrayList<String>();
	ArrayList<String> truckfldtrailerimg = new ArrayList<String>();
	ArrayList<String> truckfldgoodtrailer = new ArrayList<>();
	ArrayList<String> truckfld_statustrailer = new ArrayList<String>();
	ArrayList<String> selectidtrailer=new ArrayList<>();

	private TextView buy_button;

	private String strtruckcount="";
	private String strtrailercount="";
	private TextView txttruckcount;
	private TextView txttrailcount;
	private TextView txtcompname;
	private TextView txtconverify;
	String model_name="";
	signature mSignature;
	public Bitmap mBitmap;
	private byte[] imagesign;
	public String updatedStringimagesign;
	Button btntransmit, btnclear;
	LinearLayout mContent;
	View mView;
	private String straddress="";
	boolean GpsStatus;
	LocationManager locationManager;
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	Location mCurrentLocation;
	private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
	private long FASTEST_INTERVAL = 10; /* 2 sec */
	private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
	Eld_api api;
	private int mStatusCode;


	String updatedStringimage1;
	private Paint paint;

	View view;
	String DIRECTORY;
	String StoredPath;

	private Bitmap bitmap1;

	private byte[] image1;
	private ImageView lin_driversignature;
	private String driverid="";
	int drivesig=0;
	private TextView txttruckno;

	private int truckitemcount=40;
	private int traileritemcount=15;
	private TextView txttruckgood,txttruckbad;
	private TextView txttrailergood,txttrailerbad;
	Font_manager font_manager;
	private ImageView imgtruck,imgtrailer;
	Dialog dialogfederal;

	private BroadcastReceiver mRegistrationBroadcastReceiver;
	private static final int NOTIFY_ME_ID_DISPATCH = 1340;

	private String androidId="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_driver_checklist);
		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				this.MODE_PRIVATE);
		editor = sharedpreferences.edit();
		context = this;
		findViewById();
		pref = Preference.getInstance(context);
		truckfldgood = new ArrayList<>();
		font_manager=new Font_manager();
		et_driver_name.setText(""+pref.getString(Constant.DRIVER_NAME));
		txtcompname.setText(""+pref.getString(Constant.COMPANY_NAME));
		txttruckgood.setText("Good condition item selected : 0/"+truckitemcount);
		txttruckbad.setText("Bad condition item selected : 0/"+truckitemcount);

		txttrailergood.setText("Good condition item selected : 0/"+traileritemcount);
		txttrailerbad.setText("Bad condition item selected : 0/"+traileritemcount);
		buy_button	.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));


		//truck_item_old=new ArrayList<>();
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();
		mGoogleApiClient.connect();
		CheckGpsStatus();
		if (!GpsStatus) {
			getMyLocation(context);
		}
		String truckd="";
		if(pref.getString(Constant.VID_NUMBER) !=null && pref.getString(Constant.VID_NUMBER).length()>0)
		{
			truckd=pref.getString(Constant.VID_NUMBER);
		}
		if(truckd!=null && truckd.length()>0 && !truckd.contentEquals("null")) {
			txttruckno.setText("" + truckd);
		}else{
			txttruckno.setText("");
		}
		 model_name=pref.getString(Constant.MODEL_NAME);
		String ccode=pref.getString(Constant.COMPANY_CODE).trim();
		if(ccode.contentEquals("wwe"))
		{

			lintks.setVisibility(View.GONE);
		}else
		{
			lintks.setVisibility(View.GONE);
		}
		tv_truck_no.setText(""+model_name);
        try
        {
			SimpleDateFormat formatz = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());
			String myDatez = formatz.format(new Date());

			tv_date.setText(""+myDatez);
        }catch (Exception e)
        {

        }
getcount();
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		DIRECTORY = Environment.getExternalStorageDirectory().getPath() + File.separator + getResources().getString(R.string.app_name);
		StoredPath = DIRECTORY + "/" + driverid + ".png";
		File dir = new File(DIRECTORY);
		if (!dir.exists())
			dir.mkdirs();
		try {
			Calendar calander = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a" );

			String time = simpleDateFormat.format(calander.getTime());

			if(time.contains("am")|| time.contains("AM"))
			{
				mLayout_timemode.setVisibility(View.VISIBLE);
				mButton_timemode_am.setChecked(true);
				mButton_timemode_pm.setEnabled(false);
			}else{
				mLayout_timemode.setVisibility(View.VISIBLE);
				mButton_timemode_pm.setChecked(true);
				mButton_timemode_am.setEnabled(false);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
String vb=pref.getString(Constant.DLIST_STATUS);
		if(vb.contentEquals("add"))
		{
			imgval=0;
		}
        buy_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                       callprivacy();
                    }else {
                        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(AddDriverChecklist.this);
                    }
                }else {
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(AddDriverChecklist.this);
                }
            }
        });

imgadd.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
				callprivacy();

            }else {
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(AddDriverChecklist.this);
            }
        }else {
            CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(AddDriverChecklist.this);
        }





	}
});








		Intent mIntent = getIntent();
		s_action = mIntent.getStringExtra("action");
		if (s_action.equalsIgnoreCase("edit")) {
			title.setText("Edit Driver's Vehicle Inspection Report");
			id = mIntent.getStringExtra("ID");
			//edit_driver_checklist(id);
		} else {
//getcount();

			mLayout.setVisibility(View.VISIBLE);
		}
if(mIntent.hasExtra(Constant.TRIP_OPTION))
{
	strTrip=mIntent.getStringExtra(Constant.TRIP_OPTION);
}

		tv_truck_no.setOnClickListener(this);
		tv_truck_items.setOnClickListener(this);

		tv_trailer_items.setOnClickListener(this);
		tv_trailer.setOnClickListener(this);
		tv_driver_sign.setOnClickListener(this);

		tv_save.setOnClickListener(this);
		tv_save.setText("SUBMIT");
		tv_submit.setOnClickListener(this);
		imgphoto.setOnClickListener(this);

		if(strTrip !=null && strTrip.length()>0 && !strTrip.contentEquals("null"))
		{
			if(strTrip.contentEquals("PRE TRIP"))
			{
				mButton_trippre.setChecked(true);
			}else{
				mButton_trippre.setChecked(false);
			}
			//Constant.setSpinnerval(spintripoption, context, Constant.TRIP_ARRAY,strTrip);
		}else
		{
			//Constant.setSpinnervf(spintripoption, context, Constant.TRIP_ARRAY);
			mButton_trippre.setChecked(true);
		}
imgtruck.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) {
		if (OnlineCheck.isOnline(context)) {
//				Intent mIntent6 = new Intent(AddDriverChecklist.this,
//						CheckListActivity.class);
//				mIntent6.putExtra("tag", "truck");
//				mIntent6.putStringArrayListExtra("truckfld_status",
//						truckfld_status);
//				startActivityForResult(mIntent6, 6);

			Intent mIntent6 = new Intent(AddDriverChecklist.this,
					Trucklistview.class);
			mIntent6.putExtra("tag", "truck");
			mIntent6.putExtra("id", ""+id);
			if(s_action.contentEquals("edit"))
			{
				pref.putString(Constant.CHECKLIST_MODE,"edit");
			}else{
				pref.putString(Constant.CHECKLIST_MODE,"add");
				deleteCache(context);
			}
			mIntent6.putStringArrayListExtra("sel_item_ids_bad",
					truckfldimg);
			mIntent6.putStringArrayListExtra("sel_item_ids_good",
					truckfldgood);
			mIntent6.putExtra("count", ""+truckitemcount);
//				mIntent6.putStringArrayListExtra("sel_item_ids",
//						selectid);
			mIntent6.putStringArrayListExtra("sel_item_ids",
					selectid);
			//Log.e("13",""+truck_item_old.toString());
//				mIntent6.putStringArrayListExtra("edit_item",
//						truck_item_old);
			mIntent6.putStringArrayListExtra("edit_item",
					truckfldimg);
//				mIntent6.putStringArrayListExtra("truckfld_status",
//						truckfld_status);
			startActivityForResult(mIntent6, 6);
		}
	}
});
imgtrailer.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) {
		if (OnlineCheck.isOnline(context)) {
//				Intent mIntent7 = new Intent(AddDriverChecklist.this,
//						CheckListActivity.class);
//				mIntent7.putExtra("tag", "trailer");
//				mIntent7.putStringArrayListExtra("trailfld_status",
//						trailfld_status);
//				//Log.e("truckfldgoododzxx",""+truckfldgood.toString());
//				mIntent7.putStringArrayListExtra("sel_item_ids_good",
//						truckfldgood);
//				mIntent7.putStringArrayListExtra("sel_item_ids",
//						truckfld);
//				startActivityForResult(mIntent7, 6);

			Intent mIntent6 = new Intent(AddDriverChecklist.this,
					Trucklistview.class);
			mIntent6.putExtra("tag", "trailer");
			mIntent6.putExtra("id", ""+id);
			mIntent6.putExtra("count", ""+traileritemcount);
			if(s_action.contentEquals("edit"))
			{
				pref.putString(Constant.CHECKLIST_MODE,"edit");
			}else{
				pref.putString(Constant.CHECKLIST_MODE,"add");
				deleteCache(context);
			}
			mIntent6.putStringArrayListExtra("sel_item_ids_bad",
					truckfldtrailerimg);
			mIntent6.putStringArrayListExtra("sel_item_ids_good",
					truckfldgoodtrailer);
			mIntent6.putStringArrayListExtra("sel_item_ids",
					selectidtrailer);
			//Log.e("13",""+truck_item_old.toString());
			mIntent6.putStringArrayListExtra("edit_item",
					truckfldtrailerimg);
//				mIntent6.putStringArrayListExtra("truckfld_status",
//						truckfld_status);
			startActivityForResult(mIntent6, 6);
		}
	}
});
		cb_tick1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked)
				{


					if(traileritemcount==truckfldtrailer.size()+truckfldgoodtrailer.size()
					&& truckitemcount==truckfld.size()+truckfldgood.size()) {


						getdriversignature();
					}else if(truckitemcount!=truckfld.size()+truckfldgood.size())
					{
						itemalert("truck","Please choose all TRUCK items");
					}


					else{
						itemalert("trailer","Please choose all TRAILER items");
					}
				}
			}
		});

		mRegistrationBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// Log.e("call","........."+"fcm..............");
				// checking for type intent filter
				if (intent.getAction().equals(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION)) {
					// new push notification is received
					//handlePushNotification(intent);
					String msg = intent.getStringExtra("message");
					//  Log.e("msg","........."+msg);
					if (msg != null && msg.length() > 0 && !msg.contentEquals("null")) {

						if (msg.contentEquals("app_logout")) {
							Log.e("callv","#"+msg);
							applogout();
						}



					}
				}
			}
		};
	}

	private void findViewById() {
		// TODO Auto-generated method stub
		imgtruck=findViewById(R.id.imgtruck);
		imgtrailer=findViewById(R.id.imgtrailer);
		txttruckgood=findViewById(R.id.txt_gitem);
		txttruckbad=findViewById(R.id.txt_bitem);
		txttrailergood=findViewById(R.id.txt_trgitem);
		txttrailerbad=findViewById(R.id.txt_trbitem);
		txtconverify=findViewById(R.id.txtverify);
		txtcompname=findViewById(R.id.txt_cname);
		//spintripoption = findViewById(R.id.id_tripoption);
		txttruckno=findViewById(R.id.truck_no);
		lintks=(LinearLayout)findViewById(R.id.tks) ;
		txttl=(TextView)findViewById(R.id.addih) ;
        buy_button=findViewById(R.id.buy_button);
        txttruckcount=findViewById(R.id.txt_tcount);
        txttrailcount=findViewById(R.id.txt_tracount);
        lytLayout = (LinearLayout) findViewById(R.id.addimg);
        imgadd = (ImageView) findViewById(R.id.addplus);
		title = (TextView) findViewById(R.id.add_driver_checklist_tv_title);

		tv_date = (TextView) findViewById(R.id.add_driver_checklist_tv_date);

		tv_truck_no = (TextView) findViewById(R.id.add_driver_checklist_tv_truck_no);
		tv_truck_items = (TextView) findViewById(R.id.add_driver_checklist_tv_truck_items);
		tv_trailer_items = (TextView) findViewById(R.id.add_driver_checklist_tv_trailer_items);
		tv_trailer = (TextView) findViewById(R.id.add_driver_checklist_tv_trailer);
		tv_driver_sign = (TextView) findViewById(R.id.add_driver_checklist_tv_signDriverButton);

		tv_save = (TextView) findViewById(R.id.add_driver_checklist_tv_save_button);
		tv_submit = (TextView) findViewById(R.id.add_driver_checklist_tv_submit_button);

		et_odometer = (EditText) findViewById(R.id.add_driver_checklist_et_odometer);
		et_remark = (EditText) findViewById(R.id.add_driver_checklist_et_remark);
		et_driver_name =  findViewById(R.id.add_driver_checklist_et_driver_name);

		et_truck_other = (EditText) findViewById(R.id.add_driver_checklist_et_truck_other);
		et_trailer_other = (EditText) findViewById(R.id.add_driver_checklist_et_trailer_other);

		cb_tick1 = (CheckBox) findViewById(R.id.add_driver_checklist_cb1);

		iv_sign_driver = (ImageView) findViewById(R.id.add_driver_checklist_iv_driver_signView);

		iv_sign_driver_clear = (ImageView) findViewById(R.id.add_driver_checklist_iv_clear_driver);
		mLayout = (LinearLayout) findViewById(R.id.add_driver_checklist_ll_main);
		mLayout_timemode = (LinearLayout) findViewById(R.id.add_driver_checklist_ll_timemode);
		mLayout_truck_other = (LinearLayout) findViewById(R.id.add_driver_checklist_ll_truck_other);
		mLayout_trailer_other = (LinearLayout) findViewById(R.id.add_driver_checklist_ll_trailer_other);

		mGroup_timemode = (RadioGroup) findViewById(R.id.add_timemode);
		mButton_timemode_am = (RadioButton) findViewById(R.id.r_am);
		mButton_timemode_pm = (RadioButton) findViewById(R.id.r_pm);

		mGroup_tripmode = (RadioGroup) findViewById(R.id.add_tripmode);
		mButton_trippost = (RadioButton) findViewById(R.id.r_post);
		mButton_trippre = (RadioButton) findViewById(R.id.r_pre);


		imgphoto = (ImageView) findViewById(R.id.imgs);
		taddress = (TextView) findViewById(R.id.txt_address);
		txt_ddate = (TextView) findViewById(R.id.txt_ddate);
		linedriver = (LinearLayout) findViewById(R.id.dlinear);



		timgaddress = (TextView) findViewById(R.id.txt_imgaddress);
		txt_imgddate = (TextView) findViewById(R.id.txt_imgddate);
		lineimg = (LinearLayout) findViewById(R.id.imglinear);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {


		case R.id.add_driver_checklist_tv_trailer:
			if (OnlineCheck.isOnline(this)) {
//				Intent mIntent3 = new Intent(AddDriverChecklist.this,
//						SelectFilter.class);
				Intent mIntent3 = new Intent(AddDriverChecklist.this,
						Checklist_selectfilter.class);
				mIntent3.putExtra("tag", "trailer_d_checklist");
				mIntent3.putExtra("value", tv_trailer.getText().toString());
				mIntent3.putExtra("title", "Select Trailer");
				startActivityForResult(mIntent3, 3);
			}
			break;





		case R.id.add_driver_checklist_tv_signDriverButton:
//			Intent mIntent4 = new Intent(AddDriverChecklist.this,
//					CaptureSignature.class);
//			mIntent4.putExtra("tag", "carrier");
//			mIntent4.putExtra("class", "freight_ticket");
//			startActivityForResult(mIntent4, 4);
			drivesig=1;
			getsignature("driver");
			break;



		case R.id.add_driver_checklist_tv_truck_items:
			if (OnlineCheck.isOnline(this)) {
//				Intent mIntent6 = new Intent(AddDriverChecklist.this,
//						CheckListActivity.class);
//				mIntent6.putExtra("tag", "truck");
//				mIntent6.putStringArrayListExtra("truckfld_status",
//						truckfld_status);
//				startActivityForResult(mIntent6, 6);

				Intent mIntent6 = new Intent(AddDriverChecklist.this,
						Trucklistview.class);
				mIntent6.putExtra("tag", "truck");
				mIntent6.putExtra("id", ""+id);
				if(s_action.contentEquals("edit"))
				{
					pref.putString(Constant.CHECKLIST_MODE,"edit");
				}else{
					pref.putString(Constant.CHECKLIST_MODE,"add");
					deleteCache(context);
				}

				mIntent6.putStringArrayListExtra("sel_item_ids_good",
						truckfldgood);
				mIntent6.putExtra("count", ""+truckitemcount);
//				mIntent6.putStringArrayListExtra("sel_item_ids",
//						selectid);
				mIntent6.putStringArrayListExtra("sel_item_ids",
						selectid);
				//Log.e("13",""+truck_item_old.toString());
//				mIntent6.putStringArrayListExtra("edit_item",
//						truck_item_old);
				mIntent6.putStringArrayListExtra("edit_item",
						truckfldimg);
				mIntent6.putStringArrayListExtra("sel_item_ids_bad",
						truckfldimg);
//				mIntent6.putStringArrayListExtra("truckfld_status",
//						truckfld_status);
				startActivityForResult(mIntent6, 6);
			}
			break;


		case R.id.add_driver_checklist_tv_trailer_items:
			if (OnlineCheck.isOnline(this)) {
//				Intent mIntent7 = new Intent(AddDriverChecklist.this,
//						CheckListActivity.class);
//				mIntent7.putExtra("tag", "trailer");
//				mIntent7.putStringArrayListExtra("trailfld_status",
//						trailfld_status);
//				//Log.e("truckfldgoododzxx",""+truckfldgood.toString());
//				mIntent7.putStringArrayListExtra("sel_item_ids_good",
//						truckfldgood);
//				mIntent7.putStringArrayListExtra("sel_item_ids",
//						truckfld);
//				startActivityForResult(mIntent7, 6);

				Intent mIntent6 = new Intent(AddDriverChecklist.this,
						Trucklistview.class);
				mIntent6.putExtra("tag", "trailer");
				mIntent6.putExtra("id", ""+id);
				mIntent6.putExtra("count", ""+traileritemcount);
				if(s_action.contentEquals("edit"))
				{
					pref.putString(Constant.CHECKLIST_MODE,"edit");
				}else{
					pref.putString(Constant.CHECKLIST_MODE,"add");
					deleteCache(context);
				}
				mIntent6.putStringArrayListExtra("sel_item_ids_bad",
						truckfldtrailerimg);
				mIntent6.putStringArrayListExtra("sel_item_ids_good",
						truckfldgoodtrailer);
				mIntent6.putStringArrayListExtra("sel_item_ids",
						selectidtrailer);
				//Log.e("13",""+truck_item_old.toString());
				mIntent6.putStringArrayListExtra("edit_item",
						truckfldtrailerimg);
//				mIntent6.putStringArrayListExtra("truckfld_status",
//						truckfld_status);
				startActivityForResult(mIntent6, 6);
			}
			break;


		case R.id.add_driver_checklist_tv_save_button:
			List<Chlitem_model> mvselected=new ArrayList<>();
			Gson gson = new Gson();
			String json = pref.getString("seletecteditem");
			//Log.e("jsonnn","@"+json);
			if(json!=null && json.length()>0) {
				Type type = new TypeToken<List<Chlitem_model>>() {
				}.getType();
				mvselected = gson.fromJson(json, type);
			}
			ArrayList<String> arrdf=new ArrayList<>();
			if(mvselected.size()>0 && mvselected!=null)
			{
				for (int j=0;j<mvselected.size();j++)
				{
					Chlitem_model cl=new Chlitem_model();
					cl=mvselected.get(j);
					if(cl.getStrconvert() !=null) {

						if(arrdf.size()<=0)
						{
							arrdf.add(cl.getId()+">>"+cl.getStrconvert());
						}else{
							arrdf.add("&&"+cl.getId()+">>"+cl.getStrconvert());
						}

					}
				}
			}



			//traier

			List<Chlitem_model> mvselectedtrailer=new ArrayList<>();
			Gson gsontrailer = new Gson();
			String jsontrailer = pref.getString("seletecteditemtrailer");
			//Log.e("jsonnn","@"+json);
			if(jsontrailer!=null && jsontrailer.length()>0) {
				Type type = new TypeToken<List<Chlitem_model>>() {
				}.getType();
				mvselectedtrailer = gsontrailer.fromJson(jsontrailer, type);
			}
			ArrayList<String> arrdftrailer=new ArrayList<>();
			if(mvselectedtrailer.size()>0 && mvselectedtrailer!=null)
			{
				for (int j=0;j<mvselectedtrailer.size();j++)
				{
					Chlitem_model cl=new Chlitem_model();
					cl=mvselectedtrailer.get(j);
					if(cl.getStrconvert() !=null) {

						if(arrdftrailer.size()<=0)
						{
							arrdftrailer.add(cl.getId()+">>"+cl.getStrconvert());
						}else{
							arrdftrailer.add("&&"+cl.getId()+">>"+cl.getStrconvert());
						}

					}
				}
			}






			if(traileritemcount==truckfldtrailer.size()+truckfldgoodtrailer.size()
					&& truckitemcount==truckfld.size()+truckfldgood.size()) {

				save_driver_checklist(s_action, "Save", arrayimgstring,arrdf,arrdftrailer);

			}else if(truckitemcount!=truckfld.size()+truckfldgood.size())
			{


				itemalert("truck","Please choose all TRUCK items");
			}


			else{
				itemalert("trailer","Please choose all TRAILER items");
			}












			break;

		case R.id.add_driver_checklist_iv_back:
			finish();
			break;





		case R.id.add_driver_checklist_iv_cancel_trailer:
			tv_trailer.setText("Select Trailer");
			break;

		case R.id.add_driver_checklist_iv_clear_driver:
			iv_sign_driver.setVisibility(View.GONE);
			iv_sign_driver_clear.setVisibility(View.GONE);
			tv_driver_sign.setVisibility(View.VISIBLE);
			image_string = "";
			lat = "";
			lon = "";
			break;


		case R.id.imgs:
			lat3 = "";
			lon3 = "";
			onPhotoClick();
			break;

		}

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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//Log.e("valllzz", "@nvb" );
		//Log.e("requestCodek", "@" + requestCode);
		//Log.e("resultCodek", "@" + resultCode);
		Uri selectedImageUri = null;
		if (resultCode == RESULT_OK) {
			//Log.e("ok","ok");
		} else {
			//Log.e("res","damn - its not working");
		}
		//Log.e("requesrcc","@@@"+requestCode);
		//Log.e("data","@@@"+data.toString());
		switch (requestCode) {
			case 203:
				if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
					CropImage.ActivityResult result = CropImage.getActivityResult(data);
					if (resultCode == RESULT_OK) {
imgval++;
						try {
							InputStream iStream = getContentResolver().openInputStream(result.getUri());
							img = getBytes(iStream);
							convertstring(img);
                            String vb=pref.getString(Constant.DLIST_STATUS);
                            if(vb.contentEquals("add")) {

                                arrayimg.add(result.getUri());
								setimagenew();
                            }


							//(imgprofile).setImageURI(result.getUri());
							//uploadphoto();


						} catch (Exception e) {

						}
						//  Log.e("ddd",""+result.getUri().toString());
						// decodeFile(result.getUri().toString());
					}

					 else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
						Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
					}}
				break;

		case 1:


		case 2:
			String str2 = sharedpreferences.getString("truck_f_ticket",
					"Select Truck");
			if(str2==null || str2.length()==0 || str2.contentEquals("null"))
			{
				str2="Select Truck";
			}
			tv_truck_no.setText(str2);
			break;

		case 3:
			String str3 = sharedpreferences.getString("trailer_d_checklist",
					"Select Trailer");
			if(str3==null || str3.length()==0 || str3.contentEquals("null"))
			{
				str3="Select Trailer";
			}
			tv_trailer.setText(str3);
			break;

		case 4:
			if (resultCode == RESULT_OK) {
				Bundle bundle_car = data.getExtras();
				if(bundle_car !=null) {
					if (bundle_car.containsKey("status")) {
						String status = bundle_car.getString("status");
						if (status.equalsIgnoreCase("done")) {

							iv_sign_driver.setVisibility(View.VISIBLE);
							iv_sign_driver_clear.setVisibility(View.VISIBLE);
							tv_driver_sign.setVisibility(View.GONE);

							lat = bundle_car.getString("lat");
							lon = bundle_car.getString("lon");//pp
							//Log.e("lat",""+lat);
							//Log.e("lon",""+lon);
							image_string = bundle_car.getString("image_string");

							Double d = 0.00;
							Double d2 = 0.00;

							d = Double.parseDouble("" + lat);
							d2 = Double.parseDouble("" + lon);
							getAddressFromLocation(d,d2);
//							Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//							List<Address> addresses = null;
//
//							try {
//								addresses = geocoder.getFromLocation(d, d2, 1);
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//								//Log.e("e",""+e.toString());
//							}
							// Log.e("addresses", "/"+addresses.toString() +"/"+d+"/"+d2);

//							if (addresses != null && addresses.size() > 0) {
//								String cityName = addresses.get(0).getAddressLine(0);
//								//Log.e("cityName", "/"+cityName +"/"+d+"/"+d2);
//								String stateName = addresses.get(0).getAddressLine(1);
//								//Log.e("stateName", "/"+stateName +"/"+d+"/"+d2);
//								String countryName = addresses.get(0).getAddressLine(2);
//								//Log.e("countryName", "/"+countryName +"/"+d+"/"+d2);
//								String country = addresses.get(0).getAddressLine(3);
//								//Log.e("country", "/"+country +"/"+d+"/"+d2);
//
//								if(cityName !=null && cityName.length()>0 && stateName !=null && stateName.length()>0
//										&& countryName !=null && countryName.length()>0 &&
//										country!=null && country.length()>0)
//								{
//									taddress.setText(cityName + "," + stateName + ","
//											+ countryName + "," + country);
//								}else 	if(cityName !=null && cityName.length()>0 && stateName !=null && stateName.length()>0
//										&& countryName !=null && countryName.length()>0 )
//								{
//									taddress.setText(cityName + "," + stateName + ","
//											+ countryName);
//								}else 	if(cityName !=null && cityName.length()>0 && stateName !=null && stateName.length()>0)
//								{
//									taddress.setText(cityName + "," + stateName + ","
//											+ countryName);
//								}else 	if(cityName !=null && cityName.length()>0)
//								{
//									taddress.setText(cityName );
//								}
//
//							}
							taddress.setText(""+straddress );
							byte[] imageAsBytes = Base64.decode(
									image_string.getBytes(), Base64.DEFAULT);
							str_driversig = Base64.encodeToString(imageAsBytes,
									Base64.DEFAULT);
							iv_sign_driver.setImageBitmap(BitmapFactory
									.decodeByteArray(imageAsBytes, 0,
											imageAsBytes.length));
						}
					}
				}
			}
			break;


		case 6:

			if (resultCode == RESULT_OK) {

				Bundle bundle = data.getExtras();
				String tagg = bundle.getString("tag");
				Log.e("tagg","@"+tagg);

				if (tagg.equalsIgnoreCase("truck")) {


                    truckfld = new ArrayList<String>();
                    truckfld_status = new ArrayList<String>();
					truckfldgood = new ArrayList<>();
                    truckfld.addAll(bundle.getStringArrayList("sel_item_ids"));
					if(bundle.getStringArrayList("truck_item_old") !=null) {
						truckfldimg=new ArrayList<>();
						truckfldimg.addAll(bundle.getStringArrayList("truck_item_old"));
						//Log.e("truckfldimg",""+truckfldimg.toString());

					}
                    if(bundle.getStringArrayList("sel_item_ids_good") !=null) {
						truckfldgood.addAll(bundle.getStringArrayList("sel_item_ids_good"));
						txttruckgood.setText("Good condition item selected  : "+String.valueOf(truckfldgood.size())+"/"+truckitemcount);

					}else
					{
						truckfldgood=new ArrayList<>();
						txttruckgood.setText("Good condition item selected  : 0/"+truckitemcount);

					}
                    for (int i = 0; i < truckfld.size(); i++) {
                        if (truckfld.get(i).equalsIgnoreCase("other_id")) {
                            check_truck_ids = true;
                            truckfld.remove(i);
                        } else {
                            check_truck_ids = false;
                        }
                    }
                    if (check_truck_ids) {
                        mLayout_truck_other.setVisibility(View.VISIBLE);
                        tv_truck_items
                                .setText(String.valueOf(truckfld.size() + 1)
                                        + " items selected");
                        txttruckbad.setText("Bad condition item selected  : "+String.valueOf(truckfld.size() + 1)+"/"+truckitemcount);
                    } else {
                        mLayout_truck_other.setVisibility(View.GONE);
                        tv_truck_items.setText(String.valueOf(truckfld.size())
                                + " items selected");
						txttruckbad.setText("Bad condition item selected  : "+String.valueOf(truckfld.size())+"/"+truckitemcount);

					}








					if(truckfld.size()>0 || truckfldtrailer.size()>0)
					{
						txtconverify.setTextColor(getResources().getColor(R.color.Red));
						cb_tick1.setChecked(false);
						cb_tick1.setEnabled(false);
						tv_save.setText("SAVE");
					}else{
						txtconverify.setTextColor(getResources().getColor(R.color.gree));
						cb_tick1.setEnabled(true);
						tv_save.setText("SUBMIT");
					}













//					truckfld = new ArrayList<String>();
//					truckfld_status = new ArrayList<String>();
//					truckfld.addAll(bundle.getStringArrayList("sel_item_ids"));
//					truckfld_status.addAll(bundle
//							.getStringArrayList("checked_status_array"));
//					for (int i = 0; i < truckfld.size(); i++) {
//						if (truckfld.get(i).equalsIgnoreCase("other_id")) {
//							check_truck_ids = true;
//							truckfld.remove(i);
//						} else {
//							check_truck_ids = false;
//						}
//					}
//					if (check_truck_ids) {
//						mLayout_truck_other.setVisibility(View.VISIBLE);
//						tv_truck_items
//								.setText(String.valueOf(truckfld.size() + 1)
//										+ " items selected");
//					} else {
//						mLayout_truck_other.setVisibility(View.GONE);
//						tv_truck_items.setText(String.valueOf(truckfld.size())
//								+ " items selected");
//					}

				} else if (tagg.equalsIgnoreCase("trailer")) {

					truckfldtrailer = new ArrayList<String>();
					truckfld_statustrailer = new ArrayList<String>();
					truckfldgoodtrailer = new ArrayList<>();
					truckfldtrailer.addAll(bundle.getStringArrayList("sel_item_ids"));


					if(bundle.getStringArrayList("truck_item_old") !=null) {
						truckfldtrailerimg=new ArrayList<>();
						truckfldtrailerimg.addAll(bundle.getStringArrayList("truck_item_old"));
						//Log.e("truckfldimg",""+truckfldimg.toString());

					}
					if(bundle.getStringArrayList("sel_item_ids_good") !=null) {
						truckfldgoodtrailer.addAll(bundle.getStringArrayList("sel_item_ids_good"));
						txttrailergood.setText("Good condition item selected  : "+String.valueOf(truckfldgoodtrailer.size())+"/"+traileritemcount);

					}else
					{
						txttrailergood.setText("Good condition item selected  : 0/"+traileritemcount);

						truckfldgoodtrailer=new ArrayList<>();
					}
					for (int i = 0; i < truckfldtrailer.size(); i++) {
						if (truckfldtrailer.get(i).equalsIgnoreCase("other_id")) {
							check_trailer_ids = true;
							truckfldtrailer.remove(i);
						} else {
							check_trailer_ids = false;
						}
					}
					if (check_trailer_ids) {
						mLayout_trailer_other.setVisibility(View.VISIBLE);
						tv_trailer_items
								.setText(String.valueOf(truckfldtrailer.size() + 1)
										+ " items selected");
						txttrailerbad.setText("Bad condition item selected  : "+String.valueOf(truckfldtrailer.size() + 1)+"/"+traileritemcount);

					} else {
						mLayout_trailer_other.setVisibility(View.GONE);
						tv_trailer_items.setText(String.valueOf(truckfldtrailer.size())
								+ " items selected");
						txttrailerbad.setText("Bad condition item selected  : "+String.valueOf(truckfldtrailer.size() )+"/"+traileritemcount);

					}

//j
					if(truckfld.size()>0 || truckfldtrailer.size()>0)
					{
						txtconverify.setTextColor(getResources().getColor(R.color.Red));
						cb_tick1.setChecked(false);
						cb_tick1.setEnabled(false);
						tv_save.setText("SAVE");
					}else{
						txtconverify.setTextColor(getResources().getColor(R.color.gree));
						cb_tick1.setEnabled(true);
						tv_save.setText("SUBMIT");
					}



//					trailfld = new ArrayList<String>();
//					trailfld_status = new ArrayList<String>();
//					trailfld.addAll(bundle.getStringArrayList("sel_item_ids"));
//					trailfld_status.addAll(bundle
//							.getStringArrayList("checked_status_array"));
//					for (int i = 0; i < trailfld.size(); i++) {
//						if (trailfld.get(i).equalsIgnoreCase("other_id")) {
//							check_trailer_ids = true;
//							trailfld.remove(i);
//						} else {
//							check_trailer_ids = false;
//						}
//					}
//					if (check_trailer_ids) {
//						mLayout_trailer_other.setVisibility(View.VISIBLE);
//						tv_trailer_items
//								.setText(String.valueOf(trailfld.size() + 1)
//										+ " items selected");
//					} else {
//						mLayout_trailer_other.setVisibility(View.GONE);
//						tv_trailer_items
//								.setText(String.valueOf(trailfld.size())
//										+ " items selected");
//					}

				}
			}
			break;

		case PICK_FROM_CAMERA:
			doCrop();
			break;

		case PICK_FROM_FILE:
			if (Build.VERSION.SDK_INT < 19) {
				mImageCaptureUri = data.getData();
			} else {
				if (data != null) {
					mImageCaptureUri = data.getData();
					ParcelFileDescriptor parcelFileDescriptor;
					try {
						parcelFileDescriptor = getApplicationContext()
								.getContentResolver().openFileDescriptor(
										mImageCaptureUri, "r");
						FileDescriptor fileDescriptor = parcelFileDescriptor
								.getFileDescriptor();
						if (bitmap != null) {
							bitmap.recycle();
						}
						bitmap = null;
						bitmap = BitmapFactory
								.decodeFileDescriptor(fileDescriptor);
						parcelFileDescriptor.close();
					} catch (Exception e) {
						//Log.e("Error", e.toString());
					}
				}
			}
			//doCrop();
			try {
				ImageCropFunction();
			}catch (RuntimeException e)
			{

			}
			break;

		case CROP_FROM_CAMERA:

			if (data != null) {
                if (mImageCaptureUri != null) {
                    File f = new File(mImageCaptureUri.getPath());
                    if(f!=null) {
                        if (f.exists())
                            f.delete();
                    }
                }
                //Log.e("mimagecrop",""+mImageCropUri);
                //Log.e("mImageCaptureUri",""+mImageCaptureUri);
                if (mImageCropUri != null) {
                    File f = new File(mImageCropUri.getPath());
                    Bitmap bitmap = BitmapFactory.decodeFile(f
                            .getAbsolutePath());
                    //Log.e("bitmap",""+bitmap);
                    if(bitmap !=null) {
                        imgphoto.setImageBitmap(bitmap);
                        // Bitmap bmp=bitmap;
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        image = stream.toByteArray();
                        updatedStringimage = Base64.encodeToString(image,
                                Base64.DEFAULT);
                        image_string3 = Base64
                                .encodeToString(image, Base64.DEFAULT);
                    }else{
                        if (data != null) {

                            Bundle bundle = data.getExtras();

                            Bitmap bitmapd = bundle.getParcelable("data");
                            //Log.e("bitmapd+++",""+bitmapd);

                            imgphoto.setImageBitmap(bitmapd);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmapd.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            image = stream.toByteArray();
                            updatedStringimage = Base64.encodeToString(image,
                                    Base64.DEFAULT);
                            image_string3 = Base64
                                    .encodeToString(image, Base64.DEFAULT);

                        }
                    }


//					if (mImageCaptureUri != null) {
//						File fd = new File(mImageCaptureUri.getPath());
//						if (fd.exists())
//							fd.delete();
//					}
                    // uploadimage(updatedStringimage);

                    // ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    // if(bos.size()>0)
                    // {
                    // bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    // }
                    // img = bos.toByteArray();

//
                    if(f!=null) {
                        if (f.exists()) {
                            f.delete();
                        }
                    }

					CheckGpsStatus();
					if (!GpsStatus) {
						getMyLocation(context);
					}

                    Double d = 0.00;
                    Double d2 = 0.00;
if(lat3 !=null && lat3.length()>0) {
	d = Double.parseDouble("" + lat3);
	d2 = Double.parseDouble("" + lon3);
	getAddressFromLocation(d, d2);
	taddress.setText("" + straddress);
}
                }else{

                    File f = new File(mImageCaptureUri.getPath());
                    Bitmap bitmap = BitmapFactory.decodeFile(f
                            .getAbsolutePath());
                    //Log.e("bitmap",""+bitmap);
                    if(bitmap !=null) {
                        imgphoto.setImageBitmap(bitmap);
                        // Bitmap bmp=bitmap;
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        image = stream.toByteArray();
                        updatedStringimage = Base64.encodeToString(image,
                                Base64.DEFAULT);
                        image_string3 = Base64
                                .encodeToString(image, Base64.DEFAULT);
                    }
                    // uploadimage(updatedStringimage);

                    // ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    // if(bos.size()>0)
                    // {
                    // bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    // }
                    // img = bos.toByteArray();
                    if (f.exists()) {
                        f.delete();
                    }

					CheckGpsStatus();
					if (!GpsStatus) {
						getMyLocation(context);
					}

                    Double d = 0.00;
                    Double d2 = 0.00;

                    d = Double.parseDouble("" + lat3);
                    d2 = Double.parseDouble("" + lon3);
                    getAddressFromLocation(d,d2);


					taddress.setText(""+straddress );
                }

                // uploadphoto();
            }
			break;

			case RESULT_LOAD_IMAGE:
				if (resultCode == RESULT_OK) {
					selectedImageUri = data.getData();

					if (selectedImageUri != null) {
						try {
							// OI FILE Manager
							String filemanagerstring = selectedImageUri.getPath();

							// MEDIA GALLERY
							String selectedImagePath = getPath(selectedImageUri);

							if (selectedImagePath != null) {
								filePath = selectedImagePath;
							} else if (filemanagerstring != null) {
								filePath = filemanagerstring;
							} else {
								Toast.makeText(getApplicationContext(),
										"Unknown path", Toast.LENGTH_LONG).show();
								//Log.e("Bitmap", "Unknown path");
							}

							if (filePath != null) {
								decodeFile(filePath);

							} else {
								bitmap = null;
								Toast.makeText(getApplicationContext(),
										"Retake Image", Toast.LENGTH_LONG).show();
							}

						} catch (Exception e) {
							Toast.makeText(getApplicationContext(), "Retake Image",
									Toast.LENGTH_LONG).show();
							//Log.e(e.getClass().getName(), e.getMessage(), e);
						}
					} else {
						Toast.makeText(getApplicationContext(), "Retake Image",
								Toast.LENGTH_LONG).show();
					}
				}
				break;


		}

	}
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.MediaColumns.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {

			int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	public void decodeFile(String filePath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
		Bitmap scaledBitmap = null;
		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;

//		      max Height and width values of the compressed image is taken as 816x612

		float maxHeight = 612.0f;
		float maxWidth = 816.0f;
		float imgRatio = actualWidth / actualHeight;
		float maxRatio = maxWidth / maxHeight;

//		      width and height values are set maintaining the aspect ratio of the image

		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {               imgRatio = maxHeight / actualHeight;                actualWidth = (int) (imgRatio * actualWidth);               actualHeight = (int) maxHeight;             } else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;

			}
		}
		// ////////////////////////////////////////////////////////////////////////

		// BitmapFactory.Options o2 = new BitmapFactory.Options();
		//  setting inSampleSize value allows to load a scaled down version of the original image

		options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//		      inJustDecodeBounds set to false to load the actual bitmap
		options.inJustDecodeBounds = false;

//		      this options allow android to claim the bitmap memory if it runs low on memory
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16 * 1024];

		try {
//		          load the bitmap from its path
			bmp = BitmapFactory.decodeFile(filePath, options);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();

		}
		try {
			scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();
		}

		float ratioX = actualWidth / (float) options.outWidth;
		float ratioY = actualHeight / (float) options.outHeight;
		float middleX = actualWidth / 2.0f;
		float middleY = actualHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//		      check the rotation of the image and display it properly
		ExifInterface exif;
		try {
			exif = new ExifInterface(filePath);

			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, 0);

			Matrix matrix = new Matrix();
			if (orientation == 6) {
				matrix.postRotate(90);

			} else if (orientation == 3) {
				matrix.postRotate(180);

			} else if (orientation == 8) {
				matrix.postRotate(270);

			}


			bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
					scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
					true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		byte[] data = bos.toByteArray();
		imgphoto.setImageBitmap(bitmap);
		image = bos.toByteArray();
		updatedStringimage = Base64.encodeToString(image, Base64.DEFAULT);
		image_string3 = Base64
				.encodeToString(image, Base64.DEFAULT);

	}



	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }       final float totalPixels = width * height;       final float totalReqPixelsCap = reqWidth * reqHeight * 2;       while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
			inSampleSize++;
		}

		return inSampleSize;
	}


//	protected void edit_checklist(JSONArray response) {
//		// TODO Auto-generated method stub
//		// Log.e("sdk", ""+response.toString());
//		try {
//			if (response.length() > 0) {
//				mLayout.setVisibility(View.VISIBLE);
//				for (int i = 0; i < response.length(); i++) {
//
//					JSONObject mResultJsonObject = response.getJSONObject(i);
//
//					String status = mResultJsonObject.getString("status");
//					if (status.equalsIgnoreCase("1")) {
//						String id = mResultJsonObject.getString("id");
//
//						String Date_Time = mResultJsonObject
//								.getString("Date_Time");
//
//						if(Date_Time !=null && Date_Time.length()>0 && !Date_Time.contentEquals("null")) {
//							// if(formattedDate.contains(" "))
//							// {
//							//tv_date.setText(""+Date_Time);
//							StringTokenizer stt = new StringTokenizer(Date_Time, " ");
//							String myDatez = "" + stt.nextToken();
//							if(stt.hasMoreTokens())
//							{
//								String stime=stt.nextToken();
//
//								try {
//									final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
//									final Date dateObj = sdf.parse(stime);
//									String fg=""+new SimpleDateFormat("h:mm").format(dateObj);
//									tv_date.setText(""+myDatez+" "+fg);
//									String starttime=""+new SimpleDateFormat("h:mma").format(dateObj);
//
//									//Log.e("starttime",""+starttime);
//
//									if (starttime.contains("pm")|| starttime.contains("PM")) {
//										mLayout_timemode.setVisibility(View.VISIBLE);
//										mButton_timemode_pm.setChecked(true);
//									} else {
//										mLayout_timemode.setVisibility(View.VISIBLE);
//										mButton_timemode_am.setChecked(true);
//									}
//
//								} catch (final ParseException e) {
//									e.printStackTrace();
//								}
//							}
//						}
//
//
//
//						String Truck_id = mResultJsonObject
//								.getString("Truck_id");
//						String Truck_No = mResultJsonObject
//								.getString("Truck_No");
//
//						if (!(Truck_id.equalsIgnoreCase("null") || Truck_No
//								.equalsIgnoreCase("null"))) {
//							editor.putString("truck_f_ticket", Truck_No);
//							editor.putString("truck_f_ticket_id", Truck_id);
//							editor.commit();
//							tv_truck_no.setText(Truck_No);
//						} else
//							tv_truck_no.setText("Select Truck");
//						String Odometer_Reading = mResultJsonObject
//								.getString("Odometer_Reading");
//						et_odometer.setText(Odometer_Reading);
//						String Trailer_id = mResultJsonObject
//								.getString("Trailer_id");
//						String Trailer_NO = mResultJsonObject
//								.getString("Trailer_NO");
//						if (!(Trailer_id.equalsIgnoreCase("null") || Trailer_NO
//								.equalsIgnoreCase("null"))) {
//							editor.putString("trailer_d_checklist", Trailer_NO);
//							editor.putString("trailer_d_checklist_id",
//									Trailer_id);
//							editor.commit();
//							tv_trailer.setText(Trailer_NO);
//						} else
//							tv_trailer.setText("Select Trailer");
//						String Remark = mResultJsonObject.getString("Remark");
//						et_remark.setText(Remark);
//						String CONDITION_OF_THE = mResultJsonObject
//								.getString("CONDITION_OF_THE");
//						if (CONDITION_OF_THE.equalsIgnoreCase("1")) {
//							cb_tick1.setChecked(true);
//						}
//						String Driver_Name = mResultJsonObject
//								.getString("Driver_Name");
//						et_driver_name.setText(Driver_Name);
//						String Driver_Signature = mResultJsonObject
//								.getString("Driver_Signature");
//						image_string = Driver_Signature;
//						if (Driver_Signature.length() > 0) {
//							iv_sign_driver.setVisibility(View.VISIBLE);
//							iv_sign_driver_clear.setVisibility(View.VISIBLE);
//							tv_driver_sign.setVisibility(View.GONE);
//							str_driversig=Driver_Signature;
//							Picasso.with(getApplicationContext())
//									.load(Driver_Signature)
//									.into(iv_sign_driver);
//							String Driver_date = mResultJsonObject
//									.getString("Driver_date");
//
//							if (!(Driver_date.equalsIgnoreCase("null"))) {
//								if (Driver_date.length() > 0) {
//									linedriver.setVisibility(View.VISIBLE);
//									txt_ddate.setText(Driver_date);
//								}
//							}
//
//							String Driver_address = mResultJsonObject
//									.getString("Driver_address");
//
//							if (!(Driver_address.equalsIgnoreCase("null"))
//									&& !(Driver_address
//											.equalsIgnoreCase("false"))) {
//								if (Driver_address.length() > 0) {
//									taddress.setText(Driver_address);
//								}
//							}
//
//						}
//
//						String Driver_lat = mResultJsonObject
//								.getString("Driver_lat");
//						lat = Driver_lat;
//						String Driver_long = mResultJsonObject
//								.getString("Driver_long");
//						lon = Driver_long;
//
//
//
//
//
//
//						String photos = mResultJsonObject
//								.getString("image_photo");
//						String photoaddress = mResultJsonObject
//								.getString("imgaddress");
//						String imgdate = mResultJsonObject.getString("imgdate");
//
//					//	Log.e("photos", "" + photos);
//
//						if (photos.length() > 0) {
//							//imgphoto.setVisibility(View.VISIBLE); mimage kf
//
//							// imgphoto.setClickable(false);
//							Picasso.with(getApplicationContext()).load(photos)
//									.into(imgphoto);
//						} else {
//							// imgphoto.setVisibility(View.GONE);
//						}
//
//						if (!(imgdate.equalsIgnoreCase("null"))
//								&& !(imgdate.equalsIgnoreCase("false"))) {
//							if (imgdate.length() > 0) {
//								// lineimg.setVisibility(View.VISIBLE);
//								txt_imgddate.setText(imgdate);
//							}
//						}
//
//						if (!(photoaddress.equalsIgnoreCase("null"))
//								&& !(photoaddress.equalsIgnoreCase("false"))) {
//							if (photoaddress.length() > 0) {
//								lineimg.setVisibility(View.VISIBLE);
//								timgaddress.setText(photoaddress);
//							}
//						}
////gh
////						//truck item image az
////						try {
////							JSONArray tr_item_image = mResultJsonObject
////									.getJSONArray("item_img");
////							//Log.e("tr_item_image",""+tr_item_image.toString());
////							truck_item_old = new ArrayList<>();
////							if (tr_item_image != null) {
////								if (tr_item_image.length() > 0) {
////
////									for (int j = 0; j < tr_item_image.length(); j++) {
////										JSONObject mResultJsonimg = tr_item_image
////												.getJSONObject(j);
////										String imgid = mResultJsonimg
////												.getString("img_id");
////										selectid.add(imgid);
////										String imgurl = mResultJsonimg
////												.getString("img_url");
////										truck_item_old.add(imgid + ">>" + imgurl);
////										//Log.e("12",""+truck_item_old.toString());
////									}
////
////
////								}
////							}
////						}catch (Exception e)
////						{
////
////						}
//                        if (check_truck_ids) {
//                            mLayout_truck_other.setVisibility(View.VISIBLE);
//                            tv_truck_items
//                                    .setText(String.valueOf(selectid.size() + 1)
//                                            + " items selected");
//                        } else {
//                            mLayout_truck_other.setVisibility(View.GONE);
//                            tv_truck_items.setText(String.valueOf(selectid.size())
//                                    + " items selected");
//                        }
//
//
//try {
//	//multiple image
//	JSONArray multi_img = mResultJsonObject
//			.getJSONArray("truck_img");
//	arrayimgold = new ArrayList<>();
//	if (multi_img != null && multi_img.length() > 0) {
//		if (multi_img.length() > 0) {
//
//			for (int j = 0; j < multi_img.length(); j++) {
//				JSONObject mResultJsonimg = multi_img
//						.getJSONObject(j);
//				String imgid = mResultJsonimg
//						.getString("img_id");
//				String imgurl = mResultJsonimg
//						.getString("img_url");
//				arrayimgold.add(imgid + ">>" + imgurl);
//			}
//
//			setimageold();
//		}
//	}
//}catch (Exception e)
//{
//
//}
//
//
//
//
//						//end
//
//						JSONArray truck_item_details = mResultJsonObject
//								.getJSONArray("truck_item_details");
//						truckfldimg = new ArrayList<String>();
//						truckfld_status = new ArrayList<String>();
//						truckfldgood=new ArrayList<>();
//						if (truck_item_details != null) {
//							if (truck_item_details.length() > 0) {
//
//								for (int j = 0; j < truck_item_details.length(); j++) {
//									JSONObject mResultJsonObject2 = truck_item_details
//											.getJSONObject(j);
//									String truck_item_status = mResultJsonObject2
//											.getString("truck_item_status");
//									truckfld_status.add(truck_item_status);
//									String truck_id = mResultJsonObject2
//											.getString("truck_id");
//									String imgurl = mResultJsonObject2
//											.getString("img_url");
//									if (truck_item_status.equalsIgnoreCase("1")) {
//										truckfldimg.add(truck_id+">>"+imgurl);
//										truckfld.add(truck_id);
//									}else if (truck_item_status.equalsIgnoreCase("2")) {
//										truckfldgood.add(truck_id);
//
//									}
////									String truck_item_name = mResultJsonObject2
////											.getString("truck_item_name");
//									// String truck_sub_item_name =
//									// mResultJsonObject2.getString("truck_sub_item_name");
//								}
//							}
//							//Log.e("truck ","ok");
//						}
//
////						Log.e("truck ","fail");
////
////
////						try{
////							//truck item image az
////							JSONArray tr_item_image = mResultJsonObject
////									.getJSONArray("itemtrailer_img");
////							//Log.e("tr_item_image",""+tr_item_image.toString());
////							truck_item_oldtrailer=new ArrayList<>();
////							if (tr_item_image != null) {
////								if (tr_item_image.length() > 0) {
////
////									for (int j = 0; j < tr_item_image.length(); j++) {
////										JSONObject mResultJsonimg = tr_item_image
////												.getJSONObject(j);
////										String imgid = mResultJsonimg
////												.getString("img_id");
////										selectidtrailer.add(imgid);
////										String imgurl = mResultJsonimg
////												.getString("img_url");
////										truck_item_oldtrailer.add(imgid+">>"+imgurl);
////										//Log.e("12",""+truck_item_old.toString());
////									}
////
////
////								}
////							}
////						}catch (Exception e)
////						{
////
////						}
////
//
//
//
//
//
//
//
//
//						JSONArray trailer_item_details = mResultJsonObject
//								.getJSONArray("trailer_item_details");
//						//Log.e("trailer_item_details","@"+trailer_item_details.toString());
//						truckfldtrailerimg = new ArrayList<String>();
//						trailfld_status = new ArrayList<String>();
//						truckfldgoodtrailer=new ArrayList<>();
//						if (trailer_item_details != null) {
//							if (trailer_item_details.length() > 0) {
//
//								for (int j = 0; j < trailer_item_details
//										.length(); j++) {
//									JSONObject mResultJsonObject2 = trailer_item_details
//											.getJSONObject(j);
//									String trailer_item_status = mResultJsonObject2
//											.getString("trailer_item_status");
//									trailfld_status.add(trailer_item_status);
//									String trailer_id = mResultJsonObject2
//											.getString("trailer_id");
//									String imgurl = mResultJsonObject2
//											.getString("img_url");
//									if (trailer_item_status
//											.equalsIgnoreCase("1")) {
//										truckfldtrailerimg.add(trailer_id+">>"+imgurl);
//										truckfldtrailer.add(trailer_id);
//									}else if (trailer_item_status
//											.equalsIgnoreCase("2")) {
//										truckfldgoodtrailer.add(trailer_id);
//
//									}
////									String trailer_item_name = mResultJsonObject2
////											.getString("trailer_item_name");
//
//								}
//							}
//						}
//
//						String truck_other_status = mResultJsonObject
//								.getString("truck_other_status");
//						if (truck_other_status.equalsIgnoreCase("0")) {
//							truckfld_status.add("0");
//							mLayout_truck_other.setVisibility(View.GONE);
//							tv_truck_items.setText(truckfld.size()
//									+ " items selected");
//						} else {
//							truckfld_status.add("1");
//							mLayout_truck_other.setVisibility(View.VISIBLE);
//							String truck_other = mResultJsonObject
//									.getString("truck_other");
//							et_truck_other.setText(truck_other);
//							tv_truck_items.setText((truckfld.size() + 1)
//									+ " items selected");
//
//						}
//						if(truckfld.size()>0 || truckfldtrailer.size()>0)
//						{
//							txtconverify.setTextColor(getResources().getColor(R.color.Red));
//							cb_tick1.setChecked(false);
//							cb_tick1.setEnabled(false);
//							tv_save.setText("SAVE");
//						}else{
//							txtconverify.setTextColor(getResources().getColor(R.color.gree));
//							cb_tick1.setEnabled(true);
//							tv_save.setText("SUBMIT");
//						}
//						strtruckcount=mResultJsonObject.getString("truckcount");
//						txttruckcount.setText(""+strtruckcount+" Items");
//						strtrailercount=mResultJsonObject.getString("trailercount");
//						txttrailcount.setText(""+strtrailercount+" Items");
//
//
//						String trailer_other_status = mResultJsonObject
//								.getString("trailer_other_status");
//						if (trailer_other_status.equalsIgnoreCase("0")) {
//							trailfld_status.add("0");
//							mLayout_trailer_other.setVisibility(View.GONE);
//							tv_trailer_items.setText(truckfldtrailer.size()
//									+ " items selected");
//						} else {
//							trailfld_status.add("1");
//							mLayout_trailer_other.setVisibility(View.VISIBLE);
//							String trailer_other = mResultJsonObject
//									.getString("trailer_other");
//							tv_trailer_items.setText((truckfldtrailer.size() + 1)
//									+ " items selected");
//							et_trailer_other.setText(trailer_other);
//
//						}
//
//					}
//				}
//
//			}
//		} catch (Exception e) {
//			//Log.e("exccccccccccc",""+e.toString());
//			// TODO: handle exception
//		}
//
//	}

	private void save_driver_checklist(String action, String submit, ArrayList<String> arrayimgstring, ArrayList<String> arrdf, ArrayList<String> arrdftrailers) {


		if (lat != null) {
			straddress = getAddressFromLocation(Double.parseDouble(lat), Double.parseDouble(lon));
		}
		String vb=pref.getString(Constant.DLIST_STATUS);
		String typech="edit";
		if(vb.contentEquals("add"))
		{
		}else{
			typech="add";
			arrayimg=new ArrayList<>();
			arrdf=new ArrayList<>();



		}
//Log.e("kd",""+datess);
		String date = tv_date.getText().toString();
		String dateperiod = getRadioItem();


		String vehsat = "0";
		if (cb_tick1.isChecked()) {
			vehsat = "1";
		}
		String drvname = et_driver_name.getText().toString();
		String driverimage = image_string;
		String driverlat = lat;
		String driverlong = lon;
		String defcorr = "0";

		String safeope = "0";
//		if (cb_tick3.isChecked()) {
//			safeope = "1";
//		}

		String other;
		if (et_truck_other.getVisibility() == View.VISIBLE) {
			other = et_truck_other.getText().toString().trim();
		} else {
			other = "";
		}
		String traother;
		if (et_truck_other.getVisibility() == View.VISIBLE) {
			traother = et_trailer_other.getText().toString().trim();
		} else {
			traother = "";
		}


		if (dialog != null && dialog.isShowing()) {

		} else {
			dialog = new ProgressDialog(AddDriverChecklist.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setMessage("Please wait.........");

			dialog.setIndeterminate(false);
			dialog.show();
		}
		api = DispatchServiceGenerator.createService(Eld_api.class,context);
		Map<String, String> params = new HashMap<>();
		String tvalid = "";
		for (int i = 0; i < truckfld.size(); i++) {
			if (tvalid == "" || tvalid.length() == 0) {
				tvalid = truckfld.get(i);
			} else {
				tvalid += "==" + truckfld.get(i);
			}
		}

		String truckvalid = "";

		for (int i = 0; i < truckfldgood.size(); i++) {
			if (truckvalid == "" || truckvalid.length() == 0) {
				truckvalid = truckfldgood.get(i);
			} else {
				truckvalid += "==" + truckfldgood.get(i);
			}
		}

//trailer defect
		String trailervalid = "";

		for (int i = 0; i < truckfldgoodtrailer.size(); i++) {
			if (trailervalid == "" || trailervalid.length() == 0) {
				trailervalid = truckfldgoodtrailer.get(i);
			} else {
				trailervalid += "==" + truckfldgoodtrailer.get(i);
			}
		}

		String trailernotvalid = "";

		for (int i = 0; i < truckfldtrailer.size(); i++) {
			if (trailernotvalid == "" || trailernotvalid.length() == 0) {
				trailernotvalid = truckfldtrailer.get(i);
			} else {
				trailernotvalid += "==" + truckfldtrailer.get(i);
			}
		}




		try {

				androidId = Settings.Secure.getString(getContentResolver(),
						Settings.Secure.ANDROID_ID);

		}catch (Exception e)
		{

		}







		if(  str_driversig != null && str_driversig.length()>0 &&  !str_driversig.contentEquals("null"))
		{
		}else
		{
			Toast.makeText(context,"Please Sign",Toast.LENGTH_SHORT).show();
		}

		if(str_driversig !=null && str_driversig.length()>0 && !str_driversig.contentEquals("null")) {
			api = ApiServiceGenerator.createService(Eld_api.class);
			String truck = "";
//			if (!(tv_truck_no.getText().toString().equalsIgnoreCase("Select Truck"))) {
//				truck = sharedpreferences.getString("truck_f_ticket_id", "");
//			}
			if(pref.getString(Constant.VID_NUMBER) !=null && pref.getString(Constant.VID_NUMBER).length()>0)
			{
				truck=pref.getString(Constant.VID_NUMBER);
			}

//Log.e("truck","@"+truck);
			String odoreading = et_odometer.getText().toString();
			String trailer = "";
			if (!(tv_trailer.getText().toString()
					.equalsIgnoreCase("Select Trailer"))) {
				trailer = sharedpreferences.getString("trailer_d_checklist_id", "");
			}

			String remark = et_remark.getText().toString();

			//strTrip=getTripItem();
			strTrip=getTripItem();
			params.put("cc", pref.getString(Constant.COMPANY_CODE));
			params.put("did", pref.getString(Constant.DRIVER_ID));
			params.put("action", "add");
			params.put("submit", ""+submit);
			params.put("date", ""+date);
			params.put("dateperiod", "" + dateperiod);
			params.put("tripopt", ""+strTrip);
			params.put("truck", ""+truck);
			params.put("odoreading", ""+odoreading);
			params.put("trailer", ""+trailer);
			params.put("remark", "" + remark);
			params.put("vehsat", "" + vehsat);
			params.put("drvname", "" + drvname);
			params.put("driverlat", "" + driverlat);
			params.put("driverlong", driverlong);
			params.put("defcorr", ""+defcorr);
			params.put("safeope", ""+safeope);
			params.put("photolat", "" + driverlat);
			params.put("photolong", driverlong);
			params.put("address", "" + straddress);
			params.put("traother", ""+traother);
			params.put("typefld",  "driver");
			params.put("typech", ""+typech);
			params.put("subimgkk", "" + arrdf);
			params.put("subimgkktrailer", ""+arrdftrailers);
			params.put("truckfld", tvalid);
			params.put("truckfldpositive", truckvalid);
			params.put("other", "" + other);
			params.put("trailfld", ""+trailernotvalid);
			params.put("trailfldpositive", ""+trailervalid);
			params.put("imgval", ""+arrayimgstring);
			params.put("lat", "" + lat);
			params.put("lon", "" + lon);
			params.put("nval", "");
					Log.e("params","@"+params.toString());
			params.put("driverimage", "" + "" + driverimage);
			params.put("photo", ""+image_string3);

			params.put("android_id", ""+androidId);
			Call<Response_model> call =null;

							Log.e(" reerrer", " rwerwere" );
				call = api.savedriverchecklist(params,""+androidId,""+androidId,
						"and");
				//call = api.savedriverchecklist(params);




			call.enqueue(new Callback<Response_model>() {
				@Override
				public void onResponse(Call<Response_model> call, Response<Response_model> response) {
					//			Log.e(" Responsev", " " + response.toString());
					//		  Log.e(" Responsesskk", " " + String.valueOf(response.code()));
					if (response.isSuccessful()) {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}

						if (response.body() != null) {
							// movies.addAll(response.body());
							//					 Log.e(" Responsecqevv","z "+response.body());

							Response_model tr = new Response_model();
							tr = response.body();
							if (tr.status.contentEquals("1")) {
								Toast.makeText(getApplicationContext(),
										tr.getMessage(), Toast.LENGTH_SHORT).show();

								pref.putString(Constant.VIEW_BACK, Constant.VIEW_BACK);
								finish();


							}

							else {

								Toast.makeText(context, "" + tr.message, Toast.LENGTH_SHORT).show();

							}
						}

					} else {
						dialog.dismiss();

					}
				}

				@Override
				public void onFailure(Call<Response_model> call, Throwable t) {
					dialog.dismiss();
					//			Log.e(" errrres", " " +t.toString());
				}

			});
		}
	}

	protected void objectMethod(JSONObject response) {
		// TODO Auto-generated method stub
//Log.e("res","#"+response.toString());
		try {
			Toast.makeText(getApplicationContext(),
					response.getString("message"), Toast.LENGTH_SHORT).show();
			String status = response.getString("status");

			if (status.equalsIgnoreCase("1")) {
				pref.putString(Constant.VIEW_BACK,Constant.VIEW_BACK);
				finish();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}












//	private void save_driver_checklist(String action, String submit, ArrayList<String> arrayimgstring, ArrayList<String> arrdf, ArrayList<String> arrdftrailers) {
//
//		String s_action = action;
//		String s_submit = submit;
//		String s_id = id;
//
//		String date = tv_date.getText().toString();
//		String dateperiod = getRadioItem();
//		//String tripopt = "Pre Trip";
//		String truck = "";
////		if (!(tv_truck_no.getText().toString().equalsIgnoreCase("Select Truck"))) {
////			truck = sharedpreferences.getString("truck_f_ticket_id", "");
////		}
//
//		if(pref.getString(Constant.VID_NUMBER) !=null && pref.getString(Constant.VID_NUMBER).length()>0)
//		{
//			truck=pref.getString(Constant.VID_NUMBER);
//		}
//		String odoreading = et_odometer.getText().toString();
//		String trailer = "";
//		if (!(tv_trailer.getText().toString()
//				.equalsIgnoreCase("Select Trailer"))) {
//			trailer = sharedpreferences.getString("trailer_d_checklist_id", "");
//		}
//		String remark = et_remark.getText().toString();
//		String vehsat = "0";
//		if (cb_tick1.isChecked()) {
//			vehsat = "1";
//		}
//		String drvname = et_driver_name.getText().toString();
//		String driverimage = image_string;
//		String driverlat = lat;
//		String driverlong = lon;
//		String defcorr = "0";
//
//		String safeope = "0";
////		if (cb_tick3.isChecked()) {
////			safeope = "1";
////		}
//
//		String other;
//		if (et_truck_other.getVisibility() == View.VISIBLE) {
//			other = et_truck_other.getText().toString().trim();
//		} else {
//			other = "";
//		}
//		String traother;
//		if (et_truck_other.getVisibility() == View.VISIBLE) {
//			traother = et_trailer_other.getText().toString().trim();
//		} else {
//			traother = "";
//		}
//		if(submit.contentEquals("Submit"))
//		{
//			if(  str_driversig != null && str_driversig.length()>0 &&  !str_driversig.contentEquals("null"))
//			{
//
//				if(drvname != null && drvname.length()>0 && !drvname.contentEquals("null"))
//				{
//				}else{
//					Toast.makeText(context,"Please enter Driver Name",Toast.LENGTH_SHORT).show();
//
//				}
//
//			}else
//			{
//				Toast.makeText(context,"Please Sign",Toast.LENGTH_SHORT).show();
//			}
//
//			//strTrip=spintripoption.getSelectedItem().toString();
//			strTrip=getTripItem();
//
//			if(str_driversig !=null && str_driversig.length()>0 && !str_driversig.contentEquals("null")
//					&& drvname !=null && drvname.length()>0 && !drvname.contentEquals("null")) {
//				dialog = new ProgressDialog(AddDriverChecklist.this,
//						AlertDialog.THEME_HOLO_LIGHT);
//				String vb=pref.getString(Constant.DLIST_STATUS);
//				String typech="edit";
//				if(vb.contentEquals("add"))
//				{
//				}else{
//					typech="add";
//					arrayimgstring=new ArrayList<>();
//					arrayimg=new ArrayList<>();
//					arrdf=new ArrayList<>();
//					arrdftrailers=new ArrayList<>();
//
//
//
//				}
//
//
//
//
//
//				//Log.e("truckfle",""+truckfld.toString());
//				if (OnlineCheck.isOnline(this)) {
//					dialog.setMessage("Please wait...");
//					dialog.setCancelable(false);
//					dialog.show();
//					String did=pref.getString(Constant.DRIVER_ID);
//					String cc=pref.getString(Constant.COMPANY_CODE);
//
//
//
//
//
//
//
//
//
////					api = DispatchServiceGenerator.createService(Trucksoft_api.class,context);
////					Call<List<Response_model>> call = api.savechecklist(cc,did,s_action, s_submit,
////							 date, dateperiod, strTrip, truck,
////							odoreading, trailer, remark, vehsat, drvname, driverimage,
////							driverlat, driverlong, defcorr, safeope, truckfld, other, truckfldtrailer, traother,
////							image_string3, lat, lon,arrayimgstring,typech,arrdf,arrdftrailers,truckfldgood,truckfldgoodtrailer,straddress);
////
////					call.enqueue(new Callback<List<Response_model>>() {
////						@Override
////						public void onResponse(Call<List<Response_model>> call, Response<List<Response_model>> response) {
////							if(response.isSuccessful()){
////								dialog.dismiss();
////
////							}else{
////								dialog.dismiss();
////
////								Log.e("ss"," Response "+String.valueOf(response.code()));
////							}
////						}
////
////						@Override
////						public void onFailure(Call<List<Response_model>> call, Throwable t) {
////							Log.e("dd"," Response Error "+t.getMessage());
////							dialog.dismiss();
////
////						}
////					});
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//					WebServices.saveDriverChecklist(this, s_action, s_submit, s_id,
//						 date, dateperiod, strTrip, truck,
//							odoreading, trailer, remark, vehsat, drvname, driverimage,
//							driverlat, driverlong, defcorr, safeope,  truckfld, other, truckfldtrailer, traother,
//							image_string3, lat, lon,arrayimgstring,typech,arrdf,arrdftrailers,truckfldgood,truckfldgoodtrailer,straddress, new JsonHttpResponseHandler() {
//								@Override
//								public void onFailure(int statusCode, Header[] headers,
//													  String responseString, Throwable throwable) {
//									// TODO Auto-generated method stub
//									super.onFailure(statusCode, headers,
//											responseString, throwable);
//									Log.e("1111",""+responseString);
//									dialog.dismiss();
//								}
//
//								@Override
//								public void onFailure(int statusCode, Header[] headers,
//													  Throwable throwable, JSONArray errorResponse) {
//									// TODO Auto-generated method stub
//									super.onFailure(statusCode, headers, throwable,
//											errorResponse);
//									Log.e("reserdp24",""+errorResponse.toString());
//									dialog.dismiss();
//								}
//
//								@Override
//								public void onFailure(int statusCode, Header[] headers,
//													  Throwable throwable, JSONObject errorResponse) {
//									// TODO Auto-generated method stub
//									super.onFailure(statusCode, headers, throwable,
//											errorResponse);
//									Log.e("resp24",""+errorResponse.toString());
//									dialog.dismiss();
//
//								}
//
//								@Override
//								public void onSuccess(int statusCode, Header[] headers,
//													  JSONArray response) {
//									// TODO Auto-generated method stub
//									super.onSuccess(statusCode, headers, response);
//									dialog.dismiss();
//									Log.e("resp3",""+response.toString());
//									if (response != null) {
//
//									}
//								}
//
//								@Override
//								public void onSuccess(int statusCode, Header[] headers,
//													  JSONObject response) {
//									// TODO Auto-generated method stub
//									super.onSuccess(statusCode, headers, response);
//									Log.e("resp2",""+response.toString());
//									dialog.dismiss();
//									if (response != null) {
//										pref.putString(Constant.VIEW_BACK, Constant.VIEW_BACK);
//										objectMethod(response);
//									}
//
//								}
//
//								@Override
//								public void onSuccess(int statusCode, Header[] headers,
//													  String responseString) {
//									// TODO Auto-generated method stub
//									super.onSuccess(statusCode, headers, responseString);
//									Log.e("resp1",""+responseString);
//									dialog.dismiss();
//								}
//
//							});
//				}
//			}
//		}else {
//
//			if(  str_driversig != null && str_driversig.length()>0 &&  !str_driversig.contentEquals("null"))
//			{
//			}else
//			{
//				Toast.makeText(context,"Please Sign",Toast.LENGTH_SHORT).show();
//			}
//
//			if(str_driversig !=null && str_driversig.length()>0 && !str_driversig.contentEquals("null")) {
//
//				//strTrip = spintripoption.getSelectedItem().toString();
//				strTrip=getTripItem();
//				dialog = new ProgressDialog(AddDriverChecklist.this,
//						AlertDialog.THEME_HOLO_LIGHT);
//
//				if (OnlineCheck.isOnline(this)) {
//					dialog.setMessage("Please wait...");
//					dialog.setCancelable(false);
//					dialog.show();
//					String vb = pref.getString(Constant.DLIST_STATUS);
//					String typech = "edit";
//					if (vb.contentEquals("add")) {
//					} else {
//						typech = "add";
//						arrayimgstring = new ArrayList<>();
//						arrayimg = new ArrayList<>();
//						arrdf = new ArrayList<>();
//						arrdftrailers = new ArrayList<>();
//					}
//
//
//
//					String did=pref.getString(Constant.DRIVER_ID);
//					String cc=pref.getString(Constant.COMPANY_CODE);
//
//
////
////					Log.e("truckfle1","cc="+cc+"&did="+did+"&action="+action
////							+"&submit="+submit+"&date="+date+"&dateperiod="+dateperiod+
////							"&tripopt="+strTrip+"&truck="+truck+"&odoreading="+odoreading
////							+"&trailer="+trailer+"&remark="+remark+"&vehsat="+vehsat
////							+"&drvname"+drvname);
////
////					Log.e("trck2","driverlat="+driverlat+"&driverlong="+driverlong
////							+"&driverimage="+driverimage);
////
////
////					Log.e("trck3","defcorr="+defcorr+"&safeope="+safeope
////							+"&truckfld[]="+truckfld+"&other="+other+"&truckfldpositive[]="+truckfldtrailer
////							+"&traother="+traother+"photo="+image_string3+"&photolat="+lat
////							+"&photolong="+lon+"&subimgkktrailer="+arrayimgstring+"&trailfld[]="+truckfldgood
////							+"&trailfldpositive[]="+truckfldgoodtrailer);
////
////
////
////					api = DispatchServiceGenerator.createService(Trucksoft_api.class,context);
////					Call<List<Response_model>> call = api.savechecklist(cc,did,s_action, s_submit,
////							date, dateperiod, strTrip, truck,
////							odoreading, trailer, remark, vehsat, drvname, driverimage,
////							driverlat, driverlong, defcorr, safeope, truckfld, other, truckfldtrailer, traother,
////							image_string3, lat, lon,arrayimgstring,typech,arrdf,arrdftrailers,truckfldgood,truckfldgoodtrailer,straddress);
////
////					call.enqueue(new Callback<List<Response_model>>() {
////						@Override
////						public void onResponse(Call<List<Response_model>> call, Response<List<Response_model>> response) {
////							if(response.isSuccessful()){
////								dialog.dismiss();
////
////							}else{
////								dialog.dismiss();
////
////								Log.e("ss"," Response "+String.valueOf(response.code()));
////							}
////						}
////
////						@Override
////						public void onFailure(Call<List<Response_model>> call, Throwable t) {
////							Log.e("dd"," Response Error "+t.getMessage());
////							dialog.dismiss();
////
////						}
////					});
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//					//Log.e("truckflet","@@@@"+truckfld.toString());
//
////					//Log.e("truckfld",""+truckfld);
////					//Log.e("trail",""+truckfldtrailer);
//					WebServices.saveDriverChecklist(this, s_action, s_submit, s_id,
//							 date, dateperiod, strTrip, truck,
//							odoreading, trailer, remark, vehsat, drvname, driverimage,
//							driverlat, driverlong, defcorr, safeope, truckfld, other, truckfldtrailer, traother,
//							image_string3, lat3, lon3, arrayimgstring, typech, arrdf, arrdftrailers, truckfldgood, truckfldgoodtrailer,straddress, new JsonHttpResponseHandler() {
//								@Override
//								public void onFailure(int statusCode, Header[] headers,
//													  String responseString, Throwable throwable) {
//									// TODO Auto-generated method stub
//									super.onFailure(statusCode, headers,
//											responseString, throwable);
//									//Log.e("res1",responseString);
//									dialog.dismiss();
//								}
//
//								@Override
//								public void onFailure(int statusCode, Header[] headers,
//													  Throwable throwable, JSONArray errorResponse) {
//									// TODO Auto-generated method stub
//									super.onFailure(statusCode, headers, throwable,
//											errorResponse);
//									//Log.e("res2",errorResponse.toString());
//									dialog.dismiss();
//								}
//
//								@Override
//								public void onFailure(int statusCode, Header[] headers,
//													  Throwable throwable, JSONObject errorResponse) {
//									// TODO Auto-generated method stub
//									super.onFailure(statusCode, headers, throwable,
//											errorResponse);
//									//Log.e("res3",errorResponse.toString());
//									dialog.dismiss();
//
//								}
//
//								@Override
//								public void onSuccess(int statusCode, Header[] headers,
//													  JSONArray response) {
//									// TODO Auto-generated method stub
//									super.onSuccess(statusCode, headers, response);
//									//Log.e("res4",response.toString());
//									dialog.dismiss();
//									if (response != null) {
//
//									}
//								}
//
//								@Override
//								public void onSuccess(int statusCode, Header[] headers,
//													  JSONObject response) {
//									// TODO Auto-generated method stub
//									super.onSuccess(statusCode, headers, response);
//									dialog.dismiss();
//									//Log.e("res5",response.toString());
//									if (response != null) {
//										pref.putString(Constant.VIEW_BACK, Constant.VIEW_BACK);
//										objectMethod(response);
//									}
//
//								}
//
//								@Override
//								public void onSuccess(int statusCode, Header[] headers,
//													  String responseString) {
//									// TODO Auto-generated method stub
//									super.onSuccess(statusCode, headers, responseString);
//									dialog.dismiss();
//								}
//
//							});
//				}
//			}
//		}
//
//	}
//
//











//	protected void objectMethod(JSONObject response) {
//		// TODO Auto-generated method stub
////Log.e("res","#"+response.toString());
//		try {
//			Toast.makeText(getApplicationContext(),
//					response.getString("message"), Toast.LENGTH_SHORT).show();
//			String status = response.getString("status");
//
//			if (status.equalsIgnoreCase("1")) {
//				pref.putString(Constant.VIEW_BACK,Constant.VIEW_BACK);
//				finish();
//			}
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//
//	}













	@Override
	public void onConnected(@Nullable Bundle bundle) {
// Get last known recent location.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (ContextCompat.checkSelfPermission(context,
					Manifest.permission.ACCESS_FINE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED) {



			}
		}
		mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		// Note that this can be NULL if last location isn't already known.
		if (mCurrentLocation != null) {
			// Print current location if not null
			//Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
			LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
			lat=String.valueOf(mCurrentLocation.getLatitude());
			lon=String.valueOf(mCurrentLocation.getLongitude());

		}
		// Begin polling for new location updates.
		try {
			startLocationUpdates();
		}catch (Exception e)
		{
			Log.e("loc",""+e.toString());
		}
	}

	@Override
	public void onConnectionSuspended(int i) {
		if (i == CAUSE_SERVICE_DISCONNECTED) {
			//	Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
		} else if (i == CAUSE_NETWORK_LOST) {
			//Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}

	@Override
	public void onLocationChanged(Location location) {

		lat=String.valueOf(location.getLatitude());
		lon=String.valueOf(location.getLongitude());

		LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
	}
	protected void startLocationUpdates() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (ContextCompat.checkSelfPermission(context,
					Manifest.permission.ACCESS_FINE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED) {
			}
		}
		mLocationRequest = LocationRequest.create()
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
				.setInterval(UPDATE_INTERVAL)
				.setFastestInterval(FASTEST_INTERVAL);
		// Request location updates
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
				mLocationRequest, this);
	}
	public void getMyLocation(final Context cons){
		// Log.e("common dislog","location");
		if(mGoogleApiClient==null )
		{ //Log.e("mGoogleApiClient","empty");
			buildGoogleApiClient();
		}
		if(mGoogleApiClient!= null)
		{
			//Log.e("mGoogleApiClient","not connected");
			try{
				mGoogleApiClient.connect();
			}catch (IllegalStateException e)
			{
				// Log.e("IllegalStateException", e.toString());
			}

		}
		if(mGoogleApiClient!=null) {
			// Log.e("mGoogleApiClient","okok");
			if (mGoogleApiClient.isConnected()) {
				// Log.e("mGoogleApiClient","okokdone");

				int permissionLocation = ContextCompat.checkSelfPermission(cons,
						Manifest.permission.ACCESS_FINE_LOCATION);
				if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
					// Log.e("permission","ok");
					mCurrentLocation =                     LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
					LocationRequest locationRequest = new LocationRequest();
					locationRequest.setInterval(10000);
					locationRequest.setFastestInterval(10000);
					locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
					LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
							.addLocationRequest(locationRequest);
					builder.setAlwaysShow(true);
					LocationServices.FusedLocationApi
							.requestLocationUpdates(mGoogleApiClient, locationRequest,  this);
					PendingResult<LocationSettingsResult> result =
							LocationServices.SettingsApi
									.checkLocationSettings(mGoogleApiClient, builder.build());
					result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

						@Override
						public void onResult(LocationSettingsResult result) {
							final Status status = result.getStatus();
							//   Log.e("statuserh",""+status.getStatusCode());
							switch (status.getStatusCode()) {

								case LocationSettingsStatusCodes.SUCCESS:
									// Log.e("calling",""+"gps");
									// All location settings are satisfied.
									// You can initialize location requests here.
									int permissionLocation = ContextCompat
											.checkSelfPermission(cons,
													Manifest.permission.ACCESS_FINE_LOCATION);
									if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
										mCurrentLocation = LocationServices.FusedLocationApi
												.getLastLocation(mGoogleApiClient);
									}
									break;
								case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
									// Location settings are not satisfied.
									// But could be fixed by showing the user a dialog.
									//Log.e("calling",""+"gps fail");
									try {
										// Show the dialog by calling startResolutionForResult(),
										// and check the result in onActivityResult().
										// Ask to turn on GPS automatically
										status.startResolutionForResult((Activity) cons,
												REQUEST_CHECK_SETTINGS_GPS);
									} catch (IntentSender.SendIntentException e) {
										// Ignore the error.
										// Log.e("calling gps erorrrrrrr",""+e.toString());
									}
									break;
								case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
									// Location settings are not satisfied.
									// However, we have no way
									// to fix the
									// settings so we won't show the dialog.
									// finish();
									break;
							}
						}
					});
				}
			}
		}
	}
	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(context)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		mGoogleApiClient.connect();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();


		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}


	}

	public static class SelectDateFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		int yy, mm, dd;
		 int value;
//		public SelectDateFragment() {
//			// Required empty public constructor
//		}
//		public  SelectDateFragment(int i) {
//			// TODO Auto-generated constructor stub
//			value = i;
//		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			final Calendar calendar = Calendar.getInstance();
			yy = calendar.get(Calendar.YEAR);
			mm = calendar.get(Calendar.MONTH);
			dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,this, yy, mm, dd);
		}

		public void onDateSet(DatePicker view, int yy, int mm, int dd) {
			populateSetDate(yy, mm + 1, dd);
		}

		public void populateSetDate(int year, int month, int day) {

			if (month < 10 && day < 10) {

				tv_date.setText(year + "-0" + month + "-0" + day);

			} else if (day < 10) {

				tv_date.setText(year + "-" + month + "-0" + day);

			} else if (month < 10) {

				tv_date.setText(year + "-0" + month + "-" + day);

			} else {

				tv_date.setText(year + "-" + month + "-" + day);

			}

		}

	}


	private String getRadioItem() {
		// TODO Auto-generated method stub
		String str = "";

		int selectedId = mGroup_timemode.getCheckedRadioButtonId();

		if (selectedId == mButton_timemode_am.getId()) {
			return "A.M.";

		} else if (selectedId == mButton_timemode_pm.getId()) {
			return "P.M.";

		}

		return str;
	}
	private String getTripItem() {
		// TODO Auto-generated method stub
		String str = "";

		int selectedId = mGroup_tripmode.getCheckedRadioButtonId();
		if (selectedId == mButton_trippre.getId()) {
			return "PRE TRIP";

		} else if (selectedId == mButton_trippost.getId()) {
			return "POST TRIP";

		}

		return str;
	}
	public void onPhotoClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
		builder.setTitle("Select Picker").setItems(R.array.str_arr_img_picker,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
								Intent i = new Intent(
										Intent.ACTION_PICK,
										MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(i, RESULT_LOAD_IMAGE);
							}else {
								pickImage();
							}
						} else {
							doTakePhotoAction();
						}
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public void pickImage() {
		mImageCropUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "tmp_avatar.jpg"));
		mImageCaptureUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "tmp_avatar_"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent();
			intent.setAction(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(
					Intent.createChooser(intent, "Complete action using"),
					PICK_FROM_FILE);
		} else {
			intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					mImageCaptureUri);
			startActivityForResult(
					Intent.createChooser(intent, "Complete action using"),
					PICK_FROM_FILE);
		}
	}

	private void doTakePhotoAction() {
		mImageCropUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "tmp_avatar.jpg"));
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		mImageCaptureUri = Uri.fromFile(new File(Environment
//				.getExternalStorageDirectory(), "tmp_avatar_"
//				+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
//		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
//				mImageCaptureUri);
//		try {
//			intent.putExtra("return-data", true);
//			startActivityForResult(intent, PICK_FROM_CAMERA);
//		} catch (ActivityNotFoundException e) {
//			Log.e("Error", e.toString());
//		}




		ContentValues values;
		//captureImage();
		values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, "New Picture");
		values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

		mImageCaptureUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
		startActivityForResult(intent, PICK_FROM_CAMERA);




	}

	private void doCrop() {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
		List<ResolveInfo> list = getApplicationContext().getPackageManager()
				.queryIntentActivities(intent, 0);
		int size = list.size();
		if (size == 0) {
			Toast.makeText(context, "Can not find image crop app",
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			if (mImageCaptureUri != null) {
				intent.setData(mImageCaptureUri);
			}
			// intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
			// mImageCaptureUri);
			intent.putExtra("outputX", 350);
			intent.putExtra("outputY", 350);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", false);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCropUri);
			Intent i = new Intent(intent);
			ResolveInfo res = list.get(0);
			i.setComponent(new ComponentName(res.activityInfo.packageName,
					res.activityInfo.name));
			System.out.println(res.activityInfo.packageName);
			System.out.println(res.activityInfo.name);
			startActivityForResult(i, CROP_FROM_CAMERA);
		}
	}
	public void ImageCropFunction() {

		// Image Crop Code
		Intent CropIntent;
		try {
			CropIntent = new Intent("com.android.camera.action.CROP");

			CropIntent.setDataAndType(mImageCaptureUri, "image/*");

			CropIntent.putExtra("crop", "true");
			CropIntent.putExtra("outputX", 180);
			CropIntent.putExtra("outputY", 180);
			CropIntent.putExtra("aspectX", 3);
			CropIntent.putExtra("aspectY", 4);
			CropIntent.putExtra("scaleUpIfNeeded", true);
			CropIntent.putExtra("return-data", true);

			startActivityForResult(CropIntent, CROP_FROM_CAMERA);

		} catch (ActivityNotFoundException e) {

		}
	}
//	public void uploadimage(String image) {
//
//		dialog = new ProgressDialog(AddDriverChecklist.this,
//				AlertDialog.THEME_HOLO_LIGHT);
//
//		if (OnlineCheck.isOnline(this)) {
//			dialog.setMessage("Please wait...");
//			dialog.setCancelable(false);
//			dialog.show();
//
//			gps = new GPSTracker(AddDriverChecklist.this);
//			if (gps.canGetLocation()) {
//
//				latitude = gps.getLatitude();
//				lat = String.valueOf(latitude);
//				longitude = gps.getLongitude();
//				lon = String.valueOf(longitude);
//
//			} else {
//				gps.showSettingsAlert();
//			}
//
//			WebServices.uploadImage(this, image, lat, lon,
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
//								// arrayMethod(response);
//							}
//						}
//
//						@Override
//						public void onSuccess(int statusCode, Header[] headers,
//								JSONObject response) {
//							// TODO Auto-generated method stub
//							super.onSuccess(statusCode, headers, response);
//							dialog.dismiss();
//							/*
//							 * Toast.makeText(getApplicationContext(),
//							 * response.toString(), Toast.LENGTH_SHORT) .show();
//							 */
//							// objectMethod(response);
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
//		else {
//
//			Toast.makeText(getApplicationContext(),
//					"Please check your internet connection", Toast.LENGTH_SHORT)
//					.show();
//		}
//
//	}

	private void setimagenew() {
		lytLayout.removeAllViews();

        //Log.e("arrimg size",""+arrayimg.size());
		for(int j=0;j<arrayimg.size();j++)
		{
			Uri imkkk=arrayimg.get(j);

		LayoutInflater layoutInflater =
				(LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View addView = layoutInflater.inflate(R.layout.add_views, null);
		// TextView textOut = (TextView)addView.findViewById(R.id.textout);
		// textOut.setText(textIn.getText().toString());
		ImageView imgkt = (ImageView) addView.findViewById(R.id.dashkt);
		final ImageView imgremove = (ImageView) addView.findViewById(R.id.badge1);
		TextView txt_url = (TextView) addView.findViewById(R.id.txturl);
		TextView txt_rid = (TextView) addView.findViewById(R.id.txtrid);
		//Log.e("txt_rid", "/"+lastid);
txt_url.setText(""+imkkk);
		 txt_rid.setText(""+j);
			//Log.e("arrimg imkkk",""+imkkk.toString());
		// txt_url.setText(add_url + "?.time();");
		(imgkt).setImageURI(imkkk);

		imgremove.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {
                View li1 = (RelativeLayout) v.getParent();
                TextView cno = (TextView) li1.findViewById(R.id.txturl);
                TextView txt_rid = (TextView) li1.findViewById(R.id.txtrid);
                //Log.e("urldelete", "kt"+cno.getText().toString()+"---"+txt_rid.getText().toString());
                if (arrayimg.contains(cno.getText().toString())) {
                    arrayimg.remove(cno.getText().toString());
                }
                int a = 0;
                a = arrayimg.size();
                //deleteBol(dispatid, cno.getText().toString(), txt_rid.getText().toString(), "" + a);
                ((LinearLayout) addView.getParent()).removeView(addView);
			}
		});
			//Log.e("arrimg okkkkk","okkkkkkkkk");
		lytLayout.addView(addView);
	}

    }
    private void setimageold()
	{
		lytLayout.removeAllViews();
		for(int k=0;k<arrayimgold.size();k++)
		{
			String urlval=arrayimgold.get(k);
			StringTokenizer skr=new StringTokenizer(urlval,">>");
			String simgid=skr.nextToken();
			String url=skr.nextToken();
			LayoutInflater layoutInflater =
					(LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View addView = layoutInflater.inflate(R.layout.add_views, null);
			// TextView textOut = (TextView)addView.findViewById(R.id.textout);
			// textOut.setText(textIn.getText().toString());
			ImageView imgkt = (ImageView) addView.findViewById(R.id.dashkt);
			final ImageView imgremove = (ImageView) addView.findViewById(R.id.badge1);
			TextView txt_url = (TextView) addView.findViewById(R.id.txturl);
			TextView txt_rid = (TextView) addView.findViewById(R.id.txtrid);
			final TextView txt_demo = (TextView) addView.findViewById(R.id.txtdemo);
			//Log.e("txt_rid", "/"+lastid);
			txt_url.setText(""+urlval);
			txt_rid.setText(simgid);
			txt_demo.setText(""+url);
			Picasso.with(context)
					.load(url + "?.time();")
					.memoryPolicy(MemoryPolicy.NO_CACHE)
					.networkPolicy(NetworkPolicy.NO_CACHE)
					.placeholder(R.drawable.prog_animation)
					.error(R.drawable.whitekt)
					.into(imgkt);
imgkt.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) {
		Intent intenttt = new Intent(AddDriverChecklist.this, Image_view_bol.class);
		intenttt.putExtra("img_url", "" + txt_demo.getText().toString());
		startActivity(intenttt);
	}
});
			imgremove.setOnClickListener(new OnClickListener() {


				public void onClick(View v) {
					View li1 = (RelativeLayout) v.getParent();
					TextView cno = (TextView) li1.findViewById(R.id.txturl);
					TextView txt_rid = (TextView) li1.findViewById(R.id.txtrid);
					//Log.e("urldelete", "kt"+cno.getText().toString()+"---"+txt_rid.getText().toString());
					//Log.e("arcno",cno.getText().toString());
					//Log.e("cno",arrayimgold.toString());
					if (arrayimgold.contains(cno.getText().toString().trim())) {
						arrayimgold.remove(cno.getText().toString().trim());
					}
					//delimages(id,txt_rid.getText().toString());
					//deleteBol(dispatid, cno.getText().toString(), txt_rid.getText().toString(), "" + a);
					((LinearLayout) addView.getParent()).removeView(addView);
				}
			});

			lytLayout.addView(addView);
		}
	}


    @RequiresApi(api = Build.VERSION_CODES.M)
	private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add(Manifest.permission.CAMERA);
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
//            permissionsNeeded.add("Read phone state");
//        if (!addPermission(permissionsList, Manifest.permission.SEND_SMS))
//            permissionsNeeded.add("Send sms");
//        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
//            permissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
//        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
//            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

    }
    private boolean addPermission(List<String> permissionsList, String permission) {
        boolean bool=false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    bool= false;
            }
            bool=true;
        }
        return bool;
    }
		public byte[] getBytes(InputStream inputStream) throws IOException {
			ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];

			int len = 0;
			while ((len = inputStream.read(buffer)) != -1) {
				byteBuffer.write(buffer, 0, len);
			}
			return byteBuffer.toByteArray();
		}

		private void convertstring(byte[] imgkk)
		{
			String strPhoto = "";
			if (imgkk != null) {
				if (imgkk.length > 0) {
					try {
						CommonUtil commonUtil = new CommonUtil(context);
						strPhoto = commonUtil.ecode64(imgkk);
						commonUtil = null;
					} catch (Exception e) {
						//Log.e("Image Exception", e.getMessage());
					}
				}
			} else {
				strPhoto = null;
			}

            String vb=pref.getString(Constant.DLIST_STATUS);
            if(vb.contentEquals("add"))
            {


			if(arrayimgstring.size()<=0)
            {
                arrayimgstring.add(strPhoto);
            }else{
                arrayimgstring.add("&&"+strPhoto);
            }
           // setimage();
            }else if(s_action.contentEquals("edit")){
                //savimages(id,strPhoto);
            }

			//Log.e("arrimg",""+arrayimgstring.toString());
			//savimages();

			//save_driver_checklist(s_action, "Save",arrayimgstring);
		}

	private void setimageval(JSONArray response) {

		if (response != null) {
			if (response.length() > 0) {
try {
	for (int j = 0; j < response.length(); j++) {
		JSONObject mResultJsonimg = response
				.getJSONObject(j);
		String imgid = mResultJsonimg
				.getString("img_id");
		String imgurl = mResultJsonimg
				.getString("img_url");
		arrayimgold.add(imgid + ">>" + imgurl);
	}
}catch (Exception e)
{

}

				setimageold();
			}
		}
	}

	public void getdriversignature()
	{   // Button btntransmit, btnclear;
		drivesig=0;
ImageView imgback;
		final Dialog dialog = new Dialog(context);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.checklist_signature);
		btntransmit = dialog.findViewById(R.id.signature_tv_save);
		mContent = dialog.findViewById(R.id.signature_ll_signView);
		imgback=dialog.findViewById(R.id.signature_iv_back);
		btnclear = dialog.findViewById(R.id.signature_clear);
		mSignature = new signature(AddDriverChecklist.this, null);
		mView = mContent;
		mSignature.setBackgroundColor(Color.WHITE);
		mContent.addView(mSignature, ActionBar.LayoutParams.FILL_PARENT,
				ActionBar.LayoutParams.FILL_PARENT);
		btntransmit.setEnabled(false);
		btntransmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				boolean error = captureSignature();
				if (!error) {
					mView.setDrawingCacheEnabled(true);
					mSignature.save(mView);
					try {
						if (straddress == null || straddress.length() == 0) {
							double d = Double.parseDouble("" + lat);
							double d2 = Double.parseDouble("" + lon);
							getAddressFromLocation(d, d2);
						}
					}catch (Exception e)
					{

					}
ArrayList<String> arrdf1=new ArrayList<>();
					if(traileritemcount==truckfldtrailer.size()+truckfldgoodtrailer.size()
							&& truckitemcount==truckfld.size()+truckfldgood.size()) {

						save_driver_checklist(s_action, "Submit", arrayimgstring, arrdf1, arrdf1);
					}else if(truckitemcount!=truckfld.size()+truckfldgood.size())
					{
						AlertDialog.Builder builder= new AlertDialog.Builder(context);
						builder.setMessage("Please choose all truck items") .setTitle("Note");

						builder.setMessage("Please choose all truck items ")
								.setCancelable(false)
								.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.dismiss();
										if(cb_tick1.isChecked()) {
											cb_tick1.setChecked(false);
										}
									}
								});

						//Creating dialog box
						AlertDialog alert = builder.create();
						//Setting the title manually
						alert.setTitle("Note !");
						alert.show();
					}


					else{
						AlertDialog.Builder builder= new AlertDialog.Builder(context);
						builder.setMessage("Please choose all trailer items") .setTitle("Note");

						builder.setMessage("Please choose all trailer items ")
								.setCancelable(false)
								.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.dismiss();
										if(cb_tick1.isChecked()) {
											cb_tick1.setChecked(false);
										}
									}
								});

						//Creating dialog box
						AlertDialog alert = builder.create();
						//Setting the title manually
						alert.setTitle("Note !");
						alert.show();
					}
				}

			}
		});
		imgback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				cb_tick1.setChecked(false);
			}
		});
		btnclear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSignature.clear();
				btntransmit.setEnabled(false);
			}
		});

		dialog.show();
	}

	private boolean captureSignature() {

		boolean error = false;
		String errorMessage = "";

		/*
		 * if(yourName.getText().toString().equalsIgnoreCase("")){ errorMessage
		 * = errorMessage + "Please enter your Name\n"; error = true; }
		 */

		if (error) {
			Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			/*
			 * toast.setGravity(Gravity.TOP, 105, 50); toast.show();
			 */
		}

		return error;
	}
	public class signature extends View {
		private static final float STROKE_WIDTH = 5f;
		private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
		private Paint paint = new Paint();
		private Path path = new Path();

		private float lastTouchX;
		private float lastTouchY;
		private final RectF dirtyRect = new RectF();

		public signature(Context context, AttributeSet attrs) {
			super(context, attrs);
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeWidth(HALF_STROKE_WIDTH);
		}

		public void save(View v) {
			//Log.v("log_tag", "Width: " + v.getWidth());
			//Log.v("log_tag", "Height: " + v.getHeight());
			if (mBitmap == null) {
				mBitmap = Bitmap.createBitmap(mContent.getWidth(),
						mContent.getHeight(), Bitmap.Config.RGB_565);

			}
			Canvas canvas = new Canvas(mBitmap);
			try {
				v.draw(canvas);

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				// bitmap = BitmapFactory.decodeFile(filePath, o2);
				mBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
				imagesign = stream.toByteArray();
				//updatedStringimagesign = Base64.encodeToString(imagesign,
					//	Base64.DEFAULT);
				image_string= Base64.encodeToString(imagesign,
						Base64.DEFAULT);
				str_driversig= Base64.encodeToString(imagesign,
						Base64.DEFAULT);
				//Log.e("ffff",""+updatedStringimagesign);

if(drivesig==1)
{
	taddress.setText(""+straddress );
	byte[] imageAsBytes = Base64.decode(
			image_string.getBytes(), Base64.DEFAULT);
	str_driversig = Base64.encodeToString(imageAsBytes,
			Base64.DEFAULT);
	iv_sign_driver.setImageBitmap(BitmapFactory
			.decodeByteArray(imageAsBytes, 0,
					imageAsBytes.length));
}
			} catch (Exception e) {
				//Log.e("log_tag", e.toString());
			}
		}

		public void clear() {
			path.reset();
			invalidate();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawPath(path, paint);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if(drivesig==1)
			{
				//mGetSign.setEnabled(true);
			}else {
				btntransmit.setEnabled(true);
			}
			float eventX = event.getX();
			float eventY = event.getY();
			//btntransmit.setEnabled(true);

			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					path.moveTo(eventX, eventY);
					lastTouchX = eventX;
					lastTouchY = eventY;
					return true;

				case MotionEvent.ACTION_MOVE:

				case MotionEvent.ACTION_UP:

					resetDirtyRect(eventX, eventY);
					int historySize = event.getHistorySize();
					for (int i = 0; i < historySize; i++) {
						float historicalX = event.getHistoricalX(i);
						float historicalY = event.getHistoricalY(i);
						expandDirtyRect(historicalX, historicalY);
						path.lineTo(historicalX, historicalY);
					}
					path.lineTo(eventX, eventY);
					break;

				default:
					debug("Ignored touch event: " + event.toString());
					return false;
			}

			invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
					(int) (dirtyRect.top - HALF_STROKE_WIDTH),
					(int) (dirtyRect.right + HALF_STROKE_WIDTH),
					(int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

			lastTouchX = eventX;
			lastTouchY = eventY;

			return true;
		}

		private void debug(String string) {
		}

		private void expandDirtyRect(float historicalX, float historicalY) {
			if (historicalX < dirtyRect.left) {
				dirtyRect.left = historicalX;
			} else if (historicalX > dirtyRect.right) {
				dirtyRect.right = historicalX;
			}

			if (historicalY < dirtyRect.top) {
				dirtyRect.top = historicalY;
			} else if (historicalY > dirtyRect.bottom) {
				dirtyRect.bottom = historicalY;
			}
		}

		private void resetDirtyRect(float eventX, float eventY) {
			dirtyRect.left = Math.min(lastTouchX, eventX);
			dirtyRect.right = Math.max(lastTouchX, eventX);
			dirtyRect.top = Math.min(lastTouchY, eventY);
			dirtyRect.bottom = Math.max(lastTouchY, eventY);
		}
	}


	public  String getAddressFromLocation(final double latitude, final double longitude) {

		Geocoder geocoder = new Geocoder(context, Locale.getDefault());

		try {
			List<Address> addressList = geocoder.getFromLocation(
					latitude, longitude, 1);
			if (addressList != null && addressList.size() > 0) {
				Address address = addressList.get(0);
				StringBuilder sb = new StringBuilder();
				if(address.getAddressLine(0) !=null && address.getAddressLine(0).length()>0 && !address.getAddressLine(0).contentEquals("null"))
				{
					sb.append(address.getAddressLine(0)).append("\n");
				}else {

					sb.append(address.getLocality()).append("\n");
					sb.append(address.getPostalCode()).append("\n");
					sb.append(address.getCountryName());
				}
				straddress = sb.toString();
				//Log.e("address",""+straddress);
			}
		} catch (IOException e) {
			//Log.e("df", "Unable connect to Geocoder", e);
		}


		return straddress;
	}


	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();

	}
	public void CheckGpsStatus(){

		locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

		GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
//	private void editdriverchklist(String id) {
//
//		api = DispatchServiceGenerator.createService(Trucksoft_api.class,context);
//		dialog= new ProgressDialog(AddDriverChecklist.this);
//		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//
//		// Set the progress dialog title and message
//		// dialog.setTitle("Title of progress dialog.");
//		dialog.setMessage("Loading.........");
//
//		// Set the progress dialog background color
//		// dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B53391")));
//
//		dialog.setIndeterminate(false);
//		if (OnlineCheck.isOnline(this)) {
//			dialog.setMessage("Please wait...");
//			dialog.setCancelable(false);
//			dialog.show();
//
//
//			RequestBody idbody = RequestBody.create(MediaType.parse("text/plain"), id);
//			Call<ResponseBody> call = api.getchecklistdetails(idbody);
//			call.enqueue(new Callback<ResponseBody>() {
//				@Override
//				public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//					try {
//						mStatusCode = response.code();
//						Log.e("mStatusCoded", "mStatusCode==" + mStatusCode);
//						if (mStatusCode == Constant.SUCEESSRESPONSECODE) {
//							dialog.dismiss();
//							// JSONObject result = new JSONObject(response.body().string());
//							try {
//								JSONArray resultarray = new JSONArray(response.body().string());
//								if (resultarray.length() > 0) {
//									edit_checklist(response);
//								}
//							}catch (JSONException e)
//							{
//								Log.e("qw2","@"+e.toString());
//							}
//							catch (Exception e)
//							{
//								Log.e("qw23","@"+e.toString());
//							}
//
//						} else if (mStatusCode == Constant.FAILURERESPONSECODE) {
//							dialog.dismiss();
////                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
////                                    "Invalid username or password!", Snackbar.LENGTH_LONG);
////                            snackbar.show();
//
//						} else {
//							dialog.dismiss();
//							if (mStatusCode == Constant.INTERNALERRORRESPONSECODE) {
//
//								Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
//										response.errorBody().string(), Snackbar.LENGTH_LONG);
//								snackbar.show();
//							}
//						}
//					}catch (IOException e) {
//						Log.e("qw2","@"+e.toString());
//						e.printStackTrace();
//					}
//				}
//
//				@Override
//				public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//					if (t instanceof SocketTimeoutException) {
//						Log.v("SocketTimeOut", "SocketTimeOutError");
//						Toast.makeText(AddDriverChecklist.this, getString(R.string.socket_timeout)
//								, Toast.LENGTH_SHORT).show();
//					}
//					dialog.dismiss();
//				}
//			});
//		}
//
//	}
	// Function to convert ArrayList<String> to String[]
	public static String[] GetStringArray(ArrayList<String> arr)
	{

		// declaration and initialise String Array
		String str[] = new String[arr.size()];

		// ArrayList to Array Conversion
		for (int j = 0; j < arr.size(); j++) {

			// Assign each value to String array
			str[j] = arr.get(j);
		}

		return str;
	}

	private void getsignature(final String val) {
		View viewd = View.inflate(context, R.layout.signature_new, null);
		final Button mClear, mGetSign, mCancel;
		mClear = (Button) viewd.findViewById(R.id.clear);
		mGetSign = (Button) viewd.findViewById(R.id.getsign);
		mContent = (LinearLayout) viewd.findViewById(R.id.linearLayout);
		//mGetSign.setEnabled(false);
		mCancel = (Button) viewd.findViewById(R.id.cancel);
		final Dialog dialog = new Dialog(context, R.style.DialogTheme);
		//dialog = new Dialog(this, R.style.DialogTheme);
		mSignature = new signature(this, null);
		mSignature.setBackgroundColor(Color.WHITE);
		mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		view = mContent;
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(viewd);
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
		mClear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Log.v("log_tag", "Panel Cleared");
				mSignature.clear();
				//mGetSign.setEnabled(false);
			}
		});

		mGetSign.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// Log.v("log_tag", "Panel Saved");
				view.setDrawingCacheEnabled(true);
				//mSignature.save(view, StoredPath, dialog, val);

				//   Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
				// Calling the same class
				// recreate();

				////view.setDrawingCacheEnabled(true);
				////mSignature.save(mView);
				//mSignature.save(view, StoredPath, dialog, val);


				dialog.dismiss();
				boolean error = captureSignature();
				if (!error) {
					view.setDrawingCacheEnabled(true);

					iv_sign_driver.setVisibility(View.VISIBLE);
					iv_sign_driver_clear.setVisibility(View.VISIBLE);
					try {
						if (straddress == null || straddress.length() == 0) {
							double d = Double.parseDouble("" + lat);
							double d2 = Double.parseDouble("" + lon);
							getAddressFromLocation(d, d2);
						}
					}catch (Exception e)
					{

					}
					mSignature.save(view);
				}


			}
		});

		mCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Log.v("log_tag", "Panel Canceled");
				// finish();
				dialog.dismiss();
				// recreate();
			}
		});
	}
//	public class signature extends View {
//		// Log.v("log_tag", "Width: ");
//
//		private static final float STROKE_WIDTH = 5f;
//		private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
//		private Paint paint = new Paint();
//		private Path path = new Path();
//
//		private float lastTouchX;
//		private float lastTouchY;
//		private final RectF dirtyRect = new RectF();
//
//		public signature(Context context, AttributeSet attrs) {
//			super(context, attrs);
//			paint.setAntiAlias(true);
//			paint.setColor(Color.BLACK);
//			//Log.e("calling", "signature...........");
//			paint.setStyle(Paint.Style.STROKE);
//			paint.setStrokeJoin(Paint.Join.ROUND);
//			paint.setStrokeWidth(STROKE_WIDTH);
//		}
//
//		public void save(View v, String StoredPath, Dialog dialogn, String val) {
//			//Log.v("log_tag", "Width: " + v.getWidth());
//			//Log.v("log_tag", "Height: " + v.getHeight());
//			StoredPath = DIRECTORY + "/" + val + driverid + ".png";
//			// Log.e("StoredPath", ": " + StoredPath);
//			Canvas canvas;
//			if(val.contentEquals("driver")) {
//				if (bitmap == null) {
//					//Log.e("okkkkk", ": kkkkkkkk");
//					bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
//				}
//				canvas = new Canvas(bitmap);
//			}else
//			{if (bitmap1 == null) {
//				// Log.e("okkkkk", ": kkkkkkkk");
//				bitmap1 = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
//			}
//				canvas = new Canvas(bitmap1);
//			}
//			// Canvas canvas = new Canvas(bitmap);
//			try {
//
//
////Log.e("val","-----------"+val);
//
//				ByteArrayOutputStream stream=null,stream1=null;
//				if(val.contentEquals("driver")) {
//					v.draw(canvas);
//					stream = new ByteArrayOutputStream();
//					bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
//					image = stream.toByteArray();
//				}else
//				{
//					v.draw(canvas);
//					stream1 = new ByteArrayOutputStream();
//					bitmap1.compress(Bitmap.CompressFormat.PNG, 80, stream1);
//					image1 = stream1.toByteArray();
//				}
//
//
//				if(val.contentEquals("driver")) {
//					image = stream.toByteArray();
//					updatedStringimage = Base64.encodeToString(image,
//							Base64.DEFAULT);
//
//					BitmapDrawable backgroundz = new BitmapDrawable(bitmap);
//					lin_driversignature.setBackgroundDrawable(backgroundz);
//				}
//				//  mFileOutStream.flush();
//				// mFileOutStream.close();
//
//			} catch (Exception e) {
//				//Log.v("log_tag", e.toString());
//			}
//			dialogn.dismiss();
//		}
//
//		public void clear() {
//			path.reset();
//			invalidate();
//		}
//
//		@Override
//		protected void onDraw(Canvas canvas) {
//			canvas.drawPath(path, paint);
//		}
//
//		@Override
//		public boolean onTouchEvent(MotionEvent event) {
//			float eventX = event.getX();
//			float eventY = event.getY();
//			//  mGetSign.setEnabled(true);
//			//  Log.e("Event", "clicked......" + event);
//
//			switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					path.moveTo(eventX, eventY);
//					lastTouchX = eventX;
//					lastTouchY = eventY;
//					return true;
//
//				case MotionEvent.ACTION_MOVE:
//
//				case MotionEvent.ACTION_UP:
//
//					resetDirtyRect(eventX, eventY);
//					int historySize = event.getHistorySize();
//					for (int i = 0; i < historySize; i++) {
//						float historicalX = event.getHistoricalX(i);
//						float historicalY = event.getHistoricalY(i);
//						expandDirtyRect(historicalX, historicalY);
//						path.lineTo(historicalX, historicalY);
//					}
//					path.lineTo(eventX, eventY);
//					break;
//
//				default:
//					debug("Ignored touch event: " + event.toString());
//					return false;
//			}
//
//			invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
//					(int) (dirtyRect.top - HALF_STROKE_WIDTH),
//					(int) (dirtyRect.right + HALF_STROKE_WIDTH),
//					(int) (dirtyRect.bottom + HALF_STROKE_WIDTH));
//
//			lastTouchX = eventX;
//			lastTouchY = eventY;
//
//			return true;
//		}
//
//		private void debug(String string) {
//
//			// Log.v("log_tag", string);
//
//		}
//
//		private void expandDirtyRect(float historicalX, float historicalY) {
//			if (historicalX < dirtyRect.left) {
//				dirtyRect.left = historicalX;
//			} else if (historicalX > dirtyRect.right) {
//				dirtyRect.right = historicalX;
//			}
//
//			if (historicalY < dirtyRect.top) {
//				dirtyRect.top = historicalY;
//			} else if (historicalY > dirtyRect.bottom) {
//				dirtyRect.bottom = historicalY;
//			}
//		}
//
//		private void resetDirtyRect(float eventX, float eventY) {
//			dirtyRect.left = Math.min(lastTouchX, eventX);
//			dirtyRect.right = Math.max(lastTouchX, eventX);
//			dirtyRect.top = Math.min(lastTouchY, eventY);
//			dirtyRect.bottom = Math.max(lastTouchY, eventY);
//		}
//
//	}


	private void getcount(){
		api = DispatchServiceGenerator.createService(Eld_api.class,context);

			Call<Item_model> call = api.getitemcount();

		call.enqueue(new Callback<Item_model>() {
			@Override
			public void onResponse(Call<Item_model> call, Response<Item_model> response) {
				//Log.e(" Responsev"," "+response.toString());
				//Log.e(" Responsesskk"," "+String.valueOf(response.code()));
				if(response.isSuccessful()){

					Item_model imodel=new Item_model();
					imodel=response.body();
					truckitemcount=imodel.tcount;
					traileritemcount=imodel.tracount;
					txttruckgood.setText("Good condition item selected : 0/"+truckitemcount);
					txttruckbad.setText("Bad condition item selected : 0/"+truckitemcount);
					 // Log.e(" Responsecqevv","z "+imodel.tcount);
					txttrailergood.setText("Good condition item selected : 0/"+traileritemcount);
					txttrailerbad.setText("Bad condition item selected : 0/"+traileritemcount);



				}else{

					// load(0);
					// Log.e("ggg"," Response Error "+String.valueOf(response.code()));
				}
			}

			@Override
			public void onFailure(Call<Item_model> call, Throwable t) {
				// Log.e("tttt"," Response Error "+t.getMessage());

			}
		});
	}

	private void itemalert(String strval,String msg) {

		if (dialogfederal != null) {
			if (dialogfederal.isShowing()) {
				dialogfederal.dismiss();
			}
		}
		LayoutInflater inflater = this.getLayoutInflater();
		final View dialogView;
		if(strval.contentEquals("truck"))
		{
			dialogView= inflater.inflate(R.layout.item_notification, null);
		}else {
			dialogView = inflater.inflate(R.layout.item_trailernotification, null);
		}


		final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
		final ImageView imgstatus = dialogView.findViewById(R.id.txt_img);
		final TextView txtalert = dialogView.findViewById(R.id.txtalert);
		final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
		// final Dialog dialog = new Dialog(context, R.style.DialogTheme);
		dialogfederal = new Dialog(context, R.style.DialogTheme);
		dialogfederal.setCancelable(false);
		//dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
		txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
		txtstatus.setText(""+msg);
		dialogfederal.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogfederal.setContentView(dialogView);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialogfederal.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.CENTER;

		dialogfederal.getWindow().setAttributes(lp);
		dialogfederal.show();
		btnsubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogfederal.dismiss();
				if(cb_tick1.isChecked()) {
					cb_tick1.setChecked(false);
				}
			}
		});


	}
	@Override
	protected void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
				new IntentFilter(Trucksoft_elog_DriverConfig.ISOFT_DRIVER_APP_NOTIFICATION));

		// clearing the notification tray
		Trucksoft_elog_Notify_Utils.clearNotifications(context);
	}
	private void applogout()
	{
		Log.e("called","kk************logout");
		api =DispatchServiceGenerator.createService(Eld_api.class,context);
		String did=pref.getString(Constant.DRIVER_ID);
		String vin=pref.getString(Constant.VIN_NUMBER);
		cancelnotification();

		//  Log.e("url","&did="+did+"&name="+type);
		Call<List<Remark_model>> call = api.applogout(did,""+vin,did,""+pref.getString(Constant.CURRENT_LATITUDE),""+pref.getString(Constant.CURRENT_LOGINGITUDE));

		call.enqueue(new Callback<List<Remark_model>>() {
			@Override
			public void onResponse(Call<List<Remark_model>> call, Response<List<Remark_model>> response) {
				if(response.isSuccessful()){
//Log.e("msg","logout successfully");

				}else{


				}
			}

			@Override
			public void onFailure(Call<List<Remark_model>> call, Throwable t) {
				//Log.e("dd"," Response Error "+t.getMessage());

//setdistancealert(mils);
			}
		});
		Log.e("called","************logout1");
		pref = Preference.getInstance(context);
		//Log.e("login", "fail");
		pref.putString(Constant.LOGIN_CHECK,
				"logged_off");
		pref.putString(Constant.BLUETOOTH_CONNECTED,"0");
		pref.putString(Constant.BLUETOOTH_CONNECTED_STATUS,"0");
		pref.putString(Constant.ONDUTY_NOTIFICATION, "0");
		pref.putString(Constant.DRIVE_NOTIFICATON, "0");
		pref.putString(Constant.VIN_NUMBER, "");
		pref.putString(Constant.BLUETOOTH_ADDRESS,"");
		pref.putString(Constant.BLUETOOTH_NAME,"");
		pref.putString(Constant.NETWORK_TYPE,Constant.CELLULAR);


		try {
			finish();
			Intent mIntent = new Intent(context,
					Loginactivitynew.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mIntent.putExtra("EXIT", true);
			startActivity(mIntent);
		}catch (Exception e)
		{
			Log.e("serviceer","@"+e.toString());
		}




		// ((Activity) context). finishAndRemoveTask();
	}
	public void cancelnotification() {


		final NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mgr.cancel(NOTIFY_ME_ID_DISPATCH);

	}

	private void callprivacy()
	{
		if (dialogprivacy != null) {
			if (dialogprivacy.isShowing()) {
				dialogprivacy.dismiss();
			}
		}
		String val2=" \n  * When uploading images. \n \n " +
				" * Capture Images";
		LayoutInflater inflater = this.getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.file_privacy, null);
		final TextView btnsubmitz = dialogView.findViewById(R.id.tsubmit);
		final TextView tcancel = dialogView.findViewById(R.id.tcancel);
		final TextView lsub = dialogView.findViewById(R.id.lsub);
		lsub.setText(""+val2);
		dialogprivacy = new Dialog(context, R.style.DialogTheme);
		dialogprivacy.setCancelable(false);
		dialogprivacy.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogprivacy.setContentView(dialogView);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialogprivacy.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.CENTER;

		dialogprivacy.getWindow().setAttributes(lp);
		dialogprivacy.show();
		btnsubmitz.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogprivacy.dismiss();
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					insertDummyContactWrapper();
				}

			}
		});
		tcancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogprivacy.dismiss();
			}
		});

	}
}
