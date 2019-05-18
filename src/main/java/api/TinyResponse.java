package api;

public class TinyResponse {
    public Input input;
    public Output output;

    public class Input {
        public int size;
        public String type;
    }

    public class Output {
        public int size;
        public String type;
        public int width;
        public int height;
        public double ratio;
        public String url;
    }
}
