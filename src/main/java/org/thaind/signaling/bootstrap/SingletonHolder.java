package org.thaind.signaling.bootstrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author duyenthaind
 */
public class SingletonHolder {

    private static final Map<Class<?>, Object> MAP_SINGLE_TON_HOLDER = new ConcurrentHashMap<>();
    private static final Logger LOGGER = LogManager.getLogger("SingletonHolder");

    private SingletonHolder() {
    }

    public static void addBean(Object object) {
        MAP_SINGLE_TON_HOLDER.putIfAbsent(object.getClass(), object);
    }

    public static void removeBean(Class<?> clazz) {
        MAP_SINGLE_TON_HOLDER.remove(clazz);
    }

    public static <T> Optional<T> getBean(Class<T> clazz) {
        Object object = MAP_SINGLE_TON_HOLDER.get(clazz);
        if (object.getClass().equals(clazz)) {
            return Optional.of((T) object);
        }
        return Optional.empty();
    }

    public static <T> T getBeanOrDefault(Class<T> clazz) {
        Object object = MAP_SINGLE_TON_HOLDER.get(clazz);
        try {
            if (object == null) {
                return clazz.getDeclaredConstructor().newInstance();
            }
        } catch (Exception ex) {
            LOGGER.error("Error when trying to init default bean, ", ex);
        }
        return (T) object;
    }
}
