package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import download.DownloadManager;
import download.FileTask;
import org.jetbrains.annotations.NotNull;
import upload.UploadManager;

import java.io.File;

public class TinyPNGAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        VirtualFile[] selectedFiles = DataKeys.VIRTUAL_FILE_ARRAY.getData(e.getDataContext());
        if (selectedFiles == null || selectedFiles.length == 0) {
            return;
        }
        UploadManager uploadManager = new UploadManager();
        String url = uploadManager.uploadImage(new File(selectedFiles[0].getPath()));
        DownloadManager.getInstance().downloadFile(new FileTask(url, selectedFiles[0].getPath()));
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
