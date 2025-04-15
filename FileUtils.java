import java.io.*;

public class FileUtils {
    public static byte[] readFile(File file) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            return is.readAllBytes();
        }
    }

    public static void writeFile(File file, byte[] data) throws IOException {
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(data);
        }
    }
}
