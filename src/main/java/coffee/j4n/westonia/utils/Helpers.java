package coffee.j4n.westonia.utils;

import coffee.j4n.westonia.Westonia;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A utility class that provides helper methods.
 */
public final class Helpers {

    /**
     * Creates a new instance of the given class with default values.
     *
     * @param clazz The class to create an instance of.
     * @return The new instance of the class.
     * @param <T> The type of the class that should be created.
     */
    public static <T> T createWithDefaultValues(Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (!field.getType().isPrimitive()) {
                    field.setAccessible(true);
                    field.set(instance, null);
                }
            }

            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Runs a task on the main thread from a different thread.
     *
     * @param task The task to run.
     */
    public static void runOnMainThread(Runnable task) {
        if (Bukkit.isPrimaryThread()) {
            task.run();
        } else {
            Bukkit.getScheduler().runTask(Westonia.getInstance(), task);
        }
    }
}
