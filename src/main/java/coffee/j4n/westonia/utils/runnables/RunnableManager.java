package coffee.j4n.westonia.utils.runnables;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * RunnableManager is responsible for managing multiple timers associated with UUIDs.
 * It allows starting, stopping, and checking the status of timers, as well as executing countdowns.
 */
public class RunnableManager {
    private final JavaPlugin plugin;
    private final Map<UUID, List<ManagedTimer>> timers;
    private final Map<UUID, Map<UUID, Integer>> countdownTimers; // Stores remaining time for each countdown per player

    /**
     * Creates a new RunnableManager instance.
     * This instance is used to manage timers and countdowns for a specific plugin.
     * <p>
     * Usally, you would create a new instance of RunnableManager in the main class of your plugin's onEnable method once, and then use it to manage timers throughout your plugin.
     *
     * @param plugin the plugin instance used for scheduling tasks.
     */
    public RunnableManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.timers = new HashMap<>();
        this.countdownTimers = new HashMap<>();
    }

    /**
     * Starts a timer for a given UUID with specified delay, period, and task to execute.
     *
     * @param id     the UUID associated with the timer.
     * @param delay  the initial delay before the task runs, in ticks.
     * @param period the period between consecutive executions, in ticks.
     * @param task   the task to execute periodically.
     * @return the unique ID of the created timer.
     */
    public UUID startTimer(UUID id, long delay, long period, Runnable task) {
        UUID timerId = UUID.randomUUID();
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        };

        runnable.runTaskTimer(plugin, delay, period);
        ManagedTimer managedTimer = new ManagedTimer(timerId, runnable);
        timers.computeIfAbsent(id, k -> new ArrayList<>()).add(managedTimer);
        return timerId;
    }

    /**
     * Stops a specific timer associated with a UUID by its timer ID.
     *
     * @param id      the UUID associated with the timer.
     * @param timerId the unique ID of the timer to stop.
     */
    public void stopTimer(UUID id, UUID timerId) {
        List<ManagedTimer> runnableList = timers.get(id);
        if (runnableList != null) {
            runnableList.removeIf(managedTimer -> {
                if (managedTimer.getTimerId().equals(timerId)) {
                    managedTimer.getRunnable().cancel();
                    countdownTimers.getOrDefault(id, new HashMap<>()).remove(timerId);
                    return true;
                }
                return false;
            });
        }
    }

    /**
     * Stops all timers associated with a specific UUID.
     *
     * @param id the UUID whose timers should be stopped.
     */
    public void stopAllTimers(UUID id) {
        List<ManagedTimer> runnableList = timers.get(id);
        if (runnableList != null) {
            for (ManagedTimer managedTimer : runnableList) {
                managedTimer.getRunnable().cancel();
            }
            runnableList.clear();
        }
        countdownTimers.remove(id);
    }

    /**
     * Checks if there are any running timers associated with a specific UUID.
     *
     * @param id the UUID to check.
     * @return true if there are running timers, false otherwise.
     */
    public boolean isTimerRunning(UUID id) {
        List<ManagedTimer> runnableList = timers.get(id);
        return runnableList != null && !runnableList.isEmpty();
    }

    /**
     * Stops all timers managed by this RunnableManager.
     */
    public void stopAllTimers() {
        for (List<ManagedTimer> runnableList : timers.values()) {
            for (ManagedTimer managedTimer : runnableList) {
                managedTimer.getRunnable().cancel();
            }
        }
        timers.clear();
        countdownTimers.clear();
    }

    /**
     * Starts a countdown timer for a given UUID that runs for a specified duration and executes a task upon completion.
     *
     * @param id       the UUID associated with the countdown.
     * @param duration the duration of the countdown in seconds.
     * @param onTick   the task to execute every second during the countdown.
     * @param onFinish the task to execute when the countdown finishes.
     * @return the unique ID of the created countdown timer.
     */
    public UUID startCountdown(UUID id, int duration, Runnable onTick, Runnable onFinish) {
        UUID timerId = UUID.randomUUID();
        countdownTimers.computeIfAbsent(id, k -> new HashMap<>()).put(timerId, duration); // Initialize the remaining time

        BukkitRunnable countdownRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                Map<UUID, Integer> playerCountdowns = countdownTimers.get(id);
                int remainingTime = playerCountdowns.getOrDefault(timerId, 0);
                if (remainingTime > 0) {
                    onTick.run();
                    playerCountdowns.put(timerId, remainingTime - 1);
                } else {
                    onFinish.run();
                    this.cancel();
                    timers.get(id).removeIf(timer -> timer.getTimerId().equals(timerId));
                    playerCountdowns.remove(timerId);
                }
            }
        };

        countdownRunnable.runTaskTimer(plugin, 0L, 20L);
        ManagedTimer managedTimer = new ManagedTimer(timerId, countdownRunnable);
        timers.computeIfAbsent(id, k -> new ArrayList<>()).add(managedTimer);
        return timerId;
    }

    /**
     * Gets the remaining time of a specific countdown for a given UUID and timer ID.
     *
     * @param playerId the UUID associated with the player.
     * @param timerId  the unique ID of the countdown timer.
     * @return the remaining time in seconds, or -1 if no countdown is found.
     */
    public int getRemainingTime(UUID playerId, UUID timerId) {
        Map<UUID, Integer> playerCountdowns = countdownTimers.get(playerId);
        if (playerCountdowns != null) {
            return playerCountdowns.getOrDefault(timerId, -1);
        }
        return -1;
    }
}
