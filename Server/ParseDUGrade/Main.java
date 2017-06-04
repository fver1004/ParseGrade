package parseDUGrade;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	/*
	 * 1. Main class
	 *    메세지 수신을 받으면 GetData()로 값 전달
	 * 
	 * 2. GetData class
	 *    받은 값(userKey, Option, ID, PW..)으로 옵션별 파싱 시작/중지, 상태값전송, 파싱값전송. 파싱은 각 유저마다 ParseDUGrade에 각각 객체에 쓰레드로 돌림
	 * 
	 * 3. ParseDUGrade class
	 *    받은 ID와 PW, Option 값으로 주기적 파싱 쓰레드 시작(Parse). 변경이 있으면 알림메세지 FCMNotification
	 *    
	 * 4. Parse class
	 *    주어진 ID와 PW 값으로 파싱하고 파싱된 값 리턴.
	 * 
	 * 5. FCMNotification class
	 *    userKey 값을 받고 해당 기기에게 변경되었다는 메세지를 전송.
	 *    
	 */
	public static void main(String []args){
		
		GetData getdata = new GetData();;
        String message;
        byte[] inpData;
		int port = 443;
		
		try{
			ServerSocket server = new ServerSocket();
	        InetSocketAddress SoketAddress = new InetSocketAddress(port);
	        server.bind(SoketAddress);
	        System.out.println(date() + " :: Started");
	        
	        //수신 대기
	    	while(true){
	    	
	        Socket client = server.accept();
	        System.out.println(date() + " :: Client Connected");
	        InputStream reciever = client.getInputStream();
	        OutputStream sender = client.getOutputStream();
	        DataOutputStream dataSender = new DataOutputStream(sender);

	        //메시지 수신
	        inpData = new byte[200];
	        reciever.read(inpData, 0, inpData.length);
	        
	        //수신 메시지 처리. parse 후 처리종류에 따른 return값 전송
	        message = new String(inpData);
	        System.out.println(String.format(date() + " :: Data recieved :: %s", message));
	        String sendMessage = getdata.extract(message);
	        dataSender.writeUTF(sendMessage);
	        
	        client.close();
	        System.out.println(date() + " :: Client closed");
	        
	        }
	    }catch(Throwable e){
	        e.printStackTrace();}
		
	}
	
	static String date(){
		return  new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
	}
	
}
