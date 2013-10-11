package de.illonis.eduras.networking.discover;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import de.illonis.eduras.logger.EduLog;

/**
 * Eduras Servers can register and deregister at the meta server, so they are
 * listed as available Eduras internet servers. Clients can send requests to the
 * meta server to retrieve a list of available Eduras online servers.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class MetaServer extends Thread {

	private boolean running = false;
	private int port = ServerDiscoveryListener.META_SERVER_PORT;
	private ServerSocket socket;

	static final String META_SERVER_ANSWER = "EDURAS_META_SERVER_ANSWER";
	private LinkedList<InetAddress> registeredServers;

	/**
	 * The string that indicates a GET_SERVERS request.
	 */
	public static final String GET_SERVERS_REQUEST = "GET_SERVERS";

	/**
	 * The string that indicates a REGISTER request.
	 */
	public static final String REGISTER_REQUEST = "REGISTER";

	/**
	 * The string that indicates a DEREGISTER request.
	 */
	public static final String DEREGISTER_REQUEST = "DEREGISTER";

	/**
	 * Create a new Server that listens on the default port.
	 * 
	 * @throws IOException
	 */
	public MetaServer() throws IOException {
		init();
	}

	private void init() throws IOException {
		socket = new ServerSocket(port);
	}

	private void deinit() throws IOException {
		socket.close();
	}

	/**
	 * Create a new MetaServer that listens on the given port.
	 * 
	 * @param port
	 * @throws IOException
	 */
	public MetaServer(int port) throws IOException {
		this.port = port;
		init();
	}

	/**
	 * Stopping the meta server makes it stop listing to new clients and stop
	 * responding to clientrequests.
	 */
	public void stopMetaServer() {
		running = false;
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			try {
				Socket newClient = socket.accept();
				new MetaServerRequestHandler(newClient).start();
			} catch (IOException e) {
				EduLog.passException(e);
				running = false;
				continue;
			}
		}

		try {
			deinit();
		} catch (IOException e) {
			EduLog.passException(e);
		}
	}

	class MetaServerRequestHandler extends Thread {

		private Socket client;
		private PrintWriter clientWriter;

		public MetaServerRequestHandler(Socket newClient) {
			this.client = newClient;
		}

		@Override
		public void run() {
			BufferedReader messageReader = null;
			try {
				messageReader = new BufferedReader(new InputStreamReader(
						client.getInputStream()));
				clientWriter = new PrintWriter(client.getOutputStream(), true);
			} catch (IOException e) {
				EduLog.passException(e);
				return;
			}

			while (running) {
				try {
					String message = messageReader.readLine();
					handleMessage(message);
				} catch (IOException e) {
					EduLog.passException(e);
					return;
				}
			}
		}

		private void handleMessage(String message) {
			if (message.contains(GET_SERVERS_REQUEST)) {
				String ipsOfRegisteredServers = META_SERVER_ANSWER;
				for (InetAddress singleServer : registeredServers) {
					ipsOfRegisteredServers = ipsOfRegisteredServers + "#"
							+ singleServer.getHostAddress();
				}

				clientWriter.println(ipsOfRegisteredServers);
			}

			if (message.contains(REGISTER_REQUEST)) {
				if (!registeredServers.contains(client.getInetAddress()))
					registeredServers.add(client.getInetAddress());
			}

			if (message.contains(DEREGISTER_REQUEST)) {
				registeredServers.remove(client.getInetAddress());
			}
		}
	}

	/**
	 * Start the meta server.
	 * 
	 * @param args
	 *            1st argument: The port on which the meta server will listen.
	 */
	public static void main(String[] args) {
		MetaServer metaServer;
		try {
			if (args.length > 0) {

				metaServer = new MetaServer(Integer.parseInt(args[0]));

			} else {
				metaServer = new MetaServer();
			}
		} catch (NumberFormatException | IOException e) {
			EduLog.passException(e);
			return;
		}

		metaServer.start();
	}
}
