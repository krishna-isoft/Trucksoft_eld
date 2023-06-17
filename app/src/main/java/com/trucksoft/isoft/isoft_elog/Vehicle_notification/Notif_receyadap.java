package com.trucksoft.isoft.isoft_elog.Vehicle_notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isoft.trucksoft_elog.Multiused.Preference;
import com.trucksoft.isoft.isoft_elog.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class Notif_receyadap extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    static Preference pref;
    static Context context;
    List<Notiflist_model> movies;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    public Notif_receyadap(Context context, List<Notiflist_model> moviesz) {
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
            return new MovieHolder(inflater.inflate(R.layout.notification_homelist,parent,false));
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
        if(movies.get(position).note_type.equals("notification")){
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
        TextView txtmessage;
        TextView txtvehicle;
        RelativeLayout mLayout;
        LinearLayout lin_top;
        TextView txttype;

        public MovieHolder(View itemView) {
            super(itemView);
            date_text =  itemView
                    .findViewById(R.id.date_text);
            txtmessage =  itemView.findViewById(R.id.txt_message);

           txtvehicle =  itemView.findViewById(R.id.txtvehicle);
            txttype =  itemView.findViewById(R.id.txttype);

        }

        void bindData(final Notiflist_model movieModel){

            if(movieModel.created_on !=null && movieModel.created_on.length()>0)
            {
                StringTokenizer str=new StringTokenizer(movieModel.created_on," ");
                String dt=str.nextToken();
                String timr=str.nextToken();
                String ctime="";
                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                    final Date dateObj = sdf.parse(timr);
                    ctime="  "+new SimpleDateFormat("hh:mm aa").format(dateObj);

                } catch (final ParseException e) {
                    e.printStackTrace();
                }





                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(dt);
                    sdf = new SimpleDateFormat("MMM dd, yyyy");
                    String yourFormatedDateString = sdf.format(date);
                    date_text.setText(""+yourFormatedDateString+ctime);
                }catch (Exception e)
                {

                }


            }else{
                date_text.setText("");
            }
            if(movieModel.message !=null && movieModel.message.length()>0)
            {
                txtmessage.setText(""+movieModel.message);
            }else{
                txtmessage.setText("");
            }



            if(movieModel.truck !=null && movieModel.truck.length()>0)
            {
                txtvehicle.setText(""+movieModel.truck);
            }else{
                txtvehicle.setText("");
            }
            if(movieModel.title !=null && movieModel.title.length()>0)
            {
                txttype.setText(""+movieModel.title);
            }else{
                txttype.setText("");
            }

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
