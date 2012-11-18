package de.illonis.eduras.logger;

public class TestClass {

	public TestClass() {

		net();
	}

	private void net() {
		EduLog.error("This is an error.");
		EduLog.info("some example warning");
	}

	public static void main(String[] args) {

		LoggerGui g = new LoggerGui();
		new TestClass();
		g.setVisible(true);
	}

}
