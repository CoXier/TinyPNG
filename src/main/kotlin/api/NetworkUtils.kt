package api

import okhttp3.OkHttpClient

import java.util.concurrent.TimeUnit

object NetworkUtils {
    var client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
}
