package de.uprank.cloud.module.wrapper.group.server;

import java.util.HashMap;
import java.util.Map;

public class ServiceGroupManager {

    private final Map<String, ServiceGroup> serviceGroups = new HashMap<>();

    public Map<String, ServiceGroup> getServiceGroups() {
        return serviceGroups;
    }
}
