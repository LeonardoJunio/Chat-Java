package chat.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import chat.bean.ChatMessage;
import chat.bean.ChatMessage.Action;
import chat.frame.ClienteFrame;
import chat.util.ChatMessageUtils;
import chat.util.FileUtils;

public class ChatMessageService {
	public ChatMessage message;
	private ClienteFrame frame;
	private ClienteService clientService;

	public ChatMessageService(ClienteFrame frame, ChatMessage message, ClienteService clientService) {
		this.frame = frame;
		this.message = message;
		this.clientService = clientService;
	}

	public String messageAreaReceive(String text) {
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

		return textReturn.trim() + "\n";
	}

	public void sendMessagem(String text, boolean existsFileMessage) {
		String name = this.message.getName().trim();
		File fileMessage = this.message.getFile();
		List<Object> listSelectedUser = frame.getListOnlines().getSelectedValuesList();
		String textSend = this.messageTextSend(text);

		// New instance of ChatMessage because is a new message
		if (listSelectedUser.isEmpty() && !existsFileMessage) {
			this.message = new ChatMessage(name, textSend);
			this.message.setAction(Action.SEND_ALL);

			this.clientService.send(this.message);
		} else {
			if (listSelectedUser.isEmpty()) {
				listSelectedUser = this.getAllUsersOnline();
			} else {
				String textGroupUsers = ChatMessageUtils
						.getSelectedUsers(frame.getListOnlines().getSelectedValuesList());
				frame.getLabelGroup().setText("Last Selected(s): " + textGroupUsers.trim());
			}

			String textSelectedUsers = listSelectedUser.toString();

			listSelectedUser.forEach(selectedUser -> {
				this.message = new ChatMessage(name, textSend);
				this.message.setSelectedUsers(textSelectedUsers);
				this.message.setNameReserved(selectedUser.toString().trim());
				this.message.setAction(Action.SEND_ONE);

				if (existsFileMessage) {
					this.message.setFile(fileMessage);
					this.saveFileMessage(this.message);
				}

				this.clientService.send(this.message);
			});

			frame.getListOnlines().clearSelection();
		}
	}

	private List<Object> getAllUsersOnline() {
		int maxIndex = frame.getListOnlines().getLastVisibleIndex() + 1;
		List<Integer> indicesSelected = new ArrayList<>();

		for (int i = 0; i < maxIndex; i++) {
			indicesSelected.add(i);
		}

		int[] array = new int[indicesSelected.size()];
		for (int i = 0; i < indicesSelected.size(); i++) {
			array[i] = indicesSelected.get(i);
		}

		frame.getListOnlines().setSelectedIndices(array);

		return frame.getListOnlines().getSelectedValuesList();
	}

	private String messageTextSend(String text) {
		StringBuilder textSend = new StringBuilder();

		if (FileUtils.checkFileMessage(this.message)) {
			textSend.append("You received the following file: ").append("'" + this.message.getFile().getName() + "'");

			if (!text.isEmpty()) {
				textSend.append(" with the following message: ").append("'" + text + "'");
			}

			textSend.append(". It was saved in the default folder.");
		} else {
			textSend.append(text);
		}

		return textSend.toString().trim();
	}

	private void saveFileMessage(ChatMessage fileMessage) {
		if (FileUtils.checkFileMessage(this.message)) {
			FileUtils.checkAndCreateDirectory(message.getNameReserved());

			FileUtils.saveFile(fileMessage);
		}
	}
}
