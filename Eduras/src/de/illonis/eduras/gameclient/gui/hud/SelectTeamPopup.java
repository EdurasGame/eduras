package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;

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
					L.info("Team " + team.getName() + " selected.");
					try {
						if (!(team.equals(getInfo().getPlayer().getTeam())))
							getMouseHandler().teamSelected(team);
					} catch (PlayerHasNoTeamException | ObjectNotFoundException e) {
						L.log(Level.WARNING, "Cannot find team!", e);
					}
					setVisible(false);
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
