package com.loadbalancer;

import com.loadbalancer.balancing_strategy.BalancingStrategy;
import java.io.IOException;
import java.util.HashSet;

public class Configuration {
    private static volatile Configuration instance;
    public String[] servers;
    public BalancingStrategy balancingStrategy;
    public HashSet<String> activeServers;

    private Configuration(String[] value, BalancingStrategy balancingStrategy) {
        this.servers = value;
        this.balancingStrategy = balancingStrategy;
        this.activeServers = new HashSet<>();
    }

    public static Configuration getInstance(String[] url, BalancingStrategy balancingStrategy) throws IOException {
        if(instance==null){
            synchronized (Configuration.class){
                if(instance==null){
                    instance = new Configuration(url, balancingStrategy);
                }
            }
        }
        return instance;
    }

    public static BalancingStrategy getBalancingStrategy(){
        return instance.balancingStrategy;
    }
    public static String[] getServers(){
        return instance.servers;
    }
    public synchronized static void addActiveServer(String server){
        instance.activeServers.add(server);
    }
    public synchronized static void removeActiveServer(String server){
        instance.activeServers.remove(server);
    }

    public synchronized static boolean isActiveServer(String server){
        return instance.activeServers.contains(server);
    }
}
