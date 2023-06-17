package com.trucksoft.isoft.isoft_elog.Model_class;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.trucksoft.isoft.isoft_elog.R;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by isoft on 26/1/18.
 */


public class Currentreportadap extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    static Preference pref;
    static Context context;
    static int ncount=0;
    static Dialog dialogrk;
    static Dialog dialogedit;
    static Dialog dialogtimer;
//    static String sktootime="00:00";
//    static String skftontime="00:00";
    List<Daily_reportmodel> movies;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    static private TextView btnSpeak;
    private final static int REQ_CODE_SPEECH_INPUT = 100;
    static  ProgressDialog dialogz;
    static private EditText name;
    static Eld_api api;
    static String strfield="";
    static Font_manager font_manager;

    static CommonUtil commonUtil;
    private static String[] statusarray;
    private static ArrayAdapter<String> statusspinner;
   static ProgressDialog progressdlog;
//    static String sktootime="00:00";
//    static String skftontime="00:00";
    private static String strfromtime="00:00";
    private  static String strtotime="00:00";
    private String strtotaltime="";
    private static int intstatus=0;
    private static String myDatez1="";
    private static String ttime="";

    private static refreshpage refreshinter;
    private static  Dialog dialogmk;
    /*
    * isLoading - to set the remote loading and complete status to fix back to back load more call
    * isMoreDataAvailable - to set whether more data from server available or not.
    * It will prevent useless load more request even after all the server data loaded
    * */


     public Currentreportadap(Context context, List<Daily_reportmodel> moviesz,String sldate,refreshpage ink) {
        this.context = context;
        pref=Preference.getInstance(context);
        this.movies = moviesz;
font_manager=new Font_manager();
commonUtil=new CommonUtil(context);
this.refreshinter=ink;
myDatez1=sldate;

         //Log.e("movies","#"+movies);
        // Log.e("calling","adapter");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new MovieHolder(inflater.inflate(R.layout.daily_adap_second,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//Log.e("position",""+position);
       // Log.e("getItemCount",""+getItemCount());
       //Log.e("ncount",""+ncount);
        if(getItemCount()<10)
        {

        }else if(getItemCount()==ncount) {
        }
        else {
            if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
                isLoading = true;
                loadMoreListener.onLoadMore();
            }
        }

        if(getItemViewType(position)==TYPE_MOVIE){
            ((MovieHolder)holder).bindData(movies.get(position));
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {
         //Log.e("movlist","q"+movies.toString());
        // Log.e("movlissdt","q"+movies.get(position).type);
        if(movies.get(position).type.equals("elog")){
            return TYPE_MOVIE;
        }else{
            return TYPE_LOAD;
        }
    }

    @Override
    public int getItemCount() {
        //Log.e("mov.size",""+movies.size());
        return movies.size();
    }

    /* VIEW HOLDERS */

    static class MovieHolder extends RecyclerView.ViewHolder{

        TextView txt_status;
        TextView txt_from;
        TextView txt_to;
        TextView txt_duration;
        TextView txt_address;
        TextView txt_remark;
        TextView txt_trip;
        TextView txt_cmdty;
        TextView txt_lid;
        TextView txt_vid;TextView txt_vin;
        LinearLayout lik;
        ImageView imgactive;
        ImageView imgconnection;
        TextView txtedit;

        private TextView txttrack;
        public MovieHolder(View itemView) {
            super(itemView);
             txt_status =  itemView
                    .findViewById(R.id.tstatus);
             txt_from =  itemView.findViewById(R.id.txtfrom);
             txt_to =  itemView.findViewById(R.id.txtto);
             txt_duration=  itemView.findViewById(R.id.txtduration);
             txt_address =  itemView.findViewById(R.id.taddress);
            txt_remark =  itemView.findViewById(R.id.tremark);
            txt_trip =  itemView.findViewById(R.id.ttrip);
            txt_vid =  itemView.findViewById(R.id.txt_vid);
            txt_vin = itemView.findViewById(R.id.txt_vin);
            txt_lid = itemView.findViewById(R.id.lid);
            txt_cmdty= itemView.findViewById(R.id.tcmdty);
            imgactive= itemView.findViewById(R.id.imgactive);
            imgconnection=itemView.findViewById(R.id.imgconnection);
            lik=itemView.findViewById(R.id.monthn);
            txtedit = itemView.findViewById(R.id.txt_edit);
            txtedit.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));





        }

        void bindData(Daily_reportmodel movieModel){
            String dstatusname=movieModel.status;
            ncount=movieModel.tcount;
            txt_from.setText(movieModel.ftime);
            txt_to.setText(movieModel.ttime);
            txt_duration.setText(movieModel.dtime);
            txt_address.setText(movieModel.address);
         //   txt_address.setText(movieModel.address);
           // Log.e("ftime","@"+movieModel.ftime);
            //Log.e("remark","@"+movieModel.remark);
            if(movieModel.comm !=null && movieModel.comm.length()>0)
            {
                txt_cmdty.setText(""+movieModel.comm);
            }else{
                txt_cmdty.setText(""+"Property");
            }
            if(movieModel.remark !=null && movieModel.remark.length()>0)
            {
                txt_remark.setText(""+movieModel.remark);
                txt_remark.setBackgroundResource(android.R.color.white);

                if (Build.VERSION.SDK_INT >=  23) {
                    txt_remark.setTextColor(ContextCompat.getColor(context, R.color.companycolor));
                }else {
                    txt_remark.setTextColor(context.getResources().getColor(R.color.companycolor));
            }
            }else{
                txt_remark.setText("Add Remark");
                try {
                    txt_remark.setBackgroundResource(R.drawable.remark_btn);
                    if (Build.VERSION.SDK_INT >=  23) {
                        txt_remark.setTextColor(ContextCompat.getColor(context, R.color.white));
                    }else {
                        txt_remark.setTextColor(context.getResources().getColor(R.color.white));
                    }
                }catch (Exception e)
                {

                }
            }

            txt_cmdty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commoditypopup(""+movieModel.lid,""+movieModel.comm,txt_cmdty);
                }
            });

            ///trip
            if(movieModel.tripnum !=null && movieModel.tripnum.length()>0)
            {

                if (movieModel.tripnum.contentEquals("Add")) {
                    txt_trip.setText("Add Ship. Doc. #");
                    try {
                        txt_trip.setBackgroundResource(R.drawable.remark_btn);
                        if (Build.VERSION.SDK_INT >=  23) {
                            txt_trip.setTextColor(ContextCompat.getColor(context, R.color.white));
                        }else {
                            txt_trip.setTextColor(context.getResources().getColor(R.color.white));
                        }
                    }catch (Exception e)
                    {

                    }

                }else {
                    txt_trip.setText("" + movieModel.tripnum);
                    txt_trip.setBackgroundResource(android.R.color.white);

                    if (Build.VERSION.SDK_INT >= 23) {
                        txt_trip.setTextColor(ContextCompat.getColor(context, R.color.companycolor));
                    } else {
                        txt_trip.setTextColor(context.getResources().getColor(R.color.companycolor));
                    }
                }
            }else{
                txt_trip.setText("Add Ship. Doc. #");
                try {
                    txt_trip.setBackgroundResource(R.drawable.remark_btn);
                    if (Build.VERSION.SDK_INT >=  23) {
                        txt_trip.setTextColor(ContextCompat.getColor(context, R.color.white));
                    }else {
                        txt_trip.setTextColor(context.getResources().getColor(R.color.white));
                    }
                }catch (Exception e)
                {

                }
            }


            if(movieModel.edited_status !=null && movieModel.edited_status.length()>0)
            {
                if(movieModel.edited_status.contentEquals("true"))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imgactive.setImageDrawable(context.getResources().getDrawable(R.drawable.yello, context.getTheme()));
                    } else {
                        imgactive.setImageDrawable(context.getResources().getDrawable(R.drawable.yello));
                    }
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imgactive.setImageDrawable(context.getResources().getDrawable(R.drawable.grey, context.getTheme()));
                    } else {
                        imgactive.setImageDrawable(context.getResources().getDrawable(R.drawable.grey));
                    }
                }
            }


            if(movieModel.connection !=null && movieModel.connection.length()>0)
            {
                if(movieModel.connection.contentEquals("0"))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imgconnection.setImageDrawable(context.getResources().getDrawable(R.drawable.cellular_icon, context.getTheme()));
                    } else {
                        imgconnection.setImageDrawable(context.getResources().getDrawable(R.drawable.cellular_icon));
                    }
                }else if(movieModel.connection.contentEquals("1"))
                    {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imgconnection.setImageDrawable(context.getResources().getDrawable(R.drawable.bluetoothicon, context.getTheme()));
                    } else {
                        imgconnection.setImageDrawable(context.getResources().getDrawable(R.drawable.bluetoothicon));
                    }
                }
            }



            String word=""+movieModel.vin;
           // Log.e("vin",""+movieModel.lid);
            txt_lid.setText(""+movieModel.lid);
            String myString="";

            if(word !=null && word.length()>6 && !word.contentEquals("null")) {
                 myString = word.substring(word.length()-6, word.length());

            }else if(word !=null && word.length()>0 && !word.contentEquals("null")) {
                myString=word;
            }
            txt_vin.setText(""+myString);
            String vkid=""+movieModel.vid;
            if(vkid !=null && vkid.length()>0 && !vkid.contentEquals("null"))
            {

            }else{
                vkid="";
            }
            txt_vid.setText(""+vkid);
           // Log.e("lastindex","#"+movieModel.lastindex);
            pref.putString(Constant.LAST_INDEX,""+movieModel.lastindex);
            String fnamez=movieModel.message;
            String ststatus="";
            if (fnamez.contentEquals("ON_DUTY")) {
                ststatus = "ON";
            } else if (fnamez.contentEquals("OFF_DUTY")) {
                ststatus = "OFF";
            } else if (fnamez.contentEquals("DRIVING")) {
                ststatus = "D";
            } else {
                ststatus = "SB";
            }

            if (ststatus.contentEquals("ON")) {
                txt_status.setBackgroundResource(R.color.inroute);
            } else if (ststatus.contentEquals("OFF")) {
                txt_status.setBackgroundResource(R.color.Red);
            } else if (ststatus.contentEquals("D")) {
                txt_status.setBackgroundResource(R.color.golden);
            } else {
                txt_status.setBackgroundResource(R.color.dd);
            }



            txt_status.setText(ststatus);
