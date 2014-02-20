package de.illonis.eduras.gameclient.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

import de.illonis.eduras.locale.Localization;

/**
 * A basic logic that handles a gui-part of the game client.
 * 
 * @author illonis
 * 
 */
public abstract class ClientGuiStepLogic {

	/**
	 * Action triggered when controlled panel is shown.
	 */
	public abstract void onShown();

	/**
	 * Action triggered when controlled panel is hidden.
	 */
	public abstract void onHidden();

	/**
	 * @return the controlled {@link Component}.
	 */
	public abstract Component getGui();

	protected final void showError(String message, String title) {
		JOptionPane.showMessageDialog(getGui(), message, title,
				JOptionPane.ERROR_MESSAGE);
	}

	protected final void showLocalizedError(String messageKey, String titleKey) {
		showError(Localization.getString(messageKey),
				Localization.getString(titleKey));
	}
}
