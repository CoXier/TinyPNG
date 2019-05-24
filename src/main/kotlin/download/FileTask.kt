package download

import java.io.File

class FileTask(override var url: String, var filePath: String) : IFileTask {

    override val outputFilePath: String
        get() = tmpFilePath

    private val tmpFilePath: String
        get() {
            var extension = ""
            var fileName = ""
            val i = filePath.lastIndexOf('.')
            if (i > 0) {
                extension = filePath.substring(i)
                fileName = filePath.substring(0, i)
            }
            return fileName + "_tmp" + extension
        }

    override fun onPostExecute() {
        val inputFile = File(filePath)
        val outputFile = File(outputFilePath)
        inputFile.deleteOnExit()
        outputFile.renameTo(inputFile)
    }
}
