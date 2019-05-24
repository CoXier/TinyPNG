package action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataKeys
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.vfs.VirtualFile
import download.DownloadManager
import download.FileTask
import manager.TinyPNGManager

import java.io.File

class TinyPNGAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val selectedFiles = DataKeys.VIRTUAL_FILE_ARRAY.getData(e.dataContext)
        if (selectedFiles == null || selectedFiles.isEmpty()) {
            return
        }
        ProgressManager.getInstance().run(object : Task.Modal(e.project, "TinyPNG", false) {
            override fun run(progressIndicator: ProgressIndicator) {
                val files = arrayOfNulls<File>(selectedFiles.size)
                for (i in files.indices) {
                    files[i] = File(selectedFiles[i].path)
                }
                TinyPNGManager.compress(files)
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
