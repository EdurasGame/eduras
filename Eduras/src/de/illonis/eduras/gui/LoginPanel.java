package de.illonis.eduras.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.illonis.eduras.exceptions.InvalidValueEnteredException;

/**
 * Displays login form.
 * 
 * @author illonis
 * 
 */
public class LoginPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton connectButton;
	private JTextField userInput, hostInput;
	private NumericTextField portInput;
	private int port;
	private InetAddress address;
	private ActionListener listener;

	public LoginPanel() {
		super();
		setLayout(new BorderLayout());
		buildGui();
	}

	private void buildGui() {
		JLabel title = new JLabel("Eduras?");
		title.setFont(title.getFont().deriveFont(30f));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JPanel form = new JPanel(new GridLayout(4, 2, 5, 10));
		JLabel userLabel = new JLabel("Benutzername:");
		JLabel hostLabel = new JLabel("Server-Adresse:");
		JLabel portLabel = new JLabel("Server-Port:");
		userInput = new JTextField();
		hostInput = new JTextField();
		portInput = new NumericTextField(5);
		userLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		hostLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		portLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		userLabel.setAlignmentX(RIGHT_ALIGNMENT);
		hostLabel.setAlignmentX(RIGHT_ALIGNMENT);
		portLabel.setAlignmentX(RIGHT_ALIGNMENT);
		userLabel.setHorizontalAlignment(JLabel.RIGHT);
		hostLabel.setHorizontalAlignment(JLabel.RIGHT);
		portLabel.setHorizontalAlignment(JLabel.RIGHT);
		userLabel.setLabelFor(userInput);
		portLabel.setLabelFor(portInput);
		hostLabel.setLabelFor(hostInput);
		userInput.getDocument().addDocumentListener(new UserNameChecker());
		form.add(userLabel);
		form.add(userInput);
		form.add(hostLabel);
		form.add(hostInput);
		form.add(portLabel);
		form.add(portInput);
		form.add(new JLabel());

		hostInput.setText("localhost");
		portInput.setText("4387");

		connectButton = new JButton("Verbinden");
		connectButton.setEnabled(false);
		form.add(connectButton);

		connectButton.addActionListener(this);
		userInput.addActionListener(this);
		hostInput.addActionListener(this);
		portInput.addActionListener(this);
		title.setAlignmentX(CENTER_ALIGNMENT);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setHorizontalTextPosition(SwingConstants.CENTER);
		add(title, BorderLayout.CENTER);
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
	 * Returns address that was accepted by this dialog.
	 * 
	 * @returnaddress to connect to.
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
		return userInput.getText();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			address = InetAddress.getByName(hostInput.getText());
		} catch (UnknownHostException e1) {
			return;
		}
		port = portInput.getValue();
		listener.actionPerformed(e);

	}

	private class UserNameChecker implements DocumentListener {

		private void check(DocumentEvent e) {
			connectButton.setEnabled(e.getDocument().getLength() > 2);
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
}
