package de.illonis.eduras.gameclient;

import java.awt.Component;

import javax.swing.JOptionPane;

import de.illonis.eduras.locale.Localization;

public abstract class LoginStepLogic {

	public abstract void onShown();

	public abstract void onHidden();

	protected abstract Component getGui();

	protected final void showError(String message, String title) {
		JOptionPane.showMessageDialog(getGui(), message, title,
				JOptionPane.ERROR_MESSAGE);
	}

	protected final void showLocalizedError(String messageKey, String titleKey) {
		showError(Localization.getString(messageKey),
				Localization.getString(titleKey));
	}
}
