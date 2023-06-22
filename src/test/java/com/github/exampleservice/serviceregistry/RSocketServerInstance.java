package com.github.exampleservice.serviceregistry;

public class RSocketServerInstance {

    private String host;
    private int port;

    public RSocketServerInstance() {

    }

    public RSocketServerInstance(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "RSocketServerInstance{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
