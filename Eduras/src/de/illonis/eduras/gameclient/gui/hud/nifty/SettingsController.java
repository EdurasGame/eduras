package de.illonis.eduras.gameclient.gui.hud.nifty;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import de.illonis.eduras.gameclient.gui.SoundMachine;
import de.illonis.eduras.gameclient.gui.SoundMachine.SoundType;
import de.illonis.eduras.gameclient.userprefs.KeyBindings;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;

public class SettingsController extends EdurasScreenController {

	private Label resolutionLabel;
	private ListBox<KeyBinding> box;
	private final KeyBindings bindings;

	public SettingsController(GameControllerBridge game) {
		super(game);
		bindings = EdurasInitializer.getInstance().getSettings()
				.getKeyBindings();
	}

	public void back() {
		SoundMachine.getSound(SoundType.CLICK).play(2f, 0.1f);
		game.enterState(2, new FadeOutTransition(Color.black, 100),
				new FadeInTransition(Color.black, 300));
		try {
			EdurasInitializer.getInstance().getSettings().save();
		} catch (IOException e) {
			System.out.println("Could not save settingsgwe");
		}
	}

	public void resetAllKeyBindings() {
		for (KeyBinding binding : KeyBinding.values()) {
			bindings.resetToDefault(binding);
		}
		box.refresh();
	}

	public void resetKeyBinding() {
		List<KeyBinding> selection = box.getSelection();
		if (selection.size() == 1) {
			bindings.resetToDefault(selection.get(0));
			box.refresh();
		}
	}

	private void fillResolutionSelect(Screen screen) {
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
			control.selectItem(currentMode);
		} catch (LWJGLException e) {
		}
	}

	@Override
	protected void initScreen(Screen screen) {
		fillResolutionSelect(screen);
		Label hintLabel = screen.findNiftyControl("hintLabel", Label.class);
		hintLabel.setText("Select a keybinding and press a key to bind it.");
		box = (ListBox<KeyBinding>) screen.findNiftyControl("keyBindingsList",
				ListBox.class);

		for (KeyBinding binding : KeyBinding.values()) {
			box.addItem(binding);
		}
		box.setFocus();
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

	public void keyPressed(int key) {
		List<KeyBinding> selection = box.getSelection();
		if (selection.size() == 1) {
			bindings.setKeyBinding(selection.get(0), key);
			box.refresh();
		}
	}

	@Override
	public void onStartScreen() {
		super.onStartScreen();
		nifty.setIgnoreKeyboardEvents(true);
	}

	@Override
	public void onEndScreen() {
		super.onEndScreen();
		nifty.setIgnoreKeyboardEvents(false);
	}
}