package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import de.illonis.eduras.Player;
import de.illonis.eduras.events.RespawnEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.units.InteractMode;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

/**
 * A basic player frame without any interaction.
 * 
 * @author illonis
 * 
 */
public abstract class PlayerDisplay extends ClickableGuiElement {

	private final static Color COLOR_BAR = Color.yellow;
	private final static Color COLOR_BG = Color.black;
	private final static Color COLOR_TEXT = Color.black;
	public final static int PLAYER_BAR_WIDTH = 60;

	private int health;
	private int maxHealth;
	private int barWidth;
	protected final Player player;
	private Rectangle bounds;
	protected final float statBarSize;

	protected PlayerDisplay(UserInterface ui, Player player) {
		super(ui);
		this.player = player;
		health = maxHealth = 10;
		screenX = 5;
		bounds = new Rectangle(0, 0, 10, 10);
		setActiveInteractModes(InteractMode.MODE_STRATEGY);
		statBarSize = PLAYER_BAR_WIDTH * GameRenderer.getRenderScale();
		PlayerMainFigure mainFigure = player.getPlayerMainFigure();
		if (mainFigure != null) {
			maxHealth = mainFigure.getMaxHealth();
			health = mainFigure.getHealth();
		} else {
			health = 0;
		}
		recalculate();
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public void render(Graphics g2d) {
		Font font = FontCache.getFont(FontKey.DEFAULT_FONT, g2d);
		g2d.setLineWidth(1f);

		bounds = new Rectangle(screenX, screenY, statBarSize,
				font.getLineHeight() * 2);
		// fill black
		g2d.setColor(COLOR_BG);
		g2d.fill(bounds);
		// draw frame
		g2d.setColor(Color.white);
		g2d.draw(bounds);

		font.drawString(screenX + 5, screenY, player.getName(), Color.white);
		// g2d.setColor(COLOR_BG);
		// g2d.fillRect(screenX, yPosition + statBarSize / 3, statBarSize,
		// statBarSize - statBarSize / 3);
		g2d.setColor(COLOR_BAR);
		g2d.fillRect(screenX, screenY + font.getLineHeight(), barWidth,
				font.getLineHeight());
		font.drawString(screenX + 5, screenY + font.getLineHeight(), health
				+ " / " + maxHealth, COLOR_TEXT);
		if (player.isDead()
				&& player.equals(getInfo().getClientData()
						.getCurrentResurrectTarget())) {
			g2d.setColor(Color.red);
			g2d.setLineWidth(3f);
			g2d.draw(bounds);
		}
	}

	@Override
	public void onHealthChanged(Unit unit, int oldValue, int newValue) {
		PlayerMainFigure mainFigure = player.getPlayerMainFigure();
		if (mainFigure != null && unit.equals(mainFigure)) {

			health = newValue;
			maxHealth = mainFigure.getMaxHealth();
			recalculate();
		}
	}

	@Override
	public void onRespawn(RespawnEvent event) {
		if (event.getOwner() == player.getPlayerId()) {
			onHealthChanged(player.getPlayerMainFigure(), 0, player
					.getPlayerMainFigure().getHealth());
		}
	}

	@Override
	public void onMaxHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		PlayerMainFigure mainFigure = player.getPlayerMainFigure();
		if (event.getObjectId() == mainFigure.getId()) {
			maxHealth = event.getNewValue();
			recalculate();
		}
	}

	private void recalculate() {
		float percent = (float) health / maxHealth;
		barWidth = Math.round(percent * statBarSize);
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}
}
