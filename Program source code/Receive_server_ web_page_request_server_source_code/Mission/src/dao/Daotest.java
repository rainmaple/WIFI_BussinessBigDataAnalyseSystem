package dao;

import java.io.IOException;

public class Daotest {
	public static void main(String[] args) {
		String table_name="rudian";
		String[] Family="now,hour,day,week,month,h_hour,h_day,h_week,h_month".split(",");
		for(int j=0;j<=Family.length-1;j++){
			for(int i=1;i<=10;i++){
				try {
					Dao.put(table_name, "1", Family[i], String.valueOf(i),String.valueOf(i*5));
				} catch (IOException e) {
					System.out.println("rudiann"+Family[i]+"写入失败");
				}
			}
		}
	}
}
