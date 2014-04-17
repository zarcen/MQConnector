package mq.openwire;
import java.awt.EventQueue;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.DefaultComboBoxModel;

public class MQOpenwireWindow implements Listener {

	private JFrame frame;
	JTextArea logRecv;
	JTextArea logSend;
	JScrollPane logRecvScrollPane;
	JScrollPane logSendScrollPane;
	JComboBox url_comboBox;
	JComboBox topic_comboBox;
	JComboBox url_comboBox_1;
	JComboBox topic_comboBox_1;
	JComboBox msg_comboBox;
	String[] url_list = { "tcp://localhost:61616", "tcp://140.112.49.153:61616","tcp://140.112.49.154:61616","tcp://140.112.49.155:61616" };
	String[] topic_list = { "ssh.RAW_DATA", "ssh.CONTEXT", "ssh.COMMAND", "ssh.SITUATION",
			"ssh.HCI.SR", "ssh.HCI.TTS", "ssh.HCI.COMMAND.DISPLAY"
	};
	String[] msg_list = { "{\"id\":\"110\",\"subject\":\"temperature\",\"value\":\"27.0\"}",
						  "{\"subject\":\"activity\",\"value\":\"Sleeping\"}",
	};
	
	Consumer receiver;
	boolean rcv_connecting = false;
	Producer sender;
	private JButton btnRecvClear;
	private JButton btnSendClear;
	private JPanel RecvPanel;
	private JPanel SendPanel;
	private JTextField showTextField_Recv;
	private JTextField filterTextField_Recv;
	private JButton msgFilterButton_Recv;
	private JButton button;
	String showOnlyText = "";
	String filterText = "";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MQOpenwireWindow window = new MQOpenwireWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MQOpenwireWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {	
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 626);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		RecvPanel = new JPanel();
		RecvPanel.setBounds(0, 0, 700, 294);
		RecvPanel.setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().add(RecvPanel);
		RecvPanel.setLayout(null);
		
		logRecv = new JTextArea();
		logRecv.setMargin(new Insets(5,5,5,5));
		logRecv.setEditable(false);
		
		logRecvScrollPane = new JScrollPane(logRecv);
		logRecvScrollPane.setBounds(6, 25, 688, 204);
		RecvPanel.add(logRecvScrollPane);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(316, 254, 120, 30);
		RecvPanel.add(btnConnect);
		
		
		url_comboBox = new JComboBox(url_list);
		url_comboBox.setBounds(74, 233, 362, 20);
		RecvPanel.add(url_comboBox);
		url_comboBox.setSelectedIndex(0);
		url_comboBox.setEditable(true);
		
		
		topic_comboBox = new JComboBox(topic_list);
		topic_comboBox.setBounds(74, 258, 238, 20);
		RecvPanel.add(topic_comboBox);
		topic_comboBox.setModel(new DefaultComboBoxModel(new String[] {"ssh.RAW_DATA", "ssh.CONTEXT", "ssh.COMMAND", "ssh.SITUATION", "ssh.HCI.SR", "ssh.HCI.TTS", "ssh.HCI.COMMAND.DISPLAY", "ssh.WK", "ssh.DVS"}));
		topic_comboBox.setSelectedIndex(0);
		topic_comboBox.setEditable(true);
		
		JLabel lblUrl = new JLabel("URL:");
		lblUrl.setBounds(16, 235, 42, 16);
		RecvPanel.add(lblUrl);
		
		JLabel lblTopic = new JLabel("TOPIC:");
		lblTopic.setBounds(16, 260, 46, 16);
		RecvPanel.add(lblTopic);
		
		JLabel lblReceiver = new JLabel("Receiver");
		lblReceiver.setBounds(6, 5, 60, 16);
		RecvPanel.add(lblReceiver);
		
		btnRecvClear = new JButton("Clear");
		btnRecvClear.setBounds(583, 0, 117, 29);
		RecvPanel.add(btnRecvClear);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(442, 233, 252, 51);
		RecvPanel.add(panel);
		panel.setLayout(null);
		
		showTextField_Recv = new JTextField();
		showTextField_Recv.setBounds(72, 3, 120, 22);
		panel.add(showTextField_Recv);
		showTextField_Recv.setColumns(10);
		
		JLabel lblShowOnly = new JLabel("Show only:");
		lblShowOnly.setBounds(6, 6, 68, 16);
		panel.add(lblShowOnly);
		
		JLabel lblFilter = new JLabel("Filter:");
		lblFilter.setBounds(6, 29, 61, 16);
		panel.add(lblFilter);
		
		filterTextField_Recv = new JTextField();
		filterTextField_Recv.setColumns(10);
		filterTextField_Recv.setBounds(72, 26, 120, 22);
		panel.add(filterTextField_Recv);
		
