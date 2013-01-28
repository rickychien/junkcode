package sodar.server.search.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import sodar.server.search.model.SearchModel;

public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SearchModel searchModel;

	public SearchServlet() {
		super();
		searchModel = new SearchModel();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/test.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	        IOException {
		JSONObject jsonResult = new JSONObject();

		request.setCharacterEncoding("UTF-8");
		// 取得Request參數
		String searchText = request.getParameter("search_text");
		String uid = request.getParameter("uid_me");
		String range = request.getParameter("search_range");
		String relationship = request.getParameter("relationship");
		String eventType = request.getParameter("event_type");
		String eventTimeRange = request.getParameter("event_time_range");
		String clientGPSLongitude = request.getParameter("client_longitude");
		String clientGPSLatitude = request.getParameter("client_latitude");
		String accessToken = request.getParameter("access_token");
		String APIKey = request.getParameter("api_key");
		
		// 使用者透過文字搜尋或者條件搜尋
		if (searchText == null) {
			jsonResult = searchModel.search(uid, range, relationship, eventType, eventTimeRange, clientGPSLongitude,
			        clientGPSLatitude, accessToken, APIKey);
		}
		else {
			jsonResult = searchModel.search(uid, searchText, clientGPSLongitude, clientGPSLatitude, accessToken, APIKey);
		}

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.getWriter().print(jsonResult);
		response.getWriter().close();
	}
}
