package handle;

import dao.DaoUtil;

public class ButtonHandler {
	private static String[] Zhudian_TABLE=new String[4];
	static{
		for(int i=1;i<=4;i++){
			Zhudian_TABLE[i-1]="zhudian"+String.valueOf(i);
		}
	}
	public static String[] GetAccNum(int length){
		String Num[]=new String[length];
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhudian_TABLE[0],"1","now",qua)[i];
		}
		return Num;
	}
	public static String[] GetLowNum(int length){
		String Num[]=new String[length];
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhudian_TABLE[1],"1","now",qua)[i];
		}
		return Num;
	}
	public static String[] GetCenterNum(int length){
		String Num[]=new String[length];
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhudian_TABLE[2],"1","now",qua)[i];
		}
		return Num;
	}
	public static String[] GetHighNum(int length){
		String Num[]=new String[length];
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhudian_TABLE[3],"1","now",qua)[i];
		}
		return Num;
	}
	public static double[] GetRate(int type) {
		double[] rate=new double[4];
		String[] qua={"1"};
		String[] Family="h_hour,h_day,h_week,h_month".split(",");
		for(int i=0;i<=3;i++){
			String[] d=DaoUtil.getValueByFamily(Zhudian_TABLE[type],"1",Family[i],qua);
			if(d!=null && d[0]!=null){
				rate[i] = Double.valueOf(d[0]);
			}else{
				rate[i]=0;
			}
			
		}
		return rate;
	}
	public static String[] FormatRate(double[] rate) {
		String[] result=new String[rate.length];
		for(int i=0;i<=rate.length-1;i++){
			result[i]=String.format("%.2f", rate[i]);
		}
		return result;
	}
	public static String[] GetRateStatus(double[] r) {
		String[] result=new String[r.length];
		for(int i=0;i<=r.length-1;i++){
			if(r[i]>0.5){
				result[i]="正常";
			}else{
				result[i]="衰退";
			}
		}
		return result;
	}
}
