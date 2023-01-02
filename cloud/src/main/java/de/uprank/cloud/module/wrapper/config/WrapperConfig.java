package de.uprank.cloud.module.wrapper.config;

public class WrapperConfig {

    private final String name;

    private final String hostName;
    private final Integer port;

    private final String templateLoaderDownload;

    public WrapperConfig(String name, String hostName, Integer port, String templateLoaderDownload) {
        this.name = name;

        this.hostName = hostName;
        this.port = port;

        this.templateLoaderDownload = templateLoaderDownload;
    }
}