//            lik.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    responseremark(""+txt_status.getText().toString().trim(),txt_lid.getText().toString().trim(),txt_remark.getText().toString().trim(),txt_remark);
//                }
//            });
txtedit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
Log.e("mdftime","&"+movieModel.ftime);
        Log.e("mdttime","&"+movieModel.ttime);
        setedit(myDatez1,movieModel.ftime,movieModel.ttime,movieModel.dtime,movieModel.lid,movieModel.remark,movieModel.message);
    }
});
            txt_remark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    responseremark(""+txt_status.getText().toString().trim(),txt_lid.getText().toString().trim(),txt_remark.getText().toString().trim(),txt_remark);
                }
            });

//            if(movieModel.devicetype !=null && movieModel.devicetype.contentEquals("1"))
//            {
//                txt_trip.setVisibility(View.VISIBLE);
//                refreshinter.refreshrvisible(""+movieModel.devicetype);
//            }  else if(movieModel.devicetype !=null && movieModel.devicetype.contentEquals("0"))
//            {
//                txt_trip.setVisibility(View.GONE);
//                refreshinter.refreshrvisible(""+movieModel.devicetype);
//            }
//            else{
//                txt_trip.setVisibility(View.INVISIBLE);
//                refreshinter.refreshrvisible(""+movieModel.devicetype);
//            }
            txt_trip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//if(txt_trip.getText().toString() !=null && txt_trip.getText().toString().contentEquals("N/A")) {
//
//}else{
                    String straction="save";
                    try {
                        if (movieModel.tripnum.contentEquals("Add") ||
                                movieModel.tripnum.contentEquals("N/A") ||
                                movieModel.tripnum.contentEquals("n/a")) {
                            straction = "save";
                        } else {
                            straction = "change";
                        }
                    }catch (Exception e)
                    {

                    }

    responsetrip(txt_lid.getText().toString().trim(), txt_trip.getText().toString().trim(), txt_remark, movieModel.trip_id, movieModel.tripnum,straction);

//}
                }
            });
        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }


    interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
    public static  void responseremark(String status, final String rowid, final String remarktxt,final TextView txtmark){
        if(dialogrk !=null) {
            if (dialogrk.isShowing()) {
                dialogrk.dismiss();
            }
        }

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // final View dialogView = inflater.inflate(R.layout.remark_popup_new, null);
        final View dialogView = inflater.inflate(R.layout.remark_edit, null);

        name = dialogView.findViewById(R.id.rmark);
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        btnSpeak =  dialogView.findViewById(R.id.btnspeak);

        final LinearLayout linbrk = dialogView.findViewById(R.id.linbreak);
        if (status != null && status.length() > 0 && !status.contentEquals("null")) {
            if (status.contentEquals("DRIVING")) {
                linbrk.setVisibility(View.GONE);
            }
        }
        final Button btncancel = dialogView.findViewById(R.id.btn_cancel);
        final ImageView imgstatus=dialogView.findViewById(R.id.txt_img);
        final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialogrk = new Dialog(context, R.style.DialogTheme);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        if (status != null && status.length() > 0 && !status.contentEquals("null"))
        {
            if (status.contentEquals("ON")) {
                status = "ON DUTY";
            } else if (status.contentEquals("D")) {
                status = "DRIVING";
            } else if (status.contentEquals("SB")) {
                status = "SLEEP";
            } else if (status.contentEquals("OFF")) {
                status = "OFF DUTY";
                // status="DRIVING";
            }else{
                status = "DRIVING";
            }
        }else {
            status = "OFF DUTY";
        }
        if(status.contentEquals("ON DUTY")) {
            imgstatus.setBackgroundResource(R.drawable.timeimg);
        }else if(status.contentEquals("DRIVING"))
        {
            imgstatus.setBackgroundResource(R.drawable.driveimg);
        }else if(status.contentEquals("SLEEPER"))
        {
            imgstatus.setBackgroundResource(R.drawable.sleepimg);
        }else if(status.contentEquals("SLEEP"))
        {
            imgstatus.setBackgroundResource(R.drawable.sleepimg);
        }else if(status.contentEquals("OFF DUTY"))
        {
            imgstatus.setBackgroundResource(R.drawable.offimg);
        }
        if(remarktxt !=null && remarktxt.contentEquals("Add Remark"))
        {
            name.setText("");
        }else{



                name.setText("" + remarktxt);

        }
        txtstatus.setText("STATUS : "+status);
        dialogrk.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogrk.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogrk.getWindow().getAttributes());
       // lp.width = WindowManager.LayoutParams.MATCH_PARENT;
       // lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.70);
        int height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.90);
        lp.width = width;
        lp.height = height;
        dialogrk.getWindow().setAttributes(lp);
        dialogrk.show();
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogz = new ProgressDialog(context,
                        AlertDialog.THEME_HOLO_LIGHT);
                dialogz.setMessage("Please wait...");
                dialogz.setCancelable(false);
                dialogz.show();

