package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Logger;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.networking.ClientRole;
import de.illonis.eduras.units.InteractMode;
import de.illonis.eduras.units.PlayerMainFigure;

class TeamPlayerDisplay extends PlayerDisplay {

	private final static Logger L = EduLog.getLoggerFor(TeamPlayerDisplay.class
			.getName());
	private final UserInterface ui;

	protected TeamPlayerDisplay(UserInterface ui, Player player) {
		super(ui, player);
		zIndex = 1;
		this.ui = ui;
		visibleForSpectator = true;
		setActiveInteractModes(InteractMode.MODE_SPECTATOR);
	}

	@Override
	public boolean init(Graphics g, int windowWidth, int windowHeight) {
		return true;
	}

	@Override
	public boolean mouseReleased(int button, int x, int y) {
		PlayerMainFigure mainFigure = player.getPlayerMainFigure();
		Vector2f gamePos = new Vector2f(mainFigure.getShape().getX(),
				mainFigure.getShape().getY());
		ui.getGameCamera().getCameraOffset().set(gamePos.x, gamePos.y);
		if (getInfo().getClientData().getRole() == ClientRole.SPECTATOR) {
			getInfo().getClientData().setSelectedUnit(
					player.getPlayerMainFigure().getId());
		}
		return true;
	}

}