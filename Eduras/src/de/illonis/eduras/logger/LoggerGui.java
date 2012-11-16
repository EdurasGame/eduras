package de.illonis.eduras.logger;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.logging.Level;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.illonis.eduras.logger.EduLog.LogMode;

public class LoggerGui extends JFrame implements LogListener, ActionListener,
		ListSelectionListener {

	private static final long serialVersionUID = 1L;
	private JList<LogEntry> list;
	private DefaultListModel<LogEntry> model;
	private JComboBox<String> classSelector;
	private JComboBox<Level> levelSelector;
	private DefaultComboBoxModel<String> classModel;
	private DefaultComboBoxModel<Level> levelModel;
	private JTextArea details;

	public LoggerGui() {
		super("Eduras? logging result");
		setSize(700, 400);
		buildGui();
		EduLog.getInstance().addLogListener(this);
		EduLog.setLogOutput(LogMode.GUI, LogMode.CONSOLE);
	}

	private void initLevelSelector() {
		levelModel = new DefaultComboBoxModel<Level>();
		levelModel.addElement(Level.ALL);
		levelModel.addElement(Level.FINE);
		levelModel.addElement(Level.INFO);
		levelModel.addElement(Level.WARNING);
		levelModel.addElement(Level.SEVERE);
		levelSelector = new JComboBox<Level>(levelModel);
		levelSelector.addActionListener(this);
	}

	private void initClassSelector() {
		classModel = new DefaultComboBoxModel<String>();
		classModel.addElement("All");
		for (Iterator<String> iterator = EduLog.getInstance().getClasslist()
				.iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			classModel.addElement(type);
		}
		classSelector = new JComboBox<String>(classModel);
		classSelector.addActionListener(this);
	}

	private void buildGui() {
		JPanel p = (JPanel) getContentPane();
		p.setLayout(new BorderLayout());
		JPanel topPanel = new JPanel(new BorderLayout());
		model = new DefaultListModel<LogEntry>();

		JButton b = new JButton("test");

		initLevelSelector();
		initClassSelector();

		JPanel detailPanel = new JPanel(new BorderLayout());
		details = new JTextArea();
		details.setEditable(false);
		detailPanel.add(details, BorderLayout.CENTER);
		detailPanel.setSize(150, 100);
		// p.add(detailPanel, BorderLayout.EAST);
		topPanel.add(levelSelector, BorderLayout.WEST);
		topPanel.add(classSelector, BorderLayout.CENTER);

		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EduLog.warning("Some random message");
			}
		});

		p.add(topPanel, BorderLayout.NORTH);
		JPanel statuspanel = new JPanel();
		statuspanel.add(new JLabel("Started logging at "
				+ EduLog.getInstance().getStartDate().toString() + "."));
		p.add(statuspanel, BorderLayout.SOUTH);
		list = new JList<LogEntry>(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(this);
		JScrollPane scroller = new JScrollPane(detailPanel);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				list, scroller);
		splitPane.setDividerLocation(400);

		p.add(splitPane, BorderLayout.CENTER);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	@Override
	public void onNewLogEntry(LogEntry entry) {
		model.addElement(entry);
	}

	@Override
	public void logOutputTypeChanged(int newType) {
	}

	@Override
	public void logClassAdded(String className) {
		classModel.addElement(className);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String selectedClass = (String) classSelector.getSelectedItem();

		Level selectedLevel = (Level) levelSelector.getSelectedItem();
		filterItems(selectedLevel, selectedClass);

	}

	private void filterItems(Level l, String selectedClass) {
		model.removeAllElements();

		for (LogEntry e : EduLog.getInstance().getLogdata()) {
			if ((l == Level.ALL || e.getLevel().intValue() >= l.intValue())
					&& (selectedClass.equalsIgnoreCase("All") || e
							.containsClass(selectedClass))) {
				model.addElement(e);
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

		if (e.getValueIsAdjusting())
			return;

		int i = list.getSelectedIndex();
		if (i == -1 || i > model.size() - 1)
			return;

		onSelect(model.get(i));
	}

	private void onSelect(LogEntry logEntry) {
		details.setText(logEntry.toFullString());
	}
}
