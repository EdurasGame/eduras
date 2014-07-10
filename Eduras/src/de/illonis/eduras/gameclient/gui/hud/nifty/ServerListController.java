package de.illonis.eduras.gameclient.gui.hud.nifty;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import de.illonis.eduras.gameclient.LoginData;
import de.illonis.eduras.gameclient.gui.SoundMachine;
import de.illonis.eduras.gameclient.gui.SoundMachine.SoundType;
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

	private ListBox<ServerInfo> listBox;

	ServerListController(GameControllerBridge game) {
		super(game);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initScreen(Screen screen) {
		listBox = (ListBox<ServerInfo>) screen.findNiftyControl("serverList",
				ListBox.class);
	}

	/**
	 * Joins the selected server.
	 */
	public void join() {
		List<ServerInfo> selected = listBox.getSelection();
		if (selected.size() == 1) {
			ServerInfo current = selected.get(0);
			game.setServer(current);
			game.enterState(3);
			String userName = game.getUsername();
			ClientRole role = ClientRole.PLAYER;
			LoginData data = new LoginData(current.getUrl(), current.getPort(),
					userName, role);
			game.setLoginData(data);
			game.enterState(5);
		}
	}

	/**
	 * switches to settings view.
	 */
	public void showSettings() {
		SoundMachine.getSound(SoundType.CLICK).play(2f, 0.1f);
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