package de.illonis.eduras.gameclient.gui.hud;

import java.util.HashMap;
import java.util.LinkedList;

import de.illonis.eduras.gameclient.GameEventAdapter;

/**
 * A page that can contain multiple buttons and is displayed on
 * {@link ActionBar}.
 * 
 * @author illonis
 * 
 */
public class ActionBarPage extends GameEventAdapter {

	private final static HashMap<PageNumber, ActionBarPage> pages = new HashMap<PageNumber, ActionBarPage>();

	private final PageNumber id;

	/**
	 * Identifies a page.
	 */
	@SuppressWarnings("javadoc")
	public enum PageNumber {
		MAIN, RESURRECT, HEAL;
	}

	/**
	 * Retrieves the action bar page for given page number.
	 * 
	 * @param number
	 *            the page to look for.
	 * @return the page, null if no page has given identifier.
	 */
	public static ActionBarPage getPage(PageNumber number) {
		return pages.get(number);
	}

	private final LinkedList<ActionButton> buttons;

	/**
	 * Creates a new actionbar with given identifier.<br>
	 * Note that any existing page with the same identifier will be dropped.
	 * 
	 * @param id
	 *            the identifier for this page.
	 */
	public ActionBarPage(PageNumber id) {
		buttons = new LinkedList<ActionButton>();
		this.id = id;
		pages.put(id, this);
	}

	/**
	 * @return the id of this page.
	 */
	public final PageNumber getId() {
		return id;
	}

	/**
	 * Adds a button to this page.<br>
	 * If this page is currently shown, you need to manually re-show this page
	 * using {@link ActionBar#setPage(PageNumber)}
	 * 
	 * @param button
	 *            the button to add.
	 */
	public final void addButton(ActionButton button) {
		buttons.add(button);
	}

	/**
	 * @return a list of all contained buttons.
	 */
	public final LinkedList<ActionButton> getButtons() {
		return buttons;
	}

	/**
	 * Called when this page is shown.
	 */
	public void onShown() {
	}

	/**
	 * Called when this page is hidden.
	 */
	public void onHidden() {
	}
}
