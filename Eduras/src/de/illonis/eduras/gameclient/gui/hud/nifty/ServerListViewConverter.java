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
	public final void display(final Element listBoxItem, final ServerInfo item) {
		final Element text = listBoxItem.findElementById(SERVER_NAME);
		final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);

		final Element text2 = listBoxItem.findElementById(SERVER_ADDRESS);
		final TextRenderer textRenderer2 = text2
				.getRenderer(TextRenderer.class);

		final Element text3 = listBoxItem.findElementById(SERVER_STATUS);
		final TextRenderer textRenderer3 = text3
				.getRenderer(TextRenderer.class);

		final Element text4 = listBoxItem.findElementById(SERVER_PLAYERS);
		final TextRenderer textRenderer4 = text4
				.getRenderer(TextRenderer.class);

		final Element text5 = listBoxItem.findElementById(SERVER_MAP);
		final TextRenderer textRenderer5 = text5
				.getRenderer(TextRenderer.class);

		final Element text6 = listBoxItem.findElementById(SERVER_GAMEMODE);
		final TextRenderer textRenderer6 = text6
				.getRenderer(TextRenderer.class);

		if (item != null) {
			textRenderer.setText(item.getName());
			textRenderer2.setText(item.getUrl().getHostAddress() + ":"
					+ item.getPort());
			textRenderer3.setText(item.getVersion());
			textRenderer4.setText("" + item.getNumberOfPlayers());
			textRenderer5.setText(item.getMap());
			textRenderer6.setText(item.getGameMode());
		} else {
			textRenderer.setText("");
			textRenderer2.setText("");
			textRenderer3.setText("");
			textRenderer4.setText("");
		}
	}

	@Override
	public final int getWidth(final Element listBoxItem, final ServerInfo item) {
		return 100;
	}
}