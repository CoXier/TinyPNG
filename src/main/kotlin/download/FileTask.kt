package download

import util.FileUtils
import java.io.File

class FileTask(override var url: String, var filePath: String) : IFileTask {

    override val outputFilePath: String
        get() = tmpFilePath

    private val tmpFilePath: String
        get() {
            return "${FileUtils.extractFileName(filePath)}_tmp${FileUtils.extractFileExtension(filePath)}"
        }

    override fun onPostExecute() {
        val inputFile = File(filePath)
        val outputFile = File(outputFilePath)
        inputFile.deleteOnExit()
        outputFile.renameTo(inputFile)
    }
}
