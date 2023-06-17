package com.trucksoft.isoft.isoft_elog.E_log_chat.Dispatchchat_adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_home.DispatchChatRoomActivity;
import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_model.Dispatch_chat_mod;
import com.isoft.trucksoft_elog.Multiused.CommonUtil;
import com.trucksoft.isoft.isoft_elog.R;

import java.util.ArrayList;

/**
 * Created by isoft on 19/9/17.
 */

public class DispatchnotificationAdapter extends BaseAdapter {
    ArrayList<Dispatch_chat_mod> mBeans = new ArrayList<Dispatch_chat_mod>();
    Context mcontext;
    LayoutInflater inflater;
    Dialog dialognn;
    CommonUtil commonUtil;

    public DispatchnotificationAdapter(Context context,
                                       ArrayList<Dispatch_chat_mod> mBeans, Dialog dialogn) {
        // TODO Auto-generated constructor stub
        mcontext = context;
        this.mBeans = mBeans;
        this.dialognn=dialogn;
        commonUtil = new CommonUtil(context);
        inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mBeans.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView name;
        TextView content;
        TextView count;
        TextView time;
        TextView txtavailable;
        ImageView imgdispatcher;
        RelativeLayout count_bg;
        ImageView imgonline;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.chat_listview_layout_screen, null);
        }

        setAttributes(position, convertView);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialognn.dismiss();
                click(position);
            }
        });
        return convertView;
    }

    public void click(int position) {
//        Intent mintent = new Intent(mcontext, ChatViewBackgroundActivity.class);
//        mintent.putExtra("name", mBeans.get(position).getName());
//
//        mintent.putExtra("position", position);
//      //  String imk=mBeans.get(position).getOpponent_image();
//       // mintent.putExtra("dispimg", ""+imk);
//
//        String stonline=mBeans.get(position).getOnline();
//
//        //Log.e("stonline", "h"+stonline);
//        mintent.putExtra("online", ""+stonline);

        Dispatch_chat_mod chatRoom = mBeans.get(position);
        Intent intent = new Intent(mcontext, DispatchChatRoomActivity.class);
        intent.putExtra("chat_room_id", chatRoom.getId());
        intent.putExtra("name", chatRoom.getName());
       mcontext.startActivity(intent);

        //((ChatActivity) mcontext).startActivityForResult(mintent, 1);
    }


    public void setAttributes(final int position, View convertView) {


        Holder holder = new Holder();

        holder.imgdispatcher = (ImageView) convertView
                .findViewById(R.id.chat_list_iv_profilepic);


        holder.imgonline = (ImageView) convertView
                .findViewById(R.id.chat_list_iv_chat_count1);


        holder.txtavailable=(TextView)convertView.findViewById(R.id.chat_yext);







      //  String imk=mBeans.get(position).getOpponent_image();


        int screenSize = mcontext.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;


        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                holder.imgdispatcher.getLayoutParams().height = 105;
                holder.imgdispatcher.getLayoutParams().width = 110;
                holder.imgdispatcher.requestLayout();
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:

                holder.imgdispatcher.getLayoutParams().height = 95;
                holder.imgdispatcher.getLayoutParams().width = 100;
                holder.imgdispatcher.requestLayout();

                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                holder.imgdispatcher.getLayoutParams().height = 65;
                holder.imgdispatcher.getLayoutParams().width = 70;
                holder.imgdispatcher.requestLayout();
                break;

        }









//        Picasso.with(mcontext).load(imk)
//                .placeholder(R.drawable.userpic).into(holder.imgdispatcher);

        holder.name = (TextView) convertView
                .findViewById(R.id.chat_list_tv_name);
        holder.name.setText(mBeans.get(position).getName());



        holder.count = (TextView) convertView
                .findViewById(R.id.chat_list_tv_chat_count);

        holder.count_bg = (RelativeLayout) convertView
                .findViewById(R.id.chat_list_rl_chat_count);
        int chatCount = 0;
        if (!(String.valueOf(mBeans.get(position).getUnreadCount())
                .equalsIgnoreCase("null") || String.valueOf(mBeans.get(position).getUnreadCount())
                .equalsIgnoreCase("false"))) {
            if (String.valueOf(mBeans.get(position).getUnreadCount()).length() > 0) {
                chatCount = Integer
                        .valueOf(String.valueOf(mBeans.get(position).getUnreadCount()));
            }
        }

        String online_status=mBeans.get(position).getOnline();

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


        if (chatCount == 0) {
            holder.count_bg.setVisibility(View.INVISIBLE);
        } else {
            holder.count_bg.setVisibility(View.VISIBLE);
            holder.count.setText(""
                    + String.valueOf(mBeans.get(position).getUnreadCount()));
        }

		/*for (int i = 0; i < mBeans.get(position).getmChatHistoryBeans().size(); i++) {

		}*/

        holder.content = (TextView) convertView
                .findViewById(R.id.chat_list_tv_content);
        holder.time = (TextView) convertView
                .findViewById(R.id.chat_list_tv_time);

        if(mBeans.get(position).getLastMessage()!=null)
        {


            holder.content.setText(mBeans.get(position).getLastMessage());

            String s_time = mBeans.get(position).getTimestamp();

//            String s_date = mBeans.get(position).getmChatHistoryBeans()
//                    .get((mBeans.get(position).getmChatHistoryBeans().size()) - 1)
//                    .getDate();
            //Log.e("s_date", "_"+s_date);changeinnormaldate
          //  holder.time.setText(commonUtil.changeinnormaldate(s_date)+" "+s_time);
            holder.time.setText(" "+s_time);
        }else{
            holder.content.setText("");
            holder.time.setText("");
        }
		/*
		 * StringTokenizer tokens = new StringTokenizer(date_time, " "); String
		 * date = ""; if (tokens.hasMoreTokens()) { date = tokens.nextToken(); }
		 * String time = ""; if (tokens.hasMoreTokens()) { time =
		 * tokens.nextToken(); } String mode = ""; if (tokens.hasMoreTokens()) {
		 * mode = tokens.nextToken(); }
		 */



    }
}
