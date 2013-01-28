package sodar.server.event.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import sodar.server.event.model.EventModel;

public class EditEventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EventModel eventModel;
	
    public EditEventServlet() {
        super();
        eventModel = new EventModel();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonResult = new JSONObject();

		request.setCharacterEncoding("UTF-8");
		
		// 取得Request參數
		String uid = request.getParameter("uid");
		String eid = request.getParameter("eid");
		String type = request.getParameter("event_type");
		String title = request.getParameter("title");
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		String display_time = request.getParameter("display_time");
		String description = request.getParameter("description");
		String longitude = request.getParameter("longitude");
		String latitude = request.getParameter("latitude");
		String APIKey = request.getParameter("api_key");
		
		jsonResult = eventModel.editEvent(eid,uid, type, title, start_time, end_time, display_time, description, longitude, latitude, APIKey);

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.getWriter().print(jsonResult);
		response.getWriter().close();
	}
}
