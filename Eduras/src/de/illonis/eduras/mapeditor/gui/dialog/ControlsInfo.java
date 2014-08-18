package de.illonis.eduras.mapeditor.gui.dialog;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A simple help dialog that shows some static help text.
 * 
 * @author illonis
 * 
 */
public class ControlsInfo extends JDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates the dialog.
	 */
	public ControlsInfo() {
		setTitle("Help: Controls");
		JPanel content = (JPanel) getContentPane();
		content.setLayout(new BorderLayout());
		setModal(true);
		setSize(300, 300);

		JTextArea area = new JTextArea();
		area.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		area.setEditable(false);
		area.setWrapStyleWord(true);
		area.setLineWrap(true);
		area.setText("Select an item from the list on the left and then click in the map to place it.\n\n"
				+ "Rightclick any object to open a properties dialog.\n\n"
				+ "Go to Map->Properties to change map size.\n\n"
				+ "Hover a shape and press V to edit it\n\n"
				+ "Hover an object and press X to delete it\n\n"
				+ "Press WASD or hold right mousebutton to drag the map.\n\n"
				+ "drag objects by holding left mousebutton\n\n"
				+ "Rightclick to abort shape import");
		area.setCaretPosition(0);
		JScrollPane scroller = new JScrollPane(area);
		content.add(scroller, BorderLayout.CENTER);
	}

	@Override
	public void setVisible(boolean b) {
		setLocationRelativeTo(null);
		super.setVisible(b);
	}
}
