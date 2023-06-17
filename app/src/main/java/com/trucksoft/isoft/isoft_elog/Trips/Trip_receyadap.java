package com.trucksoft.isoft.isoft_elog.Trips;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isoft.trucksoft_elog.Model_class.ApiServiceGenerator;
import com.isoft.trucksoft_elog.Model_class.Res_model;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.trucksoft.isoft.isoft_elog.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Trip_receyadap extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    static Preference pref;
    static Context context;
    static Dialog dialogrk;
    List<Triplist_model> movies;
    OnLoadMoreListener loadMoreListener;
    static Eld_api api;
    static ProgressDialog progressdlog;
    boolean isLoading = false, isMoreDataAvailable = true;
    public Trip_receyadap(Context context, List<Triplist_model> moviesz) {
        this.context = context;
        pref=Preference.getInstance(context);
        this.movies = moviesz;


        //Log.e("movies","#"+movies);
        // Log.e("calling","adapter");
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new MovieHolder(inflater.inflate(R.layout.triplist_homelist,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Log.e("sizeeeeee",""+getItemCount());
//        if(getItemCount()<8)
//        {
//
//        }else if(getItemCount()==ncount) {
//        }
//        else {
            if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
                isLoading = true;
                loadMoreListener.onLoadMore();
            }
        //}

        if(getItemViewType(position)==TYPE_MOVIE){
            //Log.e("stat","success");
            ((MovieHolder)holder).bindData(movies.get(position));
        }
    }
    @Override
    public int getItemViewType(int position) {
        //Log.e("movlist","q"+movies.toString());
        //Log.e("movlissdt","q"+movies.get(position).type);
        if(movies.get(position).triptype.equals("triplist")){
            return TYPE_MOVIE;
        }else{
            return TYPE_LOAD;
        }
    }
    @Override
    public int getItemCount() {

        return movies.size();
    }
    /* VIEW HOLDERS */

    static class MovieHolder extends RecyclerView.ViewHolder{

        TextView date_text;
        TextView startfrom;
        TextView endto;
        TextView txtvehicle,txttripid;
        RelativeLayout mLayout;
        LinearLayout lin_top;
        TextView txtnickname,txtmileage;
        TextView txtstarttime,txtendtime;
        TextView txtbusiness;
        TextView txtpersonal;
        TextView txtempty;

        public MovieHolder(View itemView) {
            super(itemView);
            date_text =  itemView
                    .findViewById(R.id.date_text);
            startfrom =  itemView.findViewById(R.id.startfrom);
            endto=  itemView.findViewById(R.id.endto);
           txtvehicle =  itemView.findViewById(R.id.txtvehicle);
            txttripid =  itemView.findViewById(R.id.txttripid);
            txtnickname =  itemView.findViewById(R.id.txtnickname);
            txtmileage =  itemView.findViewById(R.id.txtmileage);
            txtstarttime =  itemView.findViewById(R.id.starttime);
            txtendtime =  itemView.findViewById(R.id.endtime);
            txtbusiness=  itemView.findViewById(R.id.txtbusiness);
            txtpersonal=  itemView.findViewById(R.id.txtpersonal);
            txtempty=  itemView.findViewById(R.id.txtempty);



//


        }

        void bindData(final Triplist_model movieModel){


//          //  pref.putString(Constant.LAST_INDEX,""+movieModel.lastindex);
//
//txt_id.setText(""+movieModel.id);
//            //Log.e("idid",""+movieModel.id);
//txt_carrier.setText(""+movieModel.carrier);
////Log.e("truckno",""+movieModel.truck_no);
            if(movieModel.date !=null && movieModel.date.length()>0)
            {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(movieModel.date);
                    sdf = new SimpleDateFormat("MMM dd, yyyy");
                    String yourFormatedDateString = sdf.format(date);
                    date_text.setText(""+yourFormatedDateString);
                }catch (Exception e)
                {

                }


            }else{
                date_text.setText("");
            }
            if(movieModel.start_from !=null && movieModel.start_from.length()>0)
            {
                startfrom.setText(""+movieModel.start_from);
            }else{
                startfrom.setText("");
            }
            if(movieModel.end_to !=null && movieModel.end_to.length()>0)
            {
                endto.setText(""+movieModel.end_to);
            }else{
                endto.setText("");
            }

            if(movieModel.trip_id !=null && movieModel.trip_id.length()>0)
            {
                txttripid.setText(" Trip ID : "+movieModel.trip_id);
            }else{
                txttripid.setText(" Trip ID : ");
            }

            if(movieModel.vehicle !=null && movieModel.vehicle.length()>0)
            {
                txtvehicle.setText("Vehicle : "+movieModel.vehicle);
            }else{
                txtvehicle.setText("Vehicle : ");
            }
            if(movieModel.nickname !=null && movieModel.nickname.length()>0)
            {
                txtnickname.setText(""+movieModel.nickname);
            }else{
                txtnickname.setText("");
            }
            if(movieModel.miles !=null && movieModel.miles.length()>0)
            {
                txtmileage.setText("Miles : "+movieModel.miles);
            }else{
                txtmileage.setText("Miles : ");
            }
            if(movieModel.timefrom !=null && movieModel.timefrom.length()>0)
            {

                String timd=movieModel.timefrom;
                StringTokenizer str=new StringTokenizer(timd," ");
                String dt=str.nextToken();
                String timr=str.nextToken();
                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                    final Date dateObj = sdf.parse(timr);
                    txtstarttime.setText(dt+"  "+new SimpleDateFormat("hh:mm aa").format(dateObj));
                } catch (final ParseException e) {
                    e.printStackTrace();
                }

            }else{
                txtstarttime.setText(": ");
            }
            if(movieModel.timeto !=null && movieModel.timeto.length()>0)
            {
String timd=movieModel.timeto;
StringTokenizer str=new StringTokenizer(timd," ");
String dt=str.nextToken();
String timr=str.nextToken();
                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                    final Date dateObj = sdf.parse(timr);
                    txtendtime.setText(dt+"  "+new SimpleDateFormat("hh:mm aa").format(dateObj));
                } catch (final ParseException e) {
                    e.printStackTrace();
                }
               // txtendtime.setText(""+movieModel.timeto);
            }else{
                txtendtime.setText("");
            }
