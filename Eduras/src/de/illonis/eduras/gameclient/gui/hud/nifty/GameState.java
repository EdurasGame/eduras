package de.illonis.eduras.gameclient.gui.hud.nifty;

import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;

import de.illonis.eduras.gameclient.EdurasGameInterface;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.slick2d.NiftyOverlayBasicGameState;

/**
 * The state for ingame.
 * 
 * @author illonis
 * 
 */
public class GameState extends NiftyOverlayBasicGameState {

	public static final int GAME_STATE_ID = 4;
	protected final GameControllerBridge gameBridge;
	private EdurasGameInterface edurasGame;
	private static UnicodeFont defaultFont;
	private static UnicodeFont bigNoteFont;

	GameState(GameControllerBridge game) {
		this.gameBridge = game;
	}

	public static UnicodeFont getBigNoteFont() {
		return bigNoteFont;
	}

	@Override
	protected void initGameAndGUI(GameContainer container, StateBasedGame game) {
		initNifty(container, game);
		defaultFont = new UnicodeFont(new Font("Arial", Font.PLAIN, 12));
		defaultFont.addAsciiGlyphs();
		bigNoteFont = new UnicodeFont(new Font("Arial", Font.PLAIN, 30));
		bigNoteFont.addAsciiGlyphs();
		container.setMinimumLogicUpdateInterval(10);
		container.setMaximumLogicUpdateInterval(70);
		container.setTargetFrameRate(60);
		container.setAlwaysRender(true);
		ColorEffect e = new ColorEffect();
		e.setColor(Color.white);
		defaultFont.getEffects().add(e);
		bigNoteFont.getEffects().add(e);
		try {
			defaultFont.loadGlyphs();
			bigNoteFont.loadGlyphs();
		} catch (SlickException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	protected void enterState(GameContainer container, StateBasedGame game) {
		edurasGame = gameBridge.getEduras();
	}

	@Override
	protected void leaveState(GameContainer container, StateBasedGame game) {
	}

	@Override
	protected void renderGame(GameContainer container, StateBasedGame game,
			Graphics g) {
		g.setFont(defaultFont);
		try {
			edurasGame.render(container, g);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void updateGame(GameContainer container, StateBasedGame game,
			int delta) {
		edurasGame.update(container, delta);
	}

	@Override
	protected void prepareNifty(Nifty nifty, StateBasedGame game) {
		nifty.fromXml("res/hud/game.xml", "game",
				new GameController(gameBridge));
	}

	@Override
	public int getID() {
		return GAME_STATE_ID;
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		edurasGame.mousePressed(button, x, y);
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		edurasGame.mouseClicked(button, x, y, clickCount);
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		edurasGame.mouseMoved(oldx, oldy, newx, newy);
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		edurasGame.mouseDragged(oldx, oldy, newx, newy);
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		edurasGame.mouseReleased(button, x, y);
	}

	@Override
	public void mouseWheelMoved(int change) {
		edurasGame.mouseWheelMoved(change);
	}

	@Override
	public void keyPressed(int key, char c) {
		edurasGame.keyPressed(key, c);
	}

	@Override
	public void keyReleased(int key, char c) {
		edurasGame.keyReleased(key, c);
	}

	public static org.newdawn.slick.Font getDefaultFont() {
		return defaultFont;
	}

}
