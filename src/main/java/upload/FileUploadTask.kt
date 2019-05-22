package upload

import api.TinyResponse
import java.io.File

class FileUploadTask(file: File) : UploadTask<File>(file) {

    override fun run(t: File): TinyResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}