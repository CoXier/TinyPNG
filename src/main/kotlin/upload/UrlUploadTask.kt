package upload

import api.TinyPNGService
import api.TinyResponse
import com.google.gson.JsonObject
import java.io.IOException

class UrlUploadTask(var service: TinyPNGService, var url: String): UploadTask<String, TinyResponse>(url) {

    override fun run(): TinyResponse? {
        val paramObject = JsonObject()
        val urlObject = JsonObject()
        urlObject.addProperty("url", url)
        paramObject.add("source", urlObject)
        try {
            return service.uploadUrl(paramObject).execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}