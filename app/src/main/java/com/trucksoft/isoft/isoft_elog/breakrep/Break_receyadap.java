package com.trucksoft.isoft.isoft_elog.breakrep;

import android.app.Dialog;
import android.app.ProgressDialog;
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

public class Break_receyadap extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    static Preference pref;
    static Context context;
    static Dialog dialogrk;
    List<Breakrep_model> movies;
    OnLoadMoreListener loadMoreListener;
    static ProgressDialog progressdlog;
    boolean isLoading = false, isMoreDataAvailable = true;
    public Break_receyadap(Context context, List<Breakrep_model> moviesz) {
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
        if(movies.get(position).loadtype.equals("elogrep")){
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
        TextView txtreason;

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

        void bindData(final Breakrep_model movieModel){



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
            if(movieModel.time !=null && movieModel.time.length()>0)
            {
                startfrom.setText(""+movieModel.time);
            }else{
                startfrom.setText("");
            }
            if(movieModel.time_taken !=null && movieModel.time_taken.length()>0)
            {
                endto.setText(""+movieModel.time_taken);
            }else{
                endto.setText("");
            }



            if(movieModel.time !=null && movieModel.time.length()>0)
            {

                String timd=movieModel.time;
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
            if(movieModel.time_taken !=null && movieModel.time_taken.length()>0)
            {
String timd=movieModel.time_taken;
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
