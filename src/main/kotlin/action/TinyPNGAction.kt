package action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataKeys
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.vfs.VirtualFile
import manager.TinyPNGManager
import task.TinyPNGTask
import util.FileUtils
import util.ILogger

import java.io.File

class TinyPNGAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val selectedFiles = DataKeys.VIRTUAL_FILE_ARRAY.getData(e.dataContext)
        if (selectedFiles == null || selectedFiles.isEmpty()) {
            return
        }
        ProgressManager.getInstance().run(object : Task.Backgroundable(e.project, "TinyPNG", false) {
            override fun run(progressIndicator: ProgressIndicator) {
                progressIndicator.text = "TinyPNG"
                val tinyPNGArray = arrayOfNulls<TinyPNGTask>(selectedFiles.size)
                for (i in tinyPNGArray.indices) {
                    val sourceFile = File(selectedFiles[i].path)
                    val fileName = sourceFile.nameWithoutExtension +
                            System.currentTimeMillis() + FileUtils.extractFileExtension(selectedFiles[i].path)
                    val tmpFile = File("${e.project?.basePath}/build/tiny/$fileName")
                    tmpFile.mkdir()
                    sourceFile.copyTo(tmpFile, true)
                    tinyPNGArray[i] = TinyPNGTask(tmpFile, sourceFile, sourceFile.name)
                }
                TinyPNGManager.compress(tinyPNGArray, object: ILogger {
                    override fun print(msg: String) {
                        progressIndicator.text2 = msg
                    }

                })
            }
        })
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        val selectedFiles = DataKeys.VIRTUAL_FILE_ARRAY.getData(e.dataContext)
        if (selectedFiles == null || selectedFiles.isEmpty()) {
            return
        }
        for (file in selectedFiles) {
            if (file.isDirectory || !isImage(file)) {
                e.presentation.isEnabledAndVisible = false
                return
            }
        }
    }

    private fun isImage(file: VirtualFile?): Boolean {
        return file != null && file.extension != null && (file.extension == "png" || file.extension == "jpeg" || file.extension == "jpg")
    }
}
