package manager

import api.NetworkUtils
import api.TinyPNGService
import com.intellij.openapi.vfs.VirtualFile
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import task.TinyPNGTask
import java.io.File

object TinyPNGManager {
    private val url = "https://tinypng.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(NetworkUtils.client)
        .build()

    val service = retrofit.create(TinyPNGService::class.java)



    @JvmStatic fun compress(fileArray: Array<File>) {
        val task = TinyPNGTask(fileArray[0])
        task.run()
    }

    @JvmStatic fun enqueueTask(task: TinyPNGTask) {
        task.run()
    }
}