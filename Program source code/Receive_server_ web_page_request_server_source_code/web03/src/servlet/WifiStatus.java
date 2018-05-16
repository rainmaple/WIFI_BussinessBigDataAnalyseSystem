package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import handle.StatusHandler;
import handle.Wifihandler;
import net.sf.json.JSONObject;
import util.Util;

public class WifiStatus extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String numstr=req.getParameter("num");
		String beginstr=req.getParameter("begin");
		int num=8;
		int begin=0;
		if(numstr!=null && numstr.length()>0){
			num=Integer.valueOf(numstr);
		}
		if(beginstr!=null && beginstr.length()>0){
			begin=Integer.valueOf(beginstr);
		}
		List<String> status=Wifihandler.getWifiStatus(begin, num);
		String[] type=new String[num];
		for(int i=0;i<=num-1;i++){
			if(StatusHandler.GetStatus(String.valueOf(i+1))) type[i]="状态良好";
			else type[i]="非正常";
		}
		JSONObject jo=new JSONObject();
		jo.put("status", status);
		jo.put("type", type);
		resp.getOutputStream().write(jo.toString().getBytes());
		System.out.println(status);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
