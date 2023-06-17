package com.trucksoft.isoft.isoft_elog.Model_class;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sab99r
 */
public class FmcsaServiceGenerator {

    public static <S> S createService(Class<S> serviceClass) {

        OkHttpClient httpClient=new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eld.e-logbook.info/eldtest/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient).build();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.225.54/elog_app/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient).build();
        return retrofit.create(serviceClass);

    }

}
