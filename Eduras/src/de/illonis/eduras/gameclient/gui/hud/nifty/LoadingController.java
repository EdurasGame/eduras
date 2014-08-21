package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.lwjgl.opengl.Display;

import de.illonis.eduras.gameclient.datacache.GraphicsPreLoader;
import de.illonis.eduras.gameclient.gui.animation.EffectFactory;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Handles the loading screen work.
 * 
 * @author illonis
 * 
 */
public class LoadingController extends EdurasScreenController {

	private Element progressBarElement;
	private Label loadingTextDisplay;
	private boolean load = false;
	private int frameCount = 0;

	LoadingController(GameControllerBridge game) {
		super(game);
	}

	@Override
	protected void initScreen(Screen screen) {
		progressBarElement = screen.findElementById("progressbar");
		loadingTextDisplay = screen
				.findNiftyControl("loadingtext", Label.class);
	}

	/**
	 * Called from update logic and performs the actual loading each step.
	 */
	public void update() {
		if (load) {
			switch (frameCount) {
			case 1:
				setProgress(0.1f, "Loading shapes");
				GraphicsPreLoader.loadShapes();
				float scale = Display.getWidth() / 800;
				GraphicsPreLoader.loadFonts(scale);
				break;
			case 2:
				setProgress(0.2f, "Loading objects");
				GraphicsPreLoader.loadGraphics();
				break;
			case 3:
				setProgress(0.3f, "Loading interface");
				GraphicsPreLoader.loadGuiGraphics();
				break;
			case 4:
				setProgress(0.4f, "Loading items");
				GraphicsPreLoader.loadInventoryIcons();
				break;
			case 5:
				setProgress(0.5f, "Loading other icons");
				GraphicsPreLoader.loadIcons();
				break;
			case 6:
				setProgress(0.6f, "Loading animations");
				EffectFactory.init();
				break;
			case 7:
				game.getEduras().init();
				setProgress(0.7f, "Retrieving server data.");
				break;
			default:
				break;
			}
			frameCount++;
		}
	}

	@Override
	public void onStartScreen() {
		super.onStartScreen();
	}

	private void setProgress(final float progress, final String loadingText) {
		final int MIN_WIDTH = 32;
		int pixelWidth = (int) (MIN_WIDTH + (progressBarElement.getParent()
				.getWidth() - MIN_WIDTH) * progress);
		progressBarElement.setConstraintWidth(new SizeValue(pixelWidth + "px"));
		progressBarElement.getParent().layoutElements();
		loadingTextDisplay.setText(loadingText);
	}

	/**
	 * Resets the loading progress.
	 */
	public void reset() {
		setProgress(0, "Start loading...");
		frameCount = 0;
		load = true;
	}
}