package util

import java.io.File

object FileUtils {
    fun extractFileName(filePath: String): String {
        var fileName = ""
        val i = filePath.lastIndexOf('.')
        if (i > 0) {
            fileName = filePath.substring(0, i)
        }
        return fileName
    }


    fun extractFileExtension(filePath: String): String {
        var extension = ""
        val i = filePath.lastIndexOf('.')
        if (i > 0) {
            extension = filePath.substring(i)
        }
        return extension
    }
}