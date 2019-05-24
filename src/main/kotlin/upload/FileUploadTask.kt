package upload

import api.TinyPNGService
import api.TinyResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.io.IOException

class FileUploadTask(var service: TinyPNGService, file: File) : UploadTask<File, TinyResponse>(file) {

    override fun run(): TinyResponse? {
        val requestBody = RequestBody.create(MediaType.parse("image/png"), param)
        try {
            val result = service.uploadFile(requestBody).execute().body()!!
            return result
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

}