package chat.util;

import java.util.List;

import chat.constant.ChatMessageConstants;

public class ChatMessageUtils {

	public static String getSelectedUsers(List<Object> selectedUsers) {
		if (selectedUsers.isEmpty()) {
			return "";
		}

		StringBuilder textAllUsers = new StringBuilder();
		StringBuilder smallNameUser = new StringBuilder();

		selectedUsers.forEach(user -> {
			String userModifed = user.toString();
			
			if(user.toString().length() > ChatMessageConstants.LENGTH_NAME_USER_SELECTED) {
				userModifed = userModifed.substring(0, ChatMessageConstants.LENGTH_NAME_USER_SELECTED) + "., ";
			}
			
			smallNameUser.append(userModifed);
		});

		textAllUsers.append(smallNameUser.substring(0, smallNameUser.length() - 2));

		return textAllUsers.toString();
	}
}
