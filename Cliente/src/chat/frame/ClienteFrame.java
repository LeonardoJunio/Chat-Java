package chat.frame;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import chat.bean.ChatMessage;
import chat.bean.ChatMessage.Action;
import chat.service.ClienteService;

//Tentar mover o que nao for do frame para uma outra service, tipo ClienteFrameService
public class ClienteFrame extends JFrame {
	private static final long serialVersionUID = -8895033625174605700L;

	private Socket socket;
	private ChatMessage message;
	private ClienteService service;

	/**
	 * Creates new form ClienteFrame
	 */
	public ClienteFrame() {
		initComponents();
	}

	private class ListenerSocket implements Runnable {

		private ObjectInputStream input;

		public ListenerSocket(Socket socket) {
			try {
				this.input = new ObjectInputStream(socket.getInputStream());
			} catch (IOException ex) {
				Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
			}

		}

		@Override
		public void run() {
			ChatMessage message = null;

			try {
				while ((message = (ChatMessage) input.readObject()) != null) {
					Action action = message.getAction(); // get the action sended by the server

					if (action.equals(Action.CONNECT)) {
						connected(message);
					} else if (action.equals(Action.DISCONNECT)) {
						disconnected();
						socket.close();
					} else if (action.equals(Action.SEND_ONE)) {
						receive(message);
					} else if (action.equals(Action.USERS_ONLINE)) {
						refreshOnlines(message);
					}
				} // servidor nao envia mensagem "Send_all"

			} catch (IOException | ClassNotFoundException ex) {
				Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private void connected(ChatMessage message) {
		if (message.getText().equals("NO")) {
			this.txtName.setText("");
			JOptionPane.showMessageDialog(this, "Conexão não realizada. \nTente novamente com um novo nome.");
			return;
		}

		this.message = message;
		this.btnConnectar.setEnabled(false);
		this.txtName.setEditable(false);

		this.txtAreaReceive.setEnabled(true);
		this.btnSair.setEnabled(true);
		this.txtAreaSend.setEnabled(true);
		this.btnEnviar.setEnabled(true);
		this.btnLimpar.setEnabled(true);

		JOptionPane.showMessageDialog(this, "Voce está conectado no chat!");
	}

	private void disconnected() {
		this.btnConnectar.setEnabled(true);
		this.txtName.setEditable(true);

		this.txtAreaReceive.setEnabled(false);
		this.btnSair.setEnabled(false);
		this.txtAreaSend.setEnabled(false);
		this.btnEnviar.setEnabled(false);
		this.btnLimpar.setEnabled(false);

		this.txtAreaReceive.setText("");
		this.txtAreaSend.setText("");

		JOptionPane.showMessageDialog(this, "Voce saiu do chat!");
	}

	private void receive(ChatMessage message) {
		this.txtAreaReceive.append(message.getName() + " diz: " + message.getText() + "\n");
	}

	private void refreshOnlines(ChatMessage message) {
		Set<String> names = message.getSetOnlines(); // alterar nome variavel 'SetOnlines'?

		// remove of the list the respective user of 'window'
		names.remove(message.getName());

		String[] arrayNames = names.toArray(new String[names.size()]);

		this.listOnlines.setListData(arrayNames);
		this.listOnlines.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.listOnlines.setLayoutOrientation(JList.VERTICAL);
	}

	private void btnConnectarActionPerformed(ActionEvent evt) {
		// name of user typed on interface, to connect
		String nameUserConnect = this.txtName.getText();

		if (!nameUserConnect.isEmpty()) {
			// new instance of chat message when create a new connection (?)
			this.message = new ChatMessage(nameUserConnect, Action.CONNECT);

			this.service = new ClienteService();
			this.socket = this.service.connect();

			new Thread(new ListenerSocket(this.socket)).start();

			this.service.send(message);
		}
	}

	private void btnSairActionPerformed(ActionEvent evt) {
		// why new instance?
		ChatMessage message = new ChatMessage(this.message.getName(), Action.DISCONNECT);
		this.service.send(message);
		disconnected();
	}

	private void btnLimparActionPerformed(ActionEvent evt) {
		this.txtAreaSend.setText("");
	}

	private void btnEnviarActionPerformed(ActionEvent evt) {
		String text = this.txtAreaSend.getText();
		String name = this.message.getName();
		List<Object> listSelectedUser = this.listOnlines.getSelectedValuesList();

		if (text.isEmpty()) {
			return;
		}

		if (listSelectedUser.isEmpty()) {
			this.message = new ChatMessage(name, text);// why new instance? and dont send correctly if isolate
			this.message.setAction(Action.SEND_ALL);

			this.service.send(this.message);
		} else {
			if (listSelectedUser.size() > 1) {
				this.LabelGrupo.setText("Último Grupo: " + listSelectedUser.toString());
			}

			listSelectedUser.forEach(selectedUser -> {
				this.message = new ChatMessage(name, text);// why new instance? and dont send correctly if isolate
				this.message.setNameReserved(selectedUser.toString());
				this.message.setAction(Action.SEND_ONE);

				this.service.send(this.message);
			});

			this.listOnlines.clearSelection();
		}

		this.txtAreaReceive.append("Voce disse: " + text + "\n");
		this.btnLimparActionPerformed(evt);
	}

	// Variables declaration - be careful when changing it. 
	// The content of this method is always modified by the Form Editor.
	private JLabel LabelGrupo;
	private JButton btnArquivo;
	private JButton btnAudio;
	private JButton btnConnectar;
	private JButton btnEnviar;
	private JButton btnLimpar;
	private JButton btnSair;
	private JButton jButton1;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JPanel jPanel3;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane3;
	private JList listOnlines;
	private JTextArea txtAreaReceive;
	private JTextArea txtAreaSend;
	private JTextField txtName;

	/**
	 * This method is called from within the constructor to initialize the form. The
	 * content of this method is always modified by the Form Editor.
	 */
	private void initComponents() {

		jPanel1 = new JPanel();
		txtName = new JTextField();
		btnConnectar = new JButton();
		btnSair = new JButton();
		jPanel2 = new JPanel();
		jScrollPane3 = new JScrollPane();
		listOnlines = new JList();
		jButton1 = new JButton();
		jPanel3 = new JPanel();
		jScrollPane1 = new JScrollPane();
		txtAreaReceive = new JTextArea();
		jScrollPane2 = new JScrollPane();
		txtAreaSend = new JTextArea();
		btnEnviar = new JButton();
		btnLimpar = new JButton();
		btnArquivo = new JButton();
		btnAudio = new JButton();
		LabelGrupo = new JLabel();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		jPanel1.setBorder(BorderFactory.createTitledBorder("Conectar"));

		btnConnectar.setText("Connectar");
		btnConnectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnConnectarActionPerformed(evt);
			}
		});

