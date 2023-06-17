package com.trucksoft.isoft.isoft_elog.Multiused;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {
	private Context context;
	public CommonUtil(Context con) {
		context = con;
		
	}
	public  final String AMT_TOTAL="amt_total";
	public String ecode64(byte[] image) {
		Bitmap bitmap = decodeSampledBitmap(image);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 70, stream);
		return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
	}

	public final String[] STATUS_TYPE = new String[] { "OFF DUTY", "SLEEP", "ON DUTY" ,"DRIVING" };
	public byte[] convertStringToByte(String strPhoto) {
		byte[] buffer = null;
		if (strPhoto.length() > 0) {
			byte[] logobyte = strPhoto.getBytes();
			byte[] img = Base64.decode(logobyte, Base64.DEFAULT);
			final int buffSize = img.length;
			byte[] tempBuffer = new byte[buffSize];
			ByteArrayInputStream memstream = new ByteArrayInputStream(img);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			int size = 0;
			while ((size = memstream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
				stream.write(tempBuffer, 0, size);
			}
			buffer = stream.toByteArray();
			stream = null;
			memstream = null;
			logobyte = null;
			img = null;
			tempBuffer = null;
		}
		return buffer;
	}
	private int calculateInSampleSize(Options options, int reqWidth,
			int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	
	private Bitmap decodeSampledBitmap(byte[] image) {
		int reqWidth = 200;
		int reqHeight = 200;

		// First decode with inJustDecodeBounds=true to check dimensions
		final Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(image, 0, image.length, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(image, 0, image.length, options);
	}





	public final String DRIVER="driver";

	public final String USER_LOGIN_DETAIL="user_login_detail";
	public final String SERVER_TIME = "Server_Time";

	public final String PC_ENABLE = "pc_enable";
	public final String PC_DISABLE = "pc_disable";

	
	final String VERIFICATION_APP = "verification app";
	public final String APP_STATUS = "app_status";
	public final String APP_VERIFIED = "app_verified";
	public final String APP_NOT_VERIFIED = "app_not_verified";
	public final String VERIFY_CODE = "verify_code";
	public final String VERIFY_CODE_VALUE = "verify_value";
	
	public final String MAXTIME_APP = "maxtime app";
	public final String IMAGE_PATH = "image path";
	public final String IMAGE_LINK = "image link";
	
	public final String MAXTIME_APP_STATUS = "max_app_status";
//	public final String MAXTIME_APP_COUNT= "count";
//
     public static String TIME_DATE_VALUE= "sleep_time_value";
	public static String TIME_DATE_SCHEDULE= "sleep_time_shdule";

//	public final String MAXTIME_COUNT = "maxtime count";
	
	
	public final int MAX_MOBILE_N0_SIZE = 13;
	public final int MIN_MOBILE_N0_SIZE = 1;


	public final String NOTIFICATION_VIEW_TIME = "notify_view_time";
	public String changeinnormaldate(String date) {
		String normaldate = null;
		try {
			Date datefor = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			normaldate = new SimpleDateFormat("dd/MMM/yyyy").format(datefor);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return normaldate;
	}
	public String changedisplayinnormaldate(String date) {
		String normaldate = null;
		try {
			Date datefor = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			normaldate = new SimpleDateFormat("E, MMM d, yyyy").format(datefor);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return normaldate;
	}


	public final String ON_DUTY = "ON_DUTY";
	//public final String ON_DUTY = "ON_DUTY";
	public final String OFF_DUTY = "OFF_DUTY";
	public final String DRIVING = "DRIVING";
	public final String SLEEP = "SLEEP";


	//public final String LOGIN_ON="logged_onz";
	//public final String LOGIN_OFF="logged_offz";

	//public final String LOGIN_ON="logged_inn";
//	public final String LOGIN_OFF="logged_off";
}
