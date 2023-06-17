package com.trucksoft.isoft.isoft_elog.breakrep;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.isoft.trucksoft_elog.driverchecklist.DispatchServiceGenerator;
import com.isoft.trucksoft_elog.driverchecklist.Dispatchline_decoder;
import com.isoft.trucksoft_elog.isoft_api.Eld_api;
import com.trucksoft.isoft.isoft_elog.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Break_report extends AppCompatActivity {
    Break_receyadap adapter;
    RecyclerView recyclerView;
    List<Breakrep_model> movies;
    Context contexts;
    Preference pref;
    ProgressDialog dialog;
    Eld_api api;

    private ImageView imgback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breakrep_home);
        contexts=this;
        movies=new ArrayList<>();
        pref=Preference.getInstance(contexts);
        pref.putString(Constant.LAST_INDEX,"0");

        imgback=findViewById(R.id.driver_list_iv_back);
        recyclerView =findViewById(R.id.driver_list_listView);

        adapter = new Break_receyadap(contexts, movies);


        adapter.setLoadMoreListener(new Break_receyadap.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = movies.size() - 1;
                        loadMore(index);
                    }
                });
            }
        });
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(contexts));
        recyclerView.addItemDecoration(new Dispatchline_decoder(2));
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        api = DispatchServiceGenerator.createService(Eld_api.class, contexts);

        load(0);


        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//                    Intent ink = new Intent(contexts, Home_activity.class);
//                    startActivity(ink);
//                    finish();
//                }else{
                    finish();
//                }
            }
        });




    }
    private void load(int index){

        if(dialog !=null && dialog.isShowing()) {

        }else {
            dialog = new ProgressDialog(contexts,
                    AlertDialog.THEME_HOLO_LIGHT);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }
        String page="0";


        Call<List<Breakrep_model>> call = api.getBreakreport(pref.getString(Constant.DRIVER_ID),""+page,pref.getString(Constant.COMPANY_CODE));

        call.enqueue(new Callback<List<Breakrep_model>>() {
            @Override
            public void onResponse(Call<List<Breakrep_model>> call, Response<List<Breakrep_model>> response) {
                Log.e(" Responsev"," "+response.toString());
                Log.e(" Responsesskk"," "+String.valueOf(response.code()));
                if(response.isSuccessful()){
                    if(dialog !=null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    //  Log.e(" Responsecqevv","z "+response.body());
                    if(response.body() !=null) {
                        movies.addAll(response.body());
                    }else{

                    }

                    //  adapter = new Currentreportadap(context, movies);

                    adapter.notifyDataChanged();
                }else{
                    dialog.dismiss();
                    // load(0);
                    // Log.e("ggg"," Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Breakrep_model>> call, Throwable t) {
                // Log.e("tttt"," Response Error "+t.getMessage());
                dialog.dismiss();
            }
        });
    }

    private void loadMore(int index){
//Log.e("indexc","loadmpre"+index);
        //add loading progress view
        movies.add(new Breakrep_model("load"));
        adapter.notifyItemInserted(movies.size()-1);

        String page=pref.getString(Constant.LAST_INDEX);
        int ab= Integer.parseInt(pref.getString(Constant.LAST_INDEX));
        ab++;
        pref.putString(Constant.LAST_INDEX,""+ab);
        Call<List<Breakrep_model>> call = api.getBreakreport(pref.getString(Constant.DRIVER_ID),""+ab,pref.getString(Constant.COMPANY_CODE));

        call.enqueue(new Callback<List<Breakrep_model>>() {
            @Override
            public void onResponse(Call<List<Breakrep_model>> call, Response<List<Breakrep_model>> response) {
                // Log.e(" Responsekk","z "+response.toString());
                //Log.e(" Responsesskk"," z"+String.valueOf(response.code()));

                if(response.isSuccessful()){
                    //  Log.e(" Responsec","z "+response.body());
                    //remove loading view
                    movies.remove(movies.size()-1);

                    List<Breakrep_model> result = response.body();
                    if(result.size()>0){
                        //add loaded data
                        movies.addAll(result);
                    }else{//result size 0 means there is no more data available at server
                        adapter.setMoreDataAvailable(false);
                        //telling adapter to stop calling load more as no more server data available
                        Toast.makeText(contexts,"No More Data Available",Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataChanged();
                    //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                }else{
                    //  Log.e(TAG," Load More Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Breakrep_model>> call, Throwable t) {
                //  Log.e(TAG," Load More Response Error "+t.getMessage());
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(pref.getString(Constant.NETWORK_TYPE).contentEquals(""+Constant.CELLULAR)) {
//            Intent ink = new Intent(contexts, Home_activity.class);
//            startActivity(ink);
//            finish();
//        }else{
            finish();
//        }
    }
}
