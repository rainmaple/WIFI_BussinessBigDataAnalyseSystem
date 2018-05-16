package dao;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.hadoop.hbase.client.Result;

public class DaoUtil {
	public static String[] getValueByFamily(String tablename,String rowKey,String Family,String[] qualifier){
		String[] result=new String[qualifier.length];
		try {
			Result rs=Dao.getResult(tablename, rowKey);
			int i=0;
			if(rs!=null){
				for(String qua:qualifier){
					if(rs.getValue(Family.getBytes(),qua.getBytes())!=null){
						result[i]=new String(rs.getValue(Family.getBytes(),qua.getBytes()));
					}else {
						result[i]=null;
					}
					i++;
				}
				return result;
			}else{
				return null;
			}
			
		} catch (IOException e) {
			
		}
		return null;
	}
	public static void main(String[] args) {
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy",Locale.ENGLISH);
		String time=format.format(date);
		System.out.println(time);
	}
}
