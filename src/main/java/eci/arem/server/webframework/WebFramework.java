package eci.arem.server.webframework;

import eci.arem.server.HttpServer;

import eci.arem.server.webframework.utils.JsonUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class WebFramework {

    private static String basePath;

    static final Map<String, WebService> services = new HashMap<>();

    public static void get(String route, WebService ws){
      services.put(route, ws);
    }

    public static HttpResponse invokeService(HttpRequest request, HttpResponse response){
        WebService service = services.get(request.getPath());
        service.invoke(request, response);
        return response;
    }

    public static void staticFiles(String baseFolderPath){
        basePath = baseFolderPath;
    }

    public static HttpResponse invokeStaticFiles(HttpRequest request, HttpResponse response){
        String path = request.getPath().equals("/") ? "/async-client.html" : request.getPath();
        try{
            response.setBody(FileResolver.getFileBytes(basePath,path));
            response.setContentType(FileResolver.getFileType(path));
        } catch (FileNotFoundException e){
            return invokeNotFound(path, response);
        } catch (IOException e) {
            return invokeError(e, response);
        }
        return response;
    }

    public static HttpResponse invokeNotFound(String path, HttpResponse response){
        response.setStatusCode(HttpStatus.NOT_FOUND);
        response.setBody(("404 Not Found: " + JsonUtils.escapeJson(path))
                .getBytes(StandardCharsets.UTF_8));
        return response;
    }

    public static HttpResponse invokeError(Exception error, HttpResponse response){
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setBody(("500 Internal Server Error: " + JsonUtils.escapeJson(error.getMessage()))
                .getBytes(StandardCharsets.UTF_8));
        return response;
    }

    public static HttpResponse invokeBadMethod(HttpRequest request, HttpResponse response){
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        response.setBody(("400 Bad Request: method not allowed: " + request.getMethod()).getBytes(StandardCharsets.UTF_8));
        return response;
    }

    public static boolean isAService(HttpRequest route){
        return services.containsKey(route.getPath());
    }

    public static void start(int port) throws IOException, URISyntaxException {
        HttpServer.main(new String[]{String.valueOf(port)});
    }
}
