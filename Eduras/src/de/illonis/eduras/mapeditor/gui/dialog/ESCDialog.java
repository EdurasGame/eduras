package de.illonis.eduras.mapeditor.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

/**
 * A simple dialog that can be closed by pressing ESC.
 * 
 * @author illonis
 * 
 */
public class ESCDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	ESCDialog(JFrame parent) {
		super(parent);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
		getRootPane().getActionMap().put("close", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

}
