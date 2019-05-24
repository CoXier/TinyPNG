package download

import api.NetworkUtils
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.BufferedSink
import okio.Okio
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

import java.io.File
import java.io.IOException

class DownloadManager private constructor() {

    fun downloadFile(fileTask: IFileTask) {
        val outputFile = File(fileTask.outputFilePath)
        try {
            val request = Request.Builder().url(fileTask.url).build()
            val response = NetworkUtils.client.newCall(request).execute()
            val responseBody = response.body()
            val sink = Okio.buffer(Okio.sink(outputFile))
            sink.writeAll(responseBody!!.source())
            sink.close()
            fileTask.onPostExecute()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    companion object {
        private var downloadManager: DownloadManager? = null

        val instance: DownloadManager
            get() {
                if (downloadManager == null) {
                    synchronized(DownloadManager::class.java) {
                        if (downloadManager == null) {
                            downloadManager = DownloadManager()
                        }
                    }
                }
                return downloadManager!!
            }
    }

}
