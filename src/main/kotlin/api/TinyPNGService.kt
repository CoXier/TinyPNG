package api

import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TinyPNGService {

    @Headers("Content-Type: image/png")
    @POST("web/shrink")
    fun uploadFile(@Body file: RequestBody): Call<TinyResponse>

    @Headers(
        "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36",
        "Content-Type: application/json"
    )
    @POST("web/shrink")
    fun uploadUrl(@Body url: JsonObject): Call<TinyResponse>

    companion object {
        val url = "https://tinypng.com/"
    }
}
