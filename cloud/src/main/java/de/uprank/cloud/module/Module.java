package de.uprank.cloud.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;

public abstract class Module {

    private final String name;

    private final Gson gson;

    @SneakyThrows
    protected Module(String name) {
        this.name = name;
        this.gson = new GsonBuilder().create();
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public String getName() {
        return name;
    }

}
