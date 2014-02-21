package de.illonis.eduras.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides several methods to retrieve data from internet.
 * 
 * @author illonis
 * 
 */
public class WebFetcher {

	private WebFetcher() {

	}

	/**
	 * Performs a GET-Request to given url.
	 * 
	 * @param url
	 *            the requested url
	 * @param params
	 *            a key-value list of GET parameters (may be empty).
	 * @return the response.
	 * @throws UnsupportedEncodingException
	 *             if UTF-8 is not supported.
	 * @throws MalformedURLException
	 *             if url was invalid.
	 * @throws IOException
	 *             if a network error occured.
	 */
	public static String get(URL url, HashMap<String, String> params)
			throws UnsupportedEncodingException, MalformedURLException,
			IOException {

		String args = mapToArgs(params);

		URL u = new URL(url.toString() + "?" + args);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				u.openStream()));
		StringBuilder output = new StringBuilder();
		for (String line; (line = reader.readLine()) != null;) {
			output.append(line);
		}
		reader.close();

		return output.toString();
	}

	/**
	 * Performs a POST-Request to given url.
	 * 
	 * @param url
	 *            the requested url.
	 * @param params
	 *            a key-value list of POST-parameters (may be empty).
	 * @return the response.
	 * @throws UnsupportedEncodingException
	 *             if UTF-8 is not supported.
	 * @throws MalformedURLException
	 *             if url was invalid.
	 * @throws IOException
	 *             if a network error occured.
	 */
	public static String post(URL url, HashMap<String, String> params)
			throws UnsupportedEncodingException, MalformedURLException,
			IOException {
		String body = mapToArgs(params);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length",
				String.valueOf(body.length()));

		OutputStreamWriter writer = new OutputStreamWriter(
				connection.getOutputStream());
		writer.write(body);
		writer.flush();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		StringBuilder output = new StringBuilder();
		for (String line; (line = reader.readLine()) != null;) {
			output.append(line);
		}
		writer.close();
		reader.close();
		return output.toString();
	}

	private static String mapToArgs(HashMap<String, String> map)
			throws UnsupportedEncodingException {
		StringBuilder args = new StringBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			args.append(entry.getKey());
			args.append("=");
			args.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			args.append("&");
		}
		return args.toString();
	}
}
