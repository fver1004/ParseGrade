package parseDUGrade;

import java.util.Timer;
import java.util.TimerTask;

public class ParseDUGrade {
	static String time;
	static Parse parse;
	static FCMNotification fcmNotify;
	static boolean diff = false;
	static String preValue = "", nowValue="";
	
	public static void main(String []args) throws Exception{
		fcmNotify = new FCMNotification();
		parse = new Parse();
		parse.setUserInfo("21249409", "1243omg");
		
		
		time = "시작 시간:"+new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
		System.out.println(time);
		/*시작*/
		preValue = parse.load();
		Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                
                try {
                	//0.크롤링 후 파싱하기
                	nowValue = parse.load();
                	
                	//1. 이전의 데이터와 비교하고 다르다면 fcm 전송
                	if(preValue.equals(nowValue)){
                		//System.out.println("같아!");
                		diff = false;}
                	else{
                		//System.out.println("달라!");
                		//System.out.println(preValue);
                		//System.out.println("-------------------------------");
                		//System.out.println(nowValue);
                		FCMNotification.pushFCMNotification("");
                		preValue = nowValue;}
                	
                }catch(Exception e){e.printStackTrace();timer.cancel();}
                
                time = "최근 갱신시간:"+new java.text.SimpleDateFormat("HHmmss").format(new java.util.Date());
                System.out.println(time + ": " + diff);
            }};
        
        timer.schedule(timerTask, 1000*60, 1000*60*30);//1초뒤 시작, 30분 주기
        /*Timer는 일단 오류날때만 중지됨.*/
        
        
        
	}

}
