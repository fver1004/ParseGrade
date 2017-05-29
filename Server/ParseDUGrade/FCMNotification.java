package parseDUGrade;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;


public class FCMNotification {
	public final static String AUTH_KEY_FCM = "Key";
	public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
	
	// userDeviceIdKey is the device id you will query from your database
	public static void pushFCMNotification(String userDeviceIdKey) throws Exception{

	   String authKey = AUTH_KEY_FCM; // You FCM AUTH key
	   String FMCurl = API_URL_FCM; 

	   URL url = new URL(FMCurl);
	   HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	   conn.setUseCaches(false);
	   conn.setDoInput(true);
	   conn.setDoOutput(true);

	   conn.setRequestMethod("POST");
	   conn.setRequestProperty("Authorization","key="+authKey);
	   conn.setRequestProperty("Content-Type","application/json");

	   JSONObject json = new JSONObject();
	   json.put("to","/topics/news");
	   JSONObject info = new JSONObject();
	   info.put("title", "WARNING!!!"); // Notification title
	   info.put("body", "ANGANGANG"); // Notification body
	   json.put("notification", info);

	   OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	   wr.write(json.toString());
	   wr.flush();
	   conn.getInputStream();
	}
}
