package com.lifeng.common;

public class ConfigOption <T>{
    private final String key;
    private final T defaultValue;
    private final String description;

    public ConfigOption(String key, T defaultValue) {
        this(key, defaultValue, "");

    }

    public ConfigOption(String key, T defaultValue, String description) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public String getDescription() {
        return description;
    }
}
