package de.illonis.eduras.gameclient.gui.login;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import de.illonis.eduras.networking.discover.ServerInfo;

/**
 * Renders the server information into the serverlist in client login dialog.
 * 
 * @author illonis
 * 
 */
public class ServerListRenderer extends JPanel implements
		ListCellRenderer<ServerInfo> {

	private static final long serialVersionUID = 1L;

	private final JLabel[] lbl = new JLabel[3];

	/**
	 * Creates a new list renderer.
	 */
	ServerListRenderer() {
		setLayout(new GridLayout(0, 3, 15, 0));
		lbl[0] = new JLabel("", JLabel.RIGHT);
		add(lbl[0]);
		lbl[1] = new JLabel("", JLabel.CENTER);
		add(lbl[1]);
		lbl[2] = new JLabel("", JLabel.LEFT);
		add(lbl[2]);
	}

	@Override
	public Component getListCellRendererComponent(
			JList<? extends ServerInfo> list, ServerInfo value, int index,
			boolean isSelected, boolean cellHasFocus) {
		lbl[0].setText(value.getName());
		lbl[1].setText(value.getUrl().getHostAddress());
		lbl[2].setText(value.getPort() + "");
		if (isSelected)
			setBackground(Color.CYAN);
		else
			setBackground(Color.WHITE);
		return this;
	}

}
