package handle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dao.DaoUtil;

public class PassFlowHandler {
	private static String TABLE_NAME="keliu";
	public static String[] GetAccPassFlow(int length){
		String[] qua=new String[length];
		for(int i=0;i<=length-1;i++){
			qua[i]=String.valueOf(length-i);
		}
		return DaoUtil.getValueByFamily(TABLE_NAME,"1","now",qua);
	}
	public static String[] GetHourPassFlow(int length){
		String[] Old=new String[length-1];
		String[] New={"1"};
		for(int i=1;i<=length-1;i++){
			Old[i-1]=String.valueOf(length-i);
		}
		String[] Olddata=DaoUtil.getValueByFamily(TABLE_NAME,"1","hour",Old);
		String[] predata=DaoUtil.getValueByFamily(TABLE_NAME+"_pr","1","hour",New);
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
	/**
	 * @param length
	 * @return
	 */
	public static String[] GetDayPassFlow(int length){
		String[] Old=new String[length-1];
		String[] New={"1"};
		for(int i=1;i<=length-1;i++){
			Old[i-1]=String.valueOf(length-i);
		}
		String[] Olddata=DaoUtil.getValueByFamily(TABLE_NAME,"1","day",Old);
		String[] predata=DaoUtil.getValueByFamily(TABLE_NAME+"_pr","1","day",New);
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
	public static String[] GetWeekPassFlow(int length){
		String[] Old=new String[length-1];
		String[] New={"1"};
		for(int i=1;i<=length-1;i++){
			Old[i-1]=String.valueOf(length-i);
		}
		String[] Olddata=DaoUtil.getValueByFamily(TABLE_NAME,"1","week",Old);
		String[] predata=DaoUtil.getValueByFamily(TABLE_NAME+"_pr","1","week",New);
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
	public static String[] GetMonthPassFlow(int length){
		String[] Old=new String[length-1];
		String[] New={"1"};
		for(int i=1;i<=length-1;i++){
			Old[i-1]=String.valueOf(length-i);
		}
		String[] Olddata=DaoUtil.getValueByFamily(TABLE_NAME,"1","month",Old);
		String[] predata=DaoUtil.getValueByFamily(TABLE_NAME+"_pr","1","month",New);
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
	public static double[] GetRate() {
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
	
	public static String[] GetClockNum(){
		String[] qua=new String[24];
		for(int i=0;i<=24-1;i++){
			qua[i]=String.valueOf(24-i);
		}
		String Num[]=DaoUtil.getValueByFamily("day_keliu", "1", "hour", qua);
		return Num;
	}
	public static String[] GetWeekpre() {
		String[] qua={"1","2","3","4","5","6","7"};
		return DaoUtil.getValueByFamily("week_pr", "1", "values", qua);
	}
}
