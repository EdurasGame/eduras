package de.illonis.eduras.networking;

import java.util.NoSuchElementException;

import de.illonis.eduras.Logic;

public class ServerLogic extends Thread {

	private Buffer inpBuffer;
	private Logic logic;

	public ServerLogic(Buffer inputBuffer, Logic logic) {
		this.logic = logic;
		this.inpBuffer = inputBuffer;

	}

	@Override
	public void run() {
		while (true) {
			try {
				String s = fetchFromBuffer();
				// TODO: deserialize and send to logic
			} catch (NoSuchElementException e) {
				try {
					wait();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			try {
				Thread.sleep(33);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Fetches next String from Buffer.
	 * 
	 * @return next String
	 * @throws NoSuchElementException
	 *             Returned when buffer is empty.
	 */
	private String fetchFromBuffer() throws NoSuchElementException {
		return inpBuffer.getNext();
	}
}