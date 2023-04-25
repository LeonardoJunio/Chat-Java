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
				Action action = message.getAction(); // Get the action sent by the server

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
				// the server doesn't send "SEND_ALL". This one is many "SEND_ONE"
			}
		} catch (IOException | ClassNotFoundException ex) {
			Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void connected(ChatMessage message) {
		if (message.getText().equals("NO")) {
			frame.getTxtName().setText("");
			JOptionPane.showMessageDialog(frame, "Conexão não realizada. \nTente novamente com um novo nome.");
			return;
		}

		frameService.message = message;
		frame.getBtnConnectar().setEnabled(false);
		frame.getTxtName().setEditable(false);

		frame.getTxtAreaReceive().setEnabled(true);
		frame.getBtnSair().setEnabled(true);
		frame.getTxtAreaSend().setEnabled(true);
		frame.getBtnEnviar().setEnabled(true);
		frame.getBtnLimpar().setEnabled(true);

		JOptionPane.showMessageDialog(frame, "Voce está conectado no chat!");
	}

	public void disconnected() {
		frame.getBtnConnectar().setEnabled(true);
		frame.getTxtName().setEditable(true);

		frame.getTxtAreaReceive().setEnabled(false);
		frame.getBtnSair().setEnabled(false);
		frame.getTxtAreaSend().setEnabled(false);
		frame.getBtnEnviar().setEnabled(false);
		frame.getBtnLimpar().setEnabled(false);

		frame.getTxtAreaReceive().setText("");
		frame.getTxtAreaSend().setText("");

		JOptionPane.showMessageDialog(frame, "Voce saiu do chat!");
	}

	private void receive(ChatMessage message) {
		frame.getTxtAreaReceive().append(message.getName() + " diz: " + message.getText() + "\n");
	}

	private void refreshOnlines(ChatMessage message) {
		Set<String> names = message.getSetOnlines(); // alterar nome variavel 'SetOnlines'?

		// remove of the list the respective user of 'window'
		names.remove(message.getName());

		String[] arrayNames = names.toArray(new String[names.size()]);

		frame.getListOnlines().setListData(arrayNames);
		frame.getListOnlines().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		frame.getListOnlines().setLayoutOrientation(JList.VERTICAL);
	}
}
