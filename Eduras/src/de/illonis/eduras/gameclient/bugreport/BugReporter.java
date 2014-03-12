package de.illonis.eduras.gameclient.bugreport;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import de.illonis.eduras.utils.PathFinder;

/**
 * Sends a bug report to server.
 * 
 * @author illonis
 * 
 */
public class BugReporter extends SwingWorker<String, Void> {

	private final static String LOG_BASE_URL = "http://illonis.dyndns.org/pstatus/bugupload.php";
	private final BugReportFrame frame;
	private final BugReport report;
	private String message;

	BugReporter(BugReportFrame frame, BugReport report) {
		this.frame = frame;
		this.report = report;
		message = "not reported yet";
	}

	@Override
	protected String doInBackground() throws Exception {
		try {
			sendReport();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return message;
	}

	private void sendReport() throws MalformedURLException, IOException {

		// long byteTransferred = 0;
		long logLength = 0;
		long screenLength = 0;

		File screenFile = null;
		if (report.hasScreen()) {
			URI file = PathFinder.findFile("bugscreen.png");
			if (file == null) {
				message = "Could not locate game dir.";
				return;
			}
			screenFile = new File(file);
			try {
				ImageIO.write(report.getScreenshot(), "png", screenFile);
				screenLength = screenFile.length();
			} catch (IOException e) {
				message = "Could not save image: " + e.getMessage();
				e.printStackTrace();
				return;
			}
		}

		File log = null;
		if (report.hasLogFile()) {
			URI logFile = PathFinder.findFile(report.getLogFile());
			if (logFile == null) {
				message = "Could not locate game dir.";
				return;
			}
			log = new File(logFile);
			logLength = log.length();
		}

		String url = LOG_BASE_URL + "?user="
				+ URLEncoder.encode(report.getReportingUser(), "UTF-8") + "&";
		
		url += "screen=" + screenLength + "&";
		url += "log=" + logLength;
		
		HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(url)
				.openConnection();
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setRequestMethod("POST");
		OutputStream os = httpUrlConnection.getOutputStream();

		if (report.hasScreen()) {
			BufferedInputStream fis = new BufferedInputStream(
					new FileInputStream(screenFile));

			for (int i = 0; i < screenLength; i++) {
				os.write(fis.read());
			}
			fis.close();
		}

		if (report.hasLogFile()) {
			BufferedInputStream fis = new BufferedInputStream(
					new FileInputStream(log));

			for (int i = 0; i < logLength; i++) {
				os.write(fis.read());
			}
			fis.close();
		}
		os.write(report.getText().getBytes("UTF-8"));
		os.flush();
		os.close();
		System.out.println(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				httpUrlConnection.getInputStream()));

		String msg = "";
		String s = null;
		while ((s = in.readLine()) != null) {
			msg += s;
		}
		in.close();
		message = "<html>Your report has been sent. Thank You!<br><br>" + msg + "</html>";
	}

	@Override
	protected void done() {
		try {
			frame.onBugReportCompleted(get());
		} catch (InterruptedException | ExecutionException e) {
			frame.onBugReportCompleted("error completing task.");
		}
	}
}
