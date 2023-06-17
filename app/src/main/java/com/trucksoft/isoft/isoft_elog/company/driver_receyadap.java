package com.trucksoft.isoft.isoft_elog.company;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isoft.trucksoft_elog.Multiused.OnlineCheck;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.trucksoft.isoft.isoft_elog.R;

import java.io.File;
import java.util.List;

public class driver_receyadap extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    static Preference pref;
    static Context context;
    static int ncount=0;
    static Dialog dialogrk;
    List<driver_model> movies;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    public driver_receyadap(Context context, List<driver_model> moviesz) {
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
            return new MovieHolder(inflater.inflate(R.layout.driver_homelist,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.e("sizeeeeee",""+getItemCount());
//        if(getItemCount()<8)
//        {
//
//        }else if(getItemCount()==ncount) {
//        }
      //  else {
            if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
                isLoading = true;
                loadMoreListener.onLoadMore();
            }
       // }

        if(getItemViewType(position)==TYPE_MOVIE){
            //Log.e("stat","success");
            ((MovieHolder)holder).bindData(movies.get(position));
        }
    }
    @Override
    public int getItemViewType(int position) {
//        Log.e("movlist","q"+movies.toString());
        Log.e("movlissdt","q"+movies.get(position).type);
        if(movies.get(position).type !=null) {
            if (movies.get(position).type.equals("driver_home")) {
                return TYPE_MOVIE;
            } else {
                return TYPE_LOAD;
            }
        }else{
            return TYPE_MOVIE;
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
        TextView txt_drivername,txtvtype;
        RelativeLayout mLayout;
        LinearLayout lin_top;
        TextView txtlicense,type;
        TextView txtemail;

        public MovieHolder(View itemView) {
            super(itemView);
            txt_id =  itemView
                    .findViewById(R.id.listview_layout_tv1_value);
            txt_carrier =  itemView.findViewById(R.id.listview_layout_tv2_value);
            txtvtype=  itemView.findViewById(R.id.txtvtype);
            txt_drivername =  itemView.findViewById(R.id.listview_layout_tv3_value);
            type=itemView.findViewById(R.id.txttypeff);
            mLayout = itemView
                    .findViewById(R.id.freight_ticket_rl_ship_con);
            lin_top=itemView.findViewById(R.id.lintop);
            txtlicense=itemView.findViewById(R.id.txt_license);
            txtemail=itemView.findViewById(R.id.txt_email);





        }

        void bindData(final driver_model movieModel){
Log.e("stid","@"+movieModel.staff_id);

txt_id.setText(""+movieModel.staff_id);
            if(movieModel.staff_name !=null && movieModel.staff_name.length()>0)
            {
                txt_drivername.setText(""+movieModel.staff_name);
            }else{
                txt_drivername.setText("");
            }
            if(movieModel.license !=null && movieModel.license.length()>0)
            {
                txtlicense.setText(""+movieModel.license);
            }else{
                txtlicense.setText("");
            }
            if(movieModel.e_mail !=null && movieModel.e_mail.length()>0)
            {
                txtemail.setText(""+movieModel.e_mail);
            }else{
                txtemail.setText("");
            }



lin_top.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        if(OnlineCheck.isOnline(context))
        {
            deleteCache(context);

            Intent mIntent = new Intent(context,
                    viewdriver.class);
            mIntent.putExtra("drivervalue", movieModel);

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
