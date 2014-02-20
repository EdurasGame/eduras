package de.illonis.eduras.utils;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class holds several {@link Long} - {@link TimerTask} pairs to collect
 * and execute all at once. Each TimerTask is scheduled to run every x ms given
 * by the long value. Note that every task is scheduled to run for the first
 * time immediately after the execute() has been invoked. Also note that you can
 * run each TimedTasksHolder only once.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class TimedTasksHolder {
	private LinkedList<Pair<TimerTask, Long>> myTasks;

	private boolean executing = false;

	/**
	 * Create a new TimedTasksHolder with an empty set of tasks.
	 */
	public TimedTasksHolder() {
		myTasks = new LinkedList<Pair<TimerTask, Long>>();
	}

	/**
	 * Create a new TImedTasksHolder with the given set of tasks.
	 * 
	 * @param tasks
	 */
	public TimedTasksHolder(LinkedList<Pair<TimerTask, Long>> tasks) {
		myTasks = tasks;
	}

	/**
	 * Add a task that is scheduled with the given period.
	 * 
	 * @param task
	 * @param period
	 */
	public void addTask(TimerTask task, long period) {
		myTasks.add(new Pair<TimerTask, Long>(task, period));
	}

	/**
	 * Start executing the given tasks. Will only work once.
	 */
	public void execute() {
		if (executing) {
			return;
		}

		for (Pair<TimerTask, Long> task : myTasks) {
			Timer timerForTask = new Timer();
			timerForTask.schedule(task.getFirst(), 0, task.getSecond());
		}
	}

	/**
	 * Cancel executing the tasks.
	 */
	public void cancel() {
		for (Pair<TimerTask, Long> task : myTasks) {
			task.getFirst().cancel();
		}
	}
}
