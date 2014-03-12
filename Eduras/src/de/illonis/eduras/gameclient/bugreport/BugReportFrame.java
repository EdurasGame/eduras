package de.illonis.eduras.gameclient.bugreport;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.gui.hud.UserInterface;
import de.illonis.eduras.images.ImageFiler;

/**
 * Input mask for reporting a bug from within the client.
 * 
 * @author illonis
 * 
 */
public class BugReportFrame implements ActionListener {

	private final static Logger L = EduLog.getLoggerFor(BugReportFrame.class
			.getName());

	private final static String REPORT_TEXT = "<html>Thank you for taking the time to report a bug!<br>"
			+ "This really helps us to make Eduras even more epic!<br><br>"
			+ "Please describe the bug below.</html>";

	final static String REPORTPANEL = "Reportp";
	final static String LOADPANEL = "Loadp";
	final static String RESULTPANEL = "Resultp";

	private final JFrame frame;
	private JTextArea textInput;
	private JButton sendButton;
	private JCheckBox attachLogBox;
	private JCheckBox attachScreenBox;
	private String username;
	private BufferedImage image;
	private JPanel content;
	private CardLayout layout;
	private JLabel resultLabel;
	private JButton closeButton;

	/**
	 * Creates a new Bugreport frame. You have to create a frame for each
	 * report.
	 */
	public BugReportFrame() {
		frame = new JFrame("Bug report");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		initGui();
	}

	private void initGui() {
		content = (JPanel) frame.getContentPane();
		layout = new CardLayout();
		content.setLayout(layout);
		JPanel reportPanel = new JPanel(new BorderLayout());

		frame.setSize(400, 500);

		JLabel text = new JLabel(REPORT_TEXT);
		text.setFont(text.getFont().deriveFont(Font.PLAIN));
		text.setBorder(BorderFactory.createEmptyBorder(5, 5, 15, 5));
		reportPanel.add(text, BorderLayout.NORTH);
		textInput = new JTextArea();
		textInput.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		textInput.setWrapStyleWord(true);
		textInput.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(textInput);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		reportPanel.add(scrollPane, BorderLayout.CENTER);

		attachLogBox = new JCheckBox("attach log file");
		attachScreenBox = new JCheckBox("attach map screenshot");

		JPanel bottomPanel = new JPanel(new BorderLayout());
		JPanel boxPanel = new JPanel();
		boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.PAGE_AXIS));
		boxPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		boxPanel.add(attachLogBox);
		boxPanel.add(attachScreenBox);
		sendButton = new JButton("Send");
		sendButton.addActionListener(this);
		sendButton.setActionCommand("send");
		bottomPanel.add(boxPanel, BorderLayout.CENTER);
		bottomPanel.add(sendButton, BorderLayout.SOUTH);
		reportPanel.add(bottomPanel, BorderLayout.SOUTH);
		reportPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		content.add(reportPanel, REPORTPANEL);

		JPanel loadPanel = new JPanel(new BorderLayout());
		ImageIcon loadIcon = ImageFiler.loadIcon("gui/login/ajax-loader.gif");
		JLabel loadLabel = new JLabel(loadIcon);
		loadPanel.add(loadLabel);
		content.add(loadPanel, LOADPANEL);

		JPanel resultPanel = new JPanel(new BorderLayout());
		resultLabel = new JLabel();
		resultLabel.setHorizontalAlignment(JLabel.CENTER);

		resultPanel.add(resultLabel, BorderLayout.CENTER);
		closeButton = new JButton("close");
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		resultPanel.add(closeButton, BorderLayout.SOUTH);
		content.add(resultPanel, RESULTPANEL);

	}

	/**
	 * Shows the frame.
	 * 
	 * @param ui
	 *            the associated userinterface.
	 */
	public void show(UserInterface ui) {
		try {
			username = ui.getInfos().getPlayer().getName();
		} catch (ObjectNotFoundException e) {
			username = "unknown";
			L.log(Level.WARNING, "Could not retrieve player's username.", e);
		}
		textInput.setText("");
		attachLogBox.setSelected(true);
		attachScreenBox.setSelected(true);
		frame.setLocationRelativeTo(null);
		image = ui.getScreenshot();
		frame.setVisible(true);
		/*
		 * JOptionPane.showMessageDialog( null, new JLabel(new
		 * ImageIcon(image.getScaledInstance( image.getWidth(null) / 2,
		 * image.getHeight(null) / 2, Image.SCALE_SMOOTH))));
		 */
	}

	/**
	 * Hides the frame.
	 */
	public void hide() {
		frame.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "send":
			BugReport report = new BugReport(username, textInput.getText(),
					new Date());
			layout.show(content, LOADPANEL);
			if (attachScreenBox.isSelected())
				report.attachScreen(image);
			if (attachLogBox.isSelected())
				report.attachLogFile(EduLog.getCurrentLogFile());
			BugReporter reporter = new BugReporter(this, report);

			reporter.execute();
			break;
		case "close":
			hide();
			break;
		default:
			break;
		}

	}

	void onBugReportCompleted(String msg) {
		resultLabel.setText(msg);
		layout.show(content, RESULTPANEL);
		closeButton.requestFocus();
	}
}
