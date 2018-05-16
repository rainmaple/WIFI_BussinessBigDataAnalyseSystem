package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import net.sf.json.JSONObject;

public class Util {
	public static String httprequestpost(String url,String para){
		String result="";
		try {
			//1.连接url
			URL readurl=new URL(url);
			URLConnection con=readurl.openConnection();
			//2.设置编码为utf-8
            con.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
            //3.将dooutput、doinput设置为true，这样才能够发送post请求 
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStream out=con.getOutputStream();
            //4.发送请求
            para=new String(para.getBytes(), "UTF-8");
            out.write(para.getBytes());
            out.flush();
            out.close();
            //5.接受请求返回数据
            InputStream in =con.getInputStream();
            InputStreamReader inr=new InputStreamReader(in);
            BufferedReader br=new BufferedReader(inr);
            String str="";
            while((str=br.readLine())!=null) {
            	result+=(str+" ");
            }
            in.close();
		} catch (MalformedURLException e) {
			System.out.println("url连接失败");
			return "ERROR";
		} catch (IOException e) {
			System.out.println("io异常");
			e.printStackTrace();
			return "ERROR";
		}
		return result;
	}
	public static void main(String[] args) {
		File file=new File("e:/json.txt");
		String context="";
		try {
			FileReader fr=new FileReader(file);
			BufferedReader br=new BufferedReader(fr);
			String str="";
			while((str=br.readLine())!=null){
				context+=str;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject jo=JSONObject.fromObject(context);
		jo.remove("id");
		jo.put("id", "11aabbcc");
		System.out.println("data="+jo.toString());
		httprequestpost("http://123.207.217.157:8080/Mission/servlet/Get", "data="+jo.toString());
//		for(int i=0;i<=99;i++){
//			String id="";
//			if(i/10==0){
//				id="0"+String.valueOf(i)+"aabbcc";
//			}else {
//				id=String.valueOf(i)+"aabbcc";
//			}
//			jo.remove("id");
//			jo.put("id", id);
//			if(httprequestpost("http://192.168.43.67:8080/Mission/servlet/Get", jo.toString())==true) System.out.println(true);;
//		}
	}
}
