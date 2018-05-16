package handle;

import dao.DaoUtil;

public class EnterHandler {
	private static String numTABLE_NAME="rudian";
	private static String rateTABLE_NAME="rudr";
	
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
	public static String[] GetAccEnterRate(int length){
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		String[] data=DaoUtil.getValueByFamily(rateTABLE_NAME,"1","now",qua);
		if(data!=null){
			for(int i=0;i<=data.length-1;i++){
				if(data[i]!=null)data[i]=String.valueOf(Double.valueOf(data[i])*100);
			}
		}
		return RateFormat(data);
	}
	
	public static String[] GetHourEnterRate(int length){
		String[] Old=new String[length-1];
		String[] New={"1"};
		for(int i=1;i<=length-1;i++){
			Old[i-1]=String.valueOf(length-i);
		}
		String[] Olddata=DaoUtil.getValueByFamily(rateTABLE_NAME,"1","hour",Old);
		String[] predata=DaoUtil.getValueByFamily(rateTABLE_NAME+"_pr","1","hour",New);
		String[] data=new String[length];
		for(int i=0;i<=length-2;i++){
			data[i]=Olddata[i];
		}
		if(predata!=null){
			data[length-1]=predata[0];
		}else{
			data[length-1]=null;
		}
		if(data!=null){
			for(int i=0;i<=data.length-1;i++){
				if(data[i]!=null )data[i]=String.valueOf(Double.valueOf(data[i])*100);
			}
		}
		return RateFormat(data);
	}
	/**
	 * @param length
	 * @return
	 */
	public static String[] GetDayEnterRate(int length){
		String[] Old=new String[length-1];
		String[] New={"1"};
		for(int i=1;i<=length-1;i++){
			Old[i-1]=String.valueOf(length-i);
		}
		String[] Olddata=DaoUtil.getValueByFamily(rateTABLE_NAME,"1","day",Old);
		String[] data=new String[length];
		String[] predata=DaoUtil.getValueByFamily(rateTABLE_NAME+"_pr","1","day",New);
		for(int i=0;i<=length-2;i++){
			data[i]=Olddata[i];
		}
		if(predata!=null){
			data[length-1]=predata[0];
		}else{
			data[length-1]=null;
		}
		if(data!=null){
			for(int i=0;i<=data.length-1;i++){
				if(data[i]!=null )data[i]=String.valueOf(Double.valueOf(data[i])*100);
			}
		}
		return RateFormat(data);
	}
	public static String[] GetWeekEnterRate(int length){
		String[] Old=new String[length-1];
		String[] New={"1"};
		for(int i=1;i<=length-1;i++){
			Old[i-1]=String.valueOf(length-i);
		}
		String[] Olddata=DaoUtil.getValueByFamily(rateTABLE_NAME,"1","week",Old);
		String[] predata=DaoUtil.getValueByFamily(rateTABLE_NAME+"_pr","1","week",New);
		String[] data=new String[length];
		for(int i=0;i<=length-2;i++){
			data[i]=Olddata[i];
		}
		if(predata!=null){
			data[length-1]=predata[0];
		}else{
			data[length-1]=null;
		}
		if(data!=null){
			for(int i=0;i<=data.length-1;i++){
				if(data[i]!=null )data[i]=String.valueOf(Double.valueOf(data[i])*100);
			}
		}
		return RateFormat(data);
	}
	public static String[] GetMonthEnterRate(int length){
		String[] Old=new String[length-1];
		String[] New={"1"};
		for(int i=1;i<=length-1;i++){
			Old[i-1]=String.valueOf(length-i);
		}
		String[] Olddata=DaoUtil.getValueByFamily(rateTABLE_NAME,"1","month",Old);
		String[] predata=DaoUtil.getValueByFamily(rateTABLE_NAME+"_pr","1","month",New);
		String[] data=new String[length];
		for(int i=0;i<=length-2;i++){
			data[i]=Olddata[i];
		}
		if(predata!=null){
			data[length-1]=predata[0];
		}else{
			data[length-1]=null;
		}
		if(data!=null){
			for(int i=0;i<=data.length-1;i++){
				if(data[i]!=null )data[i]=String.valueOf(Double.valueOf(data[i])*100);
			}
		}
		return RateFormat(data);
	}
	
