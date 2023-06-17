package com.trucksoft.isoft.isoft_elog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Isoft_activity.Report_Home;
import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.ServerResponse;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by isoft on 31/1/18.
 */

public class Month_digital_sig extends Activity{
    Toolbar toolbar;
    Button mClear, mGetSign, mCancel;
    Eld_api api;

    // Creating Separate Directory for saving Generated Images
    //new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.appname));

    String DIRECTORY;
    String StoredPath;
    private String driverid;
    Preference pref;
    private Context contxt;
    File file;
    // Dialog dialog;
    LinearLayout mContent;
    View view;
    signature mSignature;
    private Bitmap bitmap;
    int progress=0;
    ProgressDialog progressBar;
    private long stroff,strdrive,strslp;
    long stron;
    private Handler progressBarHandler = new Handler();
    ArrayList<String> arraylistschedule = new ArrayList<>();
    private Paint paint;
    ArrayList<String> arraylisttotal = new ArrayList<>();
    ProgressDialog progressDialog;
    String mediaPath;
    private EditText edtmail;
    AlertDialog.Builder builder;
    boolean boolsig=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.digit_signature);
       // Log.e("calling","signature.........");
        contxt = this;
        Intent inten=getIntent();
        arraylistschedule=inten.getStringArrayListExtra("arraylistscheduled");
      //  Log.e("calling","signature........."+arraylistschedule.toString());
        pref = Preference.getInstance(contxt);
        driverid = pref.getString(Constant.DRIVER_ID);
        this.paint = new Paint();
        // Dynamically generating Layout through java code
        mContent = (LinearLayout) findViewById(R.id.linearLayout);
        edtmail=(EditText)findViewById(R.id.txt_mail);
        mSignature = new signature(this,null);
        mSignature.setBackgroundColor(Color.WHITE);
        DIRECTORY = Environment.getExternalStorageDirectory().getPath() + File.separator + getResources().getString(R.string.appname);
        StoredPath = DIRECTORY + "/" + driverid + ".png";
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear = (Button) findViewById(R.id.clear);
        mGetSign = (Button) findViewById(R.id.getsign);
       // mGetSign.setEnabled(false);
        boolsig=false;
        mCancel = (Button) findViewById(R.id.cancel);
        view = mContent;

        file = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.appname));
//
        if (!file.exists()) {
            file.mkdir();
        }

        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Cleared");
                mSignature.clear();
              //  mGetSign.setEnabled(false);
                boolsig=false;
            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Log.e("boolsig", "Panel"+boolsig);
                if(boolsig) {
                    String mailz = edtmail.getText().toString().trim();

                    if (mailz != null && mailz.length() > 0 && !mailz.contentEquals("null")) {
                        Log.e("mailz", "Panel"+mailz);
                        if (mailz.length() > 3 && mailz.length() < 60) {
                            view.setDrawingCacheEnabled(true);
                            mSignature.save(view, StoredPath);
                        } else if (mailz.length() <= 3) {
                            Toast.makeText(getApplicationContext(), "Invalid Mail ID", Toast.LENGTH_SHORT).show();

                        } else if (mailz.length() >= 60) {
                            Toast.makeText(getApplicationContext(), "Invalid Mail ID", Toast.LENGTH_SHORT).show();

                        }
                    } else {

                        Toast.makeText(getApplicationContext(), "Please Enter Mail ID", Toast.LENGTH_SHORT).show();
                    }

                    //   Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
                    // Calling the same class
                    // recreate();
                }else{
                    Toast.makeText(getApplicationContext(), "Please Sign here", Toast.LENGTH_SHORT).show();

                }

            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");
                //finish();
