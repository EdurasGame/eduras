package de.illonis.eduras.gameclient.gui.game;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.gui.hud.ClickableGuiElementInterface;
import de.illonis.eduras.gameclient.gui.hud.TooltipTriggerer;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * Handles mouse clicks and mouse movement in the gui.<br>
 * This handler passes events - if not consumed by user interface - to the
 * mousehandler that is responsible for players current interaction mode.
 * 
 * @author illonis
 * 
 */
public final class GuiMouseHandler extends GuiMouseAdapter implements
		TooltipTriggererNotifier {

	private final static Logger L = EduLog.getLoggerFor(GuiMouseHandler.class
			.getName());

	private final EgoModeMouseAdapter egoModeHandler;
	private final BuildModeMouseAdapter buildModeHandler;
	private final DeadModeMouseAdapter deadModeHandler;
	private final LinkedList<ClickableGuiElementInterface> clickListeners;
	private boolean gameHasMouse;
	private GuiMouseAdapter lastHandler;

	private final LinkedList<TooltipTriggerer> triggerers;

	private final InformationProvider infoPro;

	GuiMouseHandler(GamePanelLogic logic, GuiInternalEventListener reactor) {
		super(logic, reactor);
		gameHasMouse = true;
		lastHandler = null;
		infoPro = EdurasInitializer.getInstance().getInformationProvider();
		triggerers = new LinkedList<TooltipTriggerer>();
		buildModeHandler = new BuildModeMouseAdapter(logic, reactor);
		egoModeHandler = new EgoModeMouseAdapter(logic, reactor);
		deadModeHandler = new DeadModeMouseAdapter(logic, reactor);
		clickListeners = new LinkedList<ClickableGuiElementInterface>();
	}

	@Override
	public void registerTooltipTriggerer(TooltipTriggerer elem) {
		triggerers.add(elem);
	}

	@Override
	public void removeTooltipTriggerer(TooltipTriggerer elem) {
		triggerers.remove(elem);
	}

	@Override
	public void mapClicked(Vector2f gamePos) {
		getCurrentHandler().mapClicked(gamePos);
	}

	@Override
	public void itemClicked(int slot) {
		getCurrentHandler().itemClicked(slot);
	}

	@Override
	public void addClickableGuiElement(ClickableGuiElementInterface elem) {
		clickListeners.add(elem);
	}

	@Override
	public void removeClickableGuiElement(ClickableGuiElementInterface elem) {
		clickListeners.remove(elem);
	}

	private void loseGameFocus() {
		if (gameHasMouse) {
			getCurrentHandler().mouseLost();
			gameHasMouse = false;
		}
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		for (Iterator<ClickableGuiElementInterface> iterator = clickListeners
				.iterator(); iterator.hasNext();) {
			ClickableGuiElementInterface nextReactor = iterator.next();
			if (nextReactor.isActive()
					&& nextReactor.getBounds().contains(x, y)) {
				if (nextReactor.mouseClicked(button, x, y, clickCount)) {
					loseGameFocus();
					return;
				}
			}
		}
		getCurrentHandler().mouseClicked(button, x, y, clickCount);
	}

	private GuiMouseAdapter getCurrentHandler() {
		Player player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Something terribly bad happened.", e);
			return null;
		}
		GuiMouseAdapter handler;
		switch (player.getCurrentMode()) {
		case MODE_EGO:
			handler = egoModeHandler;
			break;
		case MODE_STRATEGY:
			handler = buildModeHandler;
			break;
		case MODE_DEAD:
			handler = deadModeHandler;
			break;
		default:
			// hopefully never happens
			lastHandler.mouseLost();
			return null;
		}
		if (!handler.equals(lastHandler)) {
			if (lastHandler != null)
				lastHandler.mouseLost();
			lastHandler = handler;
		}
		return handler;
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		getPanelLogic().getTooltipHandler().hideTooltip();
		Vector2f p = new Vector2f(newx, newy);

		for (TooltipTriggerer t : triggerers) {
			if (t.isActive() && t.getTriggerArea().contains(newx, newy)) {
				t.onMouseOver(p);
				break;
			}
		}
		for (Iterator<ClickableGuiElementInterface> iterator = clickListeners
				.iterator(); iterator.hasNext();) {
			ClickableGuiElementInterface nextReactor = iterator.next();
			if (nextReactor.isActive()
					&& (nextReactor.getBounds().contains(newx, newy))) {
				if (nextReactor.mouseMoved(oldx, oldy, newx, newy)) {
					loseGameFocus();
					return;
				}
			}
		}
		gameHasMouse = true;
		getCurrentHandler().mouseMoved(oldx, oldy, newx, newy);
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		getPanelLogic().getTooltipHandler().hideTooltip();
		Vector2f p = new Vector2f(newx, newy);

		for (TooltipTriggerer t : triggerers) {
			if (t.isActive() && t.getTriggerArea().contains(newx, newy)) {
				t.onMouseOver(p);
				break;
			}
		}
		for (Iterator<ClickableGuiElementInterface> iterator = clickListeners
				.iterator(); iterator.hasNext();) {
			ClickableGuiElementInterface nextReactor = iterator.next();
			if (nextReactor.isActive()
					&& (nextReactor.getBounds().contains(newx, newy))) {
				if (nextReactor.mouseDragged(oldx, oldy, newx, newy)) {
					loseGameFocus();
					return;
				}
			}
		}
		gameHasMouse = true;
		getCurrentHandler().mouseDragged(oldx, oldy, newx, newy);
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		for (Iterator<ClickableGuiElementInterface> iterator = clickListeners
				.iterator(); iterator.hasNext();) {
			ClickableGuiElementInterface nextReactor = iterator.next();
			if (nextReactor.isActive()
					&& nextReactor.getBounds().contains(x, y)) {
				if (nextReactor.mouseReleased(button, x, y)) {
					loseGameFocus();
					return;
				}
			}
		}
		gameHasMouse = true;
		getCurrentHandler().mouseReleased(button, x, y);
	}

	@Override
	public void mouseWheelMoved(int change) {
		Vector2f mouse = getPanelLogic().getCurrentMousePos();
		for (Iterator<ClickableGuiElementInterface> iterator = clickListeners
				.iterator(); iterator.hasNext();) {
			ClickableGuiElementInterface nextReactor = iterator.next();
			if (nextReactor.isActive()
					&& nextReactor.getBounds().contains(mouse.x, mouse.y)) {
				if (nextReactor.mouseWheelMoved(change)) {
					loseGameFocus();
					return;
				}
			}
		}
		gameHasMouse = true;
		getCurrentHandler().mouseWheelMoved(change);
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		for (Iterator<ClickableGuiElementInterface> iterator = clickListeners
				.iterator(); iterator.hasNext();) {
			ClickableGuiElementInterface nextReactor = iterator.next();
			if (nextReactor.isActive()
					&& nextReactor.getBounds().contains(x, y)) {
				if (nextReactor.mousePressed(button, x, y)) {
					loseGameFocus();
					return;
				}
			}
		}
		gameHasMouse = true;
		getCurrentHandler().mousePressed(button, x, y);
	}

	@Override
	public void mouseLost() {
		// never received here.
	}
}
