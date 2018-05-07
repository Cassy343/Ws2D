package ws2d.core.generic;

import ws2d.util.UniquelyIdentifiableObject;

/**
 * The base class for all tasks that can be registered in the server scheduler.
 * 
 * @author Ian
 * @see ws2d.core.server.Scheduler Server Scheduler
 */
public abstract class TaskBase extends UniquelyIdentifiableObject {
    /**
     * The task to execute.
     */
    protected final Runnable task;
    /**
     * The delay between or before execution(s).
     */
    protected long delay;
    /**
     * Whether or not the task is currently updating.
     */
    protected boolean suspended;
    /**
     * Whether or not the task is finished.
     */
    protected boolean complete;
    
    /**
     * Constructs a new instance of <code>TaskBase</code> with a task to execute
     * and a delay between or before execution(s).
     * 
     * @param task the task to execute.
     * @param delay the delay between or before execution(s).
     */
    protected TaskBase(Runnable task, long delay) {
        this.task = task;
        this.delay = delay;
        this.suspended = false;
        this.complete = false;
    }
    
    /**
     * Updates the task. This method should be called once per tick to keep timing
     * consistent.
     */
    public abstract void tick();
    
    /**
     * Suspends the current task's execution.
     */
    public void suspend() {
        suspended = true;
    }
    
    /**
     * Resumes the current task's execution.
     */
    public void resume() {
        suspended = false;
    }
    
    /**
     * Halts the task so that it will no longer run.
     */
    public void stop() {
        complete = true;
    }
    
    /**
     * Returns whether or not the task is halted.
     * 
     * @return <code>true</code>, if the task is halted, <code>false</code> otherwise.
     */
    public boolean isFinished() {
        return complete;
    }
}
