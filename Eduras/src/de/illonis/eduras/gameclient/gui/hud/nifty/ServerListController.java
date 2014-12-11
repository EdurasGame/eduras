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
import de.illonis.eduras.EdurasVersion;
import de.illonis.eduras.gameclient.LoginData;
import de.illonis.eduras.gameclient.audio.SoundMachine;
import de.illonis.eduras.gameclient.audio.SoundMachine.SoundType;
import de.illonis.eduras.networking.ClientRole;
import de.illonis.eduras.networking.discover.ServerFoundListener;
import de.illonis.eduras.networking.discover.ServerInfo;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
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

	private final static String VERSION_POPUP_TEXT = "Your client version does not match version on selected server.\n\nClient version: %s\nServer version: %s";

	private final static String VERSION_POPUP = "incompatibleVersionPopup";

	private ListBox<ServerInfo> listBox;
	private Label versionPopupLabel;
	private TextField customIpTextField;
	private final String presetServerIp;
	private final int presetServerPort;

	private boolean firstTime = true;

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
		customIpTextField = screen.findNiftyControl("customIpTextField",
				TextField.class);
		listBox = screen.findNiftyControl("serverList", ListBox.class);
	}

	private void showVersionPopup(String clientVersion, String serverVersion) {
		Element popup = nifty.createPopupWithId(VERSION_POPUP, VERSION_POPUP);
		versionPopupLabel = popup.findNiftyControl("versionPopupLabel",
				Label.class);
		System.out.println(versionPopupLabel);
		versionPopupLabel.setText(String.format(VERSION_POPUP_TEXT,
				clientVersion, serverVersion));
		nifty.showPopup(nifty.getCurrentScreen(), VERSION_POPUP, null);
	}

	/**
	 * This methods triggers joining a server, if a valid server IP and port
	 * have been set on initialization.
	 */
	public void connectIfPresetAndFirstTime() {
		if (!presetServerIp.isEmpty() && presetServerPort != 0 && firstTime) {
			try {
				joinServer(new ServerInfo(
						InetAddress.getByName(presetServerIp), presetServerPort));
			} catch (UnknownHostException e) {
				L.log(Level.WARNING, "Cannot find specified IP '"
						+ presetServerIp + "'", e);
				return;
			}
		}
		firstTime = false;
	}

	/**
	 * Joins the selected server.
	 */
	public void join() {
		List<ServerInfo> selected = listBox.getSelection();
		if (selected.size() == 1) {
			ServerInfo current = selected.get(0);
			joinServer(current);
		} else {
			try {
				String serverAddressAndPort = customIpTextField
						.getDisplayedText();
				String hostAddress = serverAddressAndPort.split(":")[0];
				int port = Integer.parseInt(serverAddressAndPort.split(":")[1]);
				joinServer(new ServerInfo(InetAddress.getByName(hostAddress),
						port));
			} catch (NumberFormatException | UnknownHostException e) {
				// TODO: Show a notification or something
				return;
			}
		}
	}

	private void joinServer(ServerInfo server) {
		String clientVersion = EdurasVersion.getVersion();
		if (server.getMap().isEmpty()
				|| server.getVersion().equals(clientVersion)) {
			game.setServer(server);
			game.enterState(LoadingState.LOADING_STATE_ID);
			String userName = game.getUsername();
			ClientRole role = ClientRole.PLAYER;
			LoginData data = new LoginData(server.getUrl(), server.getPort(),
					userName, role);
			game.setLoginData(data);
			game.enterState(5);
		} else {
			showVersionPopup(clientVersion, server.getVersion());
		}
	}

	/**
	 * switches to settings view.
	 */
	public void showSettings() {
		SoundMachine.play(SoundType.CLICK, 2f);
		game.enterState(1, new FadeOutTransition(Color.black, 100),
				new FadeInTransition(Color.black, 300));
	}

	/**
	 * Close error popup.
	 */
	public void closeVersionPopup() {
		nifty.closePopup(VERSION_POPUP);
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