//                Intent mIntent1 = new Intent(contxt, Home_activity.class);
//                startActivity(mIntent1);
                finish();
                // recreate();
            }
        });

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
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v, String StoredPath) {
           Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            Log.e("StoredPath", ": " + StoredPath);
            if (bitmap == null) {
               // Log.e("okkkkk", ": kkkkkkkk" );
                bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap);
            try {
                // Output the file
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);

                // Convert the output file to Image such as .png
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();
                myAsyncTask myWebFetch = new myAsyncTask();
                myWebFetch.execute();
//                progressBar = new ProgressDialog(contxt,
//                        AlertDialog.THEME_HOLO_LIGHT);
//
//
//                progressBar.setMessage("Please wait...");
//                progressBar.setCancelable(false);
//                progressBar.show();
//
//
//                new Thread(new Runnable() {
//
//                    public void run() {
//                        long timerEnd = System.currentTimeMillis() + 20 * 1000;
//
//                        while (timerEnd >  System.currentTimeMillis()) {
//
//                            progress = 20 - (int) (timerEnd - System.currentTimeMillis()) / 1000;
//                            // Update the progress bar
//
//                            progressBarHandler.post(new Runnable() {
//                                public void run() {
//                                    progressBar.setProgress(progress);
//                                }
//                            });
//
//                            try {
//                                Thread.sleep(500);
//                            } catch (InterruptedException e) {
//                                Log.w("App","Progress thread cannot sleep");
//                            }
//                        }
//                        progressBarHandler.post(new Runnable() {
//                            public void run() {
//                                progressBar.dismiss();
//                                myAsyncTask myWebFetch = new myAsyncTask();
//                                myWebFetch.execute();
//
//                            }
//                        });
//                    }
//                }).start();

            } catch (Exception e) {
                Log.e("log_tag", e.toString());
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
            float eventX = event.getX();
            float eventY = event.getY();
           // mGetSign.setEnabled(true);
            boolsig=true;

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

            Log.v("log_tag", string);

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


    class myAsyncTask extends AsyncTask<Void, Void, Void> {
        TextView tv;
        public ProgressDialog dialog;
        myAsyncTask()
        {
            dialog = new ProgressDialog(Month_digital_sig.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(true);
            dialog.setIndeterminate(true);
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.dismiss();


            try {
                createPDF();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
           // openDialog();
            mediaPath = DIRECTORY + "/" + pref.getString(Constant.DRIVER_ID) + ".pdf";
            progressDialog = new ProgressDialog(contxt);
            progressDialog.setMessage("Sending...");
            uploadFile();

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }
        protected Void doInBackground(Void... arg0) {
            try {
                //set the download URL, a url that points to a file on the internet
                //this is the file to be downloaded
                // Log.e("urlx","http://e-logbook.info/ExportedImages/"+driverid+".png");
                URL url = new URL("https://eld.e-logbook.info/ExportedImages/"+driverid+".png");


                //create the new connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //set up some things on the connection
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                //and connect!
                urlConnection.connect();

                //set the path where we want to save the file
                //in this case, going to save it on the root directory of the
                //sd card.
                //File SDCardRoot = Environment.getExternalStorageDirectory();
                final File dir = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.appname));
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String mPath = dir + "/" + "gr" + ".jpg";

                //create a new file, specifying the path, and the filename
                //which we want to save the file as.
                File file = new File(dir,"/" + "gr" + ".jpg");

                //this will be used to write the downloaded data into the file we created
                FileOutputStream fileOutput = new FileOutputStream(file);

                //this will be used in reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                //this is the total size of the file
                int totalSize = urlConnection.getContentLength();
                //variable to store total downloaded bytes
                int downloadedSize = 0;

                //create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0; //used to store a temporary size of the buffer

                //now, read through the input buffer and write the contents to the file
                while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                    //add the data in the buffer to the file in the file output stream (the file on the sd card
                    fileOutput.write(buffer, 0, bufferLength);
                    //add up the size so we know how much is downloaded
                    downloadedSize += bufferLength;
                    //this is where you would do something to report the prgress, like this maybe
                    //updateProgress(downloadedSize, totalSize);

                }
                //close the output stream when done
                fileOutput.close();

                //catch some possible errors...
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // see http://androidsnippets.com/download-an-http-file-to-sdcard-with-progress-notification
            return null;
        }
    }
    /************************************************************************/
    private void openDialog() {
        Toast.makeText(getApplicationContext(), "Report generated Successfully", Toast.LENGTH_SHORT).show();

        View view = View.inflate(contxt, R.layout.dialog_new, null);

        final TextView subEditText = (TextView) view.findViewById(R.id.edtmail);
        //subEditText.setVisibility(View.INVISIBLE);
        Button btnyes = (Button) view.findViewById(R.id.btnYes);
        Button btnno = (Button) view.findViewById(R.id.btnNo);
        subEditText.setText("Do you want to send email this report?");
        final Dialog dialog = new Dialog(contxt, R.style.DialogTheme);
        //dialog = new Dialog(this, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        //dialog.setMessage("Do you want to send email this report?");
//    builder.setIcon(R.drawable.email);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        btnyes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();

                mediaPath = DIRECTORY + "/" + pref.getString(Constant.DRIVER_ID) + ".pdf";
                progressDialog = new ProgressDialog(contxt);
                progressDialog.setMessage("Sending...");
                uploadFile();
//                String strmail = "trucksoft@gmail.com";
//                if (strmail != null && strmail.length() > 0) {
//
//                    String path = File.separator + getResources().getString(R.string.appname) + "/" + "DailyRep" + ".pdf";
//                    SimpleDateFormat formatz = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//
//                    String myDatez = formatz.format(new Date());
//                    // Log.e("dcx", "-" + myDatez);
//                    //date spilit
//                    String dta = null;
//                    StringTokenizer skdte;
//                    if (myDatez.contains("-")) {
//                        //  Log.e("d1", "-");
//                        skdte = new StringTokenizer(myDatez, "-");
//                        String styr = skdte.nextToken();
//                        String stmnth = skdte.nextToken();
//                        String stdte = skdte.nextToken();
//
//                        dta = stdte + "/" + getMonth(Integer.parseInt(stmnth)) + "/" + styr;
//                    }
//
//                    //Log.e("PDFCreator", "PDF Path: " + path);
//
//                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
//
//
//                    emailIntent.setType("application/pdf");
//                    // emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{strmail});
//                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Driver Inspection Report");
//                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Here attached Driver Inspection Report details copy");
//                    File root = Environment.getExternalStorageDirectory();
//                    //Log.e("root",""+root);
//                    String pathToMyAttachedFile = path;
//                    File file = new File(root, pathToMyAttachedFile);
//                    //Log.e("file",""+file);
//                    if (!file.exists() || !file.canRead()) {
//                        //Log.e("file","existes");
//                        return;
//                    }
//                    Uri uri = Uri.fromFile(file);
//                    emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                    startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
//
////                Intent intent = getIntent();
//                finish();
////                startActivity(intent);
//                }
            }
        });

        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                finish();
            }
        });


    }


    public String getMonth(int month) {
        String monthtext = null;

        switch (month) {
            case 1:
                monthtext = "Jan";
                break;
            case 2:
                monthtext = "Feb";
                break;
            case 3:
                monthtext = "Mar";
                break;
            case 4:
                monthtext = "Apr";
                break;
            case 5:
                monthtext = "May";
                break;
            case 6:
                monthtext = "Jun";
                break;
            case 7:
                monthtext = "Jul";
                break;
            case 8:
                monthtext = "Aug";
                break;
            case 9:
                monthtext = "Sep";
                break;
            case 10:
                monthtext = "Oct";
                break;
            case 11:
                monthtext = "Nov";
                break;
            case 12:
                monthtext = "Dec";
                break;
        }
        return monthtext;
    }
