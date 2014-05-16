package de.illonis.eduras.gameclient.gui.hud.nifty;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ButtonClickedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Handles gui interaction for {@link TestState}.
 * 
 * @author illonis
 * 
 */
public class TestController implements ScreenController {

	private Label textChangedLabel;

	/**
	 * Handles a textfield content change.<br>
	 * The annotation automatically registers the appropriate eventlistener.
	 * 
	 * @param id
	 *            the id of the textfield.
	 * @param event
	 *            the event.
	 */
	@NiftyEventSubscriber(id = "mainTextField")
	public void onTextfieldChange(final String id,
			final TextFieldChangedEvent event) {
		// sets the content of the textlabel to content of this textfield.
		textChangedLabel.setText(event.getText());
	}

	/**
	 * Handles a button click.
	 * 
	 * @param id
	 *            the id of the button.
	 * @param event
	 *            the event.
	 */
	@NiftyEventSubscriber(id = "appendButton")
	public void ButtonClickedEvent(final String id,
			final ButtonClickedEvent event) {
		System.out.println("button with id " + id + " clicked.");
	}

	@Override
	public void bind(Nifty nifty, Screen screen) {
		// get the textlabel from the xml file.
		this.textChangedLabel = screen.findNiftyControl("helloLabel",
				Label.class);

		// create a 8px height red panel (the border at bottom)
		PanelCreator createPanel = new PanelCreator();
		createPanel.setHeight("8px");
		createPanel.setBackgroundColor("#f00f");
		createPanel.create(nifty, screen, screen.findElementById("mainlayer"));
	}

	/**
	 * This method is called when the textfield is clicked. This was specified
	 * directly in the xml file.
	 */
	public void next() {
		System.out.println("next() clicked for element");
	}

	@Override
	public void onStartScreen() {
	}

	@Override
	public void onEndScreen() {
	}

}
