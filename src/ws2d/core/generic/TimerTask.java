package ws2d.core.generic;

/**
 * A task which executes once after a specified tick delay.
 * 
 * @author Ian
 */
public class TimerTask extends TaskBase {
    /**
     * Constructs a new instance of <code>TimerTask</code> with a task to execute
     * and an initial delay.
     * 
     * @param task the task to execute.
     * @param delay the initial delay.
     */
    public TimerTask(Runnable task, long delay) {
        super(task, delay);
    }

    /**
     * Updates the task. This method should be called once per to keep the timer
     * consistent.
     */
    @Override
    public void tick() {
        if(suspended || complete)
            return;
        if(delay <= 0) {
            task.run();
            complete = true;
            return;
        }
        -- delay;
    }
}
