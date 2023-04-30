package chat.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import chat.bean.ChatMessage;
import chat.constant.ChatMessageConstants;

public class FileUtils {
	public static boolean checkFileMessage(ChatMessage message) {
		return checkFile(message.getFile());
	}

	public static boolean checkFile(File fileMessage) {
		return !(fileMessage == null || fileMessage.getName().isEmpty());
	}

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

	private static void saveFile(File file, String dir) {
		try {
			Thread.sleep(new Random().nextInt(1000));

			long time = System.currentTimeMillis();

			// Try-with-resources close the object when finished
			try (FileInputStream fileInputStream = new FileInputStream(file)) {
				String dirWithFileName = dir + time + "-" + file.getName();

				try (FileOutputStream fileOutputStream = new FileOutputStream(dirWithFileName)) {
					FileChannel fileIn = fileInputStream.getChannel();
					FileChannel fileOut = fileOutputStream.getChannel();

					long size = fileIn.size();

					fileIn.transferTo(0, size, fileOut);
				}
			}
		} catch (IOException | InterruptedException ex) {
			LogUtils.saveLogError(LogUtils.getErrorMessageLog(ex));

			Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void saveUsersFile(ChatMessage fileMessage) {
		if (FileUtils.checkFile(fileMessage.getFile())) {
			String dir = ChatMessageConstants.PATH_DEFAULT.concat(System.getProperty("file.separator"))
					.concat(ChatMessageConstants.DIRECTORY_FILE).concat(System.getProperty("file.separator"))
					.concat(fileMessage.getNameReserved()).concat(System.getProperty("file.separator"));

			saveFile(fileMessage.getFile(), dir);
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
