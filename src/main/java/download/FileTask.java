package download;

import java.io.File;

public class FileTask implements IFileTask {

    public String url;

    public String filePath;

    public FileTask(String url, String filePath) {
        this.url = url;
        this.filePath = filePath;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getOutputFilePath() {
        return getTmpFilePath();
    }

    @Override
    public void onPostExecute() {
        File inputFile = new File(filePath);
        File outputFile = new File(getOutputFilePath());
        inputFile.deleteOnExit();
        outputFile.renameTo(inputFile);
    }

    private String getTmpFilePath() {
        String extension = "";
        String fileName = "";
        int i = filePath.lastIndexOf('.');
        if (i > 0) {
            extension = filePath.substring(i);
            fileName = filePath.substring(0, i);
        }
        return fileName + "_tmp" + extension;
    }
}
