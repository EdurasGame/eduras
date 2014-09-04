package de.illonis.eduras.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCalculator {
	/**
	 * Computes SHA-256 hash for given file
	 * 
	 * @param file
	 *            the file to compute hash from.
	 * @return SHA-256 hash in hexadecimal format.
	 */
	public static String computeHash(Path file) {
		try {
			InputStream in = Files.newInputStream(file);
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
