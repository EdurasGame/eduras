package de.illonis.eduras.gameclient.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;

import de.illonis.eduras.exceptions.InvalidValueEnteredException;
import de.illonis.eduras.gameclient.gui.animation.LoginAnimation;
import de.illonis.eduras.images.ImageFiler;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.networking.ServerClient.ClientRole;
import de.illonis.eduras.networking.discover.ServerFoundListener;
import de.illonis.eduras.networking.discover.ServerInfo;

/**
 * Displays login form.
 * 
 * @author illonis
 * 
 */
public class LoginPanel extends JPanel implements ActionListener,
		ServerFoundListener {

	private static final long serialVersionUID = 1L;
	private JButton connectButton;
	private JTextField userInput, hostInput;
	private NumericTextField portInput;
	private JComboBox<ClientRole> roleSelect;
	private int port;
	private String userName;
	private InetAddress address;
	private ActionListener listener;
	private JList<ServerInfo> serverList;
	private DefaultListModel<ServerInfo> serverData;
	private LoginAnimation titlePanel;

	/**
	 * Starts login animation.
	 */
	public void startAnimation() {
		titlePanel.start();
	}

	/**
	 * Stops login animation.
	 */
	public void stopAnimation() {
		titlePanel.stop();
	}

	/**
	 * Creates the login panel.
	 */
	public LoginPanel() {
		super();
		setBackground(Color.BLACK);
		setLayout(new BorderLayout());
		serverData = new DefaultListModel<ServerInfo>();
		buildGui();
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

	private void buildGui() {
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

		connectButton.addActionListener(this);
		userInput.addActionListener(this);
		hostInput.addActionListener(this);
		portInput.addActionListener(this);
		title.setAlignmentX(CENTER_ALIGNMENT);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setHorizontalTextPosition(SwingConstants.CENTER);
		add(titlePanel, BorderLayout.CENTER);
		add(form, BorderLayout.SOUTH);
	}

	/**
	 * Assigns an action listener to login panel that fires action events when
	 * hitting enter or clicking login button.
	 * 
	 * @param l
	 *            action listener to assign.
	 */
	public void setActionListener(ActionListener l) {
		listener = l;
	}

	/**
	 * Returns port entered by user.
	 * 
	 * @return port.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Returns the role that is selected by user.
	 * 
	 * @return selected role.
	 * 
	 * @author illonis
	 */
	public ClientRole getRole() {
		return (ClientRole) roleSelect.getSelectedItem();
	}

	/**
	 * Returns address that was accepted by this dialog.
	 * 
	 * @return address to connect to.
	 * @throws InvalidValueEnteredException
	 *             thrown when no address was entered (that is when abort is
	 *             pressed) or input did not have a valid url format.
	 */
	public InetAddress getAddress() throws InvalidValueEnteredException {
		if (address == null)
			throw new InvalidValueEnteredException(
					"Invalid address value given.");
		return address;
	}

	/**
	 * Returns username entered by user.
	 * 
	 * @return username.
	 */
	public String getUserName() {
		return userName;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			address = InetAddress.getByName(hostInput.getText());
		} catch (UnknownHostException e1) {
			return;
		}
		port = portInput.getValue();
		userName = userInput.getText().trim();
		listener.actionPerformed(e);
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

	@Override
	public void onServerFound(ServerInfo info) {
		if (!serverData.contains(info))
			serverData.addElement(info);
	}

	@Override
	public void onDiscoveryFailed() {
		serverData.clear();
		JOptionPane.showMessageDialog(this,
				Localization.getString("client.discovery.failed"));
	}
}