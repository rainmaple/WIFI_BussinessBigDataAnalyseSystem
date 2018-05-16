package util;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class SmsUtil {
	public static void SendSms(String Uid,String Key,String smsMob,String smsText) throws HttpException, IOException {
		Util.httprequestpost("http://utf8.sms.webchinese.cn/?Uid="+Uid+"&Key="+Key+"&smsMob="+smsMob+"&smsText="+smsText, "");
		System.out.println(smsMob+" "+"发送成功");
	}
}
