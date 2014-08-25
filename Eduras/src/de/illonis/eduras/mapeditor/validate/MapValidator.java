package de.illonis.eduras.mapeditor.validate;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import de.illonis.eduras.mapeditor.gui.dialog.ValidateDialog;

/**
 * Validates a map against all validation tasks.
 * 
 * @author illonis
 * 
 */
public class MapValidator extends SwingWorker<ValidateTask[], Void> {

	/**
	 * all validation tasks.
	 */
	public final static List<ValidateTask> tasks = new LinkedList<ValidateTask>();
	private final ValidateDialog dialog;

	/**
	 * @param dialog
	 *            the result dialog.
	 */
	public MapValidator(ValidateDialog dialog) {
		this.dialog = dialog;
	}

	@Override
	protected ValidateTask[] doInBackground() {
		ValidateTask[] results = new ValidateTask[tasks.size()];
		synchronized (tasks) {
			for (int i = 0; i < tasks.size(); i++) {
				ValidateTask task = tasks.get(i);
				task.validate();
				results[i] = task;
			}
		}
		return results;
	}

	@Override
	protected void done() {
		try {
			get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		dialog.onCheckCompleted();
	}

	/**
	 * Creates a list of all tasks available.
	 */
	public static void init() {
		tasks.add(new BasicValidation());
		tasks.add(new SpawnValidation());
		tasks.add(new PortalValidation());
		tasks.add(new NamingValidation());
	}

}
