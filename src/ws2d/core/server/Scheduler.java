package ws2d.core.server;

import ws2d.core.generic.RepeatingTask;
import ws2d.core.generic.TaskBase;
import ws2d.core.generic.TimerTask;
import ws2d.util.UidSet;

/**
 * This class keeps track of timed tasks for the server.
 * 
 * @author Ian
 */
public class Scheduler {
    /**
     * The set of scheduled tasks.
     */
    private final UidSet<TaskBase> tasks;
    
    /**
     * Constructs a new instance of <code>Scheduler</code>.
     */
    public Scheduler() {
        this.tasks = new UidSet();
    }
    
    /**
     * Schedules a task to be executed after a certain number of ticks.
     * 
     * @param task the task to execute.
     * @param delay the number of ticks before it executes.
     * @return the unique ID of the scheduled task.
     */
    public int runTaskLater(Runnable task, long delay) {
        return tasks.add(new TimerTask(task, delay));
    }
    
    /**
     * Schedules a task to be executed on the next server tick.
     * 
     * @param task the task to execute.
     * @return the unique ID of the scheduled task.
     */
    public int runTaskNextTick(Runnable task) {
        return runTaskLater(task, 1);
    }
    
    /**
     * Schedules a task to be repeated every certain number of ticks.
     * 
     * @param task the task to execute.
     * @param cycle the amount of ticks between executions.
     * @return the unique ID of the scheduled task.
     */
    public int runTaskRepeatedly(Runnable task, long cycle) {
        return tasks.add(new RepeatingTask(task, cycle));
    }
    
    /**
     * Pauses the execution of the task with the specified unique ID.
     * 
     * @param uid the unique ID of the task.
     */
    public void suspendTask(int uid) {
        tasks.get(uid).suspend();
    }
    
    /**
     * Resumes the execution of the task with the specified unique ID.
     * 
     * @param uid the unique ID of the task.
     */
    public void resumeTask(int uid) {
        tasks.get(uid).resume();
    }
    
    /**
     * Terminates the execution of the task with the specified unique ID.
     * 
     * @param uid the unique ID of the task.
     */
    public void stopTask(int uid) {
        tasks.remove(uid).stop();
    }
    
    /**
     * Updates all schedules tasks by calling their respective tick methods. This
     * method should be called once per tick to keep timing consistent.
     */
    public void tick() {
        tasks.forEach(task -> {
            task.tick();
            if(task.isFinished())
                tasks.remove(task);
        });
    }
}
