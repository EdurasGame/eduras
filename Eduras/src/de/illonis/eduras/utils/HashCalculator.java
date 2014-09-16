package de.illonis.eduras.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCalculator {
	/**
	 * Computes SHA-256 hash for given file
	 * 
	 * @param in
	 *            input stream of file to compute hash from.
	 * @return SHA-256 hash in hexadecimal format.
	 */
	public static String computeHash(InputStream in) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] md = new byte[8192];

			for (int n = 0; (n = in.read(md)) > -1;)
				digest.update(md, 0, n);

			byte[] hashed = digest.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : hashed) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (IOException | NoSuchAlgorithmException e) {
			return "";
		}
	}
}
