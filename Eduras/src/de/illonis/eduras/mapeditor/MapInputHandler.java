package de.illonis.eduras.mapeditor;

import javax.swing.JOptionPane;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.InputAdapter;

import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;
import de.illonis.eduras.mapeditor.MapInteractor.InteractType;
import de.illonis.eduras.shapecreator.EditablePolygon;

/**
 * Handles user input in map editor.
 * 
 * @author illonis
 * 
 */
public class MapInputHandler extends InputAdapter {

	enum InteractMode {
		SCROLL, DRAG, NONE, DRAG_SELECT;
	}

	private InteractMode mode;

	private final StatusListener status;
	private final MapInteractor interactor;
	private Rectangle dragRect;
	private Vector2f startPos;
	private Input input;

	MapInputHandler(MapInteractor interactor, StatusListener status) {
		this.status = status;
		this.interactor = interactor;
		dragRect = new Rectangle(0, 0, 0, 0);
		mode = InteractMode.NONE;
		startPos = new Vector2f();
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button == Input.MOUSE_RIGHT_BUTTON) {
			mode = InteractMode.SCROLL;
		} else if (button == Input.MOUSE_LEFT_BUTTON) {
			if (interactor.isAnythingAt(x, y)) {
				if (interactor.getInput().isKeyDown(Input.KEY_LSHIFT)) {
					interactor.toggleSelectionAt(x, y);
				} else {
					if (interactor.isSelected(x, y)) {
					} else {
						interactor.selectAt(x, y);
					}
					mode = InteractMode.DRAG;
					interactor
							.startDragging(interactor
									.computeGuiPointToGameCoordinate(new Vector2f(
											x, y)));

				}
			} else {
				mode = InteractMode.DRAG_SELECT;
				dragRect.setLocation(x, y);
				dragRect.setSize(0, 0);
				startPos.set(x, y);
				interactor.setDragRect(dragRect);
			}
		}
	}

	@Override
	public void mouseWheelMoved(int change) {
		if (interactor.getInput().isKeyDown(Input.KEY_LCONTROL)) {
			float amount;
			if (change < 0) {
				amount = 1f;
			} else {
				amount = -1f;
			}
			if (interactor.getInput().isKeyDown(Input.KEY_LSHIFT)) {
				amount *= 10;
			}
			interactor.rotateSelectedShapes(amount);
		} else {
			float amount;
			if (change > 0) {
				amount = 0.1f;
			} else {
				amount = -0.1f;
			}
			interactor.setZoom(interactor.getZoom() + amount);
		}
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if (button == Input.MOUSE_RIGHT_BUTTON) {
			if (interactor.getInteractType() == InteractType.PLACE_SHAPE
					|| interactor.getInteractType() == InteractType.PLACE_OBJECT
					|| interactor.getInteractType() == InteractType.PLACE_BASE
					|| interactor.getInteractType() == InteractType.PLACE_SPAWN) {
				interactor.setInteractType(InteractType.DEFAULT);
				MapData.getInstance().setPlacingObject(null);
			} else {
				interactor.selectAt(x, y);
				interactor.showPropertiesOfSelected();
			}
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		if (button == Input.MOUSE_LEFT_BUTTON) {
			if (mode == InteractMode.DRAG_SELECT) {
				if (!interactor.selectIn(dragRect, interactor.getInput()
						.isKeyDown(Input.KEY_LSHIFT))) {
					mode = InteractMode.NONE;
				}
				dragRect.setSize(0, 0);
			}
			if (mode == InteractMode.NONE) {
				switch (interactor.getInteractType()) {
				case PLACE_OBJECT:
					interactor.spawnAt(x, y);
					break;
				case PLACE_BASE:
					interactor.createBaseAt(x, y);
					break;
				case PLACE_SPAWN:
					interactor.createSpawnPointAt(x, y);
					break;
				case PLACE_SHAPE:
					if (button == Input.MOUSE_LEFT_BUTTON) {
						interactor.placeShapeAt(x, y);
					} else if (button == Input.MOUSE_RIGHT_BUTTON) {

					}
					break;
				case DEFAULT:
					break;
				default:
					break;
				}
			} else if (mode == InteractMode.DRAG_SELECT) {
				interactor.setInteractType(InteractType.DEFAULT);
			}
		} else {

		}
		if (mode == InteractMode.DRAG) {
			interactor.onStopDragging(interactor
					.computeGuiPointToGameCoordinate(new Vector2f(x, y)));
		}
		mode = InteractMode.NONE;
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		updateCoordinateStatus(newx, newy);
	}

	private void updateCoordinateStatus(int newx, int newy) {
		Vector2f mapCoord = interactor
				.computeGuiPointToGameCoordinate(new Vector2f(newx, newy));
		status.setCoordinate(mapCoord.x, mapCoord.y);
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (mode == InteractMode.SCROLL)
			interactor.scroll(oldx - newx, oldy - newy);
		else if (mode == InteractMode.DRAG) {
			interactor.dragSelected(newx - oldx, newy - oldy);
		} else if (mode == InteractMode.DRAG_SELECT) {
			float minX = Math.min(startPos.x, newx);
			float minY = Math.min(startPos.y, newy);
			float maxX = Math.max(startPos.x, newx);
			float maxY = Math.max(startPos.y, newy);
			dragRect.setBounds(minX, minY, maxX - minX, maxY - minY);
		}
		updateCoordinateStatus(newx, newy);
	}

	@Override
	public void keyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_A:
			interactor.startScrolling(Direction.LEFT);
			break;
		case Input.KEY_D:
			interactor.startScrolling(Direction.RIGHT);
			break;
		case Input.KEY_W:
			interactor.startScrolling(Direction.TOP);
			break;
		case Input.KEY_S:
			interactor.startScrolling(Direction.BOTTOM);
			break;
		case Input.KEY_X:
			interactor.deleteSelected();
			break;
		case Input.KEY_V:
			interactor.editSelectedShape();
			break;
		case Input.KEY_M:
			interactor.mirrorSelectedElements(EditablePolygon.X_AXIS);
			break;
		case Input.KEY_N:
			interactor.mirrorSelectedElements(EditablePolygon.Y_AXIS);
			break;
		case Input.KEY_C:
			interactor.copySelectedElements();
			break;
		case Input.KEY_F8:
			MapData.getInstance().setShowNodeConnections(
					!MapData.getInstance().isShowNodeConnections());
			break;
		case Input.KEY_F9:
			MapData.getInstance().setShowPortalLinks(
					!MapData.getInstance().isShowPortalLinks());
			break;
		case Input.KEY_Z:
			if (input.isKeyDown(Input.KEY_LCONTROL)) {
				if (input.isKeyDown(Input.KEY_LSHIFT)) {
					if (!interactor.redo()) {
						JOptionPane.showMessageDialog(null, "Nothing to redo");
					}
				} else
					if (!interactor.undo()) {
						JOptionPane.showMessageDialog(null, "Nothing to undo");
					}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void setInput(Input input) {
		super.setInput(input);
		this.input = input;
	}

	@Override
	public void keyReleased(int key, char c) {
		switch (key) {
		case Input.KEY_A:
			interactor.stopScrolling(Direction.LEFT);
			break;
		case Input.KEY_D:
			interactor.stopScrolling(Direction.RIGHT);
			break;
		case Input.KEY_W:
			interactor.stopScrolling(Direction.TOP);
			break;
		case Input.KEY_S:
			interactor.stopScrolling(Direction.BOTTOM);
			break;
		default:
			break;
		}
	}
}
