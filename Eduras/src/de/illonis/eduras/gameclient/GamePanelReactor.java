package de.illonis.eduras.gameclient;

import java.awt.geom.Rectangle2D;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.exceptions.NotWithinBaseException;
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.gameobjects.NeutralBase;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

/**
 * Handles interactions from graphical user interface that should be passed
 * either to logic or server.
 * 
 * @author illonis
 * 
 */
public interface GamePanelReactor {

	/**
	 * Triggers an item use.
	 * 
	 * @param slotId
	 *            the slot that's item should be used.
	 * @param target
	 *            the target location.
	 */
	void onItemUse(int slotId, Vector2f target);

	/**
	 * Triggers direction change.
	 * 
	 * @param viewingPoint
	 *            the new viewing direction.
	 */
	void onViewingDirectionChanged(Vector2f viewingPoint);

	/**
	 * Triggers start of movement of clients {@link PlayerMainFigure}.
	 * 
	 * @param direction
	 *            the movement direction.
	 */
	void onStartMovement(Direction direction);

	/**
	 * Triggers stop of movement of clients {@link PlayerMainFigure}.
	 * 
	 * @param direction
	 *            the movement direction.
	 */
	void onStopMovement(Direction direction);

	/**
	 * Triggers that player dragged a rectangle to select multiple units.
	 * 
	 * @param area
	 *            the rectangle that has been drawn.
	 */
	void onUnitsSelected(Rectangle2D.Double area);

	/**
	 * Resurrects given player.
	 * 
	 * @param player
	 *            the player to resurrect.
	 * @param base
	 *            the base to resurrect at.
	 */
	void onPlayerRezz(Player player, NeutralBase base);

	/**
	 * Heals a unit.
	 * 
	 * @param targetUnit
	 *            the unit to heal.
	 */
	void onUnitHeal(Unit targetUnit);

	/**
	 * Triggers users wish of quitting the game.
	 */
	void onGameQuit();

	/**
	 * Scout spell.
	 * 
	 * @param target
	 *            target location.
	 */
	void onSpawnScout(Vector2f target);

	/**
	 * Indicates that the player wants to spawn an item of the given type at the
	 * given location.
	 * 
	 * @param type
	 * @param locationToSpawnAt
	 * @throws WrongObjectTypeException
	 *             thrown if the given type isn't an item
	 */
	void onSpawnItem(ObjectType type, Vector2f locationToSpawnAt)
			throws WrongObjectTypeException;

	/**
	 * Indicates a mode switch.
	 * 
	 * @throws NotWithinBaseException
	 */
	void onModeSwitch() throws NotWithinBaseException;

	/**
	 * Indicates a simple click at given position. This should either select a
	 * single unit (if any is at that point) or remove selection.
	 * 
	 * @param point
	 *            the point where user clicked.
	 */
	void selectOrDeselectAt(Vector2f point);

	/**
	 * Indicates to trigger a send units event to server with currently selected
	 * units and given target.
	 * 
	 * @param target
	 *            the target location.
	 */
	void sendSelectedUnits(Vector2f target);

	/**
	 * Spawns a unit at given base.
	 * 
	 * @param type
	 *            the type of unit to spawn.
	 * @param base
	 *            the base to spawn unit at.
	 */
	void onUnitSpawned(ObjectType type, NeutralBase base);

	/**
	 * Sets current click state in gui.
	 * 
	 * @param newState
	 *            the new state.
	 */
	void setClickState(ClickState newState);

}
