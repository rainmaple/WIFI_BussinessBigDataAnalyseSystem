package handle;

import dao.DaoUtil;

public class VisitingCycleHandler {
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
	public static String[] getRate(){
		String[] numStr=getNumArray(5);
		double[] num=new double[5];
		double sun=0;
		for(int i=0;i<=4;i++){
			num[i]=Double.valueOf(numStr[i]);
			sun+=num[i];
		}
		for(int i=0;i<=4;i++){
			numStr[i]=String.format("%.2f", 1.0*num[i]/sun*100);
		}
		return numStr;
	}
}
