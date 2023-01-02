package de.uprank.cloud.packets.type.wrapper;

import java.io.Serializable;

public class WrapperAlivePacket implements Serializable {

    private final String name;
    private final String hostName;

    private Integer usedMemory;
    private Integer freeMemory;
    private final Integer maxMemory;

    private double cpuUsage;
    private double averageCpu;

    public WrapperAlivePacket(String name, String hostName, Integer usedMemory, Integer freeMemory, Integer maxMemory, double cpuUsage, double averageCpu) {
        this.name = name;
        this.hostName = hostName;
        this.usedMemory = usedMemory;
        this.freeMemory = freeMemory;
        this.maxMemory = maxMemory;
        this.cpuUsage = cpuUsage;
        this.averageCpu = averageCpu;
    }

    public String getName() {
        return name;
    }

    public String getHostName() {
        return hostName;
    }

    public Integer getUsedMemory() {
        return usedMemory;
    }

    public Integer getFreeMemory() {
        return freeMemory;
    }

    public Integer getMaxMemory() {
        return maxMemory;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public double getAverageCpu() {
        return averageCpu;
    }
}
