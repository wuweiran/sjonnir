package clan.midnight.sm;

public interface StateContext {
    Object get(String key);

    void put(String key, Object value);

    void remove(String key);
}
