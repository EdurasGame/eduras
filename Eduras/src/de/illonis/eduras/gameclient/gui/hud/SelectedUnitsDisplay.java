package de.illonis.eduras.gameclient.gui.hud;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.units.InteractMode;

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
	private final Rectangle bounds;

	protected SelectedUnitsDisplay(UserInterface gui) {
		super(gui);
		setActiveInteractModes(InteractMode.MODE_STRATEGY);
		bounds = new Rectangle(0, 0, 5, 5);
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void render(Graphics g) {
		Set<Integer> selected = getInfo().getClientData().getSelectedUnits();
		FontCache.getFont(FontKey.TOOLTIP_FONT, g).drawString(screenX, screenY,
				selected.size() + " object(s) selected.", Color.yellow);
		int i = 1;
		for (int id : selected) {
			drawSelectionFrameFor(id, g, i++);
		}
	}

	private void drawSelectionFrameFor(int id, Graphics g, int index) {
		Font font = FontCache.getFont(FontKey.TOOLTIP_FONT, g);
		GameObject object;
		try {
			object = getInfo().findObjectById(id);
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING,
					"Could not find selected object with id " + id, e);
			return;
		}
		font.drawString(screenX, screenY + index * font.getLineHeight(), object
				.getType().name() + " (" + id + ")", Color.white);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		float scale = GameRenderer.getRenderScale();
		float height = StrategyPanel.HEIGHT * scale;
		float width = WIDTH * scale;
		screenX = newWidth - width;
		screenY = newHeight - height;
		bounds.setBounds(screenX, screenY, width, height);
	}
}
