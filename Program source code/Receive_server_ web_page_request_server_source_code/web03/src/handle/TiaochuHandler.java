package handle;

import dao.DaoUtil;

public class TiaochuHandler {
	private static String[] Zhudian_TABLE=new String[5];
	static{
		for(int i=1;i<=5;i++){
			Zhudian_TABLE[i-1]="zhudian"+String.valueOf(i);
		}
	}
	public static String[] getNumArray(int length) {
		String Num[]=new String[length];
		String[] qua={"1"};
		for(int i=0;i<=length-1;i++){
			Num[i]=DaoUtil.getValueByFamily(Zhudian_TABLE[i],"1","now",qua)[0];
		}
		return Num;
	}
	public static int getHisSun(int index,String type){
		int sun=0;
		String[] qua={String.valueOf(index)};
		for(int i=0;i<=4;i++){
			String[] his=DaoUtil.getValueByFamily(Zhudian_TABLE[i],"1",type,qua);
			if(his!=null && his[0]!=null){
				sun+=Integer.valueOf(his[0]);
			}
		}
		return sun;
	}
	
	protected static String[] RateFormat(String[] rate){
		String[] format=new String[rate.length];
		for(int i=0;i<=rate.length-1;i++){
			if(rate[i]!=null) format[i]=String.format("%.2f", Double.valueOf(rate[i]));
			else{
				format[i]=null;
			}
		}
		return format;
	}
	public static String GetRate(){
		String[] numStr=getNumArray(4);
		double[] num=new double[4];
		double sun=0;
		for(int i=0;i<=3;i++){
			num[i]=Double.valueOf(numStr[i]);
			sun+=num[i];
		}
		return String.valueOf(1.0*num[0]/sun*100);
	}
	public static double[] GetHisRate() {
		double[] rate=new double[4];
		String[] qua={"1"};
		String[] Family="h_hour,h_day,h_week,h_month".split(",");
		for(int i=0;i<=3;i++){
			rate[i] = Double.valueOf(DaoUtil.getValueByFamily(Zhudian_TABLE[0],"1",Family[i],qua)[0]);
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
	
	public static String[] GetHourNum(int length){
		String Num[]=new String[length];
		String[] qua={"6","5","4","3","2","1"};
		String[] HisArr=DaoUtil.getValueByFamily(Zhudian_TABLE[0],"1","hour",qua);
		for(int i=0;i<=length-1;i++){
			if(HisArr[i]!=null){
				Num[i]=String.valueOf(1.0*Integer.valueOf(HisArr[i])/getHisSun(Integer.valueOf(qua[i]),"hour")*100);
			}else{
				Num[i]=null;
			}
		}
		return RateFormat(Num);
	}

	public static String[] GetDayNum(int length){
		String Num[]=new String[length];
		String[] qua={"6","5","4","3","2","1"};
		String[] HisArr=DaoUtil.getValueByFamily(Zhudian_TABLE[0],"1","day",qua);
		for(int i=0;i<=length-1;i++){
			if(HisArr[i]!=null){
				Num[i]=String.valueOf(1.0*Integer.valueOf(HisArr[i])/getHisSun(Integer.valueOf(qua[i]),"day")*100);
			}else{
				Num[i]=null;
			}
		}
		return RateFormat(Num);
	}
	public static String[] GetWeekNum(int length){
		String Num[]=new String[length];
		String[] qua={"4","3","2","1"};
		String[] HisArr=DaoUtil.getValueByFamily(Zhudian_TABLE[0],"1","week",qua);
		for(int i=0;i<=length-1;i++){
			if(HisArr[i]!=null){
				Num[i]=String.valueOf(1.0*Integer.valueOf(HisArr[i])/getHisSun(Integer.valueOf(qua[i]),"week")*100);
			}else{
				Num[i]=null;
			}
		}
		return RateFormat(Num);
	}
	public static String[] GetMonthNum(int length){
		String Num[]=new String[length];
		String[] qua={"4","3","2","1"};
		String[] HisArr=DaoUtil.getValueByFamily(Zhudian_TABLE[0],"1","month",qua);
		for(int i=0;i<=length-1;i++){
			if(HisArr[i]!=null){
				Num[i]=String.valueOf(1.0*Integer.valueOf(HisArr[i])/getHisSun(Integer.valueOf(qua[i]),"month")*100);
			}else{
				Num[i]=null;
			}
		}
		return RateFormat(Num);
		
	}
}
