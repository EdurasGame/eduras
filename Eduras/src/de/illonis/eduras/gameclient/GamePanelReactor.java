package de.illonis.eduras.gameclient;

import java.awt.geom.Rectangle2D;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ActionFailedException;
import de.illonis.eduras.exceptions.InsufficientResourceException;
import de.illonis.eduras.exceptions.NotWithinBaseException;
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
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
	 * @param itemType
	 *            the type of item that should be used.
	 * @param target
	 *            the target location.
	 */
	void onItemUse(ObjectType itemType, Vector2f target);

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
	 * @param add
	 *            true if selected units should be added to current selection.
	 * @param remove
	 *            true if selected units should be substracted from current
	 *            selection. <i>add</i> is ignored if this is true.
	 */
	void onUnitsSelected(Rectangle2D.Double area, boolean add, boolean remove);

	/**
	 * Resurrects given player.
	 * 
	 * @param player
	 *            the player to resurrect.
	 * @param base
	 *            the base to resurrect at.
	 * @throws InsufficientResourceException
	 *             when resources are too low.
	 * @throws CantSpawnHereException
	 *             thrown if there is no room in the base to spawn the player
	 *             there
	 */
	void onPlayerRezz(Player player, Base base)
			throws InsufficientResourceException, CantSpawnHereException;

	/**
	 * Casts a spell on a unit.
	 * 
	 * @param targetUnit
	 *            the unit to cast the spell on.
	 * @throws InsufficientResourceException
	 *             if not enough resources available
	 * @throws ActionFailedException
	 *             Thrown if the action cannot be performed for some other
	 *             reason
	 */
	void onUnitSpell(Unit targetUnit) throws InsufficientResourceException,
			ActionFailedException;

	/**
	 * Triggers users wish of quitting the game.
	 */
	void onGameQuit();

	/**
	 * Scout spell.
	 * 
	 * @param target
	 *            target location.
	 * @throws InsufficientResourceException
	 *             if not enough resources available
	 */
	void onSpawnScout(Vector2f target) throws InsufficientResourceException;

	/**
	 * Indicates that the player wants to spawn an item of the given type at the
	 * given location.
	 * 
	 * @param type
	 * @param locationToSpawnAt
	 * @throws WrongObjectTypeException
	 *             thrown if the given type isn't an item
	 * @throws InsufficientResourceException
	 *             if not enough resources available.
	 * @throws CantSpawnHereException
	 *             thrown if you cannot spawn at the location
	 */
	void onSpawnItem(ObjectType type, Vector2f locationToSpawnAt)
			throws WrongObjectTypeException, InsufficientResourceException,
			CantSpawnHereException;

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
	 * @param add
	 *            true if unit should be added to existing selection.
	 * @param delete
	 *            true if unit should be deleted from existing selection,
	 *            overrides <i>add</i>
	 */
	void selectOrDeselectAt(Vector2f point, boolean add, boolean delete);

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
	 * @throws InsufficientResourceException
	 *             if not enough resources available
	 * @throws CantSpawnHereException
	 *             thrown if there is no room for the object to spawn at that
	 *             position
	 */
	void onUnitSpawned(ObjectType type, Base base)
			throws InsufficientResourceException, CantSpawnHereException;

	/**
	 * Sets current click state in gui.
	 * 
	 * @param newState
	 *            the new state.
	 */
	void setClickState(ClickState newState);

	void onBlink(Vector2f blinkTarget) throws InsufficientChargesException,
			CantSpawnHereException;

	void teamSelected(Team team);
}
