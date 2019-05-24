package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.vfs.VirtualFile;
import download.DownloadManager;
import download.FileTask;
import manager.TinyPNGManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class TinyPNGAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        VirtualFile[] selectedFiles = DataKeys.VIRTUAL_FILE_ARRAY.getData(e.getDataContext());
        if (selectedFiles == null || selectedFiles.length == 0) {
            return;
        }
        ProgressManager.getInstance().run(new Task.Modal(e.getProject(), "TinyPNG", false) {
            public void run(@NotNull ProgressIndicator progressIndicator) {
                File[] files = new File[selectedFiles.length];
                for (int i = 0; i < files.length; i++) {
                    files[i] = new File(selectedFiles[i].getPath());
                }
                TinyPNGManager.compress(files);
            }
        });
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        VirtualFile[] selectedFiles = DataKeys.VIRTUAL_FILE_ARRAY.getData(e.getDataContext());
        if (selectedFiles == null || selectedFiles.length == 0) {
            return;
        }
        for (VirtualFile file : selectedFiles) {
            if (file.isDirectory() || !isImage(file)) {
                e.getPresentation().setEnabledAndVisible(false);
                return;
            }
        }
    }

    private boolean isImage(VirtualFile file) {
        return file != null && file.getExtension() != null && (file.getExtension().equals("png") || file.getExtension().equals("jpeg") || file.getExtension().equals("jpg"));
    }
}
