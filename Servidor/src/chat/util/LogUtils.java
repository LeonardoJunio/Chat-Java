package chat.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import chat.constant.ChatMessageConstants;

public class LogUtils {
	public static void saveLogError(String textMessage) {
		String dirFolder = ChatMessageConstants.DIRECTORY_LOG.concat(System.getProperty("file.separator"))
				.concat(ChatMessageConstants.DIRECTORY_LOG_ERROR);

		String dirFile = dirFolder.concat(System.getProperty("file.separator")).concat(DateUtils.dateNowFormated())
				.concat("-log-error").concat(".txt");

		String textMessageLog = DateUtils.dateTimeNowFormated().concat("# " + textMessage);

		saveLogFile(dirFile, dirFolder, textMessageLog);
	}

	public static String getErrorMessageLog(Exception ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		ex.printStackTrace(pw);

		return sw.toString();
	}

	private static void saveLogFile(String dirFile, String dirFolder, String textMessage) {
		String dirPath = ChatMessageConstants.PATH_DEFAULT.concat(System.getProperty("file.separator")).concat(dirFile);

		FileUtils.checkAndCreateDirectory(dirFolder);

		FileUtils.saveLogTxt(dirPath, textMessage);
	}
}
