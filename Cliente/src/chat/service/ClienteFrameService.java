package chat.service;

import java.awt.event.ActionEvent;
import java.net.Socket;
import java.util.List;

import chat.bean.ChatMessage;
import chat.bean.ChatMessage.Action;
import chat.frame.ClienteFrame;
import chat.socket.ListenerSocket;

public class ClienteFrameService {
	private ClienteFrame frame;
	private ListenerSocket listenerSocket;

	public ChatMessage message;
	private Socket socket;
	private ClienteService clientService;

	public ClienteFrameService(ClienteFrame clienteFrame) {
		if (this.frame == null) {
			this.frame = clienteFrame;
		}
	}

	public void btnLoginActionPerformed(ActionEvent evt) {
		// Name of user typed on interface, to connect
		String nameUserConnect = frame.getTxtName().getText();

		if (!nameUserConnect.isEmpty()) {
			this.message = new ChatMessage(nameUserConnect, Action.CONNECT);

			this.clientService = new ClienteService();
			this.socket = this.clientService.connect();

			listenerSocket = new ListenerSocket(this.socket, this);
			new Thread(listenerSocket).start();

			this.clientService.send(message);
		}
	}

	public void btnLogoutActionPerformed(ActionEvent evt) {
		ChatMessage message = new ChatMessage(this.message.getName(), Action.DISCONNECT);
		this.clientService.send(message);

		frame.getListOnlines().setListData(new String[0]);
		
		listenerSocket.disconnected();
	}

	public void btnClearActionPerformed(ActionEvent evt) {
		frame.getTxtAreaSend().setText("");
	}

	public void btnSendActionPerformed(ActionEvent evt) {
		String text = frame.getTxtAreaSend().getText();
		String name = this.message.getName();
		List<Object> listSelectedUser = frame.getListOnlines().getSelectedValuesList();

		if (text.isEmpty()) {
			return;
		}

		// New instance of ChatMessage because is a new message
		if (listSelectedUser.isEmpty()) {
			this.message = new ChatMessage(name, text);
			this.message.setAction(Action.SEND_ALL);

			this.clientService.send(this.message);
			
			frame.getTxtAreaReceive().append("You said: " + text + "\n");
		} else {
			if (listSelectedUser.size() > 1) {
				frame.getLabelGroup().setText("Last Group: " + listSelectedUser.toString());
			}

			// Changed text to display all selected users
			String textAllUsers = frame.getListOnlines().getSelectedValuesList().toString()
					.replace("[", "(").replace("]", ")").concat(" ").concat(text);

			listSelectedUser.forEach(selectedUser -> {
				this.message = new ChatMessage(name, textAllUsers);
				this.message.setNameReserved(selectedUser.toString());
				this.message.setAction(Action.SEND_ONE);

				this.clientService.send(this.message);
			});

			frame.getListOnlines().clearSelection();
			frame.getTxtAreaReceive().append("You said: " + textAllUsers + "\n");
		}

		this.btnClearActionPerformed(evt);
	}

	public ClienteFrame getFrame() {
		return frame;
	}

	public Socket getSocket() {
		return socket;
	}
}
