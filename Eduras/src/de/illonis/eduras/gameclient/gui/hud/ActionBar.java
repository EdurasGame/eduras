package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.RespawnEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.gameclient.ClientData;
import de.illonis.eduras.gameclient.gui.hud.ActionBarPage.PageNumber;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.units.InteractMode;

/**
 * A bar that can contain multiple "pages" of action buttons for strategy mode.<br>
 * Only a single page is displayed at a time.
 * 
 * @author illonis
 * 
 */
public class ActionBar extends ClickableGuiElement implements TooltipTriggerer {

	private final static Color DISABLED_COLOR = new Color(0, 0, 0, 0.5f);

	private Rectangle bounds;

	private ActionBarPage currentPage;
	private int numButtons;
	private final ClientData data;

	protected ActionBar(UserInterface gui) {
		super(gui);
		data = EdurasInitializer.getInstance().getInformationProvider()
				.getClientData();
		screenX = MiniMap.SIZE + 5;
		bounds = new Rectangle(screenX, screenY, 0, 0);
		registerAsTooltipTriggerer(this);
		setActiveInteractModes(InteractMode.MODE_STRATEGY);
	}

	/**
	 * @return number of current displayed page.
	 */
	public PageNumber getCurrentPage() {
		if (currentPage == null)
			return null;
		return currentPage.getId();
	}

	@Override
	public boolean onClick(Vector2f p) {
		if (currentPage == null)
			return false;
		float x = p.x - screenX;
		if (x < ActionButton.BUTTON_SIZE * numButtons) {
			int button = (int) (x / ActionButton.BUTTON_SIZE);
			ActionButton theButton = currentPage.getButtons().get(button);
			if (!theButton.isAutoCancel()) {
				data.setCurrentActionSelected(button);
			} else {
				data.setCurrentActionSelected(-1);
			}
			theButton.click();
			return true;
		}
		return false;
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void render(Graphics g) {
		if (currentPage == null)
			return;

		float x = screenX;
		for (int i = 0; i < numButtons; i++) {
			ActionButton button = currentPage.getButtons().get(i);
			if (button.getIcon() != null) {
				g.drawImage(button.getIcon(), x, screenY);
				if (!button.isEnabled()) {
					g.setColor(DISABLED_COLOR);
					g.fillRect(x, screenY, ActionButton.BUTTON_SIZE,
							ActionButton.BUTTON_SIZE);
				}
				if (data.getCurrentActionSelected() == i) {
					if (button.isCleared()) {
						data.setCurrentActionSelected(-1);
					} else {
						g.setColor(Color.yellow);
						g.setLineWidth(3f);
						g.drawRect(x, screenY, ActionButton.BUTTON_SIZE - 2,
								ActionButton.BUTTON_SIZE - 2);
					}
				}
			}
			x += ActionButton.BUTTON_SIZE;
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = newHeight - ActionButton.BUTTON_SIZE - 30;
		bounds.setLocation(screenX, screenY);
	}

	/**
	 * Switches to given page.<br>
	 * If the page is currently being shown, it is refreshed.<br>
	 * The previously shown page will receive a {@link ActionBarPage#onHidden()}
	 * while the new one will receive a {@link ActionBarPage#onShown()}, even if
	 * it is the same page.
	 * 
	 * @param page
	 *            the new page.
	 */
	public void setPage(PageNumber page) {
		data.setCurrentActionSelected(-1);
		if (currentPage != null)
			currentPage.onHidden();
		currentPage = ActionBarPage.getPage(page);
		if (currentPage == null)
			return;
		currentPage.onShown();
		numButtons = currentPage.getButtons().size();
		bounds.setWidth(ActionButton.BUTTON_SIZE * numButtons);
		bounds.setHeight(ActionButton.BUTTON_SIZE);
	}

	@Override
	public void onMouseOver(Vector2f p) {
		if (currentPage == null)
			return;
		float x = p.x - screenX;
		if (x < ActionButton.BUTTON_SIZE * numButtons) {
			int button = (int) (x / ActionButton.BUTTON_SIZE);
			getTooltipHandler().showTooltip(p,
					currentPage.getButtons().get(button).getLabel());
		}
	}

	@Override
	public Rectangle getTriggerArea() {
		return bounds;
	}
	
	@Override
	public void onInteractModeChanged(SetInteractModeEvent setModeEvent) {
		data.setCurrentActionSelected(-1);
	}
	
	@Override
	public void onDeath(DeathEvent event) {
		data.setCurrentActionSelected(-1);
	}
	
	@Override
	public void onMatchEnd(MatchEndEvent event) {
		data.setCurrentActionSelected(-1);
	}
}
