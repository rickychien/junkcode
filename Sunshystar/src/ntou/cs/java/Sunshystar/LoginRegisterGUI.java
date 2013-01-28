package ntou.cs.java.Sunshystar;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/*
 * LoginRegisterGUI 定義登入與註冊的圖形介面
 * 程式的進入開始畫面，繪製登入與註冊基本資訊的圖形介面
 * 
 * @author Jack, Ricky
 */
public class LoginRegisterGUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel spacelabel;
	private JLabel title;
	private JLabel description;
	private JLabel notice;
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JLabel label4;
	private JLabel label5;
	private JLabel label6;
	private JLabel label7;
	private JLabel label8;
	private JLabel label9;
	private JLabel label10;
	private JLabel label11;
	private JLabel label12;
	private JTextField textfield1;
	private JTextField textfield2;
	private JTextField textfield3;
	private JPasswordField passwordfield1;
	private JPasswordField passwordfield2;
	private JPasswordField passwordfield3;
	private JButton button1;
	private JButton button2;
	private JComboBox dept;
	private JComboBox grad;
	private JComboBox gend;
	private JTextField age;
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
	private String grade[] = {"1", "2", "3", "4"};
	private String gender[] = {"男", "女"};
	private int gradIndex;
	private int gendIndex;
	private int deptIndex;
	private BufferedImage image;
	
	public LoginRegisterGUI(final LoginRegister loginRegister, final BufferedImage image) {
		this.image = image;
		GridBagLayout gridbag = new GridBagLayout();
	    GridBagConstraints cons = new GridBagConstraints();
		setLayout( gridbag );
		
		cons.gridx = 1;
		cons.gridy = 0;
		cons.gridwidth = 10;
		title = new JLabel("SunshyStar");
		title.setFont( new Font("Serif", Font.BOLD, 80) );
		gridbag.setConstraints(title, cons);
		add(title);
		
		cons.gridx = 1;
		cons.gridy = 1;
		cons.gridwidth = 10;
		cons.insets = new Insets(5, 0, 5, 0);
		description = new JLabel("馬上加入，不要害羞！");
		description.setFont( new Font("Serif", Font.ITALIC, 20) );
		gridbag.setConstraints(description, cons);
		add(description);
		
		cons.insets = new Insets(2, 0, 2, 0);
		
		cons.gridx = 1;
		cons.gridy = 2;
		cons.gridwidth = 2;
		label1 = new JLabel("Login");
		label1.setFont( new Font("Serif", Font.BOLD, 40) );
		gridbag.setConstraints(label1, cons);
		add(label1);
		
		cons.gridx = 1;
		cons.gridy = 3;
		cons.gridwidth = 1;
		label2 = new JLabel("學校信箱  ");
		gridbag.setConstraints(label2, cons);
		add(label2);
		
		cons.gridx = 2;
		cons.gridy = 3;
		cons.gridwidth = 1;
		textfield1 = new JTextField(15);
		gridbag.setConstraints(textfield1, cons);
		add(textfield1);
		
		cons.gridx = 1;
		cons.gridy = 4;
		cons.gridwidth = 1;
		label3 = new JLabel("登入密碼  ");
		gridbag.setConstraints(label3, cons);
		add(label3);
		
		cons.gridx = 2;
		cons.gridy = 4;
		cons.gridwidth = 1;
		passwordfield1 = new JPasswordField(15);
		gridbag.setConstraints(passwordfield1, cons);
		add(passwordfield1);
		
		cons.gridx = 1;
		cons.gridy = 6;
		cons.gridwidth = 2;
		button1 = new JButton("登入");
		gridbag.setConstraints(button1, cons);
		add(button1);
		
		button1.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						textfield1.setText("B97570136@ntou.edu.tw");
						passwordfield1.setText("123456");
						
						if(textfield1.getText().equals("")) {
							JOptionPane.showMessageDialog(LoginRegisterGUI.this, "信箱欄位未輸入");
						}
						else if(String.valueOf(passwordfield1.getPassword()).equals("")) {
							JOptionPane.showMessageDialog(LoginRegisterGUI.this, "密碼欄位未輸入");
						}
						else {
							Sunshystar.ValidationState state = loginRegister.login(textfield1.getText(), String.valueOf(passwordfield1.getPassword()));
							
							//登入失敗跳出錯誤訊息視窗
							if(state != Sunshystar.ValidationState.SUCCESS) {
								JOptionPane.showMessageDialog(LoginRegisterGUI.this, state) ;
							}
							//成功登入後會設定與取得雙方uid，並建立ChatRoom系統與Profile系統
							else {
								loginPreparation();
							}
						}
					}
				}
		);
		
		cons.gridx = 4;
		cons.gridy = 1;
		cons.gridwidth = 2;
		spacelabel = new JLabel("                ");
		gridbag.setConstraints(spacelabel, cons);
		add(spacelabel);
		
		cons.gridx = 6;
		cons.gridy = 2;
		cons.gridwidth = 4;
		label4 = new JLabel("Register");
		label4.setFont( new Font("Serif", Font.BOLD, 40) );
		gridbag.setConstraints(label4, cons);
		add(label4);
		
		cons.anchor = GridBagConstraints.WEST;
		
		cons.gridx = 6;
		cons.gridy = 3;
		cons.gridwidth = 1;
		label5 = new JLabel("學校信箱  ");
		gridbag.setConstraints(label5, cons);
		add(label5);
		
		cons.gridx = 7;
		cons.gridy = 3;
		cons.gridwidth = 4;
		textfield2 = new JTextField(20);
		gridbag.setConstraints(textfield2, cons);
		add(textfield2);
		
		cons.gridx = 6;
		cons.gridy = 4;
		cons.gridwidth = 1;
		label6 = new JLabel("登入密碼  ");
		gridbag.setConstraints(label6, cons);
		add(label6);
		
		cons.gridx = 7;
		cons.gridy = 4;
		cons.gridwidth = 4;
		passwordfield2 = new JPasswordField(20);
		gridbag.setConstraints(passwordfield2, cons);
		add(passwordfield2);
		
		cons.gridx = 6;
		cons.gridy = 5;
		cons.gridwidth = 1;
		label7 = new JLabel("確認密碼  ");
		gridbag.setConstraints(label7, cons);
		add(label7);
		
		cons.gridx = 7;
		cons.gridy = 5;
		cons.gridwidth = 4;
		passwordfield3 = new JPasswordField(20);
		gridbag.setConstraints(passwordfield3, cons);
		add(passwordfield3);
		
		cons.gridx = 6;
		cons.gridy = 6;
		cons.gridwidth = 1;
		label8 = new JLabel("名稱  ");
		gridbag.setConstraints(label8, cons);
		add(label8);
		
		cons.fill = GridBagConstraints.HORIZONTAL;
		
		cons.gridx = 7;
		cons.gridy = 6;
		cons.gridwidth = 4;
		textfield3 = new JTextField(20);
		gridbag.setConstraints(textfield3, cons);
		add(textfield3);
		
		cons.gridx = 6;
		cons.gridy = 7;
		cons.gridwidth = 1;
		label9 = new JLabel("系所  ");
		gridbag.setConstraints(label9, cons);
		add(label9);
		
		cons.gridx = 7;
		cons.gridy = 7;
		cons.gridwidth = 4;
		dept = new JComboBox(department);
		dept.setMaximumRowCount(5);
		gridbag.setConstraints(dept, cons);
		add(dept);
		
		dept.addItemListener(
				new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent event) {
						if ( event.getStateChange() == ItemEvent.SELECTED )
							deptIndex = dept.getSelectedIndex();
					}
				}
		);
		
		cons.gridx = 6;
		cons.gridy = 8;
		cons.gridwidth = 1;
		label10 = new JLabel("年級  ");
		gridbag.setConstraints(label10, cons);
		add(label10);
		
		cons.gridx = 7;
		cons.gridy = 8;
		cons.gridwidth = 4;
		grad = new JComboBox(grade);
		grad.setMaximumRowCount(5);
		gridbag.setConstraints(grad, cons);
		add(grad);
		
		grad.addItemListener(
				new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent event) {
						if ( event.getStateChange() == ItemEvent.SELECTED )
							gradIndex = grad.getSelectedIndex();
					}
				}
		);
		
		cons.gridx = 6;
		cons.gridy = 9;
		cons.gridwidth = 1;
		cons.fill = GridBagConstraints.NONE;
		label12 = new JLabel("性別  ");
		gridbag.setConstraints(label12, cons);
		add(label12);
		
		cons.gridx = 7;
		cons.gridy = 9;
		cons.gridwidth = 1;
		gend = new JComboBox(gender);
		gend.setMaximumRowCount(5);
		gridbag.setConstraints(gend, cons);
		add(gend);
		
		gend.addItemListener(
				new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent event) {
						if ( event.getStateChange() == ItemEvent.SELECTED )
							gendIndex = gend.getSelectedIndex();
					}
				}
		);
		
		cons.gridx = 8;
		cons.gridy = 9;
		cons.gridwidth = 1;
		JLabel space = new JLabel("                  ");
		gridbag.setConstraints(space, cons);
		add(space);
		
		cons.gridx = 9;
		cons.gridy = 9;
		cons.gridwidth = 1;
		label11= new JLabel("年齡  ");
		gridbag.setConstraints(label11, cons);
		add(label11);
		
		cons.gridx = 10;
		cons.gridy = 9;
		cons.gridwidth = 1;
		age = new JTextField(5);
		gridbag.setConstraints(age, cons);
		add(age);
		
		cons.gridx = 6;
		cons.gridy = 11;
		cons.gridwidth = 5;
		spacelabel = new JLabel("                          ");
		gridbag.setConstraints(spacelabel, cons);
		add(spacelabel);
		
		cons.anchor = GridBagConstraints.CENTER;
		
		cons.gridx = 7;
		cons.gridy = 12;
		cons.gridwidth = 3;
		cons.fill = 0;
		button2 = new JButton("註冊");
		gridbag.setConstraints(button2, cons);
		add(button2);
		
		button2.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						int ageNum = 0;
						
						try {
							ageNum = Integer.parseInt(age.getText());
						}
						catch(NumberFormatException e) {
							ageNum = 0;
							JOptionPane.showMessageDialog( LoginRegisterGUI.this, "請填入正確的年齡" );
						}
						
						if(textfield2.getText().equals("")) {
							JOptionPane.showMessageDialog( LoginRegisterGUI.this, "信箱欄位未輸入" );
						}
						else if(String.valueOf(passwordfield2.getPassword()).equals("")) {
							JOptionPane.showMessageDialog( LoginRegisterGUI.this, "密碼欄位未輸入" );
						}
						else if(String.valueOf(passwordfield3.getPassword()).equals("")) {
							JOptionPane.showMessageDialog( LoginRegisterGUI.this, "確認密碼欄位未輸入" );
						}
						else if(textfield3.getText().equals("")) {
							JOptionPane.showMessageDialog( LoginRegisterGUI.this, "暱稱欄位未輸入" );
						}
						else if(ageNum < 18 || ageNum > 30) {
							JOptionPane.showMessageDialog( LoginRegisterGUI.this, "大學生年齡應介於18-30之間，請勿謊報年齡！" );
						}
						else if(!String.valueOf(passwordfield2.getPassword()).equals(String.valueOf(passwordfield3.getPassword()))) {
							JOptionPane.showMessageDialog( LoginRegisterGUI.this, "兩次密碼輸入相異" );
						}
						else {
							Sunshystar.ValidationState state = loginRegister.register(textfield2.getText(), String.valueOf(passwordfield2.getPassword()), textfield3.getText(), gendIndex, deptIndex, gradIndex, Integer.parseInt(age.getText()));
							
							// 註冊失敗跳出錯誤訊息視窗，註冊成功進入聊天室，
							if(state != Sunshystar.ValidationState.SUCCESS) {
								JOptionPane.showMessageDialog( LoginRegisterGUI.this, state) ;
							}
							else {
								JOptionPane.showMessageDialog( LoginRegisterGUI.this, "註冊成功！ 馬上開始 SunshyStar！") ;
								loginPreparation();
							}
						}
					}
				}
		);
		
		cons.gridx = 0;
		cons.gridy = 13;
		cons.gridwidth = 10;
		spacelabel = new JLabel("                          ");
		gridbag.setConstraints(spacelabel, cons);
		add(spacelabel);
		
		cons.gridx = 0;
		cons.gridy = 14;
		cons.gridwidth = 10;
		notice = new JLabel("※信箱為國立台灣海洋大學之信箱, 如：B12345678@ntou.edu.tw");
		gridbag.setConstraints(notice, cons);
		add(notice);
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = (getWidth() - image.getWidth()) / 2;
        int y = (getHeight() - image.getHeight()) / 2;
        g.drawImage(image, x, y, this);
    }
	
	public void loginPreparation() {
		Sunshystar.pairProfile = new Profile();
		Sunshystar.chatRoom = new ChatRoom();
		Sunshystar.chatRoomGUI = new ChatRoomGUI(Sunshystar.chatRoom, image);
		Sunshystar.myProfile = new Profile();
		Sunshystar.profileGUI = new ProfileGUI(Sunshystar.myProfile, image);

		// 建立Thread 接收訊息
		Thread receiveMsgThread = new Thread(new ReceiveMsgThread());
		receiveMsgThread.start();
		
		Sunshystar.paintChatRoomGUI();
	}

}
