package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.newdawn.slick.Graphics;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.gameclient.ClientData;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.gameclient.gui.hud.actionbar.ItemPage;
import de.illonis.eduras.gameclient.gui.hud.actionbar.ResurrectPage;
import de.illonis.eduras.gameclient.gui.hud.actionbar.SpellPage;
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

	protected ActionBar(UserInterface gui, GamePanelReactor guiReactor) {
		super(gui);
		pages = new LinkedList<ActionBarPage>();
		data = EdurasInitializer.getInstance().getInformationProvider()
				.getClientData();
		screenX = MiniMap.SIZE * GameRenderer.getRenderScale() + 5;
		setActiveInteractModes(InteractMode.MODE_STRATEGY);
		currentPage = 0;
	}

	@Override
	public void render(Graphics g) {
		if (currentPage == -1)
			return;

	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
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
		pages.add(new SpellPage(1, gui, guiReactor));
		pages.add(new ResurrectPage(2, gui, guiReactor));
		setPage(0);
	}

}
