package de.illonis.eduras.shapecreator;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.illonis.eduras.shapecreator.gui.DrawPanel;
import de.illonis.eduras.shapecreator.gui.MenuPanel;
import de.illonis.eduras.shapecreator.gui.ToolPanel;
import de.illonis.eduras.shapecreator.gui.VerticeListPanel;

/**
 * A visual shape creator that allows easy creation and editing of shapes. It
 * provides a visual preview while creating a shape and displays warnings on
 * mistakes.
 * 
 * @author illonis
 * 
 */
public class ShapeCreator {
	private JFrame frame;
	private DataHolder data;
	private DrawPanel panel;
	private Renderer renderer;

	public ShapeCreator() {

		buildGui();
	}

	private void buildGui() {
		frame = new JFrame("Eduras? ShapeCreator");
		data = DataHolder.getInstance();
		panel = new DrawPanel();
		data.setDrawPanel(panel);
		VerticeListPanel verticePanel = new VerticeListPanel();
		JPanel framePanel = (JPanel) frame.getContentPane();
		PanelInteractor pi = new PanelInteractor(panel);

		FrameListener frameListener = new FrameListener();
		MenuTriggerer triggerer = new MenuTriggerer(frameListener, pi);

		ToolPanel toolPanel = new ToolPanel(pi);
		panel.addMouseListener(pi);
		panel.addMouseMotionListener(pi);
		panel.addMouseWheelListener(pi);
		frame.setJMenuBar(new MenuPanel(triggerer));
		framePanel.setLayout(new BorderLayout());
		framePanel.add(toolPanel, BorderLayout.NORTH);
		framePanel.add(panel, BorderLayout.CENTER);
		framePanel.add(verticePanel, BorderLayout.WEST);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(frameListener);
	}

	public class FrameListener extends WindowAdapter {

		public void tryExit() {
			int result = JOptionPane
					.showConfirmDialog(
							frame,
							"Do you really want to exit?\nUnsaved changes will be lost.",
							"Exiting", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				renderer.interrupt();
				frame.dispose();
			}
		}

		@Override
		public void windowClosing(WindowEvent e) {
			tryExit();
		}
	}

	private void showFrame() {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		data.loadPolygon(new EditablePolygon());
		renderer = new Renderer(panel);
		renderer.start();
	}

	public static void main(String[] args) {
		ShapeCreator creator = new ShapeCreator();
		creator.showFrame();
	}

}
