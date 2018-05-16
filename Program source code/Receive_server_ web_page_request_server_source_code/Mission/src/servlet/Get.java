package servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Dao;
import handler.MissionHandler;
import javafx.print.JobSettings;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 *
 */
public class Get extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String data = (String) req.getParameter("data");
		if (data == null){
//			resp.getOutputStream().write(this.getServletConfig().getServletContext().getRealPath("/").getBytes());
			return;
		}
		String id = JSONObject.fromObject(data).getString("id");
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy",Locale.ENGLISH);
		String time=format.format(date);
		String tableName = id;
		String[] Family = new String[3];
		Family[0] = "mac";
		Family[1] = "range";
		Family[2] = "time";
		
		if(Dao.isExists(tableName)){
			Dao.clear(tableName);
		}
		else {
			try {
				Dao.create(tableName, Family);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		JSONArray ja = JSONObject.fromObject(data).getJSONArray("data");
		for (int i = 0; i <= ja.size() - 1; i++) {
			JSONObject jo = ja.getJSONObject(i);
			try {
				Dao.put(tableName, jo.get("mac").toString(), "time", "1",time );
				Dao.put(tableName, jo.get("mac").toString(), "range", "1",jo.get("range").toString() );
			} catch (Exception e) {
			}
		}
//		resp.getOutputStream().write(("sucess:"+this.getServletConfig().getServletContext().getRealPath("/")).getBytes());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
