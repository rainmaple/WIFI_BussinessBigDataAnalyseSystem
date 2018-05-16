package session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Log extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String user=req.getParameter("u");
		String pass=req.getParameter("p");
		String username="859732541";
		String password="asd";
		String admin = "admin";
		String adpass="123";
		System.out.println(username+" "+password);
		if(user!=null && user.equals(admin)){
			if( pass!=null && pass.equals(password)){
				req.getSession().setAttribute("admin", user);
				resp.getOutputStream().write("y".getBytes());
			}
			return ;
		}else{
			if(user!=null && user.equals(username)){
				if( pass!=null && pass.equals(password)){
					req.getSession().setAttribute("user", user);
					resp.getOutputStream().write("y".getBytes());
				}
			}else{
				resp.getOutputStream().write("n".getBytes());
			}
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
