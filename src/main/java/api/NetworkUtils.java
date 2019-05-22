package api;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class NetworkUtils {
    public static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
}
