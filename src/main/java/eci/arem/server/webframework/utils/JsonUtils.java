package eci.arem.server.webframework.utils;

public class JsonUtils {

    private JsonUtils() {}

    public static String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
