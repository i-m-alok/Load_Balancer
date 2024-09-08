package com.loadbalancer;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
    HttpServer server;
    public void createServer(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
        server.createContext("/", new  LBHttpHandler());
        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println("Server listening on port::"+ port);
    }


}
