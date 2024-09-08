package com.loadbalancer;

import com.loadbalancer.balancing_strategy.BalancingStrategy;
import com.loadbalancer.commonUtlis.BasicHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LBHttpHandler extends BasicHandler {

    protected static final Logger logger = LogManager.getLogger(LBHttpHandler.class);
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logRequestDetails(exchange);
        // urls of the servers we need to divert our traffic to
        // later we can make it flexible and read it from some config file
        BalancingStrategy balancingStrategy = Configuration.getBalancingStrategy();

        // Retry Mechanism
        String nextRoute;
        while(true){
            nextRoute=balancingStrategy.getNextRoute();
            if(Configuration.isActiveServer(nextRoute)){
                break;
            }
            System.out.println("Server is inactive " + nextRoute + " Re-routing to some other server.");
        }
        routeRequestToServer(exchange, nextRoute);
    }

    private void routeRequestToServer(HttpExchange exchange, String nextRoute) throws IOException {
        URL url = new URL(nextRoute);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        BufferedReader responseReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        // read response
        StringBuilder response = new StringBuilder();
        String line;
        while ((line=responseReader.readLine())!=null){
            response.append(line);
        }
        logger.info("Response from server: {}", urlConnection.getResponseCode());
        logger.info("{}", response);
        // write back to client
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
    }
}
