package task

import api.TinyResponse
import download.DownloadManager
import download.FileTask
import upload.FileUploadTask
import manager.TinyPNGManager
import upload.UrlUploadTask
import java.io.File

class TinyPNGTask(var inputFile: File) : Runnable {

    var compressCount = 0
    var startTime = 0L
    var inputFileSize = 0

    override fun run() {
        when {
            resultUrl != null -> {
                println("[Download][${inputFile.name}] start download file")
                DownloadManager.getInstance().downloadFile(FileTask(resultUrl, inputFile.path))
                println("[Download][${inputFile.name}] compress file in ${(System.currentTimeMillis() - startTime) / 1000}s")
                println("[Result] input: ${inputFileSize}B output: ${preTinyResponse!!.output.size}B")
            }
            preTinyResponse == null -> {
                startTime = System.currentTimeMillis()
                println("[FileUpload][${inputFile.name}] start upload file in compress[$compressCount]")
                preTinyResponse = FileUploadTask(TinyPNGManager.service, inputFile).run()
                inputFileSize = preTinyResponse!!.input.size
                println("[FileUpload][${inputFile.name}] finish upload file in compress[${compressCount++}]")
                if (preTinyResponse != null && 1 - preTinyResponse!!.output.ratio <= 0.005) {
                    resultUrl = preTinyResponse!!.output.url
                }
                TinyPNGManager.enqueueTask(this)
            }
            preTinyResponse != null -> {
                println("[UrlUpload][${inputFile.name}] start upload url in compress[$compressCount]")
                print(preTinyResponse.hashCode())
                preTinyResponse = UrlUploadTask(TinyPNGManager.service, preTinyResponse!!.output.url).run() ?: preTinyResponse
                print(preTinyResponse.hashCode())
                println("[UrlUpload][${inputFile.name}] finish upload url in compress[${compressCount++}]")
                if (preTinyResponse != null && 1 - preTinyResponse!!.output.ratio <= 0.005) {
                    resultUrl = preTinyResponse!!.output.url
                }
                TinyPNGManager.enqueueTask(this)
            }
        }
    }

    var preTinyResponse: TinyResponse? = null
    var resultUrl: String? = null
}