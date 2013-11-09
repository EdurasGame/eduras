package de.illonis.eduras.gameclient.gui.progress;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class LoadingPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JLabel text;
	private JProgressBar progressBar;

	LoadingPanel() {
		super(new BorderLayout());
		text = new JLabel();
		text.setHorizontalTextPosition(SwingConstants.CENTER);
		text.setVerticalTextPosition(SwingConstants.BOTTOM);
		text.setAlignmentX(CENTER_ALIGNMENT);
		text.setHorizontalAlignment(JLabel.CENTER);
		text.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		text.setText("Loading graphics...");
		progressBar = new JProgressBar();

		add(text, BorderLayout.CENTER);
		add(progressBar, BorderLayout.SOUTH);
	}

	public void setProgress(int progress) {
		progressBar.setValue(progress);
	}

}
