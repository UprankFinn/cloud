package de.uprank.cloud.module.master.proxies;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ProxyManager {

    private final Map<String, Proxy> proxies = new HashMap<>();

}
