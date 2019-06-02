package manager

import api.NetworkUtils
import api.TinyPNGService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import task.TinyPNGTask
import util.ILogger
import java.io.File
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock


object TinyPNGManager {
    private const val url = "https://tinypng.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(NetworkUtils.client)
        .build()

    val service: TinyPNGService = retrofit.create(TinyPNGService::class.java)

    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()

    private val CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4))

    private val MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1

    private const val KEEP_ALIVE_SECONDS = 30L

    private val threadFactory: ThreadFactory = ThreadFactory { r -> Thread(r) }

    private val poolWorkQueue = LinkedBlockingQueue<Runnable>(128)

    private val threadPoolExecutor = ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS,
        TimeUnit.SECONDS, poolWorkQueue, threadFactory)

    private val lock: ReentrantLock = ReentrantLock(false)

    private val notFinish: Condition = lock.newCondition()

    private val count: AtomicInteger = AtomicInteger(0)

    @JvmStatic fun compress(tinyPNGArray: Array<TinyPNGTask?>, logger: ILogger) {
        tinyPNGArray.forEach {
            it!!.logger = logger
            it!!.callback = object : TinyPNGTask.Callback {
                override fun finish() {
                    count.incrementAndGet()
                    if (count.get() == tinyPNGArray.size) {
                        lock.lock()
                        notFinish.signal()
                        lock.unlock()
                    }
                }
            }
            threadPoolExecutor.execute(it)
        }
        lock.lock()
        while (count.get() != tinyPNGArray.size) {
            notFinish.await()
        }
        lock.unlock()
    }

    @JvmStatic fun enqueueTask(task: TinyPNGTask) {
        threadPoolExecutor.execute(task)
    }

}