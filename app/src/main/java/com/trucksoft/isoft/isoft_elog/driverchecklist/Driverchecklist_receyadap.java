package com.trucksoft.isoft.isoft_elog.driverchecklist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.trucksoft.isoft.isoft_elog.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class Driverchecklist_receyadap extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    static Preference pref;
    static Context context;
    static int ncount=0;
    static Dialog dialogrk;
    List<Checklist_model> movies;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    public Driverchecklist_receyadap(Context context, List<Checklist_model> moviesz) {
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
            return new MovieHolder(inflater.inflate(R.layout.checklist_homelist,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Log.e("sizeeeeee",""+getItemCount());
        if(getItemCount()<8)
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
            //Log.e("stat","success");
            ((MovieHolder)holder).bindData(movies.get(position));
        }
    }
    @Override
    public int getItemViewType(int position) {
        //Log.e("movlist","q"+movies.toString());
        //Log.e("movlissdt","q"+movies.get(position).type);
        if(movies.get(position).type.equals("checklist")){
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

        TextView txt_id;
        TextView txt_carrier;
        TextView txt_truck,txtvtype;
        RelativeLayout mLayout;
        LinearLayout lin_top;
        TextView txtdate,type;
        TextView txtaddress;

        public MovieHolder(View itemView) {
            super(itemView);
            txt_id =  itemView
                    .findViewById(R.id.listview_layout_tv1_value);
            txt_carrier =  itemView.findViewById(R.id.listview_layout_tv2_value);
            txtvtype=  itemView.findViewById(R.id.txtvtype);
            txt_truck =  itemView.findViewById(R.id.listview_layout_tv3_value);
            type=itemView.findViewById(R.id.txttypeff);
            mLayout = itemView
                    .findViewById(R.id.freight_ticket_rl_ship_con);
            lin_top=itemView.findViewById(R.id.lintop);
            txtdate=itemView.findViewById(R.id.txtdate);
            txtaddress=itemView.findViewById(R.id.txtaddress);





        }

        void bindData(final Checklist_model movieModel){

            ncount=movieModel.tcount;
            pref.putString(Constant.LAST_INDEX,""+movieModel.lastindex);

txt_id.setText(""+movieModel.id);
            //Log.e("idid",""+movieModel.id);
txt_carrier.setText(""+movieModel.carrier);
//Log.e("truckno",""+movieModel.truck_no);
            if(movieModel.tractor !=null && movieModel.tractor.length()>0)
            {
                txt_truck.setText(""+movieModel.tractor);
            }else{
                txt_truck.setText("");
            }
            if(movieModel.address !=null && movieModel.address.length()>0)
            {
                txtaddress.setText(""+movieModel.address);
            }else{
                txtaddress.setText("");
            }

if(movieModel.editmode.contentEquals("1"))
{
   try
   {
       String stdate=movieModel.datedd;
       StringTokenizer sk=new StringTokenizer(stdate," ");
       String dt=sk.nextToken();
       String timed=sk.nextToken();
       final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
       final Date dateObj = sdf.parse(timed);
       //String fg=""+new SimpleDateFormat("h:mm").format(dateObj);

       String starttime=""+new SimpleDateFormat("h:mma").format(dateObj);
       txtdate.setText(""+dt+" "+starttime);
   }catch (Exception e)
   {

   }
}else{

    try
    {
        String stdate=movieModel.savedate;
        StringTokenizer sk=new StringTokenizer(stdate," ");
        String dt=sk.nextToken();
        String timed=sk.nextToken();
        final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        final Date dateObj = sdf.parse(timed);
        //String fg=""+new SimpleDateFormat("h:mm").format(dateObj);

        String starttime=""+new SimpleDateFormat("h:mma").format(dateObj);
        txtdate.setText(""+dt+" "+starttime);
    }catch (Exception e)
    {
        //Log.e("er",""+e.toString());

    }
  //  txtdate.setText(""+movieModel.savedate);
}
            type.setText(""+movieModel.tripopt);
            if (movieModel.trashmode.contentEquals("1")) {
               mLayout.setBackgroundColor(context.getResources().getColor(
                        R.color.ldgray));
                txt_id.setBackgroundColor(context.getResources()
                        .getColor(R.color.ldgray));
                txtvtype.setVisibility(View.VISIBLE);
                txtvtype.setText("void");
                txtvtype.setBackgroundColor(context.getResources()
                        .getColor(R.color.ldgray));

            } else if (movieModel.trashmode.contentEquals("0")) {
                mLayout.setBackgroundColor(context.getResources().getColor(
                        R.color.white));
                txt_id.setBackgroundColor(context.getResources()
                        .getColor(R.color.white));
                txtvtype.setBackgroundColor(context.getResources()
                        .getColor(R.color.white));
                txtvtype.setText("");
                txtvtype.setVisibility(View.VISIBLE);
            }
            if(movieModel.trashmode.contentEquals("0") && movieModel.editmode.contentEquals("1"))
            {
                mLayout.setBackgroundColor(context.getResources().getColor(
                        R.color.yellow));
                txt_id.setBackgroundColor(context.getResources()
                        .getColor(R.color.yellow));
                txtvtype.setText("");
                txtvtype.setVisibility(View.VISIBLE);
                txtvtype.setBackgroundColor(context.getResources()
                        .getColor(R.color.yellow));
            }

lin_top.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

//        String ab=movieModel.tractor;
//        if(ab==null || ab.length()==0 || ab.contentEquals("null"))
//        {
//            ab="";
//        }
//        pref.putString(Constant.DLIST_STATUS,"edit");
//        Intent mintent1 = new Intent(context,
//                DriverCheckInsideActivity.class);
//        mintent1.putExtra("id", ""+movieModel.id);
//        mintent1.putExtra("carrier", movieModel.carrier);
//        mintent1.putExtra("tractor", ab);
//        mintent1.putExtra("truck_no",movieModel.truck_no);
//        mintent1.putExtra("odometer_reading",movieModel.odometer_reading);
//        mintent1.putExtra("date", movieModel.savedate);
//        mintent1.putExtra("savedate", movieModel.date);
//        mintent1.putExtra("trashmode",movieModel.trashmode);
//        mintent1.putExtra("editmode", movieModel.editmode);
//        mintent1.putExtra(Constant.TRIP_OPTION, movieModel.tripopt);
//
//            String wwemsg =movieModel.msg;
//            if(wwemsg !=null && wwemsg.length()>0) {
//                pref.putString(Constant.WWE_MSG,
//                        ""+wwemsg);
//                //Log.e("msg","kk"+ Html.fromHtml(wwemsg));
//            }else
//            {
//                pref.putString(Constant.WWE_MSG,
//                        "");
//            }
//
//        ((Activity) context).startActivityForResult(mintent1, 1);


        if(OnlineCheck.isOnline(context))
        {
            deleteCache(context);
            String wwemsg =movieModel.msg;
            if(wwemsg !=null && wwemsg.length()>0) {
                pref.putString(Constant.WWE_MSG,
                        ""+wwemsg);
                //Log.e("msg","kk"+ Html.fromHtml(wwemsg));
            }else
            {
                pref.putString(Constant.WWE_MSG,
                        "");
            }
            Intent mIntent = new Intent(context,
                    DriverChecklistView.class);
            mIntent.putExtra("ID", ""+movieModel.id);
            mIntent.putExtra(Constant.TRIP_OPTION, ""+movieModel.tripopt);
            ((Activity) context).startActivityForResult(mIntent, 1);
            //context.startActivity(mIntent);

        }
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


}
