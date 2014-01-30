package de.illonis.eduras.gameclient.gui.login;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;

import de.illonis.eduras.gameclient.LoginData;
import de.illonis.eduras.gameclient.LoginPanelReactor;
import de.illonis.eduras.gameclient.gui.ClientGuiStepLogic;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.networking.ClientRole;
import de.illonis.eduras.networking.discover.ServerFoundListener;
import de.illonis.eduras.networking.discover.ServerInfo;

/**
 * Displays login form.
 * 
 * @author illonis
 * 
 */
public final class LoginPanelLogic extends ClientGuiStepLogic implements
		ActionListener, ServerFoundListener {

	private final LoginPanel gui;
	private final LoginPanelReactor reactor;
	private final DefaultListModel<ServerInfo> serverData;

	/**
	 * Creates the login panel.
	 * 
	 * @param reactor
	 *            the listener that is triggered when user clicks the login
	 *            button.
	 */
	public LoginPanelLogic(LoginPanelReactor reactor) {
		this.reactor = reactor;
		serverData = new DefaultListModel<ServerInfo>();
		gui = new LoginPanel(serverData, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		startLogin();
	}

	private void startLogin() {
		InetAddress address;
		try {
			address = InetAddress.getByName(gui.getAddress());
		} catch (UnknownHostException e1) {
			showLocalizedError("client.login.invalidaddress",
					"client.login.invalidaddress.title");
			return;
		}
		int port;
		try {
			port = gui.getPort();
		} catch (NumberFormatException e) {
			showLocalizedError("client.error.invalidport",
					"client.error.invalidport.title");
			return;
		}

		String userName = gui.getUserName();
		ClientRole role = gui.getRole();
		LoginData data = new LoginData(address, port, userName, role);
		if (data.validate()) {
			reactor.login(data);
		} else
			showError(
					Localization.getStringF("client.login.invaliddata",
							data.getErrorMessage()),
					Localization.getString("client.login.invaliddata.title"));
	}

	@Override
	public void onServerFound(ServerInfo info) {
		if (!serverData.contains(info))
			serverData.addElement(info);
	}

	@Override
	public void onDiscoveryFailed() {
		serverData.clear();
		serverData.addElement(new ServerInfo("<Discovery failed>", InetAddress
				.getLoopbackAddress(), 0));
	}

	@Override
	public void onShown() {
		gui.startAnimation();
	}

	@Override
	public void onHidden() {
		gui.stopAnimation();
	}

	@Override
	public Component getGui() {
		return gui;
	}
}