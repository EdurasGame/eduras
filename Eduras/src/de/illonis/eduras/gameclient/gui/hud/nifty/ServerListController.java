package de.illonis.eduras.gameclient.gui.hud.nifty;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.LoginData;
import de.illonis.eduras.gameclient.audio.SoundMachine;
import de.illonis.eduras.gameclient.audio.SoundMachine.SoundType;
import de.illonis.eduras.networking.ClientRole;
import de.illonis.eduras.networking.discover.ServerFoundListener;
import de.illonis.eduras.networking.discover.ServerInfo;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;

/**
 * Controller for serverlisting.
 * 
 * @author illonis
 * 
 */
public class ServerListController extends EdurasScreenController implements
		ServerFoundListener {

	private final static Logger L = EduLog
			.getLoggerFor(ServerListController.class.getName());

	private ListBox<ServerInfo> listBox;
	private final String presetServerIp;
	private final int presetServerPort;

	ServerListController(GameControllerBridge game) {
		this(game, "", 0);
	}

	ServerListController(GameControllerBridge game, String presetServerAddress,
			int presetServerPort) {
		super(game);
		presetServerIp = presetServerAddress;
		this.presetServerPort = presetServerPort;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initScreen(Screen screen) {
		listBox = screen.findNiftyControl("serverList", ListBox.class);

		if (!presetServerIp.isEmpty() && presetServerPort != 0) {
			try {
				joinServer(new ServerInfo("",
						InetAddress.getByName(presetServerIp),
						presetServerPort, "", 0, "", ""));
			} catch (UnknownHostException e) {
				L.log(Level.WARNING, "Cannot find specified IP '"
						+ presetServerIp + "'", e);
				return;
			}
		}
	}

	/**
	 * Joins the selected server.
	 */
	public void join() {
		List<ServerInfo> selected = listBox.getSelection();
		if (selected.size() == 1) {
			ServerInfo current = selected.get(0);
			joinServer(current);
		}
	}

	private void joinServer(ServerInfo server) {
		game.setServer(server);
		game.enterState(3);
		String userName = game.getUsername();
		ClientRole role = ClientRole.PLAYER;
		LoginData data = new LoginData(server.getUrl(), server.getPort(),
				userName, role);
		game.setLoginData(data);
		game.enterState(5);
	}

	/**
	 * switches to settings view.
	 */
	public void showSettings() {
		SoundMachine.play(SoundType.CLICK, 2f);
		game.enterState(1, new FadeOutTransition(Color.black, 100),
				new FadeInTransition(Color.black, 300));
	}

	@Override
	public void onServerFound(ServerInfo info) {
		if (!listBox.getItems().contains(info)) {
			listBox.addItem(info);
			listBox.refresh();
		}
	}

	@Override
	public void onDiscoveryFailed() {
		System.out.println("discovery failed");
		// TODO: implement
	}

	/**
	 * Clears the server list.
	 */
	public void clearList() {
		listBox.clear();
	}
}