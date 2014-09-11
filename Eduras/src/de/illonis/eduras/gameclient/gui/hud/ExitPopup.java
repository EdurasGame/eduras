package de.illonis.eduras.gameclient.gui.hud;

/**
 * An exit confirmation popup.
 * 
 * @author illonis
 * 
 */
public class ExitPopup extends DialoguePopup {

	protected ExitPopup(UserInterface gui) {
		super(gui, "Do you really want to exit?");
		addAnswer(new AnswerButton("Yes") {

			@Override
			protected void onClick() {
				getMouseHandler().exitRequested();
			}
		});
		addAnswer(new AnswerButton("No") {

			@Override
			protected void onClick() {
				setVisible(false);
			}
		});
	}

}
