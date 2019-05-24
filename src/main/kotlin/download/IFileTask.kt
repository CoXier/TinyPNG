package download

interface IFileTask {
    val url: String

    val outputFilePath: String

    fun onPostExecute()
}
