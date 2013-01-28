package sodar.server.sign.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import sodar.server.sign.model.SignModel;

public class SignServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SignModel signInModel;

	public SignServlet() {
		super();
		signInModel = new SignModel();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonResult = new JSONObject(); // 回傳insert會員資料成功與否的狀態
		
		request.setCharacterEncoding("UTF-8");
		
		// 取得Request參數
		String uid = request.getParameter("uid");
		String name = request.getParameter("name");
		String sex = request.getParameter("sex");
		String aboutMe = request.getParameter("aboutMe");
		String educationHistory = request.getParameter("educationHistory");
		String workHistory = request.getParameter("workHistory");
		String APIKey = request.getParameter("api_key");
		
		jsonResult = signInModel.SignIn(uid, name, sex, aboutMe, educationHistory, workHistory, APIKey);
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.getWriter().print(jsonResult);
		response.getWriter().close();
	}
}
