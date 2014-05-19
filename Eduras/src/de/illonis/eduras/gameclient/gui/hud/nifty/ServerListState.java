package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.state.StateBasedGame;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.NiftyBasicGameState;
import de.lessvoid.nifty.tools.SizeValue;

public class ServerListState extends NiftyBasicGameState {

	private final GameControllerBridge game;

	public ServerListState(GameControllerBridge game) {
		super("serverlist");
		this.game = game;
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame game) {
		nifty.fromXml("/res/hud/serverlist.xml", "serverlist",
				new ServerListController(this.game));
	}

	@Override
	public int getID() {
		return 2;
	}

}