//    public long splittime(String time)
//    {
//
//        String timeSplit[] = time.split(":");
//        int seconds = Integer.parseInt(timeSplit[0]) * 60 * 60 +  Integer.parseInt(timeSplit[1]) * 60 ;
//
//
//        return seconds;
//    }

    public void createPDF() throws IOException, DocumentException {
        String path = null;
        //CommonUtil commonUtil = new CommonUtil();
        Document doc = new Document(PageSize.A4);
        try {
//			path = Environment.getExternalStorageDirectory().getAbsolutePath()
//					+ "/elog" + "/dailyreportpdf";

            path = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.appname);

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            Log.d("PDFCreator", "PDF Path: " + path);
            File file = new File(dir, pref.getString(Constant.DRIVER_ID) + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter.getInstance(doc, fOut);
            doc.open();


            Font mainheadin = new Font(Font.TIMES_ROMAN, 12.0f, Font.BOLD, harmony.java.awt.Color.black);

            Canvas canvas = new Canvas();
            LineSeparator line = new LineSeparator();
            /*
			 * LOGO
			 */

            PdfPTable head = new PdfPTable(1);
            head.setWidthPercentage(100);
            PdfPCell cellk;
            cellk = new PdfPCell(new Paragraph("Electronic Logbook Report", mainheadin));
            cellk.setBorder(0);
            cellk.setPaddingBottom(3);
			/*cellk.setBorderWidthLeft(1);
			cellk.setBorderWidthRight(1);*/

            cellk.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            head.addCell(cellk);
            doc.add(head);


            PdfPTable logo = new PdfPTable(1);
            logo.setWidthPercentage(100);

            PdfPTable logosign = new PdfPTable(1);
            logosign.setWidthPercentage(25);







			/*
			 * heading
			 */

            Font listheading = new Font(Font.TIMES_ROMAN, 10.0f, Font.BOLD, harmony.java.awt.Color.white);
            Font listvalues = new Font(Font.TIMES_ROMAN, 10.0f, Font.NORMAL, harmony.java.awt.Color.black);


//set driver name and date

            PdfPTable lhtab1 = new PdfPTable(7);
            lhtab1.setWidthPercentage(100);
            lhtab1.setHorizontalAlignment(0);
            PdfPCell lhcell1;

            lhcell1 = new PdfPCell(new Paragraph("Driver Name :", listvalues));
            lhcell1.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell1.setColspan(2);
            lhcell1.setBorder(0);
            lhcell1.setPadding(3);


            lhtab1.addCell(lhcell1);

//

            String dname = pref.getString(Constant.DRIVER_NAME);
            PdfPCell lh2;
            lh2 = new PdfPCell(new Paragraph("" + dname, listvalues));
            lh2.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lh2.setColspan(2);
            lh2.setBorder(0);
            lh2.setPadding(3);

            lhtab1.addCell(lh2);

            PdfPCell lh21;
            lh21 = new PdfPCell(new Paragraph("Date :", listvalues));
            lh21.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lh21.setColspan(1);
            lh21.setBorder(0);
            lh21.setPadding(3);

            lhtab1.addCell(lh21);


            SimpleDateFormat formatz = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            String myDatez = formatz.format(new Date());
            // Log.e("dcx", "-" + myDatez);
            //date spilit
            String dta = null;
            StringTokenizer skdte;
            if (myDatez.contains("-")) {
                //  Log.e("d1", "-");
                skdte = new StringTokenizer(myDatez, "-");
                String styr = skdte.nextToken();
                String stmnth = skdte.nextToken();
                String stdte = skdte.nextToken();

                dta = stdte + "/" + getMonth(Integer.parseInt(stmnth)) + "/" + styr;
            }


            PdfPCell lh11;
            lh11 = new PdfPCell(new Paragraph("" + dta, listvalues));
            lh11.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lh11.setColspan(2);
            lh11.setBorder(0);
            lh11.setPadding(3);

            lhtab1.addCell(lh11);
            doc.add(lhtab1);


            LineSeparator lik = new LineSeparator();
            doc.add(lik);


            PdfPTable head12 = new PdfPTable(1);
            head12.setWidthPercentage(100);
            PdfPCell cellk21;
            cellk21 = new PdfPCell(new Paragraph("", mainheadin));
            cellk21.setBorder(0);
            cellk21.setPaddingBottom(4);
			/*cellk.setBorderWidthLeft(1);
			cellk.setBorderWidthRight(1);*/

            cellk21.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            head12.addCell(cellk21);
            doc.add(head12);


            File dir1 = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.appname));
            String signpath=dir1+"/"+driverid+".png";

            SimpleDateFormat formatsec = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String dfg=formatsec.format(new Date());



            PdfPTable advisetab = new PdfPTable(1);
            advisetab.setWidthPercentage(100);
            PdfPCell advisecellCell;

            advisecellCell = new PdfPCell(new Paragraph(
                    "Driver Signature", listvalues));
            advisecellCell.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            // advisecellCell.setColspan(6);
            advisecellCell.setBorder(0);
            advisecellCell.setPadding(3);
            advisetab.addCell(advisecellCell);
            Bitmap bitmapsign = BitmapFactory.decodeFile(signpath);

            if (bitmapsign != null) {

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmapsign.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] buffer = stream.toByteArray();
                this.paint.setColor(getResources().getColor(R.color.bg));
                canvas.drawPaint(this.paint);
                Image image = Image.getInstance(buffer);
                image.scalePercent(12, 14);
                Log.v("Stage 6", "Image path adding");
                image.setAlignment(Image.ALIGN_RIGHT);
                image.setBorderWidth(0);
                image.setSpacingAfter(10);
                advisecellCell = new PdfPCell(image);
                advisecellCell.setPadding(3);
                // advisecellCell.setColspan(2);
                advisecellCell
                        .setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                advisecellCell.setBorder(0);
                // advisecellCell.setBorderWidthLeft(1);
                // advisecellCell.setBorderWidthRight(1);
                advisetab.addCell(advisecellCell);

            } else {
                advisecellCell = new PdfPCell(new Paragraph("", listvalues));
                advisecellCell.setPadding(3);
                // advisecellCell.setColspan(2);
                advisecellCell.setBorder(0);
                advisetab.addCell(advisecellCell);
            }

            advisecellCell = new PdfPCell(new Paragraph("(  " + dname
                    + "     )", listvalues));
            advisecellCell.setPadding(3);
            advisecellCell.setBorder(0);
            advisecellCell.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            advisetab.addCell(advisecellCell);
            advisecellCell = new PdfPCell(new Paragraph("Mobile :  " + pref.getString(Constant.DRIVER_PHONE)
                    + "       ", listvalues));
            advisecellCell.setPadding(3);
            advisecellCell.setPaddingBottom(5);
            advisecellCell.setBorder(0);
            advisecellCell.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            advisetab.addCell(advisecellCell);
            String stimezone= ""+ TimeZone.getDefault();
            TimeZone tz = TimeZone.getDefault();
            Log.e("dc","TimeZone   "+tz.getDisplayName(false, TimeZone.SHORT)+" Timezon id :: " +tz.getID());


            String fk="";
            try {
                DateFormat f1 = new SimpleDateFormat("HH:mm"); //HH for hour of the day (0 - 23)
                Date d = null;
                try {
                    d = f1.parse(dfg);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat f2 = new SimpleDateFormat("h:mma");

                fk = f2.format(d).toUpperCase();
            }catch (Exception e)
            {

            }



            advisecellCell = new PdfPCell(new Paragraph("  " + myDatez +" "+fk
                    + " " +tz.getDisplayName(false, TimeZone.SHORT)+"       ", listvalues));
            advisecellCell.setPadding(3);
            advisecellCell.setPaddingBottom(5);
            advisecellCell.setBorder(0);
            advisecellCell.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            advisetab.addCell(advisecellCell);
            doc.add(advisetab);

            LineSeparator lint = new LineSeparator();
            doc.add(lint);

/*
			 * Set the list heading
			 */
            PdfPTable lhtab = new PdfPTable(6);
            lhtab.setWidthPercentage(100);
            lhtab.setHorizontalAlignment(0);
            PdfPCell lhcell;

            lhcell = new PdfPCell(new Paragraph("DATE", listheading));
            lhcell.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lhcell.setColspan(1);
            lhcell.setBorder(0);
            lhcell.setPadding(3);
            lhcell.setBorderColor(harmony.java.awt.Color.GRAY);
            lhcell.setBackgroundColor(harmony.java.awt.Color.GRAY);
            lhcell.setBorderWidthLeft(1);
            lhtab.addCell(lhcell);

//
            PdfPCell lh;
            lh = new PdfPCell(new Paragraph("ON DUTY (Hr)", listheading));
            lh.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lh.setColspan(1);
            lh.setBorder(0);
            lh.setPadding(3);
            lh.setBorderColor(harmony.java.awt.Color.GRAY);
            lh.setBackgroundColor(harmony.java.awt.Color.GRAY);
            lhtab.addCell(lh);

            PdfPCell lh1;
            lh1 = new PdfPCell(new Paragraph("OFF DUTY (Hr)", listheading));
            lh1.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lh1.setColspan(1);
            lh1.setBorder(0);
            lh1.setPadding(3);
            lh1.setBorderColor(harmony.java.awt.Color.GRAY);
            lh1.setBackgroundColor(harmony.java.awt.Color.GRAY);
            lhtab.addCell(lh1);

            PdfPCell lh2a;
            lh2a = new PdfPCell(new Paragraph("DRIVE(Hr)", listheading));
            lh2a.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lh2a.setColspan(1);
            lh2a.setBorder(0);
            lh2a.setPadding(3);
            lh2a.setBorderColor(harmony.java.awt.Color.GRAY);
            lh2a.setBackgroundColor(harmony.java.awt.Color.GRAY);
            lhtab.addCell(lh2a);


            PdfPCell lh2b;
            lh2b = new PdfPCell(new Paragraph("SLEEP (Hr)", listheading));
            lh2b.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lh2b.setColspan(1);
            lh2b.setBorder(0);
            lh2b.setPadding(3);
            lh2b.setBorderColor(harmony.java.awt.Color.GRAY);
            lh2b.setBackgroundColor(harmony.java.awt.Color.GRAY);
            lhtab.addCell(lh2b);


            PdfPCell lh2bz;
            lh2bz = new PdfPCell(new Paragraph("ON+DR (Hr)", listheading));
            lh2bz.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            lh2bz.setColspan(1);
            lh2bz.setBorder(0);
            lh2bz.setPadding(3);
            lh2bz.setBorderColor(harmony.java.awt.Color.GRAY);
            lh2bz.setBackgroundColor(harmony.java.awt.Color.GRAY);
            lhtab.addCell(lh2bz);

            doc.add(lhtab);



            //PdfPCell lvcell;
            for (int ij = 0; ij < arraylistschedule.size(); ij++) {
                String valz=arraylistschedule.get(ij);

                StringTokenizer stk=new StringTokenizer(valz,">>");
                String date,onduty,offduty,drive,sleep;
                date=stk.nextToken();
                onduty=stk.nextToken();
                offduty=stk.nextToken();
                drive=stk.nextToken();

                long ldrive=splittime(drive);
                long londuty=splittime(onduty);
                long ltotal=ldrive+londuty;

                sleep=stk.nextToken();
                PdfPTable lvtab = new PdfPTable(6);
                lvtab.setWidthPercentage(100);
                lvtab.setHorizontalAlignment(0);


                PdfPCell lv;
                lv = new PdfPCell(new Paragraph("" + date, listvalues));
                lv.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
                lv.setColspan(1);
                lv.setPadding(3);
                lv.setBorder(0);
                lvtab.addCell(lv);

                PdfPCell lv1;
                lv = new PdfPCell(new Paragraph("" +onduty, listvalues));
                lv.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
                lv.setColspan(1);
                lv.setPadding(3);
                lv.setBorder(0);
                lvtab.addCell(lv);

                PdfPCell lv2;
                lv2 = new PdfPCell(new Paragraph("" + offduty, listvalues));
                lv2.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
                lv2.setColspan(1);
                lv2.setPadding(3);
                lv2.setBorder(0);
                lvtab.addCell(lv2);

                PdfPCell lv3;
                lv3 = new PdfPCell(new Paragraph("" + drive, listvalues));
                lv3.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
                lv3.setColspan(1);
                lv3.setPadding(3);
                lv3.setBorder(0);
                lvtab.addCell(lv3);

                PdfPCell lv4;
                lv4 = new PdfPCell(new Paragraph("" + sleep, listvalues));
                lv4.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
                lv4.setColspan(1);
                lv4.setPadding(3);
                lv4.setBorder(0);
                lvtab.addCell(lv4);

//lg
                PdfPCell lv5;
                lv5 = new PdfPCell(new Paragraph(""+printsum(ltotal) , listvalues));
                lv5.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
                lv5.setColspan(1);
                lv5.setPadding(3);
                lv5.setBorder(0);
                lvtab.addCell(lv5);

                doc.add(lvtab);
            }

            LineSeparator li = new LineSeparator();
            doc.add(li);


            PdfPTable head1 = new PdfPTable(1);
            head1.setWidthPercentage(100);
            PdfPCell cellk1;
            cellk1 = new PdfPCell(new Paragraph("", mainheadin));
            cellk1.setBorder(0);
            cellk1.setPaddingBottom(10);
			/*cellk.setBorderWidthLeft(1);
			cellk.setBorderWidthRight(1);*/

            cellk1.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            head1.addCell(cellk1);
            doc.add(head1);

//            PdfPTable lvtabsub = new PdfPTable(1);
//            lvtabsub.setWidthPercentage(100);
//            lvtabsub.setHorizontalAlignment(0);


//            PdfPCell lv5;
//            lv5 = new PdfPCell(new Paragraph("Total Driving (7 Days) : " , listvalues));
//            lv5.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
//            lv5.setColspan(1);
//            lv5.setPadding(3);
//            lv5.setBorder(0);
//            lvtabsub.addCell(lv5);
//            doc.add(lvtabsub);
//
//            PdfPCell lv5z;
//            lv5z = new PdfPCell(new Paragraph("Total ONDUTY (7 Days) : " , listvalues));
//            lv5z.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
//            lv5z.setColspan(1);
//            lv5z.setPadding(3);
//            lv5z.setBorder(0);
//            lvtabsub.addCell(lv5z);
//            doc.add(lvtabsub);

            LineSeparator liz = new LineSeparator();
            doc.add(liz);
            PdfPTable head2 = new PdfPTable(1);
            head2.setWidthPercentage(100);
            PdfPCell cellk2;
            cellk2 = new PdfPCell(new Paragraph("", mainheadin));
            cellk2.setBorder(0);
            cellk2.setPaddingBottom(5);
			/*cellk.setBorderWidthLeft(1);
			cellk.setBorderWidthRight(1);*/

            cellk2.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            head1.addCell(cellk2);
            doc.add(head2);



//            }

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

    }
    // Uploading Image/Video
    private void uploadFile() {
        progressDialog.show();

        // Map is used to multipart the file using okhttp3.RequestBody
        Log.e("path","#"+mediaPath);
        File file = new File(mediaPath);
        // File mFolder = new File(getFilesDir() + "/"+getResources().getString(R.string.appname));
        // File file = new File(mFolder.getAbsolutePath() + "/DailyRep.pdf");
        Log.e("file","#"+file.getName());
        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("application/pdf"), file.getName());
        api = ApiServiceGenerator.createService(Eld_api.class);
        Call<ServerResponse> call = api.uploadFile(fileToUpload, filename);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Log.e("success","ok");
                      //  Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("success","false");
                       // Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } else {
                    assert serverResponse != null;

                    progressDialog.dismiss();
                }
                sendemail();

                //finish();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e("Responsesdtre", t.toString());
               // sendemail();
                progressDialog.dismiss();
            }
        });
    }


    public void sendemail() {
        if (OnlineCheck.isOnline(this)) {
            api = ApiServiceGenerator.createService(Eld_api.class);
            //  Log.e("url","saveTripNo.php?vin="+vinnumber+"&lid="+"&did="+did+"&num="+msg+"&trip="+msg+"&action="+straction+"&date="+gettimeonedate());
            Call<JsonObject> call = api.sendemail("" + pref.getString(Constant.DRIVER_ID)
                    , "" + pref.getString(Constant.VIN_NUMBER), "" + edtmail.getText().toString());

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> responsez) {

                    Log.e("Responsestring", responsez.body().toString());
                    //Toast.makeText()
                    if (responsez.isSuccessful()) {
                        callalert();

                    } else {
                        callalert();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                    callalert();
                }
            });
        }
    }
    private void callalert()
    {
        builder = new AlertDialog.Builder(this);
        //Uncomment the below code to Set the message and title from the strings.xml file
        // builder.setMessage("E-mail send successfully") .setTitle("E-logbook");

        //Setting message manually and performing action on button click
        builder.setMessage("Your E-logbook report has been sent Successfully!")
                .setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogdx, int id) {
                        dialogdx.cancel();
                        Intent mIntent1 = new Intent(contxt, Report_Home.class);
                        startActivity(mIntent1);
                        finish();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("E-logbook");
        alert.show();
    }
    public long splittime(String time) {
        int seconds = 00;
//Log.e("splittime",""+time);
        if (time != null && time.length() > 0 && !time.contentEquals("null") && !time.contains("-")) {
            String timeSplit[] = time.split(":");

            seconds = Integer.parseInt(timeSplit[0]) * 60 * 60 + Integer.parseInt(timeSplit[1]) * 60;

        }
        return seconds;

    }



    public String printsum(long different) {

        //	long secondsInMilli = 1000;
        long minutesInMilli = 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        //  Log.e("elapsedDays",""+elapsedDays);

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different;

        if (String.valueOf(elapsedHours).contains("-") || String.valueOf(elapsedMinutes).contains("-")) {
            return elapsedHours + ":" + elapsedMinutes;
        } else {
            return pad(elapsedHours) + ":" + pad(elapsedMinutes);
        }

    }
    public static String pad(Long num) {
        String res = null;
        if (num < 10)
            res = "0" + num;
        else
            res = "" + num;
//Log.e("resk","@"+res);
        return res;
    }
}
