package sodar.server.user.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import sodar.server.user.model.UserProfileModel;

public class EditUserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserProfileModel userProfileModel;
       
    public EditUserProfileServlet() {
        super();
        userProfileModel = new UserProfileModel();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonResult = new JSONObject();

		request.setCharacterEncoding("UTF-8");

		// 取得Request參數
		String uid = request.getParameter("uid");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String APIKey = request.getParameter("api_key");
		
		jsonResult = userProfileModel.editUserProfile(uid, name, phone, APIKey);
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.getWriter().print(jsonResult);
		response.getWriter().close();
	}
}
