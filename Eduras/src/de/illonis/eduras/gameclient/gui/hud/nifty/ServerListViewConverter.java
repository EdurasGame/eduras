package de.illonis.eduras.gameclient.gui.hud.nifty;

import de.illonis.eduras.networking.discover.ServerInfo;
import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 * Handles the displaying of the items in the ChatBox.
 * 
 * @author Mark
 * @version 0.1
 */
public class ServerListViewConverter implements
		ListBoxViewConverter<ServerInfo> {
	private static final String SERVER_NAME = "#serverName";
	private static final String SERVER_ADDRESS = "#serverAddress";
	private static final String SERVER_PLAYERS = "#serverPlayers";
	private static final String SERVER_STATUS = "#serverStatus";
	private static final String SERVER_MAP = "#serverMap";
	private static final String SERVER_GAMEMODE = "#serverGamemode";

	/**
	 * Default constructor.
	 */
	public ServerListViewConverter() {
	}

	@Override
	public final void display(final Element listBoxItem, final ServerInfo serverInfo) {
		final Element serverNameElement = listBoxItem.findElementById(SERVER_NAME);
		final TextRenderer serverNameRenderer = serverNameElement.getRenderer(TextRenderer.class);

		final Element serverAddressElement = listBoxItem.findElementById(SERVER_ADDRESS);
		final TextRenderer serverAddressRenderer = serverAddressElement
				.getRenderer(TextRenderer.class);

		final Element serverStatusElement = listBoxItem.findElementById(SERVER_STATUS);
		final TextRenderer serverStatusRenderer = serverStatusElement
				.getRenderer(TextRenderer.class);

		final Element serverPlayersElement = listBoxItem.findElementById(SERVER_PLAYERS);
		final TextRenderer serverPlayersRenderer = serverPlayersElement
				.getRenderer(TextRenderer.class);

		final Element mapNameElement = listBoxItem.findElementById(SERVER_MAP);
		final TextRenderer mapNameRenderer = mapNameElement
				.getRenderer(TextRenderer.class);

		final Element gameModeElement = listBoxItem.findElementById(SERVER_GAMEMODE);
		final TextRenderer gameModeNameRenderer = gameModeElement
				.getRenderer(TextRenderer.class);

		if (serverInfo != null) {
			serverNameRenderer.setText(serverInfo.getName());
			serverAddressRenderer.setText(serverInfo.getUrl().getHostAddress() + ":"
					+ serverInfo.getPort());
			serverStatusRenderer.setText(serverInfo.getVersion());
			serverPlayersRenderer.setText("" + serverInfo.getNumberOfPlayers());
			mapNameRenderer.setText(serverInfo.getMap());
			gameModeNameRenderer.setText(serverInfo.getGameMode());
		} else {
			serverNameRenderer.setText("");
			serverAddressRenderer.setText("");
			serverStatusRenderer.setText("");
			serverPlayersRenderer.setText("");
			mapNameRenderer.setText("");
			gameModeNameRenderer.setText("");
		}
	}

	@Override
	public final int getWidth(final Element listBoxItem, final ServerInfo item) {
		return 100;
	}
}