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

	/**
	 * Create a new BugReport.
	 * 
	 * @param reporter
	 * @param text
	 * @param created
	 */
	public BugReport(String reporter, String text, Date created) {
		this.reportingUser = reporter;
		this.text = text;
		this.dateCreated = created;
		logFile = "";
		screenshot = null;
	}

	/**
	 * Returns false if the logfile is empty.
	 * 
	 * @return false if empty.
	 */
	public boolean hasLogFile() {
		return !logFile.isEmpty();
	}

	/**
	 * Tells whether the bug report contains a screenshot.
	 * 
	 * @return true if there is a screenshot
	 */
	public boolean hasScreen() {
		return (screenshot != null);
	}

	/**
	 * Attaches a log to the BugReport.
	 * 
	 * @param logFileToAttach
	 *            The log to attach
	 */
	public void attachLogFile(String logFileToAttach) {
		this.logFile = logFileToAttach;
	}

	/**
	 * Attaches the given screenshot to the BugReport.
	 * 
	 * @param screen
	 *            screenshot to attach
	 */
	public void attachScreen(BufferedImage screen) {
		this.screenshot = screen;
	}

	/**
	 * Returns the date on which the BugReport was created.
	 * 
	 * @return date
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * Returns the log attached to the BugReport.
	 * 
	 * @return The log as a string. May be empty.
	 */
	public String getLogFile() {
		return logFile;
	}

	/**
	 * Returns the user who reported the bug.
	 * 
	 * @return user
	 */
	public String getReportingUser() {
		return reportingUser;
	}

	/**
	 * Returns the screenshot attached to the bugreport
	 * 
	 * @return the screenshot
	 */
	public BufferedImage getScreenshot() {
		return screenshot;
	}

	/**
	 * Returns the text explaining the bug.
	 * 
	 * @return text
	 */
	public String getText() {
		return text;
	}

}