		msgFilterButton_Recv = new JButton("Set");
		msgFilterButton_Recv.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		msgFilterButton_Recv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(showTextField_Recv.getText().equals("")) {
					showOnlyText="";
				}
				else {
					showOnlyText = showTextField_Recv.getText();
				}
			}
		});
		msgFilterButton_Recv.setBounds(196, 4, 50, 21);
		panel.add(msgFilterButton_Recv);
		
		button = new JButton("Set");
		button.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(filterTextField_Recv.getText().equals("")) {
					filterText="";
				}
				else {
					filterText = filterTextField_Recv.getText();
				}
			}
		});
		button.setBounds(196, 28, 50, 21);
		panel.add(button);
		btnRecvClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recvClearButtonAction();
			}
		});
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recvConnectButtonAction(e);
			}
		});
		
		SendPanel = new JPanel();
		SendPanel.setBounds(0, 300, 700, 298);
		SendPanel.setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().add(SendPanel);
		SendPanel.setLayout(null);
		
		
		logSend = new JTextArea();
		logSend.setMargin(new Insets(5,5,5,5));
		logSend.setEditable(false);
		
		logSendScrollPane = new JScrollPane(logSend);
		logSendScrollPane.setBounds(6, 25, 688, 180);
		SendPanel.add(logSendScrollPane);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(441, 253, 120, 30);
		SendPanel.add(btnSend);
		
		JLabel lblSendor = new JLabel("Sender");
		lblSendor.setBounds(6, 3, 60, 16);
		SendPanel.add(lblSendor);
		
		JLabel label = new JLabel("URL:");
		label.setBounds(16, 211, 42, 16);
		SendPanel.add(label);
		
		JLabel label_1 = new JLabel("TOPIC:");
		label_1.setBounds(16, 235, 46, 16);
		SendPanel.add(label_1);
		
		url_comboBox_1 = new JComboBox(url_list);
		url_comboBox_1.setBounds(74, 209, 362, 20);
		SendPanel.add(url_comboBox_1);
		url_comboBox_1.setSelectedIndex(0); // initialize at url_list[0]
		url_comboBox_1.setEditable(true);
		
		topic_comboBox_1 = new JComboBox(topic_list);
		topic_comboBox_1.setBounds(74, 233, 362, 20);
		SendPanel.add(topic_comboBox_1);
		topic_comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"ssh.RAW_DATA", "ssh.CONTEXT", "ssh.COMMAND", "ssh.SITUATION", "ssh.HCI.SR", "ssh.HCI.TTS", "ssh.HCI.COMMAND.DISPLAY", "ssh.WK", "ssh.DVS"}));
		topic_comboBox_1.setSelectedIndex(0); // initialize at topic_list[0]
		topic_comboBox_1.setEditable(true);
		
		JLabel lblNewLabel = new JLabel("Message:");
		lblNewLabel.setBounds(16, 259, 61, 16);
		SendPanel.add(lblNewLabel);
		
		msg_comboBox = new JComboBox(msg_list);
		msg_comboBox.setBounds(74, 257, 362, 20);
		msg_comboBox.setMaximumRowCount(20);
		SendPanel.add(msg_comboBox);
		msg_comboBox.setEditable(true);
		msg_comboBox.setToolTipText((String)msg_comboBox.getSelectedItem());
		
		btnSendClear = new JButton("Clear");
		btnSendClear.setBounds(583, -2, 117, 29);
		SendPanel.add(btnSendClear);
		btnSendClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendClearButtonAction();
			}
		});
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendButtonAction(e);
			}
		});
	}
	
	void recvConnectButtonAction(ActionEvent e) {
		if(rcv_connecting) {
			receiver.disconnect();
			if (!receiver.isStarted()) {
				((JButton)(e.getSource())).setText("Connect");
				rcv_connecting = false;
			} else {
				logRecv.append("Error: connection cannot be closed.\n");
			}
			return;
		}
		else {	
			String url_now = (String)(url_comboBox.getSelectedItem());
			if (Arrays.asList(url_list).contains(url_now) == false) {
				url_comboBox.addItem(url_now);
			}
			
			String topic_now = (String)(topic_comboBox.getSelectedItem());
			if (Arrays.asList(topic_list).contains(url_now) == false) {
				topic_comboBox.addItem(topic_now);
			}
			receiver = new Consumer();
			receiver.setURL(url_now);
			receiver.setTopic(topic_now);
			receiver.connect(); // construct connection
			receiver.listen(); // listen to the default topic
			if (receiver.isStarted()) {
				((JButton)(e.getSource())).setText("Disconnect");
				rcv_connecting = true;
				receiver.addListener(this);
			}
			else {
				logRecv.append("Error: Could not connect to broker URL\n");
			}
		}
	}
	
	void sendButtonAction(ActionEvent e) {
		String url_now = (String)(url_comboBox_1.getSelectedItem());
		if (Arrays.asList(url_list).contains(url_now) == false) {
			url_comboBox_1.addItem(url_now);
		}
		
		String topic_now = (String)(topic_comboBox_1.getSelectedItem());
		if (Arrays.asList(topic_list).contains(url_now) == false) {
			topic_comboBox_1.addItem(topic_now);
		}
		
		String msg_now = (String)(msg_comboBox.getSelectedItem());
		if (Arrays.asList(msg_list).contains(url_now) == false) {
			msg_comboBox.addItem(msg_now);
		}
		
		sender = new Producer();
		sender.setURL(url_now);
		sender.setTopic(topic_now);
		sender.connect();
		sender.getSendor();
		if (sender.isStarted()) {
			sender.sendOut((String)msg_comboBox.getSelectedItem());
			logSend.append(sender.getMsg()+'\n');
			sender.disconnect();
		} else {
			logSend.append("Error: Could not connect to broker URL\n");
		}
	}
	
	void recvClearButtonAction() {
		logRecv.replaceRange("", 0, logRecv.getText().length()); 
	}
	
	void sendClearButtonAction() {
		logSend.replaceRange("", 0, logSend.getText().length()); 
	}

	@Override
	public void onEvent(String str) {
		if(showOnlyText.equals("") && filterText.equals("")) {
			logRecv.append(str+'\n');
			logRecv.setCaretPosition(logRecv.getDocument().getLength()); // set the cursor to last line(move the scrollbar downest)
		}
		else if(str.contains(showOnlyText) && ((filterText.equals("")) || !(str.contains(filterText)))) {
			logRecv.append(str+'\n');
			logRecv.setCaretPosition(logRecv.getDocument().getLength()); // set the cursor to last line(move the scrollbar downest)
		}
	}
}
