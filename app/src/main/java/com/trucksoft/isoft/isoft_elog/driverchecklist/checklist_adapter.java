package com.trucksoft.isoft.isoft_elog.driverchecklist;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.squareup.picasso.Picasso;
import com.trucksoft.isoft.isoft_elog.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class checklist_adapter extends BaseAdapter {
    private Context mcontext;
    private Preference pref;
    private List<Chlitem_model> movies;
    private List<Chlitem_model> mvselected;
    LayoutInflater inflater;
    static OnItemCheckListener onItemCheckListener;
    private ArrayList<String> selectid=new ArrayList<>();
    private ArrayList<String> selectidgood=new ArrayList<>();
    ArrayList<String> truck_item_oldz=new ArrayList<>();


    private ArrayList<String> selectidtrailer=new ArrayList<>();
    private ArrayList<String> selectidgoodtrailer=new ArrayList<>();
    ArrayList<String> truck_item_oldztrailer=new ArrayList<>();
    ProgressDialog dialog;

   String tag="";
    String id="";

    /*
     * isLoading - to set the remote loading and complete status to fix back to back load more call
     * isMoreDataAvailable - to set whether more data from server available or not.
     * It will prevent useless load more request even after all the server data loaded
     * */
    public interface OnItemCheckListener
    {
        void OnItemCheck(Chlitem_model rcy, int position);
        void OnItemUnCheck(Chlitem_model item, int position);
        void OnImageClick(Chlitem_model item, int position);
    }
    public checklist_adapter(Context context, List<Chlitem_model> status_array, ArrayList<String> selectidz, ArrayList<String> selectidgoodz, ArrayList<String> truck_item_old, String idz,String tag, OnItemCheckListener onItemCheckListener) {
        // TODO Auto-generated constructor stub
        mcontext=context;
        pref=Preference.getInstance(mcontext);
        mvselected=new ArrayList<>();
        this.movies=status_array;
//        this.selectid=selectidz;
//        this.selectidgood=selectidgoodz;
//        this.truck_item_oldz=truck_item_old;
        this.id=idz;
        this.tag=tag;

        if(tag.contentEquals("truck"))
        {
            this.selectid=selectidz;
            this.selectidgood=selectidgoodz;
            this.truck_item_oldz=truck_item_old;
        }else{
            this.selectidtrailer=selectidz;
            this.selectidgoodtrailer=selectidgoodz;
            this.truck_item_oldztrailer=truck_item_old;
        }


       this.onItemCheckListener=onItemCheckListener;
        inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
        Gson gson = new Gson();
        String json="";
       // Log.e("tk",""+tag);
        if(tag.contentEquals("truck")) {
             json = pref.getString("seletecteditem");
            //Log.e("truckjson",""+json.toString());
        }else{
             json = pref.getString("seletecteditemtrailer");
             //Log.e("trjson",""+json.toString());
        }

        if(json!=null && json.length()>0) {
            Type type = new TypeToken<List<Chlitem_model>>() {
            }.getType();
            mvselected = gson.fromJson(json, type);
        }
    }
        @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder {
        CheckBox chselect;
        TextView txt_name;
       CheckBox chunselect;
       ImageView imgupload;
       RelativeLayout rlhead;
       ImageView imgview;
       ImageView imgdelete;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;

        if (row == null) {
            row = inflater.inflate(R.layout.truck_lis, null);
            setAttributes(position, row);
        }else
        {
            row = (View) convertView;
        }




        return row;
    }
    public void setAttributes(final int position, View convertView) {


       final Holder holder=new Holder();

        holder.txt_name = convertView
                .findViewById(R.id.txt_name);
        holder.imgupload=convertView.findViewById(R.id.img_upload);
        holder.chselect=convertView.findViewById(R.id.ch_select);

        holder.chunselect=convertView.findViewById(R.id.ch_unselect);
        holder.rlhead=convertView.findViewById(R.id.badge_layout1);
        holder.imgview=convertView.findViewById(R.id.dashkt);
        holder.imgdelete=convertView.findViewById(R.id.badge1);
       // holder.chselect.setChecked(false);

if(pref.getString(Constant.CHECKLIST_MODE).contentEquals("edit"))
{
if(tag.contentEquals("truck")) {
   // Log.e("truck_item_oldz",""+truck_item_oldz.toString());
    if (truck_item_oldz.size() > 0 && truck_item_oldz != null) {
        for (int i = 0; i < truck_item_oldz.size(); i++) {
            String ab = truck_item_oldz.get(i);
            StringTokenizer st = new StringTokenizer(ab, ">>");
            String url="";
            String id = st.nextToken();
            if (movies.get(position).getId().contentEquals(id)) {


                if(st.hasMoreTokens())
                {
                    url= st.nextToken();
                    movies.get(position).setUrimg(url);
                }

                movies.get(position).setSelectstatus(1);
            }
        }
    }
}else
{
    if(truck_item_oldztrailer.size()>0 && truck_item_oldztrailer !=null)
    {
        for(int i=0;i< truck_item_oldztrailer.size();i++)
        {
            String ab=truck_item_oldztrailer.get(i);
            StringTokenizer st=new StringTokenizer(ab,">>");
            String url="";
            String id=st.nextToken();
            if(movies.get(position).getId().contentEquals(id))
            {
if(st.hasMoreTokens()) {
    url = st.nextToken();
}
                movies.get(position).setUrimg(url);
                movies.get(position).setSelectstatus(1);
            }
        }
    }
}
}
        //Log.e("mvselected",""+mvselected.toString());

        if (mvselected != null && mvselected.size() > 0) {
                for (int k = 0; k < mvselected.size(); k++) {
                    Chlitem_model df = new Chlitem_model();
                    df = mvselected.get(k);
                    if(df.getId().contentEquals(movies.get(position).getId())) {
                        movies.get(position).setSelectstatus(df.getSelectstatus());
                        movies.get(position).setUrimg(df.getUrimg());
                    }

                }
            }
        if(tag.contentEquals("truck")) {




            if (selectidgood != null && selectidgood.size() > 0) {
                for (int k = 0; k < selectidgood.size(); k++) {

                    String idz = selectidgood.get(k);

                    if (idz.contentEquals(movies.get(position).getId())) {
                        movies.get(position).setSelectstatus(2);
                    }

                }
            }
        }else{
            if (selectidgoodtrailer != null && selectidgoodtrailer.size() > 0) {
                for (int k = 0; k < selectidgoodtrailer.size(); k++) {

                    String idz = selectidgoodtrailer.get(k);

                    if (idz.contentEquals(movies.get(position).getId())) {
                        movies.get(position).setSelectstatus(2);
                    }

                }
            }
        }





        Uri uriz=null;
        holder.txt_name.setText(""+movies.get(position).getItem_name());
        if(movies.get(position).getUrimg() !=null && movies.get(position).getUrimg().length()>0)
        {

            if(movies.get(position).getUrimg().contains(".png"))
            {

                Picasso.with(mcontext).load(movies.get(position).getUrimg()).into(holder.imgview);
            }else {

                try {
                    uriz = Uri.parse(movies.get(position).getUrimg());

                } catch (Exception e) {


                }
                Picasso.with(mcontext).load(uriz).into(holder.imgview);
            }
            holder.imgupload.setVisibility(View.GONE);
            holder.rlhead.setVisibility(View.VISIBLE);



//            holder.imgview.setImageURI(uriz);
           //movies.get(position).setSelectstatus(1);
        }else{

            if(movies.get(position)
                    .getSelectstatus()==1)
            {

                holder.imgupload.setVisibility(View.VISIBLE);
            }
            //holder.imgupload.setVisibility(View.VISIBLE);
            holder.rlhead.setVisibility(View.GONE);
            //movies.get(position).setSelectstatus(0);
        }


        holder.chselect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    holder.chselect.setChecked(true);

                    holder.imgupload.setVisibility(View.VISIBLE);
                    holder.chunselect.setChecked(false);
//                    if(tag.contentEquals("truck")) {
                        movies.get(position).setSelectstatus(1);
//                    }else{
//                        movies.get(position).setSelectstatus(2);
//                    }
                    onItemCheckListener.OnItemCheck(movies.get(position),position);

                }else{
                    holder.chselect.setChecked(false);
                    holder.imgupload.setVisibility(View.GONE);
                    movies.get(position).setSelectstatus(0);
                    holder.rlhead.setVisibility(View.GONE);
                    onItemCheckListener.OnItemUnCheck(movies.get(position),position);
                }
            }
        });
       holder.chunselect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked) {
                   holder.imgupload.setVisibility(View.GONE);
                   holder.chunselect.setChecked(true);
                   holder.chselect.setChecked(false);
                   movies.get(position).setSelectstatus(2);
                   onItemCheckListener.OnItemUnCheck(movies.get(position),position);

               }else{
                   movies.get(position).setSelectstatus(0);
                   //holder.imgupload.setVisibility(View.VISIBLE);
                   holder.chunselect.setChecked(false);
                   onItemCheckListener.OnItemUnCheck(movies.get(position),position);

               }
           }
       });


        if(movies.get(position)
                .getSelectstatus()==1)
        {
if(tag.contentEquals("truck")) {
    holder.chselect.setChecked(true);
    if (movies.get(position).getUrimg() != null && movies.get(position).getUrimg().length() > 0) {
        holder.imgupload.setVisibility(View.GONE);
    }else{
        holder.rlhead.setVisibility(View.GONE);
    }
}else{
    holder.chselect.setChecked(true);
    if (movies.get(position).getUrimg() != null && movies.get(position).getUrimg().length() > 0) {
        holder.imgupload.setVisibility(View.GONE);
    }else{
        holder.rlhead.setVisibility(View.GONE);
    }
}




            }else if(movies.get(position)
                .getSelectstatus()==2)
        {
//            if(tag.contentEquals("trailer")) {
//                holder.chunselect.setChecked(true);
//                if (movies.get(position).getUrimg() != null && movies.get(position).getUrimg().length() > 0) {
//                    holder.imgupload.setVisibility(View.GONE);
//                }
//            }
            holder.rlhead.setVisibility(View.GONE);
            holder.imgupload.setVisibility(View.GONE);
            holder.chunselect.setChecked(true);
        }
       holder.imgupload.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onItemCheckListener.OnImageClick(movies.get(position),position);
           }
       });
       holder.imgdelete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(pref.getString(Constant.CHECKLIST_MODE).contentEquals("edit"))
               {
                   movies.get(position).setUrimg(null);
                   holder.imgupload.setVisibility(View.VISIBLE);
                   holder.rlhead.setVisibility(View.GONE);
                  // delimages(id, movies.get(position).getId());
               }else {
                   movies.get(position).setUrimg(null);
                   holder.imgupload.setVisibility(View.VISIBLE);
                   holder.rlhead.setVisibility(View.GONE);
               }
           }
       });
    }



}
