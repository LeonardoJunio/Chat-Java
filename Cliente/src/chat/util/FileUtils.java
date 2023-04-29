package chat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import chat.constant.ClienteConstans;

public class FileUtils {

	public static boolean checkFileMessage(ChatMessage message) {
		return checkFile(message.getFile());
	}

	public static boolean checkFile(File fileMessage) {
		if (fileMessage == null || fileMessage.getName().isEmpty()) {
			return false;
		}

		return true;
	}
	
	public static void checkAndCreateDirectory(String nameDirectory) {		
		Path dir = Paths.get(ClienteConstans.PATH_DEFAULT.concat(System.getProperty("file.separator") + nameDirectory));
		
		if (!Files.exists(dir, LinkOption.NOFOLLOW_LINKS) || !Files.isDirectory(dir, LinkOption.NOFOLLOW_LINKS)) {
			try {
				Files.createDirectories(dir);
			} catch (IOException ex) {
	            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
	        }
		}		
	}
	
	public static void saveFile(ChatMessage fileMessage) {
		try {
			if (FileUtils.checkFile(fileMessage.getFile())) {	
	            Thread.sleep(new Random().nextInt(1000));
	            
	            long time = System.currentTimeMillis();//tempo exato da execucao dessa linha
	            
	            FileInputStream fileInputStream = new FileInputStream(fileMessage.getFile());
	    		
	    		String dir = ClienteConstans.PATH_DEFAULT.concat(System.getProperty("file.separator"))
	    				.concat(fileMessage.getNameReserved()).concat(System.getProperty("file.separator"));
	            
	            try (FileOutputStream fileOutputStream = new FileOutputStream(dir + time + fileMessage.getFile().getName())) {
					FileChannel fileIn = fileInputStream.getChannel();
					FileChannel fileOut = fileOutputStream.getChannel();
					
					long size = fileIn.size();
					
					fileIn.transferTo(0, size, fileOut);
				}
			}
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
}
