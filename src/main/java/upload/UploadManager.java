package upload;

import api.NetworkUtils;
import api.TinyPNGService;
import api.TinyResponse;
import com.google.gson.JsonObject;
import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;

public class UploadManager {

    private static String url = "https://tinypng.com/";

    private TinyPNGService service;

    public UploadManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(NetworkUtils.client)
                .build();
        service = retrofit.create(TinyPNGService.class);
    }

    public String uploadImage(File sourceFile) {
        if (sourceFile == null || !sourceFile.exists()) return null;
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), sourceFile);
        try {
            TinyResponse tinyResponse = service.uploadFile(requestBody).execute().body();
            // loop until compress ratio is close to 0
            double ratio = tinyResponse.output.ratio;
            String url = tinyResponse.output.url;
            while (1 - ratio > 0.005) {
                TinyResponse response = uploadUrl(url);
                System.out.println("[Compress] input: " + response.input.size + " output: " + response.output.size + " compress ratio:" + (1 - response.output.ratio));
                ratio = response.output.ratio;
                url = response.output.url;
            }
            System.out.println("[Result] " + url);
            return url;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private TinyResponse uploadUrl(String url) {
        JsonObject paramObject = new JsonObject();
        JsonObject urlObject = new JsonObject();
        urlObject.addProperty("url", url);
        paramObject.add("source", urlObject);
        try {
            return service.uploadUrl(paramObject).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
