package de.illonis.eduras.gameclient.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import de.illonis.eduras.locale.Localization;

/**
 * A loading tip that is shown in loading screen. Also provides static random
 * access to all loading tips.
 * 
 * @author illonis
 * 
 */
public final class LoadingTip {

	private final static Random random = new Random(System.currentTimeMillis());
	private final static List<LoadingTip> tips = new LinkedList<LoadingTip>();
	static {
		new LoadingTip("Client.gui.loadingtip.strategyscroll");
		new LoadingTip("Client.gui.loadingtip.unitdeselect");
		new LoadingTip("Client.gui.loadingtip.baseoccupy");
	}

	/**
	 * @return a random tip.
	 */
	public static LoadingTip getRandomTip() {
		if (tips.size() == 0)
			return null;
		return tips.get(random.nextInt(tips.size()));
	}

	private String localeId;

	/**
	 * Creates a new loading tip and adds it to the list of all loading tips.
	 * 
	 * @param localeId
	 *            the identifier for localized text.
	 */
	public LoadingTip(String localeId) {
		this.localeId = localeId;
		tips.add(this);
	}

	/**
	 * @return the localized loading tip text.
	 */
	public String getText() {
		return Localization.getString(localeId);
	}

}
