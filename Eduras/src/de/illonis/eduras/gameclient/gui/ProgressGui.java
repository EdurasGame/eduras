package de.illonis.eduras.gameclient.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import de.illonis.eduras.images.ImageFiler;
import de.illonis.eduras.networking.Client;

public class ProgressGui extends JPanel {
	private static final long serialVersionUID = 1L;

	private JLabel text;
	private ImageIcon icon;
	private JButton backButton;

	ProgressGui(ActionListener listener) {
		super(new BorderLayout());
		text = new JLabel();
		reset();
		text.setHorizontalTextPosition(SwingConstants.CENTER);
		text.setVerticalTextPosition(SwingConstants.BOTTOM);
		text.setAlignmentX(CENTER_ALIGNMENT);
		text.setHorizontalAlignment(JLabel.CENTER);
		text.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		icon = ImageFiler.loadIcon("gui/login/ajax-loader.gif");
		text.setIcon(icon);

		add(text, BorderLayout.CENTER);

		backButton = new JButton("Abbrechen");
		backButton.addActionListener(listener);
		add(backButton, BorderLayout.SOUTH);
	}

	void updateTimeMessage(int t) {
		text.setText("<html><center>Verbindung wird hergestellt...<br>Warte "
				+ t + "s</center></html>");
	}

	void setText(String text) {
		this.text.setText(text);
		repaint();
	}

	void onError(String msg) {
		text.setIcon(null);
		setText(msg);
	}

	/**
	 * Resets message.
	 */
	void reset() {
		updateTimeMessage(Client.CONNECT_TIMEOUT / 1000);
		text.setIcon(icon);
	}
}
