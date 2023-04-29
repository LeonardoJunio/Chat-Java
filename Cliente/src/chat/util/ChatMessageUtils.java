package chat.util;

import java.util.List;

public class ChatMessageUtils {

	public static String getSelectedUsers(List<Object> selectedUsers) {
		if (selectedUsers.isEmpty()) {
			return "";
		}

		// Pegar 5 prim caracteres para economizar espaco
		String textAllUsers = selectedUsers.toString().replace("[", "(").replace("]", ")");

		return textAllUsers;
	}
}
