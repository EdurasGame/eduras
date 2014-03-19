package de.illonis.eduras.gameclient.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.userprefs.KeyBindings;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameclient.userprefs.Settings;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logicabstraction.EdurasInitializer;

/**
 * A Window for user preferences.
 * 
 * @author illonis
 * 
 */
public class SettingsWindow extends JDialog {

	private final static Logger L = EduLog.getLoggerFor(SettingsWindow.class
			.getName());
	private static final long serialVersionUID = 1L;

	private final Settings settings;
	private JPanel bindingframe, settingsPanel;
	private NewKeyReader nkr;
	private JButton resetButton;

	/**
	 * Creates a new settings window.
	 */
	public SettingsWindow() {
		setModal(true);
		settings = EdurasInitializer.getInstance().getSettings();
		nkr = new NewKeyReader(this);
		getContentPane().setLayout(new BorderLayout());
		setTitle(Localization.getString("Client.preferences"));
		bindingframe = new JPanel();
		bindingframe
				.setLayout(new BoxLayout(bindingframe, BoxLayout.PAGE_AXIS));
		JScrollPane sp = new JScrollPane(bindingframe);

		settingsPanel = new JPanel();
		settingsPanel.setLayout(new BoxLayout(settingsPanel,
				BoxLayout.PAGE_AXIS));

		resetButton = new JButton(Localization.getString("Client.preferences.resetall"));
		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				settings.loadDefaults();
				builddisplay();
			}
		});
		settingsPanel.add(resetButton);
		getContentPane().add(sp, BorderLayout.CENTER);
		setSize(new Dimension(400, 500));
		JButton exitButton = new JButton(Localization.getString("Client.preferences.saveandclose"));
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					settings.save();
				} catch (IOException e1) {
					L.log(Level.WARNING, "Could not save user preferences.", e1);
				}
				dispose();
			}
		});
		getContentPane().add(exitButton, BorderLayout.SOUTH);
		getContentPane().add(settingsPanel, BorderLayout.NORTH);
		builddisplay();
		setLocationRelativeTo(null);
	}

	private void builddisplay() {
		bindingframe.removeAll();
		bindingframe.add(new JLabel(Localization.getString("Client.preferences.keybindings")));
		final KeyBindings keyBindings = settings.getKeyBindings();

		for (final KeyBinding kb : KeyBinding.values()) {
			String desc = keyBindings.getDescription(kb);
			String key = keyBindings.getBindingString(kb);
			JLabel l = new JLabel("<html><br>" + desc
					+ "<br><i>" + Localization.getString("Client.preferences.currentassigned") + ":</i> " + key);
			JButton b = new JButton(Localization.getString("Client.preferences.reset"));
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					keyBindings.resetToDefault(kb);
					builddisplay();
				}
			});
			JButton s = new JButton(Localization.getString("Client.preferences.assign"));
			s.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					nkr.listenForNewKey(kb);
					builddisplay();
				}
			});

			bindingframe.add(l);
			bindingframe.add(s);
			bindingframe.add(b);
		}
		bindingframe.revalidate();

		repaint();
	}
}
