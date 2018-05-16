package handler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

public class MissionHandler {
	public static String GetId(String data){
		return JSONObject.fromObject(data).getString("id");
	}

	public static void FileWritter(String path, String id,String data) {
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("YYYYMMddHHmmss");
		File file =new File(path+id+format.format(date));
		System.out.println(path+id+format.format(date));
		if(file.exists()==false){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				file.delete();
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileOutputStream fout = null;
		try {
			fout=new FileOutputStream(file);
			BufferedOutputStream bfr=new BufferedOutputStream(fout);
			bfr.write(data.getBytes());
			bfr.flush();
			bfr.close();
		} catch (IOException e) {
		}finally{
			try {
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void FileDeleter(String path, String id) {
		File file=new File(path);
		if(file.isDirectory()){
			File[] filelist=file.listFiles();
			for(File f:filelist){
				if(f.getName().substring(0,id.length()).equals(id)) f.delete();
			}
		}
	}
}
