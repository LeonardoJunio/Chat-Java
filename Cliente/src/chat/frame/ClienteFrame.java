package chat.frame;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

import chat.service.ClienteFrameService;

//Tentar mover o que nao for do frame para uma outra service, tipo ClienteFrameService
public class ClienteFrame extends JFrame {
	private static final long serialVersionUID = -8895033625174605700L;

	ClienteFrameService service;

	/** Creates new form ClienteFrame */
	public ClienteFrame() {
		if (this.service == null) {
			this.service = new ClienteFrameService(this);
		}

		initComponents();
	}

	// Variables declaration - be careful when changing it.
	// The content of this method is always modified by the Form Editor.
	private JButton btnArquivo;
	private JButton btnAudio;
	private JButton btnGrupo;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JPanel jPanel3;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane3;

	private JButton btnConnectar;
	private JButton btnEnviar;
	private JButton btnLimpar;
	private JButton btnSair;
	private JTextField txtName;
	private JTextArea txtAreaReceive;
	private JTextArea txtAreaSend;
	private JLabel labelGrupo;
	private JList<Object> listOnlines;

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
		listOnlines = new JList<>();
		btnGrupo = new JButton();
		jPanel3 = new JPanel();
		jScrollPane1 = new JScrollPane();
		txtAreaReceive = new JTextArea();
		jScrollPane2 = new JScrollPane();
		txtAreaSend = new JTextArea();
		btnEnviar = new JButton();
		btnLimpar = new JButton();
		btnArquivo = new JButton();
		btnAudio = new JButton();
		labelGrupo = new JLabel();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		jPanel1.setBorder(BorderFactory.createTitledBorder("Conectar"));

		btnConnectar.setText("Connectar");
		btnConnectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				service.btnConnectarActionPerformed(evt);
			}
		});

		btnSair.setText("Sair");
		btnSair.setEnabled(false);
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				service.btnSairActionPerformed(evt);
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

		btnGrupo.setText("Grupo");
		btnGrupo.setEnabled(false);
		btnGrupo.setVisible(false);

		GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE).addComponent(
										btnGrupo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addComponent(jScrollPane3)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(btnGrupo)));

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
				service.btnEnviarActionPerformed(evt);
			}
		});

		btnLimpar.setText("Limpar");
		btnLimpar.setEnabled(false);
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				service.btnLimparActionPerformed(evt);
			}
		});

		btnArquivo.setText("Arquivo");
		btnArquivo.setEnabled(false);

		btnAudio.setText("Audio");
		btnAudio.setEnabled(false);

		labelGrupo.setFont(new Font("Ubuntu", 0, 14)); // NOI18N
		labelGrupo.setText("Último Grupo: ---");

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
								.addGroup(jPanel3Layout.createSequentialGroup().addComponent(labelGrupo).addGap(0, 0,
										Short.MAX_VALUE)))
						.addContainerGap()));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
						.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(labelGrupo)
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

	public JButton getBtnConnectar() {
		return btnConnectar;
	}

	public JButton getBtnEnviar() {
		return btnEnviar;
	}

	public JButton getBtnLimpar() {
		return btnLimpar;
	}

	public JButton getBtnSair() {
		return btnSair;
	}

	public JTextField getTxtName() {
		return txtName;
	}

	public JTextArea getTxtAreaReceive() {
		return txtAreaReceive;
	}

	public JTextArea getTxtAreaSend() {
		return txtAreaSend;
	}

	public JLabel getLabelGrupo() {
		return labelGrupo;
	}

	public JList<Object> getListOnlines() {
		return listOnlines;
	}
}
