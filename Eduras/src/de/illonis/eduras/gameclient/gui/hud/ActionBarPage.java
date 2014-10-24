package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.gameclient.ClientData;
import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.gameclient.userprefs.KeyBindings;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.units.InteractMode;

/**
 * A page that can contain multiple buttons and is displayed on
 * {@link ActionBar}.
 * 
 * @author illonis
 * 
 */
public class ActionBarPage extends ClickableGuiElement implements
		TooltipTriggerer {
	private final static Logger L = EduLog.getLoggerFor(ActionBarPage.class
			.getName());
	private final static Color DISABLED_COLOR = new Color(0, 0, 0, 0.5f);
	private final static Color UNAVAILABLE_COLOR = new Color(1f, 0, 0, 0.4f);

	private final LinkedList<ActionButton> buttons;
	private final Rectangle bounds;
	protected final GamePanelReactor reactor;
	private final int index;
	private final KeyBindings bindings;
	private Image resIcon;
	private final ClientData data;
	private final float buttonSize;
	private boolean activePage;
	private final ActionBar bar;

	/**
	 * Creates a new actionbar.
	 * 
	 * @param index
	 *            index.
	 * 
	 * @param gui
	 *            the user interface.
	 * @param reactor
	 *            the reactor.
	 * 
	 */
	public ActionBarPage(int index, UserInterface gui, GamePanelReactor reactor) {
		super(gui);
		bar = gui.getActionBar();
		this.index = index;
		this.reactor = reactor;
		buttons = new LinkedList<ActionButton>();
		buttonSize = (float) ActionButton.BUTTON_SIZE
				* GameRenderer.getRenderScale();

		bindings = EdurasInitializer.getInstance().getSettings()
				.getKeyBindings();
		data = EdurasInitializer.getInstance().getInformationProvider()
				.getClientData();
		bounds = new Rectangle(0, 0, 1, buttonSize);
		registerAsTooltipTriggerer(this);
		setActiveInteractModes(InteractMode.MODE_STRATEGY);
		activePage = false;
	}

	protected void updateBounds() {
		bounds.setBounds(screenX, screenY, buttonSize * buttons.size(),
				buttonSize);
	}

	public void setActivePage(boolean active) {
		this.activePage = active;
	}

	/**
	 * Adds a button to this page.
	 * 
	 * @param button
	 *            the button to add.
	 */
	public final void addButton(ActionButton button) {
		buttons.add(button);
	}

	/**
	 * Removes all buttons from this page.
	 */
	public void removeAllButtons() {
		buttons.clear();
	}

	/**
	 * Removes a button from the page.
	 * 
	 * @param buttonToRemove
	 */
	public void removeButton(ActionButton buttonToRemove) {
		buttons.remove(buttonToRemove);
	}

	/**
	 * @return a list of all contained buttons.
	 */
	public final LinkedList<ActionButton> getButtons() {
		return buttons;
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void render(Graphics g) {
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
		if (activePage) {
			g.setColor(Color.black);
		} else {
			g.setColor(DISABLED_COLOR);
		}

		Rectangle rect = new Rectangle(x, screenY, buttonSize, buttonSize);
		for (int i = 0; i < buttons.size(); i++) {
			rect.setX(x);
			ActionButton button = buttons.get(i);
			if (button.getIcon() != null) {
				g.drawImage(button.getIcon(), x, screenY);
				if (!activePage) {
					g.setColor(DISABLED_COLOR);
					g.fill(rect);
				}
				if (activePage && data.getCurrentActionSelected() == i) {
					if (button.isCleared()) {
						data.setCurrentActionSelected(-1);
					} else {
						g.setColor(Color.yellow);
						g.setLineWidth(2f);
						g.drawRect(x, screenY, buttonSize - 2, buttonSize - 2);
					}
				}
				if (!button.isEnabled() || button.getCosts() > resources) {
					g.setColor(UNAVAILABLE_COLOR);
					g.fill(rect);
				}

				if (activePage) {
					label = "";
					label = bindings.getBindingString(KeyBinding
							.valueOf("STRATEGY_" + (1 + i)));
					g.setColor(DISABLED_COLOR);

					int w = font.getWidth(label);
					int h = font.getHeight(label);
					int bgSize = Math.max(w, h) + 1;
					g.fillRect(x + 2, screenY + 2, bgSize, bgSize);
					font.drawString(x + 2 + (bgSize - w) / 2, screenY + 2,
							label, Color.white);
				}

			}
			x += buttonSize;
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		if (resIcon == null) {
			try {
				resIcon = ImageCache.getGuiImage(ImageKey.RESOURCE_ICON_SMALL);
			} catch (CacheException e) {
			}
		}
		float scale = GameRenderer.getRenderScale();
		screenX = MiniMap.SIZE * scale + buttonSize;
		screenY = 18 + newHeight - StrategyPanel.HEIGHT * scale + buttonSize
				* index;

		updateBounds();
	}

	@Override
	public void onMouseOver(Vector2f p) {
		float x = p.x - screenX;
		if (x < buttonSize * buttons.size()) {
			int button = (int) (x / buttonSize);
			getTooltipHandler().showTooltip(p, buttons.get(button).getLabel(),
					buttons.get(button).getDescription(),
					buttons.get(button).getCosts());
		}
	}

	@Override
	public boolean mouseReleased(int button, int x, int y) {
		if (!activePage) {
			bar.setPage(index);
		}
		float mouseX = x - screenX;
		if (mouseX < buttons.size() * buttonSize) {
			int actionButton = (int) (mouseX / buttonSize);
			selectButton(actionButton);
			return true;
		}
		return false;
	}

	public void selectButton(int index) {
		if (index >= buttons.size()) {
			data.setCurrentActionSelected(-1);
			return;
		}
		ActionButton theButton = buttons.get(index);
		if (!theButton.isAutoCancel()) {
			data.setCurrentActionSelected(index);
		} else {
			data.setCurrentActionSelected(-1);
		}
		theButton.click();
	}

	@Override
	public Rectangle getTriggerArea() {
		return bounds;
	}
}
