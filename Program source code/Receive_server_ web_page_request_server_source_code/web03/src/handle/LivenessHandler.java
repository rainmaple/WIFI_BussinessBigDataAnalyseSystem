package handle;

import dao.DaoUtil;

public class LivenessHandler {
	private static String[] Zhouqi_TABLE=new String[5];
	static{
		for(int i=1;i<=5;i++){
			Zhouqi_TABLE[i-1]="zhouqi"+String.valueOf(i);
		}
	}
	
	public static String[] getNumArray(int length) {
		String Num[]=new String[length];
		String[] qua={"1"};
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhouqi_TABLE[i],"1","now",qua)[0];
		}
		return Num;
	}
	
	public static String[] GetLivenessRate(){
		String[] numStr=getNumArray(4);
		double[] num=new double[4];
		int sun=0;
		for(int i=0;i<=3;i++){
			num[i]=Double.valueOf(numStr[i]);
			sun+=num[i];
		}
		for(int i=0;i<=3;i++){
			numStr[i]=String.format("%.2f", num[i]/sun);
		}
		return numStr;
	}
	public static String[] GetHourHighNum(int length){
		String Num[]=new String[length];
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhouqi_TABLE[0],"1","hour",qua)[i];
		}
		return Num;
	}
	public static String[] GetHourCenterNum(int length){
		String Num[]=new String[length];
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhouqi_TABLE[1],"1","hour",qua)[i];
		}
		return Num;
	}
	public static String[] GetHourLowNum(int length){
		String Num[]=new String[length];
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhouqi_TABLE[2],"1","hour",qua)[i];
		}
		return Num;
	}
	public static String[] GetHourNonNum(int length){
		String Num[]=new String[length];
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhouqi_TABLE[3],"1","hour",qua)[i];
		}
		return Num;
	}
	public static String[] GetDayHighNum(int length){
		String Num[]=new String[length];
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhouqi_TABLE[0],"1","day",qua)[i];
		}
		return Num;
	}
	public static String[] GetDayCenterNum(int length){
		String Num[]=new String[length];
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhouqi_TABLE[1],"1","day",qua)[i];
		}
		return Num;
	}
	public static String[] GetDayLowNum(int length){
		String Num[]=new String[length];
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhouqi_TABLE[2],"1","day",qua)[i];
		}
		return Num;
	}
	public static String[] GetDayNonNum(int length){
		String Num[]=new String[length];
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhouqi_TABLE[3],"1","day",qua)[i];
		}
		return Num;
	}
	public static String[] GetWeekHighNum(int length){
		String Num[]=new String[length];
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhouqi_TABLE[0],"1","week",qua)[i];
		}
		return Num;
	}
	public static String[] GetWeekCenterNum(int length){
		String Num[]=new String[length];
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhouqi_TABLE[1],"1","week",qua)[i];
		}
		return Num;
	}
	public static String[] GetWeekLowNum(int length){
		String Num[]=new String[length];
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhouqi_TABLE[2],"1","week",qua)[i];
		}
		return Num;
	}
	public static String[] GetNonLowNum(int length){
		String Num[]=new String[length];
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhouqi_TABLE[3],"1","week",qua)[i];
		}
		return Num;
	}
	public static double[] GetRate(int type) {
		double[] rate=new double[4];
		String[] qua={"1"};
		String[] Family="h_hour,h_day,h_week,h_month".split(",");
		for(int i=0;i<=3;i++){
			String[] d=DaoUtil.getValueByFamily(Zhouqi_TABLE[type],"1",Family[i],qua);
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
