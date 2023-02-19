package de.uprank.cloud.module.master.wrapper;

import de.uprank.cloud.module.master.MasterModule;
import de.uprank.cloud.packets.type.wrapper.WrapperStartPacket;
import io.netty.channel.Channel;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class WrapperManager {

    private final MasterModule masterModule;

    @Getter
    private List<Wrapper> wrapperList = new CopyOnWriteArrayList<>();

    public WrapperManager(MasterModule masterModule) {
        this.masterModule = masterModule;
        new Thread(() -> {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    checkTimeOut();

                }
            }, 5L, 5L);
        }).start();
    }

    public Wrapper getWrapperbyName(String name) {
        Iterator<Wrapper> wrapperIterator = wrapperList.iterator();
        while (wrapperIterator.hasNext()) {
            Wrapper wrapper = wrapperIterator.next();
            if (wrapper.getName().equalsIgnoreCase(name)) {
                return wrapper;
            }
        }
        return null;
    }

    public void removeWrapperByName(String name) {
        Iterator<Wrapper> wrapperIterator = wrapperList.iterator();
        while (wrapperIterator.hasNext()) {
            Wrapper wrapper = wrapperIterator.next();
            if (wrapper.getName().equalsIgnoreCase(name)) {
                this.wrapperList.remove(name);
                this.masterModule.info("&7Wrapper &b&l" + wrapper.getName() + " &7is now unregistered");
            }
        }
    }

    public void checkTimeOut() {
        Iterator<Wrapper> wrapperIterator = wrapperList.iterator();
        while (wrapperIterator.hasNext()) {
            Wrapper wrapper = wrapperIterator.next();
            if (System.currentTimeMillis() > wrapper.getTimeout()) {
                this.wrapperList.remove(wrapper);
            }
        }
    }

    public void registerWrapper(WrapperStartPacket wrapperStartPacket, Channel channel) {

        new Thread(() -> {
            Iterator<Wrapper> wrapperIterator = wrapperList.iterator();
            while (wrapperIterator.hasNext()) {
                Wrapper wrapper = wrapperIterator.next();
                if (wrapper.getName().equalsIgnoreCase(wrapperStartPacket.getName())) {
                    this.masterModule.warning("&cWrapper &b&l" + wrapperStartPacket.getName() + " &cis already registered");
                    return;
                }
            }

            Wrapper wrapper = new Wrapper(wrapperStartPacket.getName(), channel, wrapperStartPacket.getHostName(), wrapperStartPacket.getMaxMemory());
            wrapper.setChannel(channel);
            wrapper.setTimeout(System.currentTimeMillis() + 200);
            this.wrapperList.add(wrapper);

            this.masterModule.info("&7Wrapper &b&l" + wrapper.getName() + " &7is now registered and ready to use!");
        }).start();
    }
}
