package de.illonis.eduras.mapeditor.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.illonis.eduras.gameclient.datacache.TextureInfo.TextureKey;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.images.ImageFiler;
import de.illonis.eduras.mapeditor.MapData;
import de.illonis.eduras.mapeditor.gui.FilteredListModel;
import de.illonis.eduras.mapeditor.gui.FilteredListModel.Filter;

/**
 * A texture chooser.
 * 
 * @author illonis
 * 
 */
public class TextureChooser extends JPanel implements ListSelectionListener,
		DocumentListener {

	private static final long serialVersionUID = 1L;
	private final GameObject object;
	private final JList<TextureKey> textureList;
	private final HashMap<TextureKey, ImageIcon> texturePreviewIcons;
	private final static Border SELECTED_BORDER = BorderFactory
			.createLineBorder(Color.black, 3);
	private final static Border EMPTY_BORDER = BorderFactory.createEmptyBorder(
			3, 3, 3, 3);
	private TextureFilter filter;
	private FilteredListModel<TextureKey> listModel;
	private final FilterTextField filterTextField;
	private TextureKey selectedValue;

	/**
	 * Size of a single preview element.
	 */
	public final static int PREVIEW_SIZE = 100;

	public TextureChooser(GameObject object) {
		super(new BorderLayout());
		this.object = object;

		final DefaultListModel<TextureKey> source = new DefaultListModel<TextureKey>();

		texturePreviewIcons = new HashMap<TextureKey, ImageIcon>();
		for (TextureKey texture : TextureKey.values()) {
			if (texture != TextureKey.NONE)
				source.addElement(texture);
		}
		filterTextField = new FilterTextField();
		filterTextField.getDocument().addDocumentListener(this);
		filter = new TextureFilter();
		listModel = new FilteredListModel<TextureKey>(source);
		listModel.setFilter(filter);
		textureList = new JList<TextureKey>(listModel);
		textureList.setCellRenderer(new TextureListRenderer());
		textureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		textureList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		textureList.addListSelectionListener(this);
		textureList.setVisibleRowCount(2);
		JScrollPane scroller = new JScrollPane(textureList);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(filterTextField, BorderLayout.NORTH);
		add(scroller, BorderLayout.CENTER);
		new TextureLoader().execute();
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (!event.getValueIsAdjusting()) {
			selectedValue = textureList.getSelectedValue();
			if (selectedValue != null) {
				if (object != null)
					object.setTexture(selectedValue);
				else {
					MapData.getInstance().setMapBackground(selectedValue);
				}
			}
		}
	}

	class TextureLoader extends
			SwingWorker<HashMap<TextureKey, ImageIcon>, Void> {

		@Override
		protected HashMap<TextureKey, ImageIcon> doInBackground()
				throws Exception {
			HashMap<TextureKey, ImageIcon> result = new HashMap<TextureKey, ImageIcon>();
			for (TextureKey texture : TextureKey.values()) {
				if (texture != TextureKey.NONE) {
					BufferedImage image = ImageIO.read(ImageFiler.class
							.getResourceAsStream("textures/"
									+ texture.getFile()));
					Image dimg = image.getScaledInstance(PREVIEW_SIZE,
							PREVIEW_SIZE, Image.SCALE_SMOOTH);
					ImageIcon imageIcon = new ImageIcon(dimg);
					result.put(texture, imageIcon);
				}
			}
			return result;
		}

		@Override
		protected void done() {
			texturePreviewIcons.clear();
			try {
				texturePreviewIcons.putAll(get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			if (object != null)
				textureList.setSelectedValue(object.getTexture(), true);
			else {
				textureList.setSelectedValue(MapData.getInstance()
						.getMapBackground(), true);
			}
		}
	}

	class TextureListRenderer extends JLabel implements
			ListCellRenderer<TextureKey> {
		private static final long serialVersionUID = 1L;

		public TextureListRenderer() {
			super("");
			Dimension dim = new Dimension(PREVIEW_SIZE, PREVIEW_SIZE);
			setMaximumSize(dim);
			setSize(dim);
		}

		@Override
		public Component getListCellRendererComponent(
				JList<? extends TextureKey> list, TextureKey value, int index,
				boolean isSelected, boolean cellHasFocus) {
			setIcon(texturePreviewIcons.get(value));
			if (isSelected) {
				setBorder(SELECTED_BORDER);
			} else {
				setBorder(EMPTY_BORDER);
			}
			return this;
		}
	}

	class TextureFilter implements Filter<TextureKey> {

		private String filterText;

		public TextureFilter() {
			filterText = "";
		}

		public void setFilterText(String filterText) {
			this.filterText = filterText;
		}

		@Override
		public boolean accept(TextureKey element) {
			if (filterText.isEmpty())
				return true;
			return element.name().contains(filterText)
					|| element.getFile().contains(filterText);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		textureList.setEnabled(enabled);
		filterTextField.setEnabled(enabled);
	}

	public TextureKey getSelectedTexture() {
		return selectedValue;
	}

	private void filter() {
		filter.setFilterText(filterTextField.getText());
		listModel.doFilter();
		try {
			textureList.setSelectedValue(selectedValue, false);
		} catch (IndexOutOfBoundsException e) {
		}
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		filter();
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		filter();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		filter();
	}

	class FilterTextField extends JTextField {

		private static final long serialVersionUID = 1L;
		private final String hintString = "Filter textures...";

		@Override
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			if (getText().isEmpty() && !isFocusOwner()) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setColor(Color.lightGray);
				g2.setFont(getFont().deriveFont(Font.ITALIC));
				int height = g2.getFontMetrics().getHeight();
				g2.drawString("Filter textures...", 5, height); // figure out x,
																// y from font's
				// FontMetrics and size of
				// component.
				g2.dispose();
			}
		}
	}

}
