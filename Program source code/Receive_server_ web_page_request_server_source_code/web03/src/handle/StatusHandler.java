package handle;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;

import dao.Dao;
import dao.DaoUtil;

public class StatusHandler {
	private static String table_name="wifiopr";
	public static boolean GetStatus(String wifinum){
		String[] qua={wifinum};
		String[] data=DaoUtil.getValueByFamily(table_name, "1", "status", qua);
		if(data==null){
			return false;
		}
		if(data[0]==null){
			return false;
		}
		String status =data[0];
		String dd="";
		Date d2=new Date();
		try {
			ResultScanner rs=Dao.scan("00aabbcc");
			for(Result r:rs){
				dd=new String(r.getValue("time".getBytes(), "1".getBytes()));
				break;
			}
		} catch (IOException e) {
			return false;
		}
		Date d1;
		DateFormat format=new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy",Locale.ENGLISH);
		try {
			d1 = format.parse(dd);
		} catch (ParseException e2) {
			return false;
		}
		if(d1.getTime()-d2.getTime()<=1000*60*5){
			return true;
		}else if(status.equals("on")){
			return false;
		}
		return true;
	}
}
