package com.trucksoft.isoft.isoft_elog.Vehicle;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.isoft.trucksoft_elog.Isoft_adapter.Font_manager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.trucksoft.isoft.isoft_elog.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 16/11/17.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Contact> contactList;
    private List<Contact> contactListFiltered;
    private ContactsAdapterListener listener;
    Font_manager font_manager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtvehicle, txtvin,txtvehiclestatus,txtvehiclestatus1;
        public TextView txtvid, txtchk,txtvid1;
        public TextView txtstatus;
        public TextView txtbluetooth;
        public TextView txtcellular;
        public ImageView thumbnail;
        public RelativeLayout rel;

        public MyViewHolder(View view) {
            super(view);
            txtvehicle = view.findViewById(R.id.name);
            txtvin = view.findViewById(R.id.phone);

            txtchk = view.findViewById(R.id.txtcheck);
            txtstatus = view.findViewById(R.id.txtassigned);
            thumbnail = view.findViewById(R.id.thumbnail);
            txtvid = view.findViewById(R.id.txtvid);
            txtvehiclestatus=view.findViewById(R.id.txtvehiclestatus);
            txtvid1 = view.findViewById(R.id.txtvid1);
            txtvehiclestatus1=view.findViewById(R.id.txtvehiclestatus1);
             rel = view.findViewById(R.id.view_foreground);

            txtbluetooth = view.findViewById(R.id.txtconnectionblue);
            txtcellular= view.findViewById(R.id.txtconnectioncellular);
            txtbluetooth.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
          //  txtcellular.setTypeface(font_manager.get_icons("fonts/ionicons.ttf", context));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
        font_manager=new Font_manager();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Contact contact = contactListFiltered.get(position);

        String devicetype=contact.getD_type();

        if(devicetype !=null && devicetype.length()>0)
        {

            if(devicetype.contentEquals("1"))
            {
                holder.txtbluetooth.setVisibility(View.VISIBLE);
            }else{
                holder.txtbluetooth.setVisibility(View.GONE);
            }
        }else{
            holder.txtbluetooth.setVisibility(View.GONE);
        }
        holder.txtvehicle.setText(contact.getmodel_name());
        holder.txtvin.setText(contact.getvin());
        holder.txtvehiclestatus.setText(""+contact.getVehiclestatus());
        String vstatus=""+contact.getVehiclestatus();
        String scolor=""+contact.getStatus_color();
       // Log.e("drivername","@@@"+contact.getdriver_name());
        //Log.e("driver statys",""+contact.getVehiclestatus());
        //Log.e("drivestta",""+contact.getstatus());
        if(contact.getvid()==null || contact.getvid().length()==0|| contact.getvid().contentEquals("null"))
        {
            holder.txtvid.setText("" );
        }else {
            if(contact.getvid().contentEquals("false"))
            {
                holder.txtvid.setText("" );
            }else {
                holder.txtvid.setText("" + contact.getvid());
            }
        }

//        Glide.with(context)
//                .load(contact.getvehicle_image())
//                .apply(RequestOptions.circleCropTransform())
//                .into(holder.thumbnail);
        Picasso.with(context)
                .load(contact.getvehicle_image() + "?.time();")
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.mipmap.car_off)
                .into(holder.thumbnail);
        if(contact.status.contentEquals("1"))
        {
if(contact.getdriver_name() !=null && contact.getdriver_name().length()>0) {
    holder.txtstatus.setText("Not Available : " + contact.getdriver_name());
}else{
    holder.txtstatus.setText("Not Available ");
}
            // rl.setBackgroundColor(Color.parseColor("#8FFF"));
            holder.rel.setBackgroundColor(Color.parseColor("#bdbdbd"));
            holder.txtvehicle.setTextColor(Color.parseColor("#ffffffff"));
            holder.txtvin.setTextColor(Color.parseColor("#ffffffff"));

            holder.txtstatus.setTextColor(Color.parseColor("#ffffffff"));
            holder.txtvid.setTextColor(Color.parseColor("#ffffffff"));
            holder.txtvehiclestatus.setTextColor(Color.parseColor("#ffffffff"));
            holder.txtvid1.setTextColor(Color.parseColor("#ffffffff"));
            holder.txtvehiclestatus1.setTextColor(Color.parseColor("#ffffffff"));
        }else{




                holder.rel.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.txtvehicle.setTextColor(Color.parseColor("#000000"));
                holder.txtvin.setTextColor(Color.parseColor("#8c8c8c"));

                holder.txtstatus.setTextColor(Color.parseColor("#8c8c8c"));
            holder.txtvid.setTextColor(Color.parseColor("#8c8c8c"));
                holder.txtvehiclestatus.setTextColor(Color.parseColor("#8c8c8c"));
                holder.txtstatus.setText(" Available ");
            holder.txtvid1.setTextColor(Color.parseColor("#8c8c8c"));
            holder.txtvehiclestatus1.setTextColor(Color.parseColor("#8c8c8c"));

        }

        if(vstatus !=null && vstatus.length()>0 && !vstatus.contentEquals("null")) {

            if (vstatus.contentEquals("stopped")) {

                holder.txtvehiclestatus.setBackgroundColor(Color.parseColor(scolor));
                holder.txtvehiclestatus.setTextColor(Color.parseColor("#ffffffff"));

            } else if (vstatus.contentEquals("moving")) {

                holder.txtvehiclestatus.setTextColor(Color.parseColor("#ffffffff"));
                holder.txtvehiclestatus.setBackgroundColor(Color.parseColor(scolor));
            } else if (vstatus.contentEquals("On") || vstatus.contentEquals("on")) {

                holder.txtvehiclestatus.setTextColor(Color.parseColor("#ffffffff"));
                holder.txtvehiclestatus.setBackgroundColor(Color.parseColor(scolor));
            }
            else if (vstatus.contentEquals("moving")) {

                holder.txtvehiclestatus.setTextColor(Color.parseColor("#ffffffff"));
                holder.txtvehiclestatus.setBackgroundColor(Color.parseColor(scolor));
            }

            else if (vstatus.contentEquals("stale")) {
                holder.txtvehiclestatus.setTextColor(Color.parseColor("#ffffffff"));
                holder.txtstatus.setText("Not Available ");
                holder.txtvehiclestatus.setBackgroundColor(Color.parseColor(scolor));
            }
        }

    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getmodel_name().toLowerCase().contains(charString.toLowerCase()) || row.getvin().contains(charSequence)
                                || row.getModel().contains(charSequence)
                                || row.getMake().contains(charSequence)
                                || row.getYear().contains(charSequence)) {
                            filteredList.add(row);
                        }

                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Contact contact);
    }

}
