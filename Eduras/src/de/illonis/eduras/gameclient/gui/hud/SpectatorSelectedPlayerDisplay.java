package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.items.Usable;
import de.illonis.eduras.items.weapons.Weapon;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.InteractMode;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * Shows player details for selected player in spectator mode.
 * 
 * @author illonis
 * 
 */
public class SpectatorSelectedPlayerDisplay extends RenderedGuiObject {
	private final static Color COLOR_SEMITRANSPARENT = new Color(0, 0, 0, 120);
	private final static Logger L = EduLog
			.getLoggerFor(SpectatorSelectedPlayerDisplay.class.getName());
	private final static int ITEMS_PER_ROW = 6;
	private final static int MAX_ROWS = 2;
	private final static int HEALTHBAR_HEIGHT = 20;

	private Font font;
	private float size;
	private float width;
	private float height;
	private float healthBarHeight;

	protected SpectatorSelectedPlayerDisplay(UserInterface gui) {
		super(gui);
		visibleForSpectator = true;
		setActiveInteractModes(InteractMode.MODE_SPECTATOR);
	}

	@Override
	public boolean isEnabledInGameMode(GameMode gameMode) {
		return GameMode.GameModeNumber.EDURA == gameMode.getNumber();
	}

	private void renderCooldown(Graphics g, float xPos, float yPos, long value,
			long total) {
		if (value > 0) {
			float cooldownPercent = total == 0 ? 0 : (float) value / total;
			g.setColor(COLOR_SEMITRANSPARENT);
			g.fillArc(xPos, yPos, size, size, -90 - cooldownPercent * 360, -90);
		}
	}

	@Override
	public void render(Graphics g) {
		if (getInfo().getClientData().getSelectedUnits().size() > 0) {
			int unitId = getInfo().getClientData().getSelectedUnits()
					.iterator().next();
			try {
				GameObject o = getInfo().findObjectById(unitId);
				if (!(o instanceof PlayerMainFigure)) {
					System.out.println(o.getClass().getSimpleName());
					return;
				}
				g.setColor(Color.darkGray);

				g.fillRect(screenX, screenY, width, height);
				PlayerMainFigure player = (PlayerMainFigure) o;
				font.drawString(screenX, screenY, player.getPlayer().getName(),
						Color.white);
				int i = 1;
				float healthPartWidth = ((float) player.getHealth() / player
						.getMaxHealth()) * width;
				g.setColor(Color.black);
				g.fillRect(screenX, screenY, width, healthBarHeight);
				g.setColor(Color.yellow);
				g.fillRect(screenX, screenY, healthPartWidth, healthBarHeight);
				font.drawString(screenX + 5, screenY, player.getPlayer()
						.getName(), Color.black);
				try {
					g.drawImage(
							ImageCache.getGuiImage(ImageKey.ACTION_SPELL_BLINK),
							screenX + 1, screenY + healthBarHeight + 5);

				} catch (CacheException e) {

				}
				renderCooldown(g, screenX + 1, screenY + healthBarHeight + 5,
						player.getPlayer().getBlinkCooldown(),
						S.Server.sv_blink_cooldown);
				font.drawString(
						screenX
								+ (size - font.getWidth(player.getPlayer()
										.getBlinksAvailable() + "")) / 2,
						screenY + healthBarHeight + 5
								+ (size - font.getLineHeight()) / 2, player
								.getPlayer().getBlinksAvailable() + "",
						Color.white);
				for (Item item : player.getPlayer().getInventory()
						.getAllItems()) {

					if (item != null) {
						int col = i % ITEMS_PER_ROW;
						int row = i / ITEMS_PER_ROW;
						float x = screenX + col * (size + 1) + 1;
						float y = screenY + row * (size + 1) + healthBarHeight
								+ 5;
						try {
							g.drawImage(
									ImageCache.getInventoryIcon(item.getType()),
									x, y);
							if (item.isUsable()) {
								Usable u = (Usable) item;
								renderCooldown(g, x, y, u.getCooldown(),
										u.getCooldownTime());
								if (item instanceof Weapon) {
									Weapon w = (Weapon) item;
									if (w.getMaxAmmunition() > 0)
										font.drawString(x + 3, y + 1,
												w.getCurrentAmmunition() + "");
								}
							}
						} catch (CacheException e) {
							L.log(Level.WARNING,
									"Item icon for " + item.getType()
											+ " not cached", e);
						}
						i++;
					}
				}
			} catch (ObjectNotFoundException e) {
				L.log(Level.WARNING,
						"Could not find selected object while showing selected player.",
						e);
			}
		}
	}

	@Override
	public void onPlayerBlinked(int owner) {
		try {
			Player p = getInfo().getPlayerByOwnerId(owner);
			// trigger blink cooldown to track it
			p.useBlink();
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Blinked player not found.", e);
		}
	}

	@Override
	public boolean init(Graphics g, int windowWidth, int windowHeight) {
		font = FontCache.getFont(FontKey.TOOLTIP_FONT, g);
		size = ItemDisplay.BLOCKSIZE * GameRenderer.getRenderScale();
		width = ITEMS_PER_ROW * size + 5;
		screenX = windowWidth - width;
		healthBarHeight = HEALTHBAR_HEIGHT * GameRenderer.getRenderScale();
		height = MAX_ROWS * size + healthBarHeight + 10;
		screenY = windowHeight - height;
		return true;
	}
}
