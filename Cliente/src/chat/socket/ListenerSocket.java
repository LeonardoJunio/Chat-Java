package chat.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import chat.bean.ChatMessage;
import chat.bean.ChatMessage.Action;
import chat.constant.ClienteConstans;
import chat.frame.ClienteFrame;
import chat.service.ClienteFrameService;

public class ListenerSocket implements Runnable {
	private ClienteFrame frame;
	private ClienteFrameService frameService;

	private ObjectInputStream input;

	public ListenerSocket(Socket socket, ClienteFrameService frameService) {
		try {
			this.input = new ObjectInputStream(socket.getInputStream());
			this.frameService = frameService;
			this.frame = frameService.getFrame();
		} catch (IOException ex) {
			Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void run() {
		ChatMessage message = null;

		try {
			while ((message = (ChatMessage) input.readObject()) != null) {
				// Get the action sent by the server
				Action action = message.getAction();

				if (action.equals(Action.CONNECT)) {
					connected(message);
				} else if (action.equals(Action.DISCONNECT)) {
					disconnected();
					frameService.getSocket().close();
				} else if (action.equals(Action.SEND_ONE)) {
					receive(message);
				} else if (action.equals(Action.USERS_ONLINE)) {
					refreshOnlines(message);
				}
				// The server doesn't send "SEND_ALL". This is many "SEND_ONE"
			}
		} catch (IOException | ClassNotFoundException ex) {
			Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void connected(ChatMessage message) {
		if (message.getText().equals(ClienteConstans.CONNECTION_ERROR)) {
			frame.getTxtName().setText("");

			JOptionPane.showMessageDialog(frame, "Connection error. \nTry again, may with a new name, please.");

			return;
		}

		frameService.message = message;

		frame.getBtnLogin().setEnabled(false);
		frame.getTxtName().setEditable(false);

		frame.getTxtAreaReceive().setEnabled(true);
		frame.getBtnLogout().setEnabled(true);
		frame.getTxtAreaSend().setEnabled(true);
		frame.getBtnSend().setEnabled(true);
		frame.getBtnClear().setEnabled(true);
		frame.getBtnFile().setEnabled(true);

		JOptionPane.showMessageDialog(frame, "You've joined the chat!");
	}

	public void disconnected() {
		frame.getBtnLogin().setEnabled(true);
		frame.getTxtName().setEditable(true);

		frame.getTxtAreaReceive().setEnabled(false);
		frame.getBtnLogout().setEnabled(false);
		frame.getTxtAreaSend().setEnabled(false);
		frame.getBtnSend().setEnabled(false);
		frame.getBtnClear().setEnabled(false);
		frame.getBtnFile().setEnabled(false);
		
		frame.getTxtAreaReceive().setText("");
		frame.getTxtAreaSend().setText("");
		frame.getLabelGroup().setText("Last Selected(s): ---");

		JOptionPane.showMessageDialog(frame, "You've left the chat!");
	}

	private void receive(ChatMessage message) {
		StringBuilder messageTxtAreaReceive = new StringBuilder();
		messageTxtAreaReceive.append(message.getName() + " said: " );
		
		if(message.getSelectedUsers() != null && !message.getSelectedUsers().isEmpty()) {
			messageTxtAreaReceive.append(message.getSelectedUsers() + " ");
		}

		messageTxtAreaReceive.append(message.getText() + "\n");

		frame.getTxtAreaReceive().append(messageTxtAreaReceive.toString());
	}

	private void refreshOnlines(ChatMessage message) {
		Set<String> names = message.getSetOnlines();

		// Remove of the list the respective user of 'window'
		names.remove(message.getName());

		String[] arrayNames = names.toArray(new String[names.size()]);

		frame.getListOnlines().setListData(arrayNames);
		frame.getListOnlines().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		frame.getListOnlines().setLayoutOrientation(JList.VERTICAL);
	}
}
