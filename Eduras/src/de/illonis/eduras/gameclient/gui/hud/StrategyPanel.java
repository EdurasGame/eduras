package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.units.InteractMode;

/**
 * The background of the strategy panel that block click-through.
 * 
 * @author illonis
 * 
 */
public class StrategyPanel extends ClickableGuiElement {

	private final static Logger L = EduLog.getLoggerFor(StrategyPanel.class
			.getName());
	/**
	 * Height of strategy panel at scale = 1.
	 */
	public final static int HEIGHT = 130;
	private float scaledWidth = 0;
	private float scaledHeight = 0;
	private final Rectangle bounds;

	protected StrategyPanel(UserInterface gui) {
		super(gui);
		setActiveInteractModes(InteractMode.MODE_STRATEGY);
		scaledHeight = HEIGHT * GameRenderer.getRenderScale();
		screenX = 0;
		scaledWidth = 100;
		zIndex = -1;
		bounds = new Rectangle(screenX, screenY, scaledWidth, scaledHeight);
	}

	@Override
	public void render(Graphics g) {
		try {
			Image image = ImageCache.getGuiImage(ImageKey.STRATEGY_BAR);
			g.drawImage(image, screenX, screenY);
		} catch (CacheException e) {
			g.setColor(Color.green);
			g.fillRect(screenX, screenY, scaledWidth, scaledHeight);
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = newHeight - GameRenderer.getRenderScale() * HEIGHT;
		scaledWidth = newWidth;
		bounds.setBounds(screenX, screenY, scaledWidth, scaledHeight);
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public boolean mouseReleased(int button, int x, int y) {
		return true;
	}

	@Override
	public boolean mouseDragged(int oldx, int oldy, int newx, int newy) {
		return true;
	}

	@Override
	public boolean mouseClicked(int button, int x, int y, int clickCount) {
		return true;
	}

	@Override
	public boolean mousePressed(int button, int x, int y) {
		return true;
	}
}
