package handle;

import dao.DaoUtil;

public class NewOldHandler {
	private static String TABLE_NAME="new_old";
	public static String GetRate(){
		String[] qua={"1"};
		String New=DaoUtil.getValueByFamily(TABLE_NAME,"1","new",qua)[0];
		String Old=DaoUtil.getValueByFamily(TABLE_NAME,"1","old",qua)[0];
		return String.format("%.2f",Double.valueOf(New)/(Double.valueOf(New)+Double.valueOf(Old))*100);
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
	
	public static String[] GetNewNum(int length){
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		return DaoUtil.getValueByFamily(TABLE_NAME,"1","new",qua);
	}
	public static String[] GetOldNum(int length){
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		return DaoUtil.getValueByFamily(TABLE_NAME,"1","old",qua);
	}
	public static double[] GetHisRate() {
		double[] rate=new double[4];
		String[] qua={"1"};
		String[] Family="h_hour,h_day,h_week,h_month".split(",");
		for(int i=0;i<=3;i++){
			rate[i] = Double.valueOf(DaoUtil.getValueByFamily(TABLE_NAME,"1",Family[i],qua)[0]);
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
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		String[] data=DaoUtil.getValueByFamily(TABLE_NAME,"1","hour",qua);
		if(data!=null){
			for(int i=0;i<=data.length-1;i++){
				if(data[i]!=null)data[i]=String.valueOf(Double.valueOf(data[i])*100);
			}
		}
		return RateFormat(data);
	}

	public static String[] GetDayNum(int length){
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		String[] data=DaoUtil.getValueByFamily(TABLE_NAME,"1","day",qua);
		if(data!=null){
			for(int i=0;i<=data.length-1;i++){
				if(data[i]!=null)data[i]=String.valueOf(Double.valueOf(data[i])*100);
			}
		}
		return RateFormat(data);
	}
	public static String[] GetWeekNum(int length){
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		String[] data=DaoUtil.getValueByFamily(TABLE_NAME,"1","week",qua);
		if(data!=null){
			for(int i=0;i<=data.length-1;i++){
				if(data[i]!=null)data[i]=String.valueOf(Double.valueOf(data[i])*100);
			}
		}
		return RateFormat(data);
	}
	public static String[] GetMonthNum(int length){
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		String[] data=DaoUtil.getValueByFamily(TABLE_NAME,"1","month",qua);
		if(data!=null){
			for(int i=0;i<=data.length-1;i++){
				if(data[i]!=null)data[i]=String.valueOf(Double.valueOf(data[i])*100);
			}
		}
		return RateFormat(data);
	}
}
