package com.github.exampleservice.serviceregistry;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ServiceRegistryClient {

    private List<RSocketServerInstance> instances;

    public ServiceRegistryClient() {
        this.instances = Arrays.asList(
                new RSocketServerInstance("localhost", 7070),
                new RSocketServerInstance("localhost", 7071)
        );
    }

    public List<RSocketServerInstance> getInstances() {
        return this.instances;
    }
}
