package de.illonis.eduras.gameobjects;

import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Rectangle;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
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

			Collection<GameObject> objectsToConsider = getGame()
					.getAllCollidableObjects(object);
			objectsToConsider.removeAll(GameInformation
					.getAllItemsAndMissiles(objectsToConsider));
			if (GameInformation.isAnyOfObjectsWithinBounds(
					partnerPortal.getShape(), objectsToConsider)) {
				// do nothing, the portal isn't free
			} else {
				MoveableGameObject objectToBePorted = (MoveableGameObject) object;
				partnerPortal.justPortedObjects.add(objectToBePorted);
				getGame().getEventTriggerer()
						.guaranteeSetPositionOfObjectAtCenter(
								objectToBePorted.getId(),
								partnerPortal.getCenterPosition());
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
