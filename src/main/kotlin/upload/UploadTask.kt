package upload

import api.TinyResponse

abstract class UploadTask<T, R>(t: T) {
    var param = t

    abstract fun run(): R?
}