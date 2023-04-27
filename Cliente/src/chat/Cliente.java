package chat;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import chat.frame.ClienteFrame;

public class Cliente {
	public static void main(String args[]) {
		/*
		 * Set the Nimbus look and feel
		 * 
		 * Nimbus is a polished cross-platform look and feel introduced in the Java SE 6
		 * Update 10 (6u10) release.
		 * 
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException ex) {
			Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
		}

		/* Create and display the form */
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Work as a Model and a Controller at same time
				new ClienteFrame().setVisible(true);
				
//				ClienteFrame clienteFrame = new ClienteFrame();
//				clienteFrame.setVisible(true);
			}
		});
	}

}
