package chat.constant;

import java.io.File;

public class ChatMessageConstants {
	public static String PATH_DEFAULT = new File(System.getProperty("user.dir")).getParent();
	public static String DIRECTORY_FILE = "_files";
	public static String DIRECTORY_LOG = "_logs";
	public static String DIRECTORY_LOG_MESSAGE = "messages";
	public static String DIRECTORY_LOG_ERROR = "errors";

	public static int LENGTH_NAME_USER_SELECTED = 5;
}