	public static String[] GetAccEnterNum(int length){
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		return DaoUtil.getValueByFamily(numTABLE_NAME,"1","now",qua);
	}
	public static String[] GetHourEnterNum(int length){
		String[] Old=new String[length-1];
		String[] New={"1"};
		for(int i=1;i<=length-1;i++){
			Old[i-1]=String.valueOf(length-i);
		}
		String[] Olddata=DaoUtil.getValueByFamily(numTABLE_NAME,"1","hour",Old);
		String[] predata=DaoUtil.getValueByFamily(numTABLE_NAME+"_pr","1","hour",New);
		String[] data=new String[length];
		for(int i=0;i<=length-2;i++){
			data[i]=Olddata[i];
		}
		if(predata!=null){
			data[length-1]=predata[0];
		}else{
			data[length-1]=null;
		}
		return data;
	}
	public static String[] GetDayEnterNum(int length){
		String[] Old=new String[length-1];
		String[] New={"1"};
		for(int i=1;i<=length-1;i++){
			Old[i-1]=String.valueOf(length-i);
		}
		String[] Olddata=DaoUtil.getValueByFamily(numTABLE_NAME,"1","day",Old);
		String[] predata=DaoUtil.getValueByFamily(numTABLE_NAME+"_pr","1","day",New);
		String[] data=new String[length];
		for(int i=0;i<=length-2;i++){
			data[i]=Olddata[i];
		}
		if(predata!=null){
			data[length-1]=predata[0];
		}else{
			data[length-1]=null;
		}
		return data;
	}
	public static String[] GetWeekEnterNum(int length){
		String[] Old=new String[length-1];
		String[] New={"1"};
		for(int i=1;i<=length-1;i++){
			Old[i-1]=String.valueOf(length-i);
		}
		String[] Olddata=DaoUtil.getValueByFamily(numTABLE_NAME,"1","week",Old);
		String[] predata=DaoUtil.getValueByFamily(numTABLE_NAME+"_pr","1","week",New);
		String[] data=new String[length];
		for(int i=0;i<=length-2;i++){
			data[i]=Olddata[i];
		}
		if(predata!=null){
			data[length-1]=predata[0];
		}else{
			data[length-1]=null;
		}
		return data;
	}
	public static String[] GetMonthEnterNum(int length){
		String[] Old=new String[length-1];
		String[] New={"1"};
		for(int i=1;i<=length-1;i++){
			Old[i-1]=String.valueOf(length-i);
		}
		String[] Olddata=DaoUtil.getValueByFamily(numTABLE_NAME,"1","month",Old);
		String[] predata=DaoUtil.getValueByFamily(numTABLE_NAME+"_pr","1","month",New);
		String[] data=new String[length];
		for(int i=0;i<=length-2;i++){
			data[i]=Olddata[i];
		}
		if(predata!=null){
			data[length-1]=predata[0];
		}else{
			data[length-1]=null;
		}
		return data;
		
	}
	
	
	
	
	public static double[] GetNumRate() {
		double[] rate=new double[4];
		String[] qua={"1"};
		String[] Family="h_hour,h_day,h_week,h_month".split(",");
		for(int i=0;i<=3;i++){
			rate[i] = Double.valueOf(DaoUtil.getValueByFamily(numTABLE_NAME,"1",Family[i],qua)[0]);
		}
		return rate;
	}
	public static String[] FormatNumRate(double[] rate) {
		String[] result=new String[rate.length];
		for(int i=0;i<=rate.length-1;i++){
			result[i]=String.format("%.2f", rate[i]);
		}
		return result;
	}
	public static String[] GetNumRateStatus(double[] r) {
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
	
	
	
	
	public static double[] GetRateRate() {
		double[] rate=new double[4];
		String[] qua={"1"};
		String[] Family="h_hour,h_day,h_week,h_month".split(",");
		for(int i=0;i<=3;i++){
			rate[i] = Double.valueOf(DaoUtil.getValueByFamily(rateTABLE_NAME,"1",Family[i],qua)[0]);
		}
		return rate;
	}
	public static String[] FormatRateRate(double[] rate) {
		String[] result=new String[rate.length];
		for(int i=0;i<=rate.length-1;i++){
			result[i]=String.format("%.2f", rate[i]);
		}
		return result;
	}
	public static String[] GetRateRateStatus(double[] r) {
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
