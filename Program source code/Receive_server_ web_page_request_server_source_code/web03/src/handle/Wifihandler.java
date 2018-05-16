package handle;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.Dao;
import dao.DaoUtil;
import util.SmsUtil;

public class Wifihandler {
	static String table_name="wifiopr";
	public static List<String> getWifiStatus(int begin,int num) {
		String[] qua=new String[num];
		for(int i=0;i<=num-1;i++){
			qua[i]=String.valueOf(begin+i);
		}
		List<String> list=new ArrayList<String>();
		String[] status=DaoUtil.getValueByFamily(table_name, "1", "status", qua);
		if(status!=null){
			for(int i=0;i<=status.length-1;i++){
				if(status[i]!=null){
					list.add(status[i]);
				}
			}
		}else{
			return null;
		}
		return list;
	}
	public static boolean WifiChange(String num){
		String[] qua={num};
		String[] data=DaoUtil.getValueByFamily(table_name, "1", "status", qua);
		if(data==null){
			return false;
		}
		if(data[0]==null){
			return false;
		}
		String key="";
		String[] data1=DaoUtil.getValueByFamily("phoneuser", "1", "key", qua);
		if(data1!=null && data1[0]!=null){
			key=data1[0];
		}else{
			return false;
		}
		String uid="";
		data1=DaoUtil.getValueByFamily("phoneuser", "1", "uid", qua);
		if(data1!=null && data1[0]!=null){
			uid=data1[0];
		}else{
			return false;
		}
		String phonenum="";
		data1=DaoUtil.getValueByFamily(table_name, "1", "phonenum", qua);
		if(data1!=null && data1[0]!=null){
			phonenum=data1[0];
		}else{
			return false;
		}
		if(data[0].equals("on")){
			try {
				Dao.put(table_name, "1", "status", num, "off");
				Date date=new Date();
				DateFormat format=new SimpleDateFormat("yyyy MM dd HH mm ss");
				Dao.put(table_name, "1", "lasthandledate", num, format.format(date));
				SmsUtil.SendSms(uid,key, phonenum,"off");
				System.out.println(key+" "+phonenum+"off");
			} catch (Exception e) {
				return false;
			}
		}else{
			try {
				Dao.put(table_name, "1", "status", num, "on");
				Date date=new Date();
				DateFormat format=new SimpleDateFormat("yyyy MM dd HH mm ss");
				Dao.put(table_name, "1", "lasthandledate", num, format.format(date));
				SmsUtil.SendSms(uid,key, phonenum,"on");
				System.out.println(key+" "+phonenum+"off");
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean CanOpr(String num){
		String[] qua={num};
		String[] data=DaoUtil.getValueByFamily(table_name, "1", "status", qua);
		if(data==null){
			return false;
		}
		if(data[0]==null){
			return false;
		}
		data=DaoUtil.getValueByFamily(table_name, "1", "lasthandledate", qua);
		if(data !=null && data[0]!=null){
			Date d1=new Date();
			DateFormat format=new SimpleDateFormat("yyyy MM dd HH mm ss");
			try {
				Date d2=format.parse(data[0]);
				if(d1.getTime()-d2.getTime()<=1000*60*5){
					return false;
				}else {
					return true;
				}
			} catch (ParseException e) {
				try {
					Dao.put(table_name, "1", "lasthandledate", num, format.format(d1));
				} catch (Exception e1) {
					return false;
				}
			}
			
		}
		return true;
	}
}
