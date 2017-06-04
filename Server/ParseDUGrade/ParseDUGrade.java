package parseDUGrade;

import java.util.Timer;
import java.util.TimerTask;

public class ParseDUGrade {
	String time;
	Parse parse;
	boolean diff = false;
	String preValue = "", nowValue="";
	Timer timer;
	
	/* data[0] : 기기 token
	 * data[1] : 학번
	 * data[2] : PW
	 */
	void startParseDUGrade(String userDeviceIdKey, String ID, String PW) throws Exception{
		parse = new Parse();
		parse.setUserInfo(ID, PW);
		
		
		//주기적으로 파싱하기 전에 한번 파싱
		preValue = parse.load();
		nowValue = preValue;
		
		timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                
                try {
                	//0.파싱 시작
                	nowValue = parse.load();
                	
                	//1.전 결과값과 다른게 있는지 비교
                	if(preValue.equals(nowValue)){
                		diff = false;}
                	else{
                		//FCM 메세지 전송
                		FCMNotification.pushFCMNotification(userDeviceIdKey);
                		preValue = nowValue;
                		diff = true;
                		
                	}
                	
                }catch(Exception e){e.printStackTrace();timer.cancel();}
                
                System.out.println(Main.date() + " :: PARSING :: " + ID + " :: difference :: " + diff);
                System.out.println("PARSED = " + ID + " - " + time + ": " + diff);
            }};
        
        timer.schedule(timerTask, 1000*60, 1000*60*30);
       
	}
	
	
	void stopParseDUGrade(){
		//타이머 중지 후 .. 객체 삭제되게 하자
		timer.cancel();
	}
	
	String getGrade(){
		return nowValue;
	}

}