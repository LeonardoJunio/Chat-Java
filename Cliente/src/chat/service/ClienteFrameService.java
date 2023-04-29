package chat.service;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.Socket;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import chat.bean.ChatMessage;
import chat.bean.ChatMessage.Action;
import chat.frame.ClienteFrame;
import chat.socket.ListenerSocket;
import chat.util.FileUtils;

public class ClienteFrameService {
	private ClienteFrame frame;
	private ListenerSocket listenerSocket;

	public ChatMessage message;
	private Socket socket;
	private ClienteService clientService;
	private ChatMessageService chatMessageService;

	public ClienteFrameService(ClienteFrame clienteFrame) {
		if (this.frame == null) {
			this.frame = clienteFrame;
		}
	}

	public void btnLoginActionPerformed(ActionEvent evt) {
		// Name of user typed on interface, to connect
		String nameUserConnect = frame.getTxtName().getText();

		if (nameUserConnect.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Your nickname can't be empty.");
			return;
		}

		this.message = new ChatMessage(nameUserConnect, Action.CONNECT);

		this.clientService = new ClienteService();
		this.socket = this.clientService.connect();

		listenerSocket = new ListenerSocket(this.socket, this);
		new Thread(listenerSocket).start();

		this.clientService.send(message);
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
		String text = frame.getTxtAreaSend().getText().trim();
		boolean existsFileMessage = FileUtils.checkFileMessage(this.message);

		if (!existsFileMessage && text.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "No message and file inserted.");
			return;
		}

		this.chatMessageService = new ChatMessageService(frame, message, clientService);
		this.chatMessageService.sendMessagem(text, existsFileMessage);

		frame.getTxtAreaReceive().append(this.chatMessageService.messageAreaReceive(text));
		frame.getTxtAreaReceive().setCaretPosition(frame.getTxtAreaReceive().getDocument().getLength());

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
