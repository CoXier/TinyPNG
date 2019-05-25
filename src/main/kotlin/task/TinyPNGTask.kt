package task

import api.TinyResponse
import download.DownloadManager
import download.FileTask
import upload.FileUploadTask
import manager.TinyPNGManager
import upload.UrlUploadTask
import java.io.File

class TinyPNGTask(var inputFile: File, var outputFile: File, var taskName: String) : Runnable {

    var compressCount = 0
    var startTime = 0L
    var inputFileSize = 0

    override fun run() {
        when {
            resultUrl != null -> {
                println("[Download][$taskName] start download file")
                DownloadManager.instance.downloadFile(FileTask(resultUrl!!, outputFile.path))
                println("[Download][$taskName] finish download")
                println("[Result][$taskName] Compress $compressCount times in ${(System.currentTimeMillis() - startTime) / 1000}s. Input: ${inputFileSize}B output: ${preTinyResponse!!.output.size}B.")
            }
            preTinyResponse == null -> {
                startTime = System.currentTimeMillis()
                println("[FileUpload][$taskName] start upload file in compress[$compressCount]")
                preTinyResponse = FileUploadTask(TinyPNGManager.service, inputFile).run()
                inputFileSize = preTinyResponse!!.input.size
                println("[FileUpload][$taskName] finish upload file in compress[${compressCount++}]")
                if (preTinyResponse != null && 1 - preTinyResponse!!.output.ratio <= 0.005) {
                    resultUrl = preTinyResponse!!.output.url
                }
                TinyPNGManager.enqueueTask(this)
            }
            preTinyResponse != null -> {
                println("[UrlUpload][$taskName] start upload url in compress[$compressCount]")
                val tinyResponse = UrlUploadTask(TinyPNGManager.service, preTinyResponse!!.output.url).run() ?: preTinyResponse
                println("[UrlUpload][$taskName] finish upload url in compress[${compressCount++}]")
                preTinyResponse = tinyResponse
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