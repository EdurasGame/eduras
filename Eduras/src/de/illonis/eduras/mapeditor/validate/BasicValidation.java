package de.illonis.eduras.mapeditor.validate;

/**
 * Validates against basic map parameter requirements.
 * 
 * @author illonis
 * 
 */
public class BasicValidation extends ValidateTask {
	protected BasicValidation() {
		super("General map attributes");
	}

	@Override
	protected boolean performValidation() {
		boolean ok = true;
		if (data.getWidth() < 50 || data.getHeight() < 50) {
			addErrorMessage("Map is too small (minimum 50x50).");
			ok = false;
		}
		if (data.getAuthor().isEmpty()) {
			addErrorMessage("Author must not be empty.");
			ok = false;
		}
		if (data.getMapName().isEmpty()) {
			addErrorMessage("Map name must not be empty.");
			ok = false;
		}
		if (data.getSupportedGameModes().isEmpty()) {
			addErrorMessage("Must support at least one gamemode.");
			ok = false;
		}
		return ok;
	}
}
