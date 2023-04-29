package chat.service;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import chat.bean.ChatMessage;
import chat.bean.ChatMessage.Action;
import chat.frame.ClienteFrame;
import chat.socket.ListenerSocket;
import chat.util.ChatMessageUtils;
import chat.util.FileUtils;

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

	private String messageAreaReceive(String text) {
		String textReturn = "You said: ";

		if (this.message.getSelectedUsers() != null && !this.message.getSelectedUsers().isEmpty()) {
			textReturn += this.message.getSelectedUsers() + " ";
		}

		if (FileUtils.checkFileMessage(this.message)) {
			textReturn += "You sent the following file: '" + this.message.getFile().getName();

			if (!text.isEmpty()) {
				textReturn += "' with the following message: '" + text + "'";
			}
		} else {
			textReturn += text;
		}

		return textReturn + "\n";
	}

	private String messageTextSend(String text) {
		StringBuilder textSend = new StringBuilder();

		if (FileUtils.checkFileMessage(this.message)) {
			textSend.append("You received the following file: ").append("'" + this.message.getFile().getName() + "'");

			if (!text.isEmpty()) {
				textSend.append(" with the following message: ").append("'" + text + "'");
			}

			textSend.append(". Ele foi salvo na pasta padrão.");
		} else {
			textSend.append(text);
		}

		return textSend.toString();
	}

	private void saveFileMessage(ChatMessage fileMessage) {
		if (FileUtils.checkFileMessage(this.message)) {
			FileUtils.checkAndCreateDirectory(message.getNameReserved());

			FileUtils.saveFile(fileMessage);
		}
	}

	public void btnSendActionPerformed(ActionEvent evt) {
		String name = this.message.getName();
		File fileMessage = this.message.getFile();
		String text = frame.getTxtAreaSend().getText();
		List<Object> listSelectedUser = frame.getListOnlines().getSelectedValuesList();
		boolean existsFileMessage = FileUtils.checkFileMessage(this.message);

		if (!existsFileMessage && text.isEmpty()) {
			return;
		}

		String textSend = this.messageTextSend(text);

		// New instance of ChatMessage because is a new message
		if (listSelectedUser.isEmpty() && !existsFileMessage) {
			this.message = new ChatMessage(name, textSend);
			this.message.setAction(Action.SEND_ALL);

			this.clientService.send(this.message);
		} else {
			// Changed text to display all selected users
			String textAllUsers = ChatMessageUtils.getSelectedUsers(frame.getListOnlines().getSelectedValuesList(),
					listSelectedUser);

			if (listSelectedUser.isEmpty()) {
				int maxIndex = frame.getListOnlines().getLastVisibleIndex() + 1;
				List<Integer> indicesSelected = new ArrayList<>();
				
				for (int i = 0; i < maxIndex; i++) {	
					indicesSelected.add(i);
				}
				
				int[] array = new int[indicesSelected.size()];
				for(int i = 0; i < indicesSelected.size(); i++) {
					array[i] = indicesSelected.get(i);
				}
				
				frame.getListOnlines().setSelectedIndices(array);
				
				listSelectedUser = frame.getListOnlines().getSelectedValuesList();
			} else if (listSelectedUser.size() > 1) {
				frame.getLabelGroup().setText("Last Group: " + listSelectedUser.toString());
			}

			listSelectedUser.forEach(selectedUser -> {
				this.message = new ChatMessage(name, textSend);
				this.message.setSelectedUsers(textAllUsers);
				this.message.setFile(fileMessage);
				this.message.setNameReserved(selectedUser.toString());
				this.message.setAction(Action.SEND_ONE);

				if(existsFileMessage) {
					this.saveFileMessage(this.message);
				}

				this.clientService.send(this.message);
			});

			frame.getListOnlines().clearSelection();
		}

		frame.getTxtAreaReceive().append(this.messageAreaReceive(text));

		this.message.setFile(null);
		this.btnClearActionPerformed(evt);
	}

	public void btnSendFileActionPerformed(ActionEvent evt) {
		JFileChooser fileChooser = new JFileChooser();
		int optFileChooser;

		// Open a window to choose some file and return if the user selected one
		optFileChooser = fileChooser.showOpenDialog(null);

		if (optFileChooser == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			this.message.setFile(file);

			this.btnSendActionPerformed(evt);
		}
	}

	public ClienteFrame getFrame() {
		return frame;
	}

	public Socket getSocket() {
		return socket;
	}
}
