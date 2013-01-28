package sodar.server.event.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import sodar.server.event.model.EventModel;

public class GetParticipantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EventModel eventModel;

	public GetParticipantServlet() {
		super();
		eventModel = new EventModel();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	        IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	        IOException {
		JSONObject jsonResult = new JSONObject();

		request.setCharacterEncoding("UTF-8");

		// 取得Request參數
		String eid = request.getParameter("eid");
		String APIKey = request.getParameter("api_key");

		jsonResult = eventModel.getParticipant(eid, APIKey);

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.getWriter().print(jsonResult);
		response.getWriter().close();
	}
}
