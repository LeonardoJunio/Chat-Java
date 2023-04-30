package chat.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import chat.constant.ChatMessageConstants;

public class FileUtils {
	public static void checkAndCreateDirectory(String directory) {
		checkAndCreateDirectory(directory, null);
	}

	public static void checkAndCreateDirectory(String directory, String nameUserDirectory) {
		String dir = ChatMessageConstants.PATH_DEFAULT.concat(System.getProperty("file.separator")).concat(directory);

		if (nameUserDirectory != null) {
			dir = dir.concat(System.getProperty("file.separator")).concat(nameUserDirectory);
		}

		Path pathDir = Paths.get(dir);

		try {
			if (!Files.exists(pathDir, LinkOption.NOFOLLOW_LINKS)
					|| !Files.isDirectory(pathDir, LinkOption.NOFOLLOW_LINKS)) {
				Files.createDirectories(pathDir);
			}
		} catch (IOException ex) {
			LogUtils.saveLogError(LogUtils.getErrorMessageLog(ex));

			Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void saveLogTxt(String dir, String textMessageLog) {
		try {
			FileWriter writer = new FileWriter(dir, true);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);

			bufferedWriter.write(textMessageLog);

			bufferedWriter.close();
		} catch (IOException ex) {
			LogUtils.saveLogError(LogUtils.getErrorMessageLog(ex));

			Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
