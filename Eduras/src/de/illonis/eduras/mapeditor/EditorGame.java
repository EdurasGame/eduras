package de.illonis.eduras.mapeditor;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import de.illonis.eduras.gameclient.datacache.GraphicsPreLoader;
import de.illonis.eduras.mapeditor.MapInteractor.InteractType;
import de.illonis.eduras.mapeditor.gui.EditorWindow;

/**
 * Pseudo game for displaying map in editor.
 * 
 * @author illonis
 * 
 */
public class EditorGame extends BasicGame {

	private final MapRenderer renderer;
	private final MapPanelLogic panelLogic;
	private final MapInputHandler inputHandler;
	private final ShapeEditInputHandler shapeInputHandler;

	EditorGame(EditorWindow window) {
		super("Eduras? Map Editor");
		panelLogic = new MapPanelLogic(window);
		renderer = new MapRenderer(panelLogic);
		inputHandler = new MapInputHandler(panelLogic, window);
		shapeInputHandler = new ShapeEditInputHandler(panelLogic, window);
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		renderer.render(container, g);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		container.setAlwaysRender(true);
		container.setClearEachFrame(false);
		container.setSoundOn(false);
		container.setMouseGrabbed(false);
		container.setTargetFrameRate(60);
		container.setMinimumLogicUpdateInterval(15);
		container.setMaximumLogicUpdateInterval(100);
		container.setShowFPS(false);
		GraphicsPreLoader.loadShapes();
		GraphicsPreLoader.loadGraphics();
		GraphicsPreLoader.loadIcons();
		panelLogic.setInput(container.getInput());
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		panelLogic.update(delta);
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (panelLogic.getInteractType() == InteractType.EDIT_SHAPE) {
			shapeInputHandler.mouseDragged(oldx, oldy, newx, newy);
		} else
			inputHandler.mouseDragged(oldx, oldy, newx, newy);
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if (panelLogic.getInteractType() == InteractType.EDIT_SHAPE) {
			shapeInputHandler.mouseClicked(button, x, y, clickCount);
		} else
			inputHandler.mouseClicked(button, x, y, clickCount);
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if (panelLogic.getInteractType() == InteractType.EDIT_SHAPE) {
			shapeInputHandler.mouseMoved(oldx, oldy, newx, newy);
		} else
			inputHandler.mouseMoved(oldx, oldy, newx, newy);
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (panelLogic.getInteractType() == InteractType.EDIT_SHAPE) {
			shapeInputHandler.mousePressed(button, x, y);
		} else
			inputHandler.mousePressed(button, x, y);
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		if (panelLogic.getInteractType() == InteractType.EDIT_SHAPE) {
			shapeInputHandler.mouseReleased(button, x, y);
		} else
			inputHandler.mouseReleased(button, x, y);
	}

	@Override
	public void mouseWheelMoved(int change) {
		inputHandler.mouseWheelMoved(change);
	}

	@Override
	public void keyPressed(int key, char c) {
		if (panelLogic.getInteractType() == InteractType.EDIT_SHAPE) {
			shapeInputHandler.keyPressed(key, c);
		} else
			inputHandler.keyPressed(key, c);
	}

	@Override
	public void keyReleased(int key, char c) {
		if (panelLogic.getInteractType() == InteractType.EDIT_SHAPE) {
			shapeInputHandler.keyReleased(key, c);
		} else
			inputHandler.keyReleased(key, c);
	}

	/**
	 * @return the panel logic.
	 */
	public MapPanelLogic getPanelLogic() {
		return panelLogic;
	}

}