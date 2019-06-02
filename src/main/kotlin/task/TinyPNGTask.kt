package task

import api.TinyResponse
import download.DownloadManager
import download.FileTask
import upload.FileUploadTask
import manager.TinyPNGManager
import upload.UrlUploadTask
import util.ILogger
import java.io.File

class TinyPNGTask(var inputFile: File, var outputFile: File, var taskName: String) : Runnable {

    var compressCount = 0
    var startTime = 0L
    var inputFileSize = 0

    override fun run() {
        when {
            resultUrl != null -> {
                print("[Download][$taskName] start download file")
                DownloadManager.instance.downloadFile(FileTask(resultUrl!!, outputFile.path))
                print("[Download][$taskName] finish download")
                print("[Result][$taskName] Compress $compressCount times in ${(System.currentTimeMillis() - startTime) / 1000}s. Input: ${inputFileSize}B output: ${preTinyResponse!!.output.size}B.")
                callback?.finish()
            }
            preTinyResponse == null -> {
                startTime = System.currentTimeMillis()
                print("[FileUpload][$taskName] start upload file in compress[$compressCount]")
                preTinyResponse = FileUploadTask(TinyPNGManager.service, inputFile).run()
                inputFileSize = preTinyResponse!!.input.size
                print("[FileUpload][$taskName] finish upload file in compress[${compressCount++}]")
                if (preTinyResponse != null && 1 - preTinyResponse!!.output.ratio <= 0.005) {
                    resultUrl = preTinyResponse!!.output.url
                }
                TinyPNGManager.enqueueTask(this)
            }
            preTinyResponse != null -> {
                print("[UrlUpload][$taskName] start upload url in compress[$compressCount]")
                val tinyResponse = UrlUploadTask(TinyPNGManager.service, preTinyResponse!!.output.url).run() ?: preTinyResponse
                print("[UrlUpload][$taskName] finish upload url in compress[${compressCount++}]")
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
    var callback: Callback? = null
    var logger: ILogger? = null

    private fun print(msg: String) {
        logger?.print(msg)
    }

    interface Callback {
        fun finish()
    }
}