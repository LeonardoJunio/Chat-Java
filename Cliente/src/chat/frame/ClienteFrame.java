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
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class ClienteFrame extends JFrame {
	private static final long serialVersionUID = -8895033625174605700L;

	ClienteFrameService service;

	/** Creates new form ClienteFrame with a service and the frame components */
	public ClienteFrame() {
		if (this.service == null) {
			this.service = new ClienteFrameService(this);
		}

		initComponents();
	}

	// Variables declaration - be careful when changing it.
	// The content of this method is always modified by the Form Editor.
	private JButton btnFile;
	private JButton btnGroup;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JPanel jPanel3;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane3;
	private GroupLayout jPanel1Layout;
	private GroupLayout jPanel2Layout;
	private GroupLayout jPanel3Layout;
	private JLabel labelNickname;

	private JButton btnLogin;
	private JButton btnSend;
	private JButton btnClear;
	private JButton btnLogout;
	private JTextField txtName;
	private JTextArea txtAreaReceive;
	private JTextArea txtAreaSend;
	private JLabel labelGroup;
	private JList<Object> listOnlines;

	/**
	 * This method is called from within the constructor to initialize the form. The
	 * content of this method is always modified by the Form Editor.
	 */
	private void initComponents() {
		jPanel1 = new JPanel();
		txtName = new JTextField();
		btnLogin = new JButton();
		btnLogout = new JButton();
		jPanel2 = new JPanel();
		jScrollPane3 = new JScrollPane();
		listOnlines = new JList<>();
		btnGroup = new JButton();
		jPanel3 = new JPanel();
		jScrollPane1 = new JScrollPane();
		txtAreaReceive = new JTextArea();
		jScrollPane2 = new JScrollPane();
		txtAreaSend = new JTextArea();
		btnSend = new JButton();
		btnClear = new JButton();
		btnFile = new JButton();
		labelGroup = new JLabel();
		labelNickname = new JLabel();

		jPanel1Layout = new GroupLayout(jPanel1);
		jPanel2Layout = new GroupLayout(jPanel2);
		jPanel3Layout = new GroupLayout(jPanel3);

		txtAreaReceive.setLineWrap(true);
		txtAreaSend.setLineWrap(true);
		txtAreaSend.setTabSize(4);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		jPanel1.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Connect", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(51, 51, 51)));

		btnLogin.setText("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				service.btnLoginActionPerformed(evt);
			}
		});

		btnLogout.setText("Logout");
		btnLogout.setEnabled(false);
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				service.btnLogoutActionPerformed(evt);
			}
		});

		labelNickname.setText("Nickname:");
		labelNickname.setFont(new Font("Ubuntu", Font.PLAIN, 14));

		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addGap(37)
						.addComponent(labelNickname, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtName, GroupLayout.PREFERRED_SIZE, 219, GroupLayout.PREFERRED_SIZE).addGap(18)
						.addComponent(btnLogin).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnLogout)));

		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup()
						.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnLogout)
								.addComponent(labelNickname, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnLogin))
						.addContainerGap(20, Short.MAX_VALUE)));

		jPanel1.setLayout(jPanel1Layout);

		jPanel2.setBorder(BorderFactory.createTitledBorder("Onlines"));

		jScrollPane3.setViewportView(listOnlines);

		btnGroup.setText("Group");
		btnGroup.setEnabled(false);
		btnGroup.setVisible(false);

		jPanel2.setLayout(jPanel2Layout);
		
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE).addComponent(
										btnGroup, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));

		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addComponent(jScrollPane3)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(btnGroup)));

		jPanel3.setBorder(BorderFactory.createEtchedBorder());

		txtAreaReceive.setEditable(false);
		txtAreaReceive.setColumns(35);
		txtAreaReceive.setRows(5);
		txtAreaReceive.setEnabled(false);
		jScrollPane1.setViewportView(txtAreaReceive);

		txtAreaSend.setColumns(35);
		txtAreaSend.setRows(2);
		txtAreaSend.setEnabled(false);
		jScrollPane2.setViewportView(txtAreaSend);

		btnSend.setText("Send");
		btnSend.setEnabled(false);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				service.btnSendActionPerformed(evt);
			}
		});

		btnClear.setText("Clear");
		btnClear.setEnabled(false);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				service.btnClearActionPerformed(evt);
			}
		});

		btnFile.setText("File");
		btnFile.setEnabled(false);

		labelGroup.setFont(new Font("Ubuntu", 0, 14)); // NOI18N
		labelGroup.setText("Last Group: ---");

		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
								.addComponent(labelGroup, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
								.addComponent(jScrollPane1, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(jScrollPane2, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(Alignment.TRAILING,
										jPanel3Layout.createSequentialGroup().addComponent(btnFile)
												.addPreferredGap(ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
												.addComponent(btnClear).addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(btnSend)))
						.addContainerGap()));

		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
						.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(labelGroup)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(jPanel3Layout.createParallelGroup(Alignment.BASELINE).addComponent(btnSend)
								.addComponent(btnClear).addComponent(btnFile))
						.addGap(6)));

		jPanel3.setLayout(jPanel3Layout);

		GroupLayout layout = new GroupLayout(getContentPane());

		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(Alignment.TRAILING)
						.addComponent(jPanel1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addGroup(Alignment.LEADING,
								layout.createSequentialGroup()
										.addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(jPanel2,
												GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
				.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
						.addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addContainerGap()));

		getContentPane().setLayout(layout);

		pack();
	}

	public JButton getBtnLogin() {
		return btnLogin;
	}

	public JButton getBtnSend() {
		return btnSend;
	}

	public JButton getBtnClear() {
		return btnClear;
	}

	public JButton getBtnLogout() {
		return btnLogout;
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

	public JLabel getLabelGroup() {
		return labelGroup;
	}

	public JList<Object> getListOnlines() {
		return listOnlines;
	}
}
