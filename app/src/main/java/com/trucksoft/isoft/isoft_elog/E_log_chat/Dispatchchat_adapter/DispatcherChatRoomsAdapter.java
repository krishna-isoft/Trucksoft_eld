package com.trucksoft.isoft.isoft_elog.E_log_chat.Dispatchchat_adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_model.Dispatch_chat_mod;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.trucksoft.isoft.isoft_elog.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

//import android.util.Log;


public class DispatcherChatRoomsAdapter extends RecyclerView.Adapter<DispatcherChatRoomsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Dispatch_chat_mod> chatRoomArrayList;
    private static String today;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, message, timestamp, count;
        TextView txtavailable;
        ImageView imgonline;
        ImageView imgcount;
        ImageView img_profile;
        ImageView img_msg;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.chat_list_tv_name);
            message = (TextView) view.findViewById(R.id.chat_list_tv_content);
            timestamp = (TextView) view.findViewById(R.id.chat_list_tv_time);
            count = (TextView) view.findViewById(R.id.count);
            img_profile=(ImageView) view.findViewById(R.id.chat_list_iv_profilepic);
imgcount=(ImageView)view.findViewById(R.id.chat_list_iv_chat_count);
            imgonline = (ImageView) view
                    .findViewById(R.id.chat_list_iv_chat_count1);
            img_msg= (ImageView) view
                    .findViewById(R.id.msg);


            txtavailable=(TextView)view.findViewById(R.id.chat_yext);

        }
    }


    public DispatcherChatRoomsAdapter(Context mContext, ArrayList<Dispatch_chat_mod> chatRoomArrayList) {
        this.mContext = mContext;
        this.chatRoomArrayList = chatRoomArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
              //  .inflate(R.layout.chat_room_list_view, parent, false);
                .inflate(R.layout.demo_chatlist, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Dispatch_chat_mod chatRoom = chatRoomArrayList.get(position);
        //Log.e("get","&&&&&&&&&&&&&&&&&&&&&&&&&"+chatRoom.getName());
        holder.name.setText(chatRoom.getName());
        if(chatRoom.getLastMessage() !=null && chatRoom.getLastMessage().length()>0 && !chatRoom.getLastMessage().contentEquals("null")) {
        if(chatRoom.getLastMessage().contains(".png"))
        {
            holder.img_msg.setVisibility(View.VISIBLE);
            holder.message.setVisibility(View.GONE);
            String msg=chatRoom.getLastMessage();
            StringTokenizer sk=new StringTokenizer(msg,">>");
            String a=sk.nextToken();
           // Log.e("a",""+a);
            if(a.contains(".png")) {
                //Log.e("aaD",""+a);
                Picasso.with(mContext).load(a).into( holder.img_msg);
//               Picasso.with(mContext)
//                       .load(a + "?.time();")
//                       .memoryPolicy(MemoryPolicy.NO_CACHE)
//                       .networkPolicy(NetworkPolicy.NO_CACHE)
//                       .error(R.drawable.whitekt)
//                       .into(((ViewHolder) holder).imgmesagepng);
            }else {
                holder.img_msg.setVisibility(View.GONE);
                holder.message.setVisibility(View.VISIBLE);
            }
        }else{
            holder.message.setText(fromHtml(chatRoom.getLastMessage()));
        }
        }else{
            holder.message.setText(fromHtml(chatRoom.getLastMessage()));
        }

        String imurl=chatRoom.getImage();
        if(imurl.contains("noprof.jpg"))
        {

        }else {
            Picasso.with(mContext)
                    .load(imurl + "?.time();")
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    //.placeholder(R.drawable.prog_animation)
                    .error(R.drawable.userpic)
                    .into(holder.img_profile);
        }
        if (chatRoom.getUnreadCount() > 0) {
            holder.count.setText(String.valueOf(chatRoom.getUnreadCount()));
            holder.count.setVisibility(View.VISIBLE);
            holder.imgcount.setVisibility(View.VISIBLE);
        } else {
            holder.count.setVisibility(View.GONE);
            holder.imgcount.setVisibility(View.GONE);
        }

        String online_status=chatRoom.getOnline();

        //	Log.e("online_status", ""+online_status);
        if(online_status.contentEquals("1"))
        {

            //	Log.e("green", "green");
            holder.txtavailable.setText("Available");
            holder.txtavailable.setTextColor(Color.parseColor("#00660F"));
            holder.imgonline.setBackgroundResource(R.drawable.chat_online_green);
        }else
        {
            holder.txtavailable.setText("Not Available");
            holder.txtavailable.setTextColor(Color.parseColor("#7F7F7F"));
            holder.imgonline.setBackgroundResource(R.drawable.chat_online);
        }

        holder.timestamp.setText(getTimeStamp(chatRoom.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return chatRoomArrayList.size();
    }

    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }
}
