package com.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class App {

    public static void main(String[] args) throws Exception {
        int port = 8081;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new RootHandler());
        server.createContext("/health", new HealthHandler());
        server.createContext("/info", new InfoHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server running on port " + port);
    }

    // ================= ROOT UI =================
    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String response =
                    "<html>" +
                    "<head>" +
                    "<title>Football Live</title>" +
                    "<meta name='viewport' content='width=device-width, initial-scale=1'>" +

                    "<style>" +
                    "body { margin:0; font-family:'Segoe UI',sans-serif; background: linear-gradient(135deg,#0f172a,#020617); color:white; }" +
                    ".header { text-align:center; padding:30px; font-size:32px; font-weight:bold; background: linear-gradient(90deg,#22c55e,#16a34a); }" +
                    ".container { max-width:700px; margin:40px auto; padding:10px; }" +
                    ".card { background: rgba(255,255,255,0.05); padding:20px; margin-bottom:20px; border-radius:15px; }" +
                    ".match { font-size:20px; font-weight:bold; margin-bottom:10px; }" +
                    ".badge { padding:5px 12px; border-radius:20px; font-size:13px; font-weight:bold; }" +
                    ".live { background:#22c55e; color:black; }" +
                    ".delay { background:#f59e0b; color:black; }" +
                    ".soon { background:#3b82f6; color:white; }" +
                    "</style>" +

                    "</head>" +
                    "<body>" +

                    "<div class='header'>⚽ Football Live</div>" +

                    "<div class='container'>" +

                    "<div class='card'>" +
                    "<div class='match'>Real Madrid vs Barcelona</div>" +
                    "<span class='badge delay'>Delayed</span>" +
                    "</div>" +

                    "<div class='card'>" +
                    "<div class='match'>Arsenal vs Man City</div>" +
                    "<span class='badge live'>LIVE</span>" +
                    "</div>" +

                    "<div class='card'>" +
                    "<div class='match'>Man United vs Liverpool</div>" +
                    "<span class='badge live'>30 MINUTES TO GO</span>" +
                    "</div>" +

                    "<div class='card'>" +
                    "<div class='match'>Bayern vs Dortmund</div>" +
                    "<span class='badge soon'>Starting Soon</span>" +
                    "</div>" +

                    "</div>" +
                    "</body>" +
                    "</html>";

            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);

            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }

    // ================= HEALTH =================
    static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "{ \"status\": \"UP\" }";

            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);

            exchange.getResponseBody().write(bytes);
            exchange.close();
        }
    }

    // ================= INFO =================
    static class InfoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "{ \"app\": \"football-live\", \"version\": \"1.0\" }";

            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);

            exchange.getResponseBody().write(bytes);
            exchange.close();
        }
    }
}