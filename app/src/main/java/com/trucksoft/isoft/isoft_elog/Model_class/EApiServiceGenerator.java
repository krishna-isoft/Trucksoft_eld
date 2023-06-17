package com.trucksoft.isoft.isoft_elog.Model_class;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sab99r
 */
public class EApiServiceGenerator {

    public static <S> S createService(Class<S> serviceClass, Context contexts) {

        OkHttpClient httpClient=new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eld.e-logbook.info/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient).build();
        return retrofit.create(serviceClass);

    }

}
