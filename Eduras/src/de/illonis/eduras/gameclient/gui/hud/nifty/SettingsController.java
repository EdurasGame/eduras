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

import de.illonis.eduras.gameclient.audio.SoundMachine;
import de.illonis.eduras.gameclient.audio.SoundMachine.SoundType;
import de.illonis.eduras.gameclient.userprefs.KeyBindings;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameclient.userprefs.Settings;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.CheckBoxStateChangedEvent;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;

/**
 * Manages settings.
 * 
 * @author illonis
 * 
 */
public class SettingsController extends EdurasScreenController {

	private Label resolutionLabel;
	private ListBox<KeyBinding> box;
	private final KeyBindings bindings;
	private final Settings settings;
	private CheckBox chooseOnPressBox;
	private CheckBox continuousItemUsageBox;

	SettingsController(GameControllerBridge game) {
		super(game);
		settings = EdurasInitializer.getInstance().getSettings();
		bindings = settings.getKeyBindings();
	}

	/**
	 * returns back to server list.
	 */
	public void back() {
		SoundMachine.play(SoundType.CLICK, 2f);
		game.enterState(2, new FadeOutTransition(Color.black, 100),
				new FadeInTransition(Color.black, 300));
		try {
			settings.save();
		} catch (IOException e) {
			System.out.println("Could not save settings.");
		}
	}

	/**
	 * Saves checkbox changes.
	 * 
	 * @param checkboxId
	 *            the changed checkbox.
	 * @param event
	 *            the event that contains information.
	 */
	@NiftyEventSubscriber(pattern = ".*Box")
	public void onCheckboxChanged(final String checkboxId,
			final CheckBoxStateChangedEvent event) {
		String setting = checkboxId.substring(0, checkboxId.length() - 3);
		settings.setBooleanOption(setting, event.getCheckBox().isChecked());
	}

	/**
	 * Resets all keybindings.
	 */
	public void resetAllKeyBindings() {
		for (KeyBinding binding : KeyBinding.values()) {
			bindings.resetToDefault(binding);
		}
		box.refresh();
	}

	/**
	 * Resets the selected key binding.
	 */
	public void resetKeyBinding() {
		List<KeyBinding> selection = box.getSelection();
		if (selection.size() == 1) {
			bindings.resetToDefault(selection.get(0));
			box.refresh();
		}
	}

	private void fillResolutionSelect(Screen screen) {
		@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	@Override
	protected void initScreen(Screen screen) {
		fillResolutionSelect(screen);
		Label hintLabel = screen.findNiftyControl("hintLabel", Label.class);
		hintLabel.setText("Select a keybinding and press a key to bind it.");
		box = (ListBox<KeyBinding>) screen.findNiftyControl("#keyBindingsList",
				ListBox.class);
		chooseOnPressBox = screen.findNiftyControl("chooseOnPressBox",
				CheckBox.class);
		continuousItemUsageBox = screen.findNiftyControl(
				"continuousItemUsageBox", CheckBox.class);

		for (KeyBinding binding : KeyBinding.values()) {
			box.addItem(binding);
		}
		chooseOnPressBox
				.setChecked(settings.getBooleanSetting("chooseOnPress"));
		continuousItemUsageBox.setChecked(settings
				.getBooleanSetting("continuousItemUsage"));
	}

	/**
	 * Changes screen resolution on select.
	 * 
	 * @param id
	 *            the select box.
	 * @param event
	 *            the event.
	 */
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

	/**
	 * Listens for new keys.
	 * 
	 * @param key
	 *            the key to be set.
	 */
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