package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import handle.PassFlowHandler;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PassFlowClockServlet extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] arr=PassFlowHandler.GetClockNum();
		JSONArray ja = JSONArray.fromObject(arr);
		JSONObject jo=new JSONObject();
		jo.put("Num", ja);
		System.out.println(jo);
		resp.getOutputStream().write(jo.toString().getBytes());
	}
}
