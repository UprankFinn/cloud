package de.uprank.cloud.module.master.wrapper;

import de.uprank.cloud.packets.Packet;
import io.netty.channel.Channel;

public class Wrapper {

    private final String name;

    private final String hostName;
    private Channel channel;

    private Integer usedMemory;
    private Integer freeMemory;
    private Integer maxMemory;

    private long timeout = -1;
    private double cpuUsage;
    private double averageCpu;

    public Wrapper(String name, String hostName, Integer maxMemory) {
        this.name = name;

        this.hostName = hostName;
        this.maxMemory = maxMemory;
    }

    public void sendPacket(Packet packet) {
        new Thread(() -> this.channel.writeAndFlush(packet)).start();
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setFreeMemory(Integer freeMemory) {
        this.freeMemory = freeMemory;
    }

    public void setUsedMemory(Integer usedMemory) {
        this.usedMemory = usedMemory;
    }

    public void setMaxMemory(Integer maxMemory) {
        this.maxMemory = maxMemory;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public void setAverageCpu(double averageCpu) {
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

    public Integer getMaxMemory() {
        return maxMemory;
    }

    public long getTimeout() {
        return timeout;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public double getAverageCpu() {
        return averageCpu;
    }
}
