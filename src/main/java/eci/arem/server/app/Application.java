package eci.arem.server.app;

import static eci.arem.server.webframework.WebFramework.*;
import static java.lang.IO.println;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;


import eci.arem.server.webframework.HttpRequest;
import eci.arem.server.webframework.HttpResponse;
import eci.arem.server.webframework.HttpStatus;

public class Application {

    public static void main(String[] args) throws Exception {

        //To run locally (IDE) uncomment this line
        //staticFiles("src/main/resources/public");
        staticFiles("public"); //Production





        get("/hello",
                (req, resp) ->{
                    String name = req.getValue("name");
                    if (name == null || name.isBlank()) {
                        resp.setStatusCode(HttpStatus.BAD_REQUEST);
                        resp.setBody(("Bad request: missing 'name' parameter").getBytes(StandardCharsets.UTF_8));
                    } else {
                        resp.setBody(("Hello " + name).getBytes(StandardCharsets.UTF_8));
                    }
                }

        );

        get("/square",
                (req, resp) ->{
                    String rawValue = req.getValue("value");
                    String strBody = "";
                    try {
                        double value = Double.parseDouble(rawValue);
                        double square = value * value;
                        resp.setContentType("application/json");
                        strBody = "{\"value\":" + value + ",\"square\":" + square + "}";
                    } catch (NumberFormatException | NullPointerException e) {
                        resp.setStatusCode(HttpStatus.BAD_REQUEST);
                        strBody = "Bad request: missing or invalid 'value' parameter";
                    }
                    resp.setBody(strBody.getBytes(StandardCharsets.UTF_8));
                }

        );
        get("/health", (req, resp) ->{
            resp.setContentType("application/json");
            resp.setBody("{\"status\":\"OK\"}".getBytes(StandardCharsets.UTF_8));
        });

        get("/time", (req, resp) ->{
           resp.setContentType("application/json");
           resp.setBody(("{\"serverTime\":\"" + Instant.now() + "\"}").getBytes(StandardCharsets.UTF_8));
        });



        get("/pi", (req, resp) ->
                resp.setBody((String.valueOf(Math.PI)).getBytes(StandardCharsets.UTF_8))
                );

        get("/e", (req, resp) ->
                resp.setBody((String.valueOf(Math.E)).getBytes(StandardCharsets.UTF_8))
        );

        String portEnv = System.getenv("PORT");
        int port = portEnv != null ? Integer.parseInt(portEnv) : 8080;

        start(port);
    }
}

