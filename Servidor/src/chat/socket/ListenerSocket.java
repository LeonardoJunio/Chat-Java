package chat.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import chat.bean.ChatMessage;
import chat.bean.ChatMessage.Action;
import chat.constant.ServidorConstants;
import chat.service.ServidorService;
import chat.util.LogUtils;

public class ListenerSocket implements Runnable {
	// Message output
	private ObjectOutputStream output;
	// Receives the users' message
	private ObjectInputStream input;
	private Map<String, ObjectOutputStream> mapOnlines;

	// Keeps listening to the server
	public ListenerSocket(Socket socket, Map<String, ObjectOutputStream> mapOnlines) {
		try {
			// Each user has his own output and input
			this.output = new ObjectOutputStream(socket.getOutputStream());
			this.input = new ObjectInputStream(socket.getInputStream());
			this.mapOnlines = mapOnlines;
		} catch (IOException ex) {
			LogUtils.saveLogError(LogUtils.getErrorMessageLog(ex));

			Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void run() {
		ChatMessage message = null;
		try {
			// "readObject()" receive the messages send by project Cliente
			while ((message = (ChatMessage) input.readObject()) != null) {
				Action action = message.getAction();

				if (action.equals(Action.CONNECT)) {
					boolean isConnect = this.connect(message, output);

					if (isConnect) {
						mapOnlines.put(message.getName(), output);

						this.sendOnlines();
					}
				} else if (action.equals(Action.DISCONNECT)) {
					this.disconnect(message, output);
					this.sendOnlines();

					return;
				} else if (action.equals(Action.SEND_ONE)) {
					this.sendOne(message);
				} else if (action.equals(Action.SEND_ALL)) {
					this.sendAll(message);
				}
			}
		} catch (IOException ex) {
			// When user close the window without logout, show this error too
			LogUtils.saveLogError(LogUtils.getErrorMessageLog(ex));

			ChatMessage cm = new ChatMessage();

			if (message != null) {
				cm.setName(message.getName());

				disconnect(cm, output);
				sendOnlines();

				System.out.println(message.getName() + ": closed the chat.");
			}
		} catch (ClassNotFoundException ex) {
			LogUtils.saveLogError(LogUtils.getErrorMessageLog(ex));

			Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// When a new user tries to log into the system
	private boolean connect(ChatMessage message, ObjectOutputStream output) {
		if (mapOnlines.size() > 0 && mapOnlines.containsKey(message.getName())) {
			message.setText(ServidorConstants.CONNECTION_ERROR);

			send(message, output);

			return false;
		}

		message.setText(ServidorConstants.CONNECTION_SUCCESS);
		send(message, output);

		if (mapOnlines.size() > 0) {
			message.setText("-- I'm entering the chat! --");
			message.setAction(Action.SEND_ONE);

			sendAll(message);
		}

		return true;
	}

	private void disconnect(ChatMessage message, ObjectOutputStream output) {
		mapOnlines.remove(message.getName());

		message.setText("-- I'm leaving the chat! --");
		message.setAction(Action.SEND_ONE);

		sendAll(message);

		System.out.println("User: " + message.getName() + " left the chat.");
	}

	private void send(ChatMessage message, ObjectOutputStream output) {
		try {
			output.writeObject(message);
		} catch (IOException ex) {
			LogUtils.saveLogError(LogUtils.getErrorMessageLog(ex));

			Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void sendOne(ChatMessage message) {
		for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
			if (kv.getKey().equals(message.getNameReserved())) {
				try {
					kv.getValue().writeObject(message);
				} catch (IOException ex) {
					LogUtils.saveLogError(LogUtils.getErrorMessageLog(ex));

					Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	private void sendAll(ChatMessage message) {
		// Get each message output (String-key; Object-value) of each user online
		for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
			// Does not send the message to the sender
			if (!kv.getKey().equals(message.getName())) {
				message.setAction(Action.SEND_ONE);

				try {
					// Value has the ObjectOutputStream
					kv.getValue().writeObject(message);
				} catch (IOException ex) {
					LogUtils.saveLogError(LogUtils.getErrorMessageLog(ex));

					Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	private void sendOnlines() {
		Set<String> setNames = new HashSet<String>();
		for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
			setNames.add(kv.getKey());
		}

		ChatMessage message = new ChatMessage();
		message.setAction(Action.USERS_ONLINE);
		// Set to show who's online
		message.setSetOnlines(setNames);

		for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
			message.setName(kv.getKey());

			try {
				kv.getValue().writeObject(message);
			} catch (IOException ex) {
				LogUtils.saveLogError(LogUtils.getErrorMessageLog(ex));

				Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
