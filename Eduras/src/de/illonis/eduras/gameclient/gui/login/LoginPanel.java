package de.illonis.eduras.gameclient.gui.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;

import de.illonis.eduras.gameclient.gui.animation.LoginAnimation;
import de.illonis.eduras.images.ImageFiler;
import de.illonis.eduras.networking.ServerClient.ClientRole;
import de.illonis.eduras.networking.discover.ServerInfo;

class LoginPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton connectButton;
	private JTextField userInput, hostInput;
	private NumericTextField portInput;
	private JComboBox<ClientRole> roleSelect;
	private JList<ServerInfo> serverList;
	private LoginAnimation titlePanel;

	LoginPanel(ListModel<ServerInfo> serverData, ActionListener listener) {
		super(new BorderLayout());

		setBackground(Color.BLACK);
		titlePanel = new LoginAnimation();
		JLabel title = new JLabel(ImageFiler.loadIcon("gui/login/logo.png"));
		titlePanel.add(title);
		// setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel form = new JPanel(new GridBagLayout());
		form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		serverList = new JList<ServerInfo>(serverData);
		serverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		serverList.setPreferredSize(new Dimension(300, 150));
		serverList.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		serverList.setCellRenderer(new ServerListRenderer());
		serverList.addListSelectionListener(new ServerSelectionListener());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.insets = new Insets(3, 3, 3, 3);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.gridwidth = 1;
		JLabel userLabel = new JLabel("Benutzername:");
		JLabel roleLabel = new JLabel("Rolle:");
		JLabel hostLabel = new JLabel("Server-Adresse:");
		JLabel portLabel = new JLabel("Server-Port:");
		userInput = new UserInputField();
		hostInput = new JTextField();
		portInput = new NumericTextField(5);
		roleSelect = new JComboBox<ClientRole>(ClientRole.values());
		userLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		roleLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		hostLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		portLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		userLabel.setAlignmentX(RIGHT_ALIGNMENT);
		roleLabel.setAlignmentX(RIGHT_ALIGNMENT);
		hostLabel.setAlignmentX(RIGHT_ALIGNMENT);
		portLabel.setAlignmentX(RIGHT_ALIGNMENT);
		userLabel.setHorizontalAlignment(JLabel.RIGHT);
		roleLabel.setHorizontalAlignment(JLabel.RIGHT);
		hostLabel.setHorizontalAlignment(JLabel.RIGHT);
		portLabel.setHorizontalAlignment(JLabel.RIGHT);
		userLabel.setLabelFor(userInput);
		roleLabel.setLabelFor(roleSelect);
		portLabel.setLabelFor(portInput);
		hostLabel.setLabelFor(hostInput);
		userInput.getDocument().addDocumentListener(new UserNameChecker());
		form.add(userLabel, c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		form.add(userInput, c);
		c.gridx = 2;
		c.anchor = GridBagConstraints.LINE_END;
		form.add(roleLabel, c);
		c.gridx = 3;
		c.anchor = GridBagConstraints.LINE_START;
		form.add(roleSelect, c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 4;
		form.add(serverList, c);
		c.gridwidth = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.LINE_END;
		form.add(hostLabel, c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		form.add(hostInput, c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_END;
		form.add(portLabel, c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		form.add(portInput, c);
		c.gridy = 3;

		// default values
		hostInput.setText("localhost");
		portInput.setText("4387");
		c.gridx = 2;
		c.gridwidth = 2;
		connectButton = new JButton("Verbinden");
		connectButton.setEnabled(false);
		form.add(connectButton, c);
		form.setBackground(new Color(.67f, .003f, .0015f, 1));

		// default user name
		Random r = new Random();
		userInput.setText("user" + r.nextInt(100));

		connectButton.addActionListener(listener);
		title.setAlignmentX(CENTER_ALIGNMENT);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setHorizontalTextPosition(SwingConstants.CENTER);
		add(titlePanel, BorderLayout.CENTER);
		add(form, BorderLayout.SOUTH);
	}

	String getUserName() {
		return userInput.getText().trim();
	}

	String getAddress() {
		return hostInput.getText().trim();
	}

	int getPort() throws NumberFormatException {
		return portInput.getValue();
	}

	ClientRole getRole() {
		return (ClientRole) roleSelect.getSelectedItem();
	}

	/**
	 * Ensures that login button is only available when entered user name is
	 * valid.
	 * 
	 * @see UserInputField
	 * 
	 * @author illonis
	 * 
	 */
	private class UserNameChecker implements DocumentListener {

		private void check(DocumentEvent e) {
			try {
				connectButton.setEnabled(e.getDocument()
						.getText(0, e.getDocument().getLength()).trim()
						.length() > 2);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			check(e);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			check(e);
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			check(e);
		}
	}

	private class ServerSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting())
				return;
			ServerInfo info = serverList.getSelectedValue();
			hostInput.setText(info.getUrl().getHostAddress());
			portInput.setText(info.getPort() + "");
		}
	}

	/**
	 * Starts login animation.
	 */
	void startAnimation() {
		titlePanel.start();
	}

	/**
	 * Stops login animation.
	 */
	void stopAnimation() {
		titlePanel.stop();
	}
}
