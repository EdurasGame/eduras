package de.illonis.eduras.gameclient.gui.game;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ActionFailedException;
import de.illonis.eduras.exceptions.InsufficientResourceException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gameclient.CantSpawnHereException;
import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.audio.SoundMachine;
import de.illonis.eduras.gameclient.audio.SoundMachine.SoundType;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Relation;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.units.InteractMode;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

/**
 * Handles mouse events in the build mode ({@link InteractMode#MODE_STRATEGY}).
 * 
 * @author illonis
 * 
 */
public class BuildModeMouseAdapter extends ScrollModeMouseAdapter {

	private final static Logger L = EduLog
			.getLoggerFor(BuildModeMouseAdapter.class.getName());

	private Point startPoint;

	protected BuildModeMouseAdapter(GamePanelLogic logic,
			GuiInternalEventListener reactor) {
		super(logic, reactor);
	}

	private void buildModeClick(int button, int x, int y, int clickCount) {
		Vector2f clickGamePoint = getPanelLogic()
				.computeGuiPointToGameCoordinate(new Vector2f(x, y));

		switch (getPanelLogic().getClickState()) {
		case DEFAULT:
			if (button == Input.MOUSE_LEFT_BUTTON) {
				getListener().selectOrDeselectAt(clickGamePoint,
						getPanelLogic().isKeyDown(Input.KEY_LSHIFT),
						getPanelLogic().isKeyDown(Input.KEY_LCONTROL));
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void itemClicked(int slot) {
	}

	private Rectangle calculateDrawRect(Point first, Point second) {
		if (first.x == second.x || first.y == second.y)
			return new Rectangle(first.x, first.y, 0, 0);

		int topLeftX = Math.min(first.x, second.x);
		int topLeftY = Math.min(first.y, second.y);
		int bottomRightX = Math.max(first.x, second.x);
		int bottomRightY = Math.max(first.y, second.y);

		return new Rectangle(topLeftX, topLeftY, bottomRightX - topLeftX,
				bottomRightY - topLeftY);
	}

	private Rectangle2D.Double calculateDragRect(Vector2f first, Vector2f second) {
		if (first.getX() == second.getX() || first.getY() == second.getY())
			return new Rectangle2D.Double(first.getX(), first.getY(), 0, 0);

		double topLeftX = Math.min(first.getX(), second.getX());
		double topLeftY = Math.min(first.getY(), second.getY());
		double bottomRightX = Math.max(first.getX(), second.getX());
		double bottomRightY = Math.max(first.getY(), second.getY());

		return new Rectangle2D.Double(topLeftX, topLeftY, bottomRightX
				- topLeftX, bottomRightY - topLeftY);
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		buildModeClick(button, x, y, clickCount);
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (getPanelLogic().getClickState() == ClickState.UNITSELECT_DRAGGING) {
			Point first = startPoint;
			Point second = new Point(newx, newy);
			getPanelLogic().getDragRect().setRectangle(
					calculateDrawRect(first, second));
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button == Input.MOUSE_LEFT_BUTTON
				&& getPanelLogic().getClickState() == ClickState.DEFAULT) {
			getPanelLogic().setClickState(ClickState.UNITSELECT_DRAGGING);
			startPoint = new Point(x, y);
			return;
		}
		if (getPanelLogic().getClickState() == ClickState.SELECT_TARGET_FOR_SPELL) {
			if (button == Input.MOUSE_LEFT_BUTTON) {
				Vector2f clickGamePoint = getPanelLogic()
						.computeGuiPointToGameCoordinate(new Vector2f(x, y));
				InformationProvider infoPro = EdurasInitializer.getInstance()
						.getInformationProvider();

				LinkedList<GameObject> obs = new LinkedList<GameObject>(
						infoPro.findObjectsAt(clickGamePoint));
				PlayerMainFigure player;
				try {
					player = infoPro.getPlayer().getPlayerMainFigure();
				} catch (ObjectNotFoundException e) {
					L.log(Level.SEVERE,
							"Player not found while selecting healtarget.", e);
					return;
				}
				if (obs.isEmpty()) {
					SoundMachine.play(SoundType.ERROR);
					return;
				}
				for (GameObject gameObject : obs) {
					if (gameObject.isUnit()
							&& infoPro.getGameMode().getRelation(gameObject,
									player) == Relation.ALLIED) {
						try {
							getListener().onUnitSpell((Unit) gameObject);
							actionDone();
							getPanelLogic().setClickState(ClickState.DEFAULT);
						} catch (ActionFailedException e) {
							getPanelLogic().onActionFailed(e);
							return;
						}
						return;
					}
				}
			}
			getPanelLogic().setClickState(ClickState.DEFAULT);
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		Vector2f clickGamePoint = getPanelLogic()
				.computeGuiPointToGameCoordinate(new Vector2f(x, y));
		if (button == Input.MOUSE_RIGHT_BUTTON) {
			getPanelLogic().setClickState(ClickState.DEFAULT);
			actionDone();
		}
		switch (getPanelLogic().getClickState()) {
		case DEFAULT:
			if (button == Input.MOUSE_RIGHT_BUTTON) {
				getListener().sendSelectedUnits(clickGamePoint);
			}
			break;
		case UNITSELECT_DRAGGING:
			if (button == Input.MOUSE_LEFT_BUTTON) {
				stopSelectDragging(getPanelLogic()
						.computeGuiPointToGameCoordinate(new Vector2f(x, y)));
			}
			break;
		case SELECT_POSITION_FOR_SCOUT:
			if (button == Input.MOUSE_LEFT_BUTTON) {
				try {
					getListener().onSpawnScout(clickGamePoint);
					actionDone();
				} catch (InsufficientResourceException e) {
					getPanelLogic().onActionFailed(e);
					return;
				}
			}
			getListener().setClickState(ClickState.DEFAULT);
			break;
		case SELECT_POSITION_FOR_ITEMSPAWN:
			if (button == Input.MOUSE_LEFT_BUTTON) {
				try {
					getListener().onSpawnItem(
							EdurasInitializer.getInstance()
									.getInformationProvider().getClientData()
									.getTypeOfItemToSpawn(), clickGamePoint);
					actionDone();
				} catch (WrongObjectTypeException e) {
					L.log(Level.WARNING, "Cannot spawn this object type!!", e);
					return;
				} catch (InsufficientResourceException | CantSpawnHereException e) {
					getPanelLogic().onActionFailed(e);
					return;
				}
			}
			getListener().setClickState(ClickState.DEFAULT);

			break;
		case SELECT_POSITION_FOR_OBSERVERUNIT:
			if (button == Input.MOUSE_LEFT_BUTTON) {
				InformationProvider infoPro = EdurasInitializer.getInstance()
						.getInformationProvider();
				Player player;
				try {
					player = infoPro.getPlayer();
				} catch (ObjectNotFoundException e) {
					L.log(Level.SEVERE,
							"Could not find player while spawning observer unit.",
							e);
					return;
				}
				LinkedList<GameObject> obs = new LinkedList<GameObject>(
						EdurasInitializer.getInstance()
								.getInformationProvider()
								.findObjectsAt(clickGamePoint));

				for (GameObject gameObject : obs) {
					if (gameObject instanceof Base) {

						try {
							Team ownerTeamOfBase = ((Base) gameObject)
									.getCurrentOwnerTeam();
							if (ownerTeamOfBase != null
									&& ownerTeamOfBase.equals(player.getTeam())) {
								getListener().onUnitSpawned(
										ObjectType.OBSERVER, (Base) gameObject);
								actionDone();
								getPanelLogic().setClickState(
										ClickState.DEFAULT);

								return;
							} else {
								getPanelLogic()
										.showNotification(
												"You can only spawn observers in bases your team owns.");
								SoundMachine.play(SoundType.ERROR);
								return;
							}
						} catch (PlayerHasNoTeamException e) {
							L.log(Level.WARNING,
									"Player doesn't have a team when trying to select position for observer unit!",
									e);
						} catch (InsufficientResourceException
								| CantSpawnHereException e) {
							getPanelLogic().onActionFailed(e);
							SoundMachine.play(SoundType.ERROR);
							return;
						}
					}
					getPanelLogic()
							.showNotification(
									"Please select a base owned by your team to spawn the unit");
					SoundMachine.play(SoundType.ERROR);
				}
				return;
			}
			getPanelLogic().setClickState(ClickState.DEFAULT);
			break;
		case SELECT_BASE_FOR_REZZ:
			if (button == Input.MOUSE_LEFT_BUTTON) {
				LinkedList<GameObject> obs = new LinkedList<GameObject>(
						EdurasInitializer.getInstance()
								.getInformationProvider()
								.findObjectsAt(clickGamePoint));

				for (GameObject gameObject : obs) {
					if (gameObject instanceof Base) {
						try {
							if (((Base) gameObject).getCurrentOwnerTeam()
									.equals(getPanelLogic().getClientData()
											.getCurrentResurrectTarget()
											.getTeam())) {

								getListener().onPlayerRezz(
										getPanelLogic().getClientData()
												.getCurrentResurrectTarget(),
										(Base) gameObject);
								actionDone();
								getPanelLogic().setClickState(
										ClickState.DEFAULT);

								return;
							} else {
								getPanelLogic()
										.showNotification(
												"You can only resurrect on bases your team owns.");
								SoundMachine.play(SoundType.ERROR);
								return;
							}
						} catch (PlayerHasNoTeamException e) {
							L.log(Level.WARNING,
									"Player doesn't have a team when selecting base for a player to be resurrected",
									e);
						} catch (InsufficientResourceException
								| CantSpawnHereException e) {
							getPanelLogic().onActionFailed(e);
							return;
						}
					}
				}
				getPanelLogic().showNotification(
						"Please select a base owned by your team to resurrect "
								+ getPanelLogic().getClientData()
										.getCurrentResurrectTarget().getName());
				SoundMachine.play(SoundType.ERROR);
				return;
			}
			getPanelLogic().setClickState(ClickState.DEFAULT);
			break;
		default:
			break;
		}
	}

	void stopSelectDragging(Vector2f end) {
		getPanelLogic().setClickState(ClickState.DEFAULT);
		Vector2f start = getPanelLogic().computeGuiPointToGameCoordinate(
				new Vector2f(startPoint.x, startPoint.y));

		Rectangle2D.Double r = calculateDragRect(start, end);
		getListener().onUnitsSelected(r,
				getPanelLogic().isKeyDown(Input.KEY_LSHIFT),
				getPanelLogic().isKeyDown(Input.KEY_LCONTROL));
		getPanelLogic().getDragRect().clear();
	}

	@Override
	public void mouseLost() {
		super.mouseLost();
		if (getPanelLogic().getClickState() == ClickState.UNITSELECT_DRAGGING)
			stopSelectDragging(getPanelLogic().getCurrentMousePos());
	}

	private void actionDone() {
		EdurasInitializer.getInstance().getInformationProvider()
				.getClientData().setCurrentActionSelected(-1);
	}

	@Override
	public void mouseWheelMoved(int change) {
		if (change > 0) {
			getPanelLogic().prevPage();
		} else {
			getPanelLogic().nextPage();
		}
	}

}
