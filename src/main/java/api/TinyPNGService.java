package api;

import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface TinyPNGService {
    public static String url = "https://tinypng.com/";

    @Headers("Content-Type: image/png")
    @POST("web/shrink")
    Call<TinyResponse> uploadFile(@Body RequestBody file);

    @Headers({
            "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36",
            "Content-Type: application/json"
            })
    @POST("web/shrink")
    Call<TinyResponse> uploadUrl(@Body JsonObject url);
}
