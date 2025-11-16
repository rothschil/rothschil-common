package io.github.rothschil.common.config.cache.caffeine;

public class CaffeineEntry {


    private String key;
    private Object value;
    private long expireTime;
    private boolean accessFresh;


    public CaffeineEntry() {
    }

    public CaffeineEntry(String key, Object value, long expireTime, boolean accessFresh) {
        this.key = key;
        this.value = value;
        this.expireTime = expireTime;
        this.accessFresh = accessFresh;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isAccessFresh() {
        return accessFresh;
    }

    public void setAccessFresh(boolean accessFresh) {
        this.accessFresh = accessFresh;
    }
}
