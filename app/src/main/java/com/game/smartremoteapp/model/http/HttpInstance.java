package com.game.smartremoteapp.model.http;

import com.game.smartremoteapp.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by zhouh on 2017/8/15.
 */
public class HttpInstance {
    private static HttpInstance instance;
    private OkHttpClient client;

    public static synchronized HttpInstance getInstance() {
        if (instance == null) {
            instance = new HttpInstance();
        }
        return instance;
    }

    private HttpInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.loge(message,"http===");
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60,TimeUnit.SECONDS)
                .build();
    }

    public OkHttpClient getClient() {
        if (client != null) {
            return client;
        }
        return null;
    }

}
