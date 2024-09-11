package coffee.j4n.westonia.utils.runnables;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * ManagedTimer is a helper class that encapsulates a BukkitRunnable and a unique identifier for the timer.
 */
public class ManagedTimer {
    private final UUID timerId;
    private final BukkitRunnable runnable;

    /**
     * Constructor for ManagedTimer.
     *
     * @param timerId  the unique ID of the timer.
     * @param runnable the runnable task associated with the timer.
     */
    public ManagedTimer(UUID timerId, BukkitRunnable runnable) {
        this.timerId = timerId;
        this.runnable = runnable;
    }

    /**
     * Gets the unique ID of the timer.
     *
     * @return the timer ID.
     */
    public UUID getTimerId() {
        return timerId;
    }

    /**
     * Gets the runnable task associated with the timer.
     *
     * @return the BukkitRunnable.
     */
    public BukkitRunnable getRunnable() {
        return runnable;
    }
}