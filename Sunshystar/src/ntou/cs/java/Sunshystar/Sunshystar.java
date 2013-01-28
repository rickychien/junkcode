package ntou.cs.java.Sunshystar;

import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/*
 * Sunshystar 主程式
 * 負責主程式的流程與管理，呼叫各個子系統執行程式
 * 
 * @author Ricky
 */
public class Sunshystar {
	
	private final String pathOfImages[] = { "images/background.jpg",
			  								"images/balloon.png",
			  								"images/cloud.png",
			  								"images/sunny.png",
			  								"images/depression.png",
			  								"images/freekick.png",
			  								"images/golf.png",
			  								"images/kemal.png",
			  								"images/squiggles.png",
			  								"images/sunflower.png",
			  								"images/thesea.png" };
	private final int guiWidth = 750;
	private final int guiHeight = 550;
	private static int meUID = -1;
	private static int pairUID = 0;
	private static JFrame frame;
	private static Container main;
	
	protected final static String serverIPAddress = "140.121.197.101";
	protected final static int port = 8000;
	protected static Socket socket;
	protected final String driver = "com.mysql.jdbc.Driver";
	protected final String databaseURL = "jdbc:mysql://140.121.197.101:3306/mysql";
	protected final String user = "sunshystar";
	protected final String password = "sunshyboyz";
	protected Connection connection = null;
	protected ResultSet resultSet = null;
	protected PreparedStatement preparedStatement = null;
	
	static BufferedImage bufferedImage;
	static LoginRegisterGUI loginRegisterGUI;
	static ChatRoomGUI chatRoomGUI;
	static ProfileGUI profileGUI;
	static LoginRegister loginRegister;
	static ChatRoom chatRoom;
	static Profile myProfile;
	static Profile pairProfile;
	static Sunshystar sunshystar = new Sunshystar();
	
	enum ValidationState {
		SUCCESS("成功！"),
		DATABASE_ERROR("資料庫發生錯誤！"),
		PWD_UNEQUAL_ERROR("密碼驗證錯誤！"),
		COMFIRM_PWD_UNEQUAL_ERROR("新密碼與確認密碼不相同！"),
		PWD_FIELD_INPUT_ERROR("密碼欄位輸入有誤，若不更新密碼請留空！"),
		MAIL_NOT_FOUND_ERROR("此信箱未註冊！"),
		MAIL_HAS_BE_USEED_ERROR("此信箱已被註冊！"),
		MAIL_FORMAT_ERROR("信箱格式錯誤，格式請參考至底訊息！"),
		PWD_FORMAT_ERROR("密碼至少6個字元，最多不超過20個字元。"),
		NAME_FORMAT_ERROR("名稱至少1個字元，最多不超過20個字元。"),
		AGE_FORMAT_ERROR("大學生年齡應介於18-30之間，請勿謊報年齡！");
		
		private String description;
		
		ValidationState(String description) {
			this.description = description;
		}
		
		public String toString() {
			return description;
		}
		
	};
	
	Sunshystar() {
		Random randomOfImages = new Random();
		String path = pathOfImages[randomOfImages.nextInt(pathOfImages.length)];
		
		//初始化Frame背景圖片
		try {
			bufferedImage = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		//初始化Database
		try {
            Class.forName(driver); 
            connection = DriverManager.getConnection(databaseURL, user, password);
        } 
        catch(ClassNotFoundException e) { 
            e.printStackTrace(); 
        } 
        catch(SQLException e) { 
            e.printStackTrace();
        }
		
		//建立程式子系統
		loginRegister = new LoginRegister();
		loginRegisterGUI = new LoginRegisterGUI(loginRegister, bufferedImage);
		
		//繪製登入與註冊圖形介面
		frame = new JFrame("SunshyStar");
		
		//Paint LoginRegisterGUI
		main = frame.getContentPane();
		main.add(loginRegisterGUI);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(guiWidth, guiHeight);
		frame.setVisible(true);
		
		//讓繪製Frame在螢幕中央
		Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		frame.setLocation(p.x - guiWidth / 2, p.y - guiHeight / 2);
		
		//偵測視窗關閉，就向Server送出訊息
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				//如果對方未登入就離開程式不需向Server發出訊息
				if(ChatRoom.sendMsgWriter != null) {
					ChatRoom.sendMsgWriter.println("out");
					ChatRoom.sendMsgWriter.flush();
				}
			} 
		});
	}
	
	protected void Close() { 
	    try { 
	    	if(resultSet!=null) { 
	    		resultSet.close(); 
	    		resultSet = null; 
	    	}
	    	if(preparedStatement!=null) { 
	    		preparedStatement.close(); 
	    		preparedStatement = null; 
	    	} 
	    } 
	    catch(SQLException e) { 
	      System.out.println("Close Exception :" + e.toString()); 
	    } 
	}
	
	public static void setMeUID(int meUID) {
		Sunshystar.meUID = meUID;
	}

	public static int getMeUID() {
		return meUID;
	}
	
	public static void setYouUID(int youUID) {
		Sunshystar.pairUID = youUID;
	}

	public static int getYouUID() {
		return pairUID;
	}
	
	public static void paintChatRoomGUI() {
		main.removeAll();
		main.add(chatRoomGUI);
		main.repaint();
		main.validate();
	}
	
	public static void paintProfileGUI() {
		main.removeAll();
		main.add(profileGUI);
		main.repaint();
		main.validate();
	}
	
	public static void main(String[] args) {
	}
}
