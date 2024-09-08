import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BackendServer {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java SimpleHttpServer <port>");
            System.exit(1);
        }

        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid port number. Please provide a valid integer.");
            System.exit(1);
            return; // Return to avoid further execution
        }

        // Validate port number
        if (port < 1 || port > 65535) {
            System.err.println("Port number must be between 1 and 65535.");
            System.exit(1);
            return;
        }

        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
        server.createContext("/", new BEHttpHandler());
        server.createContext("/healthcheck", new HealthCheckHttpHandler());
        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println("Server listening on port::"+ port);
    }

    static class HealthCheckHttpHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Heath check request from " + exchange.getRemoteAddress());

            String response = "I am server with url " + exchange.getLocalAddress() + ". I'm fine :)";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }
    }

    static class BEHttpHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            logRequestDetails(exchange);

            String response = "Hello From Backend Server";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            System.out.println("Replied with a hello message");
            outputStream.close();
        }
        public void logRequestDetails(HttpExchange exchange){
            System.out.println("Request from " + exchange.getRemoteAddress());
            System.out.println(exchange.getRequestMethod() + "\t" + exchange.getRequestURI() + "\t" + exchange.getProtocol());
            System.out.println("Host: "+ exchange.getLocalAddress());
            System.out.println("User-Agent:" + exchange.getRequestHeaders().get("User-Agent"));
            System.out.println("Accept:" + exchange.getRequestHeaders().get("Accept"));
        }

    }
}
