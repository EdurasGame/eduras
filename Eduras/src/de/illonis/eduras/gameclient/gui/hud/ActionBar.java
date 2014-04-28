package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Logger;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.gui.hud.ActionBarPage.PageNumber;
import de.illonis.eduras.units.PlayerMainFigure.InteractMode;

/**
 * A bar that can contain multiple "pages" of action buttons for strategy mode.
 * 
 * @author illonis
 * 
 */
public class ActionBar extends ClickableGuiElement implements TooltipTriggerer {

	private Rectangle bounds;

	private ActionBarPage currentPage;
	private int numButtons;

	protected ActionBar(UserInterface gui) {
		super(gui);
		screenX = MiniMap.SIZE + 5;
		bounds = new Rectangle(screenX, screenY, 0, 0);
		registerAsTooltipTriggerer(this);
		setActiveInteractModes(InteractMode.MODE_STRATEGY);
	}

	@Override
	public boolean onClick(Vector2f p) {
		if (currentPage == null)
			return false;
		float x = p.x - screenX;
		if (x < ActionButton.BUTTON_SIZE * numButtons) {
			int button = (int) (x / ActionButton.BUTTON_SIZE);
			currentPage.getButtons().get(button).actionPerformed();
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

		for (int i = 0; i < numButtons; i++) {
			ActionButton button = currentPage.getButtons().get(i);
			if (button.getIcon() != null)
				g.drawImage(button.getIcon(), screenX + i
						* ActionButton.BUTTON_SIZE, screenY);
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = newHeight - ActionButton.BUTTON_SIZE;
		bounds.setLocation(screenX, screenY);
	}

	@Override
	public void onPlayerInformationReceived() {
	}

	/**
	 * Switches to given page.
	 * 
	 * @param page
	 *            the new page.
	 */
	public void setPage(PageNumber page) {
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
}
