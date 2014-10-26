package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.gameclient.ClientData;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.gameclient.gui.hud.actionbar.ItemPage;
import de.illonis.eduras.gameclient.gui.hud.actionbar.SpellPage;
import de.illonis.eduras.gameclient.gui.hud.actionbar.UnitPage;
import de.illonis.eduras.gameclient.gui.hud.actionbar.UnitSpellPage;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.math.BasicMath;
import de.illonis.eduras.units.InteractMode;

/**
 * A bar that can contain multiple "pages" of action buttons for strategy mode.<br>
 * Only a single page is displayed at a time.
 * 
 * @author illonis
 * 
 */
public class ActionBar extends RenderedGuiObject {

	private final static Logger L = EduLog.getLoggerFor(ActionBar.class
			.getName());

	/**
	 * Distance from screen bottom to action bar.
	 */
	public final static int YOFFSET = 50;
	private final List<ActionBarPage> pages;

	private int currentPage;
	private final ClientData data;
	private int maxButtons;
	private float buttonSize;
	private final KeyBinding[] bindingOrder;
	private final KeyBinding[] pagesBindings;

	protected ActionBar(UserInterface gui, GamePanelReactor guiReactor) {
		super(gui);
		pages = new LinkedList<ActionBarPage>();
		data = EdurasInitializer.getInstance().getInformationProvider()
				.getClientData();
		buttonSize = ActionButton.BUTTON_SIZE * GameRenderer.getRenderScale();
		screenX = MiniMap.SIZE * GameRenderer.getRenderScale() + buttonSize
				* 1.5f;

		setActiveInteractModes(InteractMode.MODE_STRATEGY);
		currentPage = 0;
		maxButtons = 0;
		bindingOrder = new KeyBinding[] { KeyBinding.ITEM_ASSAULT,
				KeyBinding.ITEM_SWORD, KeyBinding.ITEM_SIMPLE,
				KeyBinding.ITEM_SNIPER, KeyBinding.ITEM_ROCKET,
				KeyBinding.ITEM_SPLASH, KeyBinding.ITEM_MINE };
		pagesBindings = new KeyBinding[] { KeyBinding.ACTIONBAR_PAGE_WEAPONS,
				KeyBinding.ACTIONBAR_PAGE_PLAYERSPELLS,
				KeyBinding.ACTIONBAR_PAGE_UNITS,
				KeyBinding.ACTIONBAR_PAGE_SPELLS };
	}

	@Override
	public void render(Graphics g) {
		Font font = FontCache.getFont(FontKey.SMALL_FONT, g);
		for (int i = 0; i < maxButtons; i++) {
			String binding = EdurasInitializer.getInstance().getSettings()
					.getKeyBindings().getBindingString(bindingOrder[i]);
			font.drawString(screenX + buttonSize * i, 2 + screenY, binding,
					Color.yellow);
		}
		for (int i = 0; i < 4; i++) {
			String binding = EdurasInitializer.getInstance().getSettings()
					.getKeyBindings().getBindingString(pagesBindings[i]);
			font.drawString(screenX - buttonSize, 10 + buttonSize / 2 + screenY
					+ i * buttonSize, binding, Color.yellow);
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = newHeight - MiniMap.SIZE * GameRenderer.getRenderScale();
	}

	/**
	 * Switches to given page.
	 * 
	 * @param index
	 *            the new page.
	 */
	public void setPage(int index) {
		pages.get(currentPage).setActivePage(false);
		currentPage = index;
		data.setCurrentActionSelected(-1);
		pages.get(index).setActivePage(true);
	}

	@Override
	public void onInteractModeChanged(SetInteractModeEvent setModeEvent) {
		if (setModeEvent.getOwner() == getInfo().getOwnerID()) {
			data.setCurrentActionSelected(-1);
			setPage(0);
		}
	}

	@Override
	public void onDeath(DeathEvent event) {
		data.setCurrentActionSelected(-1);
	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {
		data.setCurrentActionSelected(-1);
	}

	public ActionBarPage getCurrentPage() {
		return pages.get(currentPage);
	}

	public void nextPage() {
		setPage((currentPage + 1) % pages.size());
	}

	public void prevPage() {
		setPage(BasicMath.calcModulo(currentPage - 1, pages.size()));
	}

	public void initPages(UserInterface gui, GamePanelReactor guiReactor) {
		pages.add(new ItemPage(0, gui, guiReactor));
		pages.add(new UnitSpellPage(1, gui, guiReactor));
		pages.add(new UnitPage(2, gui, guiReactor));
		pages.add(new SpellPage(3, gui, guiReactor));
		setPage(0);
		for (ActionBarPage page : pages) {
			maxButtons = Math.max(page.getButtons().size(), maxButtons);
		}
	}

}
