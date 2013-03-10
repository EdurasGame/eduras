package de.illonis.eduras.gameclient.gui;

import java.awt.Component;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class FullScreenViewDemo implements ActionListener {

	private JComboBox<DisplayMode> comboBox;
	private GraphicsDevice device;

	public FullScreenViewDemo() {
		JFrame selectionFrame = new JFrame();

		JPanel p = new JPanel(new FlowLayout());
		System.out.println("Loading resolutions...");

		JLabel currentRes = new JLabel();
		p.add(currentRes);

		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Vector<DisplayMode> v = new Vector<DisplayMode>();

		GraphicsDevice[] devices = graphicsEnvironment.getScreenDevices();
		for (int cnt = 0; cnt < devices.length; cnt++) {
			DisplayMode m = devices[cnt].getDisplayMode();
			if (cnt == 0) {
				currentRes.setText("Current resolution: " + m.getWidth() + "x"
						+ m.getHeight() + " (" + m.getRefreshRate() + "Hz)");
				device = devices[0];

				for (int i = 0; i < devices[cnt].getDisplayModes().length; i++) {
					v.add(devices[cnt].getDisplayModes()[i]);
				}
			}
		}
		comboBox = new JComboBox<DisplayMode>(v);
		comboBox.setRenderer(new ListCellRenderer<DisplayMode>() {

			@Override
			public Component getListCellRendererComponent(
					JList<? extends DisplayMode> list, DisplayMode value,
					int index, boolean isSelected, boolean cellHasFocus) {
				return new JLabel(value.getWidth() + "x" + value.getHeight()
						+ " (" + value.getRefreshRate() + "Hz)");
			}
		});

		p.add(comboBox);

		selectionFrame.setContentPane(p);

		JButton b = new JButton("test");
		b.addActionListener(this);
		p.add(b);

		System.out.println("showing frame");
		selectionFrame.setTitle("Resolution tester");
		selectionFrame.pack();
		selectionFrame.setLocationRelativeTo(null);
		selectionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		selectionFrame.setVisible(true);
	}

	public static void main(String[] args) {
		new FullScreenViewDemo();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		FullScreenFrame f = new FullScreenFrame(device,
				(DisplayMode) comboBox.getSelectedItem());
		f.draw();
	}
}
