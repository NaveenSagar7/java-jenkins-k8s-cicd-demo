package com.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

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

            "body {" +
            " margin:0; font-family:'Segoe UI',sans-serif;" +
            " background: linear-gradient(135deg,#0f172a,#020617);" +
            " color:white; overflow-x:hidden; }" +

            /* HEADER */
            ".header {" +
            " text-align:center; padding:30px;" +
            " font-size:32px; font-weight:bold;" +
            " background: linear-gradient(90deg,#22c55e,#16a34a);" +
            " letter-spacing:1px;" +
            "}" +

            /* SUBTEXT */
            ".subtext {" +
            " text-align:center; margin-top:10px;" +
            " color:#94a3b8; font-size:15px;" +
            "}" +

            /* CONTAINER */
            ".container {" +
            " max-width:700px; margin:40px auto; padding:10px;" +
            "}" +

            /* CARD */
            ".card {" +
            " background: rgba(255,255,255,0.05);" +
            " backdrop-filter: blur(10px);" +
            " padding:20px;" +
            " margin-bottom:20px;" +
            " border-radius:15px;" +
            " box-shadow:0 8px 30px rgba(0,0,0,0.6);" +
            " transition:0.3s;" +
            " border:1px solid rgba(255,255,255,0.08);" +
            "}" +

            ".card:hover {" +
            " transform: translateY(-5px) scale(1.02);" +
            " box-shadow:0 12px 40px rgba(34,197,94,0.4);" +
            "}" +

            /* MATCH TITLE */
            ".match {" +
            " font-size:20px; font-weight:bold;" +
            " margin-bottom:10px;" +
            "}" +

            /* STATUS BADGES */
            ".badge {" +
            " display:inline-block;" +
            " padding:5px 12px;" +
            " border-radius:20px;" +
            " font-size:13px;" +
            " font-weight:bold;" +
            "}" +

            ".live { background:#22c55e; color:black; }" +
            ".delay { background:#f59e0b; color:black; }" +
            ".soon { background:#3b82f6; color:white; }" +

            /* FOOTER */
            ".footer {" +
            " text-align:center; margin-top:40px;" +
            " color:#64748b; font-size:13px;" +
            "}" +

            /* ANIMATION BACKGROUND GLOW */
            ".glow {" +
            " position:fixed; width:500px; height:500px;" +
            " background:radial-gradient(circle,#22c55e33,transparent);" +
            " top:-100px; left:-100px; filter:blur(80px);" +
            "}" +

            "</style>" +

            "</head>" +

            "<body>" +

            "<div class='glow'></div>" +

            "<div class='header'>⚽ Football Live</div>" +

            "<div class='subtext'>Real-time match insights • Never miss a moment</div>" +

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

            "<div class='footer'>Built with ❤️ DevOps + Java • CI/CD Powered</div>" +

            "</body>" +
            "</html>";

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // ================= HEALTH =================
    static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "{ \"status\": \"UP\" }";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        }
    }

    // ================= INFO =================
    static class InfoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "{ \"app\": \"football-live\", \"version\": \"1.0\" }";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        }
    }
}
