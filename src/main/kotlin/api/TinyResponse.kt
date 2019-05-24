package api

class TinyResponse {
    lateinit var input: Input
    lateinit var output: Output

    inner class Input {
        var size: Int = 0
        lateinit var type: String
    }

    inner class Output {
        var size: Int = 0
        lateinit var type: String
        var width: Int = 0
        var height: Int = 0
        var ratio: Double = 0.toDouble()
        lateinit var url: String
    }

    override fun equals(other: Any?): Boolean {
        return other is TinyResponse
                && this.input.size == other.input.size
                && this.output.size == other.output.size
    }
}