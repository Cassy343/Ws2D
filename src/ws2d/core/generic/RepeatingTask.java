package ws2d.core.generic;

/**
 * A task which repeats during the duration of runtime every number of ticks.
 * 
 * @author Ian
 */
public class RepeatingTask extends TaskBase {
    /**
     * The number of ticks between executions.
     */
    private final long delayCycle;
    
    /**
     * Constructs a new instance of <code>RepeatingTask</code> with a <code>Runnable</code>
     * to execute and a tick delay between executions.
     * 
     * @param task the task to run repeatedly.
     * @param delay the ticks between execution.
     */
    public RepeatingTask(Runnable task, long delay) {
        super(task, 0);
        this.delayCycle = delay;
    }

    /**
     * Updates the task. This method should be called once per tick to keep the
     * time between executions equivalent.
     */
    @Override
    public void tick() {
        if(suspended || complete)
            return;
        if(delay <= 0) {
            task.run();
            delay = delayCycle;
            return;
        }
        -- delay;
    }
}
