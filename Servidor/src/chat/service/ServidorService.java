package chat.service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import chat.constant.ServidorConstants;
import chat.socket.ListenerSocket;
import chat.util.LogUtils;

public class ServidorService {
	private ServerSocket serverSocket;
	private Socket socket;

	// Stores all connected users
	private Map<String, ObjectOutputStream> mapOnlines = new HashMap<String, ObjectOutputStream>();

	public ServidorService() {
		try {
			// Starts the server on port
			serverSocket = new ServerSocket(ServidorConstants.SERVER_PORT);
			System.out.println("Servidor on!");

			// Executed for each user who logs in
			while (true) {
				// Listens for a connection to be made to this socket and accepts it
				// This socket was created by the user (ClienteService) when logged in
				socket = serverSocket.accept();
				new Thread(new ListenerSocket(socket, mapOnlines)).start();
			}
		} catch (IOException ex) {
			LogUtils.saveLogError(LogUtils.getErrorMessageLog(ex));

			Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
