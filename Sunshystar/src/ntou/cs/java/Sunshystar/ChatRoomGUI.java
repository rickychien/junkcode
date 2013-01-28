package ntou.cs.java.Sunshystar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * ChatRoomGUI 定義聊天室的圖形介面
 * 雙人隨機聊天室的圖形介面，顯示配對的對方照片及資訊並互相聊天
 * 可透過此聊天介面進入個人檔案介面或切換下一位隨機的聊天對象
 * 
 * @author Jack, Howard
 */
public class ChatRoomGUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel imageTitle = new JLabel("您現在的聊天對象是...");
	private ImageIcon imageIcon = new ImageIcon();
	private JLabel imageLabel = new JLabel();
	private JTextArea profile = new JTextArea(10, 5);
	static JTextArea messageBox = new JTextArea();
	private JTextField inputMessage = new JTextField(49);
	private JScrollPane messageBoxScrollPane = new JScrollPane(messageBox);
	private JButton nextPerson;
	private JButton setProfile;
	private JButton changeColorButton;
	private JButton changeTextColor;
	private BufferedImage image;
	private Color messageBoxColor = new Color(51, 170, 191);
	private Container container1;
	private Container container2;
	private Container container3;
	private String department[] = { "商船學系",
									"航運管理學系",
									"運輸科學系",
									"輪機工程學系",
									"食品科學系",
									"水產養殖學系",
									"生命科學系",
									"海洋生物研究所",
									"生物科技研究所",
									"環境生物與漁業科學系 ",
									"海洋環境資訊系",
									"海洋事務與資源管理研究所",
									"海洋環境化學與生態研究所",
									"機械與機電工程學系",
									"系統工程暨造船學系",
									"河海工程學系",
									"材料工程研究所",
									"電機工程學系",
									"資訊工程學系",
									"通訊與導航工程學系",
									"光電科學研究所",
									"海洋法律研究所",
									"應用經濟研究所",
									"教育研究所",
									"海洋文化研究所",
									"應用英語研究所",
									"航運管理學系(進修部)",
									"食品科學學系(進修部)",
									"電機工程學系(進修部)",
									"資訊管理學系(進修部)",
									"海洋資源管理學系(進修部)",
									"河海工程學系(進修部)",
									"商船學系(進修部)",
									"環境生物與漁業科學系(進修部)",
									"海洋法律研究所(進修部)",
									"海洋環境資訊系(進修部)" };
	private String grade[] = { "1", "2", "3", "4" };
	private String gender[] = { "男", "女" };

	public ChatRoomGUI(final ChatRoom chatRoom, BufferedImage image) {
		int youUID = Sunshystar.getYouUID();
		String profileSet = "\n暱稱："
				+ Sunshystar.pairProfile.getUserName(youUID) + "\n性別："
				+ gender[Sunshystar.pairProfile.getUserGender(youUID)]
				+ "\n年齡：" + Sunshystar.pairProfile.getUserAge(youUID) + "\n系所："
				+ department[Sunshystar.pairProfile.getUserDeptartment(youUID)]
				+ "\n年級：" + grade[Sunshystar.pairProfile.getUserGrade(youUID)];
		String nameTitle = "  哈囉！ " + Sunshystar.pairProfile.getUserName(Sunshystar.getMeUID()) + "  ";
		
		this.image = image;
		this.setBorder(BorderFactory.createTitledBorder(nameTitle));

		setLayout(new BorderLayout(10, 10));

		container1 = new Container();
		container1.setLayout(new FlowLayout());

		container2 = new Container();
		container2.setLayout(new BorderLayout());

		container3 = new Container();
		container3.setLayout(new BorderLayout());

		imageTitle.setOpaque(false);
		imageTitle.setFont(new Font("Serif", Font.BOLD, 14));
		imageTitle.setForeground(Color.BLACK);
		container2.add(imageTitle, BorderLayout.NORTH);

		imageIcon = Sunshystar.pairProfile.getUserImage(youUID);
		imageLabel.setIcon(imageIcon);
		imageLabel.setPreferredSize(new Dimension(180, 180));
		imageLabel.setBorder(BorderFactory.createEtchedBorder());
		container2.add(imageLabel, BorderLayout.CENTER);

		profile.setLineWrap(true);
		profile.setWrapStyleWord(true);
		profile.setEditable(false);
		profile.setOpaque(false);
		profile.setText(profileSet);
		profile.setFont(new Font("Serif", Font.BOLD, 13));
		profile.setForeground(Color.BLACK);
		container2.add(profile, BorderLayout.SOUTH);

		messageBox.setBackground(messageBoxColor);
		messageBox.setLineWrap(true);
		messageBox.setWrapStyleWord(true);
		messageBox.setEditable(false);
		add(messageBoxScrollPane, BorderLayout.CENTER);

		inputMessage.setEditable(true);
		container3.add(new JScrollPane(inputMessage), BorderLayout.EAST);
		inputMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = inputMessage.getText();

				if (msg.trim().equals("")) {
					JOptionPane.showMessageDialog(ChatRoomGUI.this, "請輸入聊天內容");
					inputMessage.requestFocus();
					return;
				}

				// 發送訊息至Server
				String name = Sunshystar.myProfile.getUserName(Sunshystar.getMeUID());
				ChatRoom.sendMsgWriter.println(name + "：" + msg);

				// 使用者在打字區中送出訊息後清空打字區
				inputMessage.setText("");
			}
		});

		changeColorButton = new JButton("更換面板顏色");
		changeColorButton.setPreferredSize(new Dimension(130, 40));
		container1.add(changeColorButton);
		changeColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				messageBoxColor = JColorChooser.showDialog(ChatRoomGUI.this,
						"請選擇一種版面顏色", messageBoxColor);
				if (messageBoxColor == null)
					messageBoxColor = new Color(51, 170, 191);
				messageBox.setBackground(messageBoxColor);
			}
		});
		
		changeTextColor = new JButton("更換文字顏色");
		changeTextColor.setPreferredSize(new Dimension(130, 40));
		container1.add(changeTextColor);
		changeTextColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				messageBoxColor = JColorChooser.showDialog(ChatRoomGUI.this,
						"請選擇一種文字顏色", messageBoxColor);
				if (messageBoxColor == null)
					messageBoxColor = new Color(0, 0, 0);
				messageBox.setForeground(messageBoxColor);
			}
		});

		setProfile = new JButton("編輯個人資料");
		setProfile.setPreferredSize(new Dimension(130, 40));
		container1.add(setProfile);
		setProfile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Sunshystar.paintProfileGUI();
			}
		});

		nextPerson = new JButton("下一位");
		nextPerson.setPreferredSize(new Dimension(130, 40));
		container1.add(nextPerson);
		nextPerson.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ChatRoom.sendMsgWriter.println("change");
			}
		});

		add(container1, BorderLayout.NORTH);
		add(container2, BorderLayout.WEST);
		add(container3, BorderLayout.SOUTH);
	}
	
	public void setChatRoomGUIInfo() {
		int youUID = Sunshystar.getYouUID();
		String profileSet = "\n暱稱："
			+ Sunshystar.pairProfile.getUserName(youUID) + "\n性別："
			+ gender[Sunshystar.pairProfile.getUserGender(youUID)]
			+ "\n年齡：" + Sunshystar.pairProfile.getUserAge(youUID) + "\n系所："
			+ department[Sunshystar.pairProfile.getUserDeptartment(youUID)]
			+ "\n年級：" + grade[Sunshystar.pairProfile.getUserGrade(youUID)];
		String nameTitle = "  哈囉！ " + Sunshystar.pairProfile.getUserName(Sunshystar.getMeUID()) + "  ";
		
		this.setBorder(BorderFactory.createTitledBorder(nameTitle));
		imageIcon = Sunshystar.pairProfile.getUserImage(youUID);
		imageLabel.setIcon(imageIcon);
		profile.setText(profileSet);
		
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int x = (getWidth() - image.getWidth()) / 2;
		int y = (getHeight() - image.getHeight()) / 2;
		g.drawImage(image, x, y, this);
	}
}
