package servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import handle.PassFlowHandler;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PassFlowPreServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String []FlowArr =PassFlowHandler.GetWeekpre();
		JSONArray ja = JSONArray.fromObject(FlowArr);
		JSONObject jo=new JSONObject();
		jo.put("Flow", ja);
		System.out.println(jo);
		resp.getOutputStream().write(jo.toString().getBytes());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
