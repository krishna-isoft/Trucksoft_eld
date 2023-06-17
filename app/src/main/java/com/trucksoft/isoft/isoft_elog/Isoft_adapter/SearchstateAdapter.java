package com.trucksoft.isoft.isoft_elog.Isoft_adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trucksoft.isoft.isoft_elog.R;

import java.util.List;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class SearchstateAdapter extends ArrayAdapter<state_model> implements View.OnClickListener{

    private List<state_model> dataSet;
    Context mContext;


    // View lookup cache
    private static class ViewHolder {
        TextView txtName,tid;
        ImageView imgtick;
        RelativeLayout rlhead;


    }



    public SearchstateAdapter(List<state_model> data, Context context) {
        super(context, R.layout.status_listview_state, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        state_model dataModel=(state_model)object;




    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        state_model dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.status_listview_state, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.filter_status_textView2);
            viewHolder.imgtick=convertView.findViewById(R.id.filter_status_check_imageView);
            viewHolder.tid=convertView.findViewById(R.id.tid);
            viewHolder.rlhead=convertView.findViewById(R.id.rlhead);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.imgtick.setVisibility(View.INVISIBLE);
        viewHolder.tid.setText(""+dataModel.id);
        viewHolder.txtName.setText("State Name : "+dataModel.state_name);
      //  viewHolder.tdemoname.setText(""+dataModel.Trailer_name);


            viewHolder.rlhead.setBackgroundColor(Color.parseColor("#FFFFFF"));
            viewHolder.imgtick.setVisibility(View.INVISIBLE);
            viewHolder.txtName.setTextColor(Color.parseColor("#000000"));






//        viewHolder.txtName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(viewHolder.tid.getText().toString().contentEquals("0")) {
//                    viewHolder.imgtick.setVisibility(View.VISIBLE);
//                    viewHolder.tid.setText("1");
//                }else{
//                    viewHolder.tid.setText("0");
//                    viewHolder.imgtick.setVisibility(View.INVISIBLE);
//                }
//            }
//        });

        return convertView;
    }


}
