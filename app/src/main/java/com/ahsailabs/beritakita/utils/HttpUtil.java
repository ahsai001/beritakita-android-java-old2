package com.ahsailabs.beritakita.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.ahsailabs.beritakita.configs.Config;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ahmad s on 2019-09-26.
 */
public class HttpUtil {
    private static final int DATA_DEFAULT_CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s
    private static final int DATA_DEFAULT_READ_TIMEOUT_MILLIS = 30 * 1000; // 30s
    private static final int DATA_DEFAULT_WRITE_TIMEOUT_MILLIS = 30 * 1000; // 30s

    public static OkHttpClient getCLient(final Context context){

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();

                String apiKey = "APIKEY="+ Config.API_KEY;
                String encodedApiKey = Base64.encodeToString(apiKey.getBytes(), Base64.NO_WRAP);

                builder.addHeader("Authorization", encodedApiKey);
                builder.addHeader("x-packagename", context.getPackageName());
                builder.addHeader("x-platform","android");

                SharedPreferences sharedPreferences = context.getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE);
                String token = sharedPreferences.getString(Config.DATA_TOKEN,"");
                builder.addHeader("x-token", token);

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DATA_DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .readTimeout(DATA_DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .writeTimeout(DATA_DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .build();

        return client;
    }

}
