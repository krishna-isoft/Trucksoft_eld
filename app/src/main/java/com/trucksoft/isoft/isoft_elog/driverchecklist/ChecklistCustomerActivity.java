package com.trucksoft.isoft.isoft_elog.driverchecklist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trucksoft.isoft.isoft_elog.R;

import java.util.ArrayList;

/**
 * Created by isoft on 4/1/17.
 */

public class ChecklistCustomerActivity extends BaseAdapter {

    ArrayList<String> m_status_array, m_status_array_id,m_status_active=new ArrayList<String>();
    Context mcontext;
    String mTag;
    private int selectedItem = 0;
    LayoutInflater inflater;
    String MyPREFERENCES = "filter_status";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private int statusposition = 0;
    private String statusname;
    private String stval;
    ArrayList<String> status_mainenance=new ArrayList<>();
    @SuppressWarnings("static-access")
    public ChecklistCustomerActivity(Context context, String tag, int count,
                                     ArrayList<String> status_array, ArrayList<String> status_array_id, String value, ArrayList<String> status_active, ArrayList<String> status_array_mainenance) {
        // TODO Auto-generated constructor stub

        mcontext = context;
        mTag = tag;
        m_status_array = status_array;
        m_status_array_id = status_array_id;
        status_mainenance=status_array_mainenance;
        m_status_active=status_active;
        selectedItem = count;
        stval=value;

        //Log.e("m_status_array", "^"+m_status_array.toString());
        //Log.e("m_status_array_id", "^"+m_status_array_id.toString());
        //Log.e("m_status_active", "^"+m_status_active.toString());
        //Log.e("selectedItem", ""+selectedItem);


        inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);

