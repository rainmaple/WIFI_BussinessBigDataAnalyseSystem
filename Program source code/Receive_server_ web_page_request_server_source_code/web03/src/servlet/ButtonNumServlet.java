package servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import handle.ButtonHandler;
import handle.PassFlowHandler;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ButtonNumServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int length=0;
		String str;
		if((str=req.getParameter("num"))!=null){
			length=Integer.valueOf(str);
		}else {
			resp.getOutputStream().write("N".getBytes());
			return ;
		}
		String method_name="Get"+req.getParameter("type")+"Num";
		String[] FlowArr=null;
		try {
			System.out.println(req.getParameter("type")+" "+ method_name);
			Method m=ButtonHandler.class.getMethod(method_name, int.class);
			FlowArr =(String[]) m.invoke(null, length);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			resp.getOutputStream().write("N".getBytes());
			e.printStackTrace();
			return ;
		}
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
