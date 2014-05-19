package de.illonis.eduras.gameclient.gui.hud.nifty;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import de.illonis.eduras.gameclient.gui.SoundMachine;
import de.illonis.eduras.gameclient.gui.SoundMachine.SoundType;
import de.illonis.eduras.networking.discover.ServerInfo;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;

public class ServerListController extends EdurasScreenController {

	private ListBox<ServerInfo> listBox;

	public ServerListController(GameControllerBridge game) {
		super(game);
	}

	@Override
	protected void initScreen(Screen screen) {
		listBox = (ListBox<ServerInfo>) screen.findNiftyControl("serverList",
				ListBox.class);
		fillMyListBox();
	}

	/**
	 * Fill the listbox with items. In this case with Strings.
	 */
	public void fillMyListBox() {
		try {
			listBox.addItem(new ServerInfo("testserver", InetAddress
					.getByName("illonis.de"), 41, "4.3"));
			listBox.addItem(new ServerInfo("testserver 13", InetAddress
					.getByName("illonis.de"), 43, "4.2"));
			listBox.addItem(new ServerInfo("testserver 41", InetAddress
					.getByName("illonis.de"), 44, "1"));
		} catch (UnknownHostException e) {
		}
	}

	public void join() {
		List<ServerInfo> selected = listBox.getSelection();
		if (selected.size() == 1) {
			ServerInfo current = selected.get(0);
			System.out.println("joining " + current.getName());
			game.enterState(3);
		}
	}

	public void showSettings() {
		SoundMachine.getSound(SoundType.CLICK).play(2f, 0.1f);
		game.enterState(1, new FadeOutTransition(Color.black, 100),
				new FadeInTransition(Color.black, 300));
	}
}