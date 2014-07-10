package de.illonis.eduras.gameclient.gui.hud.nifty;

import de.illonis.eduras.gameclient.userprefs.KeyBindings;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 * Manages keybindings view in settings display.
 * 
 * @author illonis
 * 
 */
public class KeyBindingsViewConverter implements
		ListBoxViewConverter<KeyBinding> {
	private static final String BINDING_DESC = "#description";
	private static final String BINDING_KEY = "#current-key";

	private final KeyBindings bindings;

	public KeyBindingsViewConverter() {
		bindings = EdurasInitializer.getInstance().getSettings()
				.getKeyBindings();
	}

	@Override
	public final void display(final Element listBoxItem, final KeyBinding item) {
		final Element text = listBoxItem.findElementById(BINDING_DESC);
		final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);

		final Element text2 = listBoxItem.findElementById(BINDING_KEY);
		final TextRenderer textRenderer2 = text2
				.getRenderer(TextRenderer.class);

		if (item != null) {
			textRenderer.setText(bindings.getDescription(item));
			textRenderer2.setText(bindings.getBindingString(item));
		} else {
			textRenderer.setText("");
			textRenderer2.setText("");
		}
	}

	@Override
	public final int getWidth(final Element listBoxItem, final KeyBinding item) {
		return 100;
	}
}