String rmark=""+name.getText().toString().trim();
                if(rmark !=null &&rmark.length()>0)
                {
                    txtmark.setText(""+rmark);
                    txtmark.setBackgroundResource(android.R.color.white);

                    if (Build.VERSION.SDK_INT >=  23) {
                        txtmark.setTextColor(ContextCompat.getColor(context, R.color.companycolor));
                    }else {
                        txtmark.setTextColor(context.getResources().getColor(R.color.companycolor));
                    }
                }else {

                    txtmark.setText(""+rmark);
                }











                String did=pref.getString(Constant.DRIVER_ID);
               String vinnumber=""+pref.getString(Constant.VIN_NUMBER);
                String msg=name.getText().toString().trim();
                api = ApiServiceGenerator.createService(Eld_api.class);
                //Log.e("url","remarkupdate.php?vin="+vinnumber+"&lid="+rowid+"&msg="+msg+"&did="+did);
                Call<List<Remark_model>> call = api.updateremark(vinnumber,""+rowid,""+msg,did);

                call.enqueue(new Callback<List<Remark_model>>() {
                    @Override
                    public void onResponse(Call<List<Remark_model>> call, Response<List<Remark_model>> response) {
                        if(response.isSuccessful()){
                          //  Log.e("field",""+strfield);
                            responseremark(strfield,rowid,remarktxt,txtmark);
                            dialogrk.dismiss();
                            dialogz.dismiss();
                        }else{

                            dialogz.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Remark_model>> call, Throwable t) {
                       // Log.e("dd"," Response Error "+t.getMessage());
                        dialogz.dismiss();

                    }
                });

            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogrk.dismiss();

                // setdistancealert();
                //setondutyalert();
            }
        });

    }
    /**
     * Showing google speech input dialog
     * */
    private static void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                context.getString(R.string.speech_prompt));
        try {
            ((Activity) context).startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);
           // context.startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(context,
                    context.getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }


    private static void setedit(String myDatez1, String ftime,  String ttimez, String dtime, String lid, String remark, String status)
    {
        try {
            if (dialogedit != null) {
                if (dialogedit.isShowing()) {
                    dialogedit.dismiss();
                }
            }
        }catch (Exception e)
        {

        }
        ttime=ttimez;
        Log.e("ttime0","*"+ttime);
        if(ttime==""|| ttime.contentEquals("null"))
        {
            ttime=gettimezonetimeampm();
        }
        Log.e("ttime1111","*"+ttime);
        Log.e("dtime","*"+dtime);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      //  LayoutInflater inflater = context.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_log, null);


        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final Button btncancel = dialogView.findViewById(R.id.btn_cancel);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
        final TextView tDate = dialogView.findViewById(R.id.t_Date);
        final Spinner spinstatus = dialogView.findViewById(R.id.id_status);
        final TextView tfromtime = dialogView.findViewById(R.id.edt_fromtime);
        final TextView ttotime = dialogView.findViewById(R.id.edt_totime);
        final TextView ttotaltime = dialogView.findViewById(R.id.edt_totaltime);
        final EditText tremark = dialogView.findViewById(R.id.edt_remark);
        tDate.setText(""+myDatez1);
        tfromtime.setText(""+ftime);

        if(ttime !=null && ttime.length()>0 && dtime !=null && dtime.length()>0) {
            ttotime.setText("" + ttime);
            ttotaltime.setText("" + dtime);
        }else{
            if((ttime ==null || ttime.length()==0) ||dtime ==null || dtime.length()==0 )
            {
//                SimpleDateFormat formatsecd = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
//               String vc = formatsecd.format(new Date()).toLowerCase();
//
               //timeone change
                String vc=gettimezonetimeampm();
                ttotime.setText("" + vc);

                ttotaltime.setText("" + vc);
//                if(strtotime.contentEquals("00:00"))
//                {

//                }
String fk="";
                String vk="";
                if(ftime.contains("am") || ftime.contains("pm"))
                {
                    fk=convert24time(ftime);
                    vk=convert24time(vc);
                    //Log.e("fggf1","@"+fk);
                }
                try{
                   long ab=splittime(fk);
                   long ac=splittime(vk);
                 ttotaltime.setText(""+getDurationString((int) (ac-ab)));


                }catch (Exception e)
                {

                }
            }
        }


        statusarray=commonUtil.STATUS_TYPE;
        statusspinner= new ArrayAdapter<String>(context, R.layout.spinner_txt,
                commonUtil.STATUS_TYPE);
        spinstatus.setAdapter(statusspinner);

        if(status.contentEquals("SLEEP"))
        {
            spinstatus.setSelection(1);
        } if(status.contentEquals("ON_DUTY"))
    {
        spinstatus.setSelection(2);
    }if(status.contentEquals("DRIVING"))
    {
        spinstatus.setSelection(3);
    }
        dialogedit = new Dialog(context, R.style.DialogTheme);
        dialogedit.setCancelable(false);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));

        dialogedit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogedit.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogedit.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
try {
    dialogedit.getWindow().setAttributes(lp);
    dialogedit.show();
}catch (Exception e)
{

}
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogedit.dismiss();
            }
        });
        tfromtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strfromtime=tfromtime.getText().toString();
                strtotime=ttotime.getText().toString();
                intstatus=0;
              //  calltimerfrom(tfromtime,ftime,ttotaltime,dtime,tremark,remark,ttime,myDatez1);
                calltimerfrom(tfromtime,tfromtime.getText().toString(),ttotaltime,dtime,tremark,remark,ttime,myDatez1);


            }
        });
        ttotime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intstatus=1;
                strfromtime=tfromtime.getText().toString();
                strtotime=ttotime.getText().toString();
                Log.e("ttotime00",""+ttotime.getText().toString());
                calltimerto(ttotime,ttotime.getText().toString(),ttotaltime,dtime,tremark,remark,ftime,myDatez1);
            }
        });
        txtalert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogedit.dismiss();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rmark="";
                if(tremark.getText().toString() !=null && tremark.getText().toString().length()>0)
                {
                    rmark=tremark.getText().toString()+" - "+status;
                }else{
                   // tremark.setText(""+status);
                    rmark=status;
                }
                String stat="";
                if(spinstatus.getSelectedItem().toString().contentEquals("OFF DUTY"))
                {
                    stat="OFF_DUTY";
                }else if(spinstatus.getSelectedItem().toString().contentEquals("SLEEP")){
                    stat="SLEEP";
                }else if(spinstatus.getSelectedItem().toString().contentEquals("ON DUTY")){
                    stat="ON_DUTY";
                }else if(spinstatus.getSelectedItem().toString().contentEquals("DRIVING")){
                    stat="DRIVING";
                }else{
                    stat="OFF_DUTY";
                }
