package de.illonis.eduras.gui;

import java.awt.BorderLayout;
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

import de.illonis.eduras.exceptions.NoValueEnteredException;

public class ConnectDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField inputAddress;
	private NumericTextField inputPort;
	private JButton okButton, abortButton;
	private InetAddress address;
	private int port;
	private JLabel errorLabel;

	/**
	 * Creates a new connect dialog.
	 * 
	 * @param gui
	 *            parent frame
	 */
	public ConnectDialog(JFrame gui) {
		super(gui, "Connect to server");

		buildGui();
	}

	private void buildGui() {
		setModal(true);
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout());
		JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 10));
		inputAddress = new JTextField();
		inputPort = new NumericTextField(5);
		JLabel addressLabel = new JLabel("Adresse:");
		addressLabel.setLabelFor(inputAddress);
		JLabel portLabel = new JLabel("Port:");
		portLabel.setLabelFor(inputPort);
		errorLabel = new JLabel("");
		contentPane.add(errorLabel, BorderLayout.NORTH);
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
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		JButton source = (JButton) e.getSource();
		if (source == okButton) {
			if (inputPort.getValue() > 0 && inputAddress.getText().length() > 0) {
				setVisible(false);
				try {
					address = InetAddress.getByName(inputAddress.getText());
				} catch (UnknownHostException e1) {
					setErrorMessage("URL invalid.");
					setVisible(true);
					return;
				}
				port = inputPort.getValue();
			} else
				return;
		} else if (source == abortButton) {
			port = 0;
			address = null;
			setVisible(false);
		}

		errorLabel.setText("");
	}

	/**
	 * Returns address that was accepted by this dialog.
	 * 
	 * @returnaddress to connect to.
	 * @throws NoValueEnteredException
	 *             thrown when no address was entered (that is when abort is
	 *             pressed).
	 */
	public InetAddress getAddress() throws NoValueEnteredException {
		if (address == null)
			throw new NoValueEnteredException();
		return address;
	}

	/**
	 * Returns port that was accepted by this dialog.
	 * 
	 * @returnaddress to connect to.
	 * @throws NoValueEnteredException
	 *             thrown when no port was entered (that is when abort is
	 *             pressed or port is 0).
	 */
	public int getPort() throws NoValueEnteredException {
		if (port <= 0)
			throw new NoValueEnteredException();
		return port;
	}

	public void setErrorMessage(String message) {
		errorLabel.setText(message);
		pack();
	}
}