//

            if(movieModel.type !=null && movieModel.type.length()>0)
            {
                if(movieModel.type.contentEquals("0"))
                {
                    txtbusiness.setBackgroundResource(R.drawable.button_activate);
                    txtpersonal.setBackgroundResource(R.drawable.button_deactivate);
                    txtempty.setBackgroundResource(R.drawable.button_deactivate);
                }else if(movieModel.type.contentEquals("1"))
                {
                    txtpersonal.setBackgroundResource(R.drawable.button_activate);
                    txtbusiness.setBackgroundResource(R.drawable.button_deactivate);
                    txtempty.setBackgroundResource(R.drawable.button_deactivate);
                }else {
                    txtempty.setBackgroundResource(R.drawable.button_activate);
                    txtpersonal.setBackgroundResource(R.drawable.button_deactivate);
                    txtbusiness.setBackgroundResource(R.drawable.button_deactivate);
                }
            }else{
                txtempty.setBackgroundResource(R.drawable.button_deactivate);
                txtpersonal.setBackgroundResource(R.drawable.button_deactivate);
                txtbusiness.setBackgroundResource(R.drawable.button_deactivate);
            }


            txtempty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savetripval("2",""+movieModel.trip_id,""+movieModel.d_type);
                    txtempty.setBackgroundResource(R.drawable.button_activate);
                    txtpersonal.setBackgroundResource(R.drawable.button_deactivate);
                    txtbusiness.setBackgroundResource(R.drawable.button_deactivate);
                }
            });

            txtpersonal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savetripval("1",""+movieModel.trip_id,""+movieModel.d_type);
                    txtpersonal.setBackgroundResource(R.drawable.button_activate);
                    txtbusiness.setBackgroundResource(R.drawable.button_deactivate);
                    txtempty.setBackgroundResource(R.drawable.button_deactivate);
                }
            });
            txtbusiness.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savetripval("0",""+movieModel.trip_id,""+movieModel.d_type);
                    txtbusiness.setBackgroundResource(R.drawable.button_activate);
                    txtpersonal.setBackgroundResource(R.drawable.button_deactivate);
                    txtempty.setBackgroundResource(R.drawable.button_deactivate);
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
    private static void savetripval(String type,String tripid,String val) {
        progressdlog = new ProgressDialog(context,
                AlertDialog.THEME_HOLO_LIGHT);
        progressdlog.setMessage("Please wait...");
        progressdlog.setCancelable(false);
        progressdlog.show();

        String did = pref.getString(Constant.DRIVER_ID);
        //String dmak=pref.getString(Constant.ELD_MAKE);
     // Log.e("vl","@"+val);


     //   Log.e("valll","change_trip_type.php?type="+val+"&val="+type+"&trip_id="+tripid+"did="+did);
        api = ApiServiceGenerator.createService(Eld_api.class);
        Call<Res_model> call = api.savetripstatus(""+val,type, ""+tripid,did );
        call.enqueue(new Callback<Res_model>() {

            public void onResponse(Call<Res_model> call, Response<Res_model> response) {
                if (response.isSuccessful()) {
                   // Log.e("result","success");
cancelprogresssdialog();
                } else {
                    cancelprogresssdialog();
                   // Log.e("result","fail");
                }
            }

            @Override
            public void onFailure(Call<Res_model> call, Throwable t) {
                Log.e("dd"," Response Error "+t.getMessage());
cancelprogresssdialog();
            }
        });
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

}
