package de.illonis.eduras.gameclient.gui.hud.nifty;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

public class LoadingController extends EdurasScreenController {

	private final Callable<Void> loadingCallable = new AssetLoader();
	private Element progressBarElement;
	private Label loadingTextDisplay;
	private Future<Void> loadFuture = null;
	private boolean load = false;
	private Callable<Void> progressUpdater;

	private ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(
			2);

	public LoadingController(GameControllerBridge game) {
		super(game);
	}

	@Override
	protected void initScreen(Screen screen) {
		progressBarElement = screen.findElementById("progressbar");
		loadingTextDisplay = screen
				.findNiftyControl("loadingtext", Label.class);
	}

	public void update() {
		if (progressUpdater != null) {
			try {
				progressUpdater.call();
			} catch (Exception e) {
			}
			progressUpdater = null;
		}
		if (load) {
			if (loadFuture == null) {
				// if we have not started loading yet, submit the Callable to
				// the executor
				loadFuture = exec.submit(loadingCallable);
			}
			// check if the execution on the other thread is done
			if (loadFuture.isDone()) {
				// these calls have to be done on the update loop thread,
				// especially attaching the terrain to the rootNode
				// after it is attached, it's managed by the update loop thread
				// and may not be modified from any other thread anymore!
				load = false;
				game.enterState(4);
			}
		}
	}

	@Override
	public void onStartScreen() {
		super.onStartScreen();
		load = true;
	}

	public void setProgress(final float progress, final String loadingText) {
		progressUpdater = new ProgressUpdater(progress, loadingText);
	}

	class ProgressUpdater implements Callable<Void> {
		private final float progress;
		private final String loadingText;

		public ProgressUpdater(float progress, String loadingText) {
			this.progress = progress;
			this.loadingText = loadingText;
		}

		@Override
		public Void call() throws Exception {
			final int MIN_WIDTH = 32;
			int pixelWidth = (int) (MIN_WIDTH + (progressBarElement.getParent()
					.getWidth() - MIN_WIDTH) * progress);
			progressBarElement.setConstraintWidth(new SizeValue(pixelWidth
					+ "px"));
			progressBarElement.getParent().layoutElements();

			loadingTextDisplay.setText(loadingText);
			return null;
		}
	}

	class AssetLoader implements Callable<Void> {

		@Override
		public Void call() throws Exception {
			// setProgress is thread safe (see below)
			setProgress(0.2f, "Loading grass");
			Thread.sleep(500);
			setProgress(0.4f, "Loading dirt");
			Thread.sleep(500);
			setProgress(0.5f, "Loading rocks");
			Thread.sleep(1000);
			setProgress(0.6f, "Creating terrain");
			Thread.sleep(800);
			setProgress(0.8f, "Positioning terrain");
			Thread.sleep(600);
			setProgress(0.9f, "Loading cameras");
			Thread.sleep(700);
			setProgress(1f, "Loading complete");
			return null;
		}

	}

}