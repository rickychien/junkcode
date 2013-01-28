package ntou.cs.java.Sunshystar;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/*
 * ProfileGUI 定義使用者個人檔案的圖形介面
 * 透過圖形介面讓使用者更容易的存取或修改資料庫內容
 * 
 * @author Ricky
 */
public class ProfileGUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPasswordField oldPwd;
	private JPasswordField newPwd;
	private JPasswordField comPwd;
	private JTextField name;
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
	private String grade[] = { "1", "2", "3", "4" };
	private String gender[] = { "男", "女" };
	private JLabel imageLabel;
	private BufferedImage image;
	private String nameField = "";
	private String oldPwdField = "";
	private String newPwdField = "";
	private String comPwdField = "";
	private int deptField = 0;
	private int gradeField = 0;
	private int genderField = 0;
	private String ageField = null;
	private int uid = Sunshystar.getMeUID();
	
	ProfileGUI(final Profile profile, BufferedImage image) {
		this.image = image;
		setLayout(new GridBagLayout());
		String mailTitle = "  哈囉！ " + profile.getUserMail(uid) + "  ";
		this.setBorder(BorderFactory.createTitledBorder(mailTitle));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(3, 3, 3, 3);
		
		ImageIcon imageIcon = profile.getUserImage(uid);
		imageLabel = new JLabel(imageIcon);
		imageLabel.setPreferredSize(new Dimension(180, 180));
		imageLabel.setBorder(BorderFactory.createEtchedBorder());
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 7;
		add(imageLabel, gbc);

		JLabel space = new JLabel("            ");
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		add(space, gbc);

		gbc.anchor = GridBagConstraints.WEST;
		
		JLabel label2 = new JLabel("輸入舊密碼  ");
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		add(label2, gbc);

		oldPwd = new JPasswordField(15);
		gbc.gridx = 3;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		add(oldPwd, gbc);

		JLabel label3 = new JLabel("輸入新密碼  ");
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		add(label3, gbc);

		newPwd = new JPasswordField(15);
		gbc.gridx = 3;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		add(newPwd, gbc);

		JLabel label4 = new JLabel("確認新密碼  ");
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		add(label4, gbc);

		comPwd = new JPasswordField(15);
		gbc.gridx = 3;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		add(comPwd, gbc);
		
		JLabel nameLabel = new JLabel("名稱 ");
		gbc.gridx = 2;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		add(nameLabel, gbc);
		
		name = new JTextField(15);
		name.setText(profile.getUserName(uid));
		nameField = name.getText();
		gbc.gridx = 3;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		add(name, gbc);

		JLabel label5 = new JLabel("科系  ");
		gbc.gridx = 2;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		add(label5, gbc);

		dept = new JComboBox(department);
		dept.setMaximumRowCount(5);
		dept.setSelectedIndex(profile.getUserDeptartment(uid));
		deptField = dept.getSelectedIndex();
		gbc.gridx = 3;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		add(dept, gbc);
		
		JLabel label6 = new JLabel("年級  ");
		gbc.gridx = 2;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		add(label6, gbc);

		grad = new JComboBox(grade);
		grad.setMaximumRowCount(5);
		grad.setSelectedIndex(profile.getUserGrade(uid));
		gradeField = grad.getSelectedIndex();
		gbc.gridx = 3;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		add(grad, gbc);

		JLabel label7 = new JLabel("性別  ");
		gbc.gridx = 2;
		gbc.gridy = 8;
		gbc.gridwidth = 1;
		add(label7, gbc);

		gend = new JComboBox(gender);
		gend.setMaximumRowCount(5);
		gend.setSelectedIndex(profile.getUserGender(uid));
		genderField = gend.getSelectedIndex();
		gbc.gridx = 3;
		gbc.gridy = 8;
		gbc.gridwidth = 1;
		add(gend, gbc);

		JLabel label8 = new JLabel("年齡  ");
		gbc.gridx = 2;
		gbc.gridy = 9;
		gbc.gridwidth = 1;
		add(label8, gbc);

		age = new JTextField(3);
		age.setText(String.valueOf(profile.getUserAge(uid)).toString());
		ageField = age.getText();
		gbc.gridx = 3;
		gbc.gridy = 9;
		gbc.gridwidth = 1;
		add(age, gbc);

		gbc.anchor = GridBagConstraints.CENTER;
		
		JButton uploadButton = new JButton("上傳新照片");
		gbc.gridx = 0;
		gbc.gridy = 9;
		gbc.gridwidth = 1;
		add(uploadButton, gbc);

		final JFileChooser fc = new JFileChooser();

		uploadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(ProfileGUI.this);
				Profile user = new Profile();

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					if(user.setUserImage(uid, file)) {
						JOptionPane.showMessageDialog(ProfileGUI.this, "上傳照片成功！");
						ImageIcon imageIcon = user.getUserImage(uid);
						imageLabel.setIcon(imageIcon);
						ChatRoom.sendMsgWriter.println("uid=" + Sunshystar.getMeUID() + "個人照片更新了！");
					} else {
						JOptionPane.showMessageDialog(ProfileGUI.this, "上傳照片失敗，請重新上傳。");
					}
				}
			}
		});
		
		JLabel tipLabel = new JLabel("※圖片像素大小為180x180");
		gbc.gridx = 0;
		gbc.gridy = 11;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(25, 0, 0, 0);
		add(tipLabel, gbc);
		
		JLabel tipLabel2 = new JLabel("若顯示不正確，請裁減至適當大小");
		gbc.gridx = 0;
		gbc.gridy = 12;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(tipLabel2, gbc);

		JButton okButton = new JButton("確定");
		gbc.gridx = 1;
		gbc.gridy = 13;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(50, 5, 5, 5);
		add(okButton, gbc);

		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				oldPwdField = String.valueOf(oldPwd.getPassword());
				newPwdField = String.valueOf(newPwd.getPassword());
				comPwdField = String.valueOf(comPwd.getPassword());
				nameField = name.getText();
				deptField = dept.getSelectedIndex();
				gradeField = grad.getSelectedIndex();
				genderField = gend.getSelectedIndex();
				ageField = age.getText();
				
				Sunshystar.ValidationState state = profile.setProfile(uid, oldPwdField, newPwdField, comPwdField, nameField, deptField, gradeField, genderField, ageField);
				
				// 更新驗證失敗跳出失敗訊息視窗，若成功就顯示更新成功返回聊天室圖形介面
				if(state != Sunshystar.ValidationState.SUCCESS) {
					JOptionPane.showMessageDialog(ProfileGUI.this, state);
				} else {
					JOptionPane.showMessageDialog(ProfileGUI.this, "個人資料更新成功！");
					ChatRoom.sendMsgWriter.println("uid=" + Sunshystar.getMeUID() + "個人資料更新了！");
					Sunshystar.paintChatRoomGUI();
				}
			}
		});

		JButton backButton = new JButton("返回");
		gbc.gridx = 2;
		gbc.gridy = 13;
		gbc.gridwidth = 1;
		add(backButton, gbc);

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Sunshystar.paintChatRoomGUI();
			}
		});
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int x = (getWidth() - image.getWidth());
		int y = (getHeight() - image.getHeight());
		g.drawImage(image, x, y, this);
	}
}
