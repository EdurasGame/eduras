package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Image;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.GameEventAdapter;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;

/**
 * A button on action bar.
 * 
 * @author illonis
 * 
 */
public abstract class ActionButton extends GameEventAdapter {

	private final static Logger L = EduLog.getLoggerFor(ActionButton.class
			.getName());

	/**
	 * Size of a single button.
	 */
	public final static int BUTTON_SIZE = 36;

	private boolean enabled;
	protected final GamePanelReactor reactor;
	private ImageKey imageKey;
	private Image icon;
	private boolean autoCancel;
	private boolean clearSelection;
	protected String label;
	protected String description;

	/**
	 * Creates a new action button.
	 * 
	 * @param image
	 *            the key for the image used to display on this button.
	 * @param reactor
	 *            the reactor.
	 */
	public ActionButton(ImageKey image, GamePanelReactor reactor) {
		this.reactor = reactor;
		this.imageKey = image;
		enabled = true;
		autoCancel = false;
		label = "";
		description = "";
	}

	protected final void clearSelection() {
		clearSelection = true;
	}

	final boolean isAutoCancel() {
		return autoCancel;
	}

	protected final void setAutoCancel(boolean autoCancel) {
		this.autoCancel = autoCancel;
	}

	/**
	 * Action that is performed when user triggers this button.
	 */
	protected abstract void actionPerformed();

	protected final Image getIcon() {
		try {
			icon = ImageCache.getGuiImage(imageKey);
		} catch (CacheException e) {
			L.log(Level.WARNING, "Could not find image for actionbutton \""
					+ imageKey.toString() + "\".", e);
		}
		return icon;
	}

	/**
	 * @return the display text.
	 */
	public final String getLabel() {
		return label;
	}

	/**
	 * @return the description.
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * @return the costs.
	 */
	public abstract int getCosts();

	/**
	 * @return true if this button is enabled.
	 */
	public final boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets enabled state. A disabled button is greyed and cannot be clicked.
	 * 
	 * @param enabled
	 *            new state.
	 */
	public final void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Clicks this button. Will do nothing when button is disabled.
	 */
	public final void click() {
		if (enabled)
			actionPerformed();
	}

	final boolean isCleared() {
		boolean selected = clearSelection;
		clearSelection = false;
		return selected;
	}
}
