package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Team;

public class SelectTeamPopup extends DialoguePopup {

	private final static Logger L = EduLog.getLoggerFor(SelectTeamPopup.class
			.getName());

	protected SelectTeamPopup(UserInterface gui) {
		super(gui, "Select your team!");
	}

	@Override
	public void onTeamsSet(LinkedList<Team> teamList) {
		clearAnswers();

		for (final Team team : teamList) {
			addAnswer(new AnswerButton(team.getName()) {

				@Override
				protected void onClick() {
					System.out.println("Team " + team.getName() + " selected.");
				}
			});
		}

		addAnswer(new AnswerButton("cancel") {

			@Override
			protected void onClick() {
				setVisible(false);
			}
		});
	}
}
