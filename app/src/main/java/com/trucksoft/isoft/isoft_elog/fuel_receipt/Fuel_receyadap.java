package com.trucksoft.isoft.isoft_elog.fuel_receipt;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.isoft.trucksoft_elog.Isoft_activity.Image_view_ticket;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.trucksoft.isoft.isoft_elog.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Fuel_receyadap extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    static Preference pref;
    static Context context;
    static Dialog dialogrk;
    List<Receipt_model> movies;
    OnLoadMoreListener loadMoreListener;
    static Eld_api api;
    static ProgressDialog progressdlog;
    boolean isLoading = false, isMoreDataAvailable = true;
    public Fuel_receyadap(Context context, List<Receipt_model> moviesz) {
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
        //    return new MovieHolder(inflater.inflate(R.layout.fuelreceipt_list,parent,false));
            return new MovieHolder(inflater.inflate(R.layout.tkkk,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
                isLoading = true;
                loadMoreListener.onLoadMore();
            }


        if(getItemViewType(position)==TYPE_MOVIE){
            //Log.e("stat","success");
            ((MovieHolder)holder).bindData(movies.get(position));
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(movies.get(position).type.equals("fuelreceipt")){
            return TYPE_MOVIE;
        }else{
            return TYPE_LOAD;
        }
    }
    @Override
    public int getItemCount() {
//Log.e("countt","@"+movies.size());
        return movies.size();
    }
    /* VIEW HOLDERS */

    static class MovieHolder extends RecyclerView.ViewHolder{

       ImageView imgbill;
       TextView date_text,tvin,txturl;
       CardView cardView;

        public MovieHolder(View itemView) {
            super(itemView);
            date_text =  itemView
                    .findViewById(R.id.pub_date);
            imgbill =  itemView.findViewById(R.id.thumb_img);
            tvin =  itemView.findViewById(R.id.tvin);
            cardView=itemView.findViewById(R.id.cardview);
            txturl=itemView.findViewById(R.id.txturl);


        }

        void bindData(final Receipt_model movieModel){
            //Log.e("idvalz","@"+movieModel.id);
            if(movieModel.vin !=null && movieModel.vin.length()>0)
            {
                tvin.setText("Vin : "+movieModel.vin);
            }else{
                tvin.setText("Vin : ");
            }
            if(movieModel.bill_date !=null && movieModel.bill_date.length()>0)
            {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(movieModel.bill_date);
                    sdf = new SimpleDateFormat("MMM dd, yyyy");
                    String yourFormatedDateString = sdf.format(date);
                    date_text.setText("Bill Date : "+yourFormatedDateString);
                }catch (Exception e)
                {

                }


            }else{
                date_text.setText("Bill Date : ");
            }
            if(movieModel.image !=null && movieModel.image.length()>0)
            {
                txturl.setText(""+movieModel.image);
                Picasso.with(context)
                        .load(movieModel.image + "?.time();")
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .error(R.drawable.prog_animationz)
                        .into(imgbill);
            }else{

            }
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intenttt = new Intent(context, Image_view_ticket.class);
                    intenttt.putExtra("img_url", "" + txturl.getText().toString());
                    context.startActivity(intenttt);
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
