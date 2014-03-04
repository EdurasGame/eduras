package de.illonis.eduras.shapecreator.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Provides a gui to select a rotation.
 * 
 * @author illonis
 * 
 */
public class RotationSelector extends JDialog implements ActionListener,
		ChangeListener {

	private static final long serialVersionUID = 1L;
	private JSlider slider;
	private JButton okButton, abortButton;
	private boolean accepted = false;

	/**
	 * Creates a new rotation selector.
	 * 
	 * @param parent
	 *            the parent component.
	 */
	public RotationSelector(JFrame parent) {
		super(parent);
		setTitle("Select rotation value");
		setModal(true);
		buildGui();
	}

	private void buildGui() {
		JPanel panel = (JPanel) getContentPane();
		panel.setLayout(new BorderLayout());
		JLabel label = new JLabel("Please enter a rotation value (degree):");
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(label, BorderLayout.NORTH);
		slider = new JSlider(0, 360, 90);
		slider.setMajorTickSpacing(90);
		slider.setMinorTickSpacing(45);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.addChangeListener(this);

		panel.add(slider, BorderLayout.CENTER);
		okButton = new JButton("Rotate");
		abortButton = new JButton("Abort");

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		okButton.addActionListener(this);
		buttonPanel.add(abortButton);
		abortButton.addActionListener(this);
		panel.add(buttonPanel, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		accepted = (e.getSource() == okButton);
		setVisible(false);
	}

	/**
	 * @return true if user accepted rotation, false otherwise.
	 */
	public boolean isAccepted() {
		return accepted;
	}

	/**
	 * Returns the rotation value selected by user.<br>
	 * <i>Note:</i> This may not return a valid value if user aborted.
	 * 
	 * @return the selected value.
	 * @see #isAccepted()
	 */
	public int getValue() {
		return slider.getValue();
	}

	/**
	 * Shows the selector.
	 */
	public void showFrame() {
		slider.setValue(90);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		int value = source.getValue();
		okButton.setText("Rotate (" + value + "°)");
	}
}
