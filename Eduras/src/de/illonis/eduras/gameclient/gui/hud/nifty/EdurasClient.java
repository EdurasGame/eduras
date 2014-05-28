package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.transition.Transition;

import de.illonis.eduras.networking.discover.ServerInfo;
import de.lessvoid.nifty.slick2d.NiftyStateBasedGame;

/**
 * A test application for gui.
 * 
 * @author illonis
 * 
 */
public class EdurasClient implements GameControllerBridge {

	private AppGameContainer gameContainer;
	private Game game;
	private String username = "";
	private ServerInfo server;

	void startGui() throws SlickException {
		if (game != null)
			throw new IllegalStateException("Cannot start gui more than once!");
		game = new Game();
		gameContainer = new AppGameContainer(game);
		DisplayMode currentMode = Display.getDesktopDisplayMode();
		gameContainer.setDisplayMode(currentMode.getWidth(),
				currentMode.getHeight(), true);
		gameContainer.start();
	}

	/**
	 * Starts the nifty test.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		EdurasClient client = new EdurasClient();
		client.startGui();
	}

	@Override
	public void exit() {
		try {
			gameContainer.setDisplayMode(1920, 1080, true);
		} catch (SlickException e) {

		}
		gameContainer.exit();

	}
	
	@Override
	public void changeResolution(int width, int height) throws SlickException {
		gameContainer.setDisplayMode(width, height, true);
	}

	class Game extends NiftyStateBasedGame {
		
		protected Game() {
			super("Eduras? Client");
		}

		@Override
		public void initStatesList(GameContainer container)
				throws SlickException {
			// add game states here
			addState(new LoginState(EdurasClient.this));
			addState(new SettingsState(EdurasClient.this));
			addState(new ServerListState(EdurasClient.this));
			addState(new LoadingState(EdurasClient.this));
			addState(new GameState(EdurasClient.this));
		}
	}

	@Override
	public void enterState(int state) {
		game.enterState(state);
	}

	@Override
	public void enterState(int id, Transition leave, Transition enter) {
		game.enterState(id, leave, enter);
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setUsername(String name) {
		this.username = name;
	}

	@Override
	public void setServer(ServerInfo current) {
		server = current;		
	}

	@Override
	public ServerInfo getServer() {
		return server;
	}

}