//, "SLEEP", "ON_DUTY" ,"DRIVING"
                saveeditdata(myDatez1,myDatez1+" "+tfromtime.getText().toString(),myDatez1+" "+ttotime.getText().toString(),ttotaltime.getText().toString()
                ,stat,rmark);

//                saveeditdata(myDatez1,myDatez1+" "+strfromtime,myDatez1+" "+strtotime,ttotaltime.getText().toString()
//                        ,stat,rmark);

                dialogedit.dismiss();

            }
        });

    }


    public static long splittime(String time) {
        int seconds = 00;
        //Log.e("splittimekk",""+time);
        try {
//Log.e("splittime",""+time);
            if (time != null && time.length() > 0 && !time.contentEquals("null") && !time.contains("-")) {
                String timeSplit[] = time.split(":");

                seconds = Integer.parseInt(timeSplit[0]) * 60 * 60 + Integer.parseInt(timeSplit[1]) * 60;

            }
        }catch (Exception e)
        {
            //Log.e("eeee",""+e.toString());
        }
        //Log.e("seconds",""+seconds);
        return seconds;

    }

    private static void calltimerfrom(TextView tfromtime, String ftimegg, TextView ttotaltime, String dtime, EditText tremark, String remark, String ttimed, String myDatez1)
    {
        try {
            if (dialogtimer != null) {
                if (dialogtimer.isShowing()) {
                    dialogtimer.dismiss();
                }
            }
        }catch (Exception e)
        {

        }
        if(ttimed ==null || ttimed.length()==0)
        {
            SimpleDateFormat formatsecd = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
            strtotime = formatsecd.format(new Date()).toLowerCase();
        }

      ////  skftontime=ftimegg;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //  LayoutInflater inflater = context.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.timer_dialog, null);

        final TimePicker tp = dialogView.findViewById(R.id.tp);
        tp.setIs24HourView(false);



        try
        {


            DateFormat df = new SimpleDateFormat("hh:mm aa");

            DateFormat outputformat = new SimpleDateFormat("HH:mm");
            Date date = null;

            String fromtime = null;
            //Log.e("lk","@"+strfromtime);
if(strfromtime.contains("am")|| strfromtime.contains("pm"))
{
    fromtime = convert24time(strfromtime);
}else{
    fromtime=strfromtime;
}
//            try{
//                //Converting the input String to Date
//              ////  date= df.parse(skftontime);
//                date= df.parse(strfromtime);
//                fromtime = outputformat.format(date);
//                Log.e("fromtime","@@"+fromtime);
//            }catch (Exception e) {
//                Log.e("Exceptionzzz","@"+e.toString());
//            }








            StringTokenizer skrt=new StringTokenizer(fromtime,":");
            String hr=skrt.nextToken();
            //Log.e("hrhr","@@"+hr);
            String min="00";

            if(skrt.hasMoreTokens())
            {
                min =skrt.nextToken();
                //Log.e("minv","@@"+min);

            }else{
                //Log.e("val","@@");
            }
            //Log.e("minmin","@@"+min);
            tp.setCurrentHour(Integer.valueOf(hr));
            tp.setCurrentMinute(Integer.valueOf(min));
        }catch (Exception e)
        {

        }





        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
//        final TextView tDate = dialogView.findViewById(R.id.t_Date);
        dialogtimer = new Dialog(context, R.style.DialogTheme);
        dialogtimer.setCancelable(true);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
       txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));

        dialogtimer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogtimer.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogtimer.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogtimer.getWindow().setAttributes(lp);
        dialogtimer.show();

        txtalert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogtimer.dismiss();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Log.e("ftime",""+skftontime);
                //Log.e("strfromtime","@"+strfromtime);
                //Log.e("ttime","@"+strtotime);





                long anewfromtime=00;
                long afromtime=00;
                long aendtime=00;
                DateFormat df = new SimpleDateFormat("hh:mm aa");
                //Desired format: 24 hour format: Change the pattern as per the need
                DateFormat outputformat = new SimpleDateFormat("HH:mm");
                Date date = null;
                Date tdate = null;
                Date cdate = null;
                String fromtime = null;
                String tootime = null;
                String current = null;
                try{
                    //Converting the input String to Date
                    ////date= df.parse(skftontime);
                    if(strfromtime.contains("am")|| strfromtime.contains("pm")) {
                        date = df.parse(strfromtime);
                        fromtime = outputformat.format(date);
                        //Log.e("success", "@1");
                    }else{
                        fromtime =strfromtime;
                    }
                    tdate= df.parse(strtotime);
                    //Log.e("success","@2");
                    //Changing the format of date and storing it in String

                    //Log.e("success","@3");
                    tootime = outputformat.format(tdate);
                    //Displaying the date
                    //Log.e("outputvvvv","@"+fromtime);
                    //Log.e("tootime","@"+tootime);
                     afromtime=splittime(fromtime);
                     aendtime=splittime(tootime);

                    if(strfromtime.contains("am") || strfromtime.contains("pm") || strfromtime.contains("AM"))
                    {
                        cdate= df.parse(strfromtime);
                        current = outputformat.format(cdate);
                        anewfromtime=splittime(current);
                    }else{
                        anewfromtime=splittime(strfromtime);
                    }
if(strtotime=="" || strtotime.contentEquals("null") || strtotime.length()==0)
{
    strtotime=gettimezonetime();
}



                    if((afromtime<=anewfromtime) && (anewfromtime<=aendtime)) {
                        dialogtimer.dismiss();
                        tfromtime.setText(""+convert12time(strfromtime));
                        if(strfromtime.contains("am") || strfromtime.contains("pm") || strfromtime.contains("AM")) {
                            Log.e("mk1","@@"+myDatez1+" "+convert24time(strfromtime));
                            Log.e("mk2","@@"+myDatez1+" "+strtotime);
                            String a = printDifference(myDatez1 + " " + convert24time(strfromtime), myDatez1 + " " + strtotime);
                            ttotaltime.setText("" + a);
                        }else{
                            Log.e("mk3","@@"+myDatez1+" "+strfromtime);
                            Log.e("mk4","@@"+myDatez1+" "+strtotime);
                            String a = printDifference(myDatez1 + " " + strfromtime, myDatez1 + " " + strtotime);
                            ttotaltime.setText("" + a);
                        }


                }
                else{
                    Toast.makeText(context,"Please select correct time",Toast.LENGTH_SHORT).show();
                }
                }catch(ParseException pe){
                    pe.printStackTrace();
                    //Log.e("Exceptionzzzpe","@"+pe.toString());
                }











            }
        });
       // tp.setEnabled(false);

        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                //Display the new time to app interface
