package chat.service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import chat.bean.ChatMessage;
import chat.constant.ClienteConstans;

public class ClienteService {
	private Socket socket;
	private ObjectOutputStream output;

	public Socket connect() {
		try {
			// IP and port of connection
			this.socket = new Socket(ClienteConstans.CLIENT_IP, ClienteConstans.CLIENT_PORT);
			this.output = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
			Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
		}

		return socket;
	}

	// Send the Message
	public void send(ChatMessage message) {
		try {
			output.writeObject(message);
		} catch (IOException ex) {
			Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
