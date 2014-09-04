package de.illonis.eduras.mapeditor.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.illonis.eduras.gameclient.datacache.CacheInfo.TextureKey;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.images.ImageFiler;

/**
 * A texture chooser.
 * 
 * @author illonis
 * 
 */
public class TextureChooser extends JPanel implements ListSelectionListener {

	private static final long serialVersionUID = 1L;
	private final GameObject object;
	private final JList<TextureKey> textureList;
	private final DefaultListModel<TextureKey> listModel;
	private final HashMap<TextureKey, ImageIcon> texturePreviewIcons;
	private final static Border SELECTED_BORDER = BorderFactory
			.createLineBorder(Color.black, 3);
	private final static Border EMPTY_BORDER = BorderFactory.createEmptyBorder(
			3, 3, 3, 3);

	/**
	 * Size of a single preview element.
	 */
	public final static int PREVIEW_SIZE = 100;

	public TextureChooser(GameObject object) {
		super(new BorderLayout());
		this.object = object;
		listModel = new DefaultListModel<TextureKey>();
		texturePreviewIcons = new HashMap<TextureKey, ImageIcon>();
		for (TextureKey texture : TextureKey.values()) {
			if (texture != TextureKey.NONE)
				listModel.addElement(texture);
		}
		textureList = new JList<TextureKey>(listModel);
		textureList.setCellRenderer(new TextureListRenderer());
		textureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		textureList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		textureList.addListSelectionListener(this);
		textureList.setVisibleRowCount(2);
		JScrollPane scroller = new JScrollPane(textureList);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(scroller, BorderLayout.CENTER);
		new TextureLoader().execute();
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (!event.getValueIsAdjusting()) {
			TextureKey texture = textureList.getSelectedValue();
			if (texture != null) {
				object.setTexture(texture);
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
			textureList.setSelectedValue(object.getTexture(), true);
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

	public TextureKey getSelectedTexture() {
		return textureList.getSelectedValue();
	}

}
