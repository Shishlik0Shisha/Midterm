package lab;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        String envPort = System.getenv("PORT");
        if (envPort != null && !envPort.isBlank()) {
            port = Integer.parseInt(envPort);
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", exchange -> {
            sendResponse(exchange, "MathUtils service is running");
        });

        server.createContext("/health", exchange -> {
            sendResponse(exchange, "OK");
        });

        server.createContext("/add", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            int a = 0;
            int b = 0;

            if (query != null) {
                String[] params = query.split("&");
                for (String param : params) {
                    String[] pair = param.split("=");
                    if (pair.length == 2) {
                        if (pair[0].equals("a")) a = Integer.parseInt(pair[1]);
                        if (pair[0].equals("b")) b = Integer.parseInt(pair[1]);
                    }
                }
            }

            MathUtils mathUtils = new MathUtils();
            int result = mathUtils.add(a, b);
            sendResponse(exchange, String.valueOf(result));
        });

        server.setExecutor(null);
        server.start();

        System.out.println("MathUtils service started on port " + port);
    }

    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}