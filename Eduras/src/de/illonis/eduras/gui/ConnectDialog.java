package de.illonis.eduras.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ConnectDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField inputAddress;
	private NumericTextField inputPort;
	private JButton okButton, abortButton;
	private URL address;

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
		contentPane.setLayout(new GridLayout(3, 2, 5, 10));
		inputAddress = new JTextField();
		inputPort = new NumericTextField(5);
		JLabel addressLabel = new JLabel("Adresse:");
		addressLabel.setLabelFor(inputAddress);
		JLabel portLabel = new JLabel("Port:");
		portLabel.setLabelFor(inputPort);
		contentPane.add(addressLabel);
		contentPane.add(inputAddress);
		contentPane.add(portLabel);
		contentPane.add(inputPort);
		okButton = new JButton("Verbinden");
		okButton.addActionListener(this);
		abortButton = new JButton("Abbrechen");
		abortButton.addActionListener(this);
		contentPane.add(abortButton);
		contentPane.add(okButton);
		pack();
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if (source == okButton) {
			try {
				address = new URL(inputAddress.getText() + ":" + inputPort.getValue());
			} catch (MalformedURLException e1) {
				JOptionPane.showMessageDialog(this, "URL invalid.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else if (source == abortButton) {
		}
		setVisible(false);
	}

	/**
	 * Returns address that was accepted by this dialog. Contains full address
	 * including port.
	 * 
	 * @return address to connect to.
	 */
	public URL getAddress() {
		return address;
	}

}