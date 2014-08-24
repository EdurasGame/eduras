package de.illonis.eduras.mapeditor.gui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import de.illonis.eduras.mapeditor.EditorException;
import de.illonis.eduras.mapeditor.MapData;
import de.illonis.eduras.mapeditor.NodeConnection;
import de.illonis.eduras.mapeditor.gui.EditorWindow;
import de.illonis.eduras.maps.NodeData;

/**
 * Allows connecting nodes.
 * 
 * @author illonis
 * 
 */
public class ManageNodesDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final MapData data;
	private JList<NodeConnection> connections;
	private ConnectionModel connectionData;
	private JComboBox<NodeData> nodeAs;
	private JComboBox<NodeData> nodeBs;
	private JButton addButton;
	private JButton removeButton;
	private JButton saveButton;
	private JButton cancelButton;

	/**
	 * Creates a node connection edit dialog.
	 * 
	 * @param window
	 *            the parent window.
	 * @throws EditorException
	 *             if nodes on map are less than two or do not have reference
	 *             names.
	 */
	public ManageNodesDialog(EditorWindow window) throws EditorException {
		super(window);
		data = MapData.getInstance();
		if (data.getBases().size() < 2) {
			throw new EditorException(
					"Map must have at least two nodes to build connections.");
		}
		for (NodeData node : data.getBases()) {
			if (node.getRefName().isEmpty()) {
				throw new EditorException(
						"All nodes must have reference names.\nPlease set them in General tab on properties dialog.");
			}
		}
		setModal(false);
		buildGui();
		pack();
		setTitle("Manage node connections");
	}

	private class ConnectionModel extends DefaultListModel<NodeConnection> {

		private static final long serialVersionUID = 1L;

		void addConnection(NodeData a, NodeData b) throws EditorException {
			if (a.equals(b)) {
				throw new EditorException("Cannot connect a node with itself.");
			}
			NodeConnection newConn = new NodeConnection(a, b);
			if (connectionData.contains(newConn))
				return;
			else
				connectionData.addElement(newConn);
		}
	}

	private void buildGui() {
		JPanel content = (JPanel) getContentPane();
		content.setLayout(new BorderLayout());
		Border border = BorderFactory.createEmptyBorder(8, 8, 8, 8);
		content.setBorder(border);

		JPanel listPanel = new JPanel(new BorderLayout());
		connectionData = new ConnectionModel();
		NodeData[] nodes = data.getBases().toArray(new NodeData[] {});
		nodeAs = new JComboBox<>(nodes);
		nodeBs = new JComboBox<>(nodes);
		nodeBs.setSelectedIndex(1);
		addButton = new JButton("Add");
		removeButton = new JButton("remove selected");
		addButton.addActionListener(this);
		JPanel addPanel = new JPanel();
		addPanel.add(new JLabel("connect"));
		addPanel.add(nodeAs);
		addPanel.add(new JLabel("with"));
		addPanel.add(nodeBs);
		addPanel.add(addButton);
		removeButton.addActionListener(this);
		for (NodeData node : data.getBases()) {
			for (NodeData adjacent : node.getAdjacentNodes()) {
				try {
					connectionData.addConnection(node, adjacent);
				} catch (EditorException e) {
					// should never happen
					e.printStackTrace();
				}
			}
		}
		connections = new JList<NodeConnection>(connectionData);
		JScrollPane scroller = new JScrollPane(connections);
		connections.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		connections.setVisibleRowCount(7);
		listPanel.add(scroller, BorderLayout.CENTER);
		listPanel.add(removeButton, BorderLayout.SOUTH);
		JPanel buttonsPanel = new JPanel(new BorderLayout());
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		buttonsPanel.add(saveButton, BorderLayout.WEST);
		buttonsPanel.add(cancelButton, BorderLayout.EAST);
		buttonsPanel.setBorder(border);
		content.add(addPanel, BorderLayout.NORTH);
		content.add(listPanel, BorderLayout.CENTER);
		content.add(buttonsPanel, BorderLayout.SOUTH);
	}

	@Override
	public void setVisible(boolean b) {
		setLocationRelativeTo(getParent());
		super.setVisible(b);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		if (button == addButton) {
			try {
				connectionData.addConnection(
						(NodeData) nodeAs.getSelectedItem(),
						(NodeData) nodeBs.getSelectedItem());
			} catch (EditorException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(),
						"Error adding connection", JOptionPane.ERROR_MESSAGE);
			}
		} else if (button == saveButton) {
			for (NodeData node : data.getBases()) {
				node.getAdjacentNodes().clear();
			}
			for (int i = 0; i < connectionData.size(); i++) {
				NodeConnection connection = connectionData.get(i);
				connection.getA().addAdjacentNode(connection.getB());
			}
		} else if (button == removeButton) {
			if (connections.getSelectedIndex() != -1)
				connectionData.remove(connections.getSelectedIndex());
		} else if (button == cancelButton) {
			dispose();
		}
	}
}
