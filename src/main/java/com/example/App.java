package com.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    // Sonar fixes (constants)
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_HTML = "text/html; charset=UTF-8";
    private static final String CONTENT_TYPE_JSON = "application/json";

    public static void main(String[] args) throws Exception {
        int port = 8081;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new RootHandler());
        server.createContext("/health", new HealthHandler());
        server.createContext("/info", new InfoHandler());

        server.setExecutor(null);
        server.start();

        LOGGER.info(() -> String.format("Server running on port %d", port));
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
                    "body { margin:0; font-family:'Segoe UI',sans-serif; background:#020617; color:white; }" +
                    ".header { text-align:center; padding:25px; font-size:30px; font-weight:bold;" +
                    " background:linear-gradient(90deg,#22c55e,#4ade80); }" +
                    ".container { max-width:800px; margin:30px auto; padding:10px; }" +

                    ".card {" +
                    " background:rgba(255,255,255,0.05);" +
                    " border-radius:16px;" +
                    " padding:20px;" +
                    " margin-bottom:20px;" +
                    " backdrop-filter:blur(12px);" +
                    " box-shadow:0 8px 25px rgba(0,0,0,0.6);" +
                    " transition:0.3s; }" +

                    ".card:hover { transform:scale(1.02); box-shadow:0 10px 40px rgba(34,197,94,0.4); }" +

                    ".teams { font-size:20px; font-weight:bold; margin-bottom:10px; }" +
                    ".score { font-size:28px; font-weight:bold; margin:10px 0; }" +
                    ".status { font-size:13px; padding:6px 14px; border-radius:20px; display:inline-block; }" +

                    ".live { background:#22c55e; color:black; animation:pulse 1s infinite; }" +
                    ".delay { background:#f59e0b; color:black; }" +
                    ".soon { background:#3b82f6; }" +

                    "@keyframes pulse { 0%{transform:scale(1);}50%{transform:scale(1.1);}100%{transform:scale(1);} }" +

                    ".time { font-size:12px; color:#94a3b8; margin-top:5px; }" +
                    ".footer { text-align:center; margin-top:40px; color:#64748b; font-size:12px; }" +

                    "</style>" +

                    "</head>" +
                    "<body>" +

                    "<div class='header'>⚽ Football Live</div>" +
                    "<div class='container' id='matches'></div>" +
                    "<div class='footer'>Live Updates • DevOps Powered ⚡</div>" +

                    "<script>" +
                    "let matches = [" +
                    "{teams:'Real Madrid vs Barcelona', status:'delay', score:'-- : --'}," +
                    "{teams:'Arsenal vs Man City', status:'live', score:'2 : 1'}," +
                    "{teams:'Man United vs Liverpool', status:'live', score:'30 MIN TO GO'}," +
                    "{teams:'Bayern vs Dortmund', status:'soon', score:'-- : --'}" +
                    "];" +

                    "function render() {" +
                    "let html='';" +
                    "matches.forEach(m => {" +
                    "html += `<div class='card'>" +
                    "<div class='teams'>${m.teams}</div>" +
                    "<div class='score'>${m.score}</div>" +
                    "<div class='status ${m.status}'>" +
                    "${m.status==='live'?'LIVE':m.status==='delay'?'DELAYED':'STARTING SOON'}" +
                    "</div>" +
                    "<div class='time'>Updated: ${new Date().toLocaleTimeString()}</div>" +
                    "</div>`;" +
                    "});" +
                    "document.getElementById('matches').innerHTML = html;" +
                    "}" +

                    "setInterval(render,5000);" +
                    "render();" +
                    "</script>" +

                    "</body>" +
                    "</html>";

            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().add(HEADER_CONTENT_TYPE, CONTENT_TYPE_HTML);
            exchange.sendResponseHeaders(200, bytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    // ================= HEALTH =================
    static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String response = "{ \"status\": \"UP\" }";
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().add(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);
            exchange.sendResponseHeaders(200, bytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    // ================= INFO =================
    static class InfoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String response = "{ \"app\": \"football-live\", \"version\": \"2.0\", \"env\": \"dev\" }";
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().add(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);
            exchange.sendResponseHeaders(200, bytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}