//                String AMPM = "AM";
//                if(hourOfDay>11) {
//                    tp.setEnabled(false);
//                }else{
//                    tp.setEnabled(true);
//                }
//                    hourOfDay = hourOfDay-12;
//                    AMPM = "PM";
//                }
              //  tfromtime.setText("" + hourOfDay + ":" + minute );
                Log.e("tiechange","@@@@@@@@@"+ hourOfDay + ":" + minute);
                if(myDatez1.contentEquals(""+gettimeonedate())) {
                    int zonehr = Integer.parseInt(gettimezonetimehour());
                    int zoneminute = Integer.parseInt(gettimezonetimeminute());
                    if (hourOfDay > zonehr) {
                        if (dialogmk != null) {
                            if (dialogmk.isShowing()) {
                                dialogmk.dismiss();
                            }
                        }
                        calltimealert();
                        try {
                            StringTokenizer skrt = new StringTokenizer(gettimezonetime(), ":");
                            String hr = skrt.nextToken();
                            //Log.e("hrhr","@@"+hr);
                            String min = "00";

                            if (skrt.hasMoreTokens()) {
                                min = skrt.nextToken();
                                //Log.e("minv","@@"+min);

                            } else {
                                //Log.e("val","@@");
                            }
                            //Log.e("minmin","@@"+min);
                            tp.setCurrentHour(Integer.valueOf(hr));
                            tp.setCurrentMinute(Integer.valueOf(min));
                            strfromtime = "" + gettimezonetime();
                        } catch (Exception e) {

                        }
                    } else if (hourOfDay == zonehr && minute > zoneminute) {
                        if (dialogmk != null) {
                            if (dialogmk.isShowing()) {
                                dialogmk.dismiss();
                            }
                        }
                        calltimealert();
                        try {
                            StringTokenizer skrt = new StringTokenizer(gettimezonetime(), ":");
                            String hr = skrt.nextToken();
                            //Log.e("hrhr","@@"+hr);
                            String min = "00";

                            if (skrt.hasMoreTokens()) {
                                min = skrt.nextToken();
                                //Log.e("minv","@@"+min);

                            } else {
                                //Log.e("val","@@");
                            }
                            //Log.e("minmin","@@"+min);
                            tp.setCurrentHour(Integer.valueOf(hr));
                            tp.setCurrentMinute(Integer.valueOf(min));
                            strfromtime = "" + gettimezonetime();
                        } catch (Exception e) {

                        }
                    } else {
                        strfromtime = "" + hourOfDay + ":" + minute;
                    }
                }else{
                    strfromtime = "" + hourOfDay + ":" + minute;
                }


            }

        });

    }

    private static void calltimerto(TextView ttotime,  final String ttimeedweed, TextView ttotaltime, String dtime, EditText tremark, String remark, String ftime, String myDatez1)
    {
        if (dialogtimer != null) {
            if (dialogtimer.isShowing()) {
                dialogtimer.dismiss();
            }
        }
        if(ttimeedweed ==null || ttimeedweed.length()==0)
        {
//            SimpleDateFormat formatsecd = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
//            strtotime = formatsecd.format(new Date()).toLowerCase();
            //timezone change
           strtotime=gettimezonetimeampm();
        }

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //  LayoutInflater inflater = context.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.timer_dialog, null);

        final TimePicker tp = dialogView.findViewById(R.id.tp);
        tp.setIs24HourView(false);
        DateFormat df = new SimpleDateFormat("hh:mm aa");
        DateFormat outputformat = new SimpleDateFormat("HH:mm");
        Date date = null;

        String fggf = null;

        try{
            //Converting the input String to Date
          ////  date= df.parse(sktootime);
            Log.e("strtotimestrtotime","@@"+strtotime);
            date= df.parse(strtotime);
            fggf = outputformat.format(date).toLowerCase();
            Log.e("fggf","@@"+fggf);
        }catch (Exception e) {
            //Log.e("Exceptionzzz","@"+e.toString());
        }





        try
        {


            Log.e("ttime","@@"+fggf);
            StringTokenizer skrt=new StringTokenizer(fggf,":");
            String hr=skrt.nextToken();
            //Log.e("hrhr","@@"+hr);
            String min="00";

            if(skrt.hasMoreTokens())
            {
                min =skrt.nextToken();


            }
            //Log.e("minmin","@@"+min);
            tp.setCurrentHour(Integer.valueOf(hr));
            tp.setCurrentMinute(Integer.valueOf(min));
        }catch (Exception e)
        {

        }
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);
        final TextView txtalert = dialogView.findViewById(R.id.txtalert);
//        final TextView tDate = dialogView.findViewById(R.id.t_Date);
        dialogtimer = new Dialog(context, R.style.DialogTheme);
        dialogtimer.setCancelable(false);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));

        dialogtimer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogtimer.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogtimer.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialogtimer.getWindow().setAttributes(lp);
        dialogtimer.show();

        txtalert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogtimer.dismiss();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  long aendtime=splittime(ttime);




                String cdatez ="";
                try
                {
                    SimpleDateFormat formatz1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    cdatez = formatz1.format(new Date());
                }catch (Exception e)
                {

                }
                String fggf = null;
                //Log.e("strfromtime---","@"+strfromtime);
                //Log.e("strtotime---","@"+strtotime);
                if(strtotime ==null || strtotime.length()==0 )
                {
                    SimpleDateFormat formatsecd = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    strtotime = formatsecd.format(new Date());
                  ////  sktootime = formatsecd.format(new Date());
                }else{
                    if(strtotime.contentEquals("00:00"))
                    {
                        SimpleDateFormat formatsecd = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                        strtotime = formatsecd.format(new Date()).toLowerCase();
                    }

                    if(strtotime.contains("am") || strtotime.contains("pm"))
                    {
                        fggf=convert24time(strtotime);
                        //Log.e("fggf1","@"+fggf);
                    }else{
                        fggf=  strtotime;
                        //Log.e("fggf2","@"+fggf);
                    }

                }
                long anewendtime=splittime(fggf);

                long afromtime=00;

                DateFormat df = new SimpleDateFormat("hh:mm aa");
                //Desired format: 24 hour format: Change the pattern as per the need
                DateFormat outputformat = new SimpleDateFormat("HH:mm");
                Date date = null;
                Date tdate = null;
                String fromtime = null;
                String tootime = null;
                try{
                    //Converting the input String to Date
                   // SimpleDateFormat formatsec = new SimpleDateFormat("HH:mm", Locale.getDefault());
                   // String dfg = formatsec.format(new Date());
                    //long currenttime=splittime(dfg);
                    //timezone related change
                    long currenttime=splittime(gettimezonetime());
                    date= df.parse(ftime);

                    if(strtotime.contains("am")|| strtotime.contains("pm"))
                    {
                        tdate= df.parse(strtotime);
                        tootime = outputformat.format(tdate);
                    }else{
                        tootime=strtotime;
                    }

                    //Changing the format of date and storing it in String
                    fromtime = outputformat.format(date);

                    //Displaying the date
                    //Log.e("outputvvvv","@"+fromtime);
                    //Log.e("tootime","@"+tootime);
                    afromtime=splittime(fromtime);


                    if(!myDatez1.contentEquals(cdatez))
                    {
                        //  Log.e("astarttime","@"+astarttime);
                        //Log.e("anewendtime","@"+anewendtime);
                        if (afromtime <= anewendtime) {
                            dialogtimer.dismiss();
                            ttotime.setText(""+convert12time(strtotime));
                            String a = printDifference(myDatez1+" "+convert24time(strfromtime), myDatez1+" "+fggf);
                            ttotaltime.setText(""+a);
                        } else {
                            Toast.makeText(context, "Please select correct time..", Toast.LENGTH_SHORT).show();
                        }
                    }else {

                        //Log.e("anewendtimev","@"+strtotime);
                        //Log.e("afromtime","@"+afromtime);
                        //Log.e("anewendtime","@"+anewendtime);
                        //Log.e("currenttime","@"+currenttime);
                        if ((afromtime <= anewendtime) && (anewendtime <= currenttime)) {
                            dialogtimer.dismiss();

                            ttotime.setText(""+convert12time(strtotime));
                            String a = printDifference(myDatez1+" "+convert24time(strfromtime), myDatez1+" "+fggf);
                            ttotaltime.setText(""+a);
                        } else {
                            Toast.makeText(context, "Please select correct time", Toast.LENGTH_SHORT).show();
                        }
                    }






                }catch(ParseException pe){
                    pe.printStackTrace();
                    //Log.e("Exceptionzzzpe","@"+pe.toString());
                }

















            }
        });
        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                //Display the new time to app interface

