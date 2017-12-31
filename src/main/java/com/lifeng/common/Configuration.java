package com.lifeng.common;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
    private Map<String, Object> configurationItems;
    public Configuration(){
       configurationItems = new HashMap<>();
    }

    public int getInt(ConfigOption option) {
        return (Integer)getValueOrDefaultFromOption(option);
    }

    public boolean getBoolean(ConfigOption option) {
        return (boolean)getValueOrDefaultFromOption(option);
    }

    private Object getValueOrDefaultFromOption(ConfigOption option){
        Object value = configurationItems.get(option.getKey());
        return value == null ? option.getDefaultValue() : value;
    }

    public void setValue(String key, Object value) {
        configurationItems.put(key, value);
    }

}
