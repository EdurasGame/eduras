package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.illonis.eduras.networking.discover.ServerSearcher;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.NiftyBasicGameState;

/**
 * The state where serverlist is shown. Handles server discovery, too.
 * 
 * @author illonis
 * 
 */
public class ServerListState extends NiftyBasicGameState {

	private final GameControllerBridge game;
	private ServerListController controller;
	private ServerSearcher searcher;
	private final String presetServerAddress;
	private final int presetServerPort;

	ServerListState(GameControllerBridge game) {
		this(game, "", 0);
	}

	ServerListState(GameControllerBridge game, String serverIpToConnectTo,
			int serverPort) {
		super("serverlist");
		this.game = game;

		presetServerAddress = serverIpToConnectTo;
		presetServerPort = serverPort;
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame stateGame) {
		controller = new ServerListController(this.game, presetServerAddress,
				presetServerPort);
		nifty.fromXml("res/hud/serverlist.xml", "serverlist", controller);
	}

	@Override
	public int getID() {
		return 2;
	}

	@Override
	public void enterState(GameContainer container, StateBasedGame stateGame) {
		controller.connectIfPreset();
		startDiscovery();
		controller.clearList();
	}

	@Override
	public void leaveState(GameContainer container, StateBasedGame stateGame) {
		stopDiscovery();
	}

	/**
	 * Starts searching for servers in local network.
	 */
	public void startDiscovery() {
		searcher = new ServerSearcher(controller);
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
