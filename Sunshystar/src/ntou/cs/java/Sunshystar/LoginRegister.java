package ntou.cs.java.Sunshystar;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/*
 * LoginRegister 定義登入與註冊的程式邏輯
 * 負責處理登入驗證與註冊驗證機制，驗證學校信箱格式
 * 
 * @author Jack, Ricky
 */
public class LoginRegister extends Sunshystar {
	public ValidationState login(String mail, String password) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM user_information");
            resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()) {
            	if(mail.equals(resultSet.getString("mail"))) {
            		if(password.equals(resultSet.getString("password"))) {
            			Sunshystar.setMeUID(resultSet.getInt("uid"));
            			return ValidationState.SUCCESS;
            		}
            		else{
            			return ValidationState.PWD_UNEQUAL_ERROR;
            		}
            	}
            }
        }
        catch(SQLException e) {
        	return ValidationState.DATABASE_ERROR;
        }
        finally { 
  	      Close(); 
  	    }
        
        return ValidationState.MAIL_NOT_FOUND_ERROR;
    }
	
	public ValidationState register( String mail, String password, String name, int gender, int department, int grade, int age ) { 
		String mailFormat = "\\w{9}+@ntou\\.edu\\.tw";
		Pattern mailPattern = Pattern.compile(mailFormat);
		Matcher mailMatcher = mailPattern.matcher(mail);
		String passwordFormat = "\\w{6,20}";
		Pattern passwordPattern = Pattern.compile(passwordFormat);
		Matcher passwordMatcher =passwordPattern.matcher(password);
		String nameFormat = ".{1,20}";
		Pattern namePattern = Pattern.compile(nameFormat);
		Matcher nameMatcher = namePattern.matcher(name);
		
		if(!mailMatcher.matches()) {
			return ValidationState.MAIL_FORMAT_ERROR;
		}
		else if(!passwordMatcher.matches()) {
			return ValidationState.PWD_FORMAT_ERROR;
		}
		else if(!nameMatcher.matches()) {
			return ValidationState.NAME_FORMAT_ERROR;
		}
		else {
			try { 
				preparedStatement = connection.prepareStatement("INSERT INTO user_information(uid,mail,name,password,gender,department,grade,age) SELECT IFNULL(max(uid),0)+1,?,?,?,?,?,?,? FROM user_information");
				preparedStatement.setString(1, mail);
				preparedStatement.setString(2, name); 
				preparedStatement.setString(3, password); 
				preparedStatement.setInt(4, gender);
				preparedStatement.setInt(5, department); 
				preparedStatement.setInt(6, grade); 
				preparedStatement.setInt(7, age); 
				preparedStatement.executeUpdate();
				
				// 設定 meUID
				preparedStatement = connection.prepareStatement("SELECT MAX(uid) newUID FROM user_information");
				resultSet = preparedStatement.executeQuery();
	        
				while(resultSet.next()) {
					Sunshystar.setMeUID(resultSet.getInt("newUID"));
				}
			} 
			catch(MySQLIntegrityConstraintViolationException e) {
				return ValidationState.MAIL_HAS_BE_USEED_ERROR;
			}
			catch(SQLException e) { 
				return ValidationState.DATABASE_ERROR; 
			} 
			finally { 
				Close(); 
			} 
		}
		
		// 上傳預設使用者圖片
		setUserDefaultImage();
		
		return ValidationState.SUCCESS;
	}
	
	public boolean setUserDefaultImage() {
		int uid = Sunshystar.getMeUID();
		
		try {
			InputStream fis = getClass().getResourceAsStream("images/baby.jpg");
			preparedStatement = connection.prepareStatement("UPDATE user_information SET image = ? WHERE uid = ?");
			preparedStatement.setBinaryStream(1, (InputStream) fis);
			preparedStatement.setInt(2, uid);
			preparedStatement.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("DropDB Exception :" + e.toString());
		}
		finally {
			Close();
		}
		
		return false;
	}
}
