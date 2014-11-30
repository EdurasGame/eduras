package de.illonis.eduras.gameclient.gui.hud;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.units.InteractMode;
import de.illonis.eduras.units.Unit;

/**
 * Displays selected units on strategy panel.
 * 
 * @author illonis
 * 
 */
public class SelectedUnitsDisplay extends ClickableGuiElement {

	private final static Logger L = EduLog
			.getLoggerFor(SelectedUnitsDisplay.class.getName());

	private final static int WIDTH = 300;
	private final static int HEALTH_BAR_HEIGHT = 5;
	private final float scaledHealthHeight;
	private final Rectangle bounds;
	private final float buttonSize;
	private int buttonsPerRow = 5;
	private final UserInterface gui;

	protected SelectedUnitsDisplay(UserInterface gui) {
		super(gui);
		this.gui = gui;
		setActiveInteractModes(InteractMode.MODE_STRATEGY);
		bounds = new Rectangle(0, 0, 5, 5);
		buttonSize = ActionButton.BUTTON_SIZE * GameRenderer.getRenderScale();
		scaledHealthHeight = HEALTH_BAR_HEIGHT * GameRenderer.getRenderScale();
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.setLineWidth(5f);
		g.drawLine(screenX - 10, screenY + 2, screenX - 10,
				screenY + bounds.getHeight());
		Set<Integer> selected = getInfo().getClientData().getSelectedUnits();
		int i = 0;
		for (int id : selected) {
			drawSelectionFrameFor(id, g, i++);
		}
	}

	private void drawSelectionFrameFor(int id, Graphics g, int index) {
		GameObject object;
		try {
			object = getInfo().findObjectById(id);
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING,
					"Could not find selected object with id " + id, e);
			return;
		}
		float x = screenX + (buttonSize + 2) * (index % buttonsPerRow);
		float y = screenY + 10 + (index / buttonsPerRow) * (buttonSize + 2);
		try {
			Image icon = ImageCache.getGuiImage(ImageKey.typeToImageKey(object
					.getType()));
			icon.draw(x, y);
		} catch (CacheException e) {
			L.log(Level.SEVERE,
					"Could not find icon for object " + object.getType(), e);
		}
		if (object.isUnit()) {
			Unit unit = (Unit) object;
			float percent = (float) unit.getHealth() / unit.getMaxHealth();
			g.setColor(Color.black);
			g.fillRect(x, y + buttonSize - scaledHealthHeight, buttonSize,
					scaledHealthHeight);
			g.setColor(Color.yellow);
			g.fillRect(x, y + buttonSize - scaledHealthHeight, buttonSize
					* percent, scaledHealthHeight);

		}
	}

	@Override
	public boolean init(Graphics g, int windowWidth, int windowHeight) {
		float height = MiniMap.SIZE * GameRenderer.getRenderScale();
		float width = WIDTH * GameRenderer.getRenderScale();
		screenX = windowWidth - width;
		screenY = windowHeight - height;
		buttonsPerRow = (int) Math.floor((width - 10) / (buttonSize + 2));
		bounds.setBounds(screenX, screenY, width, height);
		return true;
	}

	private int pointToUnit(int x, int y) {
		int xIndex = (int) Math.floor((x - screenX) / (buttonSize + 2));
		int yIndex = (int) Math.floor((y - screenY) / (buttonSize + 2));
		int index = xIndex + buttonsPerRow * yIndex;
		int i = 0;
		for (int id : getInfo().getClientData().getSelectedUnits()) {
			if (i == index)
				return id;
			i++;
		}
		return -1;
	}

	@Override
	public boolean mouseMoved(int oldx, int oldy, int newx, int newy) {
		int unit = pointToUnit(newx, newy);
		if (unit == -1) {
			getTooltipHandler().hideTooltip();
			return false;
		}
		GameObject o;
		try {
			o = getInfo().findObjectById(unit);

			getTooltipHandler().showTooltip(new Vector2f(newx, newy),
					o.getType().name() + " " + o.getId());
			return true;
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING,
					"Could not find selected object while showing tooltip", e);
		}
		return false;
	}

	@Override
	public boolean mouseReleased(int button, int x, int y) {
		int unit = pointToUnit(x, y);
		if (unit == -1)
			return false;
		if (button == Input.MOUSE_LEFT_BUTTON) {
			if (gui.getLogic().isKeyDown(Input.KEY_LCONTROL)) {
				getInfo().getClientData().getSelectedUnits().remove(unit);
			} else
				getInfo().getClientData().setSelectedUnit(unit);
			return true;
		}
		return false;
	}
}
