package util;

public class URLUtils {

    public static String getFilePath(String requestLine) {
        String[] info = requestLine.split(" ");
        return info[1];
    }
}
