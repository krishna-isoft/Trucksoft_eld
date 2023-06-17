package com.trucksoft.isoft.isoft_elog.Break_report;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.trucksoft.isoft.isoft_elog.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Break_receyadap extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    static Preference pref;
    static Context context;
    List<Breakreport_model> movies;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    static Font_manager font_manager;
   static MediaPlayer mediaPlayer;
   static boolean isPlaying=false;

    public Break_receyadap(Context context, List<Breakreport_model> moviesz) {
        this.context = context;
        pref=Preference.getInstance(context);
        this.movies = moviesz;
font_manager=new Font_manager();

        //Log.e("movies","#"+movies);
        // Log.e("calling","adapter");
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new MovieHolder(inflater.inflate(R.layout.breakrep_homelist,parent,false));
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
        if(movies.get(position).note_type.equals("breakreport")){
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
        TextView txt_bmethod;
        TextView txt_btype;
        TextView txt_address;
        TextView txt_state;
        TextView txtstarttime;
        TextView txtendtime;
        TextView tstatus;
        TextView treason;
        TextView btn_no;
        TextView btn_play;
        LinearLayout linplay;
        LinearLayout lreason;


        public MovieHolder(View itemView) {
            super(itemView);
            date_text =  itemView
                    .findViewById(R.id.date_text);
           txt_bmethod =  itemView.findViewById(R.id.txtbmethod);
            txt_btype=itemView.findViewById(R.id.txt_btype);
//
            txt_address =  itemView.findViewById(R.id.address);
            txt_state =  itemView.findViewById(R.id.state);
            txtstarttime =  itemView.findViewById(R.id.starttime);
            txtendtime =  itemView.findViewById(R.id.endtime);
            tstatus=itemView.findViewById(R.id.tstatus);
            treason=itemView.findViewById(R.id.treason);
             btn_no = itemView.findViewById(R.id.btn_stop);
             linplay=itemView.findViewById(R.id.lin6);
            lreason=itemView.findViewById(R.id.lin7);
            btn_play = itemView.findViewById(R.id.btn_play);
            btn_play.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
            btn_no.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
//            btn_no.setTextColor(Color.parseColor("#E1000A"));
//            btn_play.setTextColor(Color.parseColor("#a9a9a9"));

            btn_no.setTextColor(Color.parseColor("#a9a9a9"));
            btn_play.setTextColor(Color.parseColor("#17BD17"));

        }

        void bindData(final Breakreport_model movieModel){

            if(movieModel.date !=null && movieModel.date.length()>0)
            {

String stdate=movieModel.date;


                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(stdate);
                    sdf = new SimpleDateFormat("MMM dd, yyyy");
                    String yourFormatedDateString = sdf.format(date);
                    date_text.setText(""+yourFormatedDateString);
                }catch (Exception e)
                {

                }


            }else{
                date_text.setText("");
            }
            if(movieModel.break_method !=null && movieModel.break_method.length()>0)
            {
                txt_bmethod.setText(""+movieModel.break_method);
            }else{
                txt_bmethod.setText("");
            }
//
//
//
            if(movieModel.type !=null && movieModel.type.length()>0)
            {
                txt_btype.setText(""+movieModel.type);
            }else{
                txt_btype.setText("");
            }
            if(movieModel.address !=null && movieModel.address.length()>0)
            {
                txt_address.setText(""+movieModel.address);
            }else{
                txt_address.setText("");
            }
            if(movieModel.state !=null && movieModel.state.length()>0)
            {
                txt_state.setText(""+movieModel.state);
            }else{
                txt_state.setText("");
            }

            if(movieModel.taken_time !=null && movieModel.taken_time.length()>0)
            {
                txtstarttime.setText(""+movieModel.taken_time);
            }else{
                txtstarttime.setText("");
            }

            if(movieModel.release_time !=null && movieModel.release_time.length()>0)
            {
                txtendtime.setText(""+movieModel.release_time);
            }else{
                txtendtime.setText("");
            }
            if(movieModel.reason !=null && movieModel.reason.length()>0)
            {
                treason.setText(""+movieModel.reason);
                lreason.setVisibility(View.VISIBLE);
            }else{
                treason.setText("");
                lreason.setVisibility(View.GONE);
            }
            if(movieModel.status !=null && movieModel.status.length()>0)
            {
                tstatus.setText(""+movieModel.status);

                if(movieModel.status.contentEquals("rejected") && movieModel.file !=null && movieModel.file.length()>0)
                {
                    linplay.setVisibility(View.VISIBLE);
                }else{
                    linplay.setVisibility(View.GONE);
                }
            }else{
                tstatus.setText("");
                linplay.setVisibility(View.GONE);
            }
            btn_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "" + movieModel.file;
                   // isPlaying=false;
                    //Log.e("urlurl", "@" + movieModel.file);
                    if (url != null && url.length() > 0) {
                        try {
                            if(!isPlaying) {
                               // Log.e("boolplay", "@" + isPlaying);

                                btn_play.setTextColor(Color.parseColor("#a9a9a9"));
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                mediaPlayer.setDataSource(url);
                                mediaPlayer.prepare(); // might take long! (for buffering, etc)
                                isPlaying=true;
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                       // Log.e("played", "finished");
                                        // performOnEnd();
                                        isPlaying=false;
                                        btn_play.setTextColor(Color.parseColor("#17BD17"));
                                    }

                                });
                                mediaPlayer.start();
                            }

                        } catch (IOException ie) {
                           // Log.e("vald","#"+ie.toString());
                        }

                    }
                }
            });
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }catch (Exception e)
                    {

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
