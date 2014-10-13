package de.illonis.eduras.shapecreator;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapecreator.gui.DrawPanel;
import de.illonis.eduras.shapecreator.gui.MenuPanel;
import de.illonis.eduras.shapecreator.gui.PreviewPanel;
import de.illonis.eduras.shapecreator.gui.ToolPanel;
import de.illonis.eduras.shapecreator.gui.VerticeListPanel;
import de.illonis.eduras.shapecreator.templates.TemplateNotFoundException;

/**
 * A visual shape creator that allows easy creation and editing of shapes. It
 * provides a visual preview while creating a shape and displays warnings on
 * mistakes.<br>
 * Simple shapes can be quickly created using templates.<br>
 * Shapes can be saved or loaded from shapefiles.
 * 
 * @author illonis
 * 
 */
public class ShapeCreator {
	private JFrame frame;
	private DataHolder data;
	private DrawPanel panel;
	private Renderer renderer, renderer2;
	private PreviewPanel previewPanel;

	private ShapeCreator() {
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

		ToolPanel toolPanel = new ToolPanel(pi);
		MenuTriggerer triggerer = new MenuTriggerer(toolPanel, frameListener,
				pi);

		JPanel rightPanel = new JPanel();
		BoxLayout layout = new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS);
		rightPanel.setLayout(layout);
		rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		JLabel label = new JLabel("1:1 Preview");
		label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		rightPanel.add(label);
		previewPanel = new PreviewPanel();
		rightPanel.add(previewPanel);

		panel.addMouseListener(pi);
		panel.addMouseMotionListener(pi);
		panel.addMouseWheelListener(pi);
		frame.setJMenuBar(new MenuPanel(triggerer, frame));
		framePanel.setLayout(new BorderLayout());
		framePanel.add(toolPanel, BorderLayout.NORTH);
		framePanel.add(panel, BorderLayout.CENTER);
		framePanel.add(rightPanel, BorderLayout.EAST);
		framePanel.add(verticePanel, BorderLayout.WEST);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(frameListener);
	}

	class FrameListener extends WindowAdapter {

		public void tryExit() {
			int result = JOptionPane
					.showConfirmDialog(
							frame,
							"Do you really want to exit?\nUnsaved changes will be lost.",
							"Exiting", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				renderer.interrupt();
				renderer2.interrupt();
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
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		EditablePolygon startPoly = new EditablePolygon();
		try {
			startPoly.importTemplate("Rectangle");
		} catch (TemplateNotFoundException e) {
		}
		data.loadPolygon(startPoly);
		renderer = new Renderer(panel);
		renderer.start();
		renderer2 = new Renderer(previewPanel);
		renderer2.start();
	}

	/**
	 * Starts the Eduras? shape creator.
	 * 
	 * @param args
	 *            <i>unused</i>
	 */
	public static void main(String[] args) {
		S.Client.localres = true;
		ShapeCreator creator = new ShapeCreator();
		creator.showFrame();
	}
}
