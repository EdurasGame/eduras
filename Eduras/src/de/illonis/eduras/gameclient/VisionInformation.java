package de.illonis.eduras.gameclient;

import java.awt.geom.Area;
import java.util.HashMap;

import de.illonis.eduras.Team;

public class VisionInformation {

	private final HashMap<Team, Area> teamVision;

	public VisionInformation() {
		teamVision = new HashMap<Team, Area>();
	}

	public void setAreaForTeam(Team team, Area area) {
		teamVision.put(team, area);
	}

	public void clear() {
		teamVision.clear();
	}

	public Area getVisionForTeam(Team team) {
		Area a = teamVision.get(team);
		if (a == null)
			return new Area();
		return a;
	}
}
