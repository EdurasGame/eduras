package de.illonis.eduras.gameclient.bugreport;

import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * Contains bug report data created by a user.
 * 
 * @author illonis
 * 
 */
public class BugReport {

	private final String reportingUser;
	private final String text;
	private final Date dateCreated;
	private String logFile;
	private BufferedImage screenshot;

	public BugReport(String reporter, String text, Date created) {
		this.reportingUser = reporter;
		this.text = text;
		this.dateCreated = created;
		logFile = "";
		screenshot = null;
	}

	public boolean hasLogFile() {
		return !logFile.isEmpty();
	}

	public boolean hasScreen() {
		return (screenshot != null);
	}

	public void attachLogFile(String logFile) {
		this.logFile = logFile;
	}

	public void attachScreen(BufferedImage screen) {
		this.screenshot = screen;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public String getLogFile() {
		return logFile;
	}

	public String getReportingUser() {
		return reportingUser;
	}

	public BufferedImage getScreenshot() {
		return screenshot;
	}

	public String getText() {
		return text;
	}

}
