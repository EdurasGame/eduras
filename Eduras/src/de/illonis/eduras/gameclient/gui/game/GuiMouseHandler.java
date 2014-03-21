package de.illonis.eduras.gameclient.gui.game;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	public void mouseMoved(MouseEvent e) {
		getPanelLogic().getTooltipHandler().hideTooltip();
		Point p = e.getPoint();

		for (TooltipTriggerer t : triggerers) {
			if (t.getTriggerArea().contains(p)) {
				t.onMouseOver(p);
				break;
			}
		}

		PlayerMainFigure player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e1) {
			L.log(Level.SEVERE, "Something terribly bad happened.", e1);
			return;
		}
		switch (player.getCurrentMode()) {
		case MODE_EGO:
			egoModeHandler.mouseMoved(e);
			break;
		case MODE_STRATEGY:
			buildModeHandler.mouseMoved(e);
		default:
			break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		Point p = e.getPoint();
		L.fine("Click at " + e.getX() + ", " + e.getY());
		for (Iterator<ClickableGuiElementInterface> iterator = clickListeners
				.iterator(); iterator.hasNext();) {
			ClickableGuiElementInterface nextReactor = iterator.next();
			if (nextReactor.isActive() && nextReactor.getBounds().contains(p))
				if (nextReactor.onClick(p))
					return;
		}

		PlayerMainFigure player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e1) {
			L.log(Level.SEVERE, "Something terribly bad happened.", e1);
			return;
		}

		switch (player.getCurrentMode()) {
		case MODE_EGO:
			egoModeHandler.mouseClicked(e);
			break;
		case MODE_STRATEGY:
			buildModeHandler.mouseClicked(e);
		default:
			break;
		}
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
	public void mouseDragged(MouseEvent e) {

		PlayerMainFigure player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e1) {
			L.log(Level.SEVERE, "Something terribly bad happened.", e1);
			return;
		}
		switch (player.getCurrentMode()) {
		case MODE_EGO:
			egoModeHandler.mouseDragged(e);
			break;
		case MODE_STRATEGY:
			buildModeHandler.mouseDragged(e);
		default:
			break;
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		PlayerMainFigure player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e1) {
			L.log(Level.SEVERE, "Something terribly bad happened.", e1);
			return;
		}
		switch (player.getCurrentMode()) {
		case MODE_EGO:
			egoModeHandler.mousePressed(e);
			break;
		case MODE_STRATEGY:
			buildModeHandler.mousePressed(e);
		default:
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		PlayerMainFigure player;
		try {
			player = infoPro.getPlayer();
		} catch (ObjectNotFoundException e1) {
			L.log(Level.SEVERE, "Something terribly bad happened.", e1);
			return;
		}
		switch (player.getCurrentMode()) {
		case MODE_EGO:
			egoModeHandler.mouseReleased(e);
			break;
		case MODE_STRATEGY:
			buildModeHandler.mouseReleased(e);
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
}
