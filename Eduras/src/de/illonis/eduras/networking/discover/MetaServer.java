package de.illonis.eduras.networking.discover;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.EventHandler;
import de.eduras.eventingserver.Server;
import de.eduras.eventingserver.ServerInterface;
import de.eduras.eventingserver.ServerNetworkEventHandler;
import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;
import de.eduras.eventingserver.test.NoSuchClientException;
import de.illonis.edulog.EduLog;

/**
 * Eduras Servers can register and deregister at the meta server, so they are
 * listed as available Eduras internet servers. Clients can send requests to the
 * meta server to retrieve a list of available Eduras online servers.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class MetaServer {

	private final static Logger L = EduLog.getLoggerFor(MetaServer.class
			.getName());

	private int port = ServerDiscoveryListener.META_SERVER_PORT;
	private ServerInterface server;

	private ConcurrentLinkedQueue<Integer> registeredServers;
	private ConcurrentHashMap<Integer, ServerInfo> serverInfos;
	private ConcurrentHashMap<Integer, Long> serverLeases;

	/**
	 * The event that indicates that the client requests the servers registered
	 * at the meta server.
	 * 
	 * #arg 0: clientId
	 */
	public static final int GET_SERVERS_REQUEST = 1;

	/**
	 * The event that indicates that an Eduras server wants to register at the
	 * meta server.
	 * 
	 * #arg 0: ServerID #arg 1: ServerName #arg 2: ServerIP #arg 3: ServerPort
	 */
	public static final int REGISTER_REQUEST = 2;

	/**
	 * The event that indicates that an Eduras server wants to deregister at the
	 * meta server.
	 * 
	 * #arg 0: ServerID
	 */
	public static final int DEREGISTER_REQUEST = 3;

	/**
	 * The event that gives information about available EdurasServers to a
	 * client.
	 * 
	 * #arg 0: number of servers #arg 1: name of server 1 #arg 2: ip of server 1
	 * #arg 3: port of server 1 #arg 4: name of server 2 ... and so on.
	 */
	public static final int GET_SERVERS_RESPONSE = 4;

	private static final long LEASE_TIME = 30000;

	/**
	 * Create a new Server that listens on the default port.
	 * 
	 * @throws IOException
	 */
	public MetaServer() throws IOException {
		init();
	}

	private void init() throws IOException {
		registeredServers = new ConcurrentLinkedQueue<Integer>();
		serverInfos = new ConcurrentHashMap<Integer, ServerInfo>();
		serverLeases = new ConcurrentHashMap<Integer, Long>();
		server = new Server();

		MetaServerRequestHandler requestHandler = new MetaServerRequestHandler();
		server.setEventHandler(requestHandler);
		server.setNetworkEventHandler(requestHandler);
		server.start("Eduras-Metaserver", port);

		new ServerRegistryLeaseChecker().start();
	}

	private void deinit() throws IOException {
		server.stop();
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
		server.stop();
	}

	private void removeServer(int id) {
		registeredServers.remove(id);
		serverInfos.remove(id);
		serverLeases.remove(id);
	}

	class MetaServerRequestHandler implements ServerNetworkEventHandler,
			EventHandler {

		@Override
		public void onClientDisconnected(int clientId) {
			if (registeredServers.contains(clientId)) {
				L.info("A registered Eduras server closed the connection and thus is deregistered.");
				registeredServers.remove(clientId);
				serverInfos.remove(clientId);
			}
		}

		@Override
		public void onClientConnected(int clientId) {
			L.info("Client " + clientId + " connected.");
		}

		@Override
		public void handleEvent(Event event) {
			L.fine("Received event #" + event.getEventNumber());
			switch (event.getEventNumber()) {
			case GET_SERVERS_REQUEST: {
				try {
					L.info("Received a GET_SERVERS request from client "
							+ event.getArgument(0));
				} catch (TooFewArgumentsExceptions e) {
					L.log(Level.WARNING,
							"Error when receiving GET_SERVERS_REQUEST", e);
				}

				Event metaserverAnswer = new Event(GET_SERVERS_RESPONSE);
				metaserverAnswer.putArgument(registeredServers.size());

				for (Integer singleServerId : registeredServers) {
					ServerInfo serverInfoForEdurasServer = serverInfos
							.get(singleServerId);

					metaserverAnswer.putArgument(serverInfoForEdurasServer
							.getName());
					metaserverAnswer.putArgument(serverInfoForEdurasServer
							.getUrl().getHostAddress());
					metaserverAnswer.putArgument(serverInfoForEdurasServer
							.getPort());
				}

				try {
					server.sendEventToClient(metaserverAnswer,
							(int) event.getArgument(0));
				} catch (IllegalArgumentException | NoSuchClientException
						| TooFewArgumentsExceptions e) {
					L.log(Level.WARNING,
							"Error when trying to send an GET_SERVERS_RESPONSE event from metaserver to client.",
							e);
				}

				break;
			}
			case REGISTER_REQUEST: {

				int clientId;
				String serverName = "";
				String ipOfEdurasServer = "";
				int portOfEdurasServer = 0;
				try {
					clientId = (Integer) event.getArgument(0);
					serverName = (String) event.getArgument(1);
					ipOfEdurasServer = (String) event.getArgument(2);
					portOfEdurasServer = (Integer) event.getArgument(3);
				} catch (TooFewArgumentsExceptions e) {
					L.log(Level.WARNING,
							"Error when accessing arguments of REGISTER_REQUEST",
							e);
					return;
				}

				L.info("Received a REGISTER request from Eduras server "
						+ serverName + " at IP " + ipOfEdurasServer + ":"
						+ portOfEdurasServer);

				// only add this if not registered yet
				if (!registeredServers.contains(clientId)) {
					registeredServers.add(clientId);

				}
				// whenever you receive a new REGISTER event, update the
				// information
				try {
					serverInfos.put(clientId, new ServerInfo(serverName,
							InetAddress.getByName(ipOfEdurasServer),
							portOfEdurasServer));
					L.fine("Renewing lease of server with id #" + clientId);
					serverLeases.put(clientId, System.currentTimeMillis());
				} catch (UnknownHostException e) {
					L.log(Level.WARNING,
							"Error generating InetAddress from ip of registering eduras server. IP: "
									+ ipOfEdurasServer, e);
				}

				break;
			}
			case DEREGISTER_REQUEST: {
				int serverId;
				try {
					serverId = (Integer) event.getArgument(0);
				} catch (TooFewArgumentsExceptions e) {
					L.log(Level.WARNING,
							"Error accessing arguments of DEREGISTER_REQUEST",
							e);
					return;
				}
				L.info("Received a DEREGISTER request from client " + serverId);

				if (registeredServers.contains(serverId)) {
					removeServer(serverId);
				}

				break;
			}

			}

		}
	}

	class ServerRegistryLeaseChecker extends Thread {

		@Override
		public void run() {
			LinkedList<Integer> serversToRemove = new LinkedList<Integer>();
			while (!interrupted()) {
				serversToRemove.clear();

				for (Integer singleServer : registeredServers) {
					if (System.currentTimeMillis()
							- serverLeases.get(singleServer) > LEASE_TIME) {
						serversToRemove.add(singleServer);
					}
				}

				for (Integer singleServer : serversToRemove) {
					L.info("Lease of server with id #" + singleServer
							+ " expired.");
					removeServer(singleServer);
				}

				try {
					sleep(10000);
				} catch (InterruptedException e) {
					L.log(Level.WARNING,
							"Cannot sleep in ServerRegistryLeaseChecker!", e);
				}
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
	}
}
