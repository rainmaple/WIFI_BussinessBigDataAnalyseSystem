package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import handle.EnterHandler;
import net.sf.json.JSONObject;

public class EnterRateTableServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jo=new JSONObject();
		double[] r=EnterHandler.GetRateRate();
		String[] rate=EnterHandler.FormatRateRate(r);
		String[] status=EnterHandler.GetRateRateStatus(r);
		jo.put("rate", rate);
		jo.put("status", status);
		resp.getOutputStream().write(jo.toString().getBytes());
		System.out.println(System.getProperty("file.encoding"));
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
