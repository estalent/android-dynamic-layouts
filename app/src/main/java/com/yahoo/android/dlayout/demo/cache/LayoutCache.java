package com.yahoo.android.dlayout.demo.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vgaurav on 3/15/16.
 */
public class LayoutCache {

    private Map<String, byte[]> cache;

    public LayoutCache() {
        cache = new HashMap<>();
    }

    public void put(String url, byte[] data) {
        cache.put(url, data);
    }

    public byte[] get(String url) {
        return cache.get(url);
    }

    public void remove(String url) {
        cache.remove(url);
    }

    public void clear() {
        cache.clear();
    }
}