//                if(hourOfDay >12)
//                {
//                    tp.setCurrentHour(hourOfDay-1);
//                }
if(myDatez1.contentEquals(""+gettimeonedate())) {
    int zonehr = Integer.parseInt(gettimezonetimehour());
    int zoneminute = Integer.parseInt(gettimezonetimeminute());
    if (hourOfDay > zonehr) {
        if (dialogmk != null) {
            if (dialogmk.isShowing()) {
                dialogmk.dismiss();
            }
        }
        calltimealert();
        try {
            StringTokenizer skrt = new StringTokenizer(gettimezonetime(), ":");
            String hr = skrt.nextToken();
            //Log.e("hrhr","@@"+hr);
            String min = "00";

            if (skrt.hasMoreTokens()) {
                min = skrt.nextToken();
                //Log.e("minv","@@"+min);

            } else {
                //Log.e("val","@@");
            }
            //Log.e("minmin","@@"+min);
            tp.setCurrentHour(Integer.valueOf(hr));
            tp.setCurrentMinute(Integer.valueOf(min));
            strtotime = "" + gettimezonetime();
        } catch (Exception e) {

        }
    } else if (hourOfDay == zonehr && minute > zoneminute) {
        if (dialogmk != null) {
            if (dialogmk.isShowing()) {
                dialogmk.dismiss();
            }
        }
        calltimealert();
        try {
            StringTokenizer skrt = new StringTokenizer(gettimezonetime(), ":");
            String hr = skrt.nextToken();
            //Log.e("hrhr","@@"+hr);
            String min = "00";

            if (skrt.hasMoreTokens()) {
                min = skrt.nextToken();
                //Log.e("minv","@@"+min);

            } else {
                //Log.e("val","@@");
            }
            //Log.e("minmin","@@"+min);
            tp.setCurrentHour(Integer.valueOf(hr));
            tp.setCurrentMinute(Integer.valueOf(min));
            strtotime = "" + gettimezonetime();
        } catch (Exception e) {

        }
    } else {
        strtotime = "" + hourOfDay + ":" + minute;
    }
}else{
    strtotime = "" + hourOfDay + ":" + minute;
}


               // strtotime="" + hourOfDay + ":" + minute;







            }
        });
    }
    public static String printDifference(String startxDate, String endxDate) {
        try {
            //Log.e("startxDate", "&" + startxDate);
            //Log.e("endxDate", "&" + endxDate);
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date startDate = null;

            try {
                startDate = simpleDateFormat.parse(startxDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date endDate = null;

            try {
                endDate = simpleDateFormat.parse(endxDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long different = endDate.getTime() - startDate.getTime();
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;


            return pad(elapsedHours) + ":" + pad(elapsedMinutes);
        } catch (Exception e) {
            return 00 + ":" + 00;
        }
    }

    public static String pad(Long num) {
        String res = null;
        if (num < 10)
            res = "0" + num;
        else
            res = "" + num;

        return res;
    }


    public static void saveeditdata(String sdate,String stime,String etime,String tottime,String status,String remark) {

        progressdlog = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        if (OnlineCheck.isOnline(context)) {

            progressdlog = new ProgressDialog(context,
                    AlertDialog.THEME_HOLO_LIGHT);
            if (OnlineCheck.isOnline(context)) {
                progressdlog = new ProgressDialog(context,
                        AlertDialog.THEME_HOLO_LIGHT);
                progressdlog.setMessage("Please wait...");
                progressdlog.setCancelable(false);
                progressdlog.show();
                api = ApiServiceGenerator.createService(Eld_api.class);


                Call<JsonObject> call = null;
                //Log.e("ccode", "" + pref.getString(Constant.COMPANY_CODE));
                //Log.e("ccode", "" + pref.getString(Constant.DRIVER_ID));
                //Log.e("ccode", "" + pref.getString(Constant.DRIVER_ID));
                //Log.e("sdate", "" + sdate);
                //Log.e("stime", "" + stime);
                //Log.e("etime", "" + etime);
                // stime="2020-05-20 11:55";
                //etime="2020-05-20 22:30";
                // tottime="10:35";

                //Log.e("tottime", "" + tottime);
                //Log.e("status", "" + status);
                //Log.e("remark", "" + remark);

                if (stime.contains("am") || stime.contains("pm")) {
                    stime = yr(stime);
                    //Log.e("resultstime", ""+stime );
                }

                if (etime.contains("am") || etime.contains("pm")) {
                    etime=yr(etime);
                    //Log.e("resultetime", ""+etime );
                }

                //Log.e("stime1", "" + stime);
                //Log.e("etime1", "" + etime);
//                Log.e("cvurl", "logHandler_mobile.php?&ccode="+pref.getString(Constant.COMPANY_CODE)
//                        +"&userid="+pref.getString(Constant.DRIVER_ID)
//                        +"&driver="+pref.getString(Constant.DRIVER_ID)
//                        +"&date="+sdate
//                        +"&stime="+stime
//                        +"&etime="+etime
//                        +"&tottime="+tottime
//                        +"&status="+status
//                        +"&remark="+remark
//                );

                call = api.uploadeditlog(pref.getString(Constant.COMPANY_CODE),
                        pref.getString(Constant.DRIVER_ID), pref.getString(Constant.DRIVER_ID),
                        sdate, stime, etime, tottime, status, remark);


                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.e("Responsestring", response.body().toString());
                        //Toast.makeText()
                        if (response.isSuccessful()) {
                            cancelprogresssdialog();
                            if (response.body() != null) {
                                String jsonresponse = response.body().toString();

                                try {
                                    JSONObject resp = new JSONObject(jsonresponse);
                                    if (response != null) {

                                        String status = resp
                                                .getString("status");
                                        if (status != null && status.contentEquals("1")) {
                                            String message = resp
                                                    .getString("message");
                                            Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                                            refreshinter.refreshrep("", "refresh");
                                        } else {
                                            refreshinter.refreshrep("", "refresh");
                                        }
                                    }
                                } catch (Exception e) {

                                }


                            } else {
                                Log.e("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                            }
                        } else {
                            cancelprogresssdialog();
                            Log.e("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        cancelprogresssdialog();
                        Log.e("imageresponseerrr", "" + t.toString());
                        refreshinter.refreshrep("", "refresh");
                    }
                });


            }}
    }
    private static void cancelprogresssdialog()
    {
        try {
            if ((progressdlog != null) && progressdlog.isShowing()) {
                progressdlog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Log.e("err1.........",""+e.toString());
            // Handle or log or ignore
        } catch (final Exception e) {
            // Log.e("err2........",""+e.toString());
            // Handle or log or ignore
        } finally {
            progressdlog = null;
        }
    }

    public static interface refreshpage
    {
        void refreshrep(String id,String msg);

        void refreshrepload(String val);
      //  void refreshrvisible(String val);
    }



    private static String convert12time(String val)
    {
        String time="00:00";
        try {


            DateFormat outputformat = new SimpleDateFormat("HH:mm");
            Date date = null;
            DateFormat dfg = new SimpleDateFormat("hh:mm aa");

            try {

                date = outputformat.parse(val);

                time = dfg.format(date);
                //Log.e("time", "@@" + time);
            } catch (Exception e) {
                //Log.e("Exceptionzzz", "@" + e.toString());
            }
        }catch (Exception e)
        {

        }
        return time.toLowerCase();
    }








    private static String convert24time(String val)
    {
        String time="00:00";
        try {
            DateFormat df = new SimpleDateFormat("hh:mm aa");
            //Log.e("dfdfdfdfdf", "@@" + df);
            DateFormat outputformat = new SimpleDateFormat("HH:mm");
            Date date = null;


            try {

                date = df.parse(val);

                time = outputformat.format(date);
                //Log.e("time", "@@" + time);
            } catch (Exception e) {
                //Log.e("Exceptionzzz", "@" + e.toString());
            }
        }catch (Exception e)
        {

        }
        return time;
    }

    //yyyy-MM-dd

    private static String yr(String val)
    {
        String time="00:00";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
            //Log.e("dfdfdfdfdf", "@@" + df);
            DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = null;


            try {

                date = df.parse(val);

                time = outputformat.format(date);
                //Log.e("time", "@@" + time);
            } catch (Exception e) {
                //Log.e("Exceptionzzz", "@" + e.toString());
            }
        }catch (Exception e)
        {

        }
        return time;
    }
    private static String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

      //  return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
        return twoDigitString(hours) + " : " + twoDigitString(minutes);
    }

    private static String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }


    public static  void responsetrip(final String rowid, final String remarktxt, final TextView txtmark, String tripid, String tripval, String straction){
        if(dialogrk !=null) {
            if (dialogrk.isShowing()) {
                dialogrk.dismiss();
            }
        }

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // final View dialogView = inflater.inflate(R.layout.remark_popup_new, null);
        final View dialogView = inflater.inflate(R.layout.edt_trip, null);

        name = dialogView.findViewById(R.id.rmark);
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);

        final LinearLayout linbrk = dialogView.findViewById(R.id.linbreak);

        final Button btncancel = dialogView.findViewById(R.id.btn_cancel);
        final ImageView imgstatus=dialogView.findViewById(R.id.txt_img);
       // final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialogrk = new Dialog(context, R.style.DialogTheme);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);

        if(remarktxt !=null && (remarktxt.contentEquals("Add Ship. Doc. #") || remarktxt.contentEquals("Add")
        || remarktxt.contentEquals("N/A")))
        {
            name.setText("");
        }

        else{



            name.setText("" + remarktxt);

        }

        dialogrk.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogrk.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogrk.getWindow().getAttributes());
        // lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        // lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.70);
        int height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.90);
        lp.width = width;
        lp.height = height;
        dialogrk.getWindow().setAttributes(lp);
        dialogrk.show();


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogz = new ProgressDialog(context,
                        AlertDialog.THEME_HOLO_LIGHT);
                dialogz.setMessage("Please wait...");
                dialogz.setCancelable(false);
                dialogz.show();

                String rmark=""+name.getText().toString().trim();
                if(rmark !=null &&rmark.length()>0)
                {
                    txtmark.setText(""+rmark);
                    txtmark.setBackgroundResource(android.R.color.white);

                    if (Build.VERSION.SDK_INT >=  23) {
                        txtmark.setTextColor(ContextCompat.getColor(context, R.color.companycolor));
                    }else {
                        txtmark.setTextColor(context.getResources().getColor(R.color.companycolor));
                    }
                }else {

                    txtmark.setText(""+rmark);
                }











                String did=pref.getString(Constant.DRIVER_ID);
                String vinnumber=""+pref.getString(Constant.VIN_NUMBER);


                String msg=name.getText().toString().trim();
                api = ApiServiceGenerator.createService(Eld_api.class);
                Call<JsonObject> call;
                if(straction.contentEquals("save"))
                {
                  //  Log.e("url","saveTripNo_new.php?vin="+vinnumber+"&lid="+rowid+"&did="+did+"&num="+msg+"&trip="+msg+"&action="+straction+"&date="+myDatez1);
                    //call = api.updatetripno(vinnumber,"0",did,""+msg,""+msg,straction,""+myDatez1);
                    call = api.updatetripno(vinnumber,""+rowid,did,""+msg,""+msg,straction,""+myDatez1);
                }else{
                  //  Log.e("urlzzz","saveTripNo_new.php?vin="+vinnumber+"&lid="+rowid+"&did="+did+"&num="+msg+"&trip="+msg+"&action="+straction+"&date="+myDatez1);
                   call = api.updatetripno(vinnumber,""+rowid,did,""+msg,""+msg,straction,""+myDatez1);

                }

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        cancelprogresssdialogz();
                        //Log.e("Responsestring", response.body().toString());
                        //Toast.makeText()
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                String jsonresponse = response.body().toString();
                                //Log.e("jsonresponse", jsonresponse.toString());
                                try {
                                    JSONObject resp = new JSONObject(jsonresponse);
                                    if (response != null)
                                    {
                                        dialogrk.dismiss();
                                        if(resp.has("status"))
                                        {
                                            String stat=resp.getString("status");

                                            if(stat !=null && stat.contentEquals("1")) {
                                                String msg = resp.getString("message");
                                                refreshinter.refreshrepload("refresh");

                                                Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();



                                            }
                                        }

                                    }
                                } catch (Exception e) {
                                    //Log.e("Exceptionwwwwwwww", e.toString());
                                }


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        //Log.e("Exceptionwttttttt", t.toString());
                        cancelprogresssdialogz();
                    }
                });
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogrk.dismiss();
               // refreshinter.refreshrepload("refresh");
                // setdistancealert();
                //setondutyalert();
            }
        });

    }


    private static void cancelprogresssdialogz() {

        try {
            if ((dialogz != null) && dialogz.isShowing()) {
                dialogz.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Log.e("err1.........",""+e.toString());
            // Handle or log or ignore
        } catch (final Exception e) {
            // Log.e("err2........",""+e.toString());
            // Handle or log or ignore
        } finally {
            dialogz = null;
        }
    }
    private static void commoditypopup(String lid, String msg, TextView txt_cmdty)
    {
        if(dialogrk !=null) {
            if (dialogrk.isShowing()) {
                dialogrk.dismiss();
            }
        }

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // final View dialogView = inflater.inflate(R.layout.remark_popup_new, null);
        final View dialogView = inflater.inflate(R.layout.edt_cmdty, null);

        name = dialogView.findViewById(R.id.rmark);
        final Button btnsubmit = dialogView.findViewById(R.id.btn_submit);

        final LinearLayout linbrk = dialogView.findViewById(R.id.linbreak);

        final Button btncancel = dialogView.findViewById(R.id.btn_cancel);
        final ImageView imgstatus=dialogView.findViewById(R.id.txt_img);
        // final TextView txtstatus = dialogView.findViewById(R.id.txt_status);
        // final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialogrk = new Dialog(context, R.style.DialogTheme);
        //dialog = new Dialog(this, R.style.DialogTheme);
//        Log.e("inttt",""+intschedule);

        if(msg ==null ||  msg.contentEquals("Property")
                || msg.contentEquals("N/A") || msg.contentEquals("null"))
        {
            name.setText("");
        }

        else{



            name.setText("" + msg);

        }

        dialogrk.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogrk.setContentView(dialogView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogrk.getWindow().getAttributes());
        // lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        // lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.70);
        int height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.90);
        lp.width = width;
        lp.height = height;
        dialogrk.getWindow().setAttributes(lp);
        dialogrk.show();


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogz = new ProgressDialog(context,
                        AlertDialog.THEME_HOLO_LIGHT);
                dialogz.setMessage("Please wait...");
                dialogz.setCancelable(false);
                dialogz.show();

                String rmark=""+name.getText().toString().trim();
                if(rmark !=null &&rmark.length()>0)
                {
                    txt_cmdty.setText(""+rmark);
                    txt_cmdty.setBackgroundResource(android.R.color.white);

                    if (Build.VERSION.SDK_INT >=  23) {
                        txt_cmdty.setTextColor(ContextCompat.getColor(context, R.color.companycolor));
                    }else {
                        txt_cmdty.setTextColor(context.getResources().getColor(R.color.companycolor));
                    }
                }else {

                    txt_cmdty.setText(""+rmark);
                }











                String did=pref.getString(Constant.DRIVER_ID);
                String vinnumber=""+pref.getString(Constant.VIN_NUMBER);


                String msg=name.getText().toString().trim();
                api = ApiServiceGenerator.createService(Eld_api.class);
                //Log.e("url","saveTripNo.php?vin="+vinnumber+"&lid="+rowid+"&did="+did+"&num="+msg+"&trip="+""+tripid);
                Call<JsonObject> call = api.savecommodity(vinnumber,""+lid,did,""+msg);

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        cancelprogresssdialogz();
                        //Log.e("Responsestring", response.body().toString());
                        //Toast.makeText()
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                String jsonresponse = response.body().toString();
                                //Log.e("jsonresponse", jsonresponse.toString());
                                try {
                                    JSONObject resp = new JSONObject(jsonresponse);
                                    if (response != null)
                                    {
                                        dialogrk.dismiss();
                                        if(resp.has("status"))
                                        {
                                            String stat=resp.getString("status");

                                            if(stat !=null && stat.contentEquals("1")) {
                                                String msg = resp.getString("message");
                                                refreshinter.refreshrepload("refresh");

                                                Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();



                                            }
                                        }

                                    }
                                } catch (Exception e) {
                                    //Log.e("Exceptionwwwwwwww", e.toString());
                                }


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        //Log.e("Exceptionwttttttt", t.toString());
                        cancelprogresssdialogz();
                    }
                });
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogrk.dismiss();
                // refreshinter.refreshrepload("refresh");
                // setdistancealert();
                //setondutyalert();
            }
        });

    }

    private static String gettimeonedate()
    {
        String tdate="";
        String timezone=pref.getString(Constant.DRIVER_HOME_TIMEZONE);
        Log.e("timezone","&"+timezone);
        try {
//        TimeZone tz = TimeZone.getTimeZone(""+timezone);
//        Calendar c = Calendar.getInstance(tz);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        tdate=dateFormat.format(c.getTime());

            Calendar c = Calendar.getInstance();
            Date date = c.getTime(); //current date and time in UTC
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setTimeZone(TimeZone.getTimeZone(timezone)); //format in given timezone
            tdate = df.format(date);

            Log.e("tdate","&"+tdate);
        }catch (Exception e)
        {

        }
        return tdate;
    }


    private static String gettimezonetime()
    {
        String tzonetime="";
        String timezone=pref.getString(Constant.DRIVER_HOME_TIMEZONE);
        try{
            TimeZone tz = TimeZone.getTimeZone(""+timezone);
            Calendar c = Calendar.getInstance(tz);
            tzonetime=String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+
                    String.format("%02d" , c.get(Calendar.MINUTE));
        }catch (Exception e)
        {

        }
        Log.e("tzonetime","@"+tzonetime);
        return  tzonetime;
    }

    private static String gettimezonetimehour()
    {
        String tzonetime="";
        String timezone=pref.getString(Constant.DRIVER_HOME_TIMEZONE);
        try{
            TimeZone tz = TimeZone.getTimeZone(""+timezone);
            Calendar c = Calendar.getInstance(tz);
            tzonetime=String.format("%02d" , c.get(Calendar.HOUR_OF_DAY));
        }catch (Exception e)
        {

        }
        Log.e("tzonetime","@"+tzonetime);
        return  tzonetime;
    }
    private static String gettimezonetimeminute()
    {
        String tzonetime="";
        String timezone=pref.getString(Constant.DRIVER_HOME_TIMEZONE);
        try{
            TimeZone tz = TimeZone.getTimeZone(""+timezone);
            Calendar c = Calendar.getInstance(tz);
            tzonetime=
                    String.format("%02d" , c.get(Calendar.MINUTE));
        }catch (Exception e)
        {

        }
        Log.e("tzonetime","@"+tzonetime);
        return  tzonetime;
    }


    private static String gettimezonetimeampm()
    {
        String tzonetimec="";
        String tzonetime="";
        String timezone=pref.getString(Constant.DRIVER_HOME_TIMEZONE);
        try{
            TimeZone tz = TimeZone.getTimeZone(""+timezone);
            Calendar c = Calendar.getInstance(tz);
            tzonetime=String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+
                    String.format("%02d" , c.get(Calendar.MINUTE));
        }catch (Exception e)
        {

        }
        Log.e("tzonetime0","@"+tzonetime);
        tzonetimec=convert12time(tzonetime);
        Log.e("tzonetime1","@ "+timezone);
        return  tzonetimec;
    }




    private static void calltimealert() {

        View view = View.inflate(context, R.layout.time_alert, null);
        Button txt_ok = view.findViewById(R.id.btn_yes);
TextView ttmsg=view.findViewById(R.id.txt_msg);
        final TextView txtalert = view.findViewById(R.id.txtalert);
        txtalert.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));

        ttmsg.setText("Home State Time now : \n"+gettimezonetimeampm());
          dialogmk = new Dialog(context, R.style.DialogTheme);
        //dialog = new Dialog(this, R.style.DialogTheme);
        dialogmk.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogmk.setContentView(view);
        dialogmk.setCancelable(false);
        dialogmk.show();

        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogmk.dismiss();

            }
        });

    }
}
