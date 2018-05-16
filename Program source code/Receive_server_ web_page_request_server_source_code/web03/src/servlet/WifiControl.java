package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import handle.Wifihandler;


public class WifiControl extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String wifinum=req.getParameter("wifinum");
		System.out.println(wifinum);
		if(wifinum!=null){
			if(Wifihandler.CanOpr(wifinum)){
				if(Wifihandler.WifiChange(wifinum)){
					resp.getOutputStream().write("Y".getBytes());
				}else {
					resp.getOutputStream().write("N".getBytes());
				}
			}else{
				resp.getOutputStream().write("N".getBytes());
			}
		}else{
			resp.getOutputStream().write("N".getBytes());
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
