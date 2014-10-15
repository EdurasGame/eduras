package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
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
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
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
	/**
	 * Distance from screen bottom to action bar.
	 */
	public final static int YOFFSET = 50;

	private Rectangle bounds;

	private ActionBarPage currentPage;
	private int numButtons;
	private final ClientData data;
	private KeyBindings bindings;
	private Image resIcon;
	private final float buttonSize;

	protected ActionBar(UserInterface gui, MiniMap map) {
		super(gui);
		buttonSize = (float) ActionButton.BUTTON_SIZE
				* GameRenderer.getRenderScale();
		data = EdurasInitializer.getInstance().getInformationProvider()
				.getClientData();
		bindings = EdurasInitializer.getInstance().getSettings()
				.getKeyBindings();
		screenX = map.getSize() + 5;
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
	public boolean mouseReleased(int button, int x, int y) {
		if (currentPage == null)
			return false;
		float mouseX = x - screenX;
		if (mouseX < buttonSize * numButtons) {
			int actionButton = (int) (mouseX / buttonSize);
			selectButton(actionButton);
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
		Font font = FontCache.getFont(FontKey.SMALL_FONT, g);
		int resources = 0;
		try {
			resources = getInfo().getPlayer().getTeam().getResource();
		} catch (ObjectNotFoundException e) {
			return;
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
		g.fillRect(x, screenY, numButtons * buttonSize, buttonSize + 15);
		for (int i = 0; i < numButtons; i++) {
			ActionButton button = currentPage.getButtons().get(i);
			if (button.getIcon() != null) {
				g.drawImage(button.getIcon(), x, screenY);
				if (!button.isEnabled()) {
					g.setColor(DISABLED_COLOR);
					g.fillRect(x, screenY, buttonSize, buttonSize);
				}
				if (data.getCurrentActionSelected() == i) {
					if (button.isCleared()) {
						data.setCurrentActionSelected(-1);
					} else {
						g.setColor(Color.yellow);
						g.setLineWidth(3f);
						g.drawRect(x, screenY, buttonSize - 2, buttonSize - 2);
					}
				}

				if (button.isBackButton())
					label = bindings.getBindingString(KeyBinding.PAGE_UP);
				else {
					label = "";
					label = bindings.getBindingString(KeyBinding
							.valueOf("STRATEGY_" + (1 + i)));
					if (button.getCosts() > 0) {
						if (resIcon != null)
							g.drawImage(
									resIcon,
									x,
									screenY
											+ buttonSize
											+ (font.getLineHeight() - resIcon
													.getHeight()) / 2);
						font.drawString(x + resIcon.getWidth() + 3, screenY
								+ buttonSize, button.getCosts() + "", (button
								.getCosts() > resources) ? Color.red
								: Color.green);
					}
				}
				g.setColor(DISABLED_COLOR);
				int w = font.getWidth(label);
				int h = font.getHeight(label);
				int bgSize = Math.max(w, h) + 1;
				g.fillRect(x + 2, screenY + 2, bgSize, bgSize);
				font.drawString(x + 2 + (bgSize - w) / 2, screenY + 2, label,
						Color.white);
			}
			x += buttonSize;
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = newHeight - buttonSize - YOFFSET;
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
		bounds.setWidth(buttonSize * numButtons);
		bounds.setHeight(buttonSize);
	}

	@Override
	public void onMouseOver(Vector2f p) {
		if (currentPage == null)
			return;
		float x = p.x - screenX;
		if (x < buttonSize * numButtons) {
			int button = (int) (x / buttonSize);
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
		if (setModeEvent.getOwner() == getInfo().getOwnerID()) {
			data.setCurrentActionSelected(-1);
			setPage(PageNumber.MAIN);
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
