package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import handle.LivenessHandler;
import handle.PassFlowHandler;
import net.sf.json.JSONObject;

public class LivenessTableServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jo=new JSONObject();
		String type=req.getParameter("type");
		double[] r=LivenessHandler.GetRate(Integer.valueOf(type));
		String[] rate=LivenessHandler.FormatRate(r);
		String[] status=LivenessHandler.GetRateStatus(r);
		jo.put("rate", rate);
		jo.put("status", status);
		resp.getOutputStream().write(jo.toString().getBytes());
		
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
