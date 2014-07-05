package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.transition.Transition;

import de.illonis.eduras.gameclient.EdurasGameClient;
import de.illonis.eduras.gameclient.EdurasGameInterface;
import de.illonis.eduras.gameclient.LoginData;
import de.illonis.eduras.networking.discover.ServerInfo;
import de.lessvoid.nifty.slick2d.NiftyStateBasedGame;

/**
 * A test application for gui.
 * 
 * @author illonis
 * 
 */
public class EdurasSlickClient implements GameControllerBridge {

	private EdurasGameInterface eduras;
	private AppGameContainer gameContainer;
	private LoginData loginData;
	private Game game;
	private String username = "";
	private ServerInfo server;

	public void startGui(String betaUser, String betaPassword) throws SlickException {
		if (game != null)
			throw new IllegalStateException("Cannot start gui more than once!");
		game = new Game();
		gameContainer = new AppGameContainer(game);
		DisplayMode currentMode = Display.getDesktopDisplayMode();
		gameContainer.setDisplayMode(currentMode.getWidth(),
				currentMode.getHeight(), true);
		gameContainer.start();
	}

	@Override
	public void exit() {
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
			addState(new LoginState(EdurasSlickClient.this));
			addState(new SettingsState(EdurasSlickClient.this));
			addState(new ServerListState(EdurasSlickClient.this));
			addState(new LoadingState(EdurasSlickClient.this));
			addState(new GameState(EdurasSlickClient.this));
			addState(new ConnectingState(EdurasSlickClient.this));
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

	@Override
	public void onDisconnect(boolean gracefully, String message) {
		enterState(2);

	}

	@Override
	public void initClient() {
		eduras = new EdurasGameClient(this, gameContainer);
	}

	@Override
	public EdurasGameInterface getEduras() {
		return eduras;
	}

	@Override
	public void setLoginData(LoginData data) {
		loginData = data;
	}

	@Override
	public LoginData getLoginData() {
		return loginData;
	}

	@Override
	public GameContainer getContainer() {
		return gameContainer;
	}

}
