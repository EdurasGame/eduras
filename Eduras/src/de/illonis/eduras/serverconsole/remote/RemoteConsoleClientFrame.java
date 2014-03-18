package de.illonis.eduras.serverconsole.remote;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.eduras.remote.RemoteException;
import de.illonis.edulog.EduLog;

public class RemoteConsoleClientFrame implements ActionListener {

	private final static Logger L = EduLog
			.getLoggerFor(RemoteConsoleClientFrame.class.getName());

	private JFrame frame;
	private JTextArea consoleOutput;
	private JTextField inputField;
	private JButton sendButton;
	private final EdurasRemoteConsoleClient client;
	private final static String PROMPT = "eduras-remote>";

	public RemoteConsoleClientFrame(EdurasRemoteConsoleClient client) {
		this.client = client;
		initGui();
	}

	private void initGui() {
		frame = new JFrame("RemoteClient");
		frame.setSize(600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		consoleOutput = new JTextArea();
		consoleOutput.setLineWrap(true);
		consoleOutput.setEditable(false);
		consoleOutput.setWrapStyleWord(true);
		consoleOutput.setBackground(Color.BLACK);
		consoleOutput.setForeground(Color.WHITE);
		consoleOutput.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
		JPanel content = (JPanel) frame.getContentPane();
		content.setLayout(new BorderLayout());
		JScrollPane areaScrollPane = new JScrollPane(consoleOutput);
		areaScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		areaScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		content.add(areaScrollPane, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		inputField = new JTextField();
		JLabel label = new JLabel(PROMPT);
		bottomPanel.add(label, BorderLayout.WEST);
		inputField.addActionListener(this);
		sendButton = new JButton("send");
		sendButton.addActionListener(this);
		bottomPanel.add(inputField, BorderLayout.CENTER);
		bottomPanel.add(sendButton, BorderLayout.EAST);
		content.add(bottomPanel, BorderLayout.SOUTH);
		sendButton.setEnabled(false);
		inputField.setEnabled(false);
	}

	public void show() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = inputField.getText();
		inputField.setText("");
		try {
			client.onCommand(command);
		} catch (RemoteException e) {
			L.log(Level.WARNING, "Received remote exception.", e);
		}
	}

	public void appendOutput(String text) {
		consoleOutput.append(text + System.lineSeparator());
		consoleOutput.setCaretPosition(consoleOutput.getDocument().getLength());
	}

	public void ready() {
		sendButton.setEnabled(true);
		inputField.setEnabled(true);
		inputField.requestFocus();
	}

}
