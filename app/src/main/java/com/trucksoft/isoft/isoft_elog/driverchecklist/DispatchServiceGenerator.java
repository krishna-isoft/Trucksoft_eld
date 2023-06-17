package com.trucksoft.isoft.isoft_elog.driverchecklist;

import android.content.Context;

import com.isoft.trucksoft_elog.Multiused.Preference;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sab99r
 */
public class DispatchServiceGenerator {
    static Preference pref;

    public static <S> S createService(Class<S> serviceClass,  Context contexts) {

        OkHttpClient httpClient=new OkHttpClient.Builder()
                .connectTimeout(35, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        pref=Preference.getInstance(contexts);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://eld.e-logbook.info/elog_app/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient).build();

        return retrofit.create(serviceClass);

    }

}
