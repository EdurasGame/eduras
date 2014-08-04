package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.gameclient.ClientData;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.gameclient.gui.hud.ActionBarPage.PageNumber;
import de.illonis.eduras.gameclient.userprefs.KeyBindings;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
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

	private final static Logger L = EduLog.getLoggerFor(ActionBar.class
			.getName());

	private final static Color DISABLED_COLOR = new Color(0, 0, 0, 0.5f);

	private Rectangle bounds;

	private ActionBarPage currentPage;
	private int numButtons;
	private final ClientData data;
	private KeyBindings bindings;
	private Image resIcon;

	protected ActionBar(UserInterface gui) {
		super(gui);
		data = EdurasInitializer.getInstance().getInformationProvider()
				.getClientData();
		bindings = EdurasInitializer.getInstance().getSettings()
				.getKeyBindings();
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
			selectButton(button);
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
		int resources = 0;
		try {
			resources = getInfo().getPlayer().getTeam().getResource();
		} catch (ObjectNotFoundException e) {
		} catch (PlayerHasNoTeamException e) {
			L.log(Level.SEVERE, "Cannot find team!!", e);
		}
		if (resIcon == null) {
			try {
				resIcon = ImageCache.getGuiImage(ImageKey.RESOURCE_ICON_SMALL);
			} catch (CacheException e) {
			}
		}
		float x = screenX;
		String label;
		g.setColor(Color.black);
		g.fillRect(x, screenY, numButtons * ActionButton.BUTTON_SIZE,
				ActionButton.BUTTON_SIZE + 15);
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

				if (button.isBackButton())
					label = bindings.getBindingString(KeyBinding.PAGE_UP);
				else {
					label = bindings.getBindingString(KeyBinding
							.valueOf("ITEM_" + (1 + i)));
					if (button.getCosts() > 0) {
						if (button.getCosts() > resources) {
							g.setColor(Color.red);
						} else {
							g.setColor(Color.green);
						}
						if (resIcon != null)
							g.drawImage(resIcon, x, screenY
									+ ActionButton.BUTTON_SIZE);
						g.drawString(button.getCosts() + "", x + 15, screenY
								+ ActionButton.BUTTON_SIZE);
					}
				}
				g.setColor(Color.black);
				g.fillRect(x + 3, screenY + 3, 15, 15);
				g.setColor(Color.white);
				g.drawString(label, x + 6, screenY + 3);
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

	/**
	 * Selects a button on action bar.
	 * 
	 * @param i
	 *            the button index. if out of bounds, nothing will happen.
	 */
	public void selectButton(int i) {
		if (i >= currentPage.getButtons().size())
			return;
		ActionButton theButton = currentPage.getButtons().get(i);
		if (!theButton.isAutoCancel()) {
			data.setCurrentActionSelected(i);
		} else {
			data.setCurrentActionSelected(-1);
		}
		theButton.click();
	}
}