		btnSair.setText("Sair");
		btnSair.setEnabled(false);
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnSairActionPerformed(evt);
			}
		});

		GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(txtName)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btnConnectar)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btnSair)
						.addGap(6, 6, 6)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup()
						.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnConnectar).addComponent(btnSair))
						.addGap(0, 8, Short.MAX_VALUE)));

		jPanel2.setBorder(BorderFactory.createTitledBorder("Onlines"));

		jScrollPane3.setViewportView(listOnlines);

		jButton1.setText("Grupo");
		jButton1.setEnabled(false);

		GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE).addComponent(
										jButton1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addComponent(jScrollPane3)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1)));

		jPanel3.setBorder(BorderFactory.createEtchedBorder());

		txtAreaReceive.setEditable(false);
		txtAreaReceive.setColumns(20);
		txtAreaReceive.setRows(5);
		txtAreaReceive.setEnabled(false);
		jScrollPane1.setViewportView(txtAreaReceive);

		txtAreaSend.setColumns(20);
		txtAreaSend.setRows(5);
		txtAreaSend.setEnabled(false);
		jScrollPane2.setViewportView(txtAreaSend);

		btnEnviar.setText("Enviar");
		btnEnviar.setEnabled(false);
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnEnviarActionPerformed(evt);
			}
		});

		btnLimpar.setText("Limpar");
		btnLimpar.setEnabled(false);
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnLimparActionPerformed(evt);
			}
		});

		btnArquivo.setText("Arquivo");
		btnArquivo.setEnabled(false);

		btnAudio.setText("Audio");
		btnAudio.setEnabled(false);

		LabelGrupo.setFont(new Font("Ubuntu", 0, 14)); // NOI18N
		LabelGrupo.setText("Último Grupo: ---");

		GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(jScrollPane1).addComponent(jScrollPane2)
								.addGroup(GroupLayout.Alignment.TRAILING,
										jPanel3Layout.createSequentialGroup().addComponent(btnArquivo)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(btnAudio).addGap(30, 30, 30).addComponent(btnLimpar)
												.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(btnEnviar))
								.addGroup(jPanel3Layout.createSequentialGroup().addComponent(LabelGrupo).addGap(0, 0,
										Short.MAX_VALUE)))
						.addContainerGap()));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
						.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(LabelGrupo)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(btnEnviar).addComponent(btnLimpar).addComponent(btnArquivo)
								.addComponent(btnAudio))
						.addGap(6, 6, 6)));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
						.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
								.addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel2,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup()
				.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addContainerGap()));

		pack();
	}

}
