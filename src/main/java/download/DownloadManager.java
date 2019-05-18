package download;

import api.NetworkUtils;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

import java.io.File;
import java.io.IOException;

public class DownloadManager {
    private static DownloadManager downloadManager;

    private DownloadManager() {
    }

    public static DownloadManager getInstance() {
        if (downloadManager == null) {
            synchronized (DownloadManager.class) {
                if (downloadManager == null) {
                    downloadManager = new DownloadManager();
                }
            }
        }
        return downloadManager;
    }

    public void downloadFile(IFileTask fileTask) {
        File outputFile = new File(fileTask.getOutputFilePath());
        try {
            Request request = new Request.Builder().url(fileTask.getUrl()).build();
            Response response = NetworkUtils.client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            BufferedSink sink = Okio.buffer(Okio.sink(outputFile));
            sink.writeAll(responseBody.source());
            sink.close();
            fileTask.onPostExecute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
