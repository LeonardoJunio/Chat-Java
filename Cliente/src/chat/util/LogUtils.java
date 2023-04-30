package chat.util;

import chat.constant.ChatMessageConstants;

public class LogUtils {
	public static void saveMessageLog(String textMessage) {
		String dir = ChatMessageConstants.PATH_DEFAULT.concat(System.getProperty("file.separator"))
				.concat(ChatMessageConstants.DIRECTORY_LOG).concat(System.getProperty("file.separator"))
				.concat(DateUtils.dateNowFormated()).concat("-log-messages").concat(".txt");

		String textMessageLog = DateUtils.dateTimeNowFormated().concat("# " + textMessage);

		saveLogFile(dir, textMessageLog);
	}

	private static void saveLogFile(String dir, String textMessage) {
		FileUtils.checkAndCreateDirectory(ChatMessageConstants.DIRECTORY_LOG);

		FileUtils.saveLogTxt(dir, textMessage);
	}
}
