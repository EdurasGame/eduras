package de.illonis.eduras.gameclient.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import de.illonis.eduras.gameclient.userprefs.KeyBindings;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.logicabstraction.EdurasInitializer;

/**
 * Listening to a new key while setting a keybinding is cool...
 * 
 * @author illonis
 * 
 */
public class NewKeyReader extends JDialog implements KeyListener {

	private static final long serialVersionUID = 1L;
	private final KeyBindings kb;
	private KeyBinding currentBinding;
	private final JLabel label;
	private final String hint = "<html><div style=\"text-align:center;\">Dr\u00FCcken Sie eine Taste,<br>um Sie der Funktion<br>\"%s\"<br>zuzuordnen.</div></html>";

	public NewKeyReader(SettingsWindow w) {
		super(w, true);
		kb = EdurasInitializer.getInstance().getSettings().getKeyBindings();
		addKeyListener(this);
		setTitle("Neu belegen");
		getContentPane().setLayout(new BorderLayout());
		label = new JLabel(hint, SwingConstants.CENTER);
		label.setHorizontalTextPosition(SwingConstants.CENTER);

		label.setBorder(new EmptyBorder(10, 10, 10, 10));
		JButton abort = new JButton(Localization.getString("Common.abort"));
		abort.setFocusable(false);
		abort.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		getContentPane().add(label, BorderLayout.CENTER);
		getContentPane().add(abort, BorderLayout.SOUTH);
		setResizable(false);
	}

	/**
	 * Starts listening for a key pressed to assign it to given binding
	 * 
	 * @param binding
	 *            key will be assigned to this binding
	 */
	public void listenForNewKey(KeyBinding binding) {
		currentBinding = binding;
		String desc = kb.getDescription(binding);
		label.setText(String.format(hint, desc));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key != KeyEvent.VK_ESCAPE && key != KeyEvent.VK_ENTER) {
			kb.setKeyBinding(currentBinding, key);
		}
		setVisible(false);
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
