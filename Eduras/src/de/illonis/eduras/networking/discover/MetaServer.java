package de.illonis.eduras.networking.discover;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * Eduras Servers can register and deregister at the meta server, so they are
 * listed as available Eduras internet servers. Clients can send requests to the
 * meta server to retrieve a list of available Eduras online servers.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class MetaServer extends Thread {

	private final static Logger L = EduLog.getLoggerFor(MetaServer.class
			.getName());

	private boolean running = false;
	private int port = ServerDiscoveryListener.META_SERVER_PORT;
	private ServerSocket socket;

	static final String META_SERVER_ANSWER = "EDURAS_META_SERVER_ANSWER";
	private LinkedList<String> registeredServers;

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
		super("MetaServer");
		init();
	}

	private void init() throws IOException {
		registeredServers = new LinkedList<String>();
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
				L.log(Level.WARNING, "error sending meta server request", e);
				running = false;
				continue;
			}
		}

		try {
			deinit();
		} catch (IOException e) {
			L.log(Level.SEVERE, "error deinitializing", e);
		}
	}

	class MetaServerRequestHandler extends Thread {

		private Socket client;
		private PrintWriter clientWriter;

		public MetaServerRequestHandler(Socket newClient) {
			super("MetaServerRequestHandler");
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
				L.log(Level.SEVERE, "error sending to client", e);
				return;
			}

			while (running) {
				try {
					String message = messageReader.readLine();
					if (message != null) {
						handleMessage(message);
					}
				} catch (IOException e) {
					L.log(Level.SEVERE, "error reading message", e);
					return;
				}
			}
		}

		private void handleMessage(String message) {
			if (message.contains(GET_SERVERS_REQUEST)) {
				L.info("Received a GET_SERVERS request from address "
						+ client.getInetAddress().getHostAddress() + ".");

				String ipsOfRegisteredServers = META_SERVER_ANSWER;
				for (String singleServer : registeredServers) {
					ipsOfRegisteredServers = ipsOfRegisteredServers + "#"
							+ singleServer.split(":")[0] + "#"
							+ singleServer.split(":")[1];
				}

				clientWriter.println(ipsOfRegisteredServers);
			}

			if (message.contains(REGISTER_REQUEST)) {
				L.info("Received a REGISTER request from address "
						+ client.getInetAddress().getHostAddress() + ":"
						+ client.getPort() + ".");

				if (!registeredServers.contains(client.getInetAddress()))
					registeredServers.add(client.getInetAddress()
							.getHostAddress() + client.getPort());
			}

			if (message.contains(DEREGISTER_REQUEST)) {
				L.info("Received a DEREGISTER request from address "
						+ client.getInetAddress().getHostAddress() + ".");

				registeredServers.remove(client.getInetAddress()
						.getHostAddress() + client.getPort());
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
		try {
			EduLog.init("metaserver.log");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"error starting logging to metaserver.log");
		}

		EduLog.setBasicLogLimit(Level.ALL);
		EduLog.setConsoleLogLimit(Level.ALL);
		EduLog.setFileLogLimit(Level.ALL);
		MetaServer metaServer;
		try {
			if (args.length > 0) {

				metaServer = new MetaServer(Integer.parseInt(args[0]));

			} else {
				metaServer = new MetaServer();
			}
		} catch (NumberFormatException | IOException e) {
			L.log(Level.SEVERE, "error starting metaserver", e);
			return;
		}

		metaServer.start();
	}
}
