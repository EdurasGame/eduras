package de.illonis.eduras.mapeditor.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.illonis.eduras.images.ImageFiler;
import de.illonis.eduras.mapeditor.gui.EditorWindow;
import de.illonis.eduras.mapeditor.validate.MapValidator;
import de.illonis.eduras.mapeditor.validate.ValidateTask;

/**
 * Displays and triggers map validation. Validation is started automatically
 * when dialog is shown.
 * 
 * @author illonis
 * 
 */
public class ValidateDialog extends JDialog implements ComponentListener {

	private static final long serialVersionUID = 1L;
	private final TaskResultPanel[] panels;
	private final JLabel titleText;

	private final static ImageIcon ICON_ACTIVE = ImageFiler
			.loadIcon("mapeditor/test_active.png");
	private final static ImageIcon ICON_SUCCESS = ImageFiler
			.loadIcon("mapeditor/test_success.png");
	private final static ImageIcon ICON_FAILED = ImageFiler
			.loadIcon("mapeditor/test_failed.png");

	/**
	 * Create a new dialog.
	 * 
	 * @param parent
	 *            parent window.
	 */
	public ValidateDialog(EditorWindow parent) {
		super(parent);
		setTitle("Map validation");
		setModal(true);
		JPanel content = (JPanel) getContentPane();
		content.setLayout(new BorderLayout());
		titleText = new JLabel("Validating...");
		titleText.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		content.add(titleText, BorderLayout.NORTH);
		JPanel tests = new JPanel();
		tests.setLayout(new BoxLayout(tests, BoxLayout.PAGE_AXIS));
		content.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		panels = new TaskResultPanel[MapValidator.tasks.size()];
		for (int i = 0; i < MapValidator.tasks.size(); i++) {
			panels[i] = new TaskResultPanel(MapValidator.tasks.get(i));
			tests.add(panels[i]);
		}
		content.add(tests, BorderLayout.CENTER);
		addComponentListener(this);
	}

	@Override
	public void setVisible(boolean b) {
		pack();
		setLocationRelativeTo(null);
		super.setVisible(b);
	}

	/**
	 * Called when check is completed and gui should be updated.
	 */
	public void onCheckCompleted() {
		for (int i = 0; i < panels.length; i++) {
			panels[i].updateResult();
		}
		titleText.setText("Validation completed.");
		pack();
		revalidate();
	}

	class TaskResultPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final ValidateTask task;
		private JLabel desc;
		private JLabel icon;
		private JLabel error;

		public TaskResultPanel(ValidateTask validateTask) {
			this.task = validateTask;
			setLayout(new BorderLayout());
			desc = new JLabel(task.getDescription());
			icon = new JLabel(ICON_ACTIVE);
			error = new JLabel("");
			error.setBorder(BorderFactory.createEmptyBorder(0, 25, 10, 0));
			error.setForeground(Color.red);
			add(icon, BorderLayout.WEST);
			add(desc, BorderLayout.CENTER);
			add(error, BorderLayout.SOUTH);
		}

		void updateResult() {
			desc.setText(task.getDescription());
			String errorString = "<html>";
			for (String s : task.getErrorMessages()) {
				errorString += "- ";
				errorString += s;
				errorString += "<br>";
			}
			errorString += "</html>";
			error.setText(errorString);
			if (task.isSuccess()) {
				icon.setIcon(ICON_SUCCESS);
			} else {
				icon.setIcon(ICON_FAILED);
			}
		}
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		new MapValidator(this).execute();
	}
}
