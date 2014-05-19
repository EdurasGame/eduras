package de.illonis.eduras.gameclient.gui.hud.nifty;

import java.util.Arrays;
import java.util.Comparator;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class SettingsController extends EdurasScreenController {

	private Label resolutionLabel;

	public SettingsController(GameControllerBridge game) {
		super(game);
	}

	public void back() {
		game.enterState(2, new FadeOutTransition(Color.black, 100),
				new FadeInTransition(Color.black, 300));
	}

	@Override
	protected void initScreen(Screen screen) {
		DropDown<DisplayMode> control = (DropDown<DisplayMode>) screen
				.findNiftyControl("resolutionSelect", DropDown.class);

		resolutionLabel = screen.findNiftyControl("currentResolution",
				Label.class);
		try {
			DisplayMode currentMode = Display.getDisplayMode();
			resolutionLabel.setText("Current resolution: "
					+ currentMode.toString());
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			Arrays.sort(modes, new Comparator<DisplayMode>() {
				@Override
				public int compare(DisplayMode a, DisplayMode b) {
					if (a.getWidth() > b.getWidth())
						return -1;
					else if (a.getWidth() < b.getWidth())
						return 1;
					else if (a.getHeight() > b.getHeight())
						return -1;
					else if (a.getHeight() < b.getHeight())
						return 1;
					else if (a.getFrequency() > b.getFrequency())
						return -1;
					else if (a.getFrequency() < b.getFrequency())
						return 1;
					else
						return 0;
				}
			});
			for (DisplayMode displayMode : modes) {
				control.addItem(displayMode);
			}

		} catch (LWJGLException e) {
		}

	}

	@NiftyEventSubscriber(id = "resolutionSelect")
	public void onDropDownSelectionChanged(final String id,
			final DropDownSelectionChangedEvent<DisplayMode> event) {
		DisplayMode selected = event.getSelection();
		try {
			game.changeResolution(selected.getWidth(), selected.getHeight());

			resolutionLabel.setText("Current resolution: "
					+ selected.toString());
		} catch (SlickException e) {
		}
	}
}