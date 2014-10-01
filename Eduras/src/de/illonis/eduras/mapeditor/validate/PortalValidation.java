package de.illonis.eduras.mapeditor.validate;

import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.Portal;

/**
 * Checks if every portal has a partner portal.
 * 
 * @author illonis
 * 
 */
public class PortalValidation extends ValidateTask {
	protected PortalValidation() {
		super("Portals");
	}

	@Override
	protected boolean performValidation() {
		for (GameObject object : data.getGameObjects()) {
			if (object instanceof Portal) {
				if (((Portal) object).getPartnerPortal() == null) {
					addErrorMessage("All portals need a target portal.");
					return false;
				}
			}
		}
		return true;
	}
}
