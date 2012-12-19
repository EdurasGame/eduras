package de.illonis.eduras.logger;

import de.illonis.eduras.logger.EduLog.LogMode;

public class TestClass {

	public TestClass() {

		net();
	}

	private void net() {
		EduLog.error("This is an error.");
		EduLog.info("some example warning");
	}

	public static void main(String[] args) {
		EduLog.setTrackDetail(2);
		LoggerGui g = new LoggerGui();
		EduLog.setLogOutput(LogMode.GUI);

		new TestClass();
		g.setVisible(true);
	}
}
