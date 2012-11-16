package de.illonis.eduras.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.illonis.eduras.exceptions.InvalidValueEnteredException;

/**
 * Provides a form for user input that asks for connection details and username.
 * Supports a basic error message display.
 * 
 * @author illonis
 * 
 */
public class ConnectDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField inputAddress, userNameField;
	private NumericTextField inputPort;
	private JButton okButton, abortButton;
	private InetAddress address;
	private int port;
	private JLabel errorLabel;
	private boolean aborted;

	/**
	 * Creates a new connect dialog.
	 * 
	 * @param gui
	 *            parent frame
	 */
	public ConnectDialog(JFrame gui) {
		super(gui, "Connect to server");
		buildGui();
		aborted = false;
	}

	/**
	 * Returns if abort button was pressed to close this dialog. This is used to
	 * distinguish between programatic and user close events.
	 * 
	 * @return true if abort button pressed, false otherwise.
	 */
	public boolean isAborted() {
		return aborted;
	}

	private void buildGui() {
		setModal(true);
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout());
		JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 10));
		inputAddress = new JTextField("localhost");
		userNameField = new JTextField("");
		inputPort = new NumericTextField(5);
		inputPort.setText(4387 + "");
		JLabel userLabel = new JLabel("User Name:");
		userLabel.setLabelFor(userNameField);
		JLabel addressLabel = new JLabel("Adresse:");
		addressLabel.setLabelFor(inputAddress);
		JLabel portLabel = new JLabel("Port:");
		portLabel.setLabelFor(inputPort);
		errorLabel = new JLabel("");
		errorLabel.setForeground(Color.red);
		errorLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		contentPane.add(errorLabel, BorderLayout.NORTH);
		inputPanel.add(userLabel);
		inputPanel.add(userNameField);
		inputPanel.add(addressLabel);
		inputPanel.add(inputAddress);
		inputPanel.add(portLabel);
		inputPanel.add(inputPort);
		contentPane.add(inputPanel, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel(new BorderLayout());
		okButton = new JButton("Verbinden");
		okButton.addActionListener(this);
		abortButton = new JButton("Abbrechen");
		abortButton.addActionListener(this);

		buttonPanel.add(abortButton, BorderLayout.WEST);
		buttonPanel.add(okButton, BorderLayout.EAST);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(getParent());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		aborted = false;
		setErrorMessage("");
		JButton source = (JButton) e.getSource();
		if (source == okButton) {
			if (inputPort.getValue() == 0)
				setErrorMessage("Invalid port number.");
			if (inputAddress.getText().length() < 5)
				setErrorMessage("Invalid target address.");
			if (userNameField.getText().length() < 3)
				setErrorMessage("Username too short.");

			if (!errorLabel.getText().isEmpty()) {
				setVisible(true);
				return;
			}

			try {
				address = InetAddress.getByName(inputAddress.getText());
			} catch (UnknownHostException e1) {
				setErrorMessage("URL invalid.");
				setVisible(true);
				return;
			}
			port = inputPort.getValue();

		} else if (source == abortButton) {
			aborted = true;
			setVisible(false);
		}
		errorLabel.setText("");
		setVisible(false);
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
	 * Returns port that was accepted by this dialog.
	 * 
	 * @returnaddress to connect to.
	 * @throws InvalidValueEnteredException
	 *             thrown when no port was entered (that is when abort is
	 *             pressed or port is 0).
	 */
	public int getPort() throws InvalidValueEnteredException {
		if (port <= 0)
			throw new InvalidValueEnteredException(
					"Invalid port number entered.");
		return port;
	}

	/**
	 * Sets error message that is displayed top of input form.
	 * 
	 * @param message
	 *            new error message.
	 */
	public void setErrorMessage(String message) {
		errorLabel.setText(message);
		pack();
	}

	/**
	 * Returns username that user put into input field.
	 * 
	 * @return username input.
	 * @throws InvalidValueEnteredException
	 *             if username is too short.
	 */
	public String getUserName() throws InvalidValueEnteredException {
		if (userNameField.getText().length() < 3)
			throw new InvalidValueEnteredException("Username too short.");
		return userNameField.getText();
	}
}