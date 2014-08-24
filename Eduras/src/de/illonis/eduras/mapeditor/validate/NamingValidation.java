package de.illonis.eduras.mapeditor.validate;

import java.util.HashSet;
import java.util.Set;

import de.illonis.eduras.ReferencedEntity;

/**
 * Checks if any reference name occurs more than once.
 * 
 * @author illonis
 * 
 */
public class NamingValidation extends ValidateTask {

	private final Set<String> names = new HashSet<String>();

	protected NamingValidation() {
		super("Reference naming");
	}

	private boolean checkExists(String refName) {
		if (refName.isEmpty())
			return false;
		if (names.contains(refName)) {
			addErrorMessage("Reference \"" + refName + "\" is ambiguos.");
			return true;
		} else {
			names.add(refName);
			return false;
		}
	}

	@Override
	protected boolean performValidation() {
		names.clear();
		boolean ok = true;
		for (ReferencedEntity o : data.getGameObjects()) {
			if (checkExists(o.getRefName())) {
				ok = false;
			}
		}
		for (ReferencedEntity o : data.getBases()) {
			if (checkExists(o.getRefName())) {
				ok = false;
			}
		}
		for (ReferencedEntity o : data.getSpawnPoints()) {
			if (checkExists(o.getRefName())) {
				ok = false;
			}
		}
		return ok;
	}
}
