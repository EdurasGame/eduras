package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.illonis.eduras.networking.discover.ServerFoundListener;
import de.illonis.eduras.networking.discover.ServerSearcher;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.NiftyBasicGameState;

public class ServerListState extends NiftyBasicGameState {

	private final GameControllerBridge game;
	private ServerListController controller;
	private ServerSearcher searcher;

	public ServerListState(GameControllerBridge game) {
		super("serverlist");
		this.game = game;
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame game) {
		controller = new ServerListController(this.game);
		nifty.fromXml("/res/hud/serverlist.xml", "serverlist", controller);
	}

	@Override
	public int getID() {
		return 2;
	}

	@Override
	public void enterState(GameContainer container, StateBasedGame game) {
		System.out.println("start serverlist");
		startDiscovery(controller);
	}

	public void leaveState(GameContainer container, StateBasedGame game) {
		stopDiscovery();
	}

	/**
	 * Starts searching for servers in local network.
	 * 
	 * @param listener
	 *            the listener that retrieves found servers.
	 */
	public void startDiscovery(ServerFoundListener listener) {
		searcher = new ServerSearcher(listener);
		searcher.start();
	}

	/**
	 * Stops searching for servers.
	 */
	public void stopDiscovery() {
		if (searcher == null)
			return;
		searcher.interrupt();
	}
}
