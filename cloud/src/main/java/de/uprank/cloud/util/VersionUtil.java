package de.uprank.cloud.util;

public enum VersionUtil {

    PAPER_1_19("PAPER_1_19", "https://api.papermc.io/v2/projects/paper/versions/1.19/builds/81/downloads/paper-1.19-81.jar"),
    WATERFALL("WATERFALL", "https://api.papermc.io/v2/projects/waterfall/versions/1.19/builds/510/downloads/waterfall-1.19-510.jar");

    private final String key;
    private final String downloadUrl;

    VersionUtil(String key, String downloadUrl) {
        this.key = key;
        this.downloadUrl = downloadUrl;
    }

    public String getKey() {
        return key;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public static boolean exists(String key) {
        for (VersionUtil versionUtil : VersionUtil.values()) {
            if (versionUtil.name().equals(key.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

}
