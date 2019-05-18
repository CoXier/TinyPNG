package download;

public interface IFileTask {
    String getUrl();

    String getOutputFilePath();

    void onPostExecute();
}
