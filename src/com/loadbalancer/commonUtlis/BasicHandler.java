package com.loadbalancer.commonUtlis;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public abstract class BasicHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logRequestDetails(exchange);
    }

    public void logRequestDetails(HttpExchange exchange){
        System.out.println("Request from " + exchange.getRemoteAddress());
        System.out.println(exchange.getRequestMethod() + "\t" + exchange.getRequestURI() + "\t" + exchange.getProtocol());
        System.out.println("Host: "+ exchange.getLocalAddress());
        System.out.println("User-Agent:" + exchange.getRequestHeaders().get("User-Agent"));
        System.out.println("Accept:" + exchange.getRequestHeaders().get("Accept"));
    }
}