        sharedpreferences = mcontext.getSharedPreferences(MyPREFERENCES,
                mcontext.MODE_PRIVATE);
        editor = sharedpreferences.edit();

    }

    public ChecklistCustomerActivity(Context context, String tag, int count,
                                     ArrayList<String> status_array, ArrayList<String> status_array_id, String value, ArrayList<String> status_array_mainenance) {
        // TODO Auto-generated constructor stub

        mcontext = context;
        mTag = tag;
        m_status_array = status_array;
        m_status_array_id = status_array_id;
        status_mainenance=status_array_mainenance;
        selectedItem = count;
        stval=value;

        //Log.e("m_status_array", "^t"+m_status_array.toString());
        //Log.e("m_status_array_id", "^"+m_status_array_id.toString());

        //Log.e("selectedItem", ""+selectedItem);


        inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);

        sharedpreferences = mcontext.getSharedPreferences(MyPREFERENCES,
                mcontext.MODE_PRIVATE);
        editor = sharedpreferences.edit();

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return m_status_array.size();
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
        TextView textView;
        ImageView imageView;
        ImageView imgmain;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.status_listview_layout_screen, null);
        }

        setAttributes(position, convertView);
        setSelection(position, convertView);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                setSelectedItem(position);
                //Log.e("mTag", "k"+mTag);

                if (mTag.equalsIgnoreCase("client")) {
                    // mBean.setClient(m_status_array.get(position));
                    editor.putString("client", m_status_array.get(position));
                    editor.putString("client_id",
                            m_status_array_id.get(position));
                    editor.commit();

                } else if (mTag.equalsIgnoreCase("shipper_from")) {
                    // mBean.setShip_from(m_status_array.get(position));
                    editor.putString("shipper_from",
                            m_status_array.get(position));
                    editor.putString("shipper_from_id",
                            m_status_array_id.get(position));
                    editor.commit();
                } else if (mTag.equalsIgnoreCase("shipper_to")) {
                    // mBean.setShip_to(m_status_array.get(position));
                    editor.putString("shipper_to", m_status_array.get(position));
                    editor.putString("shipper_to_id",
                            m_status_array_id.get(position));
                    editor.commit();
                } else if (mTag.equalsIgnoreCase("status")) {
                    // mBean.setShip_to(m_status_array.get(position));

                    //Log.e("mTagd", "k"+m_status_array.get(position)+"///"+stval);
//					String sname=m_status_array.get(position).trim();
//
//					if(sname.contentEquals("DELIVERED")|| sname.contentEquals("CANCELED")|| sname.contentEquals("PICKED UP"))
//					{
//						editor.putString("status", stval);
//						editor.commit();
//					}
//
//					else
//					{
//
//						editor.putString("status", m_status_array_id.get(position));
//						editor.commit();
//					}

                    if(m_status_active.size()>0)
                    {
                        String sname=m_status_active.get(position).trim();

                        if(sname.contentEquals("1"))
                        {
                            editor.putString("status", stval);
                            editor.commit();
                        }

                        else
                        {

                            editor.putString("status", m_status_array_id.get(position));
                            editor.commit();
                        }
                    }else
                    {
                        editor.putString("status", m_status_array_id.get(position));
                        editor.commit();
                    }




                } else if (mTag.equalsIgnoreCase("dispatch_f_ticket")) {
                    // mBean.setShip_to(m_status_array.get(position));
                    editor.putString("dispatch_f_ticket",
                            m_status_array.get(position));
                    editor.putString("dispatch_f_ticket_id",
                            m_status_array_id.get(position));
                    editor.commit();
                } else if (mTag.equalsIgnoreCase("shipper_f_ticket")) {
                    // mBean.setShip_to(m_status_array.get(position));
                    editor.putString("shipper_f_ticket",
                            m_status_array.get(position));
                    editor.putString("shipper_f_ticket_id",
                            m_status_array_id.get(position));
                    editor.commit();
                } else if (mTag.equalsIgnoreCase("consignee_f_ticket")) {
                    // mBean.setShip_to(m_status_array.get(position));
                    editor.putString("consignee_f_ticket",
                            m_status_array.get(position));
                    editor.putString("consignee_f_ticket_id",
                            m_status_array_id.get(position));
                    editor.commit();
                } else if (mTag.equalsIgnoreCase("driver_f_ticket")) {
                    // mBean.setShip_to(m_status_array.get(position));
                    editor.putString("driver_f_ticket",
                            m_status_array.get(position));
                    editor.putString("driver_f_ticket_id",
                            m_status_array_id.get(position));
                    editor.commit();
                } else if (mTag.equalsIgnoreCase("truck_f_ticket") || mTag.equalsIgnoreCase("truck_d_checklist")) {
                    // mBean.setShip_to(m_status_array.get(position));

                    if(mTag !=null && mTag.contentEquals("truck_d_checklist"))
                    {
                        String smainnn=status_mainenance.get(position);
                        if(smainnn.contentEquals("1"))
                        {
                            editor.putString("truck_f_ticket",
                                    "");
                            editor.putString("truck_f_ticket_id",
                                    "");
                            editor.commit();
                            Toast.makeText(mcontext,"This Truck is been reported with a problem and needs to be signed off by an Authorized person to be able to select. ",Toast.LENGTH_SHORT).show();
                        }else {
                            editor.putString("truck_f_ticket",
                                    m_status_array.get(position));
                            editor.putString("truck_f_ticket_id",
                                    m_status_array_id.get(position));
                            editor.commit();
                        }
                    }else{
                        editor.putString("truck_f_ticket",
                                m_status_array.get(position));
                        editor.putString("truck_f_ticket_id",
                                m_status_array_id.get(position));
                        editor.commit();
                    }



                }else if (mTag.equalsIgnoreCase("trailer_d_checklist")) {
                    // mBean.setShip_to(m_status_array.get(position));
                    if (mTag != null && mTag.contentEquals("trailer_d_checklist")) {
                        String smainnn = status_mainenance.get(position);
                        if (smainnn.contentEquals("1")) {
                            editor.putString("trailer_d_checklist",
                                    "");
                            editor.putString("trailer_d_checklist_id",
                                    "");
                            editor.commit();
                            Toast.makeText(mcontext,"This Trailer is been reported with a problem and needs to be signed off by an Authorized person to be able to select.",Toast.LENGTH_SHORT).show();

                        } else {
                            editor.putString("trailer_d_checklist",
                                    m_status_array.get(position));
                            editor.putString("trailer_d_checklist_id",
                                    m_status_array_id.get(position));
                            editor.commit();
                        }
                    }
                }

            }
        });
        return convertView;
    }

    public void setAttributes(final int position, View convertView) {
       // Log.e("call","setAttributes");
        Holder holder = new Holder();

        holder.textView = (TextView) convertView
                .findViewById(R.id.filter_status_textView2);
        holder.imgmain=convertView.findViewById(R.id.filter_status_maintain);

        holder.textView.setText(m_status_array.get(position));




//		String sname=m_status_array.get(position);
//		if(sname.contentEquals("DELIVERED")|| sname.contentEquals("CANCELED")|| sname.contentEquals("PICKED UP"))
//		{
//			holder.textView.setBackgroundColor(Color.parseColor("#A9A9A9"));
//		}else
//		{
//			holder.textView.setBackgroundColor(Color.parseColor("#FFFFFF"));
//		}
//
        if(m_status_active.size()>0)
        {
            String sname=m_status_active.get(position);
            if(sname.contentEquals("1"))
            {
                holder.textView.setBackgroundColor(Color.parseColor("#ECF03C"));
            }else
            {
                holder.textView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }else
        {
            holder.textView.setBackgroundColor(Color.parseColor("#FFFFFF"));

            if(mTag !=null && mTag.length()>0) {
                if (mTag.contentEquals("truck_d_checklist") || mTag.contentEquals("trailer_d_checklist"))
                {
if(status_mainenance !=null && status_mainenance.size()>0)
{
    String smain=status_mainenance.get(position);
    if(smain.contentEquals("1"))
    {
        holder.textView.setBackgroundColor(Color.parseColor("#ECF03C"));
        holder.imgmain.setVisibility(View.VISIBLE);
    }else{
        holder.imgmain.setVisibility(View.GONE);
        holder.textView.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }
}
                }
            }
        }


    }

    public void setSelection(final int position, View convertView) {
       // Log.e("call","setSelection");
        Holder holder = new Holder();

        holder.imageView = (ImageView) convertView
                .findViewById(R.id.filter_status_check_imageView);
        holder.imgmain=convertView.findViewById(R.id.filter_status_maintain);


        //String st_name=m_status_array.get(position);


        if (position == selectedItem) {
            if(m_status_active.size()>0)
            {
                if(m_status_active.get(position).contentEquals("1"))
                {
                    holder.imageView.setVisibility(View.INVISIBLE);
                }else
                {
                    holder.imageView.setVisibility(View.VISIBLE);
                }
            }else
            {
                if(mTag !=null && mTag.length()>0) {
                    if (mTag.contentEquals("truck_d_checklist") || mTag.contentEquals("trailer_d_checklist"))
                    {
                        if(status_mainenance !=null && status_mainenance.size()>0)
                        {
                            String smain=status_mainenance.get(position);
                            if(smain.contentEquals("1"))
                            {
                                holder.imgmain.setVisibility(View.VISIBLE);
                                holder.imageView.setVisibility(View.INVISIBLE);
                            }else{
                                holder.imgmain.setVisibility(View.GONE);
                                holder.imageView.setVisibility(View.VISIBLE);

                            }
                        }
                    }else{
                        holder.imageView.setVisibility(View.VISIBLE);
                    }
                }else{
                    holder.imageView.setVisibility(View.VISIBLE);
                }



            }
        } else {
            holder.imageView.setVisibility(View.INVISIBLE);
        }
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;

        this.statusname=m_status_array.get(selectedItem);
        if(m_status_active.size()>0)
        {
            this.statusposition=selectedItem;
        }
        notifyDataSetChanged();
    }
}
