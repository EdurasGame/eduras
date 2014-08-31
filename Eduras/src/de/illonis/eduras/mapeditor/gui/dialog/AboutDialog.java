package de.illonis.eduras.mapeditor.gui.dialog;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import de.illonis.eduras.mapeditor.MapEditor;

/**
 * A dialog that shows editor version.
 * 
 * @author illonis
 * 
 */
public class AboutDialog extends ESCDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * @param parent
	 *            parent window.
	 */
	public AboutDialog(JFrame parent) {
		super(parent);
		getContentPane().setLayout(new BorderLayout());
		setModal(true);
		setTitle("About Eduras? MapEditor");
		JLabel label = new JLabel("Version " + MapEditor.VERSION);
		label.setHorizontalAlignment(JLabel.CENTER);
		getContentPane().add(label, BorderLayout.CENTER);
		setSize(300, 300);
	}

	@Override
	public void setVisible(boolean b) {
		setLocationRelativeTo(getParent());
		super.setVisible(b);
	}

}
