package de.illonis.eduras.gameclient.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

import de.illonis.eduras.locale.Localization;

public abstract class ClientGuiStepLogic {

	public abstract void onShown();

	public abstract void onHidden();

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
