package de.illonis.eduras.gameclient.gui.hud.nifty;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.transition.Transition;

import de.illonis.eduras.gameclient.EdurasGameClient;
import de.illonis.eduras.gameclient.EdurasGameInterface;
import de.illonis.eduras.gameclient.LoginData;
import de.illonis.eduras.gameclient.userprefs.Settings;
import de.illonis.eduras.images.ImageFiler.ImageResolution;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.networking.ClientRole;
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
	 * @param port
	 *            the port of the server to connect to directly. Is ignored if
	 *            it's zero.
	 * @param serverIp
	 *            the address of the server to connect to directly. Is ignored
	 *            if empty.
	 * @throws SlickException
	 *             if there is a display problem.
	 */
	public void startGui(String betaUser, String betaPassword, String serverIp,
			int port) throws SlickException {
		betaAccountName = betaUser;
		betaAccountPassword = betaPassword;
		serverIpToConnectTo = serverIp;
		this.serverPort = port;

		if (game != null)
			throw new IllegalStateException("Cannot start gui more than once!");
		game = new Game();
		gameContainer = new AppGameContainer(game);
		String[] icons = { "res/images/icon16.png", "res/images/icon24.png",
				"res/images/icon32.png", "res/images/icon64.png" };
		gameContainer.setIcons(icons);
		Settings s = EdurasInitializer.getInstance().getSettings();
		if (S.Client.windowed) {
			s.setBooleanOption(Settings.WINDOWED, true);
			s.setIntOption(Settings.WIDTH, ImageResolution.WINDOWED.getWidth());
			s.setIntOption(Settings.HEIGHT,
					ImageResolution.WINDOWED.getHeight());
		}
		gameContainer.setDisplayMode(s.getIntSetting(Settings.WIDTH),
				s.getIntSetting(Settings.HEIGHT),
				!s.getBooleanSetting(Settings.WINDOWED));
		gameContainer.start();
	}

	@Override
	public void exit() {
		gameContainer.exit();
		System.exit(0);
	}

	@Override
	public void changeResolution(int width, int height, boolean windowed)
			throws SlickException {
		gameContainer.setDisplayMode(width, height, !windowed);
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
			addState(new LoginState(EdurasSlickClient.this, betaAccountName,
					betaAccountPassword));
			addState(new SettingsState(EdurasSlickClient.this));
			addState(new ServerListState(EdurasSlickClient.this,
					serverIpToConnectTo, serverPort));
			addState(new LoadingState(EdurasSlickClient.this));
			addState(new GameState(EdurasSlickClient.this));
			addState(new ConnectingState(EdurasSlickClient.this));
			addState(new SpectatorState(EdurasSlickClient.this));
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
		enterState(ServerListState.SERVER_LIST_STATE_ID);
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
		if (loginData.getRole() == ClientRole.PLAYER)
			enterState(GameState.GAME_STATE_ID);
		else if (loginData.getRole() == ClientRole.SPECTATOR) {
			enterState(SpectatorState.SPECTATOR_STATE_ID);
		}
	}

	@Override
	public void setSoundVolume(float volume) {
		gameContainer.setSoundVolume(volume);
	}

	@Override
	public float getSoundVolume() {
		return gameContainer.getSoundVolume();
	}

	@Override
	public void setMusicVolume(float volume) {
		gameContainer.setMusicVolume(volume);
	}

	@Override
	public float getMusicVolume() {
		return gameContainer.getMusicVolume();
	}

}
