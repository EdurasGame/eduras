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
import de.illonis.eduras.settings.S;
import de.lessvoid.nifty.slick2d.NiftyStateBasedGame;

/**
 * The slick implementation part of eduras client.
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
	private String betaAccountName;
	private String betaAccountPassword;
	private String serverIpToConnectTo;
	private int serverPort;

	/**
	 * Starts the gui display in fullscreen mode.
	 * 
	 * @param betaUser
	 *            (NYI) the name of the user to login automatically.
	 * @param betaPassword
	 *            (NYI) the password of the user to login automatically.
	 * @param serverPort
	 *            the port of the server to connect to directly. Is ignored if
	 *            it's zero.
	 * @param serverIp
	 *            the address of the server to connect to directly. Is ignored
	 *            if empty.
	 * @throws SlickException
	 *             if there is a display problem.
	 */
	public void startGui(String betaUser, String betaPassword, String serverIp,
			int serverPort) throws SlickException {
		betaAccountName = betaUser;
		betaAccountPassword = betaPassword;
		serverIpToConnectTo = serverIp;
		this.serverPort = serverPort;

		if (game != null)
			throw new IllegalStateException("Cannot start gui more than once!");
		game = new Game();
		gameContainer = new AppGameContainer(game);
		DisplayMode currentMode = Display.getDesktopDisplayMode();
		if (S.Client.windowed) {
			gameContainer.setDisplayMode(800, 600, false);
		} else {
			gameContainer.setDisplayMode(currentMode.getWidth(),
					currentMode.getHeight(), true);
		}
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

		@Override
		public void keyPressed(int key, char c) {
		}

		@Override
		public void keyReleased(int key, char c) {
		}

		@Override
		public void mousePressed(int button, int x, int y) {
		}

		@Override
		public void mouseReleased(int button, int x, int y) {
		}

		@Override
		public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		}

		@Override
		public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		}

		@Override
		public void mouseClicked(int button, int x, int y, int clickCount) {
		}

		@Override
		public void mouseWheelMoved(int newValue) {
		}

		protected Game() {
			super("Eduras? Client");
		}

		@Override
		public void initStatesList(GameContainer container)
				throws SlickException {
			// add game states here
			// addState(new LoginState(EdurasSlickClient.this, betaAccountName,
			// betaAccountPassword));
			addState(new SettingsState(EdurasSlickClient.this));
			addState(new ServerListState(EdurasSlickClient.this,
					serverIpToConnectTo, serverPort));
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
	public void onGameReady() {
		enterState(4);
	}

}
