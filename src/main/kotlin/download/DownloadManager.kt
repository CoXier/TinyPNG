package download

import api.NetworkUtils
import okhttp3.Request
import okio.Okio

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
