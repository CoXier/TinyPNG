import download.FileTask;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileTaskTest {

    @Test
    public void testTmpFilePath() {
        FileTask fileTask = new FileTask("", "/a/b/c.png");
        String filePath = fileTask.getOutputFilePath();
        assertEquals("/a/b/c_tmp.png", filePath);
    }
}
