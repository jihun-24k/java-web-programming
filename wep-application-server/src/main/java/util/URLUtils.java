package util;

public class URLUtils {

    public static String getURL(String requestLine) {
        if (requestLine == null) {
            return "/";
        }
        String[] info = requestLine.split(" ");
        return info[1];
    }

    public static String getParamQuery(String url) {
        String[] info = url.split("\\?");
        if (info.length == 1) {
            return null;
        }
        return info[1];
    }

    public static String getRequestPath(String url) {
        String[] info = url.split("\\?");
        return info[0];
    }
}
