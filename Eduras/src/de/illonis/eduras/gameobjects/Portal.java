package de.illonis.eduras.gameobjects;

import java.util.LinkedList;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Rectangle;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.NoSpawnAvailableException;
import de.illonis.eduras.maps.SpawnPosition;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.settings.S;

public class Portal extends TriggerArea {

	private final static Logger L = EduLog.getLoggerFor(Portal.class.getName());

	public final static String OTHER_PORTAL_REFERENCE = "otherPortal";
	private Portal partnerPortal;
	private LinkedList<MoveableGameObject> justPortedObjects;

	public Portal(GameInformation game, TimingSource timingSource, int id) {
		super(game, timingSource, id);
		setObjectType(ObjectType.PORTAL);
		setShape(new Rectangle(0f, 0f, S.Server.go_portal_width,
				S.Server.go_portal_height));

		justPortedObjects = new LinkedList<MoveableGameObject>();
	}

	public void setPartnerPortal(Portal portal) {
		partnerPortal = portal;
	}

	public Portal getPartnerPortal() {
		return partnerPortal;
	}

	@Override
	public void onObjectEntered(GameObject object) {

		if (!(object instanceof MoveableGameObject)
				|| justPortedObjects.contains(object)) {
			return;
		}

		if (partnerPortal != null) {

			MoveableGameObject objectToBePorted = (MoveableGameObject) object;
			partnerPortal.justPortedObjects.add(objectToBePorted);

			LinkedList<GameObject> objectsToConsider = new LinkedList<GameObject>(
					getGame().getObjects().values());
			objectsToConsider.remove(partnerPortal);

			try {
				Vector2df positionToMoveTo = GameInformation
						.findFreePointWithinSpawnPositionForShape(
								new SpawnPosition((Rectangle) partnerPortal
										.getShape(), SpawnType.ANY),
								objectToBePorted.getShape(), objectsToConsider,
								GameInformation.ATTEMPT_PER_SPAWNPOINT);
				getGame().getEventTriggerer().guaranteeSetPositionOfObject(
						objectToBePorted.getId(), positionToMoveTo);

			} catch (NoSpawnAvailableException e) {
				// do nothing, if all the room is occupied
			}
		} else {
			// nothing happens if there is no partner portal
		}
	}

	@Override
	public void onObjectLeft(GameObject object) {
		if (justPortedObjects.contains(object)) {
			justPortedObjects.remove(object);
		}
	}
}
