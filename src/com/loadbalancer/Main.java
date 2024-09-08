package com.loadbalancer;

import com.loadbalancer.balancing_strategy.BalancingStrategy;
import com.loadbalancer.balancing_strategy.RoundRobinStrategy;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws IOException {

        // configure LB with Routing Strategy:
        BalancingStrategy balancingStrategy = new RoundRobinStrategy(args);
        Configuration configuration = Configuration.getInstance(args, balancingStrategy);
        healthCheck();

        // start the Load Balancer Server
        Server server = new Server();
        server.createServer(8090);
    }

    /**
       This method is continuously running the health check on the configured servers
     */
    public synchronized static void  healthCheck(){
        Runnable runnable = ()-> {
            // keep checking the health check
            while (true) {
                // for each server run the check in different server
                for (String url :  Configuration.getServers()) {
                    Thread threadForCheck = new Thread(()->{
                        HttpURLConnection urlConnection = null;
                        try {
                            System.out.println("Trying to connect to " + url);
                            urlConnection = (HttpURLConnection) new URL(url + "/healthcheck").openConnection();

                            // if healthcheck is OK then add the server to activeServers list
                            if (urlConnection.getResponseCode() == 200) {
                                Configuration.addActiveServer(url);
                            }
                        } catch (IOException e) {
                            // if not then remove from active server list
                            Configuration.removeActiveServer(url);
                            System.out.println("Seems like server " + url + " is inactive.");
                        }
                    });
                    threadForCheck.start();
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread thread = new Thread(runnable, "HealthCheck Thread");
        thread.start();
    }
}