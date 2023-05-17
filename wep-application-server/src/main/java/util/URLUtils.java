package util;

public class URLUtils {

    public static String getURL(String requestLine) {
        String[] info = requestLine.split(" ");
        return info[1];
    }
}
