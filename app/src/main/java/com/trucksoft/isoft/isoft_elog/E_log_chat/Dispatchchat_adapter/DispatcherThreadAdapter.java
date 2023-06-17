package com.trucksoft.isoft.isoft_elog.E_log_chat.Dispatchchat_adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.isoft.trucksoft_elog.E_log_chat.Dispatchchat_model.Dispatch_message;
import com.isoft.trucksoft_elog.E_log_chat.Image_view_bol;
import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
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

public class DispatcherThreadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = DispatcherThreadAdapter.class.getSimpleName();

    private String userId;
    private int SELF = 500;
    private static String today;
    Font_manager font_manager;
    private Context mContext;
    private ArrayList<Dispatch_message> messageArrayList;
    Preference pref;
    boolean boolstatus;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message, timestamp,fileurl;
        TextView txt_view;
        ImageView imgmesage,imgmesagepng;

        public ViewHolder(View view) {
            super(view);
            message = (TextView) itemView.findViewById(R.id.message);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            txt_view= (TextView) itemView.findViewById(R.id.chatview_iv_send_statusk);
            imgmesage=itemView.findViewById(R.id.imgmsg);
            imgmesagepng=itemView.findViewById(R.id.imgmsgpng);
            fileurl=itemView.findViewById(R.id.fileurl);
        }
    }


    public DispatcherThreadAdapter(Context mContext, ArrayList<Dispatch_message> messageArrayList, String userId) {
        this.mContext = mContext;
        pref=Preference.getInstance(mContext);
        this.messageArrayList = messageArrayList;
        this.userId = userId;
        font_manager=new Font_manager();
        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        // view type is to identify where to render the chat message
        // left or right
       // Log.e("dddd",""+"----------"+viewType);
        if (viewType == SELF ) {
            // self message
           // Log.e("self",""+"self"+viewType);
            //Log.e("boolstatus","****************"+boolstatus);
//            itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.chat_item_self, parent, false);

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.drivermessage, parent, false);
        } else {
            // others message
            //Log.e("other",""+"other");

//            itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.chat_item_other, parent, false);
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dispatchermessage, parent, false);
        }


        return new ViewHolder(itemView);
    }


    @Override
    public int getItemViewType(int position) {
        Dispatch_message message = messageArrayList.get(position);
       // Log.e("1",message.getUser().getName());
       // Log.e("2",message.getUser().getId());
       // Log.e("userId",userId);
        if (message.getUser().getId().equals(userId)) {
           // Log.e("status","yes "+true);

            return SELF;

        }

        return position;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Dispatch_message message = messageArrayList.get(position);


        Resources res = holder.itemView.getContext().getResources();
        ((ViewHolder) holder).message.setText(fromHtml(message.getMessage().trim()));
        String st_status=message.getchstatus();
       // Log.e("msg1",""+"&&"+fromHtml(message.getMessage()));
       // Log.e("msg1",""+"&&"+fromHtml(message.getMessage()));

       // Log.e("getattac",""+"&&"+message.getattachment());
        String filed=message.getfile();
      //  Log.e("file1","#"+filed);
       // Log.e("filed",""+"&&"+message.getattachment());
        if(message.getMessage() !=null && message.getMessage().length()>0 && !message.getMessage().contentEquals("null")) {
       if(message.getMessage().contains(".png"))
       {
           ((ViewHolder) holder).imgmesagepng.setVisibility(View.VISIBLE);
           ((ViewHolder) holder).message.setVisibility(View.GONE);
           String msg=message.getMessage();
           StringTokenizer sk=new StringTokenizer(msg,">>");
           String a=sk.nextToken();
          // Log.e("a",""+a);
           if(a.contains(".png")) {
             //  Log.e("aa",""+a);
              Picasso.with(mContext).load(a).into(((ViewHolder) holder).imgmesagepng);
//               Picasso.with(mContext)
//                       .load(a + "?.time();")
//                       .memoryPolicy(MemoryPolicy.NO_CACHE)
//                       .networkPolicy(NetworkPolicy.NO_CACHE)
//                       .error(R.drawable.whitekt)
//                       .into(((ViewHolder) holder).imgmesagepng);
           }else {
               //Log.e("cc",""+a);
               //@drawable/ic_insert_emoticon_black_24dp
               ((ViewHolder) holder).imgmesagepng.setVisibility(View.GONE);
               ((ViewHolder) holder).message.setVisibility(View.VISIBLE);
               ((ViewHolder) holder).message.setText(""+a);
           }
           if(sk.hasMoreTokens())
           {
               ((ViewHolder) holder).imgmesagepng.setVisibility(View.VISIBLE);
               ((ViewHolder) holder).message.setVisibility(View.VISIBLE);
               String b=""+fromHtml(sk.nextToken());
              // Log.e("b",""+b);
               if(b.contains(".png")) {
                   Picasso.with(mContext).load(b).into(((ViewHolder) holder).imgmesagepng);
               }else {
                   if(message.getMessage().contains(".png")) {
                       ((ViewHolder) holder).imgmesagepng.setVisibility(View.GONE);
                       ((ViewHolder) holder).message.setVisibility(View.GONE);
                   }else {
                       ((ViewHolder) holder).imgmesagepng.setVisibility(View.GONE);
                       ((ViewHolder) holder).message.setVisibility(View.VISIBLE);
                   }
               }
           }
       }
        }
            if(message.getattachment() !=null && message.getattachment().length()>0 && !message.getattachment().contentEquals("null")) {
            if (message.getattachment().contentEquals("1")) {
                ((ViewHolder) holder).imgmesage.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).message.setVisibility(View.GONE);
                String file = message.getfile();
                ((ViewHolder) holder).fileurl.setText(""+file);
                //Log.e("file2",""+file+"?.time();");
                Picasso.with(mContext)
                        .load(file + "?.time();")
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .placeholder(R.drawable.prog_animation)
                        .error(R.drawable.whitekt)
                        .into(((ViewHolder) holder).imgmesage);
            } else {
                if(message.getMessage().contains(".png")) {
                    ((ViewHolder) holder).imgmesage.setVisibility(View.GONE);
                    ((ViewHolder) holder).message.setVisibility(View.GONE);
                }else {
                    ((ViewHolder) holder).imgmesage.setVisibility(View.GONE);
                    ((ViewHolder) holder).message.setVisibility(View.VISIBLE);
                }






//                ((ViewHolder) holder).imgmesage.setVisibility(View.GONE);
//                ((ViewHolder) holder).message.setVisibility(View.VISIBLE);
            }
        }
        ((ViewHolder) holder).imgmesage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttt = new Intent(mContext, Image_view_bol.class);
                intenttt.putExtra("img_url", "" + ((ViewHolder) holder).fileurl.getText().toString());
                mContext.startActivity(intenttt);
            }
        });
        String fc=pref.getString(Constant.READ_STATUSS);
        //Log.e("fc","#"+fc);
        if(fc !=null &&  fc.length()>0 && !fc.contentEquals("null")) {
            if (fc.contentEquals("1")) {
                ((ViewHolder) holder).txt_view.setText(R.string.tick_icon);
                ((ViewHolder) holder).txt_view.setTextColor(res.getColor(R.color.viedtick));

            }else{
                if (st_status != null && st_status.length() > 0 && !st_status.contentEquals("null")) {
                    if (st_status.contentEquals("1")) {
                        ((ViewHolder) holder).txt_view.setText(R.string.tick_icon);
                        ((ViewHolder) holder).txt_view.setTextColor(res.getColor(R.color.lkgray));
                    } else {
                        ((ViewHolder) holder).txt_view.setText(R.string.tick_icon);
                        ((ViewHolder) holder).txt_view.setTextColor(res.getColor(R.color.viedtick));
                    }
                } else {
                    ((ViewHolder) holder).txt_view.setText(R.string.tick_singleicon);
                    ((ViewHolder) holder).txt_view.setTextColor(res.getColor(R.color.lkgray));
                }
            }
        }

        else {
            if (st_status != null && st_status.length() > 0 && !st_status.contentEquals("null")) {
                if (st_status.contentEquals("1")) {
                    ((ViewHolder) holder).txt_view.setText(R.string.tick_icon);
                    ((ViewHolder) holder).txt_view.setTextColor(res.getColor(R.color.lkgray));
                } else {
                    ((ViewHolder) holder).txt_view.setText(R.string.tick_icon);
                    ((ViewHolder) holder).txt_view.setTextColor(res.getColor(R.color.viedtick));
                }
            } else {
                ((ViewHolder) holder).txt_view.setText(R.string.tick_singleicon);
                ((ViewHolder) holder).txt_view.setTextColor(res.getColor(R.color.lkgray));
            }
        }


        ((ViewHolder) holder). txt_view.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",mContext));
        String timestamp = getTimeStamp(message.getCreatedAt());

        if (message.getUser().getName() != null)
            //timestamp = message.getUser().getName() + ", " + timestamp;
            //timestamp = timestamp;
        ((ViewHolder) holder).timestamp.setText(timestamp);
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
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
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }
//private void stcovert(String msg)
//{
//    String s1=fromHtml(msg);
//    }

}

