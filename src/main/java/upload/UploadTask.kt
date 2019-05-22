package upload

import api.TinyResponse

abstract class UploadTask<T>(t: T) : Runnable {
    var param = t

    override fun run() {
        run(param)
    }

    abstract fun run(t: T): TinyResponse
}