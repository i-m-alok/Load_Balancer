package com.loadbalancer.balancing_strategy;

public class RoundRobinStrategy implements BalancingStrategy{

    private static String[] servers;
    private volatile static int pointer = 0;

    public RoundRobinStrategy(String[] servers){
        RoundRobinStrategy.servers = servers;
    }

    @Override
    public synchronized String getNextRoute(){
        int index = (pointer)%(servers.length);
        pointer++;
        return servers[index];
    }
}
