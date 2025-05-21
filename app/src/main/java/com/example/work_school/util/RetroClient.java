package com.example.work_school.util;

import java.lang.reflect.GenericArrayType;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    private static Retrofit retrofit = null;

    private static String baseUrl = "https://expense-tracker-db-kbxp.onrender.com/";

    private static String dbName = "c277ea09-d909-4b51-879e-190af4a0d055";

    public static void setBaseUrl(String url) {
        baseUrl = url;

        retrofit = null;
    }

    public static void SetDbName(String name) {
        dbName = name;
        retrofit = null;
    }

    public static Retrofit getClient(){
        if(retrofit == null){

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            Interceptor headerInterceptor = chain -> {
                Request originalRequest = chain.request();
                Request newRequest = originalRequest.newBuilder()
                        .header("X-DB-NAME",dbName)
                        .build();
                return chain.proceed(newRequest);
            };
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(headerInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15,TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
