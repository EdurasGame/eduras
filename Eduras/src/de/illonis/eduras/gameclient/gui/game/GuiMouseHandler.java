package de.illonis.eduras.gameclient.gui.game;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.gui.hud.ClickableGuiElementInterface;
import de.illonis.eduras.gameclient.gui.hud.TooltipTriggerer;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.units.PlayerMainFigure;

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
	private final LinkedList<ClickableGuiElementInterface> clickListeners;

	private final LinkedList<TooltipTriggerer> triggerers;

	private final InformationProvider infoPro;

	GuiMouseHandler(GamePanelLogic logic, GuiInternalEventListener reactor) {
		super(logic, reactor);
		infoPro = EdurasInitializer.getInstance().getInformationProvider();
		triggerers = new LinkedList<TooltipTriggerer>();
		buildModeHandler = new BuildModeMouseAdapter(logic, reactor);
		egoModeHandler = new EgoModeMouseAdapter(logic, reactor);
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
	public void itemClicked(int slot) {
		PlayerMainFigure player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e1) {
			L.log(Level.SEVERE, "Something terribly bad happened.", e1);
			return;
		}
		switch (player.getCurrentMode()) {
		case MODE_EGO:
			egoModeHandler.itemClicked(slot);
			break;
		case MODE_STRATEGY:
			buildModeHandler.itemClicked(slot);
		default:
			break;
		}

	}

	@Override
	public void addClickableGuiElement(ClickableGuiElementInterface elem) {
		clickListeners.add(elem);
	}

	@Override
	public void removeClickableGuiElement(ClickableGuiElementInterface elem) {
		clickListeners.remove(elem);
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		Vector2f p = new Vector2f(x, y);
		L.fine("Click at " + x + ", " + y);
		for (Iterator<ClickableGuiElementInterface> iterator = clickListeners
				.iterator(); iterator.hasNext();) {
			ClickableGuiElementInterface nextReactor = iterator.next();
			if (nextReactor.isActive()
					&& nextReactor.getBounds().contains(x, y))
				if (nextReactor.onClick(p))
					return;
		}

		PlayerMainFigure player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Something terribly bad happened.", e);
			return;
		}

		switch (player.getCurrentMode()) {
		case MODE_EGO:
			egoModeHandler.mouseClicked(button, x, y, clickCount);
			break;
		case MODE_STRATEGY:
			buildModeHandler.mouseClicked(button, x, y, clickCount);
		default:
			break;
		}
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

		PlayerMainFigure player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Something terribly bad happened.", e);
			return;
		}
		switch (player.getCurrentMode()) {
		case MODE_EGO:
			egoModeHandler.mouseMoved(oldx, oldy, newx, newy);
			break;
		case MODE_STRATEGY:
			buildModeHandler.mouseMoved(oldx, oldy, newx, newy);
		default:
			break;
		}
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		PlayerMainFigure player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e1) {
			L.log(Level.SEVERE, "Something terribly bad happened.", e1);
			return;
		}
		switch (player.getCurrentMode()) {
		case MODE_EGO:
			egoModeHandler.mouseDragged(oldx, oldy, newx, newy);
			break;
		case MODE_STRATEGY:
			buildModeHandler.mouseDragged(oldx, oldy, newx, newy);
		default:
			break;
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		PlayerMainFigure player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Something terribly bad happened.", e);
			return;
		}
		switch (player.getCurrentMode()) {
		case MODE_EGO:
			egoModeHandler.mouseReleased(button, x, y);
			break;
		case MODE_STRATEGY:
			buildModeHandler.mouseReleased(button, x, y);
		default:
			break;
		}
	}

	@Override
	public void mouseWheelMoved(int change) {
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		PlayerMainFigure player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Something terribly bad happened.", e);
			return;
		}
		switch (player.getCurrentMode()) {
		case MODE_EGO:
			egoModeHandler.mousePressed(button, x, y);
			break;
		case MODE_STRATEGY:
			buildModeHandler.mousePressed(button, x, y);
		default:
			break;
		}
	}
}
