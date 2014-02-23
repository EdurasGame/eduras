package de.illonis.eduras.beta;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.illonis.eduras.gameclient.gui.login.UserInputField;
import de.illonis.eduras.images.ImageFiler;

/**
 * A dialog prompting for beta authentication with username and password.
 * 
 * @author illonis
 * 
 */
public class AuthenticationForm implements ActionListener {

	/**
	 * minimum length of a username.
	 */
	public final static int USERNAME_MINIMUM_LENGTH = 3;
	/**
	 * minimum length of a password.
	 */
	public final static int PASSWORD_MINIMUM_LENGTH = 6;

	private final JDialog loginFrame;
	private UserInputField usernameInput;
	private JPasswordField passwordInput;
	private final ImageIcon loadIcon;
	private ImageIcon logo;
	private final Image originalLogo;
	private final BetaAuthenticator handler;
	private JLabel logoLabel;
	private JButton loginButton;
	private final Dimension originalLogoDimension;
	private final DocumentListener docListener;
	private boolean authenticating;

	AuthenticationForm(BetaAuthenticator handler) {
		this.handler = handler;
		docListener = new DocListener();
		loginFrame = new JDialog((Window) null);
		loginFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		loginFrame.setModalityType(ModalityType.TOOLKIT_MODAL);
		loginFrame.setTitle("Eduras? Beta login");
		loginFrame.setModal(true);
		logo = ImageFiler.loadIcon("gui/login/logo_beta.png");
		originalLogoDimension = new Dimension(logo.getIconWidth(),
				logo.getIconHeight());
		originalLogo = logo.getImage();
		loadIcon = ImageFiler.loadIcon("gui/login/ajax-loader.gif");
		loginFrame.setSize(450, 350);
		authenticating = false;
		initGui();
	}

	void show() {
		loginFrame.setLocationRelativeTo(null);
		logoLabel.addComponentListener(new LogoResizer());
		loginFrame.setVisible(true);
	}

	void hide() {
		loginFrame.setVisible(false);
		loginFrame.dispose();
	}

	void onLoginStart() {
		authenticating = true;
		usernameInput.setEnabled(false);
		passwordInput.setEnabled(false);
		loginButton.setEnabled(false);
		logoLabel.setIcon(loadIcon);
	}

	void onLoginFailed() {
		authenticating = false;
		logoLabel.setIcon(logo);
		JOptionPane.showMessageDialog(loginFrame,
				"Your username/password combination is invalid.",
				"Login failed", JOptionPane.ERROR_MESSAGE);
		usernameInput.setEnabled(true);
		usernameInput.setText("");
		passwordInput.setEnabled(true);
		passwordInput.setText("");
	}

	private void initGui() {
		JPanel content = (JPanel) loginFrame.getContentPane();
		content.setLayout(new BorderLayout());
		logoLabel = new JLabel(logo);
		logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 5, 10, 5));
		content.add(logoLabel, BorderLayout.CENTER);

		JPanel authForm = new JPanel(new GridBagLayout());
		usernameInput = new UserInputField();
		usernameInput.addActionListener(this);
		usernameInput.getDocument().addDocumentListener(docListener);

		passwordInput = new JPasswordField();
		passwordInput.addActionListener(this);
		passwordInput.getDocument().addDocumentListener(docListener);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.gridwidth = 2;
		c.insets = new Insets(3, 3, 10, 3);
		c.fill = GridBagConstraints.HORIZONTAL;
		JLabel descLabel = new JLabel(
				"<html>Welcome to Eduras Beta Test.<br>Please login with your beta account credentials!</html>");
		descLabel.setHorizontalAlignment(JLabel.CENTER);
		authForm.add(descLabel, c);
		c.insets = new Insets(3, 3, 3, 3);
		c.gridy = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		JLabel usernameLabel = new JLabel("Username:");
		authForm.add(usernameLabel, c);
		c.gridy = 2;
		JLabel passwordLabel = new JLabel("Password:");
		authForm.add(passwordLabel, c);
		c.weightx = 2;
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		authForm.add(usernameInput, c);
		c.gridx = 1;
		c.gridy = 2;
		authForm.add(passwordInput, c);

		c.gridx = 1;
		c.gridy = 3;
		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		loginButton.setEnabled(false);
		authForm.add(loginButton, c);
		content.add(authForm, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String username = usernameInput.getText();
		char[] password = passwordInput.getPassword();
		if (!isValidUsername(username)) {
			JOptionPane.showMessageDialog(loginFrame, "Username too short",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!isValidPassword(password)) {
			JOptionPane.showMessageDialog(loginFrame, "Password too short",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		handler.authenticate(username, password);
	}

	private boolean isValidPassword(char[] password) {
		return password.length >= PASSWORD_MINIMUM_LENGTH;
	}

	private boolean isValidUsername(String username) {
		return username.length() >= USERNAME_MINIMUM_LENGTH;
	}

	private class LogoResizer extends ComponentAdapter {

		@Override
		public void componentResized(ComponentEvent e) {
			if (authenticating)
				return;
			Dimension targetDim = new Dimension(logoLabel.getWidth(),
					logoLabel.getHeight());
			Dimension scaledDim = getScaledDimension(originalLogoDimension,
					targetDim);
			if (targetDim.height < 5 || targetDim.width < 5)
				return;

			logo = new ImageIcon(ImageFiler.getScaledImage(originalLogo,
					scaledDim.width, scaledDim.height));
			logoLabel.setIcon(logo);
		}
	}

	private final static Dimension getScaledDimension(Dimension imgSize,
			Dimension boundary) {

		int original_width = imgSize.width;
		int original_height = imgSize.height;
		int bound_width = boundary.width;
		int bound_height = boundary.height;
		int new_width = original_width;
		int new_height = original_height;

		// first check if we need to scale width
		if (original_width > bound_width) {
			// scale width to fit
			new_width = bound_width;
			// scale height to maintain aspect ratio
			new_height = (new_width * original_height) / original_width;
		}

		// then check if we need to scale even with the new height
		if (new_height > bound_height) {
			// scale height to fit instead
			new_height = bound_height;
			// scale width to maintain aspect ratio
			new_width = (new_height * original_width) / original_height;
		}

		return new Dimension(new_width, new_height);
	}

	private class DocListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			verifyInput();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			verifyInput();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			verifyInput();
		}

		private void verifyInput() {
			java.awt.EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					loginButton.setEnabled(isInputValid());
				}
			});
		}

	}

	private boolean isInputValid() {
		return isValidPassword(passwordInput.getPassword())
				&& isValidUsername(usernameInput.getText());
	}

	/**
	 * Prefills credentials with given values and tests if they are formally
	 * valid.
	 * 
	 * @param username
	 *            the username.
	 * @param password
	 *            the password.
	 * @return true if credentials are formally valid, false otherwise.
	 */
	boolean prefill(String username, String password) {
		usernameInput.setText(username);
		passwordInput.setText(password);
		return isInputValid();
	}
}
